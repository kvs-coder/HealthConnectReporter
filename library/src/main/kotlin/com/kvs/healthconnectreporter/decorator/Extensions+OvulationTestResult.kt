package com.kvs.healthconnectreporter.decorator

import androidx.health.connect.client.records.OvulationTestRecord
import com.kvs.healthconnectreporter.model.type.OvulationTestResult

private val ovulationTestResultConstants =
    ConstantFamily(
        OvulationTestResult.INCONCLUSIVE,
        OvulationTestRecord.RESULT_INCONCLUSIVE to OvulationTestResult.INCONCLUSIVE,
        OvulationTestRecord.RESULT_POSITIVE to OvulationTestResult.POSITIVE,
        OvulationTestRecord.RESULT_HIGH to OvulationTestResult.HIGH,
        OvulationTestRecord.RESULT_NEGATIVE to OvulationTestResult.NEGATIVE,
    )

internal val OvulationTestResult.constant: Int
    get() = ovulationTestResultConstants.constant(this)

internal val Int.asOvulationTestResult: OvulationTestResult
    get() = ovulationTestResultConstants.entry(this)
