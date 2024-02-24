package com.mgs.main.gui

import com.mgs.main.ducks.PlayerEntityDuckInterface
import com.mgs.main.utils.*
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.screen.GenericContainerScreenHandler
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

class Gui(
	var name: Text = Text.empty(),
	val inventory: Inventory = createInventory(null, 6),
	val viewer: PlayerEntity,
	val parentGui: Gui? = null,
	val onUpdate: MutableList<() -> Unit> = mutableListOf()
) : NamedScreenHandlerFactory {
	private val guiItems = Array(inventory.height) { Array(inventory.width) { GuiItem.backgroundItem } }

	private val type: ScreenHandlerType<GenericContainerScreenHandler>?
		get() {
			return when (inventory.height) {
				1 -> ScreenHandlerType.GENERIC_9X1
				2 -> ScreenHandlerType.GENERIC_9X2
				3 -> ScreenHandlerType.GENERIC_9X3
				4 -> ScreenHandlerType.GENERIC_9X4
				5 -> ScreenHandlerType.GENERIC_9X5
				6 -> ScreenHandlerType.GENERIC_9X6
				else -> null
			}
		}

	val guiSections = mutableMapOf<String, GuiSection>(
		Pair(BACKGROUND_NAME,
			StaticGuiSection(
				Array(guiItems.size){guiItems[it].asNullableArray()},
				inventory.width,
				inventory.height
			)
		)
	)

	fun update() {
		onUpdate.forEach {it()}
		guiSections.forEach {(_, section) -> section.addGuiItemsToArray(guiItems)}
		populateInventory(inventory, guiItems)
	}

	fun onClick(click: InventoryClickWrapper) : InventoryClickWrapper {
		val position = click.slotIndex
		if (position !in 0..< inventory.size()) return click
		val guiItem = guiItems.flatten()[position]
		click.isCancelled = !guiItem.canRemove
		guiItem.clickAction(click, viewer, this)
		return click
	}

	private fun updateViewerGui(gui: Gui?) {
		(viewer as PlayerEntityDuckInterface).`mgs$setCurrentGui`(gui)
	}

	fun show() {
		viewer.openHandledScreen(this)
		update()
	}

	fun close() {
		if (viewer.isMainPlayer) {
			return
		}
		(viewer as ServerPlayerEntity).closeHandledScreen()
	}

	fun goBack() {
		if (parentGui == null) {
			close()
			return
		} else {
			parentGui.update()
			parentGui.show()
		}
	}

	companion object {
		const val BACKGROUND_NAME = "background"
	}

	override fun createMenu(syncId: Int, playerInventory: PlayerInventory?, player: PlayerEntity?): ScreenHandler {
		return GuiScreenHandler(type, syncId, playerInventory, inventory, inventory.height, this)
	}

	override fun getDisplayName(): Text {
		return name;
	}
}