package org.linphone.activities.assistant.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import org.linphone.IntelewebApplication.Companion.coreContext
import org.linphone.R
import org.linphone.activities.GenericFragment
import org.linphone.activities.assistant.AssistantActivity
import org.linphone.activities.assistant.viewmodels.RemoteProvisioningViewModel
import org.linphone.activities.assistant.viewmodels.SharedAssistantViewModel
import org.linphone.activities.navigateToEchoCancellerCalibration
import org.linphone.activities.navigateToQrCode
import org.linphone.databinding.AssistantRemoteProvisioningFragmentBinding

class RemoteProvisioningFragment : GenericFragment<AssistantRemoteProvisioningFragmentBinding>() {
    private lateinit var sharedViewModel: SharedAssistantViewModel
    private lateinit var viewModel: RemoteProvisioningViewModel

    override fun getLayoutId(): Int = R.layout.assistant_remote_provisioning_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        sharedViewModel = requireActivity().run {
            ViewModelProvider(this)[SharedAssistantViewModel::class.java]
        }

        viewModel = ViewModelProvider(this)[RemoteProvisioningViewModel::class.java]
        binding.viewModel = viewModel

        binding.setQrCodeClickListener {
            navigateToQrCode()
        }

        viewModel.fetchSuccessfulEvent.observe(
            viewLifecycleOwner,
            {
                it.consume { success ->
                    if (success) {
                        if (coreContext.core.isEchoCancellerCalibrationRequired) {
                            navigateToEchoCancellerCalibration()
                        } else {
                            requireActivity().finish()
                        }
                    } else {
                        val activity = requireActivity() as AssistantActivity
                        activity.showSnackBar(R.string.assistant_remote_provisioning_failure)
                    }
                }
            }
        )
//        viewModel.urlToFetch.value = sharedViewModel.remoteProvisioningUrl.value ?: coreContext.core.provisioningUri
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedViewModel.remoteProvisioningUrl.value = null
    }
}
