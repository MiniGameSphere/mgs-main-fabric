package com.mgs.main

import com.mgs.main.events.register
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents

object Main : ModInitializer {

	val MOD_ID = "mgs_main"

	override fun onInitialize() {
		register()
	}


}