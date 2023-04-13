package hk.edu.hkmu.walkingapp;

import static hk.edu.hkmu.walkingapp.AddressInfo.url;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.HashMap;

public class RecordActivity extends AppCompatActivity {
    private String TAG = "RecordActivity";
    private ListView listView; // ui component for displaying all contacts (x8 contacts)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);


        JsonHandlerThread jsonHandlerThread = new JsonHandlerThread();
        jsonHandlerThread.start();

        try {
            jsonHandlerThread.join();

            // after retrieved the json contents and stored into the "contactList", i.e. contactList is ready for display
            // 1. setup ListView component to display the contact list
            // 2. implement the item click event handling

            // 1.
            // Create an adapter object that accommodates a data list of items to views that becomes children of an adapter view
            // i.e. the Adapter object acts as a bridge between an ListView and the contacts for that view
            SimpleAdapter adapter = new SimpleAdapter(
                    this,
                    AddressInfo.addressList,  // "contactList" that stores all the retrieved contacts, defined in ContactInfo
                    R.layout.list_view_layout, // layout resource represent item layout design
                    new String[] { AddressInfo.TITLE, AddressInfo.DISTRICT, AddressInfo.ROUTE, AddressInfo.HOWTOACCESS, AddressInfo.MAPURL, AddressInfo.LATITUDE, AddressInfo.LONGITUDE },  // represent the three data that display in an item
                    new int[] { R.id.title, R.id.district, R.id.route, R.id.howtoaccess, R.id.mapurl, R.id.latitude, R.id.longitude }  // represent where the item is displayed
            );
            adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                                      @Override
                                      public boolean setViewValue(View view, Object data, String textRepresentation) {
                                          if (view == null) {
                                              return false;
                                          }
                                          if (view.getId() == R.id.title) {
                                              // Handle text view
                                              TextView textView = (TextView) view;
                                              textView.setText(textRepresentation);
                                              return true;
                                          }else if (view.getId() == R.id.district) {
                                              // Handle text view
                                              TextView textView = (TextView) view;
                                              textView.setText(textRepresentation);
                                              return true;
                                          }else
                                          if (view.getId() == R.id.route) {
                                              // Handle text view
                                              TextView textView = (TextView) view;
                                              textView.setText(textRepresentation);
                                              return true;
                                          }else
                                          if (view.getId() == R.id.howtoaccess) {
                                              // Handle text view
                                              TextView textView = (TextView) view;
                                              textView.setText(textRepresentation);
                                              return true;
                                          }else
                                          if (view.getId() == R.id.latitude) {
                                              // Handle text view
                                              TextView textView = (TextView) view;
                                              textView.setText(textRepresentation);
                                              return true;
                                          }else
                                          if (view.getId() == R.id.longitude) {
                                              // Handle text view
                                              TextView textView = (TextView) view;
                                              textView.setText(textRepresentation);
                                              return true;
                                          }else if (view.getId() == R.id.mapurl) {
                                              // Handle image view
                                              String imageUrl = data.toString();
                                              ImageView imageView = (ImageView) view;
                                              Glide.with(getApplicationContext()).load(imageUrl).into(imageView);
                                                  return true;
                                              }
                                          return false;
                                      }
                                  });
                    // Associate the adapter with the ListView
            listView = (ListView) findViewById(R.id.listview);
                    listView.setAdapter(adapter);


        } catch (InterruptedException e) {
            Log.e(TAG, "InterruptedException: " + e.getMessage());
        }
    }
    }