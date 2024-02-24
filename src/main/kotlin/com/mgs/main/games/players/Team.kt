package com.mgs.main.games.players

import com.mgs.main.games.PlayerLists
import net.minecraft.entity.player.PlayerEntity

data class Team(
	private val playerList: MutableList<PlayerEntity> = mutableListOf(),
	var party: Party = Party(),
	var teamLeader: PlayerEntity? = null
) {
	val players: List<PlayerEntity>
		get() {
			return playerList.toList()
		}

	fun addPlayer(p: PlayerEntity) {
		teamLeader ?: { teamLeader = p}
		playerList.add(p)
	}

	fun removePlayer(p: PlayerEntity) {
		playerList.remove(p)
		if (playerList.size == 0) {
			party.removeTeam(this)
			PlayerLists.teamList.remove(this)
		} else if (teamLeader == p) {
			teamLeader = playerList[0]
		}
	}

	init {
		PlayerLists.teamList.add(this)
		party.addTeam(this)
	}

	@Override
	override fun toString(): String {
		return "players: $playerList, team leader: $teamLeader"
	}
}
