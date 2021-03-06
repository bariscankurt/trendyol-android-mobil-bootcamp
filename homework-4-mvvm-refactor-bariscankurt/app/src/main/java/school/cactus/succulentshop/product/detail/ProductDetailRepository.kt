package school.cactus.succulentshop.product.detail

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import school.cactus.succulentshop.api.GenericRequestCallback
import school.cactus.succulentshop.api.api
import school.cactus.succulentshop.api.product.Product
import school.cactus.succulentshop.api.product.RelatedProducts
import school.cactus.succulentshop.product.toProductItem
import school.cactus.succulentshop.product.toProductItemList

class ProductDetailRepository {
    fun fetchProductDetail(productId: Int, callback: GenericRequestCallback) {
        api.getProductById(productId).enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                when (response.code()) {
                    200 -> callback.onSuccess(response.body()!!.toProductItem())
                    401 -> callback.onClientError()
                    else -> callback.onUnexpectedError()
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                callback.onFailure()
            }
        })
    }

    fun fetchRelatedProducts(productId: Int, callback: GenericRequestCallback) {
        api.getRelatedProducts(productId).enqueue(object : Callback<RelatedProducts> {
            override fun onResponse(
                call: Call<RelatedProducts>,
                response: Response<RelatedProducts>
            ) {
                when (response.code()) {
                    200 -> callback.onSuccess(response.body()!!.products.toProductItemList())
                    401 -> callback.onClientError()
                    else -> callback.onUnexpectedError()
                }
            }

            override fun onFailure(call: Call<RelatedProducts>, t: Throwable) {
                callback.onFailure()
            }

        })
    }
}