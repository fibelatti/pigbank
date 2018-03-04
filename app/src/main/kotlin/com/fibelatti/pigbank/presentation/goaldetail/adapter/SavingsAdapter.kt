package com.fibelatti.pigbank.presentation.goaldetail.adapter

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.fibelatti.pigbank.presentation.base.BaseDelegateAdapter
import com.fibelatti.pigbank.presentation.base.BaseViewType
import javax.inject.Inject

interface ViewType : BaseViewType {
    companion object {
        const val SAVINGS = 0
    }
}

class SavingsAdapter @Inject constructor(
    savingsDelegateAdapter: SavingsDelegateAdapter
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: MutableList<BaseViewType> = ArrayList()

    private val delegateAdapters = SparseArrayCompat<BaseDelegateAdapter>()

    init {
        delegateAdapters.put(ViewType.SAVINGS, savingsDelegateAdapter)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        delegateAdapters[viewType].onCreateViewHolder(parent)

    override fun getItemViewType(position: Int): Int = items[position].getViewType()

    fun addManyToList(listItems: List<BaseViewType>) {
        items.clear()
        items.addAll(listItems)
        notifyDataSetChanged()
    }
}
