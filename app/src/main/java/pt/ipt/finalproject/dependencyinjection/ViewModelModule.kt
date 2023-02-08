package pt.ipt.finalproject.dependencyinjection

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pt.ipt.finalproject.viewmodels.EditImageViewModel

//patter that allows to separate graphical part and logical
val viewModelModule = module {
    viewModel { EditImageViewModel(editImageRepository = get()) }
}