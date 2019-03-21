package kz.brotandos.trailerman.actors.actor

import kz.brotandos.trailerman.common.mappers.NetworkResponseMapper

class ActorDetailsResponseMapper :
    NetworkResponseMapper<ActorDetails> {
    override fun onLastPage(response: ActorDetails): Boolean = true
}
