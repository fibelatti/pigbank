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
import com.fibelatti.pigbank.R
import com.fibelatti.pigbank.R.color
import com.fibelatti.pigbank.presentation.base.BaseDialogFragment
import com.fibelatti.pigbank.presentation.common.extensions.gone
import com.fibelatti.pigbank.presentation.common.extensions.toast
import com.fibelatti.pigbank.presentation.common.extensions.visible
import kotlinx.android.synthetic.main.dialog_rate_app.buttonEmail
import kotlinx.android.synthetic.main.dialog_rate_app.buttonPlayStore
import kotlinx.android.synthetic.main.dialog_rate_app.layoutNegativeFeedback
import kotlinx.android.synthetic.main.dialog_rate_app.layoutPositiveFeedback
import kotlinx.android.synthetic.main.dialog_rate_app.ratingBar
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
    //endregion

    //region Override properties
    //endregion

    //region Override Lifecycle methods
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = activity?.let {
        val view = View.inflate(it, R.layout.dialog_rate_app, null)

        return@let AlertDialog.Builder(it).apply {
            setView(view)
            setNegativeButton(getString(R.string.hint_cancel), null)
        }.create()
    } ?: super.onCreateDialog(savedInstanceState)

    override fun onStart() {
        super.onStart()
        setupListeners()

        (dialog as? AlertDialog)?.apply {
            getButton(DialogInterface.BUTTON_NEGATIVE)?.apply {
                setTextColor(ContextCompat.getColor(context, color.colorGray))
                setOnClickListener({ _ ->
                    dismiss()
                })
            }
        }
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
        errorMessage?.let { activity?.toast(it) }
    }

    override fun showPlayStore() {
        dialog.layoutPositiveFeedback.visible()
        dialog.layoutNegativeFeedback.gone()
    }

    override fun showEmail() {
        dialog.layoutPositiveFeedback.gone()
        dialog.layoutNegativeFeedback.visible()
    }
    //endregion

    //region Public methods
    //endregion

    //region Private methods
    private fun setupListeners() {
        dialog.ratingBar.setOnRatingBarChangeListener { _, rating, _ -> presenter.ratingChanged(rating.toInt()) }
        dialog.buttonPlayStore.setOnClickListener { rateApp() }
        dialog.buttonEmail.setOnClickListener { sendEmail() }
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
        Intent(Intent.ACTION_VIEW, Uri.parse("$url?id=${activity?.packageName}"))

    private fun sendEmail() {
        val intent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "fibelatti+dev@gmail.com", null))
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.dialog_rate_send_email_subject))
        startActivity(Intent.createChooser(intent, getString(R.string.dialog_rate_send_email_title)))
        dialog.dismiss()
    }
    //endregion
}
