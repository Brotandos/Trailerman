package kz.brotandos.trailerman.series.adapter

import android.view.View
import com.bumptech.glide.Glide
import com.github.florent37.glidepalette.BitmapPalette
import com.github.florent37.glidepalette.GlidePalette
import com.skydoves.baserecyclerviewadapter.BaseViewHolder
import kotlinx.android.synthetic.main.item_poster.view.*
import kz.brotandos.trailerman.common.utils.MediaPathHelper
import kz.brotandos.trailerman.series.Series


class SeriesViewHolder(val view: View, private val delegate: Delegate) : BaseViewHolder(view) {

    interface Delegate {
        fun onItemClick(series: Series)
    }

    private lateinit var series: Series

    @Throws(Exception::class)
    override fun bindData(data: Any) {
        if (data is Series) {
            series = data
            drawItem()
        }
    }

    private fun drawItem() {
        itemView.run {
            posterTitleTextView.text = series.name
            series.posterUrl.let {
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

    override fun onClick(view: View) = delegate.onItemClick(series)

    override fun onLongClick(view: View) = false
}
