package hk.edu.hkmu.walkingapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class JsonHandlerThread extends Thread {
    private static final String TAG = "JsonHandlerThread";
    // URL to get contacts JSON file
    private static String jsonUrl = "https://www.lcsd.gov.hk/datagovhk/facility/facility-fw.json";

    // send request to the url, no need to be changed
    public static String makeRequest() {
        String response = null;
        try {
            URL url = new URL(jsonUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = inputStreamToString(in);
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    // download of JSON file from the url to the app
    private static String inputStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = "";
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
            }
        }
        return sb.toString();
    }

    public void run() {
        // "contactStr" variable store the json file content
        String addressStr = makeRequest();
        Log.e(TAG, "Response from url: " + addressStr);

        if (addressStr != null) {
            try {
                JSONArray jsonObj = new JSONArray(addressStr);

                // Getting JSON Array node
                //SONArray addresses = jsonObj.getJSONArray("addresses");

                // looping through All Contacts
                AddressInfo.addressList.clear();
                for (int i = 0; i < jsonObj.length(); i++) {
                    JSONObject c = jsonObj.getJSONObject(i);

                    String Title_en = c.getString("Title_en");
                    String District_en = c.getString("District_en");
                    String Route_en = c.getString("Route_en");
                    String replaceRoute_en = Route_en.replace("<br>", "\n");
                    String HowToAccess_en = c.getString("HowToAccess_en");
                    String replaceHowToAccess_en = HowToAccess_en.replace("<br>", "\n");
                    String MapURL_en = c.getString("MapURL_en");

                    String Title_tc = c.getString("Title_tc");
                    String District_tc = c.getString("District_tc");
                    String Route_tc = c.getString("Route_tc");
                    String replaceRoute_tc = Route_tc.replace("<br>", "\n");
                    String HowToAccess_tc = c.getString("HowToAccess_tc");
                    String replaceHowToAccess_tc = HowToAccess_tc.replace("<br>", "\n");
                    String MapURL_tc = c.getString("MapURL_tc");

                    String Title_sc = c.getString("Title_sc");
                    String District_sc = c.getString("District_sc");
                    String Route_sc = c.getString("Route_sc");
                    String replaceRoute_sc = Route_sc.replace("<br>", "\n");
                    String HowToAccess_sc = c.getString("HowToAccess_sc");
                    String replaceHowToAccess_sc = HowToAccess_sc.replace("<br>", "\n");
                    String MapURL_sc = c.getString("MapURL_sc");

                    String Latitude = c.getString("Latitude");
                    String Longitude = c.getString("Longitude");

                    // Add contact (name, email, address) to contact list
                        AddressInfo.addAddress(Title_en, District_en, replaceRoute_en, replaceHowToAccess_en, MapURL_en, Latitude, Longitude, Title_tc, District_tc, replaceRoute_tc, replaceHowToAccess_tc, MapURL_tc, Title_sc, District_sc, replaceRoute_sc, replaceHowToAccess_sc, MapURL_sc);


                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");
        }
    }
}