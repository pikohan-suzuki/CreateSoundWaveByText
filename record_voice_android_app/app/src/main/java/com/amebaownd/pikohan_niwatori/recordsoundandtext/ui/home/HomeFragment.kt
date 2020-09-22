package com.amebaownd.pikohan_niwatori.recordsoundandtext.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.amebaownd.pikohan_niwatori.recordsoundandtext.MainActivity
import com.amebaownd.pikohan_niwatori.recordsoundandtext.MainViewModel
import com.amebaownd.pikohan_niwatori.recordsoundandtext.databinding.HomeFragmentBinding
import com.amebaownd.pikohan_niwatori.recordsoundandtext.util.getViewModelFactory

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels { getViewModelFactory() }
    private lateinit var mainFragmentBinding: HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainFragmentBinding = HomeFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = this@HomeFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return mainFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = (this.activity as? MainActivity)?.viewModel?.id ?: "null"
        viewModel.start(id)
    }
}