package com.fibelatti.pigbank.presentation.goals.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.fibelatti.pigbank.R
import com.fibelatti.pigbank.presentation.base.BaseDelegateAdapter
import com.fibelatti.pigbank.presentation.base.BaseViewType
import com.fibelatti.pigbank.presentation.common.inflate

class LoadingDelegateAdapter : BaseDelegateAdapter {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = ViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: BaseViewType) {
        (holder as? ViewHolder)?.bind()
    }

    internal inner class ViewHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(parent.inflate(R.layout.list_item_loading)) {
        fun bind() = with(itemView) {}
    }
}
