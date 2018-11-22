package eu.warble.deliverit.presentation.di

import dagger.Subcomponent
import eu.warble.deliverit.presentation.parcelinfo.ParcelInfoActivity
import eu.warble.deliverit.presentation.parcelinfo.di.ParcelInfoModule
import eu.warble.deliverit.presentation.scancode.ScanActivity
import eu.warble.deliverit.presentation.scancode.di.ScanCodeModule

@Subcomponent(modules = [ScanCodeModule::class, ParcelInfoModule::class])
interface PresentationComponent {

    fun inject(activity: ScanActivity)
    fun inject(activity: ParcelInfoActivity)

}