package com.fibelatti.pigbank.presentation.goaldetail.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import com.fibelatti.pigbank.R
import com.fibelatti.pigbank.common.asString
import com.fibelatti.pigbank.presentation.base.BaseDelegateAdapter
import com.fibelatti.pigbank.presentation.base.BaseViewType
import com.fibelatti.pigbank.presentation.common.extensions.inflate
import com.fibelatti.pigbank.presentation.models.Savings
import kotlinx.android.synthetic.main.list_item_savings.view.textViewSavings

const val ADAPTER_ANIMATION_FADE_START = 0F
const val ADAPTER_ANIMATION_FADE_END = 1F
const val ADAPTER_ANIMATION_DURATION = 1000L

class SavingsDelegateAdapter :
    BaseDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = DataViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: BaseViewType) {
        (holder as? DataViewHolder)?.bind(item as? Savings)
    }

    internal inner class DataViewHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(parent.inflate(R.layout.list_item_savings)) {
        fun bind(item: Savings?) = apply {
            item?.apply {
                itemView.textViewSavings.text = itemView.context.getString(R.string.savings_saved, amount, date.asString())
            }
            setFadeAnimation(itemView)
        }
    }

    private fun setFadeAnimation(view: View) {
        val anim = AlphaAnimation(ADAPTER_ANIMATION_FADE_START, ADAPTER_ANIMATION_FADE_END)
        anim.duration = ADAPTER_ANIMATION_DURATION
        view.startAnimation(anim)
    }
}
