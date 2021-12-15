package live.adabe.resq.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import live.adabe.resq.util.Preferences
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providePreferences(application: Application): Preferences = Preferences(application)

    @Provides
    @Singleton
    fun provideCicerone(): Cicerone<Router> = Cicerone.create()
}