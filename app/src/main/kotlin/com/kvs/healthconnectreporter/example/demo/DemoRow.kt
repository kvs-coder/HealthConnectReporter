package com.kvs.healthconnectreporter.example.demo

/**
 * One demo of one public library call.
 *
 * @param id unique id of the row
 * @param title what the demo does
 * @param call the library call it exercises
 * @param isInteractive the demo opens a screen or runs until stopped, so "Run all" skips it
 */
data class DemoRow(
    val id: String,
    val title: String,
    val call: String,
    val isInteractive: Boolean = false,
)

/** The demos of one library area. */
data class DemoSection(
    val title: String,
    val rows: List<DemoRow>,
)
