package com.fibelatti.pigbank.presentation.rateapp

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import com.fibelatti.pigbank.R
import com.fibelatti.pigbank.R.color
import com.fibelatti.pigbank.presentation.base.BaseDialogFragment
import com.fibelatti.pigbank.presentation.common.extensions.alert
import com.fibelatti.pigbank.presentation.common.extensions.gone
import com.fibelatti.pigbank.presentation.common.extensions.negativeButton
import com.fibelatti.pigbank.presentation.common.extensions.notCancelable
import com.fibelatti.pigbank.presentation.common.extensions.showListener
import com.fibelatti.pigbank.presentation.common.extensions.toast
import com.fibelatti.pigbank.presentation.common.extensions.updateNegativeButton
import com.fibelatti.pigbank.presentation.common.extensions.view
import com.fibelatti.pigbank.presentation.common.extensions.visible
import javax.inject.Inject

private const val PLAY_STORE_BASE_URL = "http://play.google.com/store/apps/details"

class RateAppDialogFragment :
    BaseDialogFragment(),
    RateAppContract.View {
    //region Companion objects and interfaces
    companion object {
        val TAG: String = RateAppDialogFragment::class.java.simpleName
    }
    //endregion

    //region Public properties
    @Inject
    lateinit var presenter: RateAppContract.Presenter
    //endregion

    //region Private properties
    private lateinit var ratingBar: RatingBar
    private lateinit var layoutPositiveFeedback: ViewGroup
    private lateinit var buttonPlayStore: Button
    private lateinit var layoutNegativeFeedback: ViewGroup
    private lateinit var buttonEmail: Button
    //endregion

    //region Override properties
    //endregion

    //region Override Lifecycle methods
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = View.inflate(activity, R.layout.dialog_rate_app, null)
        val dialog = activity.alert().apply {
            view(view)
            negativeButton(buttonText = getString(R.string.hint_cancel))
            notCancelable()
            showListener(DialogInterface.OnShowListener { dialogInstance ->
                (dialogInstance as? AlertDialog)?.apply {
                    updateNegativeButton(
                        buttonColor = ContextCompat.getColor(context, color.colorGray),
                        onClickListener = View.OnClickListener { _ ->
                            dismiss()
                        }
                    )
                }
            })
        }

        bindViews(view)
        setupListeners()
        dialog.show()

        return dialog
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        presenter.attachView(this)
    }

    override fun onDetach() {
        super.onDetach()
        presenter.detachView()
    }
    //endregion

    //region Override methods
    override fun handleError(errorMessage: String?) {
        errorMessage?.let { activity.toast(it) }
    }

    override fun showPlayStore() {
        layoutPositiveFeedback.visible()
        layoutNegativeFeedback.gone()
    }

    override fun showEmail() {
        layoutPositiveFeedback.gone()
        layoutNegativeFeedback.visible()
    }
    //endregion

    //region Public methods
    //endregion

    //region Private methods
    private fun bindViews(view: View) {
        ratingBar = view.findViewById(R.id.ratingBar)
        layoutPositiveFeedback = view.findViewById(R.id.layoutPositiveFeedback)
        buttonPlayStore = view.findViewById(R.id.buttonPlayStore)
        layoutNegativeFeedback = view.findViewById(R.id.layoutNegativeFeedback)
        buttonEmail = view.findViewById(R.id.buttonEmail)
    }

    private fun setupListeners() {
        ratingBar.setOnRatingBarChangeListener { _, rating, _ -> presenter.ratingChanged(rating.toInt()) }
        buttonPlayStore.setOnClickListener { rateApp() }
        buttonEmail.setOnClickListener { sendEmail() }
    }

    private fun rateApp() {
        val intent = try {
            createPlayStoreIntent("market://details")
        } catch (e: ActivityNotFoundException) {
            createPlayStoreIntent(PLAY_STORE_BASE_URL)
        }

        startActivity(intent)
        dialog.dismiss()
    }

    private fun createPlayStoreIntent(url: String): Intent =
        Intent(Intent.ACTION_VIEW, Uri.parse("$url?id=${activity.packageName}"))

    private fun sendEmail() {
        val intent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "fibelatti+dev@gmail.com", null))
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.dialog_rate_send_email_subject))
        startActivity(Intent.createChooser(intent, getString(R.string.dialog_rate_send_email_title)))
        dialog.dismiss()
    }
    //endregion
}
