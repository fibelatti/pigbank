package com.fibelatti.pigbank.domain.goal

import javax.inject.Inject
import com.fibelatti.pigbank.data.goal.Savings as DataModel
import com.fibelatti.pigbank.presentation.models.Savings as PresentationModel

class SavingsMapper @Inject constructor() {
    fun toPresentationModel(savings: DataModel) = with(savings) {
        PresentationModel(id, goalId, amount, date)
    }

    fun toDataModel(savings: PresentationModel) = with(savings) {
        DataModel(id, goalId, amount, date)
    }
}
