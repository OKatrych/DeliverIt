package eu.warble.deliverit.presentation.parcelinfo

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import eu.warble.deliverit.R
import eu.warble.deliverit.domain.parcel.model.Parcel
import eu.warble.deliverit.domain.parcel.model.ParcelConstants
import eu.warble.deliverit.util.toLongMillis
import kotlinx.android.synthetic.main.change_status.view.*
import kotlinx.android.synthetic.main.signature.view.*
import org.threeten.bp.LocalDateTime
import android.util.Base64
import android.widget.*
import java.io.ByteArrayOutputStream


class EditParcelFragmentDialog: DialogFragment(), AdapterView.OnItemSelectedListener {

    private lateinit var parcel: Parcel
    private lateinit var spinner: Spinner
    private lateinit var additionalText: EditText

    companion object {
        fun newInstance(parcel: Parcel): EditParcelFragmentDialog {
            return EditParcelFragmentDialog().apply {
                arguments = Bundle().apply {
                    putParcelable("parcel", parcel)
                }
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        parcel = arguments?.getParcelable("parcel")!!
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity?.layoutInflater?.inflate(R.layout.change_status, null)
        view?.let {
            val spinnerAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, ParcelConstants.STATUS_LIST)
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner = it.spinner_parcel_status
            spinner.adapter = spinnerAdapter
            spinner.setSelection(spinnerAdapter.getPosition(parcel.status.status))
            spinner.onItemSelectedListener = this

            additionalText = it.tv_addition_info
            additionalText.text.append(parcel.status.details)

            it.btn_save.setOnClickListener {
                updateParcel()
            }

            it.btn_cancel.setOnClickListener {
                dismiss()
            }
        }
        return AlertDialog.Builder(activity)
            .setView(view)
            .create()
    }

    private fun updateParcel() {
        parcel.status.details = additionalText.text.toString()
        (activity as ParcelInfoActivity).updateData(parcel)
        dismiss()
    }

    /**
     * On Spinner item selected
     */
    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val item = parent.getItemAtPosition(position) as String
        //only if status changed
        if(item != parcel.status.status) {
            when (item) {
                ParcelConstants.DELIVERED -> {
                    showSignDialog()
                }
                else -> {
                    parcel.deliveryConfirmation = ""
                    parcel.status = Parcel.ParcelStatus(item, LocalDateTime.now().toLongMillis())
                }
            }
        }
    }

    private fun showSignDialog() {
        val view = activity?.layoutInflater?.inflate(R.layout.signature, null)
        view?.let {
            val signature = it.signature_view
            AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(false)
                .setPositiveButton("Save") { dialog, _ ->
                    if (!signature.isEmpty) {
                        parcel.deliveryConfirmation = signature.signatureSvg
                        parcel.status = Parcel.ParcelStatus(ParcelConstants.DELIVERED, LocalDateTime.now().toLongMillis())
                        dialog.dismiss()
                    }
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    spinner.setSelection((spinner.adapter as ArrayAdapter<String>).getPosition(parcel.status.status))
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //NO-OP
    }

    private fun signatureToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}