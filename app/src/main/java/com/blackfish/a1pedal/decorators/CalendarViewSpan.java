package com.blackfish.a1pedal.decorators;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

import com.blackfish.a1pedal.R;

public class CalendarViewSpan implements LineBackgroundSpan {

    String text;
    public CalendarViewSpan(String text){
        this.text = text;
    }

    @Override
    public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom,
                               CharSequence text, int start, int end, int lnum) {

        text = this.text;

        c.drawText(String.valueOf(text), ((left+right)/2)-10, bottom+40, p  );
    }}
