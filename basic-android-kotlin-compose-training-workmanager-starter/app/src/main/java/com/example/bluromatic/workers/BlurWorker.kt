package com.example.bluromatic.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.bluromatic.KEY_BLUR_LEVEL
import com.example.bluromatic.KEY_IMAGE_URI
import com.example.bluromatic.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

private const val TAG = "BlurWorker"

class BlurWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
     private val DELAY_TIME_MILLIS: Long = 2000

    override suspend fun doWork(): Result {

        val appContext = applicationContext

        // ADD THIS LINE
        val resourceUri = inputData.getString(KEY_IMAGE_URI)
        val blurLevel = inputData.getInt(KEY_BLUR_LEVEL, 1)

        makeStatusNotification(
            appContext.resources.getString(R.string.blurring_image),
            appContext
        )

        return withContext(Dispatchers.IO) {

            return@withContext  try {
                // NEW code
                require(!resourceUri.isNullOrBlank()) {
                    val errorMessage =
                        applicationContext.resources.getString(R.string.invalid_input_uri)
                    Log.e(TAG, errorMessage)
                    errorMessage
                }

                /*val picture = BitmapFactory.decodeResource(
                applicationContext.resources,
                R.drawable.android_cupcake

            )*/

                // This is a utility function added to emulate slower work.
                delay(DELAY_TIME_MILLIS)


                if (TextUtils.isEmpty(resourceUri)) {
                    Log.e(TAG, "Invalid input uri")
                    throw IllegalArgumentException("Invalid input uri")
                }

                val resolver = appContext.contentResolver


                val picture = BitmapFactory.decodeStream(
                    resolver.openInputStream(Uri.parse(resourceUri))
                )


                val output = blurBitmap(picture, blurLevel)

                // Write bitmap to a temp file
                val outputUri = writeBitmapToFile(applicationContext, output)

                /*makeStatusNotification(
                    "Output is $outputUri",
                    applicationContext
                )*/

                val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())

                Result.success(outputData)
            } catch (throwable: Throwable) {
                Log.e(
                    TAG,
                    applicationContext.resources.getString(R.string.error_applying_blur),
                    throwable
                )
                Result.failure()
            }
        }

    }

}
