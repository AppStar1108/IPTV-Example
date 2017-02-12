package com.redorange.motutv1.utils;

import android.text.format.Time;

public interface OnClockTickListner {
    public void OnSecondTick(Time currentTime);
    public void OnMinuteTick(Time currentTime);
}