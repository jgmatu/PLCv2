package es.urjc.mov.javsan.plc;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



// This class get the TagsData from the PLC, all the tags
// of PLC are catched here if you wanna monitorize other
// tags you must especific here the new tag to monitorize...
public class TagsData {

    public static String[] TAGS = {
                "ALARMA_ACTIVA",
                "VEL_VIENTOA_10",
                "N_GENERADOR",
                "N_ROTOR",
                "ANGULO_PITCH"
                /* Put new Tag to monitorize */
        };

    private HashMap<String , String> data;

    // Set data from HTTP Request in a Hash Map
    // Keys  values to the tags names, and values PLC.
    TagsData(Elements tags, Elements values) {
        data = new HashMap();

        Iterator<Element> k = tags.iterator();
        Iterator<Element> v = values.iterator();

        while (k.hasNext() && v.hasNext()) {
            data.put(k.next().text(), v.next().text());
        }
    }

    @Override
    public String toString() {
        String result = "";

        for (Map.Entry<String, String> entry : data.entrySet()) {
            result += formatLine(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public String getTest() {
        String result = "\n";

        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (isTag(entry.getKey())) {
                result += formatLine(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    public HashMap<String, String> getTestUI () {
        HashMap<String, String> result = new HashMap<>();

        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (isTag(entry.getKey())) {
                result.put(entry.getKey(), entry.getValue());
            }
        }

        return result;
    }

    private String formatLine(String k , String v) {
        return String.format("Key : %30s Value : %15s\n", k , v);
    }

    private boolean isTag(String tag) {
        for (String t : TAGS) {
            if (t.equals(tag))  {
                return true;
            }
        }
        return false;
    }
}
