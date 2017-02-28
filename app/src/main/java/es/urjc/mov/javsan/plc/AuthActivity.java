package es.urjc.mov.javsan.plc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class AuthActivity extends AppCompatActivity {

    public static final String TAG = "PLC: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        new AuthUI(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // LLamar al editor.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.auth_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item Selection.
        switch (item.getItemId()) {
            case R.id.delete_all_profiles:
                clearProfiles();
                refresh();
                return true;
            case R.id.delete_profile:
                selectProfile();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectProfile() {
        startActivity(new Intent(this, ProfileDelActivity.class));
    }

    private void refresh() {
        startActivity(new Intent(this, AuthActivity.class));
    }

    private void deleteProfile(String name) {
        if (deleteFile(name)) {
            new ShowToast(this, "Profile deleted : " + name).show();
        } else {
            new ShowToast(this, "Profile not deleted : " + name).show();
        }
    }

    private void clearProfiles() {
        String[] files = fileList();

        for (String f : files) {
            if (f.contains(ProfileFile.EXT)) {
                deleteProfile(f);
            }
        }
    }
}