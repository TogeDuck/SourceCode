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
import android.view.WindowManager
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
import com.idle.togeduck.MainViewModel
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
import com.idle.togeduck.event.model.Event
import com.idle.togeduck.favorite.FavoriteSettingViewModel
import com.idle.togeduck.history.HistoryViewModel
import com.idle.togeduck.history.model.Position
import com.idle.togeduck.main_map.MapViewModel
import com.idle.togeduck.main_map.view.map_rv.MapPagerAdapter
import com.idle.togeduck.network.Coordinate
import com.idle.togeduck.network.CoordinateRequest
import com.idle.togeduck.network.CoordinateResponse
import com.idle.togeduck.network.Message
import com.idle.togeduck.network.StompManager
import com.idle.togeduck.network.TempCoordinateResponse
import com.idle.togeduck.network.toCoordinate
import com.idle.togeduck.util.CalcDistance
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate
import ted.gun0912.clustering.clustering.algo.NonHierarchicalViewBasedAlgorithm
import ted.gun0912.clustering.naver.TedNaverClustering
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.E
import kotlin.math.ln

enum class EventKind {
    PAST, TODAY, LATER
}
enum class MessageKind{
    MESSAGE, JOIN, LEAVE
}

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private var _componentBottomSheetBinding: ComponentBottomSheetBinding? = null
    private val componentBottomSheetBinding get() = _componentBottomSheetBinding!!
    private var _componentBottomAppbarBinding: ComponentBottomAppbarBinding? = null
    private val componentBottomAppbarBinding get() = _componentBottomAppbarBinding!!

    private val mainViewModel: MainViewModel by activityViewModels()
    private val mapViewModel: MapViewModel by activityViewModels()
    private val eventListViewModel: EventListViewModel by activityViewModels()
    private val favoriteSettingViewModel: FavoriteSettingViewModel by activityViewModels()
    private val historyViewModel: HistoryViewModel by activityViewModels()

    @Inject
    lateinit var stompManager: StompManager

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
    private var peopleMarkerOverlay : OverlayImage? = null
    private val peopleMarkers : MutableMap<String, Marker?> = mutableMapOf()
    private var markerSize: Int = 20


    private lateinit var sheetBehavior: BottomSheetBehavior<FrameLayout>

    private var timer: Timer? = null
    private lateinit var workRequest: PeriodicWorkRequest
    private var workManager: WorkManager? = null

    lateinit var realTimeOnOffBtn :MaterialSwitch

    private var pathLine: PathOverlay? = null

    private lateinit var tourStartCircle: GradientDrawable
    private lateinit var tourEndCircle: GradientDrawable

    @Inject
    lateinit var preference: PreferenceModule

    /** Fragment Lifecycle Functions **/
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
        /** 객체 초기화 **/
        super.onViewCreated(view, savedInstanceState)
        realTimeOnOffBtn = binding.realTimeBtn
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        /** 초기화 관련 함수 호출 **/
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
        setTourBtnTheme()

        mainViewModel.accessToken.value?.let { stompManager.setHeader(it) }
        stompManager.connect()
        peopleMarkerOverlay = initPeopleMarkerImage()

        mapViewModel.isTourStart.observe(viewLifecycleOwner) { isTourStart ->
            if (isTourStart) {
                sendPosition()
            } else {
                if (timer != null) {
                    timer!!.cancel()
                    timer = null
                }
            }
        }

        /** 버튼 동작 연결 **/
        realTimeOnOffBtn.setOnClickListener{
            Log.d("실시간 버튼","시작버튼 눌림 ${realTimeOnOffBtn.isChecked}")
            realTimeBtnOnClick()
        }
        binding.tourStart.setOnClickListener{
            changeTourBtn()
        }
        binding.questPlus.setOnClickListener {
            if (binding.plusExchange.visibility == View.GONE) {
                binding.plusExchange.visibility = View.VISIBLE
            } else if (binding.plusExchange.visibility == View.VISIBLE) {
                binding.plusExchange.visibility = View.GONE
            }

            if (binding.plusRecruit.visibility == View.GONE) {
                binding.plusRecruit.visibility = View.VISIBLE
            } else if (binding.plusRecruit.visibility == View.VISIBLE) {
                binding.plusRecruit.visibility = View.GONE
            }

            if (binding.plusShare.visibility == View.GONE) {
                binding.plusShare.visibility = View.VISIBLE
            } else if (binding.plusShare.visibility == View.VISIBLE) {
                binding.plusShare.visibility = View.GONE
            }
        }
        binding.plusExchange.setOnClickListener {
            findNavController().navigate(R.id.action_mapFragment_to_exchangePostDialogFragment)
        }
        binding.plusShare.setOnClickListener {
            findNavController().navigate(R.id.action_mapFragment_to_sharePostDialogFragment)
        }
        binding.plusRecruit.setOnClickListener {
            findNavController().navigate(R.id.action_mapFragment_to_recruitPostDialogFragment)
        }

        /** LiveData Observe **/
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
//        mapViewModel.peopleMarkerList.observe(viewLifecycleOwner) {
//            updatedMarkerList ->
//            val valuesCollection = updatedMarkerList.values
//            peopleMarker.clear()
//            peopleMarker.addAll(valuesCollection)
//        }
        historyViewModel.route.observe(viewLifecycleOwner) { list ->
            if (pathLine != null) pathLine!!.map = null
            setPathLine(list)
        }
        historyViewModel.historyEventList.observe(viewLifecycleOwner) {
            historyViewModel.setMarkerList()
        }
        historyViewModel.markerList.observe(viewLifecycleOwner) { list ->
            list.forEach { it.map = naverMap }
        }
    }

    /** Init Functions **/
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

    /** Theme Settings **/
    private fun setTourBtnTheme() {
        tourStartCircle = ContextCompat.getDrawable(requireContext(), R.drawable.shape_circle) as GradientDrawable
        tourStartCircle.setColor(ContextCompat.getColor(requireContext(), R.color.green))
        tourStartCircle.setStroke(0,0)
        binding.tourStart.background= tourStartCircle

        tourEndCircle = ContextCompat.getDrawable(requireContext(), R.drawable.shape_circle) as GradientDrawable
        tourEndCircle.setColor(ContextCompat.getColor(requireContext(), R.color.red))
        tourEndCircle.setStroke(0,0)

        val plusCircle = ContextCompat.getDrawable(requireContext(), R.drawable.shape_circle) as GradientDrawable
        plusCircle.setColor(ContextCompat.getColor(requireContext(), Theme.theme.main500))
        plusCircle.setStroke(0,0)
        binding.questPlus.background = plusCircle

        val exchangeCircle = ContextCompat.getDrawable(requireContext(), R.drawable.shape_circle) as GradientDrawable
        exchangeCircle.setColor(ContextCompat.getColor(requireContext(), R.color.yellow))
        exchangeCircle.setStroke(0,0)
        binding.plusExchange.background = exchangeCircle
        binding.plusExchange.setColorFilter(getColor(requireContext(), R.color.white))

        val shareCircle = ContextCompat.getDrawable(requireContext(), R.drawable.shape_circle) as GradientDrawable
        shareCircle.setColor(ContextCompat.getColor(requireContext(), R.color.red))
        shareCircle.setStroke(0,0)
        binding.plusShare.background = shareCircle
        binding.plusShare.setColorFilter(getColor(requireContext(), R.color.white))

        val recruitCircle = ContextCompat.getDrawable(requireContext(), R.drawable.shape_circle) as GradientDrawable
        recruitCircle.setColor(ContextCompat.getColor(requireContext(), R.color.green))
        recruitCircle.setStroke(0,0)
        binding.plusRecruit.background = recruitCircle
        binding.plusRecruit.setColorFilter(getColor(requireContext(), R.color.white))
    }

    /** Button Click & Callback Functions **/
    private fun realTimeBtnOnClick(){
        if(realTimeOnOffBtn.isChecked){
            //TODO 추후 메세지 형식에 따라 변경
            stompManager.subscribeCelebrity(favoriteSettingViewModel.selectedCelebrity.value!!.id){
                message -> coordinateMessageCallback(message)
            }
            // 추후 삭제
            stompManager.subscribeChat(1){
                message -> tempCoordinateMessageCallback(message)
            }
        }
        else{
            stompManager.unsubscribeCelebrity(favoriteSettingViewModel.selectedCelebrity.value!!.id)
            stompManager.unsubscribeChat(1)
            deleteAllMarkers()
        }
    }
    private fun coordinateMessageCallback(message: String){
       //TODO 추후 메세지 형식에 따라 변경
        val location = Gson().fromJson(message, CoordinateResponse::class.java).toCoordinate()
//        mapViewModel.updatePeopleMarker(location)
    }
    // 좌표를 json 형태로 채팅방으로 보낼 때 사용하는 메서드 (추후 삭제 가능)
    private fun tempCoordinateMessageCallback(message: String){
        val response = Gson().fromJson(message, TempCoordinateResponse::class.java)
        val coordinateResponse = Gson().fromJson(response.content, CoordinateResponse::class.java)
        if(!coordinateResponse.userId.equals(mainViewModel.guid.value)){
            updatePeopleMarker(coordinateResponse.toCoordinate(), coordinateResponse.type)
        }
    }
    fun updatePeopleMarker(coordinate: Coordinate, type:String){
        when(type){
            MessageKind.MESSAGE.toString() -> {
                peopleMarkers[coordinate.userId]?.map = null
                val marker = Marker()
                marker.position = LatLng(coordinate.latitude, coordinate.longitude)
                marker.icon = peopleMarkerOverlay!!
                marker.alpha = 0.5f
                marker.map = naverMap
                marker.height = markerSize
                marker.width = markerSize
                peopleMarkers[coordinate.userId] = marker
            }
            MessageKind.LEAVE.toString() -> {
                peopleMarkers[coordinate.userId]!!.map = null
                peopleMarkers.remove(coordinate.userId)
            }
        }
    }
    private fun initPeopleMarkerImage(): OverlayImage{
        val circleDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_circle) as GradientDrawable
        circleDrawable.setColor(getColor(requireContext(), Theme.theme.sub500))
        circleDrawable.setStroke(0, 0)

        val markerBitmap = Bitmap.createBitmap(
            dpToPx(30, requireContext()), // 마커 너비
            dpToPx(30, requireContext()), // 마커 높이
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(markerBitmap)
        circleDrawable.setBounds(0, 0, canvas.width, canvas.height)
        circleDrawable.draw(canvas)
        return OverlayImage.fromBitmap(markerBitmap)
    }

    private fun changeTourBtn() {
        if (binding.tourStart.text == "투어\n종료") {
            binding.tourStart.background = tourStartCircle
            binding.tourStart.text = "투어\n시작"
            mapViewModel.setTourStatus(false)
            // 실시간 위치 공유에서 종료 알림
            stompManager.sendChat(1,Gson().toJson(CoordinateRequest(1,0.0, 0.0, mainViewModel.guid.value!!, MessageKind.LEAVE.toString())))
            // 투어 기록 전송
            CoroutineScope(Dispatchers.IO).launch {
                historyViewModel.sendHistory(historyViewModel.historyId.value!!, mapViewModel.tourList.value!!)
            }
            mapViewModel.initTourList()
            deleteAllMarkers()
        } else if (binding.tourStart.text == "투어\n시작") {
            binding.tourStart.background = tourEndCircle
            binding.tourStart.text = "투어\n종료"
            mapViewModel.setTourStatus(true)
            // 기록된 투어 post 요청
            CoroutineScope(Dispatchers.IO).launch {
                historyViewModel.createHistory(favoriteSettingViewModel.selectedCelebrity.value!!.id)
            }
        }
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


    /**  Organization in Progress **/
    private fun toast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
            .show()
    }

    fun changeViewPagerPage(pageIdx: Int) {
        componentBottomSheetBinding.viewPager.setCurrentItem(pageIdx, true)
    }

    private fun updateMarkerSize(zoom: Double){
        getMarkerSize(zoom)
        for ((_, marker) in peopleMarkers) {
            marker?.let {
                it.width = markerSize
                it.height = markerSize
            }
        }
    }
    private fun deleteAllMarkers(){
        for ((_,marker) in peopleMarkers){
            marker?.let {
                it.map = null
            }
        }
        peopleMarkers.clear()
    }
    fun getMarkerSize(zoom: Double) {
        val baseZoomLevel = 17.0
        val baseSize = 30

        // 줌 레벨 변화에 따른 크기 조정
        val sizeChange = (zoom - baseZoomLevel)*7
        var size = baseSize + sizeChange
        size = size.coerceAtMost(50.0).coerceAtLeast(1.0)
        markerSize = dpToPx(size.toInt(), requireContext())
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

        naverMap.addOnCameraChangeListener{ reason, animated ->
            val zoom = naverMap.cameraPosition.zoom
            updateMarkerSize(zoom)
        }

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
            layoutParams.topMargin = dpToPx(115 + statusBarDp, requireContext())

            locationBtn.layoutParams = layoutParams
            locationBtn.map = naverMap
        }
    }

    // 이벤트 리스트 가져옴
    private fun getEventList() {
        CoroutineScope(Dispatchers.IO).launch {
            val celebrityId = favoriteSettingViewModel.selectedCelebrity.value?.id ?: return@launch
            val (startDate, endDate) = mapViewModel.pickedDate.value ?: return@launch
            eventListViewModel.getEventList(197, startDate.toKotlinLocalDate(), endDate.toKotlinLocalDate())
        }
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
    private fun setPathLine(list: List<Position>) {
        pathLine = PathOverlay()
        val pathLineList = mutableListOf<LatLng>()

        list.forEach { (latitude, longitude) ->
            pathLineList.add(LatLng(latitude, longitude))
        }

        if (pathLine != null) {
            pathLine!!.width = 30
            pathLine!!.outlineWidth = 5
            pathLine!!.coords = pathLineList
            pathLine!!.map = naverMap
        }
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
        // [메모]
        // GPSWorker 클래스에서 뷰모델의 데이터에 접근 하려면 어떻게 해야 하나요?
        // 선택된 아이돌 id, 당일 열리는 이벤트 리스트(today event)가 필요할 것 같은데
        // GPSWorker가 받은 좌표를 아니면 MapFragment에서 받을 수 았나?

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
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            val favorite = favoriteSettingViewModel.selectedCelebrity.value?.id
            override fun run() {
                if (TedPermissionUtil.isGranted(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location: Location? ->
                            Log.d("로그", "lastLocation : ${location?.latitude} ${location?.longitude}")
                            if(location!= null){
                                // 좌표 리스트에 등록

                                if(mapViewModel.addTourRecord(location.latitude, location.longitude)){
                                    // 웹소켓 전송 (추후 url, 전송 형식 백엔드에 맞춰 변경)
//                                    stompManager.sendLocation(favoriteSettingViewModel.selectedCelebrity.value!!.id, location.latitude, location.longitude)
                                    stompManager.sendChat(1,Gson().toJson(CoordinateRequest(1,location.latitude, location.longitude, mainViewModel.guid.value!!, MessageKind.MESSAGE.toString())))
                                }
                                else{
//                                    stompManager.sendLocation(favoriteSettingViewModel.selectedCelebrity.value!!.id, location.latitude, location.longitude)
                                    stompManager.sendChat(1,Gson().toJson(CoordinateRequest(1,location.latitude, location.longitude, mainViewModel.guid.value!!, MessageKind.MESSAGE.toString())))
                                }
                                // 이벤트 리스트 확인, 가까운 리스트 갱신
                                figureCloseEvents(location.latitude, location.longitude)
                            }
                        }
                }
            }
        }, 0 , 10 * 1000)
    }

    private fun figureCloseEvents(lat:Double, lng:Double) {
        val eventList = mutableListOf<Event>()
        eventListViewModel.listToday.value?.let { list ->
            for (event in list) {
                if(CalcDistance.idsDistanceOk(lat, lng, event.latitude, event.longitude)){
                   eventList.add(event)
                }
            }
        }
        eventListViewModel.closeEvents.value = eventList
        Log.d("가까운 이벤트 탐색 결과", eventList.toString())
        if(!eventList.isEmpty()){
            toast("이벤트 있음")
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("로그", "MapFragment - onStart() 호출됨")

        if (mapViewModel.isTourStart.value == true && timer == null) sendPosition()
        if (workManager != null) cancelWorkWithPeriodic()
    }
    
    override fun onStop() {
        super.onStop()
        Log.d("로그", "MapFragment - onStop() 호출됨")
        if (mapViewModel.isTourStart.value == true) doWorkWithPeriodic()
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
    }

}