package school.cactus.succulentshop.imageviewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class ImageViewerViewModelFactory(
    private val originalResImageUrl: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        ImageViewerViewModel(originalResImageUrl) as T

}