package pt.ipt.finalproject.dependencyinjection

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import pt.ipt.finalproject.repositories.EditImageRepository
import pt.ipt.finalproject.repositories.EditImageRepositoryImpl


val repositoryModule = module{
    factory<EditImageRepository> { EditImageRepositoryImpl(androidContext()) }
}