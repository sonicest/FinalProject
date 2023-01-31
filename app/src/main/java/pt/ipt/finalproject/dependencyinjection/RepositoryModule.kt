package pt.ipt.finalproject.dependencyinjection

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import pt.ipt.finalproject.repositories.EditImageRepository
import pt.ipt.finalproject.repositories.EditImageRepositoryImpl
import pt.ipt.finalproject.repositories.SavedImagesRepository
import pt.ipt.finalproject.repositories.SavedImagesRepositoryImpl

val repositoryModule = module{
    factory<EditImageRepository> { EditImageRepositoryImpl(androidContext()) }
    factory<SavedImagesRepository>{ SavedImagesRepositoryImpl(androidContext()) }
}