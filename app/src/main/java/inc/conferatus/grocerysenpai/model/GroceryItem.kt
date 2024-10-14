package inc.conferatus.grocerysenpai.model

data class GroceryItem(
    val name : String, // not blank
    val amount : Int = -1,
    val amountPostfix : String = "",
    val description : String = ""// optional
)
