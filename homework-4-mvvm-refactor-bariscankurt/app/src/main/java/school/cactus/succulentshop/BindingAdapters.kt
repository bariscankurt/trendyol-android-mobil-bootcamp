package school.cactus.succulentshop

import android.view.View
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.Group
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputLayout
import school.cactus.succulentshop.product.ProductItem
import school.cactus.succulentshop.product.detail.RelatedProductAdapter
import school.cactus.succulentshop.product.detail.RelatedProductDecoration
import school.cactus.succulentshop.product.list.ProductAdapter
import school.cactus.succulentshop.product.list.ProductDecoration

@BindingAdapter("app:error")
fun TextInputLayout.error(@StringRes errorMessage: Int?) {
    error = errorMessage?.resolveAsString(context)
    isErrorEnabled = errorMessage != null
}

val productAdapter = ProductAdapter()

@BindingAdapter("app:products", "app:itemClickListener")
fun RecyclerView.products(products: List<ProductItem>?, itemClickListener: (ProductItem) -> Unit) {

    productAdapter.itemClickListener = itemClickListener

    adapter = productAdapter

    if (itemDecorationCount == 0) {
        addItemDecoration(ProductDecoration())
    }

    productAdapter.submitList(products.orEmpty())
}

val relatedProductAdapter = RelatedProductAdapter()

@BindingAdapter("app:relatedProducts", "app:itemClickListener")
fun RecyclerView.relatedProducts(
    relatedProducts: List<ProductItem>?,
    itemClickListener: (ProductItem) -> Unit
) {
    relatedProductAdapter.itemClickListener = itemClickListener

    adapter = relatedProductAdapter

    if (itemDecorationCount == 0) {
        addItemDecoration(RelatedProductDecoration())
    }

    relatedProductAdapter.submitList(relatedProducts.orEmpty())
}

@BindingAdapter("app:showProductList")
fun RecyclerView.showProductList(
    showProductList: Boolean
) {
    if (showProductList) visibility = View.VISIBLE
}

@BindingAdapter("app:showProductListLoading")
fun CircularProgressIndicator.showProductListLoading(
    showProductListLoading: Boolean
) {
    if (showProductListLoading) visibility = View.INVISIBLE
}

@BindingAdapter("app:showProductDetail", "app:showRelatedProducts")
fun Group.showProductDetail(
    showProductDetail: Boolean,
    showRelatedProducts: Boolean
) {
    if (showProductDetail && showRelatedProducts) visibility = View.VISIBLE
}

@BindingAdapter("app:showProductDetailLoading", "app:showRelatedProductLoading")
fun CircularProgressIndicator.showProductDetailLoading(
    showProductDetailLoading: Boolean,
    showRelatedProductLoading: Boolean
) {
    if (showProductDetailLoading && showRelatedProductLoading) visibility = View.INVISIBLE
}

@BindingAdapter("app:imageUrl")
fun ImageView.imageUrl(imageUrl: String?) {
    imageUrl?.let {
        Glide.with(this)
            .load(it)
            .into(this)
    }
}