package com.fibelatti.pigbank.presentation.models

import android.os.Parcel
import android.os.Parcelable
import com.fibelatti.pigbank.presentation.common.extensions.createParcel
import com.fibelatti.pigbank.presentation.common.extensions.readDate
import com.fibelatti.pigbank.presentation.common.extensions.writeDate
import com.fibelatti.pigbank.presentation.goaldetail.adapter.ViewType
import java.util.Date

data class SavingsPresentationModel(
    val id: Long,
    val goalId: Long,
    val amount: Float,
    val date: Date
) : Parcelable, ViewType {
    constructor(source: Parcel) : this(
        source.readLong(),
        source.readLong(),
        source.readFloat(),
        source.readDate()
    )

    companion object {
        @JvmField
        val CREATOR = createParcel { SavingsPresentationModel(it) }
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
        writeLong(goalId)
        writeFloat(amount)
        writeDate(date)
    }

    override fun getViewType(): Int = ViewType.SAVINGS
}
