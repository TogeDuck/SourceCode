package com.idle.togeduck

import android.content.Context
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
import androidx.navigation.fragment.findNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.gson.Gson
import com.idle.togeduck.common.LoginData
import com.idle.togeduck.databinding.FragmentMainBinding
import com.idle.togeduck.di.PreferenceModule
import com.idle.togeduck.event.EventListViewModel
import com.idle.togeduck.event.EventViewModel
import com.idle.togeduck.network.Coordinate
import com.idle.togeduck.network.Quest
import com.idle.togeduck.network.WebSocketManager
import com.idle.togeduck.quest.recruit.RecruitViewModel
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
import java.time.LocalDate
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.log

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var preference: PreferenceModule

    private val mainViewModel: MainViewModel by activityViewModels()
    private val shareViewModel: ShareViewModel by activityViewModels()

    private var temp1 = false
    private var temp2 = false

    val webSocketManager = WebSocketManager()
    val webSocketManager1 = WebSocketManager()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initGUID()

        binding.btn0.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_mapFragment)
        }

        binding.btn1.setOnClickListener {
            if (!temp1) {
                webSocketManager.connect()
                webSocketManager.subscribe("/topic/quests") { message ->
                    requireActivity().runOnUiThread {
                        questToast(message)
                    }
                }
                temp1 = true
            } else {
                temp1 = false
                webSocketManager.disconnect()
            }
        }

        binding.btn2.setOnClickListener {
            if (!temp2) {
                webSocketManager1.connect()
                webSocketManager1.subscribe("/topic/coors") { message ->
                    coor(message)
                }
                temp2 = true
                Log.d("웹소켓 연결", "연결됨")
            } else {
                temp2 = false
                webSocketManager1.disconnect()
                Log.d("웹소켓 좌표 종료", "연결끊김")
            }
        }

        binding.btn3.setOnClickListener {
//            findNavController().navigate(R.id.action_mainFragment_to_FavoriteSettingFragment)
            CoroutineScope(Dispatchers.IO).launch {
                shareViewModel.getShareList(1, 0, 5)
            }
        }
    }

    private fun initGUID() {
        LoginData.guid = runBlocking {
            preference.guidFlow.first()
        }

        CoroutineScope(Dispatchers.IO).launch {
            Log.d("로그", "MainFragment - initGUID() 호출됨 ${LoginData.guid}")
            if (LoginData.guid == null) {
                LoginData.guid = LoginUtil.makeGUID()
                preference.setGuid(LoginData.guid!!)
            }

            mainViewModel.login("GUEST", LoginData.guid!!)
        }
    }

    private fun questToast(message: String) {
        val questDto = Gson().fromJson(message, Quest::class.java)
        Toast.makeText(requireContext(), "${questDto.questKind}이 생성되었습니다", Toast.LENGTH_SHORT)
            .show()
    }

    private fun coor(message: String) {
        val coorDto = Gson().fromJson(message, Coordinate::class.java)
        Log.d("좌표", coorDto.toString())
    }

    fun doWorkWithPeriodic() {
        Log.d("로그", "doWorkWithPeriodic() 호출됨")

        val workRequest = PeriodicWorkRequestBuilder<GPSWorker>(15, TimeUnit.MINUTES).build()

        val workManager = WorkManager.getInstance(requireContext())
        workManager.enqueueUniquePeriodicWork(
            "doWorkWithPeriodic",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )

        workManager.cancelWorkById(workRequest.id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}