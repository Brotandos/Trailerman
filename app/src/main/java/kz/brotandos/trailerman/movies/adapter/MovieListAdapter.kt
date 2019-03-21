package kz.brotandos.trailerman.movies.adapter

import android.view.View
import com.skydoves.baserecyclerviewadapter.BaseAdapter
import com.skydoves.baserecyclerviewadapter.BaseViewHolder
import com.skydoves.baserecyclerviewadapter.SectionRow
import kz.brotandos.trailerman.R
import kz.brotandos.trailerman.common.models.Resource
import kz.brotandos.trailerman.movies.Movie

class MovieListAdapter(private val delegate: MovieViewHolder.Delegate) : BaseAdapter() {

    init {
        addSection(ArrayList<Movie>())
    }

    fun addMovieList(resource: Resource<List<Movie>>) {
        resource.data?.let {
            sections.first().addAll(it)
            notifyDataSetChanged()
        }
    }

    override fun layout(sectionRow: SectionRow): Int = R.layout.item_poster

    override fun viewHolder(layout: Int, view: View): BaseViewHolder =
        MovieViewHolder(view, delegate)
}
