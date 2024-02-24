package com.mgs.main.gui

import net.minecraft.inventory.Inventory

interface GuiSection {
	fun checkValid(inventory: Inventory) : Boolean
	fun addGuiItemsToArray(array: Array<out Array<in GuiItem>>)

	fun setItem(x: Int, y: Int, item: GuiItem?)

	fun setItem(xy: Pair<Int,Int>, item: GuiItem?)

	fun getItem(x: Int, y: Int) : GuiItem?

	fun getItem(xy: Pair<Int, Int>) : GuiItem?

	companion object {
		val emptySection = { height: Int, width: Int ->
			StaticGuiSection(
				Array(width) { Array(height) { null } },
				height,
				width
			)
		}
	}
}