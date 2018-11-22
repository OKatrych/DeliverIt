package eu.warble.deliverit.domain.parcel.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Recipient(val name: String = "", val address: String = "", val phoneNumber: String = ""): Parcelable