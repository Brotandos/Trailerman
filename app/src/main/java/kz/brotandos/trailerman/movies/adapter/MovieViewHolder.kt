package kz.brotandos.trailerman.movies.adapter

import android.view.View
import com.bumptech.glide.Glide
import com.github.florent37.glidepalette.BitmapPalette
import com.github.florent37.glidepalette.GlidePalette
import com.skydoves.baserecyclerviewadapter.BaseViewHolder
import kotlinx.android.synthetic.main.item_poster.view.*
import kz.brotandos.trailerman.common.utils.MediaPathHelper
import kz.brotandos.trailerman.movies.Movie

class MovieViewHolder(view: View, private val delegate: Delegate) : BaseViewHolder(view) {

    interface Delegate {
        fun onItemClick(movie: Movie)
    }

    private lateinit var movie: Movie

    @Throws(Exception::class)
    override fun bindData(data: Any) {
        if (data is Movie) {
            movie = data
            drawItem()
        }
    }

    private fun drawItem() {
        itemView.run {
            posterTitleTextView.text = movie.title
            movie.posterUrl?.let {
                Glide.with(context)
                    .load(MediaPathHelper.getPosterPath(it))
                    .listener(
                        GlidePalette.with(MediaPathHelper.getPosterPath(it))
                            .use(BitmapPalette.Profile.VIBRANT)
                            .intoBackground(posterPalette)
                            .crossfade(true)
                    )
                    .into(posterImageView)
            }
        }
    }

    override fun onClick(v: View?) {
        delegate.onItemClick(movie)
    }

    override fun onLongClick(v: View?) = false
}
