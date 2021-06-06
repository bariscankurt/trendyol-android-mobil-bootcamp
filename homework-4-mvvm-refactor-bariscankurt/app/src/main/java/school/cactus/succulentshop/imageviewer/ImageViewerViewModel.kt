package school.cactus.succulentshop.imageviewer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import school.cactus.succulentshop.infra.BaseViewModel

class ImageViewerViewModel(
    private val originalResImageUrl: String
) : BaseViewModel() {
    private val _originalImageUrl = MutableLiveData<String>()
    val originalImageUrl: LiveData<String> = _originalImageUrl

    init {
        _originalImageUrl.value = originalResImageUrl
    }
}