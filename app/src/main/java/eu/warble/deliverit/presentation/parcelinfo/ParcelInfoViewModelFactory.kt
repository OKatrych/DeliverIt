package eu.warble.deliverit.presentation.parcelinfo

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import eu.warble.deliverit.domain.parcel.GetParcelUseCase
import eu.warble.deliverit.domain.parcel.UpdateParcelUseCase
import java.lang.IllegalArgumentException
import javax.inject.Inject

class ParcelInfoViewModelFactory(
    private val getParcelUseCase: GetParcelUseCase,
    private val updateParcelUseCase: UpdateParcelUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ParcelInfoViewModel::class.java)) {
            return ParcelInfoViewModel(
                getParcelUseCase,
                updateParcelUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}