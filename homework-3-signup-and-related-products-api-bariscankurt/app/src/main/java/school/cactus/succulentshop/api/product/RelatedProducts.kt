package school.cactus.succulentshop.api.product

data class RelatedProducts(
    val id: Int,
    val products: List<Product>
)
