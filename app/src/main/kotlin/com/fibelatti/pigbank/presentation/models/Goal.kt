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

data class Goal(
    val description: String,
    val cost: Float,
    val deadline: Date,
    val id: Long = 0,
    val creationDate: Date = Date(),
    val totalSaved: Float = 0F,
    val remainingCost: Float = 0F,
    val percentSaved: Float = 0F,
    val daysUntilDeadline: Long = 0,
    val emphasizeRemainingDays: Boolean = false,
    val suggestedSavingsPerDay: Float = 0F,
    val suggestedSavingsPerWeek: Float = 0F,
    val suggestedSavingsPerMonth: Float = 0F,
    val savings: List<Savings> = emptyList()
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
        source.readLong(),
        source.readBoolean(),
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
        writeString(description)
        writeFloat(cost)
        writeDate(deadline)
        writeLong(id)
        writeDate(creationDate)
        writeFloat(totalSaved)
        writeFloat(remainingCost)
        writeFloat(percentSaved)
        writeLong(daysUntilDeadline)
        writeBoolean(emphasizeRemainingDays)
        writeFloat(suggestedSavingsPerDay)
        writeFloat(suggestedSavingsPerWeek)
        writeFloat(suggestedSavingsPerMonth)
        writeList(savings)
    }

    override fun getViewType(): Int = ViewType.GOAL

    fun deepCopy(
        description: String = this.description,
        cost: Float = this.cost,
        deadline: Date = this.deadline,
        id: Long = this.id,
        creationDate: Date = this.creationDate,
        totalSaved: Float = this.totalSaved,
        remainingCost: Float = this.remainingCost,
        percentSaved: Float = this.percentSaved,
        daysUntilDeadline: Long = this.daysUntilDeadline,
        emphasizeRemainingDays: Boolean = this.emphasizeRemainingDays,
        suggestedSavingsPerDay: Float = this.suggestedSavingsPerDay,
        suggestedSavingsPerWeek: Float = this.suggestedSavingsPerWeek,
        suggestedSavingsPerMonth: Float = this.suggestedSavingsPerMonth,
        savings: List<Savings> = this.savings.map { it.copy() }
    ) = Goal(description, cost, deadline, id, creationDate, totalSaved, remainingCost, percentSaved,
        daysUntilDeadline, emphasizeRemainingDays, suggestedSavingsPerDay, suggestedSavingsPerWeek,
        suggestedSavingsPerMonth, savings)
}
