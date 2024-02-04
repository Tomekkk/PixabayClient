package com.tcode.pixabayclient.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tcode.pixabayclient.R

@Composable
fun PixabayFooter(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.powered_by),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.wrapContentWidth(),
        )

        Image(
            painter = painterResource(id = R.drawable.pixabay),
            contentDescription = stringResource(R.string.pixabay_logo),
            modifier = Modifier
                .height(24.dp)
                .padding(start = 8.dp),
            contentScale = ContentScale.FillHeight,
        )
    }
}

@Preview
@Composable
fun PixabayFooterPreview() {
    PixabayFooter(
        Modifier
            .background(color = Color.White)
            .fillMaxWidth()
            .padding(8.dp))
}
