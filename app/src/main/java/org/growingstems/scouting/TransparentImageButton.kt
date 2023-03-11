package org.growingstems.scouting

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.graphics.drawable.toBitmapOrNull

class TransparentImageButton : AppCompatImageButton {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val d = drawable
        return if (isPixelTransparent(d, event.x.toInt(), event.y.toInt())) {
            false
        } else {
            super.onTouchEvent(event)
        }
    }

    private fun isPixelTransparent(d: Drawable, x: Int, y: Int): Boolean {
        try {
            d.toBitmapOrNull(width, height, null)?.let {
                return it.getPixel(x, y) == Color.TRANSPARENT
            }
        } catch (e: Exception) {
            //TODO
        }
        return true
    }
}
