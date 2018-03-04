package com.fibelatti.pigbank.presentation.addsavings

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
import android.widget.EditText
import com.fibelatti.pigbank.R
import com.fibelatti.pigbank.common.ifNotNullThisElseThat
import com.fibelatti.pigbank.presentation.base.BaseDialogFragment
import com.fibelatti.pigbank.presentation.common.DecimalDigitsInputFilter
import com.fibelatti.pigbank.presentation.common.extensions.alert
import com.fibelatti.pigbank.presentation.common.extensions.clearError
import com.fibelatti.pigbank.presentation.common.extensions.negativeButton
import com.fibelatti.pigbank.presentation.common.extensions.positiveButton
import com.fibelatti.pigbank.presentation.common.extensions.showError
import com.fibelatti.pigbank.presentation.common.extensions.showListener
import com.fibelatti.pigbank.presentation.common.extensions.textAsString
import com.fibelatti.pigbank.presentation.common.extensions.toast
import com.fibelatti.pigbank.presentation.common.extensions.updateNegativeButton
import com.fibelatti.pigbank.presentation.common.extensions.updatePositiveButton
import com.fibelatti.pigbank.presentation.common.extensions.view
import com.fibelatti.pigbank.presentation.models.Goal
import javax.inject.Inject

class AddSavingsDialogFragment :
    BaseDialogFragment(),
    AddSavingsContract.View {
    //region Companion objects and interfaces
    companion object {
        val TAG: String = AddSavingsDialogFragment::class.java.simpleName
        private const val BUNDLE_GOAL = "GOAL"
        private const val BUNDLE_AMOUNT = "AMOUNT"

        fun newInstance(goal: Goal): AddSavingsDialogFragment {
            val fragment = AddSavingsDialogFragment()

            val args = Bundle()
            args.putParcelable(BUNDLE_GOAL, goal)
            fragment.arguments = args

            return fragment
        }
    }

    interface Callback {
        fun onSavingsAdded(goal: Goal)
    }
    //endregion

    //region Public properties
    @Inject
    lateinit var presenter: AddSavingsContract.Presenter
    //endregion

    //region Private properties
    private var callback: Callback? = null

    private var goal: Goal? = null
    private var savingsAmount: String = ""

    private lateinit var layoutRoot: ViewGroup
    private lateinit var editTextSavingsAmount: EditText
    private lateinit var inputLayoutSavingsAmount: TextInputLayout
    //endregion

    //region Override properties
    //endregion

    //region Override Lifecycle methods
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = View.inflate(activity, R.layout.dialog_add_savings, null)

        savedInstanceState.ifNotNullThisElseThat({ restoreInstance(it) }, { parseArguments(arguments) })

        val dialog = activity.alert(dialogMessage = getString(R.string.savings_description, goal?.description))
            .view(view)
            .positiveButton(
                buttonText = getString(R.string.goal_save_money),
                onClickListener = OnClickListener { _, _ ->
                    if (validateAmount()) {
                        goal?.let { presenter.addSavings(it, editTextSavingsAmount.textAsString().toFloat()) }
                    }
                }
            )
            .negativeButton(
                buttonText = getString(R.string.hint_cancel),
                onClickListener = OnClickListener { _, _ ->
                    dismiss()
                }
            )
            .showListener(OnShowListener { dialogInstance ->
                (dialogInstance as? AlertDialog)?.apply {
                    updatePositiveButton(ContextCompat.getColor(context, R.color.colorAccent))
                    updateNegativeButton(ContextCompat.getColor(context, R.color.colorGray))
                }
            })

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
            putParcelable(BUNDLE_GOAL, goal)
            putString(BUNDLE_AMOUNT, editTextSavingsAmount.textAsString())
        }
    }
    //endregion

    //region Override methods
    override fun handleError(errorMessage: String?) {
        errorMessage?.let { activity?.toast(it) }
    }

    override fun onSavingsAdded(goal: Goal) {
        callback?.onSavingsAdded(goal)
        dismiss()
    }

    override fun onErrorAddingSavings() {
        activity?.toast(getString(R.string.generic_msg_error))
    }
    //endregion

    //region Public methods
    //endregion

    //region Private methods
    private fun bindViews(view: View) {
        layoutRoot = view.findViewById(R.id.layoutRoot)
        editTextSavingsAmount = view.findViewById(R.id.editTextSavingsAmount)
        inputLayoutSavingsAmount = view.findViewById(R.id.inputLayoutSavingAmount)

        editTextSavingsAmount.filters = arrayOf(DecimalDigitsInputFilter())
    }

    private fun restoreInstance(savedInstanceState: Bundle?) {
        savedInstanceState?.apply {
            goal = getParcelable(BUNDLE_GOAL)
            savingsAmount = getString(BUNDLE_AMOUNT)
        }
    }

    private fun parseArguments(arguments: Bundle) {
        with(arguments) {
            goal = getParcelable(BUNDLE_GOAL)
        }
    }

    private fun validateAmount(): Boolean {
        if (editTextSavingsAmount.text.isBlank()) {
            inputLayoutSavingsAmount.showError(getString(R.string.savings_invalid_amount))
            return false
        } else {
            inputLayoutSavingsAmount.clearError()
        }

        return true
    }
    //endregion
}
