package org.growingstems.scouting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SuperImageButton extends androidx.appcompat.widget.AppCompatImageButton {

	private final List<ImageButton> memberImgs = new ArrayList<>();

	public SuperImageButton(@NonNull Context context) {
		super(context);
	}

	public SuperImageButton(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public SuperImageButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public void clearImageButtons() {
		memberImgs.clear();
	}

	public void addImageButton(ImageButton img) {
		if (img == null)
			return;
		memberImgs.add(img);
	}


	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		for (ImageButton button : memberImgs) {
			button.dispatchTouchEvent(event);
		}
		return super.onTouchEvent(event);
	}

}
