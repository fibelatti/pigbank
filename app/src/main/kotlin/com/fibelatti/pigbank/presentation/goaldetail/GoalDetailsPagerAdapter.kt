package com.fibelatti.pigbank.presentation.goaldetail

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.fibelatti.pigbank.presentation.goaldetail.detail.GoalDetailFragment
import com.fibelatti.pigbank.presentation.goaldetail.savingslog.SavingsLogFragment
import java.lang.AssertionError

class GoalDetailsPagerAdapter(
    fragmentManager: FragmentManager,
    private val goalDetailFragment: GoalDetailFragment?,
    private val savingsLogFragment: SavingsLogFragment?
) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment = when (position) {
        Tabs.GOAL_DETAILS.position -> goalDetailFragment ?: GoalDetailFragment.newInstance()
        Tabs.SAVINGS_LOG.position -> savingsLogFragment ?: SavingsLogFragment.newInstance()
        else -> throw AssertionError("Wrong argument. Position must be between 0-2")
    }

    override fun getCount(): Int = Tabs.values().size

    private enum class Tabs(val position: Int) {
        GOAL_DETAILS(0),
        SAVINGS_LOG(1)
    }
}
