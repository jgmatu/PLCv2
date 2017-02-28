package es.urjc.mov.javsan.plc;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;


public class AuthSubmit implements View.OnClickListener {
    private static final int DEFAULT = 0;

    private AccessList accessList;
    private Activity activity;

    AuthSubmit(Activity a, AccessList ac) {
        activity = a;
        accessList = ac;
    }

    @Override
    public void onClick(View v) {
        auth(accessList.getAccess(), getText(activity, AuthUI.USER), getText(activity, AuthUI.PASS),
                getText(activity, AuthUI.PERFIL));
    }

    private void auth(String a, String u, String p, String profile) {
        if (isSubmit(a, u, p , profile)) {

            showWaitBar();
            // AuthGet Request HTTP....
            new AuthGet(activity).execute(a, u , p, profile); // AuthGet data...

        } else {

            showBadAuth(a , u , p, profile);

        }
    }

    private void showBadAuth(String a, String u, String p, String profile) {
        if (invalidAccess(a)) {
            new ShowToast(activity, "Invalid Access").show();
        }
        if (u.isEmpty()) {
            new ShowToast(activity, "User not fill out").show();
        }
        if (p.isEmpty()) {
            new ShowToast(activity, "Pass not fill out").show();
        }
        if (invalidProfile(profile)) {
            new ShowToast(activity, "Invalid profile").show();
        }
    }

    private boolean isSubmit(String access, String user, String pass, String profile) {
        return !invalidAccess(access) && !user.isEmpty() && !pass.isEmpty() && !invalidProfile(profile);
    }

    private boolean invalidAccess(String a) {
        return a.equals(AuthUI.accessNames[DEFAULT]);
    }

    private boolean invalidProfile(String profile) {
        return profile.equals(AuthUI.EMPTYPROFILES);
    }

    private String getText(Activity a, int id) {
        EditText e = (EditText) a.findViewById(id);
        return e.getText().toString();
    }

    private void showWaitBar() {
        ProgressBar pb = (ProgressBar) activity.findViewById(R.id.wait_bar);
        TableLayout tab = (TableLayout) activity.findViewById(R.id.auth_table);

        // Hide Auth...
        tab.setLayoutParams(new RelativeLayout.LayoutParams(0 , 0));

        // Show Wait...
        pb.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
    }
}
