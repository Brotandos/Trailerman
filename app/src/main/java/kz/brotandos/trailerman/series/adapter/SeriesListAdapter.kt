package kz.brotandos.trailerman.series.adapter

import android.view.View
import com.skydoves.baserecyclerviewadapter.BaseAdapter
import com.skydoves.baserecyclerviewadapter.BaseViewHolder
import com.skydoves.baserecyclerviewadapter.SectionRow
import kz.brotandos.trailerman.R
import kz.brotandos.trailerman.common.models.Resource
import kz.brotandos.trailerman.series.Series

class SeriesListAdapter(private val delegate: SeriesViewHolder.Delegate) : BaseAdapter() {

    init {
        addSection(ArrayList<Series>())
    }

    fun addTvList(resource: Resource<List<Series>>) {
        resource.data?.let {
            sections.first().addAll(it)
            notifyDataSetChanged()
        }
    }

    override fun layout(sectionRow: SectionRow): Int {
        return R.layout.item_poster
    }

    override fun viewHolder(layout: Int, view: View): BaseViewHolder {
        return SeriesViewHolder(view, delegate)
    }
}
