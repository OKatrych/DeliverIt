package eu.warble.deliverit.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import eu.warble.deliverit.data.di.NETWORK_EXECUTOR_NAME
import eu.warble.deliverit.data.di.UI_SCHEDULER_NAME
import eu.warble.deliverit.util.AppExecutors
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.Executor
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun providesApplication(): Application {
        return application
    }

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Named(UI_SCHEDULER_NAME)
    @Provides
    @Singleton
    fun provideUIScheduler(): Scheduler = AndroidSchedulers.mainThread()

    @Named(NETWORK_EXECUTOR_NAME)
    @Provides
    @Singleton
    fun provideNetworkExecutor(): Executor = AppExecutors.NETWORK()
}