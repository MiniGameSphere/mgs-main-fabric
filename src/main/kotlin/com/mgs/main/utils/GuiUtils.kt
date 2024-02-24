package com.mgs.main.utils

import com.mgs.main.Main
import com.mgs.main.gui.*
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.InventoryOwner
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.*
import net.minecraft.nbt.visitor.NbtTextFormatter
import net.minecraft.registry.Registries
import net.minecraft.text.NbtTextContent
import net.minecraft.text.Text
import net.minecraft.util.Formatting

fun createInventory(owner: InventoryOwner? = null, rows: Int): Inventory {
	return SimpleInventory(9 * rows)
}

fun setInventoryContents(inventory: Inventory, items: Iterable<ItemStack>) {
	items.forEachIndexed { i, item ->
		inventory.setStack(i, item)
	}
}

fun populateInventory(inventory: Inventory, items2d: Array<Array<GuiItem>>) {
	val items = items2d.flatten()
	val itemStacks = Array(items.size){ i -> items[i].item }
	itemStacks.forEachIndexed{ i : Int, itemStack : ItemStack ->
		val guiItem = items[i]
		if (guiItem.name != "") itemStack.setCustomName(Text.of(guiItem.name))
		if (guiItem.lore.isNotEmpty()) {
			itemStack.setLore(guiItem.lore)
		}
		if (guiItem.enchanted) {
			itemStack.hasGlint()
			itemStack.addEnchantment(
				Enchantments.INFINITY,
				1
			)
			itemStack.addHideFlag(
				ItemStack.TooltipSection.ENCHANTMENTS
			)
		}
		if (!guiItem.canRemove) {
			itemStack.setSubNbt(
				"${Main.MOD_ID}_cant_remove",
				NbtByte.of(0b1)
			)
		}
	}
	setInventoryContents(inventory, itemStacks.asIterable())
	inventory.markDirty()
}

val Inventory.width : Int
	get(){
		return 9
	}

val Inventory.height : Int
	get() {
		return this.size() / this.width
	}

fun ItemStack.setLore(lore: Array<Text>) {
	val nbt = NbtList()
	lore.forEach {
		nbt.add(NbtString.of(Text.Serializer.toJson(it)))
	}
	val nbtCompound = getOrCreateSubNbt("display")
	nbtCompound.put(ItemStack.LORE_KEY, nbt)
}

fun openGui(
	inventory: Inventory = createInventory(null, 6),
	viewer: PlayerEntity,
	guiSections: Map<String, StaticGuiSection>? = null
) : Gui {
	val gui = Gui(inventory = inventory, viewer = viewer)
	guiSections?.forEach {
		gui.guiSections[it.key] = it.value
	}
	gui.update()
	gui.show()
	return gui
}

fun createChildGui(
	oldGui: Gui,
	inventory: Inventory = createInventory(null, 6),
	viewer: PlayerEntity,
	guiSections: Map<String, StaticGuiSection>? = null
) : Gui {
	val newGui = Gui(inventory = inventory, viewer = viewer, parentGui = oldGui)
	newGui.guiSections.putAll(guiSections ?: emptyMap())
	return newGui
}

fun getExampleGui(
	viewer: PlayerEntity
) : Gui {
	val mainGui = Gui(viewer = viewer)

	val allItems = mutableListOf<GuiItem>()

	val submenu = Gui(viewer = viewer)

	var background = submenu.guiSections[Gui.BACKGROUND_NAME]
	background?.setItem(0,0, GuiItem.closeItem)



	val killClickAction = { _: InventoryClickWrapper, player: PlayerEntity, _: Gui -> player.kill() }

	val testItems = mutableListOf(
		GuiItem(item = Items.GLASS, name = "${Formatting.AQUA}I'm a test of a coloured name."),
		GuiItem(item = Items.ELYTRA, name = "I'm a test of an item you can remove.", canRemove = true),
		GuiItem(item = Items.ENDER_PEARL, name = "I'm a test of enchantment glint.", enchanted = true),
		GuiItem(item = Items.END_PORTAL_FRAME, lore = arrayOf(
			Text.of("I'm a test of item Lore."),
			Text.of("I can have multiple lines.")
		)),
		GuiItem(item = Items.TNT, name = "I'm a test of what can happen when you click me :)", clickAction = killClickAction)
	)

	val testItemsSplit = testItems.toTypedArray().splitArrayLeftToRight(9)

	val testItemsSection = StaticGuiSection(
		items = testItemsSplit,
		width = 7,
		height = 4,
		posX = 1,
		posY = 1
	)
	val testItemsSectionName = "test_items_section"
	submenu.guiSections[testItemsSectionName] = testItemsSection

	Registries.ITEM.forEach {
		if (it.asItem() == Items.CAKE) {
			allItems.add(GuiItem.getOpenChildItem(it, "Secret :3", submenu))
		}
		else {
			allItems.add(GuiItem(it))
		}
	}

	val allItemsSplit = allItems.toTypedArray().splitArrayLeftToRight(18)

	val allItemsSection = ScrollableGuiSection(
		items = allItemsSplit,
		width = 7,
		height = 4,
		posX = 1,
		posY = 1
	)
	val allItemSectionName = "all_items_section"
	mainGui.guiSections[allItemSectionName] = allItemsSection

	background = mainGui.guiSections[Gui.BACKGROUND_NAME]
	background?.setItem(0,0, GuiItem.closeItem)

	fun scrollItemPosition(direction: ScrollDirection) : Pair<Int, Int> {
		return when(direction) {
			ScrollDirection.LEFT -> Pair(0, 2)
			ScrollDirection.UP -> Pair(4, 0)
			ScrollDirection.DOWN -> Pair(4, 5)
			ScrollDirection.RIGHT -> Pair(8, 2)
		}
	}

	fun setScrollItems() {
		ScrollDirection.entries.forEach {direction ->
			val item = if (allItemsSection.canScrollInDirection(direction)) {
				GuiItem.getScrollOneOrPageItem(gui = mainGui, section = allItemsSection, direction = direction)
			} else {
				GuiItem.backgroundItem
			}
			background?.setItem(
				xy = scrollItemPosition(direction),
				item = item
			)
		}
	}

	mainGui.onUpdate.add{setScrollItems()}

	return mainGui
}
