package org.linphone.activities.assistant.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import androidx.lifecycle.ViewModelProvider
import java.util.regex.Pattern
import org.linphone.IntelewebApplication.Companion.corePreferences
import org.linphone.R
import org.linphone.activities.*
import org.linphone.activities.assistant.viewmodels.WelcomeViewModel
import org.linphone.activities.navigateToAccountLogin
import org.linphone.activities.navigateToGenericLogin
import org.linphone.activities.navigateToRemoteProvisioning
import org.linphone.databinding.AssistantWelcomeFragmentBinding

class WelcomeFragment : GenericFragment<AssistantWelcomeFragmentBinding>() {
    private lateinit var viewModel: WelcomeViewModel

    override fun getLayoutId(): Int = R.layout.assistant_welcome_fragment

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        viewModel = ViewModelProvider(this)[WelcomeViewModel::class.java]
        binding.viewModel = viewModel

        binding.setCreateAccountClickListener {
//            if (resources.getBoolean(R.bool.isTablet)) {
//                navigateToEmailAccountCreation()
//            } else {
//                navigateToPhoneAccountCreation()
//            }
//            binding.scrollWelcomeView?.visibility = View.GONE
//            binding.webviewLogin?.visibility = View.VISIBLE
//            binding.webviewLogin?.webViewClient = object : WebViewClient() {
//                override fun onPageFinished(view: WebView?, url: String?) {
//                    super.onPageFinished(view, url)
//                    binding.webviewLogin?.evaluateJavascript("(function() { return JSON.stringify(localStorage); })();") { s ->
//                        if (s != "\"{}\"") {
// //                            var jsonAsStr = s.substring(1, s.length - 1).replace("\\", "")
// //                            val obj = JSONObject(jsonAsStr)
// //                            val token = obj.getString("token")
//                        }
//                    }
//                }
//            }
//            binding.webviewLogin?.loadUrl("accounts.Inteleweb.com")
//            binding.webviewLogin?.settings?.javaScriptEnabled = true
//            binding.webviewLogin?.addJavascriptInterface(WebAppInterface(requireContext()), "Android")
//            val link = binding.edtDeeplink?.text.toString()
//            if (link.isEmpty()) {
//                Toast.makeText(requireContext(), "Please Enter Link", Toast.LENGTH_SHORT).show()
//            } else {
//            }
        }

        binding.setAccountLoginClickListener {
            navigateToAccountLogin()
        }

        binding.setGenericAccountLoginClickListener {
            navigateToGenericLogin()
        }

        binding.setRemoteProvisioningClickListener {
            navigateToRemoteProvisioning()
        }

        viewModel.termsAndPrivacyAccepted.observe(
            viewLifecycleOwner,
            {
                if (it) corePreferences.readAndAgreeTermsAndPrivacy = true
            }
        )

        setUpTermsAndPrivacyLinks()
    }

    private fun setUpTermsAndPrivacyLinks() {
        val terms = getString(R.string.assistant_general_terms)
        val privacy = getString(R.string.assistant_privacy_policy)

        val label = getString(
            R.string.assistant_read_and_agree_terms,
            terms,
            privacy
        )
        val spannable = SpannableString(label)

        val termsMatcher = Pattern.compile(terms).matcher(label)
        if (termsMatcher.find()) {
            val clickableSpan: ClickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    val browserIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.assistant_general_terms_link))
                    )
                    startActivity(browserIntent)
                }
            }
            spannable.setSpan(clickableSpan, termsMatcher.start(0), termsMatcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        val policyMatcher = Pattern.compile(privacy).matcher(label)
        if (policyMatcher.find()) {
            val clickableSpan: ClickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    val browserIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.assistant_privacy_policy_link))
                    )
                    startActivity(browserIntent)
                }
            }
            spannable.setSpan(clickableSpan, policyMatcher.start(0), policyMatcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        binding.termsAndPrivacy.text = spannable
        binding.termsAndPrivacy.movementMethod = LinkMovementMethod.getInstance()
    }

    class WebAppInterface(private val mContext: Context) {
        @JavascriptInterface
        fun consumeToken(token: String) {
            Log.d("consumeToken", "consumeToken: --- $token ")
        }
    }
}
