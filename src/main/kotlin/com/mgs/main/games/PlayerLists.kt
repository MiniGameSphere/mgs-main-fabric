package com.mgs.main.games

import com.mgs.main.games.players.Lobby
import com.mgs.main.games.players.Party
import com.mgs.main.games.players.Team

object PlayerLists {

	val teamList = mutableListOf<Team>()

	val partyList = mutableListOf<Party>()

	val lobbyList = mutableListOf<Lobby>()
}