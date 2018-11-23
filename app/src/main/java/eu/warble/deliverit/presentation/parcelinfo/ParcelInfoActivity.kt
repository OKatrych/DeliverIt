package eu.warble.deliverit.presentation.parcelinfo

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import eu.warble.deliverit.DeliverItApp
import eu.warble.deliverit.R
import eu.warble.deliverit.domain.parcel.model.Parcel
import eu.warble.deliverit.presentation.util.Result
import kotlinx.android.synthetic.main.activity_parcel_info.*
import javax.inject.Inject

class ParcelInfoActivity : AppCompatActivity() {

    @Inject
    lateinit var parcelInfoViewModelFactory: ParcelInfoViewModelFactory

    val viewModel: ParcelInfoViewModel by lazy {
        ViewModelProviders.of(this, parcelInfoViewModelFactory).get(ParcelInfoViewModel::class.java)
    }

    private val loadingView by lazy { loading_view }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parcel_info)

        (application as DeliverItApp).presentationComponent
            .inject(this)

        initLoadingObserver()

        loadData(intent?.extras?.getString("parcelCode"))
    }

    private fun initLoadingObserver() {
        viewModel.loadingObserver.observe(this, Observer<LoadingStatus> { loadingStatus ->
            loadingStatus?.let {
                when(loadingStatus) {
                    LoadingStatus.Loading -> loadingView.visibility = View.VISIBLE
                    LoadingStatus.NotLoading -> loadingView.visibility = View.GONE
                }
            }
        })
    }

    private fun loadData(parcelId: String?) {
        parcelId?.let {
            viewModel.loadParcelInfo(it)
            viewModel.parcelInfoResult.observe(this, Observer<Result> { parcelResult ->
                parcelResult?.let { result ->
                    when (result) {
                        is Result.Success<*> -> fillViews(result.value as Parcel)
                        is Result.Failure -> showError(result.error)
                    }
                }
            })
        }
    }

    fun updateData(parcel: Parcel) {
        viewModel.updateParcelInfo(parcel)
        viewModel.parcelUpdateResult.observe(this, Observer<Result> { updateResult ->
            updateResult?.let {
                when(updateResult) {
                    is Result.Success<*> -> fillViews(updateResult.value as Parcel)
                    is Result.Failure -> showError(updateResult.error)
                }
            }
        })
    }

    private fun fillViews(parcel: Parcel) {
        tv_parcel_destination.text = parcel.recipient.address
        tv_parcel_recipient.text = parcel.recipient.name + "\n" + parcel.recipient.phoneNumber
        tv_parcel_status.text = parcel.status.status
        val date = parcel.status.getLocalDate()
        tv_parcel_date.text = "${date.dayOfMonth}.${date.monthValue}.${date.year} : ${date.hour}:${date.minute}"
        tv_parcel_info.text = parcel.status.details
        btn_change_status.setOnClickListener {
            showEditParcelDialog(parcel)
        }
    }

    private fun showEditParcelDialog(parcel: Parcel) {
        EditParcelFragmentDialog.newInstance(parcel).show(supportFragmentManager, "EditParcelFragmentDialog")
    }

    private fun showError(throwable: Throwable?) {
        Toast.makeText(this, "Error: ${throwable?.message}", Toast.LENGTH_SHORT).show()
    }
}
