package com.idle.togeduck

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.idle.togeduck.databinding.FragmentMainBinding
import com.idle.togeduck.di.PreferenceModule
import com.idle.togeduck.event.EventListViewModel
import com.idle.togeduck.favorite.FavoriteSettingViewModel
import com.idle.togeduck.favorite.model.Celebrity
import com.idle.togeduck.main_map.MapViewModel
import com.idle.togeduck.network.Chat
import com.idle.togeduck.network.Coordinate
import com.idle.togeduck.network.QuestAlert
import com.idle.togeduck.network.QuestChat
import com.idle.togeduck.network.StompManager
import com.idle.togeduck.network.WebSocketDataResponse
import com.idle.togeduck.network.WebSocketResponse
import com.idle.togeduck.quest.exchange.ExchangeViewModel
import com.idle.togeduck.quest.recruit.RecruitViewModel
import com.idle.togeduck.quest.share.ShareViewModel
import com.idle.togeduck.quest.talk.TalkViewModel
import com.idle.togeduck.quest.talk.model.Talk
import com.idle.togeduck.util.LoginUtil.guid
import com.idle.togeduck.util.SnackBarFactory
import com.idle.togeduck.websocketcustomlibrary.dto.LifecycleEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

enum class MessageKind {
    LOCATION, TOURLEAVE, CHAT, QUESTALERT, QUESTCHAT, EXCHANGEREQUEST
}

