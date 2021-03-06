package das.losaparecidos.etzi.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import das.losaparecidos.etzi.app.utils.AESCipher
import das.losaparecidos.etzi.app.utils.CipherUtil
import das.losaparecidos.etzi.model.database.EtziDatabase
import das.losaparecidos.etzi.model.datastore.Datastore
import das.losaparecidos.etzi.model.repositories.ILoginRepository
import das.losaparecidos.etzi.model.repositories.LoginRepository
import das.losaparecidos.etzi.model.webclients.AuthenticationClient
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
    @Singleton
    @Provides
    fun providesEtziDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(app, EtziDatabase::class.java, "etzi")
            .fallbackToDestructiveMigration()
            .build()


    //------------------   DAOs   ------------------//

    @Singleton
    @Provides
    fun provideTimetableDao(db: EtziDatabase) = db.timetableDao()

    @Singleton
    @Provides
    fun provideRemainderDao(db: EtziDatabase) = db.reminderDao()


    /*************************************************
     **                 Repositories                **
     *************************************************/

    @Singleton
    @Provides
    fun provideLoginRepository(authenticationClient: AuthenticationClient, datastore: Datastore): ILoginRepository =
        LoginRepository(authenticationClient, datastore)


    /*************************************************
     **                    Utils                    **
     *************************************************/

    @Singleton
    @Provides
    fun provideCipher(): CipherUtil = AESCipher()
}

