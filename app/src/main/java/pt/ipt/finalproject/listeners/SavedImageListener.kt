package pt.ipt.finalproject.listeners

import java.io.File

interface SavedImageListener {
    fun onImageClicked(file: File)
}