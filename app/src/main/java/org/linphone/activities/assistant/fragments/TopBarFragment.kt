package org.linphone.activities.assistant.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import org.linphone.R
import org.linphone.activities.GenericFragment
import org.linphone.databinding.AssistantTopBarFragmentBinding

class TopBarFragment : GenericFragment<AssistantTopBarFragmentBinding>() {
    override fun getLayoutId(): Int = R.layout.assistant_top_bar_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        useMaterialSharedAxisXForwardAnimation = false

        binding.setBackClickListener {
            goBack()
        }
    }

    override fun goBack() {
        if (!findNavController().popBackStack()) {
            requireActivity().finish()
        }
    }
}
