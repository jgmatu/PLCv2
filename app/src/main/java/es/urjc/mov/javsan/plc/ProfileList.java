package es.urjc.mov.javsan.plc;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by javi on 17/02/17.
 */

public class ProfileList implements AdapterView.OnItemSelectedListener {
    private Activity activity;
    private String[] profiles;
    private String profile;

    private boolean isFirst;


    ProfileList(Activity a, String[] ns) {
        activity = a;
        profiles = ns;
        profile = profiles[0];
        isFirst = true;
    }

    @Override
    public void onItemSelected(AdapterView<?> per, View view, int idx, long id) {
        if (isFirst) {
            isFirst = false;
            return;
        }

        profile = per.getItemAtPosition(idx).toString();
        new ProfileFile(activity).execute(profile); // Read profile...

        // new ShowToast(activity , profile).show(); // Verbose...
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        ;
    }
}

