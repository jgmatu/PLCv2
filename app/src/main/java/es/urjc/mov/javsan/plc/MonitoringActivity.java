package es.urjc.mov.javsan.plc;

import android.content.Intent;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.concurrent.Executor;


public class MonitoringActivity extends AppCompatActivity {

    private static final String INITMONITORING = "Wait...";
    private static final float SIZETEXT = 20f;
    private static final int KEYVALUE = 2;
    private static final int SIZETAGS = TagsData.TAGS.length * KEYVALUE;

    private String[] tagsSaved;
    private MonitoringData monitoring;
    private MonitoringControl control;
    private Bundle state;
    private boolean resume;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.monitoring_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.come_back_auth:

                exitMonitoring();
                startActivity(new Intent(this, AuthActivity.class));
                // Change activity, change context.
                return true;

            case R.id.show_data:

                showDataMonitoring();
                return true;

            case R.id.show_alarms:

                showAlarmsMonitoring();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);

        Intent intent = getIntent();
        state = intent.getExtras();

        // Set Saved State if the monitoring was running...
        setSaveState(savedInstanceState);

        // Draw the UI to show the monitoring...
        createUIMonitoring();

        // Start task refresh monitoring...
        startMonitoring();
        resume = false;
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        for (int i = 0; i < SIZETAGS; i++) {
            TextView t = (TextView) findViewById(i);
            state.putString(Integer.toString(i) , t.getText().toString());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        exitMonitoring();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (resume) {
            startMonitoring();
        }
        resume = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        exitMonitoring();
    }

    private void startMonitoring() {
        // New control to this activity monitoring...
        control = new MonitoringControl();

        // Monitoring : Launch Thread to monitoring the PLC from the app...
        monitoring = new MonitoringData(this , control);
        monitoring.execute(state.getString("Access"), state.getString("User") , state.getString("Pass"));
    }

    private void setSaveState(Bundle state) {
        initState();

        if (!isFirstActivity(state)) {
            putState(state);
        }
    }

    private void exitMonitoring() {
        try {
            if (control != null) {
                control.exit();
                control.close();
            }
        } catch (NetworkOnMainThreadException e) {
            // Ignore Exception NetworkOnMainThreadException
            // Be carefull we cant do nothing of
            // network in main thread we are closing
            // the connection of critical way...
        }
    }

    private void initState () {
        tagsSaved = new String[SIZETAGS];

        for (int i = 0 ; i < SIZETAGS; i++) {
            tagsSaved[i] = INITMONITORING;
        }
    }

    private void putState(Bundle state) {
        for (int i = 0 ; i < SIZETAGS ; i++) {
            tagsSaved[i] = state.getString(Integer.toString(i));
        }
    }

    private boolean isFirstActivity (Bundle msg) {
        return msg == null;
    }

    private void createUIMonitoring() {
        TableLayout table = (TableLayout) findViewById(R.id.table_monitoring);

        TableLayout.LayoutParams rows = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT, 0 , 1.0f / (float) TagsData.TAGS.length);

        TableRow.LayoutParams tagDesign = new TableRow.LayoutParams(
                0, TableRow.LayoutParams.MATCH_PARENT, 1.0f / (float) KEYVALUE);

        int id = 0;
        for (int i = 0; i < TagsData.TAGS.length; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(rows);

            for (int j = 0 ; j < KEYVALUE ; j++) {
                row.addView(getTag(tagDesign, id));
                row.setGravity(Gravity.CENTER);
                id++;
            }
            table.addView(row);
        }
    }

    private void showAlarmsMonitoring () {

    }

    private void showDataMonitoring () {

    }

    private TextView getTag (TableRow.LayoutParams tagDesign, int id) {
        TextView tag = new TextView(this);

        tag.setLayoutParams(tagDesign);
        tag.setGravity(Gravity.CENTER);
        setText(tag, id);
        tag.setId(id);

        return tag;
    }

    private void setText(TextView t, int id) {
        t.setText(tagsSaved[id]);
        t.setTextSize(SIZETEXT);
    }
}
