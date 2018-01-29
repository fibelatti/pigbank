package com.fibelatti.pigbank.presentation.goals.adapter

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.fibelatti.pigbank.presentation.base.BaseDelegateAdapter
import com.fibelatti.pigbank.presentation.base.BaseViewType
import com.fibelatti.pigbank.presentation.common.ObservableView
import com.fibelatti.pigbank.presentation.models.Goal
import javax.inject.Inject

interface ViewType : BaseViewType {
    companion object {
        const val LOADING = 0
        const val GOAL = 1
    }
}

class GoalsAdapter @Inject constructor(
    private val goalsDelegateAdapter: GoalsDelegateAdapter,
    private val loadingDelegateAdapter: LoadingDelegateAdapter
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items: MutableList<BaseViewType> = ArrayList()

    private val delegateAdapters = SparseArrayCompat<BaseDelegateAdapter>()

    init {
        delegateAdapters.put(ViewType.LOADING, loadingDelegateAdapter)
        delegateAdapters.put(ViewType.GOAL, goalsDelegateAdapter)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        delegateAdapters[viewType].onCreateViewHolder(parent)

    override fun getItemViewType(position: Int): Int = items[position].getViewType()

    fun onGoalClicked(): ObservableView<Goal> = goalsDelegateAdapter.itemClickObservable

    fun onSaveToGoalClicked(): ObservableView<Goal> = goalsDelegateAdapter.saveToGoalClickObservable

    fun addManyToList(listItems: List<BaseViewType>) {
        items.clear()
        items.addAll(listItems)
        notifyDataSetChanged()
    }
}
