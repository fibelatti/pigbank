package com.fibelatti.pigbank.presentation.addgoal

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import com.fibelatti.pigbank.R
import com.fibelatti.pigbank.R.color
import com.fibelatti.pigbank.presentation.base.BaseDialogFragment
import com.fibelatti.pigbank.presentation.common.DecimalDigitsInputFilter
import com.fibelatti.pigbank.presentation.common.extensions.clearError
import com.fibelatti.pigbank.presentation.common.extensions.hideKeyboard
import com.fibelatti.pigbank.presentation.common.extensions.setDateInputMask
import com.fibelatti.pigbank.presentation.common.extensions.showError
import com.fibelatti.pigbank.presentation.common.extensions.stealFocusOnTouch
import com.fibelatti.pigbank.presentation.common.extensions.textAsString
import com.fibelatti.pigbank.presentation.common.extensions.toast
import com.fibelatti.pigbank.presentation.models.GoalPresentationModel
import kotlinx.android.synthetic.main.dialog_add_goal.layoutRoot
import kotlinx.android.synthetic.main.layout_goal_basic_info.editTextCost
import kotlinx.android.synthetic.main.layout_goal_basic_info.editTextDeadline
import kotlinx.android.synthetic.main.layout_goal_basic_info.editTextDescription
import kotlinx.android.synthetic.main.layout_goal_basic_info.inputLayoutCost
import kotlinx.android.synthetic.main.layout_goal_basic_info.inputLayoutDeadline
import kotlinx.android.synthetic.main.layout_goal_basic_info.inputLayoutDescription
import javax.inject.Inject

private const val BUNDLE_GOAL_DESCRIPTION = "GOAL_DESCRIPTION"
private const val BUNDLE_GOAL_COST = "GOAL_COST"
private const val BUNDLE_GOAL_DEADLINE = "GOAL_DEADLINE"

class AddGoalDialogFragment :
    BaseDialogFragment(),
    AddGoalContract.View {
    //region Companion objects and interfaces
    companion object {
        val TAG: String = AddGoalDialogFragment::class.java.simpleName
    }

    interface Callback {
        fun onGoalCreated(goal: GoalPresentationModel)
    }
    //endregion

    //region Public properties
    @Inject
    lateinit var presenter: AddGoalContract.Presenter
    //endregion

    //region Private properties
    private var callback: Callback? = null
    //endregion

    //region Override properties
    //endregion

    //region Override Lifecycle methods
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = activity?.let {
        val view = View.inflate(it, R.layout.dialog_add_goal, null)

        restoreInstance(savedInstanceState)

        return@let AlertDialog.Builder(it).apply {
            setView(view)
            setTitle(getString(R.string.goal_add_title))
            setPositiveButton(getString(R.string.hint_done), null)
            setNegativeButton(getString(R.string.hint_cancel), null)
            setCancelable(false)
        }.create()
    } ?: super.onCreateDialog(savedInstanceState)

    override fun onStart() {
        super.onStart()
        setupView()

        (dialog as? AlertDialog)?.apply {
            setCanceledOnTouchOutside(false)
            getButton(DialogInterface.BUTTON_POSITIVE)?.apply {
                setOnClickListener({ _ ->
                    clearErrors()
                    presenter.createGoal(
                        dialog.editTextDescription.textAsString(),
                        dialog.editTextCost.textAsString(),
                        dialog.editTextDeadline.textAsString())
                })
            }
            getButton(DialogInterface.BUTTON_NEGATIVE)?.apply {
                setTextColor(ContextCompat.getColor(context, color.colorGray))
                setOnClickListener({ _ ->
                    dialog.layoutRoot.hideKeyboard()
                    dismiss()
                })
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            callback = context as? Callback
            presenter.attachView(this)
        } catch (castException: ClassCastException) {
            Log.d(TAG, "The activity does not implement Callback")
        }
    }

    override fun onDetach() {
        super.onDetach()
        presenter.detachView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        with(outState) {
            putString(BUNDLE_GOAL_DESCRIPTION, dialog.editTextDescription.textAsString())
            putString(BUNDLE_GOAL_COST, dialog.editTextCost.textAsString())
        }
    }
    //endregion

    //region Override methods
    override fun handleError(errorMessage: String?) {
        errorMessage?.let { activity?.toast(it) }
    }

    override fun onInvalidDescription(error: String) {
        dialog.inputLayoutDescription.showError(error)
    }

    override fun onInvalidCost(error: String) {
        dialog.inputLayoutCost.showError(error)
    }

    override fun onInvalidDeadline(error: String) {
        dialog.inputLayoutDeadline.showError(error)
    }

    override fun onErrorAddingGoal() {
        activity?.toast(getString(R.string.generic_msg_error))
    }

    override fun onGoalCreated(goal: GoalPresentationModel) {
        callback?.onGoalCreated(goal)
        dismiss()
    }
    //endregion

    //region Public methods
    //endregion

    //region Private methods
    private fun setupView() {
        dialog.layoutRoot.stealFocusOnTouch()

        dialog.editTextCost.filters = arrayOf(DecimalDigitsInputFilter())
        dialog.editTextDeadline.setDateInputMask()
    }

    private fun restoreInstance(savedInstanceState: Bundle?) {
        savedInstanceState?.run {
            dialog.editTextDescription.setText(getString(BUNDLE_GOAL_DESCRIPTION))
            dialog.editTextCost.setText(getString(BUNDLE_GOAL_COST))
            dialog.editTextDeadline.setText(getString(BUNDLE_GOAL_DEADLINE))
        }
    }

    private fun clearErrors() {
        dialog.inputLayoutDescription.clearError()
        dialog.inputLayoutCost.clearError()
        dialog.inputLayoutDeadline.clearError()
    }
    //endregion
}
