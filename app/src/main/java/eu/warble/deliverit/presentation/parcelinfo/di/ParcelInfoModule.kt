package eu.warble.deliverit.presentation.parcelinfo.di

import dagger.Module
import dagger.Provides
import eu.warble.deliverit.domain.parcel.GetParcelUseCase
import eu.warble.deliverit.domain.parcel.UpdateParcelUseCase
import eu.warble.deliverit.presentation.parcelinfo.ParcelInfoViewModelFactory

@Module
class ParcelInfoModule {

    @Provides
    fun provideParcelInfoFactory(
        getParcelUseCase: GetParcelUseCase,
        updateParcelUseCase: UpdateParcelUseCase
    ): ParcelInfoViewModelFactory {
        return ParcelInfoViewModelFactory(getParcelUseCase, updateParcelUseCase)
    }
}