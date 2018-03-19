package com.fibelatti.pigbank.presentation.goals

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.fibelatti.pigbank.R
import com.fibelatti.pigbank.presentation.addgoal.AddGoalDialogFragment
import com.fibelatti.pigbank.presentation.savings.AddSavingsDialogFragment
import com.fibelatti.pigbank.presentation.base.BaseActivity
import com.fibelatti.pigbank.presentation.base.BaseIntentBuilder
import com.fibelatti.pigbank.presentation.common.LinearLayoutManagerOffsetDecoration
import com.fibelatti.pigbank.presentation.common.extensions.setActionBar
import com.fibelatti.pigbank.presentation.common.extensions.toast
import com.fibelatti.pigbank.presentation.goaldetail.GoalDetailActivity
import com.fibelatti.pigbank.presentation.goals.adapter.GoalsAdapter
import com.fibelatti.pigbank.presentation.models.GoalPresentationModel
import com.fibelatti.pigbank.presentation.preferences.PreferencesActivity
import kotlinx.android.synthetic.main.activity_goals.buttonAddGoal
import kotlinx.android.synthetic.main.activity_goals.layoutHintContainer
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
    private var firstGoalHintShowing = false
    private var quickSaveHintShowing = false
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_goals, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuItemPreferences -> {
                presenter.preferences()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
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
        addGoalDialogFragment.show(supportFragmentManager, AddGoalDialogFragment.TAG)
    }

    override fun openGoal(goal: GoalPresentationModel) {
        startActivity(GoalDetailActivity.IntentBuilder(this).addGoalExtra(goal).build())
    }

    override fun showAddSavingsDialog(goal: GoalPresentationModel) {
        val addSavingsDialogFragment = AddSavingsDialogFragment.newInstance(goal)
        addSavingsDialogFragment.show(supportFragmentManager, AddGoalDialogFragment.TAG)
    }

    override fun updateGoals(goals: List<GoalPresentationModel>) {
        adapter.addManyToList(goals)
    }

    override fun onGoalCreated(goal: GoalPresentationModel) {
        presenter.newGoalAdded(goal)
    }

    override fun onSavingsAdded(goal: GoalPresentationModel) {
        presenter.goalsUpdated()
    }

    override fun goalClicked(goal: GoalPresentationModel) {
        presenter.goalDetails(goal)
    }

    override fun saveToGoalClicked(goal: GoalPresentationModel) {
        presenter.addSavings(goal)
    }

    override fun showFirstGoalHint() {
        if (!firstGoalHintShowing) {
            showDismissibleHint(
                container = layoutHintContainer,
                hintTitle = getString(R.string.goal_create_first_hint_title),
                hintMessage = getString(R.string.goal_create_first_hint),
                dismissListener = { presenter.firstGoalHintDismissed() }
            )
            firstGoalHintShowing = true
        }
    }

    override fun showQuickSaveHint() {
        if (!quickSaveHintShowing) {
            showDismissibleHint(
                container = layoutHintContainer,
                hintMessage = getString(R.string.goal_quick_save_hint),
                dismissListener = { presenter.quickSaveHintDismissed() }
            )
            quickSaveHintShowing = true
        }
    }
    //endregion

    //region Public methods
    //endregion

    //region Private methods
    private fun setupLayout() {
        setActionBar(toolbar, getString(R.string.goal_title))
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
