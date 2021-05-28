package school.cactus.succulentshop.login.validation

import school.cactus.succulentshop.R
import school.cactus.succulentshop.validation.Validator

class LoginIdentifierValidator : Validator {
    override fun validate(field: String): Int? = when {
        field.isEmpty() -> R.string.this_field_is_required
        field.length < 5 -> R.string.identifier_is_too_short
        else -> null
    }
}