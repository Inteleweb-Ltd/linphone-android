package org.linphone.activities.main.settings.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import org.linphone.R
import org.linphone.activities.main.settings.viewmodels.TunnelSettingsViewModel
import org.linphone.activities.navigateToEmptySetting
import org.linphone.databinding.SettingsTunnelFragmentBinding
import org.linphone.utils.Event

class TunnelSettingsFragment : GenericSettingFragment<SettingsTunnelFragmentBinding>() {
    private lateinit var viewModel: TunnelSettingsViewModel

    override fun getLayoutId(): Int = R.layout.settings_tunnel_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.sharedMainViewModel = sharedViewModel

        viewModel = ViewModelProvider(this)[TunnelSettingsViewModel::class.java]
        binding.viewModel = viewModel

        binding.setBackClickListener { goBack() }
    }

    override fun goBack() {
        if (sharedViewModel.isSlidingPaneSlideable.value == true) {
            sharedViewModel.closeSlidingPaneEvent.value = Event(true)
        } else {
            navigateToEmptySetting()
        }
    }
}
