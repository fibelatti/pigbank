package com.fibelatti.pigbank.presentation.goals

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.fibelatti.pigbank.R
import com.fibelatti.pigbank.presentation.base.BaseActivity
import com.fibelatti.pigbank.presentation.base.BaseIntentBuilder
import com.fibelatti.pigbank.presentation.common.ItemOffsetDecoration
import com.fibelatti.pigbank.presentation.common.ObservableView
import com.fibelatti.pigbank.presentation.common.toast
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
    GoalsContract.View {
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
    private val preferencesObservableView = ObservableView<Unit>()
    private val addGoalObservableView = ObservableView<Unit>()
    private val addSavingsToGoal = ObservableView<Pair<Goal, Float>>()
    //endregion

    //region Override properties
    //endregion

    //region Override Lifecycle methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals)

        setUpLayout()
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        presenter.bind(this)
    }

    override fun onPause() {
        presenter.unbind()
        super.onPause()
    }
    //endregion

    //region Override methods
    override fun handleError(errorMessage: String?) {
        toast(errorMessage ?: getString(R.string.generic_msg_error))
    }

    override fun preferencesClicked(): ObservableView<Unit> = preferencesObservableView

    override fun addGoalClicked(): ObservableView<Unit> = addGoalObservableView

    override fun goalClicked(): ObservableView<Goal> = adapter.onGoalClicked()

    override fun addSavingsClicked(): ObservableView<Goal> = adapter.onSaveToGoalClicked()

    override fun addSavingsToGoal(): ObservableView<Pair<Goal, Float>> = addSavingsToGoal

    override fun goToPreferences() {
        startActivity(PreferencesActivity.IntentBuilder(this).build())
    }

    override fun createGoal() {
        startActivity(GoalDetailActivity.IntentBuilder(this).build())
    }

    override fun openGoal(goal: Goal) {
        startActivity(GoalDetailActivity.IntentBuilder(this).addGoalExtra(goal).build())
    }

    override fun showAddSavingsDialog(goal: Goal) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateGoals(goals: List<Goal>) {
        adapter.addManyToList(goals)
    }
    //endregion

    //region Public methods
    //endregion

    //region Private methods
    private fun setUpLayout() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = getString(R.string.goal_title)
        }
        buttonAddGoal.setOnClickListener { addGoalObservableView.emitNext(Unit) }
    }

    private fun setupRecyclerView() {
        recyclerViewGoals.addItemDecoration(ItemOffsetDecoration(recyclerViewGoals.context, R.dimen.margin_smaller))
        recyclerViewGoals.adapter = adapter
        recyclerViewGoals.layoutManager = LinearLayoutManager(this)
    }

    //endregion
    class IntentBuilder(context: Context) : BaseIntentBuilder<GoalsActivity>(context, GoalsActivity::class.java)
}
