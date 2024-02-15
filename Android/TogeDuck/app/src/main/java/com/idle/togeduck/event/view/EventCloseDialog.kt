package com.idle.togeduck.event.view

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.idle.togeduck.MainViewModel
import com.idle.togeduck.R
import com.idle.togeduck.common.Theme
import com.idle.togeduck.databinding.DialogEventCloseBinding
import com.idle.togeduck.databinding.DialogQuestSharePostBinding
import com.idle.togeduck.di.PreferenceModule
import com.idle.togeduck.event.EventListViewModel
import com.idle.togeduck.event.view.list.list_rv.EventInfo
import com.idle.togeduck.event.view.list.list_rv.EventInfoAdapter
import com.idle.togeduck.favorite.FavoriteSettingViewModel
import com.idle.togeduck.history.HistoryViewModel
import com.idle.togeduck.main_map.MapViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.toKotlinLocalDate
import javax.inject.Inject

@AndroidEntryPoint
class EventCloseDialog: DialogFragment(), EventInfo {
    private var _binding: DialogEventCloseBinding? = null
    private val binding get() = _binding!!

    private val mapViewModel: MapViewModel by activityViewModels ()
    private val historyViewModel: HistoryViewModel by activityViewModels()
    private val eventListViewModel: EventListViewModel by activityViewModels()
    private val favoriteSettingViewModel: FavoriteSettingViewModel by activityViewModels()

    @Inject
    lateinit var preference: PreferenceModule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.fullscreen_dialog)
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogEventCloseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dialogEventCloseLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), Theme.theme.sub200))
        val recyclerView = binding.dialogEventCloseRv
        val eventAdpater = EventInfoAdapter(this, requireContext(),0)
        recyclerView.adapter = eventAdpater
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        eventAdpater.submitList(mapViewModel.eventList)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mapViewModel.isCloseDialogOpen.postValue(false)
    }

    override fun eventClicked(position: Int, type: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            preference.setCakeCount(++Theme.myCake)
        }
        sendEvent(position)
        getEventList()
        if(mapViewModel.eventList.getOrNull(position) != null){
            mapViewModel.visitedEvent.add(mapViewModel.eventList.getOrNull(position)!!.eventId)
        }
        exit()
    }
    fun sendEvent(position: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val eventId = mapViewModel.eventList.getOrNull(position)?.eventId
            val historyId = historyViewModel.historyId.value
            if (eventId != null && historyId != null) {
                historyViewModel.addHistory(eventId, historyId)
            } else {
                Log.d("EventClicked", "eventId or historyId is null")
            }
        }
    }
    fun getEventList(){
        CoroutineScope(Dispatchers.IO).launch {
            if(favoriteSettingViewModel.selectedCelebrity.value != null
                && mapViewModel.pickedDate.value != null ){
                eventListViewModel.getEventList(
                    favoriteSettingViewModel.selectedCelebrity.value!!.id,
                    mapViewModel.pickedDate.value!!.first.toKotlinLocalDate(),
                    mapViewModel.pickedDate.value!!.second.toKotlinLocalDate())
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        Log.d("로그", "EventCloseDialog - onDismiss() 호출됨")
        super.onDismiss(dialog)
        mapViewModel.isCloseDialogOpen.postValue(false)
    }

    override fun onCancel(dialog: DialogInterface) {
        Log.d("로그", "EventCloseDialog - onCancel() 호출됨")
        super.onCancel(dialog)
        mapViewModel.isCloseDialogOpen.postValue(false)
    }

    fun exit(){
        findNavController().navigate(R.id.action_eventCloseDialog_pop)
        mapViewModel.isCloseDialogOpen.postValue(false)
    }

    override fun likeClick(position: Int, type: Int) {
    }
}