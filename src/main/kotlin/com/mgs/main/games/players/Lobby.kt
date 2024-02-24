package com.mgs.main.games.players

import com.mgs.main.games.PlayerLists

data class Lobby(
	private val partyList: MutableList<Party> = mutableListOf()
) {
	val parties: List<Party>
		get() {
			return partyList.toList()
		}

	fun addParty(party: Party) {
		party.lobby = this
		partyList.add(party)
	}
	fun removeParty(party: Party) {
		partyList.remove(party)
		party.lobby = null
		if (partyList.size == 0) {
			PlayerLists.lobbyList.remove(this)
		}
	}

	val playerCount : Int
		get() {
			var total = 0
			parties.forEach { party -> total += party.playerCount }
			return total
		}

	init {
		PlayerLists.lobbyList.add(this)
	}
}
