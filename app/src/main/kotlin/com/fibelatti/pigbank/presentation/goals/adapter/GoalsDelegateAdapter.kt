package com.fibelatti.pigbank.presentation.goals.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.fibelatti.pigbank.R
import com.fibelatti.pigbank.presentation.base.BaseDelegateAdapter
import com.fibelatti.pigbank.presentation.base.BaseViewType
import com.fibelatti.pigbank.presentation.common.ObservableView
import com.fibelatti.pigbank.presentation.common.inflate
import com.fibelatti.pigbank.presentation.models.Goal
import kotlinx.android.synthetic.main.list_item_goal.view.imageViewSaveToGoal
import kotlinx.android.synthetic.main.list_item_goal.view.layoutClickableGoal
import kotlinx.android.synthetic.main.list_item_goal.view.progressBarPercent
import kotlinx.android.synthetic.main.list_item_goal.view.textViewDaysUntilDeadline
import kotlinx.android.synthetic.main.list_item_goal.view.textViewDescription
import kotlinx.android.synthetic.main.list_item_goal.view.textViewSavingsProgress
import kotlinx.android.synthetic.main.list_item_goal.view.textViewSavingsProgressPercent

class GoalsDelegateAdapter :
    BaseDelegateAdapter {

    val itemClickObservable = ObservableView<Goal>()
    val saveToGoalClickObservable = ObservableView<Goal>()

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = DataViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: BaseViewType) {
        (holder as? DataViewHolder)?.bind(item as? Goal)
    }

    internal inner class DataViewHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(parent.inflate(R.layout.list_item_goal)) {
        fun bind(item: Goal?) = apply {
            item?.apply {
                itemView.layoutClickableGoal.setOnClickListener { itemClickObservable.emitNext(this) }
                itemView.imageViewSaveToGoal.setOnClickListener { saveToGoalClickObservable.emitNext(this) }

                itemView.textViewDescription.text = description
                itemView.textViewSavingsProgress.text = itemView.context.resources.getString(R.string.goal_saved_relative, totalSaved, cost)
                itemView.textViewDaysUntilDeadline.text = itemView.context.resources.getQuantityString(R.plurals.goal_deadline_remaining, daysUntilDeadline.toInt(), daysUntilDeadline)
                itemView.textViewDaysUntilDeadline.setTextColor(
                    ContextCompat.getColor(itemView.context, if (emphasizeRemainingDays) R.color.alertRed else R.color.textPrimary))
                itemView.progressBarPercent.progress = percentSaved.toInt()
                itemView.textViewSavingsProgressPercent.text = itemView.context.resources.getString(R.string.goal_saved_percent, percentSaved.toInt())
            }
        }
    }
}
