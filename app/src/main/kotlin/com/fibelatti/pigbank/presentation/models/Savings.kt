package com.fibelatti.pigbank.presentation.models

import android.os.Parcel
import android.os.Parcelable
import com.fibelatti.pigbank.presentation.common.extensions.createParcel
import com.fibelatti.pigbank.presentation.common.extensions.readDate
import com.fibelatti.pigbank.presentation.common.extensions.writeDate
import com.fibelatti.pigbank.presentation.goaldetail.adapter.ViewType
import java.util.Date

data class Savings(
    val id: Long,
    val goalId: Long,
    val amount: Float,
    val date: Date
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readLong(),
        source.readLong(),
        source.readFloat(),
        source.readDate()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
        writeLong(goalId)
        writeFloat(amount)
        writeDate(date)
    }

    companion object {
        @JvmField
        val CREATOR = createParcel { Savings(it) }
    }
}
