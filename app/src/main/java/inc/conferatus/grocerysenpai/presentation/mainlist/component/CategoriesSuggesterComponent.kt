import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoriesSuggesterComponent(
    onCategoryClick: (String) -> Unit,
    categories: List<String>,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier
            .padding(horizontal = 10.dp)
            .animateContentSize(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        categories.forEach {
            TextButton(
                onClick = { onCategoryClick(it) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}