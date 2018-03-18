package com.fibelatti.pigbank.presentation.models

import android.os.Parcel
import android.os.Parcelable
import com.fibelatti.pigbank.presentation.common.extensions.createParcel
import com.fibelatti.pigbank.presentation.common.extensions.readBoolean
import com.fibelatti.pigbank.presentation.common.extensions.readDate
import com.fibelatti.pigbank.presentation.common.extensions.writeBoolean
import com.fibelatti.pigbank.presentation.common.extensions.writeDate
import com.fibelatti.pigbank.presentation.goals.adapter.ViewType
import java.util.Date

data class GoalPresentationModel(
    val description: String,
    val cost: String,
    val deadline: Date,
    val id: Long = 0,
    val creationDate: Date = Date(),
    val totalSaved: String = "",
    val remainingCost: String = "",
    val percentSaved: Int = 0,
    val isAchieved: Boolean = false,
    val daysUntilDeadline: Long = 0,
    val emphasizeRemainingDays: Boolean = false,
    val isOverdue: Boolean = false,
    val suggestedSavingsPerDay: String = "",
    val suggestedSavingsPerWeek: String = "",
    val suggestedSavingsPerMonth: String = "",
    val savings: List<SavingsPresentationModel> = emptyList()
) : Parcelable, ViewType {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readDate(),
        source.readLong(),
        source.readDate(),
        source.readString(),
        source.readString(),
        source.readInt(),
        source.readBoolean(),
        source.readLong(),
        source.readBoolean(),
        source.readBoolean(),
        source.readString(),
        source.readString(),
        source.readString(),
        ArrayList<SavingsPresentationModel>().apply { source.readList(this, SavingsPresentationModel::class.java.classLoader) }
    )

    companion object {
        @JvmField
        val CREATOR = createParcel { GoalPresentationModel(it) }
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(description)
        writeString(cost)
        writeDate(deadline)
        writeLong(id)
        writeDate(creationDate)
        writeString(totalSaved)
        writeString(remainingCost)
        writeInt(percentSaved)
        writeBoolean(isAchieved)
        writeLong(daysUntilDeadline)
        writeBoolean(emphasizeRemainingDays)
        writeBoolean(isOverdue)
        writeString(suggestedSavingsPerDay)
        writeString(suggestedSavingsPerWeek)
        writeString(suggestedSavingsPerMonth)
        writeList(savings)
    }

    override fun getViewType(): Int = ViewType.GOAL
}
