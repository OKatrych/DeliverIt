package eu.warble.deliverit.util

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import eu.warble.deliverit.R
import eu.warble.deliverit.data.parcel.model.FirebaseParcel
import eu.warble.deliverit.domain.parcel.model.Parcel
import eu.warble.deliverit.domain.parcel.model.ParcelConstants
import eu.warble.deliverit.domain.parcel.model.Recipient
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import java.io.BufferedReader
import java.io.InputStreamReader

fun importDB(context: Context) {
    val auth = FirebaseAuth.getInstance()
    if (auth.currentUser != null) {
        AppExecutors.NETWORK().execute {
            sendModelToFirebase(
                csvToModel(
                    readFile(context)
                )
            )
        }
    } else {
        auth.signInAnonymously().addOnSuccessListener {
            AppExecutors.NETWORK().execute {
                sendModelToFirebase(
                    csvToModel(
                        readFile(context)
                    )
                )
            }
        }
    }
}

fun readFile(context: Context): List<String> {
    val inStream = context.applicationContext.resources.openRawResource(R.raw.list)
    val builder = mutableListOf<String>()
    BufferedReader(InputStreamReader(inStream)).use { br ->
        var tmp: String? = br.readLine()
        while (tmp != null) {
            builder.add(tmp)
            tmp = br.readLine()
        }
    }
    return builder
}

fun csvToModel(csv: List<String>): List<Parcel> {
    val result = mutableListOf<Parcel>()
    csv.forEach {
        val parcelComponents: List<String> = it.split(";")
        assert(parcelComponents.size == 9)
        result.add(
            Parcel(
                parcelComponents[0],
                Parcel.ParcelStatus(
                    ParcelConstants.READY_FOR_DISPATCH,
                    LocalDateTime.now().toLongMillis(),
                    ""
                ),
                Recipient(
                    parcelComponents[1] + " " + parcelComponents[2], // name + surname
                    if (parcelComponents[3] == "null") "" else parcelComponents[3] + " " + //street
                            if (parcelComponents[4] == "null") "" else parcelComponents[4] + " m." + //number
                                    if (parcelComponents[5] == "null") "" else parcelComponents[5] + ", " + //room nb
                                            if (parcelComponents[6] == "null") "" else parcelComponents[6] + ", " + //zip-code
                                                    if (parcelComponents[7] == "null") "" else parcelComponents[7], //city
                    parcelComponents[8] // phone nb
                ),
                ""
            )
        )
    }
    return result
}

fun sendModelToFirebase(parcelList: List<Parcel>) {
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val dbReference = database.getReference("/")
    val PARCELS = "parcels"

    parcelList.forEach { parcel ->
        val parcelQuery =
            dbReference.child(PARCELS).child(parcel.id).setValue(parcel).addOnCompleteListener {
                Log.d("UploadParcel", "Upload finished: ID=${parcel.id}")
            }
    }
}