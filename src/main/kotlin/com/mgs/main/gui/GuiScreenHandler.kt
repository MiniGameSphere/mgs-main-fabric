package com.mgs.main.gui

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.screen.GenericContainerScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.screen.slot.SlotActionType

class GuiScreenHandler(
	type: ScreenHandlerType<*>?,
	syncId: Int,
	playerInventory: PlayerInventory?,
	inventory: Inventory?,
	rows: Int,
	val gui: Gui
) : GenericContainerScreenHandler(type, syncId, playerInventory, inventory, rows) {

	override fun onSlotClick(slotIndex: Int, button: Int, actionType: SlotActionType, player: PlayerEntity) {
		val result = gui.onClick(InventoryClickWrapper(slotIndex, button, actionType, player))
		if (result.isCancelled) {
			return
		}
		super.onSlotClick(slotIndex, button, actionType, player)
	}



}