package com.blackfish.a1pedal.decorator

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade


class EventDecorator(private val resources: Resources, private val color: Int, dates: Collection<CalendarDay>) : DayViewDecorator {
    private val dates: HashSet<CalendarDay> = HashSet(dates)
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        val x = 10f
        val y = 10f
        val r = 100f
        val mPaint = Paint()
        mPaint.color = color
        val bitmap: Bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        val mCanvas = Canvas(bitmap)
        mCanvas.drawCircle(x, y, r, mPaint)

        val drawable = BitmapDrawable(resources, bitmap)
        view.setBackgroundDrawable(drawable)
    }

}