package com.haroldadmin.moonshot.notifications

import com.haroldadmin.moonshot.notifications.new.MoonShotNotificationManager
import org.junit.After
import org.junit.Test

internal class MoonShotNotificationManagerTest {

    private val mockWorkManager = MockWorkManager()
    private val notifManager = MoonShotNotificationManager(mockWorkManager)

    @Test
    fun `should enqueue work request when notifications are enabled`() {
        notifManager.enableNotifications()
        assert(mockWorkManager.workRequests.size == 1) {
            "Expected one work request to be enqueued, got ${mockWorkManager.workRequests.size}"
        }
    }

    @Test
    fun `should cancel all work requests for scheduling task when disabling notifications`() {
        notifManager.disableAllNotifications()
        assert(mockWorkManager.workRequests.isEmpty()) {
            "Expected all work requests to be cancelled, but got ${mockWorkManager.workRequests.size} requests"
        }
    }

    @After
    fun cleanup() {
        mockWorkManager.cancelAllWork()
    }
}