package com.kvs.healthconnectreporter.service

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.HealthConnectFeatures
import com.kvs.healthconnectreporter.HealthConnectException
import com.kvs.healthconnectreporter.model.Permission
import com.kvs.healthconnectreporter.model.type.AccessType
import com.kvs.healthconnectreporter.model.type.Feature
import com.kvs.healthconnectreporter.model.type.HealthType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.async
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertSame
import kotlin.test.assertTrue

class HealthConnectManagerTest {
    private val client = mockk<HealthConnectClient>()
    private val contract = mockk<ActivityResultContract<Set<Permission>, Set<Permission>>>()
    private val sut = HealthConnectManager(client) { contract }
    private val readSteps = Permission.Data(HealthType.STEPS, AccessType.READ)
    private val registry = mockk<ActivityResultRegistry>()
    private val launcher = mockk<ActivityResultLauncher<Set<Permission>>>(relaxed = true)
    private val callback = slot<ActivityResultCallback<Set<Permission>>>()

    @After
    fun tearDown() {
        unmockkAll()
    }

    private fun activity(vararg declared: String): ComponentActivity {
        val packageManager = mockk<PackageManager>()
        every { packageManager.getPackageInfo("com.example", PackageManager.GET_PERMISSIONS) } returns
            PackageInfo().apply { requestedPermissions = arrayOf(*declared) }
        val activity = mockk<ComponentActivity>()
        every { activity.packageName } returns "com.example"
        every { activity.packageManager } returns packageManager
        every { activity.activityResultRegistry } returns registry
        every { registry.register(any(), contract, capture(callback)) } returns launcher
        return activity
    }

    @Test
    fun `request permissions returns the granted permissions`() =
        runTest {
            val activity = activity("android.permission.health.READ_STEPS")
            every { launcher.launch(setOf(readSteps)) } answers { callback.captured.onActivityResult(setOf(readSteps)) }

            assertEquals(setOf(readSteps), sut.requestPermissions(activity, setOf(readSteps)))
        }

    @Test
    fun `cancelled request unregisters its launcher`() =
        runTest {
            val activity = activity("android.permission.health.READ_STEPS")

            val request = async { sut.requestPermissions(activity, setOf(readSteps)) }
            runCurrent()
            request.cancel()
            runCurrent()

            verify(exactly = 1) { launcher.unregister() }
        }

    @Test
    fun `request of undeclared permissions never shows the screen`() =
        runTest {
            val activity = activity()

            assertFailsWith<HealthConnectException.InvalidValue> { sut.requestPermissions(activity, setOf(readSteps)) }
            verify(exactly = 0) { registry.register(any(), contract, any()) }
        }

    @Test
    fun `request of a feature gated permission needs its feature`() =
        runTest {
            val activity = activity("android.permission.health.READ_HEALTH_DATA_HISTORY")
            every { client.features.getFeatureStatus(any()) } returns HealthConnectFeatures.FEATURE_STATUS_UNAVAILABLE

            val error =
                assertFailsWith<HealthConnectException.NotAvailable> {
                    sut.requestPermissions(activity, setOf(Permission.ReadHistory))
                }

            assertEquals("Permission needs Health Connect feature READ_HEALTH_DATA_HISTORY", error.message)
        }

    @Test
    fun `request of a feature gated permission with its feature shows the screen`() =
        runTest {
            val activity = activity("android.permission.health.READ_HEALTH_DATA_HISTORY")
            every { client.features.getFeatureStatus(any()) } returns HealthConnectFeatures.FEATURE_STATUS_AVAILABLE
            every { launcher.launch(any()) } answers { callback.captured.onActivityResult(emptySet()) }

            assertEquals(emptySet(), sut.requestPermissions(activity, setOf(Permission.ReadHistory)))
        }

    @Test
    fun `granted permissions are library permissions`() =
        runTest {
            coEvery { client.permissionController.getGrantedPermissions() } returns
                setOf("android.permission.health.WRITE_WEIGHT")

            assertEquals(setOf(Permission.Data(HealthType.WEIGHT, AccessType.WRITE)), sut.grantedPermissions())
        }

    @Test
    fun `revoke all permissions`() =
        runTest {
            coEvery { client.permissionController.revokeAllPermissions() } returns Unit

            sut.revokeAllPermissions()

            coVerify(exactly = 1) { client.permissionController.revokeAllPermissions() }
        }

    @Test
    fun `feature availability`() =
        runTest {
            every { client.features.getFeatureStatus(HealthConnectFeatures.FEATURE_PLANNED_EXERCISE) } returns
                HealthConnectFeatures.FEATURE_STATUS_AVAILABLE

            assertTrue(sut.isFeatureAvailable(Feature.PLANNED_EXERCISE))
        }

    @Test
    fun `permission contract is the library contract`() {
        assertSame(contract, sut.permissionContract())
        assertIs<PermissionContract>(HealthConnectManager(client).permissionContract())
    }

    @Test
    fun `settings intent opens health connect data management`() {
        val context = mockk<Context>()
        val intent = Intent()
        mockkObject(HealthConnectClient)
        every { HealthConnectClient.getHealthConnectManageDataIntent(context) } returns intent

        assertSame(intent, sut.settingsIntent(context))
    }

    @Test
    fun `install intent opens health connect on google play`() {
        val context = mockk<Context>()
        every { context.packageName } returns "com.example"
        val uri = mockk<Uri>()
        mockkStatic(Uri::class)
        every {
            Uri.parse("market://details?id=com.google.android.apps.healthdata&url=healthconnect%3A%2F%2Fonboarding")
        } returns uri
        mockkConstructor(Intent::class)
        every { anyConstructed<Intent>().setPackage(any()) } answers { self as Intent }
        every { anyConstructed<Intent>().setData(any()) } answers { self as Intent }
        every { anyConstructed<Intent>().putExtra(any<String>(), any<Boolean>()) } answers { self as Intent }
        every { anyConstructed<Intent>().putExtra(any<String>(), any<String>()) } answers { self as Intent }

        sut.installIntent(context)

        verify { anyConstructed<Intent>().setPackage("com.android.vending") }
        verify { anyConstructed<Intent>().setData(uri) }
        verify { anyConstructed<Intent>().putExtra("callerId", "com.example") }
    }
}
