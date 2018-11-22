package eu.warble.deliverit.presentation.parcelinfo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import eu.warble.deliverit.domain.parcel.GetParcelUseCase
import eu.warble.deliverit.domain.parcel.UpdateParcelUseCase
import eu.warble.deliverit.domain.parcel.model.Parcel
import eu.warble.deliverit.presentation.util.Result
import io.reactivex.observers.DisposableObserver

class ParcelInfoViewModel(
    private val getParcelUseCase: GetParcelUseCase,
    private val updateParcelUseCase: UpdateParcelUseCase
) : ViewModel() {

    private val mParcelInfoResult = MutableLiveData<Result>()
    val parcelInfoResult: LiveData<Result> = mParcelInfoResult

    private val mParcelUpdateResult = MutableLiveData<Result>()
    val parcelUpdateResult: LiveData<Result> = mParcelUpdateResult

    private val mLoadingObserver = MutableLiveData<LoadingStatus>()
    val loadingObserver: LiveData<LoadingStatus> = mLoadingObserver


    /**
     * @param forceLoad true - when need to reload data
     */
    fun loadParcelInfo(parcelId: String, forceLoad: Boolean = false) {
        mLoadingObserver.value = LoadingStatus.Loading
        if (forceLoad || parcelInfoResult.value == null) {
            getParcelUseCase.execute(parcelId, object : DisposableObserver<Parcel>() {
                override fun onNext(parcel: Parcel) {
                    mParcelInfoResult.value = Result.Success(parcel)
                    mLoadingObserver.value = LoadingStatus.NotLoading
                }

                override fun onError(e: Throwable) {
                    mParcelInfoResult.value = Result.Failure(e)
                    mLoadingObserver.value = LoadingStatus.NotLoading
                }

                override fun onComplete() {}
            })
        }
    }

    fun updateParcelInfo(parcel: Parcel) {
        mLoadingObserver.value = LoadingStatus.Loading
        updateParcelUseCase.execute(parcel, object : DisposableObserver<Unit>() {
            override fun onComplete() {
                mParcelUpdateResult.value = Result.Success(parcel)
                mLoadingObserver.value = LoadingStatus.NotLoading
            }

            override fun onNext(t: Unit) {}

            override fun onError(e: Throwable) {
                mParcelInfoResult.value = Result.Failure(e)
                mLoadingObserver.value = LoadingStatus.NotLoading
            }
        })
    }

    override fun onCleared() {
        getParcelUseCase.dispose()
        updateParcelUseCase.dispose()
    }
}