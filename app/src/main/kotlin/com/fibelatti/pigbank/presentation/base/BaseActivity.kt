package com.fibelatti.pigbank.presentation.base

import android.support.v7.widget.AppCompatTextView
import android.view.ViewGroup
import android.widget.FrameLayout
import com.fibelatti.pigbank.R
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity :
    DaggerAppCompatActivity(),
    BaseContract.View {

    //region Companion objects and interfaces
    //endregion

    //region Public properties
    //endregion

    //region Private properties
    //endregion

    //region Override properties
    //endregion

    //region Override Lifecycle methods
    //endregion

    //region Override methods
    //endregion

    //region Public methods
    //endregion

    //region Private methods
    protected fun showDismissibleHint(
        container: ViewGroup,
        hintTitle: String = getString(R.string.hint_did_you_know),
        hintMessage: String,
        dismissListener: (() -> Unit)? = null
    ) {
        container.let {
            val layoutHint = layoutInflater.inflate(R.layout.layout_dismissible_hint, it, false)

            with(layoutHint) {
                val textViewHintTitle = findViewById<AppCompatTextView>(R.id.textViewHintTitle)
                val layoutHintBody = findViewById<FrameLayout>(R.id.layoutHintBody)
                val buttonHintDismiss = findViewById<AppCompatTextView>(R.id.buttonHintDismiss)
                val textViewHintMessage = layoutInflater.inflate(R.layout.layout_dismissible_hint_text, layoutHintBody, false) as? AppCompatTextView

                textViewHintTitle.text = hintTitle
                textViewHintMessage?.text = hintMessage

                layoutHintBody.addView(textViewHintMessage)

                buttonHintDismiss.setOnClickListener {
                    container.removeView(layoutHint)
                    dismissListener?.invoke()
                }
            }

            it.addView(layoutHint)
        }
    }
    //endregion
}
