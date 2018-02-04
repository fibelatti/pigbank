package com.fibelatti.pigbank.presentation.addgoal

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface.OnClickListener
import android.content.DialogInterface.OnShowListener
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
import com.fibelatti.pigbank.common.intPartsAsDate
import com.fibelatti.pigbank.common.intPartsAsDateString
import com.fibelatti.pigbank.presentation.base.BaseDialogFragment
import com.fibelatti.pigbank.presentation.common.DecimalDigitsInputFilter
import com.fibelatti.pigbank.presentation.common.ObservableView
import com.fibelatti.pigbank.presentation.common.extensions.alert
import com.fibelatti.pigbank.presentation.common.extensions.hideKeyboard
import com.fibelatti.pigbank.presentation.common.extensions.negativeButton
import com.fibelatti.pigbank.presentation.common.extensions.negativeButtonColor
import com.fibelatti.pigbank.presentation.common.extensions.positiveButton
import com.fibelatti.pigbank.presentation.common.extensions.positiveButtonColor
import com.fibelatti.pigbank.presentation.common.extensions.requestUserFocus
import com.fibelatti.pigbank.presentation.common.extensions.showListener
import com.fibelatti.pigbank.presentation.common.extensions.textAsString
import com.fibelatti.pigbank.presentation.common.extensions.toast
import com.fibelatti.pigbank.presentation.common.extensions.view
import com.fibelatti.pigbank.presentation.models.Goal
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class AddGoalDialogFragment :
    BaseDialogFragment(),
    AddGoalContract.View {
    //region Companion objects and interfaces
    companion object {
        val TAG: String = AddGoalDialogFragment::class.java.simpleName
        private const val BUNDLE_GOAL_DESCRIPTION = "GOAL_DESCRIPTION"
        private const val BUNDLE_GOAL_COST = "GOAL_COST"
        private const val BUNDLE_GOAL_DEADLINE = "GOAL_DEADLINE"
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

    private var calendarYear: Int
    private var calendarMonth: Int
    private var calendarDay: Int

    private lateinit var layoutRoot: ViewGroup
    private lateinit var editTextDescription: EditText
    private lateinit var editTextCost: EditText
    private lateinit var inputLayoutCost: TextInputLayout
    private lateinit var editTextDeadline: EditText
    private lateinit var inputLayoutDeadline: TextInputLayout
    private lateinit var datePickerDeadline: DatePicker
    //endregion

    //region Override properties
    override val goalDeadlineClicked = ObservableView<Unit>()
    override val createGoalClicked = ObservableView<Triple<String, Float, Date>>()
    //endregion

    init {
        val calendar = Calendar.getInstance()
        calendarYear = calendar.get(Calendar.YEAR)
        calendarMonth = calendar.get(Calendar.MONTH)
        calendarDay = calendar.get(Calendar.DAY_OF_MONTH)
    }

    //region Override Lifecycle methods
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = View.inflate(activity, R.layout.dialog_add_goal, null)

        val dialog = activity.alert(dialogTitle = getString(R.string.goal_add))
            .view(view)
            .positiveButton(
                buttonText = getString(R.string.hint_done),
                onClickListener = OnClickListener { _, _ ->
                    if (validateForm()) {
                        createGoalClicked.emitNext(Triple(
                            editTextDescription.textAsString(),
                            editTextCost.textAsString().toFloat(),
                            intPartsAsDate(calendarYear, calendarMonth, calendarDay)))
                    }
                }
            )
            .negativeButton(
                buttonText = getString(R.string.hint_cancel),
                onClickListener = OnClickListener { _, _ ->
                    if (datePickerDeadline.visibility == View.VISIBLE) {
                        datePickerDeadline.visibility = View.GONE
                    } else {
                        layoutRoot.hideKeyboard()
                        dismiss()
                    }
                }
            )
            .showListener(OnShowListener { dialogInstance ->
                (dialogInstance as? AlertDialog)?.apply {
                    positiveButtonColor(ContextCompat.getColor(context, R.color.colorAccent))
                    negativeButtonColor(ContextCompat.getColor(context, R.color.colorGray))
                }
            })

        bindViews(view)
        restoreInstance(savedInstanceState)
        dialog.show()

        return dialog
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            callback = context as? Callback
        } catch (castException: ClassCastException) {
            Log.d(TAG, "The activity does not implement Callback")
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.bind(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.unbind()
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
        datePickerDeadline.init(calendarYear, calendarMonth, calendarDay) { _, year, month, dayOfMonth ->
            calendarYear = year
            calendarMonth = month
            calendarDay = dayOfMonth

            editTextDeadline.setText(intPartsAsDateString(calendarYear, calendarMonth, calendarDay))
            datePickerDeadline.visibility = View.GONE
        }
    }

    override fun onGoalCreated(goal: Goal) {
        callback?.onGoalCreated(goal)
        dismiss()
    }

    override fun onErrorAddingGoal() {
        activity.toast(getString(R.string.generic_msg_error))
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
        editTextDeadline.setOnClickListener { goalDeadlineClicked.emitNext(Unit) }
    }

    private fun restoreInstance(savedInstanceState: Bundle?) {
        savedInstanceState?.apply {
            editTextDescription.setText(getString(BUNDLE_GOAL_DESCRIPTION))
            editTextCost.setText(getString(BUNDLE_GOAL_COST))
            editTextDeadline.setText(getString(BUNDLE_GOAL_DEADLINE))
        }
    }

    private fun validateForm(): Boolean = validateDescription() && validateCost() && validateDeadline()

    private fun validateDescription(): Boolean {
        if (editTextDescription.text.isBlank()) {
            editTextDescription.error = getString(R.string.goal_add_invalid_description)
            editTextDescription.requestUserFocus(activity)
            return false
        } else {
            editTextDescription.error = null
        }

        return true
    }

    private fun validateCost(): Boolean {
        if (editTextCost.text.isBlank()) {
            inputLayoutCost.error = getString(R.string.goal_add_invalid_cost)
            editTextCost.requestUserFocus(activity)
            return false
        } else {
            inputLayoutCost.error = null
            inputLayoutCost.isErrorEnabled = false
        }

        return true
    }

    private fun validateDeadline(): Boolean {
        if (editTextDeadline.text.isBlank()) {
            inputLayoutDeadline.error = getString(R.string.goal_add_invalid_deadline)
            editTextDeadline.requestUserFocus(activity)
            return false
        } else {
            inputLayoutDeadline.error = null
            inputLayoutDeadline.isErrorEnabled = false
        }

        return true
    }
    //endregion
}
