package org.growingstems.scouting

import androidx.appcompat.widget.AppCompatImageButton
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.MotionEvent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.util.AttributeSet
import androidx.core.graphics.drawable.toBitmapOrNull
import java.lang.Exception

class TransparentImageButton : AppCompatImageButton {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val d = drawable
        if (d is BitmapDrawable) {
            return if (isPixelTransparent(d, event.x.toInt(), event.y.toInt())) {
                false
            } else {
                super.onTouchEvent(event)
            }
        } else if (d is VectorDrawable) {
            return if (isPixelTransparent(d, event.x.toInt(), event.y.toInt())) {
                false
            } else {
                super.onTouchEvent(event)
            }
        }
        return super.onTouchEvent(event)
    }

    private fun isPixelTransparent(d: BitmapDrawable, x: Int, y: Int): Boolean {
        try {
            return d.bitmap.getPixel(x, y) == Color.TRANSPARENT
        } catch (e: Exception) {
            //TODO
        }
        return true
    }

    private fun isPixelTransparent(d: VectorDrawable, x: Int, y: Int): Boolean {
		try {
			d.toBitmapOrNull(width, height, null)?.let {
				return it.getPixel(x, y) == Color.TRANSPARENT;
			}
		} catch (e: Exception) {
			//TODO
		}
		return true;
    }
}
