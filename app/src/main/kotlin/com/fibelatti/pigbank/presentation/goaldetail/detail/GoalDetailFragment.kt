package com.fibelatti.pigbank.presentation.goaldetail.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fibelatti.pigbank.R
import com.fibelatti.pigbank.presentation.base.BaseFragment
import com.fibelatti.pigbank.presentation.common.extensions.gone
import com.fibelatti.pigbank.presentation.common.extensions.hideKeyboard
import com.fibelatti.pigbank.presentation.common.extensions.visible
import com.fibelatti.pigbank.presentation.models.GoalPresentationModel
import kotlinx.android.synthetic.main.fragment_goal_details.layoutAchieved
import kotlinx.android.synthetic.main.fragment_goal_details.layoutOverdue
import kotlinx.android.synthetic.main.fragment_goal_details.layoutRoot
import kotlinx.android.synthetic.main.fragment_goal_details.layoutSummary
import kotlinx.android.synthetic.main.layout_goal_summary.buttonRemoveFromGoal
import kotlinx.android.synthetic.main.layout_goal_summary.buttonSaveToGoal
import kotlinx.android.synthetic.main.layout_goal_summary.layoutActualSavings
import kotlinx.android.synthetic.main.layout_goal_summary.progressBarPercent
import kotlinx.android.synthetic.main.layout_goal_summary.textViewDaysUntilDeadline
import kotlinx.android.synthetic.main.layout_goal_summary.textViewSavingsActualPerDay
import kotlinx.android.synthetic.main.layout_goal_summary.textViewSavingsActualPerMonth
import kotlinx.android.synthetic.main.layout_goal_summary.textViewSavingsActualPerWeek
import kotlinx.android.synthetic.main.layout_goal_summary.textViewSavingsPerDay
import kotlinx.android.synthetic.main.layout_goal_summary.textViewSavingsPerMonth
import kotlinx.android.synthetic.main.layout_goal_summary.textViewSavingsPerWeek
import kotlinx.android.synthetic.main.layout_goal_summary.textViewSavingsProgressPercent
import kotlinx.android.synthetic.main.layout_goal_summary.textViewTotalSaved

class GoalDetailFragment :
    BaseFragment() {

    //region Companion objects and interfaces
    companion object {
        val TAG: String = GoalDetailFragment::class.java.simpleName

        fun newInstance(): GoalDetailFragment = GoalDetailFragment()
    }

    interface Callback {
        fun onDetailViewReady()

        fun onSaveToGoalClicked()

        fun onRemoveFromGoalClicked()
    }
    //endregion

    //region Public properties
    //endregion

    //region Private properties
    private var callback: Callback? = null
    //endregion

    //region Override properties
    //endregion

    //region Override Lifecycle methods
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_goal_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Callback) {
            callback = context
            callback?.onDetailViewReady()
        } else {
            throw RuntimeException(context.toString() + " must implement Callback")
        }
    }
    //endregion

    //region Override methods
    override fun handleError(errorMessage: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    //endregion

    //region Public methods
    fun showGoalDetails(goal: GoalPresentationModel) {
        with(goal) {
            textViewDaysUntilDeadline.text = resources.getQuantityString(R.plurals.goal_deadline_remaining, daysUntilDeadline.toInt(), daysUntilDeadline)
            textViewTotalSaved.text = getString(R.string.goal_total_saved, totalSaved)

            progressBarPercent.progress = percentSaved.toInt()
            textViewSavingsProgressPercent.text = getString(R.string.goal_saved_percent, percentSaved)

            showSavingsSuggestions(goal = this)
            showSavingsActual(goal = this)
        }

        layoutSummary.visible()
        layoutAchieved.gone()
        layoutOverdue.gone()
    }

    fun showGoalAchievedDetails() {
        layoutSummary.gone()
        layoutOverdue.gone()
        layoutAchieved.visible()
    }

    fun showGoalOverdueDetails() {
        layoutSummary.gone()
        layoutAchieved.gone()
        layoutOverdue.visible()
    }
    //endregion

    //region Private methods
    private fun initView() {
        buttonSaveToGoal.setOnClickListener {
            layoutRoot.hideKeyboard()
            callback?.onSaveToGoalClicked()
        }
        buttonRemoveFromGoal.setOnClickListener {
            layoutRoot.hideKeyboard()
            callback?.onRemoveFromGoalClicked()
        }
    }

    private fun showSavingsSuggestions(goal: GoalPresentationModel) {
        with(goal) {
            textViewSavingsPerDay.text = getString(R.string.goal_savings_per_day, suggestedSavingsPerDay)
            if (shouldShowSavingsPerWeek) {
                textViewSavingsPerWeek.visible()
                textViewSavingsPerWeek.text = getString(R.string.goal_savings_per_week, suggestedSavingsPerWeek)
            } else {
                textViewSavingsPerWeek.gone()
            }
            if (shouldShowSavingsPerMonth) {
                textViewSavingsPerMonth.visible()
                textViewSavingsPerMonth.text = getString(R.string.goal_savings_per_month, suggestedSavingsPerMonth)
            } else {
                textViewSavingsPerMonth.gone()
            }
        }
    }

    private fun showSavingsActual(goal: GoalPresentationModel) {
        with(goal) {
            if (shouldShowActualSavingsPerDay) {
                layoutActualSavings.visible()
                textViewSavingsActualPerDay.text = getString(R.string.goal_savings_actual_per_day, actualSavingsPerDay)

                if (shouldShowActualSavingsPerWeek) {
                    textViewSavingsActualPerWeek.visible()
                    textViewSavingsActualPerWeek.text = getString(R.string.goal_savings_actual_per_week, actualSavingsPerWeek)
                } else {
                    textViewSavingsActualPerWeek.gone()
                }

                if (shouldShowActualSavingsPerMonth) {
                    textViewSavingsActualPerMonth.visible()
                    textViewSavingsActualPerMonth.text = getString(R.string.goal_savings_actual_per_month, actualSavingsPerMonth)
                } else {
                    textViewSavingsActualPerMonth.gone()
                }
            } else {
                layoutActualSavings.gone()
            }
        }
    }
    //endregion
}
