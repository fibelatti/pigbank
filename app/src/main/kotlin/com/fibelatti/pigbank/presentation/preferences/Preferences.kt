package com.fibelatti.pigbank.presentation.preferences

import android.os.Parcel
import android.os.Parcelable
import com.fibelatti.pigbank.presentation.common.extensions.createParcel
import com.fibelatti.pigbank.presentation.common.extensions.readBoolean
import com.fibelatti.pigbank.presentation.common.extensions.writeBoolean

data class Preferences(
    val crashReportEnabled: Boolean,
    val analyticsEnabled: Boolean
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readBoolean(),
        source.readBoolean()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeBoolean(crashReportEnabled)
        writeBoolean(analyticsEnabled)
    }

    companion object {
        @JvmField
        val CREATOR = createParcel { Preferences(it) }
    }
}
