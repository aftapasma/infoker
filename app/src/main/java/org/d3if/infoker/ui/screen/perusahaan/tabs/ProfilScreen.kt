package org.d3if.infoker.ui.screen.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.d3if.infoker.ui.theme.InfokerTheme

@Composable
fun ProfilScreen() {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
        , horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderCompany()
        BeforePersonalCompany()
        PersonalCompany()
    }
}

@Composable
fun HeaderCompany() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
//            shape = RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp)
            )
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
fun PersonalCompany() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Deskripsi Perusahaan",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
            IconButton(onClick = { /* Edit action */ }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                )
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
//            backgroundColor = Color(0xFF1E1E1E)
        ) {
            Text(
                text = "P",
                color = Color.Gray,
                modifier = Modifier.padding( 16.dp)
            )
        }

    }
}

// tampilan sebelum klik tombol tambah
@Composable
fun BeforePersonalCompany() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Deskripsi Perusahaan",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )

        }
        Text(
            text = "Beritahu Pegawai tentang perusahaan anda",
            color = Color.Gray,
            modifier = Modifier.padding( 4.dp)
        )
        OutlinedButton(
            onClick = { /*Tambah pendidikan*/ },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Masukan Deskripsi")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    InfokerTheme {
        ProfilScreen()
    }
}