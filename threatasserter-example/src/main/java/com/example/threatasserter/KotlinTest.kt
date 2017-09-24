package com.example.threatasserter

import android.os.SystemClock
import com.jaggernod.threatasserter.annotations.AssertWorkerThread

class KotlinTest {

    @AssertWorkerThread
    fun crashIfNotWorkerThread() {
        // Looong task. But will crash before getting here.
        SystemClock.sleep(1000)
    }

}
