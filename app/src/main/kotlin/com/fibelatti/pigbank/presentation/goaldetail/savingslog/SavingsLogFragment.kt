package com.fibelatti.pigbank.presentation.goaldetail.savingslog

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fibelatti.pigbank.R
import com.fibelatti.pigbank.presentation.base.BaseFragment
import com.fibelatti.pigbank.presentation.goaldetail.savingslog.adapter.SavingsAdapter
import com.fibelatti.pigbank.presentation.models.SavingsPresentationModel
import kotlinx.android.synthetic.main.fragment_goal_savings_log.recyclerViewSavings
import javax.inject.Inject

class SavingsLogFragment :
    BaseFragment() {

    //region Companion objects and interfaces
    companion object {
        val TAG: String = SavingsLogFragment::class.java.simpleName

        fun newInstance(): SavingsLogFragment = SavingsLogFragment()
    }

    interface Callback {
        fun onSavingsLogViewReady()
    }
    //endregion

    //region Public properties
    @Inject
    lateinit var adapter: SavingsAdapter
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
    ): View? = inflater.inflate(R.layout.fragment_goal_savings_log, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Callback) {
            callback = context
            callback?.onSavingsLogViewReady()
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
    fun setSavings(savings: List<SavingsPresentationModel>) {
        adapter.addManyToList(savings)
    }
    //endregion

    //region Private methods
    private fun initView() {
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        recyclerViewSavings.adapter = adapter
        recyclerViewSavings.layoutManager = LinearLayoutManager(context)
    }
    //endregion
}
