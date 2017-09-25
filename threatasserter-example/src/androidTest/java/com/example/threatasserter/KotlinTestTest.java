package com.example.threatasserter;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.os.Handler;
import android.os.Looper;
import android.support.test.runner.AndroidJUnit4;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;


@SuppressWarnings("MessageMissingOnJUnitAssertion")
@RunWith(AndroidJUnit4.class)
public class KotlinTestTest {

    @Test(timeout = 2000)
    public void crashIfRunningOnUiThread() throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final AtomicReference<Throwable> throwable = new AtomicReference<>();
        new Handler(Looper.getMainLooper())
                .post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            new KotlinTest().crashIfNotWorkerThread();
                        } catch (Exception e) {
                            throwable.set(e);
                        } finally {
                            countDownLatch.countDown();
                        }
                    }
                });
        countDownLatch.await();

        assertNotNull(throwable.get());
        assertSame(throwable.get().getClass(), IllegalStateException.class);
        assertEquals("This method must be run on a worker thread.", throwable.get().getMessage());
    }

    @Test
    public void noCrashIfRunningOnWorkerThread() {
        new KotlinTest().crashIfNotWorkerThread();
    }
}
