package com.fibelatti.pigbank.presentation.goals

import android.content.Context
import android.os.Bundle
import com.fibelatti.pigbank.App
import com.fibelatti.pigbank.R
import com.fibelatti.pigbank.presentation.base.BaseActivity
import com.fibelatti.pigbank.presentation.base.BaseIntentBuilder
import com.fibelatti.pigbank.presentation.common.ObservableView
import com.fibelatti.pigbank.presentation.goaldetail.GoalDetailActivity
import com.fibelatti.pigbank.presentation.models.Goal
import com.fibelatti.pigbank.presentation.preferences.PreferencesActivity
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
    //endregion

    //region Private properties
    private val preferencesObservableView = ObservableView<Unit>()
    private val addGoalObservableView = ObservableView<Unit>()
    //endregion

    //region Override properties
    //endregion

    //region Override Lifecycle methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals)
        App.instance.instantiateGoalsComponent(activity = this)
    }

    override fun onResume() {
        super.onResume()
        presenter.bind(this)
    }

    override fun onPause() {
        presenter.unbind()
        super.onPause()
    }

    override fun onDestroy() {
        App.instance.releaseGoalsComponent()
        super.onDestroy()
    }

    //endregion

    //region Override methods
    override fun handleError(errorMessage: String?) {
        TODO("not implemented")
    }

    override fun preferencesClicked(): ObservableView<Unit> = preferencesObservableView

    override fun addGoalClicked(): ObservableView<Unit> = addGoalObservableView

    override fun goalClicked(): ObservableView<Goal> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addSavingsClicked(): ObservableView<Pair<Goal, Float>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun goToPreferences() {
        startActivity(PreferencesActivity.IntentBuilder(this).build())
    }

    override fun createGoal() {
        startActivity(GoalDetailActivity.IntentBuilder(this).build())
    }

    override fun openGoal(goal: Goal) {
        startActivity(GoalDetailActivity.IntentBuilder(this).addGoalExtra(goal).build())
    }

    override fun updateGoals(goals: List<Goal>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    //endregion

    //region Public methods
    //endregion

    //region Private methods
    //endregion
    class IntentBuilder(context: Context) : BaseIntentBuilder<GoalsActivity>(context, GoalsActivity::class.java)
}
