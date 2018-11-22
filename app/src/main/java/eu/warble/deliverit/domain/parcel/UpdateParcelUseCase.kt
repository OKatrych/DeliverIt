package eu.warble.deliverit.domain.parcel

import eu.warble.deliverit.domain.parcel.model.Parcel
import eu.warble.deliverit.domain.util.UseCase
import io.reactivex.Observable
import io.reactivex.Scheduler
import java.util.concurrent.Executor

class UpdateParcelUseCase(
    executor: Executor,
    scheduler: Scheduler,
    private val repository: ParcelRepository
): UseCase<Parcel, Unit>(executor, scheduler) {

    override fun buildUseCaseObservable(params: Parcel): Observable<Unit> {
        return repository.updateParcelObservable(params)
    }

}