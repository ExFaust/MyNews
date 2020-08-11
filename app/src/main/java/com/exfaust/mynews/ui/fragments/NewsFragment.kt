package com.exfaust.mynews.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.exfaust.mynews.App
import com.exfaust.mynews.R
import com.exfaust.mynews.data.model.NetworkState
import com.exfaust.mynews.presentation.NewsPresenter
import com.exfaust.mynews.presentation.views.MainView
import com.exfaust.mynews.ui.adapters.NewsPagingAdapter

import javax.inject.Inject

class NewsFragment : MvpAppCompatFragment(), MainView {

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    @Inject
    @InjectPresenter
    lateinit var newsPresenter: NewsPresenter

    @ProvidePresenter
    fun provide() = newsPresenter

    init {
        App.instance.getAppComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_news, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                activity,
                LinearLayoutManager.VERTICAL
            )
        )

        val activity = activity as AppCompatActivity?
        val actionBar = activity?.supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        initSwipeToRefresh()

        return view
    }

    override fun setAdapter(adapter: NewsPagingAdapter) {
        recyclerView.adapter = adapter
        newsPresenter.articlesList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
        newsPresenter.getNetworkState()
            .observe(this, Observer<NetworkState> { adapter.setNetworkState(it) })
    }

    override fun goToWebView(url: String) {
        val bundle = Bundle()
        bundle.putString(WebViewFragment.ARG_URL, url)
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            .navigate(R.id.webViewFragment, bundle)
    }

    private fun initSwipeToRefresh() {
        newsPresenter.getRefreshState().observe(viewLifecycleOwner, Observer { networkState ->
            swipeRefreshLayout.isRefreshing = networkState?.status == NetworkState.LOADING.status
        })
        swipeRefreshLayout.setOnRefreshListener { newsPresenter.onRefresh() }
    }
}