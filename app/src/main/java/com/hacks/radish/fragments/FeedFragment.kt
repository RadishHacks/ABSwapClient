package com.hacks.radish.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.hacks.radish.activities.MainApplication
import com.hacks.radish.adapters.FeedAdapter
import com.hacks.radish.adapters.layoutmanagers.FeedLayoutManager
import com.hacks.radish.repo.api.vote.VoteRepo
import com.hacks.radish.repo.dataobject.ImagePairDO
import com.hacks.radish.util.lazyAndroid
import com.hacks.radish.viewmodels.MainActivityViewModel
import com.hacks.radish.viewmodels.MainViewModelFactory
import kotlinx.android.synthetic.main.fragment_feed.*
import javax.inject.Inject

class FeedFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory

    private val mainActivityViewModel: MainActivityViewModel by lazyAndroid {
        ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel::class.java)
    }

    private var feedAdapter : FeedAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(com.hacks.radish.R.layout.fragment_feed, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        MainApplication.mainApplicationComponent.inject(this)
        observeFeedItems()
        initVoteListeners()
        observeVotes()
    }

    private fun observeFeedItems() {
        mainActivityViewModel.feedListLiveData.observe(this, Observer {
            if (feedAdapter == null) {
                feedLoadingView.cancelAnimation()
                feedLoadingView.visibility = View.GONE
                setupRecyclerView(it)
            } else {
                feedAdapter?.list = it
            }
        })
        mainActivityViewModel.fetchNewFeed(5) //Fetch a new feed
    }

    private fun setupRecyclerView(list : List<ImagePairDO>) {
        feedRecyclerView.visibility = View.VISIBLE
        feedRecyclerView.layoutManager = FeedLayoutManager()
        feedRecyclerView.adapter = FeedAdapter(list, requireContext(), mainActivityViewModel.getFeedOnClickListener())
    }

    private fun initVoteListeners() {

    }

    private fun observeVotes() {
        mainActivityViewModel.voteLiveData.observe(this, Observer {
            when(it) {
                VoteRepo.Status.VOTE_SUCCEED -> {
                    feedRecyclerView.layoutManager?.apply {this as FeedLayoutManager
                        scrollToPosition(findFirstVisibleItemPosition() + 1)
                    }
                }
                VoteRepo.Status.VOTE_FAILED -> {
                    Snackbar.make(requireView(), getString(com.hacks.radish.R.string.voting_failed), Snackbar.LENGTH_SHORT)
                }
                else -> {}
            }
        })
    }
}