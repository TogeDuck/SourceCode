package com.idle.togeduck

import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.gson.Gson
import com.idle.togeduck.databinding.FragmentMainBinding
import com.idle.togeduck.di.PreferenceModule
import com.idle.togeduck.favorite.FavoriteSettingViewModel
import com.idle.togeduck.favorite.model.Celebrity
import com.idle.togeduck.main_map.MapViewModel
import com.idle.togeduck.network.Coordinate
import com.idle.togeduck.network.Message
import com.idle.togeduck.network.Quest
import com.idle.togeduck.network.StompManager
import com.idle.togeduck.network.StompManagerTest
import com.idle.togeduck.network.WebSocketManager
import com.idle.togeduck.quest.share.ShareViewModel
import com.idle.togeduck.util.GPSWorker
import com.idle.togeduck.util.LoginUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ua.naiksoftware.stomp.dto.StompHeader
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var preference: PreferenceModule

    private val mainViewModel: MainViewModel by activityViewModels()
    private val shareViewModel: ShareViewModel by activityViewModels()
    private val mapViewModel: MapViewModel by activityViewModels()
    private val favoriteSettingViewModel: FavoriteSettingViewModel by activityViewModels()

    lateinit var webSocketManager : WebSocketManager
    lateinit var webSocketManager1 : WebSocketManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initGUID()
        setDate()
        getFavorites()

        webSocketManager = WebSocketManager(preference)
        webSocketManager1 = WebSocketManager(preference)

        // 삭제 예정 ========================================
        binding.btn0.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_mapFragment)
        }

        val stompManager = StompManager()
        val stompManagerTest = StompManagerTest()

        binding.btn1.setOnClickListener {
            Log.d("버튼","김아영 버튼 눌림")

            // Stomp 연결
            val headers = listOf(
                com.idle.togeduck.websocketcustomlibrary.dto.StompHeader("Authorization", "guest")
            )
//            stompManager.connect(headers)

            // 특정 토픽에 대한 구독
            stompManagerTest.subscribeTopic("/sub/chats/1") { message ->
                questToast(message)
                Log.d("웹소켓 1", "Received message: $message")
            }

            stompManagerTest.connect(headers)
        }

        binding.btn2.setOnClickListener {
            Log.d("버튼","이지우 버튼 눌림")
            if(webSocketManager1.getConnectedState()){
                Log.d("버튼","웹소켓 1번 연결됨")
                webSocketManager1.send("/pub/chats/1/message", 1, "안녕하세요")
            }
        }

        binding.btn3.setOnClickListener {
            Log.d("버튼","최지찬 버튼 눌림")
            val destination = "/pub/chats/1/message"
            val payload = "Hello, WebSocket!"
            val headers = listOf(
                com.idle.togeduck.websocketcustomlibrary.dto.StompHeader("Authorization", "guest")
            )
            stompManagerTest.send(destination,1, payload, headers)
        }
        //----------------------------------------------------
    }

    private fun initGUID() {
        CoroutineScope(Dispatchers.IO).launch {
            if (!mainViewModel.isAccessTokenPresent()) {
                val guid = mainViewModel.makeGUID()
                mainViewModel.login("GUEST", guid)
            }
        }
    }

    private fun setDate() {
        mapViewModel.setPickedDate(LocalDate.now(), LocalDate.now())
    }

    private fun getFavorites(){
        CoroutineScope(Dispatchers.IO).launch {
            val result = favoriteSettingViewModel.getFavoriteList()
            handelNavigate(result)
        }
    }

    private fun handelNavigate(result: Boolean){
        if(result){
            CoroutineScope(Dispatchers.Main).launch {
                getBirthdayClosest()
//                findNavController().navigate(R.id.action_mainFragment_to_mapFragment)
            }
        }
        else {
            CoroutineScope(Dispatchers.Main).launch {
//                findNavController().navigate(R.id.action_mainFragment_to_FavoriteSettingFragment)
            }
        }
    }

    private fun getBirthdayClosest(){
        val favoriteIdolList = favoriteSettingViewModel.favoriteIdolList.value
        var closestBirthdayCelebrity: Celebrity? = null
        var minDaysDifference = Int.MAX_VALUE
        val today = LocalDate.now()
        if(favoriteIdolList != null){
            for(celebrity in favoriteIdolList){
                // 달이 이전인 경우 || 달과 일이 이전인 경우
                var year = celebrity.birthday.year
                if(today.month < celebrity.birthday.month || (today.month <= celebrity.birthday.month && today.dayOfMonth < celebrity.birthday.dayOfMonth)){
                    year++
                }
                val celebrityBirthday = java.time.LocalDate.of(
                    year,
                    celebrity.birthday.month,
                    celebrity.birthday.dayOfMonth
                )
                val dayDifference = ChronoUnit.DAYS.between(today, celebrityBirthday)
                if(dayDifference < minDaysDifference){
                    closestBirthdayCelebrity = celebrity
                }
            }
            favoriteSettingViewModel.setSelectedCelebrity(closestBirthdayCelebrity!!)
        }
    }

    // 삭제 예정-----------------------------------------------------
    private fun questToast(message: String) {
        val questDto = Gson().fromJson(message, Message::class.java)
        Toast.makeText(requireContext(), "${questDto.content}이 생성되었습니다", Toast.LENGTH_SHORT)
            .show()
    }

    private fun coor(message: String) {
        val coorDto = Gson().fromJson(message, Coordinate::class.java)
        Log.d("좌표", coorDto.toString())
    }
    // -------------------------------------------------------------------

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}