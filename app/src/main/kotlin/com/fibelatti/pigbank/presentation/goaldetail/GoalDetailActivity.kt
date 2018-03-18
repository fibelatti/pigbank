package com.fibelatti.pigbank.presentation.goaldetail

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import com.fibelatti.pigbank.R
import com.fibelatti.pigbank.common.asString
import com.fibelatti.pigbank.common.ifNotNullThisElseThat
import com.fibelatti.pigbank.presentation.addsavings.AddSavingsDialogFragment
import com.fibelatti.pigbank.presentation.base.BaseActivity
import com.fibelatti.pigbank.presentation.base.BaseIntentBuilder
import com.fibelatti.pigbank.presentation.common.extensions.animateWithListener
import com.fibelatti.pigbank.presentation.common.extensions.clearError
import com.fibelatti.pigbank.presentation.common.extensions.gone
import com.fibelatti.pigbank.presentation.common.extensions.hideKeyboard
import com.fibelatti.pigbank.presentation.common.extensions.setDateInputMask
import com.fibelatti.pigbank.presentation.common.extensions.setElevated
import com.fibelatti.pigbank.presentation.common.extensions.showError
import com.fibelatti.pigbank.presentation.common.extensions.snackbar
import com.fibelatti.pigbank.presentation.common.extensions.stealFocusOnTouch
import com.fibelatti.pigbank.presentation.common.extensions.textAsString
import com.fibelatti.pigbank.presentation.common.extensions.toast
import com.fibelatti.pigbank.presentation.common.extensions.visible
import com.fibelatti.pigbank.presentation.goaldetail.detail.GoalDetailFragment
import com.fibelatti.pigbank.presentation.goaldetail.savingslog.SavingsLogFragment
import com.fibelatti.pigbank.presentation.models.GoalPresentationModel
import kotlinx.android.synthetic.main.activity_goal_details.animationAchieved
import kotlinx.android.synthetic.main.activity_goal_details.layoutRoot
import kotlinx.android.synthetic.main.activity_goal_details.layoutTabs
import kotlinx.android.synthetic.main.activity_goal_details.layoutViewPager
import kotlinx.android.synthetic.main.layout_confirmation.animationTick
import kotlinx.android.synthetic.main.layout_confirmation.layoutConfirmation
import kotlinx.android.synthetic.main.layout_confirmation.textViewConfirmation
import kotlinx.android.synthetic.main.layout_goal_basic_info.editTextCost
import kotlinx.android.synthetic.main.layout_goal_basic_info.editTextDeadline
import kotlinx.android.synthetic.main.layout_goal_basic_info.editTextDescription
import kotlinx.android.synthetic.main.layout_goal_basic_info.inputLayoutCost
import kotlinx.android.synthetic.main.layout_goal_basic_info.inputLayoutDeadline
import kotlinx.android.synthetic.main.layout_goal_basic_info.inputLayoutDescription
import kotlinx.android.synthetic.main.layout_toolbar_default.toolbar
import javax.inject.Inject

//region Top level declarations
private const val BUNDLE_GOAL = "GOAL"
//endregion

