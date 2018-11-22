package eu.warble.deliverit.data.di

import dagger.Module
import dagger.Provides
import eu.warble.deliverit.data.parcel.ParcelRepositoryImpl
import eu.warble.deliverit.domain.parcel.GetParcelUseCase
import eu.warble.deliverit.domain.parcel.ParcelRepository
import eu.warble.deliverit.domain.parcel.UpdateParcelUseCase
import io.reactivex.Scheduler
import java.util.concurrent.Executor
import javax.inject.Named

@Module
class DataModule {

    @Provides
    fun providesParcelRepository(): ParcelRepository {
        return ParcelRepositoryImpl()
    }

    @Provides
    fun providesGetParcelUseCase(
        @Named(NETWORK_EXECUTOR_NAME) executor: Executor,
        @Named(UI_SCHEDULER_NAME) postExecutionScheduler: Scheduler,
        repository: ParcelRepository
    ): GetParcelUseCase {
        return GetParcelUseCase(
            executor, postExecutionScheduler, repository
        )
    }

    @Provides
    fun providesUpdateParcelUseCase(
        @Named(NETWORK_EXECUTOR_NAME) executor: Executor,
        @Named(UI_SCHEDULER_NAME) postExecutionScheduler: Scheduler,
        repository: ParcelRepository
    ): UpdateParcelUseCase {
        return UpdateParcelUseCase(
            executor, postExecutionScheduler, repository
        )
    }
}