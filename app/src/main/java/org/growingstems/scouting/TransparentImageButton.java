package org.growingstems.scouting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TransparentImageButton extends androidx.appcompat.widget.AppCompatImageButton {



	public TransparentImageButton(@NonNull Context context) {
		super(context);
	}

	public TransparentImageButton(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public TransparentImageButton(
			@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Drawable d = getDrawable();

		if (d instanceof BitmapDrawable) {
			if (isPixelTransparent((BitmapDrawable)d, (int)event.getX(), (int)event.getY())) {
				return false;
			} else {
				return super.onTouchEvent(event);
			}
		} else if (d instanceof VectorDrawable) {
			if (isPixelTransparent((VectorDrawable)d, (int)event.getX(), (int)event.getY())) {
				return false;
			} else {
				return super.onTouchEvent(event);
			}
		}
		return super.onTouchEvent(event);
	}

	private boolean isPixelTransparent(BitmapDrawable d, int x, int y) {

		int color = Color.TRANSPARENT;
		try {
			color = d.getBitmap().getPixel(x, y);
		} catch (Exception e) {
			//TODO
		}

		return color == Color.TRANSPARENT;
	}

	private boolean isPixelTransparent(VectorDrawable d, int x, int y) {

		int color = Color.TRANSPARENT;
		try {
			boolean disableCache = false;
			if (!isDrawingCacheEnabled()) {
				setDrawingCacheEnabled(true);
				disableCache = true;
			}
			color = getDrawingCache().getPixel(x, y);
			if (disableCache) {
				setDrawingCacheEnabled(false);
			}
		} catch (Exception e) {
			//TODO
		}

		return color == Color.TRANSPARENT;
	}
}
