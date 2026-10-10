package com.kvs.healthconnectreporter.decorator

/**
 * Maps a library enum to a Health Connect int constant family, entry for entry.
 *
 * Constants Health Connect adds later map to [fallback].
 */
internal class ConstantFamily<T : Enum<T>>(
    private val fallback: T,
    vararg pairs: Pair<Int, T>,
) {
    private val entries = pairs.toMap()
    private val constants = pairs.associate { (constant, entry) -> entry to constant }

    fun entry(constant: Int): T = entries[constant] ?: fallback

    fun constant(entry: T): Int = constants.getValue(entry)
}
