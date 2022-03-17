package org.growingstems.scouting

import androidx.appcompat.widget.AppCompatImageButton
import android.widget.ImageButton
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import java.util.ArrayList

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

    fun addImageButton(img: ImageButton?) {
        if (img == null) return
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
