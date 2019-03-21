package kz.brotandos.trailerman.movies.movie

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_movie_details.*
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
import kz.brotandos.trailerman.movies.Movie
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModel


class MovieDetailsActivity : AppCompatActivity(), VideoListViewHolder.Delegate {

    companion object {
        const val MOVIE_EXTRA = "MOVIE_EXTRA"
    }

    private val movieDetailsViewModel: MovieDetailsViewModel by viewModel()

    private val videoAdapter = VideoListAdapter(this)
    private val reviewAdapter = ReviewListAdapter()

    private val movie by lazy { intent.getParcelableExtra(MOVIE_EXTRA) as Movie }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        applyToolbarMargin(this, movieToolbar)
        simpleToolbarWithHome(movieToolbar, movie.title)
        movie.backdropUrl?.let {
            Glide.with(this).load(MediaPathHelper.getBackdropPath(it))
                .listener(requestGlideListener(moviePosterImageView))
                .into(moviePosterImageView)
        } ?: let {
            Glide.with(this).load(MediaPathHelper.getBackdropPath(movie.posterUrl!!))
                .listener(requestGlideListener(moviePosterImageView))
                .into(moviePosterImageView)
        }
        detailHeaderTitleTextView.text = movie.title
        detailHeaderReleaseTextView.text = "Release Date : ${movie.releaseDate}"
        detailHeaderStarRatingBar.rating = movie.voteAverage / 2
        detailBodyTrailersRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        detailBodyTrailersRecyclerView.adapter = videoAdapter
        detailBodySummaryTextView.text = movie.overview
        detailBodyReviewsRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        detailBodyReviewsRecyclerView.adapter = reviewAdapter
        detailBodyReviewsRecyclerView.isNestedScrollingEnabled = false
        detailBodyReviewsRecyclerView.setHasFixedSize(true)
    }

    private fun observeViewModel() {
        observeLiveData(movieDetailsViewModel.getKeywordListObservable()) { updateKeywordList(it) }
        movieDetailsViewModel.postKeywordId(movie.id)

        observeLiveData(movieDetailsViewModel.getVideoListObservable()) { updateVideoList(it) }
        movieDetailsViewModel.postVideoId(movie.id)

        observeLiveData(movieDetailsViewModel.getReviewListObservable()) { updateReviewList(it) }
        movieDetailsViewModel.postReviewId(movie.id)
    }

    private fun updateKeywordList(resource: Resource<List<Keyword>>) {
        when (resource.status) {
            Status.SUCCESS -> {
                resource.data?.let {
                    detailBodyTags.tags = KeywordListMapper.mapToStringList(it)
                    if (it.isNotEmpty()) detailBodyTags.show()
                }
            }
            Status.ERROR -> toast(resource.errorEnvelope?.statusMessage.toString())
            Status.LOADING -> Unit
        }
    }

    private fun updateVideoList(resource: Resource<List<Video>>) {
        when (resource.status) {
            Status.SUCCESS -> {
                videoAdapter.addVideoList(resource)

                resource.data?.let {
                    if (it.isNotEmpty()) {
                        detailBodyTrailersTextView.show()
                        detailBodyTrailersRecyclerView.show()
                    }
                }
            }
            Status.ERROR -> toast(resource.errorEnvelope?.statusMessage.toString())
            Status.LOADING -> Unit
        }
    }

    private fun updateReviewList(resource: Resource<List<Review>>) {
        when (resource.status) {
            Status.LOADING -> Unit
            Status.SUCCESS -> {
                reviewAdapter.addReviewList(resource)
                resource.data?.let {
                    if (it.isNotEmpty()) {
                        detailBodyReviewsTextView.show()
                        detailBodyReviewsRecyclerView.show()
                    }
                }
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
