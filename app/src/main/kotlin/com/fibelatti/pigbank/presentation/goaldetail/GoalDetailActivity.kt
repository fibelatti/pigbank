package com.fibelatti.pigbank.presentation.goaldetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.fibelatti.pigbank.R
import com.fibelatti.pigbank.common.ifNotNullThisElseThat
import com.fibelatti.pigbank.presentation.base.BaseActivity
import com.fibelatti.pigbank.presentation.base.BaseIntentBuilder
import com.fibelatti.pigbank.presentation.common.ObservableView
import com.fibelatti.pigbank.presentation.common.extensions.toast
import com.fibelatti.pigbank.presentation.goaldetail.adapter.SavingsAdapter
import com.fibelatti.pigbank.presentation.models.Goal
import kotlinx.android.synthetic.main.activity_goal_details.buttonSaveToGoal
import kotlinx.android.synthetic.main.activity_goal_details.datePickerDeadline
import kotlinx.android.synthetic.main.activity_goal_details.editTextCost
import kotlinx.android.synthetic.main.activity_goal_details.editTextDeadline
import kotlinx.android.synthetic.main.activity_goal_details.editTextDescription
import kotlinx.android.synthetic.main.activity_goal_details.recyclerViewSavings
import kotlinx.android.synthetic.main.activity_goal_details.textViewDaysUntilDeadline
import kotlinx.android.synthetic.main.activity_goal_details.textViewSavingsPerDay
import kotlinx.android.synthetic.main.activity_goal_details.textViewSavingsPerMonth
import kotlinx.android.synthetic.main.activity_goal_details.textViewSavingsPerWeek
import kotlinx.android.synthetic.main.activity_goal_details.textViewTotalSaved
import kotlinx.android.synthetic.main.layout_toolbar_default.toolbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar
import javax.inject.Inject

//region Top level declarations
//endregion

class GoalDetailActivity :
    BaseActivity(),
    GoalDetailContract.View {
    //region Companion objects and interfaces
    companion object {
        val TAG: String = GoalDetailActivity::class.java.simpleName
        private const val BUNDLE_GOAL = "GOAL"
        private const val DATE_PICKER_ELEVATION = 20F
    }
    //endregion

    //region Public properties
    @Inject
    lateinit var presenter: GoalDetailContract.Presenter
    @Inject
    lateinit var adapter: SavingsAdapter
    //endregion

    //region Private properties
    lateinit var goal: Goal
    //endregion

    //region Override properties
    override val detailViewResumed = ObservableView<Goal>()
    override val goalDeadlineClicked = ObservableView<Unit>()
    override val addSavingsToGoalClicked = ObservableView<Goal>()
    override val addSavingsToGoal = ObservableView<Pair<Goal, Float>>()
    override val saveGoalClicked = ObservableView<Goal>()
    override val deleteGoalClicked = ObservableView<Goal>()
    override val deleteGoalConfirmed = ObservableView<Goal>()
    //endregion

    //region Override Lifecycle methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_details)
        setUpLayout()
        setupRecyclerView()
        savedInstanceState.ifNotNullThisElseThat({ restoreFromState(it) }, { parseIntent(intent) })
    }

    override fun onResume() {
        super.onResume()
        presenter.bind(this)
        detailViewResumed.emitNext(goal)
    }

    override fun onPause() {
        presenter.unbind()
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(BUNDLE_GOAL, goal)
    }
    //endregion

    //region Override methods
    override fun handleError(errorMessage: String?) {
        toast(errorMessage ?: getString(R.string.generic_msg_error))
    }

    override fun showGoalDetails(goal: Goal) {
        with(goal) {
            val calendar = GregorianCalendar().apply {
                time = goal.deadline
            }
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

            editTextDescription.setText(description)
            editTextCost.setText(String.format("%.2f", cost))
            editTextDeadline.setText(simpleDateFormat.format(calendar.time))
            textViewDaysUntilDeadline.text = resources.getQuantityString(R.plurals.goal_deadline_remaining, daysUntilDeadline.toInt(), daysUntilDeadline)
            textViewTotalSaved.text = getString(R.string.goal_total_saved, totalSaved)
            textViewSavingsPerDay.text = getString(R.string.goal_savings_per_day, suggestedSavingsPerDay)
            textViewSavingsPerWeek.visibility = if (suggestedSavingsPerWeek > 0) View.VISIBLE else View.GONE
            textViewSavingsPerWeek.text = getString(R.string.goal_savings_per_week, suggestedSavingsPerWeek)
            textViewSavingsPerMonth.visibility = if (suggestedSavingsPerMonth > 0) View.VISIBLE else View.GONE
            textViewSavingsPerMonth.text = getString(R.string.goal_savings_per_month, suggestedSavingsPerMonth)
            adapter.addManyToList(savings)
        }
    }

    override fun showDatePicker() {
        val calendar = GregorianCalendar().apply {
            time = goal.deadline
        }

        datePickerDeadline.visibility = View.VISIBLE
        datePickerDeadline.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)) { _, year, month, dayOfMonth ->
            editTextDeadline.setText("$dayOfMonth/$month/$year")
            datePickerDeadline.visibility = View.GONE
        }
        ViewCompat.setTranslationZ(datePickerDeadline, DATE_PICKER_ELEVATION)
    }

    override fun showAddSavingsDialog(goal: Goal) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onGoalSaved(goal: Goal) {
        showGoalDetails(goal)
    }

    override fun onSaveError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showDeleteConfirmationDialog(goal: Goal) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onGoalDeleted() {
        finish()
    }

    override fun onDeleteError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    //endregion

    //region Public methods
    //endregion

    //region Private methods
    private fun setUpLayout() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
        }
        editTextDeadline.setOnClickListener {
            layoutRoot.hideKeyboard()
            goalDeadlineClicked.emitNext(Unit)
        }
        buttonSaveToGoal.setOnClickListener {
            layoutRoot.hideKeyboard()
            addSavingsToGoalClicked.emitNext(goal)
        }
    }

    private fun setupRecyclerView() {
        recyclerViewSavings.adapter = adapter
        recyclerViewSavings.layoutManager = LinearLayoutManager(this)
    }

    private fun parseIntent(intent: Intent) {
        goal = intent.getParcelableExtra(BUNDLE_GOAL)
    }

    private fun restoreFromState(savedInstanceState: Bundle) {
        goal = savedInstanceState.getParcelable(BUNDLE_GOAL)
    }
    //endregion

    class IntentBuilder(context: Context) : BaseIntentBuilder<GoalDetailActivity>(context, GoalDetailActivity::class.java) {
        fun addGoalExtra(goal: Goal): IntentBuilder {
            intent.putExtra(BUNDLE_GOAL, goal)
            return this
        }
    }
}
