package school.cactus.succulentshop

import school.cactus.succulentshop.helpers.isContainSpecialChar

class UsernameValidator : Validator {


    override fun validateLogin(field: String): Int? = when{
        field.isEmpty() -> R.string.username_is_required
        else -> null
    }

    override fun validateSingIn(field: String): Int? = when{
        field.isEmpty() -> R.string.username_is_required
        field.isContainSpecialChar() -> R.string.username_can_only_include
        field.length <= 2 -> R.string.username_is_too_short
        field.length >= 20 -> R.string.username_is_too_long
        else -> null
    }
}