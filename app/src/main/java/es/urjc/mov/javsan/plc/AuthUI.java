package es.urjc.mov.javsan.plc;

import android.app.Activity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;


public class AuthUI {

    public static final int USER = 0;
    public static final int PASS = 1;
    public static final int SUBMIT = 2;
    public static final int ACCESS = 3;
    public static final int PERFIL = 4;
    public static final int PROFILELIST = 5;
    public static final String EMPTYPROFILES = "No profile Selected";

    private AccessList accessList;

    public static final String[] accessNames = {
        "No one Access Selected",
        "Italia",
        "Irlanda",
        "Valladolid"
    };

    AuthUI (Activity a) {
        showAuth(a);
    }

    private void showAuth(Activity a) {
        newProfile(a);
        profileFromfile(a);
        getButton(a);
    }

    private void newProfile(Activity a) {
        putSelect(a, ACCESS);

        putBox(a, "User", USER);
        putBox(a, "Password", PASS);
        putBox(a, "New Profile Name", PERFIL);
    }

    private void profileFromfile(Activity a) {
        putSelect(a, PROFILELIST);
    }

    private void getButton(Activity a) {
        putButton(a, "Monitoring", SUBMIT);
    }

    private void putSelect(Activity a, int id) {
        TableLayout tab = (TableLayout) a.findViewById(R.id.auth_table);
        TableRow row = createRow(a);

        String[] names = accessNames;
        if (isNamesFiles(id)) {
            names = getFileList(a);
        }

        row.addView(getSpinner(a, id , names));
        tab.addView(row);
    }

    private boolean isNamesFiles(int id) {
        return id == PROFILELIST;
    }

    private void putBox(Activity a , String editText, int id) {
        TableLayout tab = (TableLayout) a.findViewById(R.id.auth_table);
        TableRow row = createRow(a);

        row.addView(getEditText(a, editText, id));

        tab.addView(row);
    }

    private void putButton(Activity a, String text, int id) {
        TableLayout tab = (TableLayout) a.findViewById(R.id.auth_table);
        TableRow row = createRow(a);

        row.addView(getButton(a, text, id));

        tab.addView(row);
    }

    private Spinner getSpinner(Activity a, int id, String[] names) {
        Spinner s = new Spinner(a);
        s.setId(id);

        ArrayAdapter<String> ac = new ArrayAdapter(a, android.R.layout.simple_spinner_dropdown_item , names);
        s.setAdapter(ac);

        if (id == ACCESS) {
            setAccessList(a, s);
        }

        if (id == PROFILELIST) {
            setProfilesList(a, s, names);
        }
        return s;
    }

    private void setProfilesList(Activity a, Spinner s, String[] names) {
        s.setOnItemSelectedListener(new ProfileList(a , names));
    }

    private void setAccessList(Activity a, Spinner s) {
        accessList = new AccessList();
        s.setOnItemSelectedListener(accessList);
    }

    private EditText getEditText(Activity a, String editText, int id) {
        EditText e = new EditText(a);

        e.setHint(editText);
        e.setId(id);

        if (id == PASS) {
            // Hide text for password...
            e.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        return e;
    }

    private Button getButton(Activity a, String text, int id) {
        Button b = new Button(a);

        b.setText(text);
        b.setId(id);
        b.setOnClickListener(new AuthSubmit(a, accessList));

        return b;
    }

    private TableRow createRow(Activity a) {
        TableRow row = new TableRow(a);
        row.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT, 0 , 1.0f / 5.0f));

        row.setGravity(Gravity.CENTER);
        return row;
    }

    /*
     *  Usamos un array List por que no sabemos el tamaño real de ficheros
     *  internos de la aplicacion que son perfiles o son ficheros del sistema.
     *  El ultimo paso es pasar el array list a un array estatico para pasarselo
     *  al spinner que muestra la lista de perfiles en la pantalla de autentificacion.
     */
    private String[] getFileList(Activity a) {
        String[] files = a.fileList();
        ArrayList<String> result = new ArrayList<>();

        result.add(0, EMPTYPROFILES);
        int pos = 1;
        for (int i = 0; i < files.length; i++) {
            if (files[i].contains(ProfileFile.EXT)) {
                // Show the profile without the extension...
                result.add(pos, files[i].replace(ProfileFile.EXT, ""));
                pos++;
            }
        }

        for (String p : result) {
            Log.v("Trace:" , String.format("profile:  %s" , p));
        }
        // Cast from ArrayList to static array... in function return...
        return result.toArray(new String[result.size()]);
    }
}