enum class QuestType {
    SHARE, EXCHANGE, GROUP
}

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var preference: PreferenceModule

    @Inject
    lateinit var stompManager: StompManager

    private val mainViewModel: MainViewModel by activityViewModels()
    private val shareViewModel: ShareViewModel by activityViewModels()
    private val exchangeViewModel: ExchangeViewModel by activityViewModels()
    private val recruitViewModel: RecruitViewModel by activityViewModels()
    private val mapViewModel: MapViewModel by activityViewModels()
    private val favoriteSettingViewModel: FavoriteSettingViewModel by activityViewModels()
    private val eventListViewModel: EventListViewModel by activityViewModels()
    private val talkViewModel: TalkViewModel by activityViewModels()


    private lateinit var backPressedCallback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        addBackPressedCallback()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            setPermissionListener()
        }

        // 초기 정보 설정
        initGUID()
        setDate()
        loadSelectedCelebrity()
        getFavorites()

        // 삭제 예정 ========================================
        binding.btn0.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_mapFragment)
        }

        binding.btn1.setOnClickListener {
            Log.d("최애", favoriteSettingViewModel.selectedCelebrity.value.toString())
        }

        binding.btn2.setOnClickListener {
        }

        binding.btn3.setOnClickListener {
        }
        //----------------------------------------------------
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestPermission(permissionListener: PermissionListener) {
        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setRationaleMessage("알림 설정이 필요한 서비스입니다.")
            .setDeniedMessage("[설정] -> [권한]에서 권한 변경이 가능합니다.")
            .setDeniedCloseButtonText("닫기")
            .setGotoSettingButtonText("설정")
            .setPermissions(
                Manifest.permission.POST_NOTIFICATIONS
            )
            .check()
    }

    private fun connectSocket() {
        lifecycleScope.launch {
            mainViewModel.accessToken?.let { token ->
                stompManager.setHeader(token)
                stompManager.connect()
                stompManager.subscribeChat(1) { message ->
                    socketCallback(message)
                }
                stompManager.subscribeTopic("/user/sub/errors") { message ->
                    Log.d("웹소켓 에러 코드", message)
                }
            } ?: Log.d("MainFragment", "AccessToken is null")
        }
    }

    @SuppressLint("CheckResult")
    private fun observeConnection() {
        stompManager.stompClient.lifecycle().subscribe { event ->
            when (event.type) {
                LifecycleEvent.Type.CLOSED, LifecycleEvent.Type.ERROR -> {
                    Log.d("MainFragment", "Connection lost. Attempting to reconnect...")
                    connectSocketWithDelay()
                }

                else -> { /* 다른 이벤트 처리 */
                }
            }
        }
    }

    private fun connectSocketWithDelay() {
        lifecycleScope.launch {
            delay(1000)  // 연결 재시도 전 지연 시간
            connectSocket()
        }
    }

    /** 웹소켓 수신 메세지 한번에 처리 **/
    private fun socketCallback(message: String) {
        val response = Gson().fromJson(message, WebSocketResponse::class.java)
        val websocketDataResponse =
            Gson().fromJson(response.content, WebSocketDataResponse::class.java)
        if (websocketDataResponse.celebrityId == favoriteSettingViewModel.selectedCelebrity.value?.id) {
            when (websocketDataResponse.type) {
                // 좌표 수신
                MessageKind.LOCATION.toString() -> {
                    val coordinate =
                        Gson().fromJson(websocketDataResponse.data, Coordinate::class.java)
                    if (mainViewModel.isRealTimeOn && !coordinate.userId.equals(mainViewModel.guid)) {
                        mapViewModel.updatePeopleMarker(coordinate)
                    }
                }
                // 다른 사람의 투어 종료 수신
                MessageKind.TOURLEAVE.toString() -> {
                    val coordinate =
                        Gson().fromJson(websocketDataResponse.data, Coordinate::class.java)
                    if (mainViewModel.isRealTimeOn && !coordinate.userId.equals(mainViewModel.guid)) {
                        mapViewModel.deletePeopleMarker(coordinate)
                    }
                }

                MessageKind.QUESTALERT.toString() -> {
                    val questAlert =
                        Gson().fromJson(websocketDataResponse.data, QuestAlert::class.java)
                    mapViewModel.isQuestAlert.value = questAlert
                    if (eventListViewModel.selectedEvent.value?.eventId == questAlert.eventId) {
                        when (questAlert.questType) {
                            QuestType.SHARE.toString() -> {
                                shareViewModel.needUpdate.value = true
                            }

                            QuestType.EXCHANGE.toString() -> {
                                exchangeViewModel.needUpdate.value = true
                            }

                            QuestType.GROUP.toString() -> {
                                recruitViewModel.needUpdate.value = true
                            }
                        }
                    }
                }

                MessageKind.CHAT.toString() -> {
                    val chat = Gson().fromJson(websocketDataResponse.data, Chat::class.java)
                    if (talkViewModel.chatRoomList.value?.containsKey(chat.chatId) == true) {
                        talkViewModel.addTalkRoomTalk(
                            chat.chatId,
                            Talk(
                                chatId = chat.chatId,
                                userId = chat.userId,
                                content = chat.message,
                                isMine = chat.userId == mainViewModel.guid
                            )
                        )
                    }
                }

                MessageKind.QUESTCHAT.toString() -> {
                    val questChat =
                        Gson().fromJson(websocketDataResponse.data, QuestChat::class.java)
                    if (eventListViewModel.selectedEvent.value?.eventId == questChat.eventId) {
                        val chat = Talk(
                            0,
                            questChat.userId,
                            questChat.message,
                            questChat.userId.equals(mainViewModel.guid)
                        )
                        talkViewModel.addTalk(chat)
                    }
                }

                MessageKind.EXCHANGEREQUEST.toString() -> {
                    val questChat =
                        Gson().fromJson(websocketDataResponse.data, QuestChat::class.java)
                    if (eventListViewModel.selectedEvent.value?.eventId == questChat.eventId) {
                        val chat = Talk(
                            0,
                            questChat.userId,
                            questChat.message,
                            questChat.userId.equals(mainViewModel.guid)
                        )
                        talkViewModel.addTalk(chat)
                    }
                }
            }
        }
    }

    private fun initGUID() {
        mainViewModel.getFromLocalData()
        CoroutineScope(Dispatchers.IO).launch {
            if (mainViewModel.guid == null) {
                mainViewModel.makeGUID()
            }

            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.d("로그", "MainFragment - getFCMToken() 실패")
                    return@OnCompleteListener
                }

                val token = task.result

                CoroutineScope(Dispatchers.IO).launch {
                    Log.d("로그", "MainFragment - initGUID() fcm 토큰 : ${token}")
                    mainViewModel.login("GUEST", token!!)
                    connectSocket()
                    observeConnection()
                }
            })
        }
    }

    private fun setDate() {
        val today = java.time.LocalDate.now()
        val sixMonthsAgo = today.minusMonths(6)
        val sixMonthsLater = today.plusMonths(6)
        mapViewModel.setPickedDate(sixMonthsAgo, sixMonthsLater)
    }

    private fun loadSelectedCelebrity() {
        val savedCelebrity = runBlocking {
            preference.getSelectedCelebrity.first()
        }

        if (savedCelebrity != null) {
            favoriteSettingViewModel.setSelectedCelebrity(savedCelebrity)
        }
    }

    private fun getFavorites() {
        CoroutineScope(Dispatchers.IO).launch {
            val result = favoriteSettingViewModel.getFavoriteList()
            handelNavigate(result)
        }
    }

    private fun handelNavigate(result: Boolean) {
        if (result) {
            CoroutineScope(Dispatchers.Main).launch {
                getBirthdayClosest()
//                findNavController().navigate(R.id.action_mainFragment_to_mapFragment)
            }
        } else {
            CoroutineScope(Dispatchers.Main).launch {
//                findNavController().navigate(R.id.action_mainFragment_to_FavoriteSettingFragment)
            }
        }
    }

    private fun getBirthdayClosest() {
        val favoriteIdolList = favoriteSettingViewModel.favoriteIdolList.value
        var closestBirthdayCelebrity: Celebrity? = null
        var minDaysDifference = Int.MAX_VALUE
        val today = LocalDate.now()
        if (favoriteIdolList != null) {
            for (celebrity in favoriteIdolList) {
                // 달이 이전인 경우 || 달과 일이 이전인 경우
                var year = celebrity.birthday.year
                if (today.month < celebrity.birthday.month || (today.month <= celebrity.birthday.month && today.dayOfMonth < celebrity.birthday.dayOfMonth)) {
                    year++
                }
                val celebrityBirthday = java.time.LocalDate.of(
                    year,
                    celebrity.birthday.month,
                    celebrity.birthday.dayOfMonth
                )
                val dayDifference = ChronoUnit.DAYS.between(today, celebrityBirthday)
                if (dayDifference < minDaysDifference) {
                    closestBirthdayCelebrity = celebrity
                }
            }
            favoriteSettingViewModel.setSelectedCelebrity(closestBirthdayCelebrity!!)
        }
    }

    private fun addBackPressedCallback() {
        // OnBackPressedCallback (익명 클래스) 객체 생성
        backPressedCallback = object : OnBackPressedCallback(true) {
            // 뒤로가기 했을 때 실행되는 기능
            var backWait: Long = 0
            override fun handleOnBackPressed() {
                if (System.currentTimeMillis() - backWait >= 2000) {
                    backWait = System.currentTimeMillis()
                    Toast.makeText(
                        context, "뒤로가기 버튼을 한번 더 누르면 종료됩니다",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    activity?.finish()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        backPressedCallback.remove()
    }
}