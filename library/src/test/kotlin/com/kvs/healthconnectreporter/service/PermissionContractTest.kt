package com.kvs.healthconnectreporter.service

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContract
import com.kvs.healthconnectreporter.HealthConnectException
import com.kvs.healthconnectreporter.model.Permission
import com.kvs.healthconnectreporter.model.type.AccessType
import com.kvs.healthconnectreporter.model.type.HealthType
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class PermissionContractTest {
    private val original = mockk<ActivityResultContract<Set<String>, Set<String>>>()
    private val sut = PermissionContract(original)
    private val readSteps = Permission.Data(HealthType.STEPS, AccessType.READ)
    private val context = mockk<Context>()

    init {
        val packageManager = mockk<PackageManager>()
        every { packageManager.getPackageInfo("com.example", PackageManager.GET_PERMISSIONS) } returns
            PackageInfo().apply { requestedPermissions = arrayOf("android.permission.health.READ_STEPS") }
        every { context.packageName } returns "com.example"
        every { context.packageManager } returns packageManager
    }

    @Test
    fun `create intent asks for the permission strings`() {
        val intent = Intent()
        every { original.createIntent(context, setOf("android.permission.health.READ_STEPS")) } returns intent

        assertSame(intent, sut.createIntent(context, setOf(readSteps)))
    }

    @Test
    fun `create intent rejects undeclared permissions`() {
        assertFailsWith<HealthConnectException.InvalidValue> {
            sut.createIntent(context, setOf(Permission.Data(HealthType.WEIGHT, AccessType.READ)))
        }
    }

    @Test
    fun `parse result returns library permissions`() {
        every { original.parseResult(0, null) } returns setOf("android.permission.health.READ_STEPS")

        assertEquals(
            setOf(readSteps, Permission.Data(HealthType.STEPS_CADENCE, AccessType.READ)),
            sut.parseResult(0, null),
        )
    }

    @Test
    fun `synchronous result when everything is granted already`() {
        val strings = setOf("android.permission.health.READ_STEPS")
        every { original.getSynchronousResult(context, strings) } returns
            ActivityResultContract.SynchronousResult(strings)

        assertEquals(
            setOf(readSteps, Permission.Data(HealthType.STEPS_CADENCE, AccessType.READ)),
            sut.getSynchronousResult(context, setOf(readSteps))?.value,
        )
    }

    @Test
    fun `no synchronous result when the screen is needed`() {
        every { original.getSynchronousResult(context, any()) } returns null

        assertNull(sut.getSynchronousResult(context, setOf(readSteps)))
    }
}
