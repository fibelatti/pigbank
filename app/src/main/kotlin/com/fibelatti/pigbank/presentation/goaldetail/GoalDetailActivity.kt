package com.fibelatti.pigbank.presentation.goaldetail

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.fibelatti.pigbank.R
import com.fibelatti.pigbank.R.color
import com.fibelatti.pigbank.common.asString
import com.fibelatti.pigbank.common.ifNotNullThisElseThat
import com.fibelatti.pigbank.common.intPartsAsDate
import com.fibelatti.pigbank.common.toFormattedString
import com.fibelatti.pigbank.presentation.addsavings.AddSavingsDialogFragment
import com.fibelatti.pigbank.presentation.base.BaseActivity
import com.fibelatti.pigbank.presentation.base.BaseIntentBuilder
import com.fibelatti.pigbank.presentation.common.extensions.alert
import com.fibelatti.pigbank.presentation.common.extensions.animateWithListener
import com.fibelatti.pigbank.presentation.common.extensions.gone
import com.fibelatti.pigbank.presentation.common.extensions.hideKeyboard
import com.fibelatti.pigbank.presentation.common.extensions.negativeButton
import com.fibelatti.pigbank.presentation.common.extensions.positiveButton
import com.fibelatti.pigbank.presentation.common.extensions.setElevated
import com.fibelatti.pigbank.presentation.common.extensions.showListener
import com.fibelatti.pigbank.presentation.common.extensions.snackbar
import com.fibelatti.pigbank.presentation.common.extensions.textAsString
import com.fibelatti.pigbank.presentation.common.extensions.toast
import com.fibelatti.pigbank.presentation.common.extensions.updateNegativeButton
import com.fibelatti.pigbank.presentation.common.extensions.updatePositiveButton
import com.fibelatti.pigbank.presentation.common.extensions.visible
import com.fibelatti.pigbank.presentation.goaldetail.adapter.SavingsAdapter
import com.fibelatti.pigbank.presentation.models.Goal
import com.fibelatti.pigbank.presentation.models.GoalCandidate
import kotlinx.android.synthetic.main.activity_goal_details.animationAchieved
import kotlinx.android.synthetic.main.activity_goal_details.datePickerDeadline
import kotlinx.android.synthetic.main.activity_goal_details.layoutAchieved
import kotlinx.android.synthetic.main.activity_goal_details.layoutOverdue
import kotlinx.android.synthetic.main.activity_goal_details.layoutRoot
import kotlinx.android.synthetic.main.activity_goal_details.layoutSummary
import kotlinx.android.synthetic.main.activity_goal_details.recyclerViewSavings
import kotlinx.android.synthetic.main.layout_confirmation.animationTick
import kotlinx.android.synthetic.main.layout_confirmation.layoutConfirmation
import kotlinx.android.synthetic.main.layout_confirmation.textViewConfirmation
import kotlinx.android.synthetic.main.layout_goal_basic_info.editTextCost
import kotlinx.android.synthetic.main.layout_goal_basic_info.editTextDeadline
import kotlinx.android.synthetic.main.layout_goal_basic_info.editTextDescription
import kotlinx.android.synthetic.main.layout_goal_summary.buttonSaveToGoal
import kotlinx.android.synthetic.main.layout_goal_summary.textViewDaysUntilDeadline
import kotlinx.android.synthetic.main.layout_goal_summary.textViewSavingsPerDay
import kotlinx.android.synthetic.main.layout_goal_summary.textViewSavingsPerMonth
import kotlinx.android.synthetic.main.layout_goal_summary.textViewSavingsPerWeek
import kotlinx.android.synthetic.main.layout_goal_summary.textViewTotalSaved
import kotlinx.android.synthetic.main.layout_toolbar_default.toolbar
import java.util.Calendar
import javax.inject.Inject

//region Top level declarations
private const val BUNDLE_GOAL = "GOAL"
//endregion

