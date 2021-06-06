package school.cactus.succulentshop.product.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import school.cactus.succulentshop.R
import school.cactus.succulentshop.api.GenericRequestCallback
import school.cactus.succulentshop.infra.BaseViewModel
import school.cactus.succulentshop.infra.snackbar.SnackbarAction
import school.cactus.succulentshop.infra.snackbar.SnackbarState
import school.cactus.succulentshop.product.ProductItem
import school.cactus.succulentshop.product.list.ProductListFragmentDirections

class ProductDetailViewModel(
    private val productId: Int,
    private val repository: ProductDetailRepository
) : BaseViewModel() {

    private val _product = MutableLiveData<ProductItem>()
    val product: LiveData<ProductItem> = _product

    private val _products = MutableLiveData<List<ProductItem>>()
    val products: LiveData<List<ProductItem>> = _products

    private var _isProductLoaded = MutableLiveData<Boolean>()
    var isProductLoaded: LiveData<Boolean> = _isProductLoaded

    private var _isRelatedProductsLoaded = MutableLiveData<Boolean>()
    var isRelatedProductsLoaded: LiveData<Boolean> = _isRelatedProductsLoaded

    private var originalResImageUrl = ""

    val itemClickListener: (ProductItem) -> Unit = {
        val action = ProductDetailFragmentDirections.productDetailToSelf(it.id)
        navigation.navigate(action)
    }

    init {
        fetchProduct()
        fetchRelatedProduct()
    }


    fun fetchProduct() {
        repository.fetchProductDetail(productId, object : GenericRequestCallback {
            override fun <T> onSuccess(product: T) {
                _product.value = product as ProductItem
                _isProductLoaded.value = true
                originalResImageUrl = product.originalResImageUrl
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
                            fetchProduct()
                            fetchRelatedProduct()
                        }
                    )
                )
            }
        })
    }

    fun openZoomableScreen() {
        val action = ProductDetailFragmentDirections.productDetailToImageViewer(originalResImageUrl)
        navigation.navigate(action)
    }

    fun fetchRelatedProduct() {
        repository.fetchRelatedProducts(productId, object : GenericRequestCallback {
            override fun <T> onSuccess(products: T) {
                _products.value = products as List<ProductItem>
                _isRelatedProductsLoaded.value = true
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
                            fetchProduct()
                            fetchRelatedProduct()
                        }
                    )
                )
            }
        })
    }

    private fun navigateToLogin() {
        val directions = ProductListFragmentDirections.toLoginFragment()
        navigation.navigate(directions)
    }
}