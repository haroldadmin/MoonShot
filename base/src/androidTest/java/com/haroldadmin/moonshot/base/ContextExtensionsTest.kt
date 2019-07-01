package com.haroldadmin.moonshot.base

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ContextExtensionsTest {

    @Test
    fun intentExtensionTest() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = context.intent<ContextExtensionsTest> {
            action = Intent.ACTION_VIEW
            putExtra("Test", "Data")
        }

        assertEquals(Intent.ACTION_VIEW, intent.action)
        assertEquals("Data", intent.getStringExtra("Test"))
    }

    @Test
    fun broadcastPendingIntentTest() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val pendingIntent = context.broadcastPendingIntent(1, PendingIntent.FLAG_CANCEL_CURRENT) {
            intent<ContextExtensionsTest>()
        }

        assertEquals(context.packageName, pendingIntent.creatorPackage)
    }

    @Test
    fun exactAlarmTest() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val alarmManager = mockk<AlarmManager>()
        every { alarmManager.setExactAndAllowWhileIdle(any(), any(), any()) } returns Unit

        context.exactAlarmAt(time = System.currentTimeMillis(), alarmManager = alarmManager) {
            broadcastPendingIntent(0) {
                intent<ContextExtensionsTest>()
            }
        }

        verify(exactly = 1) { alarmManager.setExactAndAllowWhileIdle(any(), any(), any()) }
    }

    @Test
    fun cancelAlarmTest() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val alarmManager = mockk<AlarmManager>()

        every { alarmManager.setExactAndAllowWhileIdle(any(), any(), any()) } returns Unit
        every { alarmManager.cancel(any<PendingIntent>()) } returns Unit

        val pendingIntent = context.broadcastPendingIntent(0) { intent<ContextExtensionsTest>() }
        context.exactAlarmAt(System.currentTimeMillis() + 5000, alarmManager = alarmManager) { pendingIntent }

        context.cancelAlarmWithIntent(alarmManager) { pendingIntent }

        verify(exactly = 1) { alarmManager.cancel(any<PendingIntent>()) }
    }
}