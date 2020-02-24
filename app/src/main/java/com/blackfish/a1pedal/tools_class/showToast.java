package com.blackfish.a1pedal.tools_class;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blackfish.a1pedal.R;

public class showToast {
    public static void showToast(String text, boolean ok, Context k) {
        Toast toast3 = Toast.makeText(k,text, Toast.LENGTH_LONG);
        toast3.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastContainer = (LinearLayout) toast3.getView();
        // toastContainer.setBackgroundColor((Color.parseColor("#f2f2f2")));
        toastContainer.setBackgroundResource(R.drawable.toast_border);
        toastContainer.setPadding(50,50,50,50);
        TextView v = (TextView) toast3.getView().findViewById(android.R.id.message);
        v.setTextColor(Color.parseColor("#000000"));
        v.setTextSize(17);
        v.setPadding(0,30,0,0);
        ImageView Icon = new ImageView(k);

        Icon.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,100));

        if (ok){
            Icon.setImageResource(R.drawable.ic_checked);
        }
        else  {
            Icon.setImageResource(R.drawable.ic_cancel);
        }
        toastContainer.addView(Icon, 0);
        toast3.show();
    }
}
