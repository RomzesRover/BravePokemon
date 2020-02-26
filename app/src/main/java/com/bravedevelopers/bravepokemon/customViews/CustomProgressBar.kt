package com.bravedevelopers.bravepokemon.customViews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.bravedevelopers.bravepokemon.R
import kotlin.math.sqrt


class CustomProgressBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attrs, defStyleAttr) {

    private val pathTriangle = Path()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val matrixTriangle = Matrix()

    private val framesPerSecond = 60L
    private var startTime = 0L
    private val rotateAnglePerOneSec = 135f

    init {
        startTime = System.currentTimeMillis()

        var triangleColor = Color.GREEN

        attrs?.let {
            val typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomProgressBar)
            triangleColor = typedArray.getColor(R.styleable.CustomProgressBar_triangle_color, triangleColor)
            typedArray.recycle()
        }

        //set paint methods
        paint.color = triangleColor
        paint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) return

        val elapsedTime = System.currentTimeMillis() - startTime

        //Create triangle
        pathTriangle.reset()
        val triangleSide = width/1.5f
        val h = triangleSide * (sqrt(3f)/2f)
        pathTriangle.moveTo(0f, -h/2)
        pathTriangle.lineTo(-triangleSide/2f, h/2f)
        pathTriangle.lineTo(triangleSide/2f, h/2f)
        pathTriangle.lineTo(0f, -h/2f)
        pathTriangle.close()

        //Set matrix to move object
        matrixTriangle.reset()
        matrixTriangle.setTranslate(width/2f, height/2f)

        //apply matrix to triangle
        pathTriangle.transform(matrixTriangle)

        //Set matrix to rotate object
        matrixTriangle.reset()
        matrixTriangle.setRotate((rotateAnglePerOneSec*elapsedTime/1000), width/2f, height/2f)

        //apply matrix to triangle
        pathTriangle.transform(matrixTriangle)


        //draw triangle
        canvas.drawPath(pathTriangle, paint)

        //call next draw method
        postInvalidateDelayed( 1000 / framesPerSecond);
    }

    //make view squared only!
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (measuredWidth > measuredHeight)
            setMeasuredDimension(measuredWidth, measuredWidth)
        else
            setMeasuredDimension(measuredHeight, measuredHeight)
    }
}