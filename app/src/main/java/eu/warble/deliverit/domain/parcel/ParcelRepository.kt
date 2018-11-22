package eu.warble.deliverit.domain.parcel

import eu.warble.deliverit.domain.parcel.model.Parcel
import io.reactivex.Observable


interface ParcelRepository {

    fun getParcelObservable(parcelId: String): Observable<Parcel>

    fun updateParcelObservable(parcel: Parcel): Observable<Unit>

}