package com.stockita.newxyzreader.remote;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Fetch and download a josn object from the net
 */
public class RemoteEndpointUtil {

    // Constant
    private static final String TAG = "RemoteEndpointUtil";
    private static int lStatus = 0;


    /**
     * Constructor
     */
    private RemoteEndpointUtil() {
    }


    /**
     * This method will parse the String of JSON into JSON array object
     * @return      JSONArray
     */
    public static JSONArray fetchJsonArray() throws IOException {

        // Json of String that will hold the data
        String itemsJson = null;

        try {
            itemsJson = fetchPlainText(Config.BASE_URL);
        } catch (IOException e) {
            Log.e(TAG, "Error fetching items JSON", e);
            return null;
        }

        // Parse JSON
        try {
            JSONTokener tokener = new JSONTokener(itemsJson);
            Object val = tokener.nextValue();
            if (!(val instanceof JSONArray)) {
                throw new JSONException("Expected JSONArray");
            }
            return (JSONArray) val;
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing items JSON", e);
        }

        return null;
    }

    /**
     * This method will fetch data from the internet, then return a String of JSON
     * @param urls      The source of the data in the internet
     * @return          String of JSON
     * @throws IOException
     */
    static String fetchPlainText(URL urls) throws IOException {

        BufferedReader reader = null;
        HttpURLConnection con = null;

        try {
            con = (HttpURLConnection) urls.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            InputStream inputStream = con.getInputStream();
            // Return null if no date
            if (inputStream == null) return null;

            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer buffer = new StringBuffer();

            String line;
            while((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            // Return null if no data
            if (buffer.length() == 0) return null;

            return buffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
            try {
                lStatus = con.getResponseCode();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        } finally {
            try {
                if (reader != null) reader.close();
                if (con != null) con.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
