package com.amebaownd.pikohan_niwatori.recordsoundandtext.ui.permission

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.amebaownd.pikohan_niwatori.recordsoundandtext.MainActivity
import com.amebaownd.pikohan_niwatori.recordsoundandtext.databinding.HomeFragmentBinding
import com.amebaownd.pikohan_niwatori.recordsoundandtext.databinding.PermissionFragmentBinding
import com.amebaownd.pikohan_niwatori.recordsoundandtext.ui.home.HomeViewModel
import com.amebaownd.pikohan_niwatori.recordsoundandtext.util.EventObserver
import com.amebaownd.pikohan_niwatori.recordsoundandtext.util.getViewModelFactory

class PermissionFragment : Fragment() {

    private val viewModel: PermissionViewModel by viewModels { getViewModelFactory() }
    private lateinit var permissionFragmentBinding: PermissionFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        permissionFragmentBinding =
            PermissionFragmentBinding.inflate(inflater, container, false).apply {
                viewModel = this@PermissionFragment.viewModel
                lifecycleOwner = viewLifecycleOwner
            }
        return permissionFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = (this.activity as? MainActivity)?.viewModel?.id ?: "null"
        viewModel.start(id)

        viewModel.onClickedEvent.observe(this.viewLifecycleOwner,EventObserver{
            if(it){
                navigateToHomeFragment(id)
            }
        })
    }

    private fun navigateToHomeFragment(id:String){
        val action = PermissionFragmentDirections
            .actionPermissionFragmentToMainFragment(id)
        findNavController().navigate(action)
    }
}