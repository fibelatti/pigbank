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
    val savings: List<SavingsPresentationModel> = emptyList(),
    val totalSaved: String = "",
    val remainingCost: String = "",
    val percentSaved: Float = 0F,
    val isAchieved: Boolean = false,
    val daysUntilDeadline: Int = 0,
    val isOverdue: Boolean = false,
    val timeElapsed: Int = 0,
    val emphasizeRemainingDays: Boolean = false,
    val suggestedSavingsPerDay: String = "",
    val shouldShowSavingsPerWeek: Boolean = false,
    val suggestedSavingsPerWeek: String = "",
    val shouldShowSavingsPerMonth: Boolean = false,
    val suggestedSavingsPerMonth: String = "",
    val shouldShowActualSavingsPerDay: Boolean = false,
    val actualSavingsPerDay: String = "",
    val shouldShowActualSavingsPerWeek: Boolean = false,
    val actualSavingsPerWeek: String = "",
    val shouldShowActualSavingsPerMonth: Boolean = false,
    val actualSavingsPerMonth: String = ""

) : Parcelable, ViewType {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readDate(),
        source.readLong(),
        source.readDate(),
        ArrayList<SavingsPresentationModel>().apply { source.readList(this, SavingsPresentationModel::class.java.classLoader) },
        source.readString(),
        source.readString(),
        source.readFloat(),
        source.readBoolean(),
        source.readInt(),
        source.readBoolean(),
        source.readInt(),
        source.readBoolean(),
        source.readString(),
        source.readBoolean(),
        source.readString(),
        source.readBoolean(),
        source.readString(),
        source.readBoolean(),
        source.readString()
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
        writeList(savings)
        writeString(totalSaved)
        writeString(remainingCost)
        writeFloat(percentSaved)
        writeBoolean(isAchieved)
        writeInt(daysUntilDeadline)
        writeBoolean(isOverdue)
        writeInt(timeElapsed)
        writeBoolean(emphasizeRemainingDays)
        writeString(suggestedSavingsPerDay)
        writeBoolean(shouldShowSavingsPerWeek)
        writeString(suggestedSavingsPerWeek)
        writeBoolean(shouldShowSavingsPerMonth)
        writeString(suggestedSavingsPerMonth)
        writeBoolean(shouldShowActualSavingsPerDay)
        writeString(actualSavingsPerDay)
        writeBoolean(shouldShowActualSavingsPerWeek)
        writeString(actualSavingsPerWeek)
        writeBoolean(shouldShowActualSavingsPerMonth)
        writeString(actualSavingsPerMonth)
    }

    override fun getViewType(): Int = ViewType.GOAL
}
