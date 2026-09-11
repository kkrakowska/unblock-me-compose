package com.kkrakowska.unblockme.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val c = LocalAppColors.current
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = c.accent,
            contentColor   = Color.White
        )
    ) {
        Text(text, fontSize = 15.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.5.sp)
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val c = LocalAppColors.current
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = c.btn,
            contentColor   = c.text
        ),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, c.btnBorder)
    ) {
        Text(text, fontSize = 15.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun ScreenHeader(
    title: String,
    onBack: () -> Unit,
    trailingContent: @Composable (() -> Unit)? = null
) {
    val c = LocalAppColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.size(36.dp),
            shape = RoundedCornerShape(9.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = c.btn,
                contentColor   = c.text
            ),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, c.btnBorder)
        ) {
            Text("←", fontSize = 18.sp, color = c.text)
        }

        Text(
            text = title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = c.text
        )

        Box(modifier = Modifier.width(36.dp)) {
            trailingContent?.invoke()
        }
    }
}
