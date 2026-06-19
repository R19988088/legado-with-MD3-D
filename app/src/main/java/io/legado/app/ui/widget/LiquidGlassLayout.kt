package io.legado.app.ui.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.createBitmap
import androidx.core.graphics.withClip
import androidx.core.graphics.withTranslation
import androidx.core.view.doOnPreDraw
import io.legado.app.R
import io.legado.app.ui.config.themeConfig.ThemeConfig
import io.legado.app.utils.dpToPx
import io.legado.app.utils.stackBlur
import io.legado.app.utils.themeColor
import kotlin.math.roundToInt

/**
 * View-system glass panel for reader overlays.
 *
 * Compose screens use backdrop/lens directly. Reader menus are still XML/View based,
 * so this layout samples the content behind itself and draws a blurred, clipped panel.
 */
class LiquidGlassLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val panelRect = RectF()
    private val clipPath = Path()
    private val sampleRect = Rect()
    private val selfLocation = IntArray(2)
    private val rootLocation = IntArray(2)
    private val bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }
    private val highlightPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var sourceBitmap: Bitmap? = null
    private var blurredBitmap: Bitmap? = null
    private var pendingRefresh = false

    var cornerRadiusPx: Float = 28f.dpToPx()
        set(value) {
            field = value
            updateClipPath()
            invalidate()
        }

    var blurRadius: Int = ThemeConfig.bottomBarBlurRadius.coerceAtLeast(16)
        set(value) {
            field = value.coerceIn(1, 25)
            refreshGlass()
        }

    var panelAlpha: Int = ThemeConfig.bottomBarBlurAlpha.coerceIn(40, 78)
        set(value) {
            field = value.coerceIn(0, 100)
            invalidate()
        }

    var strokeAlpha: Int = 86
        set(value) {
            field = value.coerceIn(0, 255)
            invalidate()
        }

    init {
        setWillNotDraw(false)
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        clipToPadding = false
        context.withStyledAttributes(attrs, R.styleable.LiquidGlassLayout) {
            cornerRadiusPx = getDimension(
                R.styleable.LiquidGlassLayout_liquidCornerRadius,
                cornerRadiusPx
            )
            blurRadius = getInt(
                R.styleable.LiquidGlassLayout_liquidBlurRadius,
                blurRadius
            )
            panelAlpha = getInt(
                R.styleable.LiquidGlassLayout_liquidPanelAlpha,
                panelAlpha
            )
            strokeAlpha = getInt(
                R.styleable.LiquidGlassLayout_liquidStrokeAlpha,
                strokeAlpha
            )
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        refreshGlass()
    }

    override fun onDetachedFromWindow() {
        sourceBitmap?.recycle()
        blurredBitmap?.recycle()
        sourceBitmap = null
        blurredBitmap = null
        super.onDetachedFromWindow()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateClipPath()
        refreshGlass()
    }

    override fun dispatchDraw(canvas: Canvas) {
        drawGlass(canvas)
        canvas.withClip(clipPath) {
            super.dispatchDraw(this)
        }
    }

    fun refreshGlass() {
        if (pendingRefresh || width <= 0 || height <= 0) return
        pendingRefresh = true
        doOnPreDraw {
            pendingRefresh = false
            captureBackdrop()
            invalidate()
        }
    }

    private fun updateClipPath() {
        panelRect.set(0f, 0f, width.toFloat(), height.toFloat())
        clipPath.reset()
        clipPath.addRoundRect(panelRect, cornerRadiusPx, cornerRadiusPx, Path.Direction.CW)
    }

    private fun captureBackdrop() {
        val root = rootView ?: return
        if (width <= 0 || height <= 0 || root.width <= 0 || root.height <= 0) return

        visibility = INVISIBLE
        try {
            getLocationInWindow(selfLocation)
            root.getLocationInWindow(rootLocation)
            val left = selfLocation[0] - rootLocation[0]
            val top = selfLocation[1] - rootLocation[1]
            sampleRect.set(left, top, left + width, top + height)
            if (!sampleRect.intersect(0, 0, root.width, root.height)) return

            val bitmap = sourceBitmap
                ?.takeIf { it.width == sampleRect.width() && it.height == sampleRect.height() }
                ?.also { it.eraseColor(Color.TRANSPARENT) }
                ?: createBitmap(sampleRect.width(), sampleRect.height()).also {
                    sourceBitmap?.recycle()
                    sourceBitmap = it
                }

            val c = Canvas(bitmap)
            c.withTranslation(-sampleRect.left.toFloat(), -sampleRect.top.toFloat()) {
                root.draw(this)
            }

            blurredBitmap?.recycle()
            blurredBitmap = bitmap.stackBlur(blurRadius, scale = 4)
        } finally {
            visibility = VISIBLE
        }
    }

    private fun drawGlass(canvas: Canvas) {
        val baseColor = context.themeColor(com.google.android.material.R.attr.colorSurfaceContainer)
        canvas.withClip(clipPath) {
            blurredBitmap?.let {
                drawBitmap(it, 0f, 0f, bitmapPaint)
            }
            val overlayAlpha = (panelAlpha / 100f * 255).roundToInt().coerceIn(0, 255)
            fillPaint.color = setAlpha(baseColor, overlayAlpha)
            drawRoundRect(panelRect, cornerRadiusPx, cornerRadiusPx, fillPaint)

            highlightPaint.shader = LinearGradient(
                0f,
                0f,
                0f,
                height.toFloat(),
                intArrayOf(
                    Color.argb(112, 255, 255, 255),
                    Color.argb(26, 255, 255, 255),
                    Color.argb(42, 0, 0, 0)
                ),
                floatArrayOf(0f, 0.42f, 1f),
                Shader.TileMode.CLAMP
            )
            drawRoundRect(panelRect, cornerRadiusPx, cornerRadiusPx, highlightPaint)
            highlightPaint.shader = null
        }

        strokePaint.strokeWidth = 1f.dpToPx()
        strokePaint.color = Color.argb(strokeAlpha, 255, 255, 255)
        val inset = strokePaint.strokeWidth / 2f
        canvas.drawRoundRect(
            inset,
            inset,
            width - inset,
            height - inset,
            cornerRadiusPx,
            cornerRadiusPx,
            strokePaint
        )
    }

    private fun setAlpha(color: Int, alpha: Int): Int {
        return (alpha.coerceIn(0, 255) shl 24) or (color and 0x00FFFFFF)
    }
}
