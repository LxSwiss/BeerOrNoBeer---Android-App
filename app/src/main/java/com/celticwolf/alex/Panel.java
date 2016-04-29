package com.celticwolf.alex;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;


class Panel extends View {
    public Panel(Context context) {
        super(context);
    }

    @Override
    public void onDraw(Canvas canvas) {
        Bitmap _scratch = BitmapFactory.decodeResource(getResources(), R.drawable.corkone);
        canvas.drawBitmap(_scratch, 10, 10, null);
    }
}
