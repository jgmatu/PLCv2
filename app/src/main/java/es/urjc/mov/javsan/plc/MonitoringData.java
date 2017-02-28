package es.urjc.mov.javsan.plc;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.HttpStatusException;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class MonitoringData extends AsyncTask<String , String , Boolean> {
    private static final int GETAUTH = 3; // A , U , P.
    private static final int KEYVALUE = 2;  // Tag Name, Tag Value.
    private static final String USER_AGENT = "Mozilla/5.0";

    private TagsData data;
    private Activity activity;
    private MonitoringControl control;


    MonitoringData(Activity a, MonitoringControl c) {
        activity = a;
        control = c;
        data = null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        if (params.length != GETAUTH) {
            return false;
        }
        String a = params[0], u = params[1], p = params[2];
        boolean success = false;

        try {

            // Refresh TagsData while not is cancelled the task...
            while (!control.isExit()) {
                success = datamonitoring(a, u, p);
                if (success) {
                    publishProgress();
                }
            }

        } catch (InterruptedException e) {

            // The thread not must be interrumped, only notify...

        }
        Log.v("TRACE:" , "Getting Data finalized...");
        return success;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        monitoring();
    }

    private boolean datamonitoring(String a , String u , String p) {
        Boolean success = new Boolean(false);

        try {

            control.setInput(openconn(a , u , p));
            Document doc = DataUtil.load(control.getInput(), "UTF-8", a);

            data = new TagsData(getTagsName(doc) , getTagsValue(doc));
            success = true;

        } catch (SocketTimeoutException e) {

            // El dispositivo no esta conectado a la red.
            publishProgress("Time out...");

        } catch (HttpStatusException e) {

            // Se estan metiendo mal las credenciales de usuario del servidor web.
            publishProgress("Bad reply " + e.getStatusCode() + " possible bad auth");

        } catch (MalformedURLException e) {

            // El acceso a la URL del servidor web no es valida.
            publishProgress("Bad URL malformed...");

        } catch (IOException e) {

            // Error de entrada y salida desconocido...
            publishProgress("Network conection error...");

        } finally {

            control.close();
            return success;
        }
    }

    private InputStream openconn(String a, String u , String p) throws IOException {
        URL urlconn = new URL(a);
        String login = u + ":" + p;
        String b64log = new String(Base64.encode(login.getBytes() , 0));
        HttpURLConnection con = (HttpURLConnection) urlconn.openConnection();

        // Optional default is GET
        con.setRequestMethod("GET");

        con.setRequestProperty("Authorization", "Basic " + b64log);
        con.setRequestProperty("User-Agent", USER_AGENT);

        return con.getInputStream();
    }

    private void monitoring() {
        if (data == null) {
            return;
        }

        HashMap<String , String> tags = data.getTestUI();
        int id = 0;

        for (Map.Entry<String, String> entry : tags.entrySet()) {
                changeTextTags(entry.getKey() , entry.getValue() , id);
                id += KEYVALUE;
        }
    }

    private void changeTextTags(String name , String value, int id) {
        for (int i = 0 ; i < KEYVALUE ; i++) {
            TextView e = (TextView) activity.findViewById(id + i);
            if (i % KEYVALUE == 0) {
                e.setText(name);
            } else {
                e.setText(value);
            }
        }
    }

    private Elements getTagsName(Document doc) {
        return doc.getElementsByClass("tn");
    }
    private Elements getTagsValue(Document doc) {
        return doc.getElementsByClass("value");
    }
}
