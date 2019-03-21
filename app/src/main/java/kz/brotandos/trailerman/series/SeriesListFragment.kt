package kz.brotandos.trailerman.series

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.skydoves.baserecyclerviewadapter.RecyclerViewPaginator
import kotlinx.android.synthetic.main.fragment_series_list.*
import kz.brotandos.trailerman.R
import kz.brotandos.trailerman.common.models.Resource
import kz.brotandos.trailerman.common.models.Status
import kz.brotandos.trailerman.common.observeLiveData
import kz.brotandos.trailerman.main.MainActivityViewModel
import kz.brotandos.trailerman.series.adapter.SeriesListAdapter
import kz.brotandos.trailerman.series.adapter.SeriesViewHolder
import kz.brotandos.trailerman.series.detail.SeriesDetailActivity
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import org.koin.android.viewmodel.ext.android.sharedViewModel

class SeriesListFragment : Fragment(), SeriesViewHolder.Delegate {

    private val mainActivityViewModel: MainActivityViewModel by sharedViewModel()

    private val seriesListAdapter = SeriesListAdapter(this)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        observeViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_series_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isPortrait = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        recyclerView.apply {
            layoutManager = GridLayoutManager(context, if (isPortrait) 2 else 4)
            adapter = seriesListAdapter
            RecyclerViewPaginator(
                recyclerView = this,
                isLoading = { mainActivityViewModel.getSeriesListValues()?.status == Status.LOADING },
                loadMore = mainActivityViewModel::postSeriesPage,
                onLast = { mainActivityViewModel.getSeriesListValues()?.onLastPage!! }
            ).also {
                it.currentPage = 1
            }
        }
    }

    private fun observeViewModel() {
        observeLiveData(mainActivityViewModel.getTvListObservable(), ::updateSeriesList)
        mainActivityViewModel.postSeriesPage(1)
    }

    private fun updateSeriesList(resource: Resource<List<Series>>) {
        when (resource.status) {
            Status.LOADING -> Unit
            Status.SUCCESS -> seriesListAdapter.addTvList(resource)
            Status.ERROR -> toast(resource.errorEnvelope?.statusMessage.toString())
        }
    }

    override fun onItemClick(series: Series) =
        startActivity<SeriesDetailActivity>(SeriesDetailActivity.SERIES_EXTRA to series)
}
