package pt.ipt.finalproject.dependencyinjection

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pt.ipt.finalproject.viewmodels.EditImageViewModel

val viewModelModule = module {
    viewModel { EditImageViewModel(editImageRepository = get()) }
}