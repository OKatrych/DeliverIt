package eu.warble.deliverit.util

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AppExecutors(
    private val networkIO: Executor,
    private val mainThread: Executor
) {

    // For Singleton instantiation and task management
    companion object {
        private val INSTANCE = AppExecutors(
            Executors.newFixedThreadPool(3),
            MainThreadExecutor()
        )

        fun NETWORK() = INSTANCE.networkIO
        fun MAIN() = INSTANCE.mainThread
    }

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}