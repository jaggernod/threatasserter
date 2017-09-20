package com.jaggernod.threatasserter.internal;

import android.os.Looper;

final class Assertions {

    private Assertions() {
    }

    static void assertWorkerThread() {
        if (isMainThread()) {
            throw new IllegalStateException("This method must be run on a worker thread.");
        }

    }

    static void assertMainThread() {
        if (!isMainThread()) {
            throw new IllegalStateException("This method must be run on the Main thread.");
        }
    }

    private static boolean isMainThread() {
        return equals(Looper.getMainLooper(), Looper.myLooper());
    }

    private static boolean equals(Object a, Object b) {
        return a == b || a != null && a.equals(b);
    }
}
