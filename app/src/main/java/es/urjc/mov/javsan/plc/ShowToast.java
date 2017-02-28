package es.urjc.mov.javsan.plc;

import android.app.Activity;
import android.widget.Toast;

public class ShowToast {
    private Activity activity;
    private String text;

    ShowToast(Activity a, String txt) {
        activity = a;
        text = txt;
    }

    public void show() {
        int time = Toast.LENGTH_SHORT;

        Toast msg = Toast.makeText(activity, text , time);
        msg.show();
    }


}
