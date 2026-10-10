package com.kvs.healthconnectreporter.service

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import androidx.health.connect.client.PermissionController
import com.kvs.healthconnectreporter.decorator.asOriginal
import com.kvs.healthconnectreporter.decorator.asPermissions
import com.kvs.healthconnectreporter.decorator.checkDeclared
import com.kvs.healthconnectreporter.model.Permission

/** The Health Connect permission request contract, with library permissions in and out. */
internal class PermissionContract(
    private val original: ActivityResultContract<Set<String>, Set<String>> =
        PermissionController.createRequestPermissionResultContract(),
) : ActivityResultContract<Set<Permission>, Set<Permission>>() {
    override fun createIntent(
        context: Context,
        input: Set<Permission>,
    ): Intent {
        context.checkDeclared(input)
        return original.createIntent(context, input.asOriginal)
    }

    override fun parseResult(
        resultCode: Int,
        intent: Intent?,
    ): Set<Permission> = original.parseResult(resultCode, intent).asPermissions

    override fun getSynchronousResult(
        context: Context,
        input: Set<Permission>,
    ): SynchronousResult<Set<Permission>>? =
        original.getSynchronousResult(context, input.asOriginal)?.let { SynchronousResult(it.value.asPermissions) }
}