class GoalDetailActivity :
    BaseActivity(),
    GoalDetailContract.View,
    AddSavingsDialogFragment.Callback {
    //region Companion objects and interfaces
    companion object {
        val TAG: String = GoalDetailActivity::class.java.simpleName
    }
    //endregion

    //region Public properties
    @Inject
    lateinit var presenter: GoalDetailContract.Presenter
    @Inject
    lateinit var adapter: SavingsAdapter
    //endregion

    //region Private properties
    private var goal: Goal? = null
    private var calendar = Calendar.getInstance()
    //endregion

    //region Override properties
    //endregion

    //region Override Lifecycle methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_details)
        setUpLayout()
        setupRecyclerView()
        savedInstanceState.ifNotNullThisElseThat({ restoreFromState(it) }, { parseIntent(intent) })

        presenter.attachView(this)
        goal?.let { presenter.goalSet(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(BUNDLE_GOAL, goal)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_goal_details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuItemSave -> {
                layoutRoot.hideKeyboard()
                saveGoal()
                return true
            }
            R.id.menuItemDelete -> {
                layoutRoot.hideKeyboard()
                goal?.let { presenter.deleteGoal(it) }
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

    override fun showGoalDetails(goal: Goal) {
        setGoalCommonDetails(goal)

        with(goal) {
            textViewDaysUntilDeadline.text = resources.getQuantityString(R.plurals.goal_deadline_remaining, daysUntilDeadline.toInt(), daysUntilDeadline)
            textViewTotalSaved.text = getString(R.string.goal_total_saved, totalSaved)
            textViewSavingsPerDay.text = getString(R.string.goal_savings_per_day, suggestedSavingsPerDay)
            if (suggestedSavingsPerWeek > 0) {
                textViewSavingsPerWeek.visible()
                textViewSavingsPerWeek.text = getString(R.string.goal_savings_per_week, suggestedSavingsPerWeek)
            } else {
                textViewSavingsPerWeek.gone()
            }
            if (suggestedSavingsPerMonth > 0) {
                textViewSavingsPerMonth.visible()
                textViewSavingsPerMonth.text = getString(R.string.goal_savings_per_month, suggestedSavingsPerMonth)
            } else {
                textViewSavingsPerMonth.gone()
            }
        }

        layoutSummary.visible()
        layoutAchieved.gone()
        layoutOverdue.gone()
    }

    override fun showGoalAchievedDetails(goal: Goal) {
        setGoalCommonDetails(goal)
        layoutSummary.gone()
        layoutOverdue.gone()
        layoutAchieved.visible()
        showAchievedAnimation()
    }

    override fun showGoalOverdueDetails(goal: Goal) {
        setGoalCommonDetails(goal)
        layoutSummary.gone()
        layoutAchieved.gone()
        layoutOverdue.visible()
    }

    override fun showChangesSaved() {
        showConfirmationAnimation(getString(R.string.generic_changes_saved))
    }

    override fun showDatePicker() {
        datePickerDeadline.visible()
        datePickerDeadline.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)) { _, year, month, dayOfMonth ->
            val newDate = intPartsAsDate(year, month, dayOfMonth)
            calendar.time = newDate
            editTextDeadline.setText(newDate.asString())
            datePickerDeadline.gone()
        }
        datePickerDeadline.setElevated()
    }

    override fun showAddSavingsDialog(goal: Goal) {
        AddSavingsDialogFragment.newInstance(goal).show(fragmentManager, AddSavingsDialogFragment.TAG)
    }

    override fun onSaveError() {
        layoutRoot.snackbar(getString(R.string.goal_save_error))
    }

    override fun showDeleteConfirmationDialog(goal: Goal) {
        val dialog = alert(dialogTitle = getString(R.string.goal_delete_title),
            dialogMessage = getString(R.string.goal_delete_message, goal.description))
            .positiveButton(
                buttonText = getString(R.string.hint_yes),
                onClickListener = DialogInterface.OnClickListener { _, _ ->
                    presenter.confirmDeletion(goal)
                }
            )
            .negativeButton(buttonText = getString(R.string.hint_cancel))
            .showListener(DialogInterface.OnShowListener { dialogInstance ->
                (dialogInstance as? AlertDialog)?.apply {
                    updatePositiveButton(ContextCompat.getColor(context, color.colorGray))
                    updateNegativeButton(ContextCompat.getColor(context, color.colorAccent))
                }
            })

        dialog.show()
    }

    override fun onGoalDeleted() {
        toast(getString(R.string.goal_delete_success))
        finish()
    }

    override fun onDeleteError() {
        layoutRoot.snackbar(getString(R.string.goal_delete_error))
    }

    override fun onSavingsAdded(goal: Goal) {
        presenter.goalSet(goal)
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
            presenter.editDeadline()
        }
        buttonSaveToGoal.setOnClickListener {
            layoutRoot.hideKeyboard()
            goal?.let { presenter.addSavings(goal = it) }
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

    private fun setGoalCommonDetails(goal: Goal) {
        with(goal) {
            this@GoalDetailActivity.goal = this

            calendar.time = goal.deadline

            editTextDescription.setText(description)
            editTextCost.setText(cost.toFormattedString())
            editTextDeadline.setText(calendar.time.asString())

            adapter.addManyToList(savings)
        }
    }

    private fun saveGoal() {
        val goalCandidate = GoalCandidate(
            description = editTextDescription.textAsString(),
            cost = editTextCost.textAsString(),
            deadline = editTextDeadline.textAsString())

        goal?.let { presenter.saveGoal(it, goalCandidate) }
    }

    private fun showConfirmationAnimation(confirmationText: String) {
        textViewConfirmation.text = confirmationText

        animationTick.animateWithListener(
            onAnimationStart = {
                layoutConfirmation.visible()
                layoutConfirmation.setElevated()
            },
            onAnimationEnd = {
                layoutConfirmation.gone()
            }
        )
    }

    private fun showAchievedAnimation() {
        animationAchieved.animateWithListener(
            onAnimationStart = {
                animationAchieved.visible()
                animationAchieved.setElevated()
            },
            onAnimationEnd = {
                animationAchieved.gone()
            })
    }
    //endregion

    class IntentBuilder(context: Context) : BaseIntentBuilder<GoalDetailActivity>(context, GoalDetailActivity::class.java) {
        fun addGoalExtra(goal: Goal): IntentBuilder {
            intent.putExtra(BUNDLE_GOAL, goal)
            return this
        }
    }
}
