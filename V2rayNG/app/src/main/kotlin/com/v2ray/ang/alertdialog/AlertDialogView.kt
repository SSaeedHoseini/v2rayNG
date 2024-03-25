package com.v2ray.ang.alertdialog

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View


class AlertDialogView(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {
    private val rect: RectF
    private val displayMetrics: DisplayMetrics

    // defines paint and canvas
    private var drawPaint: Paint? = null

    init {
        setupPaint()
        rect = RectF()
        displayMetrics = getDisplayMetrics()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        rect.right = this.width.toFloat()
        rect.top = 0f
        rect.left = 0f
        rect.bottom = this.height.toFloat()
        canvas.drawPath(shapePath(), drawPaint!!)
    }

    // Setup paint with color and stroke styles
    private fun setupPaint() {
        drawPaint = Paint()
        drawPaint!!.color = Color.WHITE
        drawPaint!!.isAntiAlias = true
    }

    // This conversion is needed as drawing requires the actual pixels but in XML we specify in dp
    private fun dp2px(dp: Int): Int {
        return (dp * displayMetrics.density + 0.5f).toInt()
    }

    private fun shapePath(): Path {
        val path = Path()
        path.fillType = Path.FillType.WINDING
        path.addRoundRect(rect, 16f, 16f, Path.Direction.CW)
        path.addCircle(this.width / 2f, 0f, dp2px(64).toFloat(), Path.Direction.CCW)
        return path
    }

    private fun getDisplayMetrics(): DisplayMetrics {
        return Resources.getSystem().displayMetrics
    }
}