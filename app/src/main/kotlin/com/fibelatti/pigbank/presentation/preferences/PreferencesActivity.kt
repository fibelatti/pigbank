package com.fibelatti.pigbank.presentation.preferences

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.fibelatti.pigbank.BuildConfig
import com.fibelatti.pigbank.R
import com.fibelatti.pigbank.domain.userpreferences.UserPreferencesModel
import com.fibelatti.pigbank.presentation.addgoal.AddGoalDialogFragment
import com.fibelatti.pigbank.presentation.base.BaseActivity
import com.fibelatti.pigbank.presentation.base.BaseIntentBuilder
import com.fibelatti.pigbank.presentation.common.extensions.toast
import com.fibelatti.pigbank.presentation.rateapp.RateAppDialogFragment
import kotlinx.android.synthetic.main.activity_preferences.buttonRate
import kotlinx.android.synthetic.main.activity_preferences.buttonShare
import kotlinx.android.synthetic.main.activity_preferences.textViewAppVersion
import kotlinx.android.synthetic.main.layout_preferences_analytics_opt_out.checkBoxAnalyticsOptOut
import kotlinx.android.synthetic.main.layout_preferences_crash_report_opt_out.checkBoxCrashReportOptOut
import kotlinx.android.synthetic.main.layout_preferences_reset_hints.buttonResetHints
import kotlinx.android.synthetic.main.layout_toolbar_default.toolbar
import javax.inject.Inject

private const val PLAY_STORE_BASE_URL = "http://play.google.com/store/apps/details"

class PreferencesActivity :
    BaseActivity(),
    PreferencesContract.View {
    //region Companion objects and interfaces
    companion object {
        val TAG: String = PreferencesActivity::class.java.simpleName
    }
    //endregion

    //region Public properties
    @Inject
    lateinit var presenter: PreferencesContract.Presenter
    //endregion

    //region Private properties
    //endregion

    //region Override properties
    //endregion

    //region Override Lifecycle methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        setupLayout()
        presenter.attachView(this)
        presenter.requestPreferences()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    //endregion

    //region Override methods
    override fun handleError(errorMessage: String?) {
        toast(getString(R.string.generic_msg_error))
    }

    override fun showPreferences(userPreferencesModel: UserPreferencesModel) {
        setValues(userPreferencesModel)
    }

    override fun updatePreferences(userPreferencesModel: UserPreferencesModel) {
        setValues(userPreferencesModel)
    }

    override fun errorUpdatingPreferences() {
        toast(getString(R.string.generic_msg_error))
        presenter.requestPreferences()
    }

    override fun alertHintsReset() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showShareMenu() {
        startShareActivity()
    }

    override fun showRateMenu() {
        val rateAppDialogFragment = RateAppDialogFragment()
        rateAppDialogFragment.show(fragmentManager, AddGoalDialogFragment.TAG)
    }
    //endregion

    //region Public methods
    //endregion

    //region Private methods
    private fun setupLayout() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = getString(R.string.preferences_title)
            setDisplayHomeAsUpEnabled(true)
        }
        checkBoxCrashReportOptOut.setOnCheckedChangeListener { _, isChecked -> presenter.toggleCrashReport(isChecked) }
        checkBoxAnalyticsOptOut.setOnCheckedChangeListener { _, isChecked -> presenter.toggleAnalytics(isChecked) }
        buttonResetHints.setOnClickListener { presenter.resetHints() }
        buttonShare.setOnClickListener { presenter.shareApp() }
        buttonRate.setOnClickListener { presenter.rateApp() }

        textViewAppVersion.text = getString(R.string.preferences_app_version, BuildConfig.VERSION_NAME)
    }

    private fun setValues(userPreferencesModel: UserPreferencesModel) {
        with(userPreferencesModel) {
            checkBoxCrashReportOptOut.isChecked = crashReportsEnabled
            checkBoxAnalyticsOptOut.isChecked = analyticsEnabled
        }
    }

    private fun startShareActivity() {
        val message = getString(R.string.preferences_share_text, "$PLAY_STORE_BASE_URL?id=$packageName")
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.putExtra(Intent.EXTRA_TEXT, message)

        startActivity(Intent.createChooser(share, getString(R.string.preferences_share_title)))
    }
    //endregion

    class IntentBuilder(context: Context) : BaseIntentBuilder<PreferencesActivity>(context, PreferencesActivity::class.java)
}
