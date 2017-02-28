package es.urjc.mov.javsan.plc;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProfileFile extends AsyncTask<String, String, Boolean> {
    public static final String EXT = ".profile";

    // Profile data : filename, a, u, p.
    private static final int CREATEPROFILE = 4;
    // Profile name.
    private static final int READPROFILE = 1;


    private Activity activity;

    ProfileFile (Activity a) {
        activity = a;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        if (params.length == CREATEPROFILE) {
            writeProfile(params);
        }
        if (params.length == READPROFILE) {
            readProfile(params);
        }
        return new Boolean(true);
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

        if (values.length != 4) {
            return;
        }

        Spinner a = (Spinner) activity.findViewById(AuthUI.ACCESS);
        EditText u =  (EditText) activity.findViewById(AuthUI.USER);
        EditText p = (EditText) activity.findViewById(AuthUI.PASS);
        EditText profile = (EditText) activity.findViewById(AuthUI.PERFIL);

        for (String v : values) {
            Log.v("Trace:", String.format("value : %s", v));
        }

        a.setSelection(getIndex(values[0]));
        u.setText(values[1]);
        p.setText(values[2]);
        profile.setText(values[3].replace(EXT, ""));
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);
        // Result of create or read file profile....
    }

    private void writeProfile(String... params) {
        String filename = params[0] + EXT;

        String access = params[1];
        String user = params[2];
        String pass = params[3];

        createProfile(filename, access , user, pass);
    }

    private void readProfile(String... params) {
        String filename = params[0] + EXT;
        FileInputStream fis = null;

        try {
            // Open file profile to read...
            fis = activity.openFileInput(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            // Read lines a , u , p from file profile...
            String access = reader.readLine();
            String user = reader.readLine();
            String pass = reader.readLine();

            publishProgress(access, user, pass, filename);

        } catch (FileNotFoundException e) {

            new ShowToast(activity , "Profile not found!!!").show();

        } catch (IOException e) {

            new ShowToast(activity , "Error reading profile file!!!").show();

        } finally {

            close(fis);
        }
    }

    private void createProfile(String profile, String access ,String user, String pass) {
        FileOutputStream fos = null;

        try {

            fos = activity.openFileOutput(profile, Context.MODE_PRIVATE);

            fos.write((access + "\n").getBytes());
            fos.write((user + "\n").getBytes());
            fos.write((pass + "\n").getBytes());

        } catch (FileNotFoundException e) {

            new ShowToast(activity , "Profile not found!!!").show();

        } catch (IOException e) {

            new ShowToast(activity , "Error writing profile file!!!").show();

        } finally {

            close(fos);
        }
    }

    private void close (FileInputStream fis) {
        try {
            if (fis != null) {
                fis.close();
            }
        } catch (IOException e) {
            // Error closing file...
        }
    }

    private void close(FileOutputStream fos) {
        try {
            if (fos != null) {
                fos.close();
            }
        }catch (IOException e) {
            new ShowToast(activity, e.toString()).show();
        }
    }

    private int getIndex(String name) {
        int idx = 0;

        // Log.v(AuthActivity.TAG, name); Get index from Access....
        for (int i = 0 ; i < AccessList.accessUris.length ; i++) {
            if (name.equals(AccessList.accessUris[i])) {
                idx = i;
            }
        }
        return idx;
    }
}
