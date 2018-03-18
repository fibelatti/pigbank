package com.fibelatti.pigbank.di.module

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.fibelatti.pigbank.di.scope.AppScope
import dagger.Module
import dagger.Provides
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@Module
class DeviceModule {

    @Provides
    fun provideSharedPreferences(app: Application): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(app)

    @Provides
    @AppScope
    fun provideLocaleDefault(): Locale = Locale.getDefault()

    @Provides
    @AppScope
    fun provideDecimalSymbols(locale: Locale): DecimalFormatSymbols? =
        (DecimalFormat.getInstance(locale) as? DecimalFormat)?.decimalFormatSymbols
}
