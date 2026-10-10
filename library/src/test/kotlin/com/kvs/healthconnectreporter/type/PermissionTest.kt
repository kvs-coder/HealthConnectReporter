package com.kvs.healthconnectreporter.type

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.kvs.healthconnectreporter.HealthConnectException
import com.kvs.healthconnectreporter.decorator.asOriginal
import com.kvs.healthconnectreporter.decorator.asPermissions
import com.kvs.healthconnectreporter.decorator.checkDeclared
import com.kvs.healthconnectreporter.decorator.feature
import com.kvs.healthconnectreporter.model.Permission
import com.kvs.healthconnectreporter.model.type.AccessType
import com.kvs.healthconnectreporter.model.type.Feature
import com.kvs.healthconnectreporter.model.type.HealthType
import io.mockk.every
import io.mockk.mockk
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PermissionTest {
    private val all =
        Permission.of(HealthType.entries) +
            setOf(Permission.ReadInBackground, Permission.ReadHistory, Permission.WriteExerciseRoute)

    @Test
    fun `every permission maps to its string and back`() {
        all.forEach { assertTrue(it in setOf(it.asOriginal).asPermissions, it.toString()) }
    }

    @Test
    fun `data permissions map to the read and write strings of their type`() {
        assertEquals(
            "android.permission.health.READ_HEART_RATE",
            Permission.Data(HealthType.HEART_RATE, AccessType.READ).asOriginal,
        )
        assertEquals(
            "android.permission.health.WRITE_WEIGHT",
            Permission.Data(HealthType.WEIGHT, AccessType.WRITE).asOriginal,
        )
    }

    @Test
    fun `feature permissions map to their strings`() {
        val expected =
            mapOf(
                Permission.ReadInBackground to "android.permission.health.READ_HEALTH_DATA_IN_BACKGROUND",
                Permission.ReadHistory to "android.permission.health.READ_HEALTH_DATA_HISTORY",
                Permission.WriteExerciseRoute to "android.permission.health.WRITE_EXERCISE_ROUTE",
            )

        expected.forEach { (permission, string) -> assertEquals(string, permission.asOriginal) }
    }

    @Test
    fun `a shared string grants every type that shares it`() {
        val granted = setOf("android.permission.health.READ_STEPS", "android.permission.health.UNKNOWN").asPermissions

        assertEquals(
            setOf(
                Permission.Data(HealthType.STEPS, AccessType.READ),
                Permission.Data(HealthType.STEPS_CADENCE, AccessType.READ),
            ),
            granted,
        )
    }

    @Test
    fun `permissions name the feature they need`() {
        assertEquals(Feature.READ_HEALTH_DATA_IN_BACKGROUND, Permission.ReadInBackground.feature)
        assertEquals(Feature.READ_HEALTH_DATA_HISTORY, Permission.ReadHistory.feature)
        assertNull(Permission.WriteExerciseRoute.feature)
        assertEquals(Feature.SKIN_TEMPERATURE, Permission.Data(HealthType.SKIN_TEMPERATURE, AccessType.READ).feature)
        assertNull(Permission.Data(HealthType.STEPS, AccessType.WRITE).feature)
    }

    @Test
    fun `of builds read and write permissions per type`() {
        assertEquals(
            setOf(
                Permission.Data(HealthType.STEPS, AccessType.READ),
                Permission.Data(HealthType.STEPS, AccessType.WRITE),
            ),
            Permission.of(listOf(HealthType.STEPS)),
        )
        assertEquals(HealthType.entries.size, Permission.of(HealthType.entries, setOf(AccessType.READ)).size)
    }

    @Test
    fun `permissions round trip through json`() {
        val serializer = SetSerializer(Permission.serializer())

        assertEquals(all, Json.decodeFromString(serializer, Json.encodeToString(serializer, all)))
    }

    @Test
    fun `declared permissions pass the manifest check`() {
        context(arrayOf("android.permission.health.READ_STEPS")).checkDeclared(
            setOf(Permission.Data(HealthType.STEPS, AccessType.READ)),
        )
    }

    @Test
    fun `undeclared or no permissions fail the manifest check`() {
        val error =
            assertFailsWith<HealthConnectException.InvalidValue> {
                context(null).checkDeclared(setOf(Permission.Data(HealthType.STEPS, AccessType.READ)))
            }
        assertEquals("Permissions missing from the app manifest: [android.permission.health.READ_STEPS]", error.message)

        assertFailsWith<HealthConnectException.InvalidValue> { context(emptyArray()).checkDeclared(emptySet()) }
    }

    private fun context(declared: Array<String>?): Context {
        val info = PackageInfo().apply { requestedPermissions = declared }
        val packageManager = mockk<PackageManager>()
        every { packageManager.getPackageInfo("com.example", PackageManager.GET_PERMISSIONS) } returns info
        val context = mockk<Context>()
        every { context.packageName } returns "com.example"
        every { context.packageManager } returns packageManager
        return context
    }
}
