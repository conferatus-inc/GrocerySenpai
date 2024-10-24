package inc.conferatus.grocerysenpai.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import inc.conferatus.grocerysenpai.data.GroceryDatabase
import inc.conferatus.grocerysenpai.data.repository.CategoryRepositoryImpl
import inc.conferatus.grocerysenpai.data.repository.GroceryRepositoryImpl
import inc.conferatus.grocerysenpai.model.repository.CategoryRepository
import inc.conferatus.grocerysenpai.model.repository.GroceryRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): GroceryDatabase {
        return Room.databaseBuilder(
            app,
            GroceryDatabase::class.java,
            GroceryDatabase.DATABASE_NAME)
            .createFromAsset("database/prep_categories.db")
            .allowMainThreadQueries()
//            .fallbackToDestructiveMigration() // ?? ttemporary TODO
            .build()
    }

    @Provides
    @Singleton
    fun provideGroceryRepository(db: GroceryDatabase): GroceryRepository {
        return GroceryRepositoryImpl(db.groceryDao)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(db: GroceryDatabase): CategoryRepository {
        return CategoryRepositoryImpl(db.categoryDao)
    }
}
