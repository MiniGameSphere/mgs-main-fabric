package com.mgs.main.gui

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.screen.slot.SlotActionType

data class InventoryClickWrapper(
	val slotIndex: Int,
	val button: Int,
	val actionType: SlotActionType,
	val player: PlayerEntity,
	var isCancelled: Boolean = false
)
