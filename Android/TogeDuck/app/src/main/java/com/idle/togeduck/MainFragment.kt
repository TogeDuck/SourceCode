package com.idle.togeduck

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.idle.togeduck.databinding.FragmentMainBinding
import com.idle.togeduck.di.PreferenceModule
import com.idle.togeduck.event.EventListViewModel
import com.idle.togeduck.favorite.FavoriteSettingViewModel
import com.idle.togeduck.favorite.model.Celebrity
import com.idle.togeduck.main_map.MapViewModel
import com.idle.togeduck.network.Coordinate
import com.idle.togeduck.network.QuestAlert
import com.idle.togeduck.network.StompManager
import com.idle.togeduck.network.WebSocketDataResponse
import com.idle.togeduck.network.WebSocketResponse
import com.idle.togeduck.quest.exchange.ExchangeViewModel
import com.idle.togeduck.quest.recruit.RecruitViewModel
import com.idle.togeduck.quest.share.ShareViewModel
import com.idle.togeduck.util.SnackBarFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject
enum class MessageKind{
    LOCATION, TOURLEAVE, CHAT, QUESTALERT
}

enum class QuestType{
    SHARE,EXCHANGE,GROUP
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
        // 웹소켓 최초 연결
        stompManager.connect()
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
        }

        binding.btn2.setOnClickListener {
        }

        binding.btn3.setOnClickListener {
        }
        //----------------------------------------------------
    }
    /** 웹소켓 수신 메세지 한번에 처리 **/
    private fun socketCallback(message: String){
        val response = Gson().fromJson(message, WebSocketResponse::class.java)
        val websocketDataResponse = Gson().fromJson(response.content, WebSocketDataResponse::class.java)
        if(websocketDataResponse.celebrityId == favoriteSettingViewModel.selectedCelebrity.value?.id){
            when(websocketDataResponse.type){
                // 좌표 수신
                MessageKind.LOCATION.toString() -> {
                    val coordinate = Gson().fromJson(websocketDataResponse.data, Coordinate::class.java)
                    if(mainViewModel.isRealTimeOn && !coordinate.userId.equals(mainViewModel.guid.value)){
                        mapViewModel.updatePeopleMarker(coordinate)
                    }
                }
                // 다른 사람의 투어 종료 수신
                MessageKind.TOURLEAVE.toString() -> {
                    val coordinate = Gson().fromJson(websocketDataResponse.data, Coordinate::class.java)
                    if(mainViewModel.isRealTimeOn && !coordinate.userId.equals(mainViewModel.guid.value)){
                        mapViewModel.deletePeopleMarker(coordinate)
                    }
                }
                MessageKind.QUESTALERT.toString() -> {
                    val questAlert = Gson().fromJson(websocketDataResponse.data, QuestAlert::class.java)
                    mapViewModel.isQuestAlert.value = questAlert
                    if(eventListViewModel.selectedEvent.value?.eventId == questAlert.eventId){
                        when(questAlert.questType){
                            QuestType.SHARE.toString() -> {
                            }
                            QuestType.EXCHANGE.toString() -> {
                                exchangeViewModel.needUpdate.value = true
                            }
                            QuestType.GROUP.toString() -> {
                            }
                        }
                    }
                }
                MessageKind.CHAT.toString() -> {

                }
            }
        }
    }

    private fun initGUID() {
        CoroutineScope(Dispatchers.IO).launch {
            if (!mainViewModel.isAccessTokenPresent()) {
                val guid = mainViewModel.makeGUID()
                if(mainViewModel.login("GUEST", guid)){
                    connectSocket()
                }
            }
        }
    }

    private fun connectSocket(){
        stompManager.setHeader(mainViewModel.accessToken.value!!)
        stompManager.connect()
        stompManager.subscribeChat(1){
                message -> socketCallback(message)
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