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
    val cost: Float,
    val deadline: Date,
    val id: Long = 0,
    val creationDate: Date = Date(),
    val totalSaved: Float = 0F,
    val remainingCost: Float = 0F,
    val percentSaved: Float = 0F,
    val isAchieved: Boolean = false,
    val daysUntilDeadline: Long = 0,
    val emphasizeRemainingDays: Boolean = false,
    val isOverdue: Boolean = false,
    val suggestedSavingsPerDay: Float = 0F,
    val suggestedSavingsPerWeek: Float = 0F,
    val suggestedSavingsPerMonth: Float = 0F,
    val savings: List<SavingsPresentationModel> = emptyList()
) : Parcelable, ViewType {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readFloat(),
        source.readDate(),
        source.readLong(),
        source.readDate(),
        source.readFloat(),
        source.readFloat(),
        source.readFloat(),
        source.readBoolean(),
        source.readLong(),
        source.readBoolean(),
        source.readBoolean(),
        source.readFloat(),
        source.readFloat(),
        source.readFloat(),
        ArrayList<SavingsPresentationModel>().apply { source.readList(this, SavingsPresentationModel::class.java.classLoader) }
    )

    companion object {
        @JvmField
        val CREATOR = createParcel { GoalPresentationModel(it) }
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(description)
        writeFloat(cost)
        writeDate(deadline)
        writeLong(id)
        writeDate(creationDate)
        writeFloat(totalSaved)
        writeFloat(remainingCost)
        writeFloat(percentSaved)
        writeBoolean(isAchieved)
        writeLong(daysUntilDeadline)
        writeBoolean(emphasizeRemainingDays)
        writeBoolean(isOverdue)
        writeFloat(suggestedSavingsPerDay)
        writeFloat(suggestedSavingsPerWeek)
        writeFloat(suggestedSavingsPerMonth)
        writeList(savings)
    }

    override fun getViewType(): Int = ViewType.GOAL
}
