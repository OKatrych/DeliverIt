package eu.warble.deliverit.presentation.scancode.di

import dagger.Module
import dagger.Provides
import eu.warble.deliverit.presentation.scancode.ScanViewModel

@Module
class ScanCodeModule {

    @Provides
    fun provideScanViewModelFactory(): ScanViewModel.Factory {
        return ScanViewModel.Factory()
    }
}