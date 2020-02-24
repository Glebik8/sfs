package com.blackfish.a1pedal;

import android.app.Application;
import com.droidnet.DroidNet;

public class Onepedal extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DroidNet.init(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        DroidNet.getInstance().removeAllInternetConnectivityChangeListeners();
    }
}
