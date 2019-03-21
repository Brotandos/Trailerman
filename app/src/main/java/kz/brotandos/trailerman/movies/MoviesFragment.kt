package kz.brotandos.trailerman.movies

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.skydoves.baserecyclerviewadapter.RecyclerViewPaginator
import kotlinx.android.synthetic.main.fragment_movies.*
import kz.brotandos.trailerman.R
import kz.brotandos.trailerman.common.models.Resource
import kz.brotandos.trailerman.common.models.Status
import kz.brotandos.trailerman.movies.movie.MovieDetailsActivity
import kz.brotandos.trailerman.main.MainActivityViewModel
import kz.brotandos.trailerman.movies.adapter.MovieListAdapter
import kz.brotandos.trailerman.movies.adapter.MovieViewHolder
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MoviesFragment : Fragment(), MovieViewHolder.Delegate {

    private val mainActivityViewModel: MainActivityViewModel by sharedViewModel()

    private val movieListAdapter = MovieListAdapter(this)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        observeViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_movies, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isPortrait = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        recyclerView.apply {
            layoutManager = GridLayoutManager(context, if (isPortrait) 2 else 4)
            adapter = movieListAdapter
            RecyclerViewPaginator(
                recyclerView = this,
                isLoading = { mainActivityViewModel.getMovieListValues()?.status == Status.LOADING },
                loadMore = mainActivityViewModel::postMoviePage,
                onLast = { mainActivityViewModel.getMovieListValues()?.onLastPage!! }
            ).also {
                it.currentPage = 1
            }
        }
    }

    private fun observeViewModel() {
        mainActivityViewModel.getMovieListObservable().observe(this, Observer(::updateMovieList))
        mainActivityViewModel.postMoviePage(1)
    }

    private fun updateMovieList(resource: Resource<List<Movie>>) {
        when (resource.status) {
            Status.LOADING -> Unit
            Status.SUCCESS -> movieListAdapter.addMovieList(resource)
            Status.ERROR -> toast(resource.errorEnvelope?.statusMessage.toString())
        }
    }

    override fun onItemClick(movie: Movie) {
        startActivity<MovieDetailsActivity>(MovieDetailsActivity.MOVIE_EXTRA to movie)
    }
}
