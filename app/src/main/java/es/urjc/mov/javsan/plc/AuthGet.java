package es.urjc.mov.javsan.plc;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

class AuthGet extends AsyncTask<String , String , Boolean> {

    private Activity activity;

    private String access;
    private String user;
    private String pass;
    private String profile;

    AuthGet(Activity a) {
        activity = a;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        Boolean success = new Boolean(false);

        if (params.length != 4) {
            return success;
        }

        access = params[0];
        user = params[1];
        pass = params[2];
        profile = params[3];
        success = getAuthHttp(access, user, pass);

        return success;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        showAuth();
        if (success) {

            // Create newProfile in the file name...
            // Write profile...
            new ProfileFile(activity).execute(profile, access , user , pass);

            // Go to monitoring activity.
            monitoring();
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        new ShowToast(activity, values[0]).show();
    }

    private Boolean getAuthHttp(String a , String u , String p) {
        String login = u + ":" + p;
        String b64log = new String(Base64.encode(login.getBytes() , 0));

        Boolean success = new Boolean(false);

        try {

            Jsoup.connect(a).header("Authorization", "Basic " + b64log).get();
            success = true;

        } catch (SocketTimeoutException e) {

            // El dispositivo no esta conectado a la red.
            publishProgress("Time Out....");

        } catch (HttpStatusException e) {
            // Se estan metiendo mal las credenciales de usuario del servidor web.
            if (e.getStatusCode() == 401) {
                publishProgress("Error code : " + e.getStatusCode() + " possible bad Auth");
            }
            // El servidor web, no es accesible debido a problemas con el gateway de acceso a la red.
            if (e.getStatusCode() == 504) {
                publishProgress("Error code : " + e.getStatusCode() + " possible GateWay Timeout");
            }
        } catch (MalformedURLException e) {

            // El acceso a la URL del servidor web no es valida.
            publishProgress("URL Malformed...");

        } catch (IOException e) {

            // Error de entrada y salida desconocido...
            publishProgress("Networks problem...");

        } finally {

            return success;
        }
    }

    private void showAuth() {
        TableLayout tab = (TableLayout) activity.findViewById(R.id.auth_table);
        ProgressBar pb = (ProgressBar) activity.findViewById(R.id.wait_bar);

        // Hidden progress bar...
        pb.setLayoutParams(new RelativeLayout.LayoutParams(0 , 0));

        // Show Auth...
        tab.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
    }

    private void monitoring () {
        Intent activityIntent = new Intent(activity, MonitoringActivity.class);
        Bundle newActivityInfo = new Bundle();

        newActivityInfo.putString("Access" , access);
        newActivityInfo.putString("User", user);
        newActivityInfo.putString("Pass", pass);

        activityIntent.putExtras(newActivityInfo);
        activity.startActivity(activityIntent);
    }
}
