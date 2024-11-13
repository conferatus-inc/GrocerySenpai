package inc.conferatus.grocerysenpai.model

import org.apache.commons.math3.stat.regression.SimpleRegression
import kotlin.math.max

const val THRESHOLD = 0.5

fun predict(dayNumbers: List<Long>): Long {
    val regression = SimpleRegression()

    var iterDay: Long = 0
    var currBuys: Long = 0

    for (dayNo in dayNumbers.distinct().sorted()) {
        while (iterDay < dayNo) {
            regression.addData(iterDay.toDouble(), currBuys.toDouble())
            iterDay++
        }

        currBuys++
        iterDay++
        regression.addData(dayNo.toDouble(), currBuys.toDouble())
    }

    var nextDay = iterDay + 1
    while (regression.predict(nextDay.toDouble()) < currBuys + 1) {
        nextDay++
    }

    return nextDay
}
