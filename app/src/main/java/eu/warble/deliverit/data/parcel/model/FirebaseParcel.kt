package eu.warble.deliverit.data.parcel.model

import eu.warble.deliverit.domain.parcel.model.ParcelConstants
import eu.warble.deliverit.domain.parcel.model.Recipient
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

/**
 * Firebase model of Parcel with simple data types
 *
 * @param status DELIVERED or DELIVER_FAILURE or IN_TRANSIT or READY_FOR_DISPATCH
 * @param deliveryConfirmation base64 image of recipient sign
 */
data class FirebaseParcel(
    val id: String,
    var status: ParcelStatus,
    val recipient: Recipient,
    var deliveryConfirmation: String
) {

    data class ParcelStatus(val status: String, val date: Long, val details: String)

}