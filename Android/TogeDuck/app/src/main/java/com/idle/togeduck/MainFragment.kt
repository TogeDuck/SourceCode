package com.idle.togeduck

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.idle.togeduck.R
import com.idle.togeduck.databinding.FragmentMainBinding
import com.idle.togeduck.network.Coordinate
import com.idle.togeduck.network.Quest
import com.idle.togeduck.network.WebSocketManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private var temp1 = false
    private var temp2 = false

    val webSocketManager = WebSocketManager()
    val webSocketManager1 = WebSocketManager()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btn0.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_mapFragment)
        }

        binding.btn1.setOnClickListener {
            if(!temp1){
                webSocketManager.connect()
                webSocketManager.subscribe("/topic/quests") { message ->
                    requireActivity().runOnUiThread {
                        questToast(message)
                    }
                }
                temp1 = true
            }
            else{
                temp1 = false
                webSocketManager.disconnect()
            }
        }

        binding.btn2.setOnClickListener {
            if(!temp2){
                webSocketManager1.connect()
                webSocketManager1.subscribe("/topic/coors"){
                        message -> coor(message)
                }
                temp2 = true
                Log.d("웹소켓 연결","연결됨")
            }
            else{
                temp2 = false
                webSocketManager1.disconnect()
                Log.d("웹소켓 좌표 종료","연결끊김")
            }
        }

        binding.btn3.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_FavoriteSettingFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun questToast(message: String) {
        val questDto = Gson().fromJson(message, Quest::class.java)
        Toast.makeText(requireContext(), "${questDto.questKind}이 생성되었습니다", Toast.LENGTH_SHORT)
            .show()
    }

    private fun coor(message: String){
        val coorDto = Gson().fromJson(message, Coordinate::class.java)
        Log.d("좌표", coorDto.toString())
    }

}