package inc.conferatus.grocerysenpai.data

import androidx.room.TypeConverter
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

class Converters {
    @TypeConverter
    fun dateFromTimestamp(timestamp: Long?): ZonedDateTime? {
        return timestamp?.let { ZonedDateTime.ofInstant(Instant.ofEpochSecond(it), ZoneId.systemDefault()) }
    }

    @TypeConverter
    fun dateToTimestamp(date: ZonedDateTime?): Long? {
        return date?.toEpochSecond()
    }
}
