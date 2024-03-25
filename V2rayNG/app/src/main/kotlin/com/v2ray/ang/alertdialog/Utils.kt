package com.v2ray.ang.alertdialog

import android.content.Context
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes

/**
 * Returns a color associated with a particular resource ID.
 *
 * Wrapper around the deprecated [Resources.getColor][android.content.res.Resources.getColor].
 */
@Suppress("DEPRECATION")
@ColorInt
fun getColorHelper(context: Context, @ColorRes id: Int) =
    if (Build.VERSION.SDK_INT >= 23) context.getColor(id) else context.resources.getColor(id);