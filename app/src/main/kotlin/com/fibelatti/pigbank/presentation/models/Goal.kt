package com.fibelatti.pigbank.presentation.models

import android.os.Parcel
import android.os.Parcelable
import com.fibelatti.pigbank.presentation.common.createParcel
import com.fibelatti.pigbank.presentation.common.readDate
import com.fibelatti.pigbank.presentation.common.writeDate
import com.fibelatti.pigbank.presentation.goals.adapter.ViewType
import java.util.Date

data class Goal(
    val id: Long,
    val description: String,
    val cost: Float,
    val totalSaved: Float,
    val remainingCost: Float,
    val percentSaved: Float,
    val deadline: Date,
    val daysUntilDeadline: Long,
    val suggestedSavingsPerDay: Float,
    val suggestedSavingsPerWeek: Float,
    val suggestedSavingsPerMonth: Float,
    val savings: List<Savings>
) : Parcelable, ViewType {
    constructor(source: Parcel) : this(
        source.readLong(),
        source.readString(),
        source.readFloat(),
        source.readFloat(),
        source.readFloat(),
        source.readFloat(),
        source.readDate(),
        source.readLong(),
        source.readFloat(),
        source.readFloat(),
        source.readFloat(),
        ArrayList<Savings>().apply { source.readList(this, Savings::class.java.classLoader) }
    )

    companion object {
        @JvmField
        val CREATOR = createParcel { Goal(it) }
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
        writeString(description)
        writeFloat(cost)
        writeFloat(totalSaved)
        writeFloat(remainingCost)
        writeFloat(percentSaved)
        writeDate(deadline)
        writeLong(daysUntilDeadline)
        writeFloat(suggestedSavingsPerDay)
        writeFloat(suggestedSavingsPerWeek)
        writeFloat(suggestedSavingsPerMonth)
        writeList(savings)
    }

    override fun getViewType(): Int = ViewType.GOAL
}
