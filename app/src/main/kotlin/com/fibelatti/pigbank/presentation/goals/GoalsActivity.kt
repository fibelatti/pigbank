package com.fibelatti.pigbank.presentation.goals

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.fibelatti.pigbank.R
import com.fibelatti.pigbank.presentation.addgoal.AddGoalDialogFragment
import com.fibelatti.pigbank.presentation.addsavings.AddSavingsDialogFragment
import com.fibelatti.pigbank.presentation.base.BaseActivity
import com.fibelatti.pigbank.presentation.base.BaseIntentBuilder
import com.fibelatti.pigbank.presentation.common.LinearLayoutManagerOffsetDecoration
import com.fibelatti.pigbank.presentation.common.extensions.toast
import com.fibelatti.pigbank.presentation.goaldetail.GoalDetailActivity
import com.fibelatti.pigbank.presentation.goals.adapter.GoalsAdapter
import com.fibelatti.pigbank.presentation.models.Goal
import com.fibelatti.pigbank.presentation.preferences.PreferencesActivity
import kotlinx.android.synthetic.main.activity_goals.buttonAddGoal
import kotlinx.android.synthetic.main.activity_goals.recyclerViewGoals
import kotlinx.android.synthetic.main.layout_toolbar_default.toolbar
import javax.inject.Inject

class GoalsActivity :
    BaseActivity(),
    GoalsContract.View,
    AddGoalDialogFragment.Callback,
    AddSavingsDialogFragment.Callback,
    GoalsAdapter.Callback {
    //region Companion objects and interfaces
    companion object {
        val TAG: String = GoalsActivity::class.java.simpleName
    }
    //endregion

    //region Public properties
    @Inject
    lateinit var presenter: GoalsContract.Presenter
    @Inject
    lateinit var adapter: GoalsAdapter
    //endregion

    //region Private properties
    //endregion

    //region Override properties
    //endregion

    //region Override Lifecycle methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals)

        setupLayout()
        setupRecyclerView()
        presenter.attachView(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.goalsUpdated()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    //endregion

    //region Override methods
    override fun handleError(errorMessage: String?) {
        toast(errorMessage ?: getString(R.string.generic_msg_error))
    }

    override fun goToPreferences() {
        startActivity(PreferencesActivity.IntentBuilder(this).build())
    }

    override fun createGoal() {
        val addGoalDialogFragment = AddGoalDialogFragment()
        addGoalDialogFragment.show(fragmentManager, AddGoalDialogFragment.TAG)
    }

    override fun openGoal(goal: Goal) {
        startActivity(GoalDetailActivity.IntentBuilder(this).addGoalExtra(goal).build())
    }

    override fun showAddSavingsDialog(goal: Goal) {
        val addSavingsDialogFragment = AddSavingsDialogFragment.newInstance(goal)
        addSavingsDialogFragment.show(fragmentManager, AddGoalDialogFragment.TAG)
    }

    override fun updateGoals(goals: List<Goal>) {
        adapter.addManyToList(goals)
    }

    override fun onGoalCreated(goal: Goal) {
        presenter.newGoalAdded(goal)
    }

    override fun onSavingsAdded(goal: Goal) {
        presenter.goalsUpdated()
    }

    override fun goalClicked(goal: Goal) {
        presenter.goalDetails(goal)
    }

    override fun saveToGoalClicked(goal: Goal) {
        presenter.addSavings(goal)
    }
    //endregion

    //region Public methods
    //endregion

    //region Private methods
    private fun setupLayout() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = getString(R.string.goal_title)
        }
        buttonAddGoal.setOnClickListener { presenter.addGoal() }
    }

    private fun setupRecyclerView() {
        recyclerViewGoals.addItemDecoration(LinearLayoutManagerOffsetDecoration(recyclerViewGoals.context, R.dimen.margin_small))
        recyclerViewGoals.adapter = adapter
        recyclerViewGoals.layoutManager = LinearLayoutManager(this)

        adapter.callback = this
    }

    //endregion
    class IntentBuilder(context: Context) : BaseIntentBuilder<GoalsActivity>(context, GoalsActivity::class.java)
}
