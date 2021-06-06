package school.cactus.succulentshop.signup.validation

import school.cactus.succulentshop.R
import school.cactus.succulentshop.helpers.isContainDigitSpecialLowerUpper
import school.cactus.succulentshop.validation.Validator

class SignupPasswordValidator : Validator {
    override fun validate(field: String): Int? = when {
        field.isEmpty() -> R.string.password_is_required
        field.length <= 7 -> R.string.password_is_too_short
        field.length >= 40 -> R.string.password_is_too_long
        field.isContainDigitSpecialLowerUpper().not() -> R.string.password_must_contains
        else -> null
    }
}