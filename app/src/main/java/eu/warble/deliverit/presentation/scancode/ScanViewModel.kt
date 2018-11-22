package eu.warble.deliverit.presentation.scancode

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.google.android.gms.vision.barcode.Barcode
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModelProvider
import eu.warble.deliverit.presentation.util.Result
import java.lang.Exception
import java.lang.IllegalArgumentException

class ScanViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val mScanResult = MutableLiveData<Result>()

    /**
     * Views - Fragments and Activities - shouldn’t be able of updating LiveData and thus their own state
     * because that’s the responsibility of ViewModels. Views should be able to only observe LiveData.
     */
    val scanResult: LiveData<Result> = mScanResult

    fun startRecognisingCode(scanCodeObservable: Observable<Barcode>) {
        compositeDisposable.add(
            scanCodeObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { barcode ->
                        if (isCodeValid(barcode.displayValue)) {
                            mScanResult.value = Result.Success(barcode.displayValue)
                        } else {
                            mScanResult.value = Result.Failure(Throwable("Wrong QR code"))
                        }
                    },
                    { throwable ->
                        mScanResult.value = Result.Failure(throwable)
                    }
                )
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

    private fun isCodeValid(code: String): Boolean {
        return code.toIntOrNull() != null
    }

    class Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ScanViewModel::class.java)) {
                return ScanViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}