package eu.warble.deliverit

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import eu.warble.deliverit.di.AppModule
import eu.warble.deliverit.presentation.scancode.di.ScanCodeModule
import eu.warble.deliverit.di.DaggerAppComponent
import eu.warble.deliverit.presentation.parcelinfo.di.ParcelInfoModule

class DeliverItApp: Application() {

    val appComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    val presentationComponent by lazy {
        appComponent.plus(
            ScanCodeModule(),
            ParcelInfoModule()
        )
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}