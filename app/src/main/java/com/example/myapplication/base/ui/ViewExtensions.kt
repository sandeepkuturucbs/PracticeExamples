@file:JvmName("ViewExtensions")
@file:Suppress("unused")

package com.example.myapplication.base.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.text.InputFilter
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.Px
import androidx.core.content.ContextCompat
import androidx.core.view.children

private const val TAG = "ViewExtensions"

fun View.setVisible() {
    this.visibility = View.VISIBLE
}

/**
 * Will will make the view visible if the condition is true and gone if false
 */
fun View.visibleIf(condition: Boolean) {
    if (condition) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

fun View.isVisible(): Boolean {
    return this.visibility == View.VISIBLE
}

fun View.setGone() {
    this.visibility = View.GONE
}

fun View.setInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboard() {
    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

/**
 * Sets the background color for this view.
 * @param colorResId - The identifier of the color resource.
 */
fun View.setBackgroundColorResId(@ColorRes colorResId: Int) {
    this.setBackgroundColor(ContextCompat.getColor(context, colorResId))
}

/**
 * Sets the relative padding
 * This version of the method allows using named parameters to just set one or more axes.
 */
@Suppress("MagicNumber")
fun View.updatePaddingRelative(
    @Px start: Int = paddingStart,
    @Px top: Int = paddingTop,
    @Px end: Int = paddingEnd,
    @Px bottom: Int = paddingBottom
) {
    this.setPaddingRelative(start, top, end, bottom)
}

/**
 * Sets the [entireText] to be displayed, making [linkText] clickable, which can trigger the [clickAction].
 * [linkColor] specifies the color of the [linkText].
 *
 * @param entireText String, the text to be set on this TextView; must contain [linkText].
 * @param linkText String, the text which represents a clickable link; must be contained by [entireText].
 * @param linkColor int, ColorInt to use as the link color.
 * @param [clickAction] function reference, triggered on link click.
 */
fun TextView.setTextWithClickableLink(entireText: String, linkText: String, @ColorInt linkColor: Int, clickAction: () -> Unit) {
    val index = entireText.indexOf(linkText)
    if (entireText.isEmpty() || linkText.isEmpty() || index == -1) {
        Log.e(TAG, "setTextWithClickableLink: invalid input strings, cannot set text.")
        return
    }
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) = clickAction.invoke()

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
            ds.color = linkColor
        }
    }
    val spannableString = SpannableString(entireText)
    spannableString.setSpan(clickableSpan, index, index + linkText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    this.text = spannableString
    this.movementMethod = LinkMovementMethod.getInstance()
}

/**
 * Sets the text to the given [charSequence] if not null or blank and sets visible. Else sets gone.
 */
fun TextView.setTextOrGone(charSequence: CharSequence?) {
    if (charSequence.isNullOrBlank()) {
        setGone()
    } else {
        text = charSequence
        setVisible()
    }
}

fun EditText.limitLength(maxLength: Int) {
    filters = arrayOf(InputFilter.LengthFilter(maxLength))
}

/**
 * Helper method used to color a 'shape' background. Define template once (white color) and change bg programmatically
 * instead of defining a new resource for each state.
 *
 * <pre>
 * <ImageView
 *   android:background="@drawable/gcm_bullet_fitness_age" />
 *
 *  .....
 *  <shape xmlns:android="http://schemas.android.com/apk/res/android"
 *     android:shape="oval">
 *     <solid android:color="@color/white_primary" />
 *     <size android:width="8dp" android:height="8dp" />
 *  </shape>
 *
 *  </pre>
 */
fun ImageView.setBackgroundShapeColorRes(colorRes: Int) {
    if (colorRes == -1) return
    val background: Drawable? = this.background
    if (background != null) {
        val colorInt = ContextCompat.getColor(this.context, colorRes)
        when (background) {
            is ShapeDrawable -> background.paint.color = colorInt
            is GradientDrawable -> background.setColor(colorInt)
            is ColorDrawable -> background.color = colorInt
        }
    }
}

fun View.setVisibleOrGone(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.setVisibleOrHidden(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
}

// For now need to explicitly pass in width and height for measurement specs to work.
fun View.asBitmap(width: Int, height: Int): Bitmap {
    val widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
    val heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
    measure(widthSpec, heightSpec)

    val bitmap = Bitmap.createBitmap(
        measuredWidth, measuredHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    layout(0, 0, measuredWidth, measuredHeight)
    draw(canvas)
    return bitmap
}

fun View.hideKeyboard(focusedView: View?) {
    if (focusedView != null) {
        val imm = focusedView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(focusedView.windowToken, 0)
    }
}

fun View.replaceWithView(newView: View) {
    val parent: ViewGroup = parent as? ViewGroup ?: return
    val index = parent.indexOfChild(this)
    newView.id = id
    parent.removeView(this)
    parent.addView(newView, index)
}

fun View.setEnabledRecursively(enabled: Boolean) {
    isEnabled = enabled
    if (this is ViewGroup) {
        children.forEach { child ->
            child.setEnabledRecursively(enabled)
        }
    }
}