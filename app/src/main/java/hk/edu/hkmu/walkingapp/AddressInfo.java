package hk.edu.hkmu.walkingapp;

import android.media.Image;
import android.net.Uri;
import android.widget.ImageView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class AddressInfo {
    public static String TITLE = "Title: ";
    public static String DISTRICT = "District: ";
    public static String ROUTE = "Route: ";
    public static String HOWTOACCESS = "HowToAccess: ";
    public static String MAPURL = "MapURL: ";
    public static String LATITUDE = "Latitude: ";
    public static String LONGITUDE = "Longitude: ";

    // "contactList" variable used for storing all contact that retrieved from JSON
    // it is used in JsonHandlerThread and also MainActivity program
    public static ArrayList<HashMap<String, Object>> addressList = new ArrayList<>();
    public static Uri url;

    // addContact is a function
    // Creates and add contact to contact list
    // x4 input, representing name, email, address and mobile
    public static void addAddress(String Title_en, String District_en, String Route_en, String HowToAccess_en, String MapURL_en, String Latitude, String Longitude, String Title_tc, String District_tc, String Route_tc, String HowToAccess_tc, String MapURL_tc, String Title_sc, String District_sc, String Route_sc, String HowToAccess_sc, String MapURL_sc) {
        // Create contact
        HashMap<String, Object> address = new HashMap<>();
        String newroute_en = Route_en+"\n";
        String newhowtoaccess_en = HowToAccess_en+"\n";
        String newlatitude_en = Latitude+": ";
        String newlongitude_en = Longitude+": ";
        String newroute_tc = Route_tc+"\n";
        String newhowtoaccess_tc = HowToAccess_tc+"\n";
        String newlatitude_tc = Latitude+": ";
        String newlongitude_tc = Longitude+": ";
        String newroute_sc = Route_sc+"\n";
        String newhowtoaccess_sc = HowToAccess_sc+"\n";
        String newlatitude_sc = Latitude+": ";
        String newlongitude_sc = Longitude+": ";
        url = Uri.parse(MapURL_en);

        if(MainActivity.languagenum==0) {
            address.put(TITLE, Title_en);
            address.put(DISTRICT, District_en);
            address.put(ROUTE, newroute_en);
            address.put(HOWTOACCESS, newhowtoaccess_en);
            address.put(MAPURL, url);
            address.put(LATITUDE, newlatitude_en);
            address.put(LONGITUDE, newlongitude_en);
        }else if(MainActivity.languagenum==1) {
            address.put(TITLE, Title_tc);
            address.put(DISTRICT, District_tc);
            address.put(ROUTE, newroute_tc);
            address.put(HOWTOACCESS, newhowtoaccess_tc);
            address.put(MAPURL, url);
            address.put(LATITUDE, newlatitude_tc);
            address.put(LONGITUDE, newlongitude_tc);
        }
        else{
            address.put(TITLE, Title_sc);
            address.put(DISTRICT, District_sc);
            address.put(ROUTE, newroute_sc);
            address.put(HOWTOACCESS, newhowtoaccess_sc);
            address.put(MAPURL, url);
            address.put(LATITUDE, newlatitude_sc);
            address.put(LONGITUDE, newlongitude_sc);
        }

        // Add contact to contact list
        addressList.add(address);
        /*address.remove(TITLE);
        address.remove(DISTRICT);
        address.remove(ROUTE);
        address.remove(HOWTOACCESS);
        address.remove(LATITUDE);
        address.remove(LONGITUDE);*/
    }
}
