package com.mgs.main.events

import com.mgs.main.ducks.PlayerEntityDuckInterface
import com.mgs.main.utils.getExampleGui
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents

fun register() {
	ServerPlayConnectionEvents.JOIN.register { handler, _, _ ->
		// this is a test that should be removed later
		getExampleGui(handler.player).show()
	}
	ServerPlayConnectionEvents.DISCONNECT.register { handler, _ ->
		(handler.player as PlayerEntityDuckInterface).`mgs$remove`()
	}
}