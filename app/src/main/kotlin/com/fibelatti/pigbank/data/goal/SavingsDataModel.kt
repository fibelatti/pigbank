package com.fibelatti.pigbank.data.goal

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.fibelatti.pigbank.data.goal.SavingsDataModel.Companion.TABLE_NAME
import java.util.Date

@Entity(
    tableName = TABLE_NAME,
    foreignKeys = [(ForeignKey(
        entity = GoalDataModel::class,
        parentColumns = [GoalDataModel.COLUMN_ID],
        childColumns = [(SavingsDataModel.COLUMN_GOAL_ID)],
        onDelete = CASCADE)
        )]
)
data class SavingsDataModel(
    @ColumnInfo(name = COLUMN_ID)
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = COLUMN_GOAL_ID)
    val goalId: Long,
    @ColumnInfo(name = COLUMN_AMOUNT)
    val amount: Float,
    @ColumnInfo(name = COLUMN_DATE)
    val date: Date
) {
    companion object {
        const val TABLE_NAME = "savings"
        const val COLUMN_ID = "_id"
        const val COLUMN_GOAL_ID = "goal_id"
        const val COLUMN_AMOUNT = "amount"
        const val COLUMN_DATE = "date"
    }

    @Ignore constructor() : this(
        id = 0,
        goalId = 0,
        amount = 0F,
        date = Date(0)
    )
}
