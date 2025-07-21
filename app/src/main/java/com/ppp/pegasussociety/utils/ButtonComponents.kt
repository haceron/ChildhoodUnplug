package com.ppp.pegasussociety.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ppp.pegasussociety.ui.theme.Greeny
@Composable
fun Custombtn2(
    text: String,
    background: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = background,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 16.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                fontSize = 22.sp,
                maxLines = 1,
                style = TextStyle(
                    color = Greeny, // Match loading button text color
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .background(background)
                    .padding(5.dp, 8.dp)
                    .fillMaxWidth()
            )
        }
    }
}


@Composable
fun Custombtn1(
    text: String,
    background:Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    Surface(shape = RoundedCornerShape(CornerSize(size = 12.dp)),
        modifier = modifier
            .clip(RoundedCornerShape(corner = CornerSize(10.dp)))
            .background(background)
            .padding(8.dp)
            .clickable{
                onClick.invoke()
            }

    ){
        Text(
            text = text,
            textAlign = TextAlign.Center,
            maxLines = 1,
            fontSize = 22.sp,
            style = TextStyle(
                color = Color.White,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .background(background)
                .padding(5.dp, 8.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun ImageButton(
    onClick: () -> Unit,
    modifier : Modifier = Modifier,
    imageRes: Int,
    contentDescription: String? = null
){
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = contentDescription,
        modifier = modifier
            .fillMaxWidth(1f)
            .height(80.dp)
            .clickable(onClick = onClick)
    )
}

@Composable
fun BuyButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFbb2030), // Match the green shade from the image
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(6.dp) // Slightly rounded
    ) {
        Text(
            text = "B",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}