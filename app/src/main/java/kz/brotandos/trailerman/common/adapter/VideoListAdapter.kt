package kz.brotandos.trailerman.common.adapter

import android.view.View
import com.skydoves.baserecyclerviewadapter.BaseAdapter
import com.skydoves.baserecyclerviewadapter.BaseViewHolder
import com.skydoves.baserecyclerviewadapter.SectionRow
import kz.brotandos.trailerman.R
import kz.brotandos.trailerman.common.models.Resource
import kz.brotandos.trailerman.common.models.Video
import kz.brotandos.trailerman.common.viewholder.VideoListViewHolder


class VideoListAdapter(private val delegate: VideoListViewHolder.Delegate) : BaseAdapter() {

    init {
        addSection(ArrayList<Video>())
    }

    fun addVideoList(resource: Resource<List<Video>>) {
        resource.data?.let {
            sections[0].addAll(it)
        }
        notifyDataSetChanged()
    }

    override fun layout(sectionRow: SectionRow): Int = R.layout.item_video

    override fun viewHolder(layout: Int, view: View): BaseViewHolder =
        VideoListViewHolder(view, delegate)
}
