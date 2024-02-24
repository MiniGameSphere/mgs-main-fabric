package com.mgs.main.games

object MiniGames {
	val minigames = mutableListOf<MiniGame>()

	fun startRandomGame() {
		minigames.random().startGame()
	}

}