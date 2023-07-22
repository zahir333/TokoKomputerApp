package com.example.tokokomputer.ui

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tokokomputer.R
import com.example.tokokomputer.application.ComputerApp
import com.example.tokokomputer.databinding.FragmentSecondBinding
import com.example.tokokomputer.model.Computer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private lateinit var applicationContext: Context
    private val computerViewModel: ComputerViewModel by viewModels {
        ComputerViewModelFactory((applicationContext as ComputerApp).repository)
    }
    private val args : SecondFragmentArgs by navArgs()
    private var computer: Computer? = null
    private lateinit var mMap: GoogleMap
    private var currentLatlang: LatLng? = null
    private  lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onAttach(context: Context) {
        super.onAttach(context)
        applicationContext = requireContext().applicationContext
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        computer = args.computer
        // kita cek jika computer null maka tampilan default nambah toko komputer
        // jika computer tidak null tampilan sedikit berubah ada tombol hapus dan bah
        if (computer != null) {
            binding.deleteButton.visibility = View.VISIBLE
            binding.saveButton.text = "Ubah"
            binding.nameEditText.setText(computer?.name)
            binding.addressEditText.setText(computer?.address)
            binding.telephoneEditText.setText(computer?.telephone)
        }

        //binding google map
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        checkPermission()

        val name = binding.nameEditText.text
        val address = binding.addressEditText.text
        val telephone = binding.telephoneEditText.text
        binding.saveButton.setOnClickListener {
            if (name.isEmpty()) {
                Toast.makeText(context, "Nama tidak bolek kosong", Toast.LENGTH_LONG).show()
            } else if (address.isEmpty()) {
                Toast.makeText(context, "Alamat tidak bolek kosong", Toast.LENGTH_LONG).show()
            } else if (telephone.isEmpty()) {
                Toast.makeText(context, "Telephone tidak bolek kosong", Toast.LENGTH_LONG).show()
            }
            else {
                //kita kasih default dulu buat test berhasil atau tidak
                if ( computer == null ) {
                    val computer = Computer(0, name.toString(), address.toString(), telephone.toString(), currentLatlang?.latitude, currentLatlang?.longitude)
                    computerViewModel.insert(computer)
                } else{
                    val computer = Computer(computer?.id!!, name.toString(), address.toString(), telephone.toString(), currentLatlang?.longitude, currentLatlang?.longitude)
                    computerViewModel.update(computer)
                }
                findNavController().popBackStack()
            }
        }

        binding.deleteButton.setOnClickListener {
            computer?.let { computerViewModel.delete(it) }
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //implement drag marker

        val uiSettings = mMap.uiSettings
        uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerDragListener(this)
    }

    override fun onMarkerDrag(p0: Marker) {}

    override fun onMarkerDragEnd(marker: Marker) {
        val newPosition = marker.position
        currentLatlang = LatLng(newPosition.latitude, newPosition.longitude)
        Toast.makeText(context, currentLatlang.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onMarkerDragStart(p0: Marker) {

    }

    private fun checkPermission() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        if (ContextCompat.checkSelfPermission(
            applicationContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation()
        } else {
            Toast.makeText(applicationContext, "Akses lokasi ditolak", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCurrentLocation() {
        //ngecek jika returen tidak di setujui maka akan berhenti di kondisi if
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        //untuk test current coba run di device langsung atau build apknya terus build di hp masing2
        fusedLocationClient.lastLocation
            .addOnSuccessListener {location ->
                if (location != null) {
                    var latLang = LatLng(location.latitude, location.longitude)
                    currentLatlang = latLang
                    var title = "Marker"
                    //menampilkan lokasi sesuai dengan koordinat yang disimpan dan di update tadi

                    if (computer != null) {
                        title = computer?.name.toString()
                        val newCurrentLocation = LatLng(computer?.latitude!!, computer?.longitude!!)
                        latLang = newCurrentLocation
                    }
                    val markerOptions = MarkerOptions()
                        .position(latLang)
                        .title(title)
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_store_32))
                    mMap.addMarker(markerOptions)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLang, 15f))
                }

            }
    }
}