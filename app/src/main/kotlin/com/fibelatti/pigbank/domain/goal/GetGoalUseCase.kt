package com.fibelatti.pigbank.domain.goal

import com.fibelatti.pigbank.data.goal.GoalRepositoryContract
import com.fibelatti.pigbank.data.goal.GoalWithSavingsDataModel
import com.fibelatti.pigbank.domain.goal.models.GoalDomainMapper
import com.fibelatti.pigbank.domain.goal.models.GoalEntity
import io.reactivex.Single
import javax.inject.Inject

class GetGoalUseCase @Inject constructor(
    private val goalRepositoryContract: GoalRepositoryContract,
    private val goalDomainMapper: GoalDomainMapper
) {
    fun getAllGoals(): Single<List<GoalEntity>> =
        goalRepositoryContract
            .getAllGoals()
            .onErrorReturn { emptyList() }
            .flattenAsObservable<GoalWithSavingsDataModel> { list -> list }
            .map { goalDomainMapper.toDomainModel(goalWithSavingsDataModel = it) }
            .toSortedList { goal1, goal2 -> (goal1.daysUntilDeadline - goal2.daysUntilDeadline).toInt() }

    fun getGoalById(id: Long): Single<GoalEntity> =
        goalRepositoryContract
            .getGoalById(id)
            .map { goalDomainMapper.toDomainModel(goalWithSavingsDataModel = it) }
}
