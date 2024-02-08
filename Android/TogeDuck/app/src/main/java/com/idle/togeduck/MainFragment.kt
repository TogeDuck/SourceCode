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
import com.idle.togeduck.quest.share.ShareViewModel
import com.idle.togeduck.util.GPSWorker
import com.idle.togeduck.util.LoginUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.toKotlinLocalDate
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
    @Inject
    lateinit var stompManager: StompManager

    private val mainViewModel: MainViewModel by activityViewModels()
    private val shareViewModel: ShareViewModel by activityViewModels()
    private val mapViewModel: MapViewModel by activityViewModels()
    private val favoriteSettingViewModel: FavoriteSettingViewModel by activityViewModels()

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
        // 웹소켓 최초 연결
        stompManager.connect()
        // 초기 정보 설정
        initGUID()
        setDate()
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

    private fun initGUID() {
        CoroutineScope(Dispatchers.IO).launch {
            if (!mainViewModel.isAccessTokenPresent()) {
                val guid = mainViewModel.makeGUID()
                mainViewModel.login("GUEST", guid)
            }
        }
    }

    private fun setDate() {
        val today = java.time.LocalDate.now()
        val sixMonthsAgo = today.minusMonths(6)
        val sixMonthsLater = today.plusMonths(6)
        mapViewModel.setPickedDate(sixMonthsAgo, sixMonthsLater)
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

    override fun onDestroyView() {
        stompManager.disconnect()
        super.onDestroyView()
        _binding = null
    }
}