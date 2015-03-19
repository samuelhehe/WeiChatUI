package com.samuel.imooc_weichat;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class ChangeColorIconTextView extends View {

	private int tv_Color = 0xFF45C01A;

	private Paint iconPaint;

	private Canvas mCanvas;

	private Paint textPaint;

	private Bitmap icon_Bitmap;

	private Bitmap mBitmap;

	
	
	private float alpha;

	private String text = "weichat";

	private int text_Size = (int) TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());

	private Rect textBound;

	private Rect iconRect;

	public ChangeColorIconTextView(Context context) {
		super(context);
	}

	public ChangeColorIconTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * 获取自定义属性的值
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 */
	public ChangeColorIconTextView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray array = context.obtainStyledAttributes(attrs,
				R.styleable.ChangeColorIconTextView);
		int indexCount = array.getIndexCount();
		for (int i = 0; i < indexCount; i++) {
			int attr = array.getIndex(i);
			switch (attr) {
			// // color
			case R.styleable.ChangeColorIconTextView_color:
				tv_Color = array.getColor(attr, 0xFF45C01A);
				break;
			// / icon
			case R.styleable.ChangeColorIconTextView_icon:
				BitmapDrawable drawable = (BitmapDrawable) array
						.getDrawable(attr);
				icon_Bitmap = drawable.getBitmap();
				break;
			// / text
			case R.styleable.ChangeColorIconTextView_text:
				text = array.getString(attr);
				break;
			// / text_size
			case R.styleable.ChangeColorIconTextView_text_size:
				text_Size = (int) array.getDimension(attr, TypedValue
						.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12,
								getResources().getDisplayMetrics()));
				break;
			default:
				break;
			}
		}
		array.recycle();
		textBound = new Rect();
		textPaint = new Paint();
		textPaint.setTextSize(text_Size);
		textPaint.setColor(0Xff555555);
		textPaint.getTextBounds(text, 0, text.length(), textBound);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int iconwidth = Math.min(getMeasuredWidth() - getPaddingLeft()
				- getPaddingRight(), getMeasuredHeight() - getPaddingTop()
				- getPaddingBottom() - textBound.height());
		int left = getMeasuredWidth() / 2 - iconwidth / 2;
		int top = getMeasuredHeight() / 2 - (textBound.height() + iconwidth)
				/ 2;
		iconRect = new Rect(left, top, left + iconwidth, top + iconwidth);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(icon_Bitmap, null, iconRect, null);
		int nalpha = (int) Math.ceil(255 * alpha);
		setupTargetBitmap(nalpha);
		drawSourceText(canvas,nalpha);
		drawTargetText(canvas,nalpha);
		canvas.drawBitmap(mBitmap, 0, 0, null);
	}
	
	/**
	 * 在内存中绘制可变色的Icon
	 */
	private void setupTargetBitmap(int alpha) {
		mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		iconPaint = new Paint();
		iconPaint.setColor(tv_Color);
		iconPaint.setAntiAlias(true);
		iconPaint.setDither(true);
		iconPaint.setAlpha(alpha);
		mCanvas.drawRect(iconRect, iconPaint);
		iconPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		iconPaint.setAlpha(255);
		mCanvas.drawBitmap(icon_Bitmap, null, iconRect, iconPaint);
	}

	
	/**
	 * 绘制变色的文本
	 * 
	 * @param canvas
	 * @param alpha
	 */
	private void drawTargetText(Canvas canvas, int alpha) {
		textPaint.setColor(tv_Color);
		textPaint.setAlpha(alpha);
		int x = getMeasuredWidth() / 2 - textBound.width() / 2;
		int y =iconRect.bottom + textBound.height();
		canvas.drawText(text, x, y, textPaint);
	}

	/**
	 * 绘制原文本
	 * 
	 * @param canvas
	 * @param alpha
	 */
	private void drawSourceText(Canvas canvas, int alpha) {
		textPaint.setColor(0xff333333);
		textPaint.setAlpha(255 - alpha);
		int x = getMeasuredWidth() / 2 - textBound.width() / 2;
		int y = iconRect.bottom + textBound.height();
		canvas.drawText(text, x, y, textPaint);
	}
	
	

	private static final String INSTANCE_STATUS = "instance_status";
	private static final String STATUS_ALPHA = "status_alpha";

	@Override
	protected Parcelable onSaveInstanceState() {
		Bundle bundle = new Bundle();
		bundle.putParcelable(INSTANCE_STATUS, super.onSaveInstanceState());
		bundle.putFloat(STATUS_ALPHA, alpha);
		return bundle;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state instanceof Bundle) {
			Bundle bundle = (Bundle) state;
			alpha = bundle.getFloat(STATUS_ALPHA);
			super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATUS));
			return;
		}
		super.onRestoreInstanceState(state);
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
		invalidateView();
	}

	private void invalidateView() {
		if (Looper.getMainLooper() == Looper.myLooper()) {
			invalidate();
		} else {
			postInvalidate();
		}
	}

}
