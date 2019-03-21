package kz.brotandos.trailerman.common.viewholder

import android.view.View
import com.skydoves.baserecyclerviewadapter.BaseViewHolder
import kotlinx.android.synthetic.main.item_review.view.*
import kz.brotandos.trailerman.common.models.Review


class ReviewListViewHolder(val view: View) : BaseViewHolder(view) {

    private lateinit var review: Review

    override fun bindData(data: Any) {
        if (data is Review) {
            review = data
            drawItem()
        }
    }

    private fun drawItem() {
        itemView.run {
            reviewTitleTextView.text = review.author
            reviewContentTextView.text = review.content
        }
    }

    override fun onClick(v: View?) = Unit

    override fun onLongClick(v: View?) = false
}
