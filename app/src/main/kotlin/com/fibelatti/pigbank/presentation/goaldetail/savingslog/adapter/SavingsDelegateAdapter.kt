package com.fibelatti.pigbank.presentation.goaldetail.savingslog.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import com.fibelatti.pigbank.R
import com.fibelatti.pigbank.common.asString
import com.fibelatti.pigbank.presentation.base.BaseDelegateAdapter
import com.fibelatti.pigbank.presentation.base.BaseViewType
import com.fibelatti.pigbank.presentation.common.extensions.inflate
import com.fibelatti.pigbank.presentation.models.SavingsPresentationModel
import kotlinx.android.synthetic.main.list_item_savings.view.imageViewIcon
import kotlinx.android.synthetic.main.list_item_savings.view.textViewSavings
import javax.inject.Inject

const val ADAPTER_ANIMATION_FADE_START = 0F
const val ADAPTER_ANIMATION_FADE_END = 1F
const val ADAPTER_ANIMATION_DURATION = 1000L

class SavingsDelegateAdapter @Inject constructor() :
    BaseDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = DataViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: BaseViewType) {
        (holder as? DataViewHolder)?.bind(item as? SavingsPresentationModel)
    }

    internal inner class DataViewHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(parent.inflate(R.layout.list_item_savings)) {
        fun bind(item: SavingsPresentationModel?) = apply {
            item?.apply {
                itemView.imageViewIcon.setImageResource(
                    if (!isRemoval) {
                        R.drawable.ic_coin
                    } else {
                        R.drawable.ic_coin_removed
                    })

                itemView.textViewSavings.text = itemView.context.getString(
                    if (!isRemoval) {
                        R.string.savings_saved
                    } else {
                        R.string.savings_removed
                    }, amount, date.asString())
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
