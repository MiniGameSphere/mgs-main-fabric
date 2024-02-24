package com.mgs.main.utils

import kotlin.math.ceil

inline fun <reified T> Array<out T?>.splitArrayLeftToRight(columns: Int) : Array<Array<T?>> {
	val rows = ceil(this.size.toFloat() / columns).toInt()
	return Array(rows){row ->
		Array(columns)inner@{column ->
			if(row*columns + column > this.lastIndex) return@inner null
			return@inner this[row*columns + column]
		}
	}
}

inline fun <reified T> Array<out T?>.splitArrayTopToBottom(rows: Int) : Array<Array<T?>> {
	val columns = ceil(this.size.toFloat() / rows).toInt()
	return Array(rows){row ->
		Array(columns)inner@{column ->
			if(column*rows + row > this.lastIndex) return@inner null
			return@inner this[column*rows + row]
		}
	}
}

inline fun <reified T> Array<T>.asNullableArray() : Array<T?> {
	return Array(this.size){this[it]}
}

inline fun <reified T> Array<Array<T>>.transpose(): Array<Array<T>> {
	val cols = this[0].size
	val rows = this.size
	return Array(cols) { j ->
		Array(rows) { i ->
			this[i][j]
		}
	}
}