package com.fordance.student.core.common

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ResultWrapperTest {
    @Test
    fun testSuccessMapping() {
        val success = ResultWrapper.Success(10)
        val mapped = success.map { it * 2 }
        assertTrue(mapped is ResultWrapper.Success)
        assertEquals(20, (mapped as ResultWrapper.Success).data)
    }

    @Test
    fun testErrorMappingPreservesError() {
        val error = ResultWrapper.Error("Network failure")
        val mapped = error.map { 10 }
        assertTrue(mapped is ResultWrapper.Error)
        assertEquals("Network failure", (mapped as ResultWrapper.Error).message)
    }
}
