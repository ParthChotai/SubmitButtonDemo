package com.perksy.submitbuttondemo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.perksy.submitbuttondemo.ui.theme.SubmitButtonDemoTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SubmitButtonDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.primary) {
                    AnimatedButtonView()
                }
            }
        }
    }

    @Composable
    private fun AnimatedButtonView() {

        val context = LocalContext.current

        val animatedProgress = remember {
            Animatable(
                0.001f
            )
        }

        val coroutineScope = rememberCoroutineScope()

        val getProgressOnClick: () -> Unit = {

            coroutineScope.launch {
                animatedProgress.animateTo(
                    1f,
                    animationSpec = repeatable(
                        1,
                        animation = tween(
                            durationMillis = 2000,
                            easing = FastOutLinearInEasing
                        )
                    )
                )
                if (animatedProgress.value == 1.0f) {
                    Toast.makeText(context, "I am here!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val resetProgressToZero: () -> Unit = {

            coroutineScope.launch {
                animatedProgress.animateTo(
                    0f,
                    animationSpec = repeatable(
                        1,
                        animation = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                    )
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            )
            {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    val startingPoint = Offset(0f, size.height / 2f)
                    val endingPoint =
                        Offset(size.width * animatedProgress.value, size.height / 2f)

                    drawLine(
                        strokeWidth = 50.dp.toPx(),
                        color = Color.Yellow,
                        start = startingPoint,
                        end = endingPoint
                    )
                }
                Text(
                    text = "Yes",
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    getProgressOnClick()
                                    tryAwaitRelease()
                                    resetProgressToZero()
                                }
                            )
                        }
                )
            }
        }
    }
}