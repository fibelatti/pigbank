package com.fibelatti.pigbank.presentation.common.extensions

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.app.Activity
import android.content.Context
import android.support.annotation.LayoutRes
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputLayout
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.ViewCompat
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.fibelatti.pigbank.presentation.common.DateInputMask

private const val COMPONENT_ELEVATION = 20F

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commitAllowingStateLoss()
}

@JvmOverloads
fun ViewGroup.inflate(@LayoutRes layoutResource: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(layoutResource, this, attachToRoot)

//region User feedback
@JvmOverloads
fun View.snackbar(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, message, duration).show()
}

@JvmOverloads
fun FragmentActivity.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

@JvmOverloads
fun Activity.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun TextInputLayout.showError(errorMessage: String) {
    error = errorMessage
    editText?.run {
        requestFocus()
        showKeyboard()
    }
}

fun TextInputLayout.clearError() {
    error = null
    isErrorEnabled = false
}

fun View.requestUserFocus(activity: Activity?) {
    if (requestFocus()) {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }
}
//endregion

//region Visibility
fun View.gone() {
    visibility = View.GONE
}

fun View.isGone(): Boolean = visibility == View.GONE

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.isInvisible(): Boolean = visibility == View.INVISIBLE

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.isVisible(): Boolean = visibility == View.VISIBLE
//endregion

//region Keyboard
fun isKeyboardSubmit(actionId: Int, event: KeyEvent?): Boolean = actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE ||
    (event != null && event.action == KeyEvent.ACTION_UP && event.keyCode == KeyEvent.KEYCODE_ENTER)

fun View.showKeyboard() {
    requestFocus()
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(this, 0)
}

fun View.hideKeyboard() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(windowToken, 0)
}
//endregion

//region Components
fun EditText.textAsString(): String = text.toString()

fun EditText.textAsFloat(): Float = try {
    text.toString().toFloat()
} catch (e: Exception) {
    0F
}

fun EditText.setDateInputMask() {
    DateInputMask(this).listen()
}

fun View.setElevated() {
    ViewCompat.setTranslationZ(this, COMPONENT_ELEVATION)
}

fun LottieAnimationView.animateWithListener(onAnimationStart: () -> Unit, onAnimationEnd: () -> Unit) {
    addAnimatorListener(object : AnimatorListener {
        override fun onAnimationRepeat(animator: Animator?) {
        }

        override fun onAnimationEnd(animator: Animator?) {
            onAnimationEnd()
        }

        override fun onAnimationCancel(animator: Animator?) {
        }

        override fun onAnimationStart(animator: Animator?) {
            onAnimationStart()
        }
    })

    playAnimation()
}
//endregion
