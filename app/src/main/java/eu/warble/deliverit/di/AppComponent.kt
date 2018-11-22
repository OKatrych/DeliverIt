package eu.warble.deliverit.di

import dagger.Component
import eu.warble.deliverit.data.di.DataModule
import eu.warble.deliverit.presentation.di.PresentationComponent
import eu.warble.deliverit.presentation.parcelinfo.di.ParcelInfoModule
import eu.warble.deliverit.presentation.scancode.di.ScanCodeModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class])
interface AppComponent {


    fun plus(
        scanCodeModule: ScanCodeModule,
        parcelInfoModule: ParcelInfoModule
    ): PresentationComponent
}