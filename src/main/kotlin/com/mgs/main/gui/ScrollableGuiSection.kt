package com.mgs.main.gui

import com.mgs.main.utils.height
import com.mgs.main.utils.width
import net.minecraft.inventory.Inventory

class ScrollableGuiSection(
	private var items: Array<Array<GuiItem?>>,
	val width: Int,
	val height: Int,
	var posX: Int = 0,
	var posY: Int = 0,
	private var rowOffset: Int = 0,
	private var columnOffset: Int = 0
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
		items.forEachIndexed outer@{ i, row ->
			if (i < rowOffset || i >= rowOffset + height) return@outer
			row.forEachIndexed inner@{ j, item ->
				if (j < columnOffset || j >= columnOffset + width) return@inner
				item ?: return@inner
				array[posY + i - rowOffset][posX + j - columnOffset] = item
			}
		}
	}

	private fun scrollLeft() {
		columnOffset -= 1
	}
	private fun scrollRight() {
		columnOffset += 1
	}
	private fun scrollUp() {
		rowOffset -= 1
	}
	private fun scrollDown() {
		rowOffset += 1
	}

	private fun scrollLeftPage() {
		columnOffset -= width
	}
	private fun scrollRightPage() {
		columnOffset += width
	}
	private fun scrollUpPage() {
		rowOffset -= height
	}
	private fun scrollDownPage() {
		rowOffset += height
	}

	fun scrollDirection(direction: ScrollDirection, paginated: Boolean = false) {
		if (!canScrollInDirection(direction, paginated)) return
		if (!paginated) {
			return when (direction) {
				ScrollDirection.RIGHT -> scrollRight()
				ScrollDirection.LEFT -> scrollLeft()
				ScrollDirection.UP -> scrollUp()
				ScrollDirection.DOWN -> scrollDown()
			}
		}
		when (direction) {
			ScrollDirection.RIGHT -> scrollRightPage()
			ScrollDirection.LEFT -> scrollLeftPage()
			ScrollDirection.UP -> scrollUpPage()
			ScrollDirection.DOWN -> scrollDownPage()
		}
	}

	fun canScrollInDirection(direction: ScrollDirection, paginated: Boolean = false) : Boolean {
		if (!paginated) {
			return when (direction) {
				ScrollDirection.LEFT -> columnOffset - 1 >= 0
				ScrollDirection.RIGHT -> columnOffset + width < items[0].size
				ScrollDirection.UP -> rowOffset - 1 >= 0
				ScrollDirection.DOWN -> rowOffset + height < items.size
			}
		}
		else {
			return when (direction) {
				ScrollDirection.LEFT -> columnOffset - width >= 0
				ScrollDirection.RIGHT -> columnOffset + width*2 < items[0].size
				ScrollDirection.UP -> rowOffset - height >= 0
				ScrollDirection.DOWN -> rowOffset + height*2 < items.size
			}
		}
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as ScrollableGuiSection

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