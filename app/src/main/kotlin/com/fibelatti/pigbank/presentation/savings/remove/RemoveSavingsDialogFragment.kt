package com.fibelatti.pigbank.presentation.savings.remove

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import com.fibelatti.pigbank.R
import com.fibelatti.pigbank.common.ifNotNullThisElseThat
import com.fibelatti.pigbank.presentation.base.BaseDialogFragment
import com.fibelatti.pigbank.presentation.common.DecimalDigitsInputFilter
import com.fibelatti.pigbank.presentation.common.extensions.hideKeyboard
import com.fibelatti.pigbank.presentation.common.extensions.showError
import com.fibelatti.pigbank.presentation.common.extensions.textAsString
import com.fibelatti.pigbank.presentation.common.extensions.toast
import com.fibelatti.pigbank.presentation.models.GoalPresentationModel
import com.fibelatti.pigbank.presentation.savings.AddSavingsContract
import com.fibelatti.pigbank.presentation.savings.AddSavingsContract.Presenter
import kotlinx.android.synthetic.main.dialog_add_savings.editTextSavingsAmount
import kotlinx.android.synthetic.main.dialog_add_savings.inputLayoutSavingAmount
import kotlinx.android.synthetic.main.dialog_add_savings.layoutRoot
import javax.inject.Inject

private const val BUNDLE_GOAL = "GOAL"
private const val BUNDLE_AMOUNT = "AMOUNT"

class RemoveSavingsDialogFragment :
    BaseDialogFragment(),
    AddSavingsContract.View {
    //region Companion objects and interfaces
    companion object {
        val TAG: String = RemoveSavingsDialogFragment::class.java.simpleName

        fun newInstance(goal: GoalPresentationModel): RemoveSavingsDialogFragment {
            val args = Bundle().apply {
                putParcelable(BUNDLE_GOAL, goal)
            }

            return RemoveSavingsDialogFragment().apply {
                arguments = args
            }
        }
    }

    interface Callback {
        fun onSavingsRemoved(goal: GoalPresentationModel)
    }
    //endregion

    //region Public properties
    @Inject
    lateinit var presenter: Presenter
    //endregion

    //region Private properties
    private var callback: Callback? = null
    private var goal: GoalPresentationModel? = null
    private var savingsAmount: String = ""
    //endregion

    //region Override properties
    //endregion

    //region Override Lifecycle methods
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = activity?.let {
        val view = View.inflate(it, R.layout.dialog_remove_savings, null)

        savedInstanceState.ifNotNullThisElseThat({ restoreInstance(it) }, { parseArguments(arguments) })

        return@let AlertDialog.Builder(it).apply {
            setView(view)
            setTitle(getString(R.string.savings_remove_description, goal?.description))
            setPositiveButton(getString(R.string.goal_take_money), null)
            setNegativeButton(getString(R.string.hint_cancel), null)
            setCancelable(false)
        }.create()
    } ?: super.onCreateDialog(savedInstanceState)

    override fun onStart() {
        super.onStart()

        (dialog as? AlertDialog)?.apply {
            setCanceledOnTouchOutside(false)
            getButton(DialogInterface.BUTTON_POSITIVE)?.apply {
                setOnClickListener({ _ ->
                    goal?.let {
                        presenter.addSavings(
                            goal = it,
                            amount = dialog.editTextSavingsAmount.textAsString(),
                            shouldSubtract = true)
                    }
                })
            }
            getButton(DialogInterface.BUTTON_NEGATIVE)?.apply {
                setTextColor(ContextCompat.getColor(context, R.color.colorGray))
                setOnClickListener({ _ ->
                    dialog.layoutRoot.hideKeyboard()
                    dismiss()
                })
            }
        }

        dialog.editTextSavingsAmount.filters = arrayOf(DecimalDigitsInputFilter())
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
            putString(BUNDLE_AMOUNT, dialog.editTextSavingsAmount.textAsString())
        }
    }
    //endregion

    //region Override methods
    override fun handleError(errorMessage: String?) {
        errorMessage?.let { activity?.toast(it) }
    }

    override fun onSavingsAdded(goal: GoalPresentationModel) {
        dialog.layoutRoot.hideKeyboard()
        callback?.onSavingsRemoved(goal)
        dismiss()
    }

    override fun onInvalidSavingsAmount() {
        dialog.inputLayoutSavingAmount.showError(getString(R.string.savings_invalid_amount))
    }

    override fun onErrorAddingSavings() {
        activity?.toast(getString(R.string.generic_msg_error))
    }
    //endregion

    //region Public methods
    //endregion

    //region Private methods
    private fun restoreInstance(savedInstanceState: Bundle?) {
        savedInstanceState?.run {
            goal = getParcelable(BUNDLE_GOAL)
            savingsAmount = getString(BUNDLE_AMOUNT)
        }
    }

    private fun parseArguments(arguments: Bundle?) {
        arguments?.run {
            goal = getParcelable(BUNDLE_GOAL)
        }
    }
    //endregion
}
