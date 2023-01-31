package pt.ipt.finalproject.dependencyinjection

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pt.ipt.finalproject.viewmodels.EditImageViewModel
import pt.ipt.finalproject.viewmodels.SavedImagesViewModel

val viewModelModule = module {
    viewModel { EditImageViewModel(editImageRepository = get()) }
    viewModel { SavedImagesViewModel(savedImagesRepository = get()) }
}