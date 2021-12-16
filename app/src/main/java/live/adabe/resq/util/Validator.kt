package live.adabe.resq.util

object Validator {
    private val PHONE_REGEX = "^((\\+234)|(0))\\d{10}\\b".toRegex()

    fun isValidPhone(value: String): Boolean{
        return PHONE_REGEX.matches(value.trim())
    }
}