class GoalDetailActivity :
    BaseActivity(),
    GoalDetailContract.View,
    GoalDetailFragment.Callback,
    SavingsLogFragment.Callback,
    AddSavingsDialogFragment.Callback {
    //region Companion objects and interfaces
    companion object {
        val TAG: String = GoalDetailActivity::class.java.simpleName
    }
    //endregion

    //region Public properties
    @Inject
    lateinit var presenter: GoalDetailContract.Presenter
    //endregion

    //region Private properties
    private var goal: GoalPresentationModel? = null
    private var pagerAdapter: GoalDetailsPagerAdapter? = null
    private var goalDetailFragment: GoalDetailFragment? = null
    private var savingsLogFragment: SavingsLogFragment? = null
    //endregion

    //region Override properties
    //endregion

    //region Override Lifecycle methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        goalDetailFragment = GoalDetailFragment.newInstance()
        savingsLogFragment = SavingsLogFragment.newInstance()

        setContentView(R.layout.activity_goal_details)
        setupLayout()

        savedInstanceState.ifNotNullThisElseThat({ restoreFromState(it) }, { parseIntent(intent) })

        presenter.attachView(this)
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

    override fun showGoalDetails(goal: GoalPresentationModel) {
        setGoalCommonDetails(goal)
        goalDetailFragment?.showGoalDetails(goal)
        savingsLogFragment?.setSavings(goal.savings)
    }

    override fun showGoalAchievedDetails(goal: GoalPresentationModel) {
        setGoalCommonDetails(goal)
        goalDetailFragment?.showGoalAchievedDetails()
        savingsLogFragment?.setSavings(goal.savings)
        showAchievedAnimation()
    }

    override fun showGoalOverdueDetails(goal: GoalPresentationModel) {
        setGoalCommonDetails(goal)
        goalDetailFragment?.showGoalOverdueDetails()
        savingsLogFragment?.setSavings(goal.savings)
    }

    override fun showChangesSaved() {
        showConfirmationAnimation(getString(R.string.generic_changes_saved))
    }

    override fun showAddSavingsDialog(goal: GoalPresentationModel) {
        AddSavingsDialogFragment.newInstance(goal).show(supportFragmentManager, AddSavingsDialogFragment.TAG)
    }

    override fun onInvalidDescription(error: String) {
        inputLayoutDescription.showError(error)
    }

    override fun onInvalidCost(error: String) {
        inputLayoutCost.showError(error)
    }

    override fun onInvalidDeadline(error: String) {
        inputLayoutDeadline.showError(error)
    }

    override fun onSaveError() {
        layoutRoot.snackbar(getString(R.string.goal_save_error))
    }

    override fun showDeleteConfirmationDialog(goal: GoalPresentationModel) {
        val alertDialog: AlertDialog = AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.goal_delete_title))
            setMessage(getString(R.string.goal_delete_message, goal.description))
            setPositiveButton(getString(R.string.hint_yes), { _, _ -> presenter.confirmDeletion(goal) })
            setNegativeButton(getString(R.string.hint_cancel), null)
        }.create()

        alertDialog.apply {
            setOnShowListener {
                getButton(DialogInterface.BUTTON_POSITIVE)?.apply {
                    setTextColor(ContextCompat.getColor(context, R.color.colorGray))
                }
            }
        }

        alertDialog.show()
    }

    override fun onGoalDeleted() {
        toast(getString(R.string.goal_delete_success))
        finish()
    }

    override fun onDeleteError() {
        layoutRoot.snackbar(getString(R.string.goal_delete_error))
    }

    override fun onDetailViewReady() {
        goal?.let { presenter.goalSet(it) }
    }

    override fun onSavingsLogViewReady() {
        goal?.savings?.let { savingsLogFragment?.setSavings(it) }
    }

    override fun onSaveToGoalClicked() {
        goal?.let { presenter.addSavings(goal = it) }
    }

    override fun onSavingsAdded(goal: GoalPresentationModel) {
        presenter.goalSet(goal)
    }
    //endregion

    //region Public methods
    //endregion

    //region Private methods
    private fun setupLayout() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_close)
        }
        layoutRoot.stealFocusOnTouch()
        editTextDeadline.setDateInputMask()

        pagerAdapter = GoalDetailsPagerAdapter(supportFragmentManager, goalDetailFragment, savingsLogFragment)
        layoutViewPager.adapter = pagerAdapter
        layoutViewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(layoutTabs))
        layoutTabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(layoutViewPager))
    }

    private fun parseIntent(intent: Intent) {
        goal = intent.getParcelableExtra(BUNDLE_GOAL)
    }

    private fun restoreFromState(savedInstanceState: Bundle) {
        goal = savedInstanceState.getParcelable(BUNDLE_GOAL)
    }

    private fun setGoalCommonDetails(goal: GoalPresentationModel) {
        with(goal) {
            this@GoalDetailActivity.goal = this

            editTextDescription.setText(description)
            editTextCost.setText(cost)
            editTextDeadline.setText(deadline.asString())
        }
    }

    private fun saveGoal() {
        clearErrors()
        goal?.let {
            presenter.saveGoal(
                goal = it,
                description = editTextDescription.textAsString(),
                cost = editTextCost.textAsString(),
                deadline = editTextDeadline.textAsString())
        }
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

    private fun clearErrors() {
        inputLayoutDescription.clearError()
        inputLayoutCost.clearError()
        inputLayoutDeadline.clearError()
    }
    //endregion

    class IntentBuilder(context: Context) : BaseIntentBuilder<GoalDetailActivity>(context, GoalDetailActivity::class.java) {
        fun addGoalExtra(goal: GoalPresentationModel): IntentBuilder {
            intent.putExtra(BUNDLE_GOAL, goal)
            return this
        }
    }
}
