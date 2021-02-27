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
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.model.Puppy
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.theme.typography
import dev.chrisbanes.accompanist.coil.CoilImage

class DetailActivity : AppCompatActivity() {

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
                PuppyDetail(puppy = puppy ?: return@MyTheme) {
                    onBackPressed()
                }
            }
        }
    }
}

@Composable
fun PuppyDetail(puppy: Puppy, onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            val likeStatus = remember { mutableStateOf(puppy.like) }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                        contentDescription = "back"
                    )
                }

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
        }
    ) {
        MainContent(puppy = puppy)
    }
}

@Composable
fun MainContent(puppy: Puppy) {
    val context = LocalContext.current
    Column {
        puppy.images?.firstOrNull()?.let {
            CoilImage(
                data = it,
                contentDescription = "puppy image",
                fadeIn = true,
                contentScale = ContentScale.Crop,
                alignment = Alignment.TopCenter,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
        }

        Text(
            text = puppy.name ?: "",
            style = typography.h6,
            modifier = Modifier.padding(start = 20.dp, top = 16.dp)
        )
        Text(
            text = puppy.breed ?: "",
            style = typography.body1,
            modifier = Modifier.padding(start = 20.dp, top = 5.dp)
        )

        Row(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 5.dp)
        ) {

            Card {
                Text(
                    text = puppy.gender?.name ?: "",
                    style = typography.body1,
                    modifier = Modifier.padding(all = 5.dp)
                )
            }
            Card(modifier = Modifier.padding(start = 10.dp)) {
                Text(
                    text = puppy.age ?: "",
                    style = typography.body1,
                    modifier = Modifier.padding(all = 5.dp)
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 5.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_place_24),
                contentDescription = "place",
                modifier = Modifier.size(16.dp, 16.dp)
            )
            Text(
                text = puppy.address ?: "",
                style = typography.body1,
                modifier = Modifier.padding(start = 5.dp)
            )
        }

        Text(
            text = puppy.intro ?: "",
            style = typography.body1,
            modifier = Modifier.padding(start = 20.dp, top = 16.dp)
        )

        Button(
            onClick = { Toast.makeText(context, "adopt a puppy", Toast.LENGTH_SHORT).show() },
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 20.dp)
                .fillMaxWidth()
                .height(38.dp)
        ) {
            Text(text = "Adopt me!", style = typography.subtitle1, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun DetailPreview() {
    MyTheme {
        MyApp()
    }
}
