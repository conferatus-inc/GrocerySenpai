package inc.conferatus.grocerysenpai.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.Date

@Entity(tableName = "groceries")
data class GroceryEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val categoryId: Int,
    val description: String,
    val amount: Int,
    val amountPostfix: String,
    val bought: ZonedDateTime?
)

data class GroceryAndCategory(
    @Embedded val groceryEntity: GroceryEntity,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val categoryEntity: CategoryEntity
)
