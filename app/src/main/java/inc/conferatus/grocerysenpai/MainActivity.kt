package inc.conferatus.grocerysenpai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import inc.conferatus.grocerysenpai.presentation.grocerylist.GroceryListScreen
import inc.conferatus.grocerysenpai.presentation.grocerylist.GroceryListViewModel
import inc.conferatus.grocerysenpai.ui.theme.GrocerySenpaiTheme

//@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val viewModel = GroceryListViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GrocerySenpaiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GroceryListScreen(viewModel)
                }
            }
        }
    }
}
