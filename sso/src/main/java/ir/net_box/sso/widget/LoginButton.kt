package ir.net_box.sso.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import ir.net_box.sso.R
import ir.net_box.sso.databinding.LoginButtonBinding

/**
 * Custom Login Button that extends FrameLayout.
 * This button allows :
 *  - customization of layout size
 *  - customization of icon size
 *  - customization of font and text size
 *
 * @property context The context in which the button operates
 * @property attributeSet The set of attributes from XML
 * @property defStyleAttr The default style attribute
 *
 * @param layout_width Setting the width of the button
 * @param layout_height Setting the height of the button
 * @param icon_custom_width Setting the width of the icon
 * @param icon_custom_height Setting the height of the icon
 * @param textSize Setting the text size of the text
 * @param font Setting the font of the text
 */
class LoginButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet?,
    defStyleAttr: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttr) {

    private var binding: LoginButtonBinding = LoginButtonBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    init {

        val attr = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.LoginButton,
            defStyleAttr,
            0
        )

        applyCustomLayoutParams(
            attr,
            binding.netBoxButton,
            R.styleable.LoginButton_android_layout_width,
            R.styleable.LoginButton_android_layout_height,
        )
        applyCustomLayoutParams(
            attr,
            binding.netboxButtonImage,
            R.styleable.LoginButton_icon_custom_width,
            R.styleable.LoginButton_icon_custom_height,
        )
        applyCustomFont(attr, binding.netboxButtonText)
        applyCustomTextSize(attr, binding.netboxButtonText)
        attr.recycle()
    }

    /**
     * Applies custom layout parameters based on attributes.
     *
     * @param attr The TypedArray containing attribute values
     * @param view The view to which layout parameters are applied
     * @param widthStyle The attribute defining custom width
     * @param heightStyle The attribute defining custom height
     */
    private fun applyCustomLayoutParams(
        attr: TypedArray,
        view: View,
        widthStyle: Int,
        heightStyle: Int
    ) {
        val width = attr.getLayoutDimension(
            widthStyle,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val height = attr.getLayoutDimension(
            heightStyle,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val layoutParams = view.layoutParams
        layoutParams.width = when (width) {
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.FILL_PARENT ->
                ViewGroup.LayoutParams.MATCH_PARENT
            else -> width
        }

        layoutParams.height = when (height) {
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.FILL_PARENT ->
                ViewGroup.LayoutParams.MATCH_PARENT
            else -> height
        }

        view.layoutParams = layoutParams
    }

    /**
     * Applies a custom font to the button.
     *
     * @param attr The TypedArray containing attribute values
     * @param view The Button to which the font is applied
     */
    private fun applyCustomFont(attr: TypedArray, view: AppCompatTextView) {
        val fontPath = attr.getString(R.styleable.LoginButton_android_font)
        fontPath?.let {
            val typeface = Typeface.createFromAsset(context.assets, it)
            view.typeface = typeface
        }
    }

    /**
     * Applies a custom text size to the button.
     *
     * @param attr The TypedArray containing attribute values
     * @param view The Button to which the text size is applied
     */
    private fun applyCustomTextSize(attr: TypedArray, view: AppCompatTextView) {
        val fontSize = attr.getDimensionPixelSize(
            R.styleable.LoginButton_android_textSize,
            16
        )
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize.toFloat())
    }
}