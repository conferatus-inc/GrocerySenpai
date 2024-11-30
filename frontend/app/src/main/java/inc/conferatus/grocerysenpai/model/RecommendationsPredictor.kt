package inc.conferatus.grocerysenpai.model

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression

fun predict(dayNumbers: List<Long>): Long {
//    if (dayNumbers.size < 3) {
//        return -1;
//    }

    val THRESHOLD = 0.8

    fun convert(dayNumber: Long): DoubleArray {
        return doubleArrayOf(dayNumber.toDouble(), (dayNumber / 7).toDouble(), (dayNumber / 30).toDouble())
    }

    fun predictedResult(regression: OLSMultipleLinearRegression, triple: DoubleArray): Double {
        return regression.estimateRegressionParameters()[0] * triple[0] +
                regression.estimateRegressionParameters()[1] * triple[1] +
                regression.estimateRegressionParameters()[2] * triple[2]
    }


    val regression = OLSMultipleLinearRegression()

    val x = mutableListOf<DoubleArray>()
    val y = mutableListOf<Double>()

    var iterDay: Long = 0
    var currBuys: Long = 0

    for (dayNo in dayNumbers.distinct().sorted()) {
        while (iterDay < dayNo) {
            x.add(convert(iterDay))
            y.add(currBuys.toDouble())
            iterDay++
        }

        currBuys++
        iterDay++
        x.add(convert(dayNo))
        y.add(currBuys.toDouble())
    }

    regression.newSampleData(y.toDoubleArray(), x.toTypedArray())

    var nextDay = iterDay + 1
    while (predictedResult(regression, convert(nextDay)) < currBuys + THRESHOLD) {
        nextDay++
    }

    return nextDay
}
