package kz.brotandos.trailerman.actors.adapter

import android.view.View
import com.skydoves.baserecyclerviewadapter.BaseAdapter
import com.skydoves.baserecyclerviewadapter.BaseViewHolder
import com.skydoves.baserecyclerviewadapter.SectionRow
import kz.brotandos.trailerman.R
import kz.brotandos.trailerman.actors.Actor
import kz.brotandos.trailerman.common.models.Resource

class ActorsAdapter(val delegate: ActorViewHolder.Delegate) : BaseAdapter() {

    init {
        addSection(ArrayList<Actor>())
    }

    fun addPeople(resource: Resource<List<Actor>>) {
        resource.data?.let {
            sections.first().addAll(resource.data)
            notifyDataSetChanged()
        }
    }

    override fun layout(sectionRow: SectionRow): Int = R.layout.item_actor

    override fun viewHolder(layout: Int, view: View): BaseViewHolder =
        ActorViewHolder(view, delegate)
}
