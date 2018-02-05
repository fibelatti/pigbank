package com.fibelatti.pigbank.presentation.models

import android.os.Parcel
import android.os.Parcelable
import com.fibelatti.pigbank.presentation.common.extensions.createParcel

data class GoalCandidate(
    val description: String,
    val cost: String,
    val deadline: String
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString()
    )

    companion object {
        @JvmField
        val CREATOR = createParcel { GoalCandidate(it) }
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(description)
        writeString(cost)
        writeString(deadline)
    }
}
