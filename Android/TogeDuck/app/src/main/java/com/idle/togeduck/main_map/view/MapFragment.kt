package com.idle.togeduck.main_map.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.location.Location
import android.os.Build
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
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.gson.Gson
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermissionUtil
import com.gun0912.tedpermission.normal.TedPermission
import com.idle.togeduck.R
import com.idle.togeduck.databinding.ComponentBottomAppbarBinding
import com.idle.togeduck.databinding.ComponentBottomSheetBinding
import com.idle.togeduck.databinding.FragmentMapBinding
import com.idle.togeduck.util.CalcStatusBarSize.getStatusBarHeightToDp
import com.idle.togeduck.util.DpPxUtil.dpToPx
import com.idle.togeduck.common.ScreenSize.heightPx
import com.idle.togeduck.common.Theme
import com.idle.togeduck.di.PreferenceModule
import com.idle.togeduck.event.EventListViewModel
import com.idle.togeduck.favorite.FavoriteSettingViewModel
import com.idle.togeduck.main_map.MapViewModel
import com.idle.togeduck.main_map.view.map_rv.MapPagerAdapter
import com.idle.togeduck.network.Coordinate
import com.idle.togeduck.network.Message
import com.idle.togeduck.network.StompManager
import com.idle.togeduck.util.GPSWorker
import com.idle.togeduck.util.NaverItem
import com.idle.togeduck.util.builder
import com.idle.togeduck.util.getColor
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.CircleOverlay
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate
import ted.gun0912.clustering.clustering.algo.NonHierarchicalViewBasedAlgorithm
import ted.gun0912.clustering.naver.TedNaverClustering
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit
import javax.inject.Inject

enum class EventKind {
    PAST, TODAY, LATER
}

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private var _componentBottomSheetBinding: ComponentBottomSheetBinding? = null
    private val componentBottomSheetBinding get() = _componentBottomSheetBinding!!
    private var _componentBottomAppbarBinding: ComponentBottomAppbarBinding? = null
    private val componentBottomAppbarBinding get() = _componentBottomAppbarBinding!!

    private val mapViewModel: MapViewModel by activityViewModels()
    private val eventListViewModel: EventListViewModel by activityViewModels()
    private val favoriteSettingViewModel: FavoriteSettingViewModel by activityViewModels()

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

    private var clustering: TedNaverClustering<NaverItem>? = null
    private var todayClustering: TedNaverClustering<NaverItem>? = null
    private var upcomingClustering: TedNaverClustering<NaverItem>? = null
    private var pastClustering: TedNaverClustering<NaverItem>? = null

    private var peopleClustering : TedNaverClustering<NaverItem>? = null

    private lateinit var sheetBehavior: BottomSheetBehavior<FrameLayout>

    private val timer = Timer()
    private lateinit var workRequest: PeriodicWorkRequest
    private var workManager: WorkManager? = null

    lateinit var realTimeOnOffBtn :MaterialSwitch
    lateinit var tourStartBtn :FragmentContainerView
    val stompManager = StompManager()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        _componentBottomSheetBinding = binding.bsFragment
        _componentBottomAppbarBinding = binding.appbar

        addBackPressedCallback()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        realTimeOnOffBtn = binding.realTimeBtn
        tourStartBtn = binding.fragmentTsqp
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        initViewPager()
        initChildFragment()
        setPermissionListener()
        initMapView()
        setBottomSheet()
        setUpBackgroundRoundCorner()
        setUpBackgroundButtonIcon()
        setUpBottomText()
        setUpFloatingButton()
        setRealTimeContainer()
        stompManager.connect()

        realTimeOnOffBtn.setOnClickListener{
            Log.d("실시간 버튼","시작버튼 눌림")
            realTimeBtnOnClick()
        }
        tourStartBtn.setOnClickListener{
            Log.d("투어시작 버튼","시작버튼 눌림")
            tourStartBtnClick()
        }

