package com.idle.togeduck.main_map.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.idle.togeduck.R
import com.idle.togeduck.common.Theme
import com.idle.togeduck.databinding.DialogSelectCelebrityBinding
import com.idle.togeduck.favorite.FavoriteSettingViewModel
import com.idle.togeduck.main_map.MapViewModel
import com.idle.togeduck.main_map.view.select_celebrity.ISelectCelebrity
import com.idle.togeduck.main_map.view.select_celebrity.SelectCelebrityAdapter
import com.idle.togeduck.util.TogeDuckItemDecoration
import com.idle.togeduck.util.getColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectCelebrityFragment : DialogFragment(), ISelectCelebrity {
    private var _binding: DialogSelectCelebrityBinding? = null
    private val binding get() = _binding!!

    private val favoriteSettingViewModel : FavoriteSettingViewModel by activityViewModels()

    private lateinit var selectCelebrityAdapter: SelectCelebrityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.fullscreen_dialog)
        isCancelable = true
    }

    // 들어오고 나갈때 애니메이션 설정
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
//        dialog.window!!.attributes.windowAnimations = R.style.dialog_animation
        dialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogSelectCelebrityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
        setTheme()

        binding.llEmptyLayout.setOnClickListener {
            findNavController().navigate(R.id.action_selectCelebrityFragment_pop)
        }

        favoriteSettingViewModel.favoriteIdolList.observe(viewLifecycleOwner) { favoriteIdolList ->
            selectCelebrityAdapter.submitList(favoriteIdolList)
        }

        binding.btnCancel.setOnClickListener {
            favoriteSettingViewModel.favoriteIdolList.value?.forEach {
                it.isClicked = it.isSelected
            }

            findNavController().navigate(R.id.action_selectCelebrityFragment_pop)
        }

        binding.btnSelect.setOnClickListener {
            favoriteSettingViewModel.favoriteIdolList.value?.forEach { celebrity ->
                celebrity.isSelected = celebrity.isClicked
            }

//            favoriteSettingViewModel.setSelectedCelebrity()

            findNavController().navigate(R.id.action_selectCelebrityFragment_pop)
        }

        binding.btnEditCelebrity.setOnClickListener {
            findNavController().navigate(R.id.action_selectCelebrityFragment_to_favoriteSettingFragment)
        }
    }

    private fun setRecyclerView() {
        selectCelebrityAdapter = SelectCelebrityAdapter(this, requireContext())

        binding.rvSelectCelebrity.apply {
            addItemDecoration(TogeDuckItemDecoration(10, 0))
            adapter = selectCelebrityAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

//        favoriteSettingViewModel.setClickedCelebrity()

        if (favoriteSettingViewModel.selectedCelebrity.value != null) {
            favoriteSettingViewModel.favoriteIdolList.value?.forEach { celebrity ->
                celebrity.isClicked = celebrity.isSelected
            }
        }
    }

    private fun setTheme() {
        val allRoundDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_all_round_10) as GradientDrawable
        allRoundDrawable.setColor(ContextCompat.getColor(requireContext(), Theme.theme.main200))
        allRoundDrawable.setStroke(0, ContextCompat.getColor(requireContext(), Theme.theme.main500))
        binding.llDialogLayout.background = allRoundDrawable

        val squareCircleDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_square_circle) as GradientDrawable
        squareCircleDrawable.setColor(ContextCompat.getColor(requireContext(), Theme.theme.sub400))
        squareCircleDrawable.setStroke(0, ContextCompat.getColor(requireContext(), Theme.theme.main500))

        binding.btnCancel.background = squareCircleDrawable
        binding.btnSelect.background = squareCircleDrawable

        val allRoundEditCelebrityDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_all_round_5) as GradientDrawable
        allRoundEditCelebrityDrawable.setColor(getColor(requireContext(), Theme.theme.sub200))
        allRoundEditCelebrityDrawable.setStroke(0, getColor(requireContext(), Theme.theme.main500))

        binding.btnEditCelebrity.background = allRoundEditCelebrityDrawable
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun celebrityClicked(position: Int) {
        if ((favoriteSettingViewModel.clickedCelebrity.value?.id
                ?: -1) != favoriteSettingViewModel.favoriteIdolList.value!![position].id
        ) {
            favoriteSettingViewModel.clickedCelebrity(position)
            favoriteSettingViewModel.setClickedCelebrity(favoriteSettingViewModel.favoriteIdolList.value!![position])
        }

        selectCelebrityAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}