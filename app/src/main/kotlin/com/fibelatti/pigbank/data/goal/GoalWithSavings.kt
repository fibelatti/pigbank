package com.fibelatti.pigbank.data.goal

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

class GoalWithSavings {
    @Embedded
    var goal: Goal = Goal()

    @Relation(
        parentColumn = Goal.COLUMN_ID,
        entityColumn = Savings.COLUMN_GOAL_ID
    )
    var savings: List<Savings> = ArrayList()
}
