package com.mgs.main.gui

import com.mgs.main.utils.height
import com.mgs.main.utils.width
import net.minecraft.inventory.Inventory


data class StaticGuiSection(
	private var items: Array<Array<GuiItem?>>,
	val width: Int,
	val height: Int,
	var posX: Int = 0,
	var posY: Int = 0
) : GuiSection {

	override fun setItem(x: Int, y: Int, item: GuiItem?) {
		items[y][x] = item
	}

	override fun setItem(xy: Pair<Int, Int>, item: GuiItem?) {
		items[xy.second][xy.first] = item
	}

	override fun getItem(x: Int, y: Int) : GuiItem? {
		return items[y][x]
	}

	override fun getItem(xy: Pair<Int, Int>) : GuiItem? {
		return items[xy.second][xy.first]
	}

	override fun checkValid(inventory: Inventory) : Boolean {
		val inventoryWidth = inventory.width
		val inventoryHeight = inventory.height
		if (width + posX > inventoryWidth) return false
		return height + posY <= inventoryHeight
	}

	override fun addGuiItemsToArray(array: Array<out Array<in GuiItem>>) {
		items.forEachIndexed { i, row ->
			row.forEachIndexed inner@{ j, item ->
				item ?: return@inner
				if (i >= height || j >= width) return
				array[posY + i][posX + j] = item
			}
		}
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as StaticGuiSection

		if (!items.contentDeepEquals(other.items)) return false
		if (width != other.width) return false
		if (height != other.height) return false
		if (posX != other.posX) return false
		if (posY != other.posY) return false

		return true
	}

	override fun hashCode(): Int {
		var result = items.contentDeepHashCode()
		result = 31 * result + width
		result = 31 * result + height
		result = 31 * result + posX
		result = 31 * result + posY
		return result
	}
}
