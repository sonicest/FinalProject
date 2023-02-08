package pt.ipt.finalproject.utilities

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//allow execution to be suspended and resumed
object Coroutines {
    fun io(work: suspend(() -> Unit)) =
        CoroutineScope(Dispatchers.IO).launch{work()}
}