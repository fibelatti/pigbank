package com.fibelatti.pigbank.domain.userpreferences

import com.fibelatti.pigbank.data.userpreferences.UserPreferencesRepositoryContract
import com.fibelatti.pigbank.data.userpreferences.UserPreferencesType
import com.fibelatti.pigbank.domain.userpreferences.models.UserPreferencesEntity
import com.fibelatti.pigbank.domain.userpreferences.models.UserPreferencesMapper
import io.reactivex.Single
import javax.inject.Inject

class UserPreferencesUseCase @Inject constructor(
    private val userPreferencesRepositoryContract: UserPreferencesRepositoryContract,
    private val userPreferencesMapper: UserPreferencesMapper
) {
    fun getUserPreferences(): Single<UserPreferencesEntity> =
        userPreferencesRepositoryContract.getAllUserPreferences()
            .map { userPreferencesMapper.toDomainModel(it) }

    fun updateAnalyticsEnabled(value: Boolean): Single<Boolean> =
        Single.create { emitter ->
            val affectedRows = userPreferencesRepositoryContract.updateAnalyticsEnabled(value.toString())
            if (affectedRows > 0) emitter.onSuccess(true) else emitter.onError(UserPreferencesError())
        }

    fun updateCrashReportsEnabled(value: Boolean): Single<Boolean> =
        Single.create { emitter ->
            val affectedRows = userPreferencesRepositoryContract.updateCrashReportsEnabled(value.toString())
            if (affectedRows > 0) emitter.onSuccess(true) else emitter.onError(UserPreferencesError())
        }

    fun setFirstGoalHintDismissed(): Single<Boolean> =
        Single.create { emitter ->
            val affectedRows = userPreferencesRepositoryContract.setFirstGoalHintDismissed()
            if (affectedRows > 0) emitter.onSuccess(true) else emitter.onError(UserPreferencesError())
        }

    fun setQuickSaveHintDismissed(): Single<Boolean> =
        Single.create { emitter ->
            val affectedRows = userPreferencesRepositoryContract.setQuickSaveHintDismissed()
            if (affectedRows > 0) emitter.onSuccess(true) else emitter.onError(UserPreferencesError())
        }

    fun resetHints(): Single<Boolean> =
        Single.create { emitter ->
            val affectedRows = userPreferencesRepositoryContract.updateAllByType(UserPreferencesType.HINT.value, false.toString())
            if (affectedRows > 0) emitter.onSuccess(true) else emitter.onError(UserPreferencesError())
        }
}

class UserPreferencesError : Throwable()
