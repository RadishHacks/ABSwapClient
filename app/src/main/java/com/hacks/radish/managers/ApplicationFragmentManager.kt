package com.hacks.radish.managers

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import com.hacks.radish.fragments.BaseFragment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationFragmentManager @Inject constructor(private val fragmentManager: FragmentManager) {

    private var containerId = -1
    private lateinit var startingFragment: BaseFragment

    fun loadInitialFragment(@IdRes containerId : Int, startingFragment : BaseFragment) {
        this.containerId = containerId
        this.startingFragment = startingFragment

        fragmentManager
            .beginTransaction()
            .add(containerId, startingFragment)
            .commit()
    }
}