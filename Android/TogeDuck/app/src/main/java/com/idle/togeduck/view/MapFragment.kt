package com.idle.togeduck.view

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.view.marginBottom
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.idle.togeduck.R
import com.idle.togeduck.databinding.FragmentMapBinding
import com.idle.togeduck.util.CalcStatusBarSize.getStatusBarHeightToDp
import com.idle.togeduck.util.CalcStatusBarSize.getStatusBarHeightToPx
import com.idle.togeduck.util.DpPxUtil.dpToPx
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.widget.ZoomControlView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var naverMap: NaverMap

    // 최적의 위치를 반환하는 구현체
    private lateinit var locationSource: FusedLocationSource

    // 현재 위치 가져오기 위한 객체
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // OnBackPressedCallback (뒤로가기 기능) 객체 선언
    private lateinit var backPressedCallback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        // OnBackPressedCallback (익명 클래스) 객체 생성
        backPressedCallback = object : OnBackPressedCallback(true) {
            // 뒤로가기 했을 때 실행되는 기능
            var backWait: Long = 0
            override fun handleOnBackPressed() {
                if (System.currentTimeMillis() - backWait >= 2000) {
                    backWait = System.currentTimeMillis()
                    Toast.makeText(
                        context, "뒤로가기 버튼을 한번 더 누르면 이전 페이지로 이동합니다",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    findNavController().navigate(R.id.mainFragment)
                }
            }
        }

        // 액티비티의 BackPressedDispatcher에 여기서 만든 callback 객체를 등록
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFragment()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                Log.d("로그", "MainFragment - onPermissionGranted() 호출됨")
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(
                    requireContext(),
                    "권한 거부\n${deniedPermissions.toString()}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        requestPermission(permissionListener)
        initMapView()
    }

    private fun initFragment() {
        childFragmentManager.beginTransaction()
            .add(R.id.fragment_top_appbar, TopAppbarFragment())
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .commit()

        childFragmentManager.beginTransaction()
            .add(R.id.fragment_bottom_sheet, BottomSheetFragment())
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .commit()

        childFragmentManager.beginTransaction()
            .add(R.id.fragment_bottom_appbar, BottomAppbarFragment())
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .commit()
    }

    override fun onMapReady(naverMap: NaverMap) {
        val statusBarDp = getStatusBarHeightToDp(requireContext())

        this.naverMap = naverMap
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
        // 현재 위치 버튼 표시
        naverMap.uiSettings.isLocationButtonEnabled = true

        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, true)
        naverMap.isIndoorEnabled = true

        val uiSettings = naverMap.uiSettings

        uiSettings.logoGravity = Gravity.TOP
        uiSettings.setLogoMargin(dpToPx(10, requireContext()), dpToPx(115 + statusBarDp, requireContext()), 0, 0)

        uiSettings.isZoomControlEnabled = false
        uiSettings.isCompassEnabled = false

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        var currentLocation: Location?

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                currentLocation = location
                // 현재 위치 아이콘 표시
                naverMap.locationOverlay.run {
                    isVisible = true
                    position = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
                }

                // 지도 중심 카메라 이동
                naverMap.moveCamera(
                    CameraUpdate.scrollTo(
                        LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
                    )
                )
            }
    }

    // 권한 설정 알림
    private fun requestPermission(permissionListener: PermissionListener) {
        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setRationaleMessage("위치 정보 제공이 필요한 서비스입니다.")
            .setDeniedMessage("[설정] -> [권한]에서 권한 변경이 가능합니다.")
            .setDeniedCloseButtonText("닫기")
            .setGotoSettingButtonText("설정")
            .setPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .check()
    }

    // 네이버 지도 초기화
    private fun initMapView() {
        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }

        mapFragment.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        mapFragment.getMapAsync {
            val statusBarDp = getStatusBarHeightToDp(requireContext())

            val locationBtn = binding.locationBtn
            val layoutParams = locationBtn.layoutParams as FrameLayout.LayoutParams

            layoutParams.marginStart = dpToPx(8, requireContext())
            layoutParams.topMargin = dpToPx(135 + statusBarDp, requireContext())

            locationBtn.layoutParams = layoutParams
            locationBtn.map = naverMap
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        backPressedCallback.remove()
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}