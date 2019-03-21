package kz.brotandos.trailerman.actors.adapter

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.skydoves.baserecyclerviewadapter.BaseViewHolder
import kotlinx.android.synthetic.main.item_actor.view.*
import kz.brotandos.trailerman.actors.Actor
import kz.brotandos.trailerman.common.utils.MediaPathHelper

class ActorViewHolder(val view: View, private val delegate: Delegate) : BaseViewHolder(view) {

    interface Delegate {
        fun onItemClick(actor: Actor, view: View)
    }

    private lateinit var actor: Actor

    @Throws(Exception::class)
    override fun bindData(data: Any) {
        if (data is Actor) {
            actor = data
            drawItem()
        }
    }

    private fun drawItem() {
        itemView.run {
            actorNameTextView.text = actor.name
            actor.profileUrl?.let {
                Glide.with(context)
                    .load(MediaPathHelper.getPosterPath(it))
                    .apply(RequestOptions().circleCrop())
                    .into(actorImageView)
            }
        }
    }

    override fun onClick(view: View) = delegate.onItemClick(actor, itemView.actorImageView)

    override fun onLongClick(view: View) = false
}
