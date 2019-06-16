package com.hacks.radish.viewmodels

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hacks.radish.R
import com.hacks.radish.fragments.BaseFragment
import com.hacks.radish.fragments.FeedFragment
import com.hacks.radish.fragments.PostFragment
import com.hacks.radish.managers.MenuManager
import com.hacks.radish.repo.api.feed.FeedApi
import com.hacks.radish.repo.api.feed.IFeedApi
import com.hacks.radish.repo.dataobject.ImagePairDO
import com.hacks.radish.util.lazyAndroid
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainActivityViewModel @Inject constructor(private val context : Context,
                                                private val menuManager: MenuManager,
                                                private val feedApi: FeedApi) : BaseViewModel() {

    companion object {
        const val UPLOAD_IMAGE_1_REQUEST_CODE = 10
        const val UPLOAD_IMAGE_2_REQUEST_CODE = 11
    }

    lateinit var startActivityForResultListener : (intent : Intent, requestCode : Int, bundle : Bundle?) -> Unit

    val dashboardFragments : ArrayList<BaseFragment> by lazyAndroid { ArrayList<BaseFragment>() }
    val image1UriLiveData by lazyAndroid {
        MediatorLiveData<Uri>().apply {
            value = Uri.EMPTY
        }
    }
    val image2UriLiveData by lazyAndroid {
        MediatorLiveData<Uri>().apply {
            value = Uri.EMPTY
        }
    }

    val feedListLiveData by lazyAndroid {
        MutableLiveData<List<ImagePairDO>>()
    }

    init {
        dashboardFragments.add(PostFragment())
        dashboardFragments.add(FeedFragment())
    }

    fun fetchNewFeed(size : Int) {
        vms.launch {
            feedListLiveData.postValue(feedApi.getFeed(5)?.imagePairsDO)
        }
    }

    fun getMdeiaFragments() {

    }

    fun onOptionsItemSelected(item: MenuItem?) : Boolean {
        when (item?.itemId) {
            R.id.uploadMenuIcon -> { /* Do Upload Item logic */}
        }
        return true
    }

    fun onUploadImage1Clicked() {
        chooseImage(UPLOAD_IMAGE_1_REQUEST_CODE)
    }

    fun onUploadImage2Clicked() {
        chooseImage(UPLOAD_IMAGE_2_REQUEST_CODE)
    }

    private fun chooseImage(requestCode : Int) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResultListener(Intent.createChooser(intent, "Select a Picture "), requestCode, null)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            //Activity result is valid
            when (requestCode) {
                UPLOAD_IMAGE_1_REQUEST_CODE -> {
                    data?.let {
                        image1UriLiveData.value = it.data
                    }
                }
                UPLOAD_IMAGE_2_REQUEST_CODE -> {
                    data?.let {
                        image2UriLiveData.value = it.data
                    }
                }
                else -> {} /* do nothing */
            }
        } else {
            /* Activity result is not valid */
        }
    }
}