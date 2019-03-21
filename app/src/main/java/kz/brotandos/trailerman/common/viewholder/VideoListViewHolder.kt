package kz.brotandos.trailerman.common.viewholder

import android.view.View
import com.bumptech.glide.Glide
import com.github.florent37.glidepalette.BitmapPalette
import com.github.florent37.glidepalette.GlidePalette
import com.skydoves.baserecyclerviewadapter.BaseViewHolder
import kotlinx.android.synthetic.main.item_video.view.*
import kz.brotandos.trailerman.common.models.Video
import kz.brotandos.trailerman.common.utils.MediaPathHelper


class VideoListViewHolder(val view: View, private val delegate: Delegate) : BaseViewHolder(view) {

    interface Delegate {
        fun onItemClicked(video: Video)
    }

    private lateinit var video: Video

    override fun bindData(data: Any) {
        if (data is Video) {
            video = data
            drawItem()
        }
    }

    private fun drawItem() {
        itemView.run {
            videoTitleTextView.text = video.name
            Glide.with(context)
                .load(MediaPathHelper.getYoutubeThumbnailPath(video.key))
                .listener(
                    GlidePalette.with(MediaPathHelper.getYoutubeThumbnailPath(video.key))
                        .use(BitmapPalette.Profile.VIBRANT)
                        .intoBackground(videoPalette)
                        .crossfade(true)
                )
                .into(videoCoverImageView)
        }
    }

    override fun onClick(v: View?) = delegate.onItemClicked(video)

    override fun onLongClick(v: View?) = false
}
