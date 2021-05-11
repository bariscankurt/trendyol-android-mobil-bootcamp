package school.cactus.succulentshop

interface Validator {
    fun validateLogin(field: String): Int?
    fun validateSingIn(field: String): Int?
}