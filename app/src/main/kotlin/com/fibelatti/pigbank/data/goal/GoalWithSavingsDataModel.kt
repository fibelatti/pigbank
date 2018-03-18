package com.fibelatti.pigbank.data.goal

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

class GoalWithSavingsDataModel {
    @Embedded
    var goalDataModel: GoalDataModel = GoalDataModel()

    @Relation(
        parentColumn = GoalDataModel.COLUMN_ID,
        entityColumn = SavingsDataModel.COLUMN_GOAL_ID
    )
    var savingsDataModelList: List<SavingsDataModel> = ArrayList()
}
