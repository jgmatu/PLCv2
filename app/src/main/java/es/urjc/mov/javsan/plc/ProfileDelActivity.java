package es.urjc.mov.javsan.plc;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ProfileDelActivity extends AppCompatActivity {
    private static final float SIZETEXTPROFILE = 40f;
    private static final int SIZEROW = 10;

    private TableLayout table;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_delete);
        showProfiles(fileList());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.delete_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.come_back_auth :
                startActivity(new Intent(this, AuthActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void showProfiles(String[] profiles) {
        table = (TableLayout) findViewById(R.id.table_profiles);
        TableLayout.LayoutParams lr = getLr();

        for (String p : profiles) {
            if (p.contains(ProfileFile.EXT)) {
                addProfile(lr , p.replace(ProfileFile.EXT , ""));
            }
        }
    }

    private void addProfile(TableLayout.LayoutParams lr, String p) {
        table.addView(createProfileDel(lr, p));
    }

    private TableLayout.LayoutParams getLr() {
        return new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, SIZEROW);
    }

    private TableRow createProfileDel(TableLayout.LayoutParams lr, String p) {
        TableRow row = new TableRow(this);

        row.setLayoutParams(lr);

        row.addView(nameProfile(p));
        row.addView(delProfile(p, row));

        return row;
    }

    private TextView nameProfile(String p) {
        TextView t = new TextView(this);

        t.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.MATCH_PARENT, 0.60f));
        t.setTextSize(SIZETEXTPROFILE);
        t.setText(p);

        return t;
    }

    private Button delProfile(String p, TableRow r) {
        Button b = new Button(this);

        b.setLayoutParams(new TableRow.LayoutParams(0 , TableRow.LayoutParams.MATCH_PARENT, 0.40f));
        b.setOnClickListener(new DelProfile(p , r));
        b.setTextSize(SIZETEXTPROFILE - 20);
        b.setText("DELETE");

        return b;
    }

    private void showEmptyProfiles() {
        ;
    }

    private class DelProfile implements View.OnClickListener {
        private String profile;
        private TableRow row;

        DelProfile (String p, TableRow r) {
            profile = p;
            row = r;
        }

        @Override
        public void onClick(View v) {
            deleteFile(profile + ProfileFile.EXT);
            table.removeView(row);
            new ShowToast(ProfileDelActivity.this , "Profile deleted : " + profile).show();
        }
    }
}
