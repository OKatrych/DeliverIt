package eu.warble.deliverit.domain.parcel.model

import android.os.Parcelable
import com.google.firebase.database.Exclude
import eu.warble.deliverit.data.parcel.model.FirebaseParcel
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

/**
 * @param status DELIVERED or DELIVER_FAILURE or IN_TRANSIT or READY_FOR_DISPATCH
 * @param deliveryConfirmation base64 image of recipient sign
 */
@Parcelize
data class Parcel(
    val id: String = "",
    var status: ParcelStatus = ParcelStatus(),
    val recipient: Recipient = Recipient(),
    var deliveryConfirmation: String = ""
): Parcelable {

    @Parcelize
    data class ParcelStatus(var status: String = "", var date: Long = -1, var details: String = ""): Parcelable {

        @Exclude
        fun getLocalDate(): LocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneId.of("Europe/Warsaw"))

    }
}

object ParcelConstants {
    val DELIVERED = "DELIVERED"
    val DELIVER_FAILURE = "DELIVER_FAILURE"
    val IN_TRANSIT = "IN_TRANSIT"
    val READY_FOR_DISPATCH = "READY_FOR_DISPATCH"

    val STATUS_LIST = listOf(DELIVERED, DELIVER_FAILURE, IN_TRANSIT, READY_FOR_DISPATCH)
}
