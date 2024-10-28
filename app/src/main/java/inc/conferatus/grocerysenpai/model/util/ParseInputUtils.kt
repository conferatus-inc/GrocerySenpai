package inc.conferatus.grocerysenpai.model.util

class ParseInputUtils(private val input: String) {
    private val parsedInput = ArrayList<String>()
    private var isParsed = false;

    private fun parse() {
        if (isParsed) {
            return
        }
        parsedInput.addAll(input.split(" ", ","))
        parsedInput.removeIf { it == " " }
        isParsed = true
    }

    fun getCategory(): String? {
        parse()
        if (parsedInput.size > 0) {
            return parsedInput[0]
        }
        return null
    }

    fun getAmount(): String? {
        parse()
        if (parsedInput.size > 1) {
            return parsedInput[1]
        }
        return null
    }

    fun getAmountPostfix(): String? {
        parse()
        if (parsedInput.size > 2) {
            return parsedInput[2]
        }
        return null
    }
}