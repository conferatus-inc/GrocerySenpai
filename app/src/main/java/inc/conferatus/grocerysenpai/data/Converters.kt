package inc.conferatus.grocerysenpai.data

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    @TypeConverter
    fun dateFromTimestamp(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
