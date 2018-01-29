package com.fibelatti.pigbank.data.goal

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.fibelatti.pigbank.data.goal.Goal.Companion.TABLE_NAME
import java.util.Date

@Entity(tableName = TABLE_NAME)
data class Goal(
    @ColumnInfo(name = COLUMN_ID)
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = COLUMN_CREATION_DATE)
    val creationDate: Date,
    @ColumnInfo(name = COLUMN_DESCRIPTION)
    val description: String,
    @ColumnInfo(name = COLUMN_COST)
    val cost: Float,
    @ColumnInfo(name = COLUMN_SAVINGS)
    val savings: Float,
    @ColumnInfo(name = COLUMN_DEADLINE)
    val deadline: Date
) {
    companion object {
        const val TABLE_NAME = "goal"
        const val COLUMN_ID = "_id"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_COST = "cost"
        const val COLUMN_SAVINGS = "savings"
        const val COLUMN_DEADLINE = "deadline"
        const val COLUMN_CREATION_DATE = "creation_date"
    }

    @Ignore constructor() : this(
        id = 0,
        creationDate = Date(),
        description = "",
        cost = 0F,
        savings = 0F,
        deadline = Date(0))
}
