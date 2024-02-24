package com.mgs.main.gui

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.screen.slot.SlotActionType
import net.minecraft.text.Text
import net.minecraft.util.Formatting

data class GuiItem(
	val item: ItemStack,
	val name: String = "",
	val lore: Array<Text> = arrayOf(),
	val canRemove: Boolean = false,
	val enchanted: Boolean = false,
	val clickAction: (InventoryClickWrapper, PlayerEntity, Gui) -> Unit = {_, _, _ ->}
) {

	constructor(
		item: Item,
		name: String = "",
		lore: Array<Text> = arrayOf(),
		canRemove: Boolean = false,
		enchanted: Boolean = false,
		clickAction: (InventoryClickWrapper, PlayerEntity, Gui) -> Unit = {_, _, _ ->}
	) : this(
		ItemStack(item),
		name,
		lore,
		canRemove,
		enchanted,
		clickAction
	)


	companion object {
		val goBackGuiAction = { _: InventoryClickWrapper, _: PlayerEntity, gui: Gui ->
			gui.goBack()
		}

		val closeGuiAction = { _: InventoryClickWrapper, _: PlayerEntity, gui: Gui ->
			gui.close()
		}

		fun getScrollGuiAction(
			gui: Gui,
			section: ScrollableGuiSection,
			direction : ScrollDirection,
			paginated : Boolean = false
		) : (InventoryClickWrapper, PlayerEntity, Gui) -> Unit {
			return {_, _, _ ->
				section.scrollDirection(direction, paginated)
				gui.update()
			}
		}

		fun getScrollOneOrPageGuiAction(
			gui: Gui,
			section: ScrollableGuiSection,
			direction: ScrollDirection
		) : (InventoryClickWrapper, PlayerEntity, Gui) -> Unit {
			return {event, _, _ ->
				section.scrollDirection(direction, event.actionType == SlotActionType.QUICK_MOVE)
				gui.update()
			}
		}

		val showChildGuiAction = {
				child: Gui ->
			{_: InventoryClickWrapper, _: PlayerEntity, _: Gui ->
				child.show()
			}
		}


		val closeItem = GuiItem(
			item = ItemStack(Items.BARRIER),
			name = Formatting.RED.toString() + "Exit",
			clickAction = closeGuiAction
		)

		val goBackItem = GuiItem(
			item = ItemStack(Items.RED_WOOL),
			name = Formatting.RED.toString() + "Go Back",
			clickAction = goBackGuiAction
		)

		fun getOpenChildItem(item: Item, name: String, child: Gui): GuiItem {
			return GuiItem(
				item = item,
				name = name,
				clickAction = showChildGuiAction(child)
			)
		}

		fun getOpenChildItem(item: ItemStack, name: String, child: Gui): GuiItem {
			return GuiItem(
				item = item,
				name = name,
				clickAction = showChildGuiAction(child)
			)
		}

		val backgroundItem = GuiItem(
			Items.LIGHT_GRAY_STAINED_GLASS_PANE,
			Formatting.RESET.toString()
		)

		fun getScrollItem(
			gui: Gui,
			section: ScrollableGuiSection,
			item: ItemStack = ItemStack(Items.ARROW),
			name: String = "",
			direction: ScrollDirection,
			paginated: Boolean = false
			) : GuiItem {
			val formattedName : String = name.ifBlank {
				Formatting.GREEN.toString() +
						direction.fullName +
						if (paginated) " Page" else ""
			}
			return GuiItem(
				name = formattedName,
				item = item,
				clickAction = getScrollGuiAction(gui, section, direction, paginated)
			)
		}

		fun getScrollOneOrPageItem(
			gui: Gui,
			section: ScrollableGuiSection,
			item: ItemStack = ItemStack(Items.ARROW),
			name: String = "",
			direction: ScrollDirection
		) : GuiItem {
			val formattedName: String = name.ifBlank {
				Formatting.GREEN.toString() +
						direction.fullName
			}
			return GuiItem(
				name = formattedName,
				item = item,
				clickAction = getScrollOneOrPageGuiAction(gui, section, direction)
			)
		}
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as GuiItem

		if (item != other.item) return false
		if (name != other.name) return false
		if (!lore.contentEquals(other.lore)) return false
		if (canRemove != other.canRemove) return false
		if (enchanted != other.enchanted) return false
		if (clickAction != other.clickAction) return false

		return true
	}

	override fun hashCode(): Int {
		var result = item.hashCode()
		result = 31 * result + name.hashCode()
		result = 31 * result + lore.contentHashCode()
		result = 31 * result + canRemove.hashCode()
		result = 31 * result + enchanted.hashCode()
		result = 31 * result + clickAction.hashCode()
		return result
	}
}
