package com.example.threatasserter;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.os.Handler;
import android.os.Looper;
import android.support.test.runner.AndroidJUnit4;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.assertNull;

@SuppressWarnings("MessageMissingOnJUnitAssertion")
@RunWith(AndroidJUnit4.class)
public class JavaTest {

    @Test(expected = IllegalStateException.class)
    public void crashIfRunningOnWorkerThread() {
        MainActivity.onClickWithWorkerThreadHelper(getTargetContext().getApplicationContext());
    }

    @Test(timeout = 2000)
    public void noCrashIfRunningOnUiThread() throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final AtomicReference<Throwable> throwable = new AtomicReference<>();
        new Handler(Looper.getMainLooper())
                .post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            MainActivity.onClickWithWorkerThreadHelper(
                                    getTargetContext().getApplicationContext());
                        } catch (Exception e) {
                            throwable.set(e);
                        } finally {
                            countDownLatch.countDown();
                        }
                    }
                });
        countDownLatch.await();

        assertNull(throwable.get());
    }

}
