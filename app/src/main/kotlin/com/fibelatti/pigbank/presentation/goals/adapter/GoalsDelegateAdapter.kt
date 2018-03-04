package com.fibelatti.pigbank.presentation.goals.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.fibelatti.pigbank.R
import com.fibelatti.pigbank.presentation.base.BaseDelegateAdapter
import com.fibelatti.pigbank.presentation.base.BaseViewType
import com.fibelatti.pigbank.presentation.common.extensions.gone
import com.fibelatti.pigbank.presentation.common.extensions.inflate
import com.fibelatti.pigbank.presentation.common.extensions.visible
import com.fibelatti.pigbank.presentation.models.Goal
import kotlinx.android.synthetic.main.list_item_goal.view.imageViewGoalAchieved
import kotlinx.android.synthetic.main.list_item_goal.view.imageViewSaveToGoal
import kotlinx.android.synthetic.main.list_item_goal.view.layoutClickableGoal
import kotlinx.android.synthetic.main.list_item_goal.view.progressBarPercent
import kotlinx.android.synthetic.main.list_item_goal.view.textViewDaysUntilDeadline
import kotlinx.android.synthetic.main.list_item_goal.view.textViewDescription
import kotlinx.android.synthetic.main.list_item_goal.view.textViewSaveToGoal
import kotlinx.android.synthetic.main.list_item_goal.view.textViewSavingsProgress
import kotlinx.android.synthetic.main.list_item_goal.view.textViewSavingsProgressPercent

class GoalsDelegateAdapter :
    BaseDelegateAdapter {

    interface Callback {
        fun goalClicked(goal: Goal)

        fun saveToGoalClicked(goal: Goal)
    }

    var callback: Callback? = null

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = DataViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: BaseViewType) {
        (holder as? DataViewHolder)?.bind(item as? Goal)
    }

    internal inner class DataViewHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(parent.inflate(R.layout.list_item_goal)) {
        fun bind(item: Goal?) = apply {
            item?.apply {
                itemView.layoutClickableGoal.setOnClickListener { callback?.goalClicked(goal = this) }
                itemView.progressBarPercent.setOnClickListener { callback?.goalClicked(goal = this) }

                itemView.textViewDescription.text = description
                itemView.textViewSavingsProgress.text = itemView.context.resources.getString(R.string.goal_saved_relative, totalSaved, cost)
                itemView.progressBarPercent.progress = percentSaved.toInt()
                itemView.textViewSavingsProgressPercent.text = itemView.context.resources.getString(R.string.goal_saved_percent, percentSaved.toInt())

                if (!isAchieved) {
                    itemView.imageViewSaveToGoal.setOnClickListener { callback?.saveToGoalClicked(goal = this) }

                    if (!isOverdue) {
                        itemView.textViewDaysUntilDeadline.text = itemView.context.resources.getQuantityString(R.plurals.goal_deadline_remaining, daysUntilDeadline.toInt(), daysUntilDeadline)
                    } else {
                        itemView.textViewDaysUntilDeadline.text = itemView.context.resources.getString(R.string.goal_deadline_overdue)
                    }
                    itemView.textViewDaysUntilDeadline.setTextColor(
                        ContextCompat.getColor(itemView.context, if (emphasizeRemainingDays || isOverdue) R.color.alertRed else R.color.textPrimary))
                } else {
                    itemView.textViewDaysUntilDeadline.gone()
                    itemView.imageViewSaveToGoal.gone()
                    itemView.textViewSaveToGoal.gone()
                    itemView.imageViewGoalAchieved.visible()
                }
            }
        }
    }
}
