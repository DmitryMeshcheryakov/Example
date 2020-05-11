package com.android.example.core.ui.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentStatePagerAdapter
import com.android.example.core.ui.fragments.BaseFragment

class ViewPagerAdapter : FragmentStatePagerAdapter {

    private var pagesList: List<Fragment>
    private var context: Context? = null

    constructor(fragment: Fragment, pages: List<Fragment>): super(fragment.childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        this.pagesList = pages
        this.context = fragment.context
    }

    constructor(activity: FragmentActivity, pages: List<Fragment>): super(activity.supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        this.pagesList = pages
        this.context = activity
    }

    fun setItems(list: List<Fragment>) {
        pagesList = list
        notifyDataSetChanged()
    }

    fun getItems() = pagesList

    override fun getItem(position: Int) = pagesList[position]

    override fun getCount() = pagesList.size

    override fun getPageTitle(position: Int): CharSequence? = getFragment(position)?.titleId?.let { context?.getString(it) } ?: getFragment(position)?.titleString

    private fun getFragment(position: Int) = getItem(position) as? BaseFragment?
}