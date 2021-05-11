package school.cactus.succulentshop.validation

interface Validator {
    fun validateLogin(field: String): Int?
    fun validateSingIn(field: String): Int?
}