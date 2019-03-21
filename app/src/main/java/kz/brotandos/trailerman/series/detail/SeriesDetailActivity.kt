package kz.brotandos.trailerman.series.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_series_details.*
import kotlinx.android.synthetic.main.layout_detail_body.*
import kotlinx.android.synthetic.main.layout_detail_header.*
import kz.brotandos.trailerman.R
import kz.brotandos.trailerman.common.adapter.ReviewListAdapter
import kz.brotandos.trailerman.common.adapter.VideoListAdapter
import kz.brotandos.trailerman.common.models.*
import kz.brotandos.trailerman.common.observeLiveData
import kz.brotandos.trailerman.common.show
import kz.brotandos.trailerman.common.simpleToolbarWithHome
import kz.brotandos.trailerman.common.utils.KeywordListMapper
import kz.brotandos.trailerman.common.utils.MediaPathHelper
import kz.brotandos.trailerman.common.utils.applyToolbarMargin
import kz.brotandos.trailerman.common.utils.requestGlideListener
import kz.brotandos.trailerman.common.viewholder.VideoListViewHolder
import kz.brotandos.trailerman.series.Series
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModel


class SeriesDetailActivity : AppCompatActivity(), VideoListViewHolder.Delegate {

    companion object {
        const val SERIES_EXTRA = "SERIES_EXTRA"
    }

    private val seriesDetailViewModel: SeriesDetailViewModel by viewModel()

    private val videoAdapter = VideoListAdapter(this)
    private val reviewAdapter = ReviewListAdapter()

    private val series by lazy { intent.getParcelableExtra(SERIES_EXTRA) as Series }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_series_details)

        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        applyToolbarMargin(this, seriesDetailToolbar)
        simpleToolbarWithHome(seriesDetailToolbar, series.name)
        series.backdropUrl?.let {
            Glide.with(this).load(MediaPathHelper.getBackdropPath(it))
                .listener(requestGlideListener(seriesDetailPosterImageView))
                .into(seriesDetailPosterImageView)
        } ?: let {
            Glide.with(this).load(MediaPathHelper.getBackdropPath(series.posterUrl))
                .listener(requestGlideListener(seriesDetailPosterImageView))
                .into(seriesDetailPosterImageView)
        }
        detailHeaderTitleTextView.text = series.name
        detailHeaderReleaseTextView.text = "First Air Date : ${series.firstAirDate}"
        detailHeaderStarRatingBar.rating = series.voteAverage / 2
        detailBodySummaryTextView.text = series.overview
        detailBodyTrailersRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@SeriesDetailActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = videoAdapter
        }
        detailBodyReviewsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@SeriesDetailActivity, RecyclerView.VERTICAL, false)
            adapter = reviewAdapter
            isNestedScrollingEnabled = false
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {
        observeLiveData(seriesDetailViewModel.getKeywordListObservable(), ::updateKeywordList)
        seriesDetailViewModel.postKeywordId(series.id)

        observeLiveData(seriesDetailViewModel.getVideoListObservable(), ::updateVideoList)
        seriesDetailViewModel.postVideoId(series.id)

        observeLiveData(seriesDetailViewModel.getReviewListObservable(), ::updateReviewList)
        seriesDetailViewModel.postReviewId(series.id)
    }

    private fun updateKeywordList(resource: Resource<List<Keyword>>) {
        when (resource.status) {
            Status.LOADING -> Unit
            Status.SUCCESS -> resource.data?.let {
                if (it.isEmpty()) return@let
                detailBodyTags.tags = KeywordListMapper.mapToStringList(it)
                detailBodyTags.show()
            }
            Status.ERROR -> toast(resource.errorEnvelope?.statusMessage.toString())
        }
    }

    private fun updateVideoList(resource: Resource<List<Video>>) {
        when (resource.status) {
            Status.LOADING -> Unit
            Status.SUCCESS -> resource.data?.let {
                if (it.isEmpty()) return@let
                videoAdapter.addVideoList(resource)
                detailBodyTrailersTextView.show()
                detailBodyReviewsRecyclerView.show()
            }
            Status.ERROR -> toast(resource.errorEnvelope?.statusMessage.toString())
        }
    }

    private fun updateReviewList(resource: Resource<List<Review>>) {
        when (resource.status) {
            Status.LOADING -> Unit
            Status.SUCCESS -> resource.data?.let {
                if (it.isEmpty()) return@let
                reviewAdapter.addReviewList(resource)
                detailBodyReviewsTextView.show()
                detailBodyReviewsRecyclerView.show()
            }
            Status.ERROR -> toast(resource.errorEnvelope?.statusMessage.toString())
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) onBackPressed()
        return false
    }

    override fun onItemClicked(video: Video) {
        val playVideoIntent = Intent(Intent.ACTION_VIEW, Uri.parse(MediaPathHelper.getYoutubeVideoPath(video.key)))
        startActivity(playVideoIntent)
    }
}
