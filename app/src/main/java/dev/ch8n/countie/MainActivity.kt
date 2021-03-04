package dev.ch8n.countie

import android.graphics.BlurMaskFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Typeface
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ch8n.countie.ui.theme.CountieTheme


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CountieTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = Color.White) {

                    var totalTime = 30
                    var countStarted = remember(calculation = { mutableStateOf(false) })
                    val countState = remember(calculation = { mutableStateOf(0L) })

                    val timer = object : CountDownTimer(totalTime * 1000L, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            countState.value = millisUntilFinished / 1000
                            countStarted.value = true
                        }

                        override fun onFinish() {
                            countState.value = 0
                            countStarted.value = false
                        }
                    }

                    Box(modifier = Modifier.fillMaxSize()) {

                        val percentageHeight =
                            ((totalTime - countState.value) / totalTime.toFloat())

                        Log.d(
                            "chetan",
                            "surface progress: $percentageHeight"
                        )

                        // top down progress
                        Surface(
                            color = Color.Magenta,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(
                                    percentageHeight
                                ),
                        ) {}

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = countState.value.toString(),
                                    fontSize = 120.sp,
                                    color = Color.Magenta
                                )
                            }

                            // white box
                            if (totalTime / 2L >= countState.value) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(
                                            percentageHeight
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = countState.value.toString(),
                                        fontSize = 120.sp,
                                        color = Color.White
                                    )
                                }
                            }


                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(0.5f)
                                    .padding(top = 240.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(onClick = {
                                    timer.cancel()
                                    timer.start()
                                }) {
                                    Text(text = "Start")
                                }

                                Button(onClick = {
                                    timer.cancel()
                                }) {
                                    Text(text = "Stop")
                                }
                            }


                        }


                    }

                    LazyRow(
                        content = {
                            (0..100 step 10).forEach { counter ->
                                item {

                                    Box(
                                        modifier = Modifier.padding(
                                            top = if (countStarted.value) {
                                                (0..16).random().dp
                                            } else {
                                                16.dp
                                            }
                                        )
                                    ) {
                                        Text(
                                            text = counter.toString(),
                                            fontSize = 34.sp,
                                            color = Color.Black
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(24.dp))
                                }
                            }

                        })

                }
            }
        }
    }
}


@Composable
fun drawGradientText(name: String, modifier: Modifier = Modifier) {

    val paint = Paint().asFrameworkPaint()

    val gradientShader: Shader = LinearGradientShader(
        from = Offset(0f, 0f),
        to = Offset(0f, 400f),
        listOf(Color.Blue, Color.Cyan)
    )

    Canvas(modifier.fillMaxSize()) {
        paint.apply {
            isAntiAlias = true
            textSize = 400f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            style = android.graphics.Paint.Style.FILL
            color = android.graphics.Color.parseColor("#cdcdcd")
            xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)
            maskFilter = BlurMaskFilter(30f, BlurMaskFilter.Blur.NORMAL)
        }
        drawIntoCanvas { canvas ->
            canvas.save()
            canvas.nativeCanvas.translate(2f, 5f)
            canvas.nativeCanvas.drawText(name, 0f, 400f, paint)
            canvas.restore()
            paint.shader = gradientShader
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            paint.maskFilter = null
            canvas.nativeCanvas.drawText(name, 0f, 400f, paint)
            canvas.nativeCanvas.translate(2f, 5f)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)
            paint.maskFilter = BlurMaskFilter(30f, BlurMaskFilter.Blur.NORMAL)
            canvas.nativeCanvas.drawText(name, 0f, 400f, paint)
        }
        paint.reset()
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CountieTheme {
        Greeting("Android")
    }
}