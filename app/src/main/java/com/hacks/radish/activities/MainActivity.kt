package com.hacks.radish.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProviders
import com.hacks.radish.R
import com.hacks.radish.fragments.FeedFragment
import com.hacks.radish.managers.ApplicationFragmentManager
import com.hacks.radish.util.lazyAndroid
import com.hacks.radish.viewmodels.MainActivityViewModel
import com.hacks.radish.viewmodels.MainViewModelFactory
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory

    lateinit var fragmentManager: ApplicationFragmentManager

    val mainActivityViewModel : MainActivityViewModel by lazyAndroid {
        ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MainApplication.mainApplicationComponent.inject(this)

        fragmentManager = ApplicationFragmentManager(supportFragmentManager)

        //Give viewmodel ability to start activities with result
        mainActivityViewModel.startActivityForResultListener = { intent, requestCode, bundle -> startActivityForResult(intent, requestCode, bundle) }
        fragmentManager.loadInitialFragment(R.id.mainActivityFrameLayout, FeedFragment())

        //Setup action bar
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return mainActivityViewModel.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mainActivityViewModel.onActivityResult(requestCode, resultCode, data)
    }
}
