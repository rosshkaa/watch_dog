package task

data class PingResult(val code: Int, val message: String? = "") {
    public fun getResultMessage(): String {
        return when (code) {
            0 -> "reachable"
            1 -> "unreachable"
            else -> "error: $message"
        }
    }
}
