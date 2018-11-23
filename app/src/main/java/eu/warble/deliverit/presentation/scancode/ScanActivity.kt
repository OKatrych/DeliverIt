package eu.warble.deliverit.presentation.scancode

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.vision.barcode.Barcode
import com.tbruyelle.rxpermissions2.RxPermissions
import eu.warble.deliverit.DeliverItApp
import eu.warble.deliverit.R
import eu.warble.deliverit.presentation.parcelinfo.ParcelInfoActivity
import eu.warble.deliverit.presentation.util.Result
import eu.warble.deliverit.util.importDB
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_scan.*
import javax.inject.Inject

class ScanActivity : AppCompatActivity() {

    @Inject
    lateinit var scanViewModelFactory: ScanViewModel.Factory

    private val viewModel: ScanViewModel by lazy {
        ViewModelProviders.of(this, scanViewModelFactory).get(ScanViewModel::class.java)
    }

    private val barcodeView by lazy { barcode_view }
    private lateinit var permissionsDisposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        //importDB(this)
        (application as DeliverItApp).presentationComponent
            .inject(this)

        permissionsDisposable = RxPermissions(this)
            .request(Manifest.permission.CAMERA)
            .subscribe { granted ->
                if (granted) {
                    startScanForCode()
                } else {
                    Toast.makeText(this, getString(R.string.grant_permissions), Toast.LENGTH_LONG)
                        .show()
                }
            }
    }

    private fun startScanForCode() {
        barcodeView.setBarcodeFormats(Barcode.QR_CODE)
        viewModel.startRecognisingCode(barcodeView.getObservable())

        viewModel.scanResult.observe(this, Observer<Result> { scanResult ->
            scanResult?.let {
                when (it) {
                    is Result.Success<*> -> {
                        goToParcelInfoActivity(it.value as String)
                    }
                    is Result.Failure -> showError(it.error)
                }
            }
        })
    }

    private fun goToParcelInfoActivity(parcelCode: String) {
        startActivity(
            Intent(this@ScanActivity, ParcelInfoActivity::class.java).apply {
                putExtra("parcelCode", parcelCode)
            }
        )
    }

    private fun showError(throwable: Throwable?) {
        Toast.makeText(this, "Error: ${throwable?.message}", Toast.LENGTH_SHORT).show()
    }
}
