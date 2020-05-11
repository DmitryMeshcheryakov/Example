package com.android.example.core.utils

import android.content.Context
import android.graphics.*
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import kotlin.math.min
import kotlin.math.tan

object BitmapUtils {

    fun getBitmapFromView(view: View?): Bitmap? {
        return runCatching {
            val bitmap = Bitmap.createBitmap(view?.width!!, view.height, Bitmap.Config.ARGB_8888)
            val c = Canvas(bitmap)
            view?.draw(c)
            return bitmap
        }.getOrNull()
    }

    fun pairBitmap(
        first: Bitmap?,
        second: Bitmap?,
        angle: Int,
        strokeWidthPx: Float,
        @ColorInt strokeColor: Int
    ): Bitmap {

        val height = runCatching { min(first?.height!!, second?.height!!) }.getOrNull() ?: first?.height ?: second?.height ?: 50
        val width = runCatching { min(first?.width!!, second?.width!!) }.getOrNull() ?: first?.width ?: second?.width ?: 50

        val output = Bitmap.createBitmap(
            width,
            height,
            Bitmap.Config.ARGB_8888
        )

        val rect = Rect(0, 0, width, height)
        val paint = Paint()
        val separatorPaint = Paint().apply {
            color = strokeColor
            strokeWidth = strokeWidthPx
        }
        val canvas = Canvas(output)

        second?.let { bmp ->
            val crop = cropBitmapWithRatio(bmp, width, height)
            canvas.drawBitmap(crop, null, rect, paint)
        }

        first?.let { bmp ->
            val crop = cropBitmapWithRatio(bmp, width, height)
            val path = createPathForPair(crop.width, crop.height, angle)
            val resultBitmap = sliceBitmap(crop, path)

            canvas.drawBitmap((resultBitmap), null, rect, paint)

            val diff = (height * tan(Math.toRadians(angle.toDouble() + 90))).toFloat() / 2f
            canvas.drawLine(
                (width / 2f) - diff,
                0f,
                (width / 2f) + diff,
                height.toFloat(),
                separatorPaint
            )
        }

        return output
    }

    fun circleBitmap(bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)

        val color = Color.BLACK
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawCircle(bitmap.width / 2f, bitmap.height / 2f, bitmap.width / 2f, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }

    fun sliceBitmap(src: Bitmap, path: Path): Bitmap {
        val output = Bitmap.createBitmap(
            src.width,
            src.height,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(output)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.BLACK

        canvas.drawPath(path, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(src, 0f, 0f, paint)

        return output
    }


    fun cropBitmapWithRatio(bitmap: Bitmap, xRatio: Int, yRatio: Int = xRatio): Bitmap {
        val ratio = yRatio.toDouble() / xRatio.toDouble()

        val targetWidthH = (bitmap.height / ratio).toInt()
        val targetHeightW = (bitmap.width * ratio).toInt()

        val targetWidth = min(bitmap.width, targetWidthH)
        val targetHeight = min(bitmap.height, targetHeightW)

        val yOffset = (((bitmap.height.toDouble() - targetHeight) / 2) - 0.1).toInt()
        val xOffset = (((bitmap.width.toDouble() - targetWidth) / 2) - 0.1).toInt()

        val x = if (xOffset <= 0) 0 else xOffset
        val y = if (yOffset <= 0) 0 else yOffset

        return Bitmap.createBitmap(bitmap, x, y, targetWidth, targetHeight)
    }

    fun getBitmapSafe(context: Context?, @DrawableRes drawableId: Int): Bitmap {
        return context?.let { getBitmapFromDrawable(it, drawableId) }
            ?: context?.let { getBitmapFromVectorDrawable(it, drawableId) }
            ?: Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888)
    }

    fun getBitmapFromDrawable(context: Context, @DrawableRes drawableId: Int): Bitmap? {
        return runCatching {
            BitmapFactory.decodeResource(
                context.resources,
                drawableId
            )
        }.getOrNull()
    }

    fun getBitmapFromVectorDrawable(context: Context, @DrawableRes drawableId: Int): Bitmap? {
        return runCatching {
            val drawable = ContextCompat.getDrawable(context, drawableId)
            val bitmap = Bitmap.createBitmap(
                drawable!!.intrinsicWidth,
                drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)

            bitmap
        }.getOrNull()
    }

    private fun createPathForPair(width: Int, height: Int, angle: Int): Path {
        val diff = (height * tan(Math.toRadians(angle.toDouble() + 90))).toFloat() / 2f
        val maxX = width.toFloat()
        val maxY = height.toFloat()

        val path = Path()
        path.moveTo((maxX / 2f) - diff, 0f)
        path.lineTo((maxX / 2f) + diff, maxY)
        path.lineTo(0f, maxY)
        path.lineTo(0f, 0f)
        path.lineTo((maxX / 2f) - diff, 0f)
        path.close()
        return path
    }

}