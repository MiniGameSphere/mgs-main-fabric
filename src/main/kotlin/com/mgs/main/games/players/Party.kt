package com.mgs.main.games.players

import com.mgs.main.games.PlayerLists
import net.minecraft.entity.player.PlayerEntity

data class Party(
	private val teamList: MutableList<Team> = mutableListOf(),
	var lobby: Lobby? = null,
	var leadingTeam: Team? = null
) {
	val teams: List<Team>
		get() {
			return teamList.toList()
		}

	val partyLeader: PlayerEntity?
		get() {
			return leadingTeam?.teamLeader
		}

	fun addTeam(team: Team) {
		leadingTeam ?: { leadingTeam = team }
		teamList.add(team)
	}

	fun removeTeam(team: Team) {
		teamList.remove(team)
		if (teamList.size == 0) {
			PlayerLists.partyList.remove(this)
		}
	}

	val playerCount: Int
		get() {
			var total = 0
			teams.forEach { team -> total += team.players.size }
			return total
		}

	init {
		PlayerLists.partyList.add(this)
	}

	override fun toString(): String {
		return "teams: [$teamList], leading team: $leadingTeam"
	}
}
