package eu.warble.deliverit.domain.parcel

import eu.warble.deliverit.domain.parcel.model.Parcel
import eu.warble.deliverit.domain.util.UseCase
import io.reactivex.Observable
import io.reactivex.Scheduler
import java.util.concurrent.Executor

class GetParcelUseCase constructor(
    executor: Executor,
    scheduler: Scheduler,
    private val repository: ParcelRepository
): UseCase<String, Parcel>(executor, scheduler) {

    /**
     * @return Observable for Parcel
     * @param params parcel (barcode ID)
     */
    override fun buildUseCaseObservable(params: String): Observable<Parcel> {
        return repository.getParcelObservable(params)
    }
}