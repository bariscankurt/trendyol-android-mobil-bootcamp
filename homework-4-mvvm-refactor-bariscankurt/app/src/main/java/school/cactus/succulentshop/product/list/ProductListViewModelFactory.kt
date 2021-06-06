package school.cactus.succulentshop.product.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import school.cactus.succulentshop.auth.JwtStore

@Suppress("UNCHECKED_CAST")
class ProductListViewModelFactory(
    private val repository: ProductListRepository,
    private val store: JwtStore
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        ProductListViewModel(repository, store) as T
}