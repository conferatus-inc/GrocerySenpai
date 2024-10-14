package inc.conferatus.grocerysenpai.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "groceries")
data class GroceryEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val category: CategoryEntity,
    val description: String,
    val amount: Int,
    val amountPostfix: String,
    val bought: Date?
)