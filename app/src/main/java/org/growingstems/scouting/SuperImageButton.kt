package org.growingstems.scouting

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatImageButton

class SuperImageButton : AppCompatImageButton {
    private val memberImgs: MutableList<ImageButton> = ArrayList()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun clearImageButtons() {
        memberImgs.clear()
    }

    fun addImageButton(img: ImageButton) {
        memberImgs.add(img)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        for (button in memberImgs) {
            button.dispatchTouchEvent(event)
        }
        return super.onTouchEvent(event)
    }
}
