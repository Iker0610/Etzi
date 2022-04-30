package das.losaparecidos.etzi.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


/*******************************************************************************
 ****                              Hilt Module                              ****
 *******************************************************************************/
/**
 *  This module is installed in [SingletonComponent], witch means,
 *  all the instance here are stored in the application level,
 *  so they will not be destroyed until application is finished/killed;
 *  and are shared between activities.
 *
 *  Hilt injects these instances in the required objects automatically.
 */

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    init {
        TODO("IMPLEMENTAR")
    }
}

