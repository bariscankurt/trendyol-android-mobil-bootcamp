package school.cactus.succulentshop.product.list

import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import school.cactus.succulentshop.R
import school.cactus.succulentshop.api.GenericRequestCallback
import school.cactus.succulentshop.auth.JwtStore
import school.cactus.succulentshop.infra.BaseViewModel
import school.cactus.succulentshop.infra.snackbar.SnackbarAction
import school.cactus.succulentshop.infra.snackbar.SnackbarState
import school.cactus.succulentshop.product.ProductItem

class ProductListViewModel(
    private val repository: ProductListRepository,
    private val store: JwtStore
) : BaseViewModel() {
    private val _products = MutableLiveData<List<ProductItem>>()
    val products: LiveData<List<ProductItem>> = _products

    private var _isProductListLoaded = MutableLiveData<Boolean>()
    var isProductListLoaded: LiveData<Boolean> = _isProductListLoaded

    val itemClickListener: (ProductItem) -> Unit = {
        val action = ProductListFragmentDirections.productListToProductDetail(it.id)
        navigation.navigate(action)
    }

    init {
        fetchProducts()
    }

    private fun fetchProducts(): Unit = repository.fetchProducts(
        object : GenericRequestCallback {
            override fun <T> onSuccess(products: T) {
                _products.value = products as List<ProductItem>
                _isProductListLoaded.value = true
            }

            override fun onClientError(errorMessage: String?) {
                _snackbarState.value = SnackbarState(
                    errorRes = R.string.your_session_is_expired,
                    duration = Snackbar.LENGTH_INDEFINITE,
                    action = SnackbarAction(
                        text = R.string.log_in,
                        action = {
                            navigateToLogin()
                        }
                    )
                )
            }

            override fun onUnexpectedError() {
                _snackbarState.value = SnackbarState(
                    errorRes = R.string.unexpected_error_occurred,
                    duration = Snackbar.LENGTH_LONG,
                )
            }

            override fun onFailure() {
                _snackbarState.value = SnackbarState(
                    errorRes = R.string.check_your_connection,
                    duration = Snackbar.LENGTH_INDEFINITE,
                    action = SnackbarAction(
                        text = R.string.retry,
                        action = {
                            fetchProducts()
                        }
                    )
                )
            }
        })

    fun onMenuItemClicked(item: MenuItem) {
        when (item.itemId) {
            R.id.logOut -> {
                store.deleteJwt()
                navigation.navigate(ProductListFragmentDirections.toLoginFragment())
            }
        }
    }

    private fun navigateToLogin() {
        val directions = ProductListFragmentDirections.toLoginFragment()
        navigation.navigate(directions)
    }

}