//        mapViewModel.markerList.observe(viewLifecycleOwner) {
//            clustering?.addItems(it)
//        }

        eventListViewModel.listToday.observe(viewLifecycleOwner) { updatedMarkerList ->
            todayClustering?.clearItems()
            todayClustering?.addItems(updatedMarkerList.map { it -> NaverItem(it.latitude, it.longitude) })
            Log.d("이벤트 리스트 변경","오늘")
        }
        eventListViewModel.listUpcoming.observe(viewLifecycleOwner) { updatedMarkerList ->
            upcomingClustering?.clearItems()
            upcomingClustering?.addItems(updatedMarkerList.map { it -> NaverItem(it.latitude, it.longitude) })
            Log.d("이벤트 리스트 변경","미래")
        }
        eventListViewModel.listPast.observe(viewLifecycleOwner) { updatedMarkerList ->
            pastClustering?.clearItems()
            pastClustering?.addItems(updatedMarkerList.map { it -> NaverItem(it.latitude, it.longitude) })
            Log.d("이벤트 리스트 변경","과거")
        }
        mapViewModel.peopleMarkerList.observe(viewLifecycleOwner) {
            updatedMarkerList ->
            val valuesCollection = updatedMarkerList.values
            peopleClustering?.clearItems()
            peopleClustering?.addItems(valuesCollection)
        }
    }
    private fun toast(message: String) {
        val questDto = Gson().fromJson(message, Message::class.java)
        Toast.makeText(requireContext(), "${questDto.content}이 생성되었습니다", Toast.LENGTH_SHORT)
            .show()
    }

    private fun realTimeBtnOnClick(){
        if(realTimeOnOffBtn.isChecked){
            stompManager.subscribeTopic("/sub/chats/1"){ message ->
                toast(message)
                Log.d("웹소켓 1", "Received message: $message")
            }
        }
        else{
            realTimeOnOffBtn.isChecked = false
            stompManager.unsubscribeTopic("/sub/chats/1")
        }
    }
    private fun tourStartBtnClick(){
        sendPosition()
    }

    private fun addBackPressedCallback() {
        // OnBackPressedCallback (익명 클래스) 객체 생성
        backPressedCallback = object : OnBackPressedCallback(true) {
            var backWait: Long = 0

            // 뒤로가기 했을 때 실행되는 기능
            override fun handleOnBackPressed() {
                when {
                    componentBottomSheetBinding.viewPager.currentItem == 5 -> {
                        changeViewPagerPage(4)
                    }
                    componentBottomSheetBinding.viewPager.currentItem == 2 -> {
                        changeViewPagerPage(1)
                    }
                    sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED -> {
                        sheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                    }
                    sheetBehavior.state == BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                    else -> {
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
            }
        }

        // 액티비티의 BackPressedDispatcher에 여기서 만든 callback 객체를 등록
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )
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

        componentBottomSheetBinding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 ->  {

                    }
                }
            }
        })
    }

    fun changeViewPagerPage(pageIdx: Int) {
        componentBottomSheetBinding.viewPager.setCurrentItem(pageIdx, true)
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

        sheetBehavior = BottomSheetBehavior.from(componentBottomSheetBinding.bottomSheet)

        sheetBehavior.expandedOffset = dpToPx(statusDp + 5, requireContext())

        // TODO. BottomSheetBehavior state에 따른 이벤트 추후 추가
        sheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }

                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        if (halfOffset != sheetBehavior.calculateSlideOffset()) halfOffset =
                            sheetBehavior.calculateSlideOffset()

                        setHalfExpandedPadding()
                        prevOffset = halfOffset
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
                if (prevOffset < slideOffset && slideOffset > halfOffset) setExpandedPadding()
            }
        })
    }

    private fun setHalfExpandedPadding() {
        binding.bsFragment.bottomSheet.setPadding(
            0,
            0,
            0,
            dpToPx(90, requireContext()) + heightPx / 2
        )
    }

    private fun setExpandedPadding() {
        binding.bsFragment.bottomSheet.setPadding(0, 0, 0, dpToPx(105, requireContext()))
    }

    @SuppressLint("ResourceType")
    private fun setRealTimeContainer(){
        val statusBarDp = getStatusBarHeightToDp(requireContext())
        val layoutParamsRealTimeContainer = binding.realTimeContainer.layoutParams as FrameLayout.LayoutParams
        layoutParamsRealTimeContainer.topMargin = dpToPx(90 + statusBarDp, requireContext())
        layoutParamsRealTimeContainer.rightMargin = dpToPx(10, requireContext())

        val squareCircle = ContextCompat.getDrawable(requireContext(),R.drawable.shape_square_circle) as GradientDrawable
        squareCircle.setColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.realTimeTxt.background = squareCircle

        val trackStates = arrayOf(
            intArrayOf(android.R.attr.state_checked),
            intArrayOf(-android.R.attr.state_checked)
        )
        val thumbStates = arrayOf(
            intArrayOf(android.R.attr.state_checked),
            intArrayOf(-android.R.attr.state_checked)
        )

        val trackColors = intArrayOf(
            getColor(requireContext(), Theme.theme.main200),
            getColor(requireContext(), R.color.white)

        )
        val thumbColors = intArrayOf(
            getColor(requireContext(), Theme.theme.main500),
            getColor(requireContext(), R.color.gray_text)
        )

        val trackColorStateList = ColorStateList(trackStates, trackColors)
        val thumbColorStateList = ColorStateList(thumbStates, thumbColors)

        binding.realTimeBtn.trackTintList = trackColorStateList
        binding.realTimeBtn.thumbTintList = thumbColorStateList
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
        uiSettings.setLogoMargin(
            dpToPx(10, requireContext()),
            dpToPx(90 + statusBarDp, requireContext()),
            0,
            0
        )

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
                initClusterTest()
                getEventList()

//                initCluster()
//                mapViewModel.getItems(naverMap, 10)

                // People Cluster + WebSocket
//                initPeopleCluster()
//                webSocketManager.connect()
//                webSocketManager.subscribe("/topic/coors"){
//                    message ->
//                    mapViewModel.updatePeopleMarker(messageToCoordination(message))
//                }
            }
    }
    private fun messageToCoordination(message: String): Coordinate{
        return Gson().fromJson(message, Coordinate::class.java)
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
            layoutParams.topMargin = dpToPx(115 + statusBarDp, requireContext())

            locationBtn.layoutParams = layoutParams
            locationBtn.map = naverMap
        }
    }

    // 이벤트 리스트 가져옴
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getEventList() {
        val today = java.time.LocalDate.now()
        CoroutineScope(Dispatchers.IO).launch {
            val celebrityId = favoriteSettingViewModel.selectedCelebrity.value?.id ?: return@launch
            val (startDate, endDate) = mapViewModel.pickedDate.value ?: return@launch
            if (startDate.isEqual(today) && endDate.isEqual(today)) {
                val sixMonthsAgo = today.minusMonths(6)
                val sixMonthsLater = today.plusMonths(6)
                eventListViewModel.getEventList(2, sixMonthsAgo.toKotlinLocalDate(), sixMonthsLater.toKotlinLocalDate())
            } else {
                eventListViewModel.getEventList(2, startDate.toKotlinLocalDate(), endDate.toKotlinLocalDate())
            }
        }
    }

    // 지도 사용자들 실시간 위치 초기화
    private fun initPeopleCluster(){
        peopleClustering = TedNaverClustering.with<NaverItem>(requireContext(), naverMap)
            .customMarker{
                clusterItem ->
                val circleDrawable = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.shape_circle
                ) as GradientDrawable
                circleDrawable.setColor(getColor(requireContext(), Theme.theme.sub500))
                circleDrawable.setStroke(0,0)

                val markerBitmap = Bitmap.createBitmap(
                    dpToPx(15, requireContext()), // 마커 너비
                    dpToPx(15, requireContext()), // 마커 높이
                    Bitmap.Config.ARGB_8888
                )

                val canvas = Canvas(markerBitmap)
                circleDrawable.setBounds(0, 0, canvas.width, canvas.height)
                circleDrawable.draw(canvas)

                Marker(clusterItem.position).apply {
                    icon = OverlayImage.fromBitmap(markerBitmap)
                }
            }
            .make()
    }


    // 클러스터 관리 메소드
    private fun initCluster() {
        // 마커, 클러스터 주변 원 리스트
        val circleOverlayList = mutableMapOf<LatLng, CircleOverlay>()

//        setPathLine(markerList)

        clustering = TedNaverClustering.with<NaverItem>(requireContext(), naverMap)
//            .items(markerList)
            .customMarker { clusterItem ->
                Marker(clusterItem.position).apply {
                    icon = MarkerIcons.BLACK
                    iconTintColor = getColor(requireContext(), Theme.theme.main500)
                    width = dpToPx(22, requireContext())
                    height = dpToPx(30, requireContext())
                }
            }
            .customCluster {
                val circleDrawable = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.shape_circle
                ) as GradientDrawable
                circleDrawable.setColor(getColor(requireContext(), Theme.theme.main500))
                circleDrawable.setStroke(2, getColor(requireContext(), Theme.theme.main500))

                TextView(requireContext()).apply {
                    background = circleDrawable
                    setTextColor(Color.WHITE)
                    text = "${it.size}"
                    width = dpToPx(40, requireContext())
                    height = dpToPx(40, requireContext())
                    gravity = Gravity.CENTER
                }
            }
            // 마커가 추가되었을 때 이벤트
            .markerAddedListener { clusterItem, _ ->
                if (circleOverlayList[clusterItem.position] == null) {
                    circleOverlayList[clusterItem.position] = CircleOverlay().builder(
                        clusterItem.position,
                        100.0,
                        requireContext(),
                        naverMap
                    )
                } else {
                    circleOverlayList[clusterItem.position]!!.radius = 150.0
                    circleOverlayList[clusterItem.position]!!.map = naverMap
                }
            }
            // 클러스터 정보가 변경되었을 때 이벤트
            .clusterAddedListener { cluster, tedNaverMarker ->
                val radius = cluster.size * 100.0

                cluster.items.forEach { naverItem ->
                    if (circleOverlayList[naverItem.position] != null) {
                        circleOverlayList[naverItem.position]!!.map = null
                    }
                }

                val closet = cluster.items.closet(tedNaverMarker.marker.position)

                if (circleOverlayList[closet] == null) {
                    circleOverlayList[closet] = CircleOverlay().builder(
                        closet,
                        radius,
                        requireContext(),
                        naverMap
                    )
                } else {
                    circleOverlayList[closet]!!.radius = radius
                    circleOverlayList[closet]!!.map = naverMap
                }

                readjustCluster()
            }
            // 클러스터를 클릭했을 때 이벤트
            .clusterClickListener { cluster ->
                val position = cluster.position

                val cameraPosition = CameraPosition(
                    LatLng(position.latitude, position.longitude),
                    naverMap.cameraPosition.zoom + 2
                )

                naverMap.moveCamera(
                    CameraUpdate
                        .toCameraPosition(cameraPosition)
                        .animate(CameraAnimation.Fly)
                )
            }
            // 마커를 클릭했을 때 이벤트
            .markerClickListener { naverItem ->
                val position = naverItem.position

                naverMap.moveCamera(
                    CameraUpdate.scrollTo(LatLng(position.latitude, position.longitude))
                )
            }
            .make()

        clustering
            ?.setAlgorithm(NonHierarchicalViewBasedAlgorithm(1000, 1000))
    }

    // 가장 가까운 마커의 위치를 반환하는 메소드
    private fun Collection<NaverItem>.closet(pos: LatLng): LatLng {
        var min = Double.MAX_VALUE
        var result: LatLng = this.toList().first().position

        this.forEach { naverItem ->
            val latSquare =
                (naverItem.position.latitude - pos.latitude) * (naverItem.position.latitude - pos.latitude)
            val longSquare =
                (naverItem.position.longitude - pos.longitude) * (naverItem.position.longitude - pos.longitude)
            if (min > latSquare + longSquare) {
                min = latSquare + longSquare
                result = naverItem.position
            }
        }

        return result
    }

    // 카메라 위치를 미세 이동해서 클러스터 위치 재조정
    private fun readjustCluster() {
        val cPos = naverMap.cameraPosition.target

        naverMap.moveCamera(
            CameraUpdate.scrollTo(
                LatLng(cPos.latitude + 0.00000001, cPos.longitude + 0.00000001)
            )
        )

        naverMap.moveCamera(CameraUpdate.scrollTo(cPos))
    }

    // 이동 경로를 경로선으로 표시
    private fun setPathLine(markerList: List<NaverItem>) {
        val pathLine = PathOverlay()
        val pathLineList = mutableListOf<LatLng>()

        for (i in 0 until 5) {
            pathLineList.add(markerList[i].position)
        }

        pathLine.width = 30
        pathLine.outlineWidth = 5
        pathLine.coords = pathLineList
        pathLine.map = naverMap
    }


    private fun handleButtonClick(
        showFab: LinearLayout,
        hideList: List<LinearLayout>,
    ) {
        Log.d("검증", "buttonclick func")
        for (fab in hideList) {
            fab.visibility = View.INVISIBLE
        }
        showFab.visibility = View.VISIBLE
    }

    private fun setUpBackgroundRoundCorner() {
        // Bottom Appbar Background
        val upperRoundCorner = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.shape_upper_round_25
        ) as GradientDrawable
        upperRoundCorner.setColor(ContextCompat.getColor(requireContext(), Theme.theme.main100))
        val background: ConstraintLayout = componentBottomAppbarBinding.navBackground
        background.background = upperRoundCorner
    }

    private fun setUpBackgroundButtonIcon() {
        // Normal Buttons : Icons (Main 500)
        val icQuest: ImageView = componentBottomAppbarBinding.icQuest
        val icList: ImageView = componentBottomAppbarBinding.icList
        val icChat: ImageView = componentBottomAppbarBinding.icChat
        val icMyrecord: ImageView = componentBottomAppbarBinding.icMyrecord
        val icons: List<ImageView> = listOf(icQuest, icList, icChat, icMyrecord)
        for (icon in icons) {
            icon.setColorFilter(ContextCompat.getColor(requireContext(), Theme.theme.main500))
        }
    }

    private fun setUpBottomText() {
        val textQuest: TextView = componentBottomAppbarBinding.textQuest
        val textList: TextView = componentBottomAppbarBinding.textList
        val textChat: TextView = componentBottomAppbarBinding.textChat
        val textMyRecord: TextView = componentBottomAppbarBinding.textMyrecord
        val texts: List<TextView> = listOf(textQuest, textList, textChat, textMyRecord)
        for (text in texts) {
            text.setTextColor(ContextCompat.getColor(requireContext(), Theme.theme.main500))
        }
        textQuest.text = "Quest"
        textList.text = "List"
        textChat.text = "MyQuest"
        textMyRecord.text = "History"
    }

    private fun setUpFloatingButton() {
        // Floating Buttons : Buttons (Main 500)
        val fabQuest: LinearLayout = componentBottomAppbarBinding.fabQuest
        val fabList: LinearLayout = componentBottomAppbarBinding.fabList
        val fabChat: LinearLayout = componentBottomAppbarBinding.fabChat
        val fabMyrecord: LinearLayout = componentBottomAppbarBinding.fabMyrecord
        val circle =
            ContextCompat.getDrawable(requireContext(), R.drawable.shape_circle) as GradientDrawable
        circle.setColor(ContextCompat.getColor(requireContext(), Theme.theme.main500))
        circle.setStroke(0, 0)

        val fabs: List<LinearLayout> = listOf(fabQuest, fabList, fabChat, fabMyrecord)
        for (fab in fabs) {
            fab.background = circle;
        }

        componentBottomAppbarBinding.buttonQuest.setOnClickListener {
            componentBottomSheetBinding.viewPager.setCurrentItem(0, false)
            handleButtonClick(fabQuest, listOf(fabList, fabChat, fabMyrecord))
        }
        componentBottomAppbarBinding.buttonList.setOnClickListener {
            componentBottomSheetBinding.viewPager.setCurrentItem(1, false)
            handleButtonClick(fabList, listOf(fabQuest, fabChat, fabMyrecord))
        }
        componentBottomAppbarBinding.buttonChat.setOnClickListener {
            componentBottomSheetBinding.viewPager.setCurrentItem(3, false)
            handleButtonClick(fabChat, listOf(fabQuest, fabList, fabMyrecord))
        }
        componentBottomAppbarBinding.buttonMyrecord.setOnClickListener {
            componentBottomSheetBinding.viewPager.setCurrentItem(4, false)
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



    // 클러스터 생성 -----------------------------------------------------------------------------------------------
    private fun initClusterTest() {
        pastClustering = TedNaverClustering.with<NaverItem>(requireContext(), naverMap).apply {
            setupCustomMarker(EventKind.PAST)
            setupCustomCluster(EventKind.PAST)
//            setupMarkerAddedListener(circleOverlayList)
//            setupClusterAddedListener(circleOverlayList)
            setupClusterClickListener()
            setupMarkerClickListener()
        }.make()
        pastClustering?.setAlgorithm(NonHierarchicalViewBasedAlgorithm(1000, 1000))

        todayClustering = TedNaverClustering.with<NaverItem>(requireContext(), naverMap).apply {
            setupCustomMarker(EventKind.TODAY)
            setupCustomCluster(EventKind.TODAY)
//            setupMarkerAddedListener(circleOverlayList)
//            setupClusterAddedListener(circleOverlayList)
            setupClusterClickListener()
            setupMarkerClickListener()
        }.make()
        todayClustering?.setAlgorithm(NonHierarchicalViewBasedAlgorithm(1000, 1000))

        upcomingClustering = TedNaverClustering.with<NaverItem>(requireContext(), naverMap).apply {
            setupCustomMarker(EventKind.LATER)
            setupCustomCluster(EventKind.LATER)
//            setupMarkerAddedListener(circleOverlayList)
//            setupClusterAddedListener(circleOverlayList)
            setupClusterClickListener()
            setupMarkerClickListener()
        }.make()
        upcomingClustering?.setAlgorithm(NonHierarchicalViewBasedAlgorithm(1000, 1000))
    }

    private fun getClusterColor(kind: EventKind): Int{
        return when(kind){
            EventKind.PAST -> getColor(requireContext(), R.color.gray_text)
            EventKind.TODAY -> getColor(requireContext(), Theme.theme.main500)
            EventKind.LATER -> getColor(requireContext(), Theme.theme.main300)
        }
    }

    private fun TedNaverClustering.Builder<NaverItem>.setupCustomMarker(kind: EventKind) = apply {
        customMarker { clusterItem ->
            Marker(clusterItem.position).apply {
                icon = MarkerIcons.BLACK
                iconTintColor = getClusterColor(kind)
                width = dpToPx(22, requireContext())
                height = dpToPx(30, requireContext())
                // 이 `apply` 블록의 결과 (즉, Marker 객체 자체)가 반환됩니다.
            }
        }
    }

    private fun TedNaverClustering.Builder<NaverItem>.setupCustomCluster(kind: EventKind) = apply {
        customCluster {
            val circleDrawable = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.shape_circle
            ) as GradientDrawable
            circleDrawable.setColor(getClusterColor(kind))
            circleDrawable.setStroke(2, getClusterColor(kind))

            TextView(requireContext()).apply {
                background = circleDrawable
                setTextColor(Color.WHITE)
                text = "${it.size}"
                width = dpToPx(40, requireContext())
                height = dpToPx(40, requireContext())
                gravity = Gravity.CENTER
            }
        }
    }

    private fun TedNaverClustering.Builder<NaverItem>.setupMarkerAddedListener(circleOverlayList: MutableMap<LatLng, CircleOverlay>) = apply {
        markerAddedListener { clusterItem, _ ->
            if (circleOverlayList[clusterItem.position] == null) {
                circleOverlayList[clusterItem.position] = CircleOverlay().builder(
                    clusterItem.position,
                    100.0,
                    requireContext(),
                    naverMap
                )
            } else {
                circleOverlayList[clusterItem.position]!!.radius = 150.0
                circleOverlayList[clusterItem.position]!!.map = naverMap
            }
        }
    }

    fun TedNaverClustering.Builder<NaverItem>.setupClusterAddedListener(circleOverlayList: MutableMap<LatLng, CircleOverlay>) = apply {
        clusterAddedListener { cluster, tedNaverMarker ->
            val radius = cluster.size * 100.0

            cluster.items.forEach { naverItem ->
                if (circleOverlayList[naverItem.position] != null) {
                    circleOverlayList[naverItem.position]!!.map = null
                }
            }

            val closet = cluster.items.closet(tedNaverMarker.marker.position)

            if (circleOverlayList[closet] == null) {
                circleOverlayList[closet] = CircleOverlay().builder(
                    closet,
                    radius,
                    requireContext(),
                    naverMap
                )
            } else {
                circleOverlayList[closet]!!.radius = radius
                circleOverlayList[closet]!!.map = naverMap
            }

            readjustCluster()
        }
    }

    fun TedNaverClustering.Builder<NaverItem>.setupClusterClickListener() = apply {
        clusterClickListener { cluster ->
            val position = cluster.position

            val cameraPosition = CameraPosition(
                LatLng(position.latitude, position.longitude),
                naverMap.cameraPosition.zoom + 2
            )

            naverMap.moveCamera(
                CameraUpdate
                    .toCameraPosition(cameraPosition)
                    .animate(CameraAnimation.Fly)
            )
        }
    }

    fun TedNaverClustering.Builder<NaverItem>.setupMarkerClickListener() = apply {
        markerClickListener { naverItem ->
            val position = naverItem.position

            naverMap.moveCamera(
                CameraUpdate.scrollTo(LatLng(position.latitude, position.longitude))
            )
        }
    }
    // ---------------------------------------------------------------------------------------------


    private fun doWorkWithPeriodic() {
        Log.d("로그", "doWorkWithPeriodic() 호출됨")

        // TODO. GPSWorker 클래스에서 위치 기록 저장 및 전송 로직 추가 필요

        workRequest = PeriodicWorkRequestBuilder<GPSWorker>(15, TimeUnit.MINUTES).build()

        workManager = WorkManager.getInstance(requireContext())
        workManager?.enqueueUniquePeriodicWork(
            "doWorkWithPeriodic",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }

    private fun cancelWorkWithPeriodic() {
        workManager?.cancelWorkById(workRequest.id)
    }

    @SuppressLint("MissingPermission")
    private fun sendPosition() {
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (TedPermissionUtil.isGranted(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location: Location? ->
                            Log.d("로그", "lastLocation : ${location?.latitude} ${location?.longitude}")

                            val headers = listOf(
                                com.idle.togeduck.websocketcustomlibrary.dto.StompHeader("Authorization", "guest")
                            )
                            stompManager.send("/pub/chats/1/message",1,"lastLocation : ${location?.latitude} ${location?.longitude}" ,headers)
                        }
                }
            }
        }, 0 ,5000)
    }

    override fun onStart() {
        super.onStart()
        Log.d("로그", "MapFragment - onStart() 호출됨")

        sendPosition()
        if (workManager != null) cancelWorkWithPeriodic()
    }
    
    override fun onStop() {
        super.onStop()
        Log.d("로그", "MapFragment - onStop() 호출됨")
        doWorkWithPeriodic()
        timer.cancel()
    }

}