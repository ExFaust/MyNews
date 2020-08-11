package com.exfaust.mynews.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.exfaust.mynews.App
import com.exfaust.mynews.R
import com.exfaust.mynews.presentation.WebViewPresenter
import com.exfaust.mynews.presentation.views.WebFragmentView
import javax.inject.Inject

class WebViewFragment : MvpAppCompatFragment(), WebFragmentView {

    @Inject
    @InjectPresenter
    lateinit var webViewPresenter: WebViewPresenter

    @ProvidePresenter
    fun provide() = webViewPresenter

    init {
        App.instance.getAppComponent().inject(this)
    }

    companion object {
        const val ARG_URL: String = "ARG_URL"
    }

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        if (requireArguments().containsKey(ARG_URL)) {
            val url = requireArguments().getString(ARG_URL)!!
            webViewPresenter.onGetUrl(url)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_webview, container, false)

        webView = view.findViewById(R.id.webView)
        progressBar=view.findViewById(R.id.progressBar)

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress == 100) {
                    webViewPresenter.onLoadedWebView()
                } else {
                    webViewPresenter.onProgressChangedWebView(newProgress)
                }
            }
        }

        val activity = activity as AppCompatActivity?
        val actionBar = activity?.supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun loadUrl(url: String) {
        webView.loadUrl(url)
    }

    override fun showLoading(progress:Int) {
        progressBar.visibility = View.VISIBLE
        progressBar.progress = progress
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
    }
}