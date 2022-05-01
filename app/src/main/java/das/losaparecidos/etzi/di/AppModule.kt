package das.losaparecidos.etzi.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import das.losaparecidos.etzi.app.utils.AESCipher
import das.losaparecidos.etzi.app.utils.CipherUtil
import javax.inject.Singleton


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
    // With Singleton we tell Dagger-Hilt to create a singleton accessible everywhere in ApplicationComponent (i.e. everywhere in the application)

    /*************************************************
     **           ROOM Database Instances           **
     *************************************************/
/*
    @Singleton
    @Provides
    fun providesOmegaterapiaVisitsDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(app, EtziDatabase::class.java, "etzi_database").build()
*/


    //------------------   DAOs   ------------------//

    // TODO


    /*************************************************
     **                 Repositories                **
     *************************************************/

    // TODO


    /*************************************************
     **                    Utils                    **
     *************************************************/

    @Singleton
    @Provides
    fun provideCipher(): CipherUtil = AESCipher()
}

