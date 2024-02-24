package com.mgs.main.events

import com.mgs.main.games.MiniGame
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.util.ActionResult

fun interface GameEndCallback {
	fun interact(miniGame: MiniGame): ActionResult

	companion object {
		val EVENT: Event<GameEndCallback> = EventFactory.createArrayBacked(
			GameEndCallback::class.java
		) { listeners ->
			GameEndCallback { miniGame: MiniGame ->
				listeners.forEach {
					val result = it.interact(miniGame)
					if(result != ActionResult.PASS) return@GameEndCallback result
				}
				return@GameEndCallback ActionResult.PASS
			}
		}
	}
}