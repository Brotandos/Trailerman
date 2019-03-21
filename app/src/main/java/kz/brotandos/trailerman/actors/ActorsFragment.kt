package kz.brotandos.trailerman.actors

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.skydoves.baserecyclerviewadapter.RecyclerViewPaginator
import kotlinx.android.synthetic.main.fragment_actors.*
import kz.brotandos.trailerman.R
import kz.brotandos.trailerman.actors.actor.ActorDetailsActivity
import kz.brotandos.trailerman.actors.adapter.ActorsAdapter
import kz.brotandos.trailerman.actors.adapter.ActorViewHolder
import kz.brotandos.trailerman.common.models.Resource
import kz.brotandos.trailerman.common.models.Status
import kz.brotandos.trailerman.common.observeLiveData
import kz.brotandos.trailerman.main.MainActivityViewModel
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import org.koin.android.viewmodel.ext.android.sharedViewModel

class ActorsFragment : Fragment(), ActorViewHolder.Delegate {

    private val mainActivityViewModel: MainActivityViewModel by sharedViewModel()

    private val peopleAdapter = ActorsAdapter(this)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        observeViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_actors, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.apply {
            layoutManager = GridLayoutManager(context, getGridSpanCount())
            adapter = peopleAdapter
            RecyclerViewPaginator(
                recyclerView = this,
                isLoading = { mainActivityViewModel.getPeopleValues()?.status == Status.LOADING },
                loadMore = mainActivityViewModel::postPeoplePage,
                onLast = { mainActivityViewModel.getPeopleValues()?.onLastPage!! })
        }
    }

    private fun observeViewModel() {
        observeLiveData(mainActivityViewModel.getPeopleObservable(), ::updateActors)
        mainActivityViewModel.postPeoplePage(1)
    }

    private fun updateActors(resource: Resource<List<Actor>>) {
        when (resource.status) {
            Status.LOADING -> Unit
            Status.SUCCESS -> peopleAdapter.addPeople(resource)
            Status.ERROR -> toast(resource.errorEnvelope?.statusMessage.toString())
        }
    }

    private fun getGridSpanCount() =
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 4

    override fun onItemClick(actor: Actor, view: View) {
        activity?.let {
            ActorDetailsActivity.startActivity(this, it, actor, view)
        } ?: startActivity<ActorDetailsActivity>("actor" to actor)
    }
}
