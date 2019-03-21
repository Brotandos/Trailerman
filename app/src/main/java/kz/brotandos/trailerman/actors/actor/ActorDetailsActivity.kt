package kz.brotandos.trailerman.actors.actor

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_actor_details.*
import kotlinx.android.synthetic.main.layout_toolbar_default.*
import kz.brotandos.trailerman.R
import kz.brotandos.trailerman.actors.Actor
import kz.brotandos.trailerman.common.models.Resource
import kz.brotandos.trailerman.common.models.Status
import kz.brotandos.trailerman.common.observeLiveData
import kz.brotandos.trailerman.common.show
import kz.brotandos.trailerman.common.utils.MediaPathHelper
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModel


class ActorDetailsActivity : AppCompatActivity() {

    private val actorDetailsViewModel: ActorDetailsViewModel by viewModel()

    private val actor by lazy { intent.getParcelableExtra("actor") as Actor }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actor_details)
        supportPostponeEnterTransition()

        setupViews()
    }

    private fun setupViews() {
        backImageView.setOnClickListener { onBackPressed() }
        titleTextView.text = actor.name
        actor.profileUrl?.let {
            Glide.with(this).load(MediaPathHelper.getPosterPath(it))
                .apply(RequestOptions().circleCrop())
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        supportStartPostponedEnterTransition()
                        observeViewModel()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        supportStartPostponedEnterTransition()
                        observeViewModel()
                        return false
                    }
                })
                .into(actorPhotoImageView)
        }
        actorNameTextView.text = actor.name
    }

    private fun observeViewModel() {
        observeLiveData(actorDetailsViewModel.getPersonObservable()) { updatePersonDetail(it) }
        actorDetailsViewModel.postPersonId(actor.id)
    }

    private fun updatePersonDetail(resource: Resource<ActorDetails>) {
        when (resource.status) {
            Status.LOADING -> Unit
            Status.SUCCESS -> {
                resource.data?.let {
                    actorBiographyTextView.text = it.biography
                    actorTags.tags = it.alsoKnownAs
                    if (it.alsoKnownAs.isNotEmpty()) {
                        actorTags.show()
                    }
                }
            }
            Status.ERROR -> toast(resource.errorEnvelope?.statusMessage.toString())
        }
    }

    companion object {
        const val intent_requestCode = 1000

        fun startActivity(fragment: Fragment, activity: FragmentActivity, actor: Actor, view: View) {
            val intent = Intent(activity, ActorDetailsActivity::class.java)
            ViewCompat.getTransitionName(view)?.let {
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, it)
                intent.putExtra("actor", actor)
                activity.startActivityFromFragment(fragment, intent,
                    intent_requestCode, options.toBundle())
            }
        }
    }
}
