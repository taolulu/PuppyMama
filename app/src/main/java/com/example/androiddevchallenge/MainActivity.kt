/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import coil.imageLoader
import com.example.androiddevchallenge.model.Puppy
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.theme.typography
import dev.chrisbanes.accompanist.coil.CoilImage
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

class DetailActivity: AppCompatActivity() {

    private val puppy by lazy {
        DataSource.tempData.find { it.id == intent.getStringExtra(KEY_ID) }
    }

    companion object {
        private const val KEY_ID = "id"
        fun getCallIntent(context: Context, id: String): Intent {
            return Intent(context, DetailActivity::class.java).apply {
                putExtra(KEY_ID, id)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                PuppyDetail(puppy = puppy ?: return@MyTheme)
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp() {
    Scaffold(topBar = {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)) {
            Text(
                text = "Puppy Mama",
                modifier = Modifier.padding(start = 20.dp),
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                style = typography.subtitle1
            )
        }
    }) {
        PuppyList()
    }
}

@Composable
fun PuppyDetail(puppy: Puppy) {
    Scaffold(topBar = {
        val likeStatus = remember { mutableStateOf(puppy.like) }
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)) {
            Text(
                text = puppy.name ?: "",
                modifier = Modifier
                    .padding(start = 20.dp)
                    .weight(1f),
                fontWeight = FontWeight.Bold,
                style = typography.subtitle1
            )
            LikeButton(Modifier, likeStatus.value) {
                likeStatus.value = it
                puppy.like = it
            }

        }
    }) {

    }
}

@Composable
fun PuppyList() {
    val scrollState = rememberLazyListState()

    LazyColumn(state = scrollState, modifier = Modifier.fillMaxWidth()) {
        items(DataSource.tempData.size) {
            Card(modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 5.dp)
                .height(100.dp)
                .fillMaxWidth()) {
                PuppyListItem(it)
            }
        }
    }
}

@Composable
fun PuppyListItem(index: Int) {
    val item = DataSource.tempData[index]
    val context = LocalContext.current
    val likeStatus = remember { mutableStateOf(item.like) }
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable {
        context.startActivity(DetailActivity.getCallIntent(context, item.id))
    }) {

        CoilImage(
            data = item.images?.firstOrNull() ?: "",
            contentDescription = "puppy image",
            fadeIn = true,
            contentScale = ContentScale.Crop,
            modifier = Modifier.width(100.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp)
                .padding(vertical = 5.dp)
        ) {
            val nameLabel = item.name ?: ""
            Text(nameLabel,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier)

            Text(item.gender?.name ?: "", style = typography.body1, maxLines = 1)
            Text(item.breed ?: "", style = typography.body1, maxLines = 1)
            Text(item.age ?: "", style = typography.body1, maxLines = 1)
        }

        LikeButton(Modifier.padding(end = 16.dp), likeStatus.value) {
            item.like = it
            likeStatus.value = it
        }
    }
}

@Composable
fun LikeButton(modifier: Modifier, isLike: Boolean, onLikeStateChange: (Boolean) -> Unit) {
    IconToggleButton(
        checked = isLike,
        onCheckedChange = onLikeStateChange,
        modifier = modifier.apply {
            size(40.dp, 40.dp)
        }
    ) {
        Icon(
            painter = painterResource(R.drawable.baseline_favorite_24),
            contentDescription = "like",
            tint = if (isLike) Color.Magenta else Color.Gray
        )
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}
