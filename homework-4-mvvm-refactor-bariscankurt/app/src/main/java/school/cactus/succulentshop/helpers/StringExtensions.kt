package school.cactus.succulentshop.helpers

fun String.isContainSpecialChar(): Boolean {
    for (char in this) {
        if (char.isLetterOrDigit().not() && char != '_') return true
    }
    return false
}

fun String.isContainDigitSpecialLowerUpper(): Boolean {
    var digitFlag = false
    var upperFlag = false
    var lowerFlag = false
    var specialFlag = false
    for (char in this) {
        if (char.isDigit())
            digitFlag = true
        if (char.isUpperCase())
            upperFlag = true
        if (char.isLowerCase())
            lowerFlag = true
        if (char.isLetterOrDigit().not())
            specialFlag = true
    }
    return digitFlag && upperFlag && lowerFlag && specialFlag
}