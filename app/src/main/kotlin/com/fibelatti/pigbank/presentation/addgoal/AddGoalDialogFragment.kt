package com.fibelatti.pigbank.presentation.addgoal

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import com.fibelatti.pigbank.R
import com.fibelatti.pigbank.R.color
import com.fibelatti.pigbank.common.asString
import com.fibelatti.pigbank.common.intPartsAsDate
import com.fibelatti.pigbank.presentation.base.BaseDialogFragment
import com.fibelatti.pigbank.presentation.common.DecimalDigitsInputFilter
import com.fibelatti.pigbank.presentation.common.extensions.alert
import com.fibelatti.pigbank.presentation.common.extensions.hideKeyboard
import com.fibelatti.pigbank.presentation.common.extensions.negativeButton
import com.fibelatti.pigbank.presentation.common.extensions.notCancelable
import com.fibelatti.pigbank.presentation.common.extensions.positiveButton
import com.fibelatti.pigbank.presentation.common.extensions.requestUserFocus
import com.fibelatti.pigbank.presentation.common.extensions.showError
import com.fibelatti.pigbank.presentation.common.extensions.showKeyboard
import com.fibelatti.pigbank.presentation.common.extensions.showListener
import com.fibelatti.pigbank.presentation.common.extensions.textAsString
import com.fibelatti.pigbank.presentation.common.extensions.toast
import com.fibelatti.pigbank.presentation.common.extensions.updateNegativeButton
import com.fibelatti.pigbank.presentation.common.extensions.updatePositiveButton
import com.fibelatti.pigbank.presentation.common.extensions.view
import com.fibelatti.pigbank.presentation.models.Goal
import com.fibelatti.pigbank.presentation.models.GoalCandidate
import java.util.Calendar
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
        fun onGoalCreated(goal: Goal)
    }
    //endregion

    //region Public properties
    @Inject
    lateinit var presenter: AddGoalContract.Presenter
    //endregion

    //region Private properties
    private var callback: Callback? = null
    private var calendar = Calendar.getInstance()

    private lateinit var layoutRoot: ViewGroup
    private lateinit var editTextDescription: EditText
    private lateinit var editTextCost: EditText
    private lateinit var inputLayoutCost: TextInputLayout
    private lateinit var editTextDeadline: EditText
    private lateinit var inputLayoutDeadline: TextInputLayout
    private lateinit var datePickerDeadline: DatePicker
    //endregion

    //region Override properties
    //endregion

    //region Override Lifecycle methods
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        restoreInstance(savedInstanceState)

        val view = View.inflate(activity, R.layout.dialog_add_goal, null)
        val dialog = activity.alert(dialogTitle = getString(R.string.goal_add)).apply {
            view(view)
            positiveButton(buttonText = getString(R.string.hint_done))
            negativeButton(buttonText = getString(R.string.hint_cancel))
            notCancelable()
            showListener(DialogInterface.OnShowListener { dialogInstance ->
                (dialogInstance as? AlertDialog)?.apply {
                    updatePositiveButton(
                        buttonColor = ContextCompat.getColor(context, color.colorAccent),
                        onClickListener = View.OnClickListener { _ ->
                            presenter.createGoal(GoalCandidate(
                                editTextDescription.textAsString(),
                                editTextCost.textAsString(),
                                editTextDeadline.textAsString()))
                        }
                    )
                    updateNegativeButton(
                        buttonColor = ContextCompat.getColor(context, color.colorGray),
                        onClickListener = View.OnClickListener { _ ->
                            if (datePickerDeadline.visibility == View.VISIBLE) {
                                datePickerDeadline.visibility = View.GONE
                            } else {
                                layoutRoot.hideKeyboard()
                                dismiss()
                            }
                        }
                    )
                    editTextDescription.showKeyboard()
                }
            })
        }

        bindViews(view)
        dialog.show()

        return dialog
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
            putString(BUNDLE_GOAL_DESCRIPTION, editTextDescription.textAsString())
            putString(BUNDLE_GOAL_COST, editTextCost.textAsString())
        }
    }
    //endregion

    //region Override methods
    override fun handleError(errorMessage: String?) {
        errorMessage?.let { activity.toast(it) }
    }

    override fun showDatePicker() {
        layoutRoot.hideKeyboard()

        datePickerDeadline.visibility = View.VISIBLE
        datePickerDeadline.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)) { _, year, month, dayOfMonth ->
            editTextDeadline.setText(intPartsAsDate(year, month, dayOfMonth).asString())
            datePickerDeadline.visibility = View.GONE
        }
    }

    override fun onInvalidDescription(error: String) {
        editTextDescription.error = error
        editTextDescription.requestUserFocus(activity)
    }

    override fun onInvalidCost(error: String) {
        inputLayoutCost.showError(error)
    }

    override fun onInvalidDeadline(error: String) {
        inputLayoutDeadline.showError(error)
    }

    override fun onErrorAddingGoal() {
        activity.toast(getString(R.string.generic_msg_error))
    }

    override fun onGoalCreated(goal: Goal) {
        callback?.onGoalCreated(goal)
        dismiss()
    }
    //endregion

    //region Public methods
    //endregion

    //region Private methods
    private fun bindViews(view: View) {
        layoutRoot = view.findViewById(R.id.layoutRoot)
        editTextDescription = view.findViewById(R.id.editTextDescription)
        editTextCost = view.findViewById(R.id.editTextCost)
        inputLayoutCost = view.findViewById(R.id.inputLayoutCost)
        editTextDeadline = view.findViewById(R.id.editTextDeadline)
        inputLayoutDeadline = view.findViewById(R.id.inputLayoutDeadline)
        datePickerDeadline = view.findViewById(R.id.datePickerDeadline)

        editTextCost.filters = arrayOf(DecimalDigitsInputFilter())
        editTextDeadline.setOnClickListener { presenter.editDeadline() }
    }

    private fun restoreInstance(savedInstanceState: Bundle?) {
        savedInstanceState?.apply {
            editTextDescription.setText(getString(BUNDLE_GOAL_DESCRIPTION))
            editTextCost.setText(getString(BUNDLE_GOAL_COST))
            editTextDeadline.setText(getString(BUNDLE_GOAL_DEADLINE))
        }
    }
    //endregion
}
