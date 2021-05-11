package school.cactus.succulentshop.validation

import school.cactus.succulentshop.R

class EmailValidator: Validator {

    override fun validateLogin(field: String): Int? = when{
        field.isEmpty() -> R.string.email_is_required
        field.length <= 5 -> R.string.email_is_invalid
        field.length >= 50 -> R.string.email_is_invalid
        field.contains("@",ignoreCase = true).not() -> R.string.email_is_invalid
        else -> null
    }

    override fun validateSingIn(field: String): Int? = when{
        field.isEmpty() -> R.string.email_is_required
        field.length <= 5 || field.length >= 50 -> R.string.email_is_invalid
        field.contains("@",ignoreCase = true).not() -> R.string.email_is_invalid
        else -> null
    }
}