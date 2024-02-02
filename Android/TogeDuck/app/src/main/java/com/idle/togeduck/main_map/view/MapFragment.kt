package com.idle.togeduck.main_map.view

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.drawable.GradientDrawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.idle.togeduck.R
import com.idle.togeduck.databinding.ComponentBottomAppbarBinding
import com.idle.togeduck.databinding.ComponentBottomSheetBinding
import com.idle.togeduck.databinding.FragmentMapBinding
import com.idle.togeduck.util.CalcStatusBarSize.getStatusBarHeightToDp
import com.idle.togeduck.util.DpPxUtil.dpToPx
import com.idle.togeduck.common.ScreenSize.heightPx
import com.idle.togeduck.common.Theme
import com.idle.togeduck.main_map.view.map_rv.MapPagerAdapter
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private var _componentBottomSheetBinding: ComponentBottomSheetBinding? = null
    private val componentBottomSheetBinding get() = _componentBottomSheetBinding!!
    private var _componentBottomAppbarBinding: ComponentBottomAppbarBinding? = null
    private val componentBottomAppbarBinding get() = _componentBottomAppbarBinding!!

    private lateinit var naverMap: NaverMap

    // 최적의 위치를 반환하는 구현체
    private lateinit var locationSource: FusedLocationSource

    // 현재 위치 가져오기 위한 객체
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // OnBackPressedCallback (뒤로가기 기능) 객체 선언
    private lateinit var backPressedCallback: OnBackPressedCallback

    private lateinit var mapPagerAdapter: MapPagerAdapter

    private var prevOffset = 0.0f
    private var halfOffset = 0.5f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        _componentBottomSheetBinding = binding.bsFragment
        _componentBottomAppbarBinding = binding.appbar

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

        initViewPager()
        initChildFragment()
        setPermissionListener()
        initMapView()
        setBottomSheet()
        setUpBackgroundRoundCorner()
        setUpBackgroundButtonIcon()
        setUpBottomText()
        setUpFloatingButton()
    }

    private fun setPermissionListener() {
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
    }

    private fun initViewPager() {
        mapPagerAdapter = MapPagerAdapter(this)
        componentBottomSheetBinding.viewPager.adapter = mapPagerAdapter
        componentBottomSheetBinding.viewPager.isUserInputEnabled = false

        setHalfExpandedPadding()
    }

    private fun initChildFragment() {
        childFragmentManager.beginTransaction()
            .add(R.id.fragment_top_appbar, TopAppbarFragment())
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .commit()
    }
    
    private fun setBottomSheet() {
        val statusDp = getStatusBarHeightToDp(requireContext())

        val sheetBehavior = BottomSheetBehavior.from(componentBottomSheetBinding.bottomSheet)

        sheetBehavior.expandedOffset = dpToPx(statusDp + 5, requireContext())

        // TODO. BottomSheetBehavior state에 따른 이벤트 추후 추가
        sheetBehavior.addBottomSheetCallback(object  : BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        if (halfOffset != sheetBehavior.calculateSlideOffset()) halfOffset = sheetBehavior.calculateSlideOffset()

                        setHalfExpandedPadding()
                        prevOffset =  halfOffset
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        prevOffset = 1.0f
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        prevOffset = 0.0f
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.d("로그", "${halfOffset} / ${slideOffset}")
                if (prevOffset < slideOffset && slideOffset > halfOffset) setExpandedPadding()
            }
        })
    }

    private fun setHalfExpandedPadding() {
        binding.bsFragment.bottomSheet.setPadding(0,0, 0, dpToPx(90, requireContext()) + heightPx / 2)
    }

    private fun setExpandedPadding() {
        binding.bsFragment.bottomSheet.setPadding(0,0, 0, dpToPx(105, requireContext()))
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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
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


    private fun handleButtonClick (
        showFab: LinearLayout,
        hideList: List<LinearLayout>
    ) {
        Log.d("검증", "buttonclick func")
        for (fab in hideList) {
            fab.visibility = View.INVISIBLE
        }
        showFab.visibility = View.VISIBLE
    }

    private fun setUpBackgroundRoundCorner(){
        // Bottom Appbar Background
        val upperRoundCorner = ContextCompat.getDrawable(requireContext(), R.drawable.shape_upper_round_25) as GradientDrawable
        upperRoundCorner.setColor(ContextCompat.getColor(requireContext(), Theme.theme.main100))
        val background: ConstraintLayout = componentBottomAppbarBinding.navBackground
        background.background = upperRoundCorner
    }

    private fun setUpBackgroundButtonIcon(){
        // Normal Buttons : Icons (Main 500)
        val icQuest: ImageView = componentBottomAppbarBinding.icQuest
        val icList: ImageView = componentBottomAppbarBinding.icList
        val icChat: ImageView = componentBottomAppbarBinding.icChat
        val icMyrecord: ImageView = componentBottomAppbarBinding.icMyrecord
        val icons: List<ImageView> = listOf(icQuest, icList, icChat, icMyrecord)
        for (icon in icons){
            icon.setColorFilter(ContextCompat.getColor(requireContext(), Theme.theme.main500))
        }
    }

    private fun setUpBottomText(){
        val textQuest: TextView = componentBottomAppbarBinding.textQuest
        val textList: TextView = componentBottomAppbarBinding.textList
        val textChat: TextView = componentBottomAppbarBinding.textChat
        val textMyRecord: TextView = componentBottomAppbarBinding.textMyrecord
        val texts: List<TextView> = listOf(textQuest, textList, textChat, textMyRecord)
        for (text in texts){
            text.setTextColor(ContextCompat.getColor(requireContext(), Theme.theme.main500))
        }
        textQuest.setText("Quest")
        textList.setText("List")
        textChat.setText("MyQuest")
        textMyRecord.setText("MyRecord")
    }

    private fun setUpFloatingButton(){
        // Floating Buttons : Buttons (Main 500)
        val fabQuest: LinearLayout = componentBottomAppbarBinding.fabQuest
        val fabList: LinearLayout = componentBottomAppbarBinding.fabList
        val fabChat: LinearLayout = componentBottomAppbarBinding.fabChat
        val fabMyrecord: LinearLayout = componentBottomAppbarBinding.fabMyrecord
        val circle = ContextCompat.getDrawable(requireContext(), R.drawable.shape_circle) as GradientDrawable
        circle.setColor(ContextCompat.getColor(requireContext(), Theme.theme.main500))
        circle.setStroke(0,0)

        val fabs: List<LinearLayout> = listOf(fabQuest,fabList,fabChat,fabMyrecord)
        for(fab in fabs){
            fab.background = circle;
        }

        componentBottomAppbarBinding.buttonQuest.setOnClickListener {
            componentBottomSheetBinding.viewPager.setCurrentItem(0, false)
            handleButtonClick(fabQuest, listOf(fabList, fabChat, fabMyrecord))
        }
        componentBottomAppbarBinding.buttonList.setOnClickListener {
            componentBottomSheetBinding.viewPager.setCurrentItem(1, false)
            handleButtonClick(fabList, listOf(fabQuest,fabChat,fabMyrecord))
        }
        componentBottomAppbarBinding.buttonChat.setOnClickListener {
            handleButtonClick(fabChat, listOf(fabQuest, fabList, fabMyrecord))
        }
        componentBottomAppbarBinding.buttonMyrecord.setOnClickListener {
            componentBottomSheetBinding.viewPager.setCurrentItem(3, false)
            handleButtonClick(fabMyrecord, listOf(fabQuest, fabList, fabChat))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _componentBottomAppbarBinding = null
        _componentBottomAppbarBinding = null
        backPressedCallback.remove()
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}