package school.cactus.succulentshop.api

interface GenericRequestCallback {
    fun <T> onSuccess(obj: T)
    fun onClientError(errorMessage: String? = null)
    fun onUnexpectedError()
    fun onFailure()
}