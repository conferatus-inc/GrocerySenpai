package inc.conferatus.grocerysenpai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import inc.conferatus.grocerysenpai.presentation.mainlist.MainListScreen
import inc.conferatus.grocerysenpai.presentation.mainlist.MainListViewModel
import inc.conferatus.grocerysenpai.ui.theme.GrocerySenpaiTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val viewModel = MainListViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GrocerySenpaiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainListScreen(viewModel)
                }
            }
        }
    }
}
