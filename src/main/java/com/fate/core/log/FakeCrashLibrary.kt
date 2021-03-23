package com.fate.core.log

import android.util.Log
import com.fate.core.log.FakeCrashLibrary.Companion
import timber.log.Timber

class FakeCrashLibrary {
    companion object {
        fun log(priority: Int, tag: String?, message: String?) {
            // TODO add log entry to circular buffer.
        }

        fun logWarning(t: Throwable?) {
            // TODO report non-fatal warning.
        }

        fun logError(t: Throwable?) {
            // TODO report non-fatal error.
        }
    }

    private fun FakeCrashLibrary() {
        throw AssertionError("No instances.")
    }

}

class CrashReportingTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return
        }

        Companion.log(priority, tag, message)
        if (t != null) {
            if (priority == Log.ERROR) {
                Companion.logError(t)
            } else if (priority == Log.WARN) {
                Companion.logWarning(t)
            }
        }
    }

}