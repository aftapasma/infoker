package org.d3if.infoker.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.d3if.infoker.ui.theme.InfokerTheme
import org.d3if.infoker.ui.theme.backgroundLight

@Composable
fun ProfileScreen(

) {
    Column(
        modifier = Modifier.fillMaxHeight().fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary)
//            .padding(16.dp)
        , horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header()
        Spacer(modifier = Modifier.height(16.dp))
        ProfileCompletionSection()
        Spacer(modifier = Modifier.height(16.dp))
        ProfileOptions()
    }
}

@Composable
fun Header() {
    Row(
        modifier = Modifier.fillMaxWidth().background(
            color = MaterialTheme.colorScheme.secondary,
            shape = RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(64.dp)
                .background(Color.Gray, shape = CircleShape)

        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = "Jawir",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(text = "jawir@email")
        }
    }
}

@Composable
fun ProfileCompletionSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "WhatsApp",
                tint = Color.Green
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = "bingung mo isi apa")
                TextButton(onClick = { /*TODO*/ }) {
                    Text(text = "Tambah Sekarang")
                }
            }
        }
    }
}

@Composable
fun ProfileOptions() {

    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            OptionItem(title = "Mboh Isino dewe")
            OptionItem(title = "Tambah CV")
            OptionItem(title = "Preferensi Kerja")
        }
    }

}

@Composable
fun OptionItem(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text(text = title, modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.AutoMirrored.Filled.NavigateNext, contentDescription = "Next")
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    InfokerTheme {
        ProfileScreen()
    }
}

