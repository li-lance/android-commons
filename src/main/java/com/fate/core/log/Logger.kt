package com.fate.core.log

import android.app.Application
import timber.log.Timber

fun Application.initLogger(isDebug: Boolean) {
  if (isDebug) {
    Timber.plant(Timber.DebugTree())
  } else {
    Timber.plant(CrashReportingTree())
  }
}

object Logger {
  /** Log a verbose exception and a message that will be evaluated lazily when the message is printed */
  @JvmStatic inline fun v(t: Throwable? = null, message: () -> String) =
    log { Timber.v(t, message()) }

  @JvmStatic fun v(t: Throwable?) = Timber.v(t)

  /** Log a debug exception and a message that will be evaluated lazily when the message is printed */
  @JvmStatic inline fun d(t: Throwable? = null, message: () -> String) =
    log { Timber.d(t, message()) }

  @JvmStatic fun d(t: Throwable?) = Timber.d(t)

  /** Log an info exception and a message that will be evaluated lazily when the message is printed */
  @JvmStatic inline fun i(t: Throwable? = null, message: () -> String) =
    log { Timber.i(t, message()) }

  @JvmStatic fun i(t: Throwable?) = Timber.i(t)

  /** Log a warning exception and a message that will be evaluated lazily when the message is printed */
  @JvmStatic inline fun w(t: Throwable? = null, message: () -> String) =
    log { Timber.w(t, message()) }

  @JvmStatic fun w(t: Throwable?) = Timber.w(t)

  /** Log an error exception and a message that will be evaluated lazily when the message is printed */
  @JvmStatic inline fun e(t: Throwable? = null, message: () -> String) =
    log { Timber.e(t, message()) }

  @JvmStatic fun e(t: Throwable?) = Timber.e(t)

  /** Log an assert exception and a message that will be evaluated lazily when the message is printed */
  @JvmStatic inline fun wtf(t: Throwable? = null, message: () -> String) =
    log { Timber.wtf(t, message()) }

  @JvmStatic fun wtf(t: Throwable?) = Timber.wtf(t)

  // These functions just forward to the real timber. They aren't necessary, but they allow method
  // chaining like the normal Timber interface.

  /** A view into Timber's planted trees as a tree itself. */
  @JvmStatic fun asTree(): Timber.Tree = Timber.asTree()

  /** Add a new logging tree. */
  @JvmStatic fun plant(tree: Timber.Tree) = Timber.plant(tree)

  /** Set a one-time tag for use on the next logging call. */
  @JvmStatic fun tag(tag: String): Timber.Tree = Timber.tag(tag)

  /** A view into Timber's planted trees as a tree itself. */
  @JvmStatic fun uproot(tree: Timber.Tree) = Timber.uproot(tree)

  /** Set a one-time tag for use on the next logging call. */
  @JvmStatic fun uprootAll() = Timber.uprootAll()

  /** A [Timber.Tree] for debug builds. Automatically infers the tag from the calling class. */
  @JvmStatic fun DebugTree() = Timber.DebugTree()
}

//
// Extensions on trees
//

/** Log a verbose exception and a message that will be evaluated lazily when the message is printed */
inline fun Timber.Tree.v(t: Throwable? = null, message: () -> String) = log { v(t, message()) }

/** Log a debug exception and a message that will be evaluated lazily when the message is printed */
inline fun Timber.Tree.d(t: Throwable? = null, message: () -> String) = log { d(t, message()) }

/** Log an info exception and a message that will be evaluated lazily when the message is printed */
inline fun Timber.Tree.i(t: Throwable? = null, message: () -> String) = log { i(t, message()) }

/** Log a warning exception and a message that will be evaluated lazily when the message is printed */
inline fun Timber.Tree.w(t: Throwable? = null, message: () -> String) = log { w(t, message()) }

/** Log an error exception and a message that will be evaluated lazily when the message is printed */
inline fun Timber.Tree.e(t: Throwable? = null, message: () -> String) = log { e(t, message()) }

/** Log an assert exception and a message that will be evaluated lazily when the message is printed */
inline fun Timber.Tree.wtf(t: Throwable? = null, message: () -> String) = log { wtf(t, message()) }

//
// Plain functions
//

/** Log a verbose exception and a message that will be evaluated lazily when the message is printed */
inline fun v(t: Throwable? = null, message: () -> String) = log { Timber.v(t, message()) }
fun v(t: Throwable?) = Timber.v(t)

/** Log a debug exception and a message that will be evaluated lazily when the message is printed */
inline fun d(t: Throwable? = null, message: () -> String) = log { Timber.d(t, message()) }
fun d(t: Throwable?) = Timber.d(t)

/** Log an info exception and a message that will be evaluated lazily when the message is printed */
inline fun i(t: Throwable? = null, message: () -> String) = log { Timber.i(t, message()) }
fun i(t: Throwable?) = Timber.i(t)

/** Log a warning exception and a message that will be evaluated lazily when the message is printed */
inline fun w(t: Throwable? = null, message: () -> String) = log { Timber.w(t, message()) }
fun w(t: Throwable?) = Timber.w(t)

/** Log an error exception and a message that will be evaluated lazily when the message is printed */
inline fun e(t: Throwable? = null, message: () -> String) = log { Timber.e(t, message()) }
fun e(t: Throwable?) = Timber.e(t)

/** Log an assert exception and a message that will be evaluated lazily when the message is printed */
inline fun wtf(t: Throwable? = null, message: () -> String) = log { Timber.wtf(t, message()) }
fun wtf(t: Throwable?) = Timber.wtf(t)

/** @suppress */
@PublishedApi
internal inline fun log(block: () -> Unit) {
  if (Timber.treeCount() > 0) block()
}