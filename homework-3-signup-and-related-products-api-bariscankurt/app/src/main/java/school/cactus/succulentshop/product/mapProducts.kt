package school.cactus.succulentshop.product

import school.cactus.succulentshop.api.BASE_URL
import school.cactus.succulentshop.api.product.Product

fun List<Product>.toProductItemList() = map {
    it.toProductItem()
}

fun Product.toProductItem(): ProductItem {
    return ProductItem(
        id = this.id,
        title = this.title,
        description = this.description,
        price = this.price.formatPrice(),
        imageUrl = BASE_URL + image.formats.small.url,
        highResImageUrl = BASE_URL + image.formats.medium.url,
        originalResImageUrl = BASE_URL + image.formats.large.url
    )
}

private fun Double.formatPrice(): String {
    return String.format("$%.2f", this)
}