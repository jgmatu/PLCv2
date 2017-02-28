package es.urjc.mov.javsan.plc;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.util.HashMap;
import java.util.Map;

public class AccessList implements AdapterView.OnItemSelectedListener {
    private static final int DEFAULT = 0;

    private String access;
    private HashMap<String , String> uriS;
    private boolean isFirst;

    public static final String[] accessUris = { "Null",
        "http://212.3.172.35/Ast/ViewIoListAst.shtm",
        "http://Irlanda/Ast/ViewIoListAst.shtm",
        "http://Valladolid/Ast/ViewIoListAst.shtm"
    };

    AccessList() {
        uriS = new HashMap<>();
        access = AuthUI.accessNames[DEFAULT];
        setUrisMap();
        isFirst = true;
    }

    @Override
    public void onItemSelected(AdapterView<?> acc, View view, int idx, long id) {
        if (isFirst) {
            isFirst = false;
            return;
        }
        access = acc.getItemAtPosition(idx).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        return;
    }

    public String getAccess() {
        return uriS.get(access).toString();
    }

    private void setUrisMap() {

        for (int i = 0 ; i < AuthUI.accessNames.length && i < accessUris.length; i++) {
            uriS.put(AuthUI.accessNames[i] , accessUris[i]);
        }
    }

    private void traceMapAccess() {
        Log.v(AuthActivity.TAG, "*************************");
        for (Map.Entry<String , String> entry: uriS.entrySet() ) {
            Log.v(AuthActivity.TAG, "Key : " + entry.getKey());
            Log.v(AuthActivity.TAG, "Value : " + entry.getValue());
        }
        Log.v(AuthActivity.TAG, "*************************");
    }
}
