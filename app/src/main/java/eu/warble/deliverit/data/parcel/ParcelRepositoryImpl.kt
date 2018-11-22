package eu.warble.deliverit.data.parcel

import com.google.firebase.database.*
import durdinapps.rxfirebase2.RxFirebaseDatabase
import eu.warble.deliverit.domain.parcel.model.Parcel
import eu.warble.deliverit.domain.parcel.ParcelRepository
import io.reactivex.Observable

class ParcelRepositoryImpl : ParcelRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val dbReference = database.getReference("/")

    private val PARCELS = "parcels"

    override fun getParcelObservable(parcelId: String): Observable<Parcel> {
        val parcelQuery = dbReference.child(PARCELS).child(parcelId)

        return RxFirebaseDatabase
            .observeSingleValueEvent(parcelQuery, Parcel::class.java)
            .toObservable()
    }

    override fun updateParcelObservable(parcel: Parcel): Observable<Unit> {
        val updateQuery = dbReference.child(PARCELS).child(parcel.id)
        return RxFirebaseDatabase
            .setValue(updateQuery, parcel)
            .toObservable()
    }
}