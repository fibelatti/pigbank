package com.fibelatti.pigbank.presentation.common.extensions

import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnShowListener
import android.support.v7.app.AlertDialog
import android.view.View

fun Context.alert(dialogTitle: String? = null, dialogMessage: String? = null): AlertDialog {
    return AlertDialog.Builder(this).apply {
        if (dialogTitle != null) setTitle(dialogTitle)
        if (dialogMessage != null) setMessage(dialogMessage)
    }.create()
}

fun AlertDialog.view(view: View): AlertDialog {
    setView(view)
    return this
}

fun AlertDialog.positiveButton(buttonText: String, onClickListener: DialogInterface.OnClickListener? = null): AlertDialog {
    setButton(AlertDialog.BUTTON_POSITIVE, buttonText, onClickListener)
    return this
}

fun AlertDialog.updatePositiveButton(buttonColor: Int? = null, onClickListener: View.OnClickListener? = null): AlertDialog {
    getButton(DialogInterface.BUTTON_POSITIVE)?.apply {
        if (buttonColor != null) setTextColor(buttonColor)
        if (onClickListener != null) setOnClickListener(onClickListener)
    }
    return this
}

fun AlertDialog.negativeButton(buttonText: String, onClickListener: DialogInterface.OnClickListener? = null): AlertDialog {
    setButton(AlertDialog.BUTTON_NEGATIVE, buttonText, onClickListener)
    return this
}

fun AlertDialog.updateNegativeButton(buttonColor: Int? = null, onClickListener: View.OnClickListener? = null): AlertDialog {
    getButton(DialogInterface.BUTTON_NEGATIVE)?.apply {
        if (buttonColor != null) setTextColor(buttonColor)
        if (onClickListener != null) setOnClickListener(onClickListener)
    }
    return this
}

fun AlertDialog.showListener(listener: OnShowListener): AlertDialog {
    setOnShowListener(listener)
    return this
}

fun AlertDialog.notCancelable(): AlertDialog {
    setCancelable(false)
    setCanceledOnTouchOutside(false)
    return this
}
