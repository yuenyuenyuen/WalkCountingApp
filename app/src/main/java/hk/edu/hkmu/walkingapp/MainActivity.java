package hk.edu.hkmu.walkingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

private CardView RecordButton;
private CardView InfoButton;
private CardView GoogleMapButton;
private CardView LanguageButton;
private CardView LocalPosButton;
private TextView walkingText;
private TextView kmText;
FusedLocationProviderClient fusedLocationProviderClient;
    private final static int REQUEST_CODE = 100;
    private double MagnitudePreview = 0;
    private Integer stepCount = 0;
    private double walkCountOnKM;

    Context context;
    Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Control Walking km Text;
        walkingText = findViewById(R.id.todaykm);
        kmText = findViewById(R.id.km);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        SensorEventListener stepDetector = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent != null) {
                    float x_accleration = sensorEvent.values[0];
                    float y_accleration = sensorEvent.values[1];
                    float z_accleration = sensorEvent.values[2];

                    double Magnitude = Math.sqrt(x_accleration * x_accleration +
                            y_accleration * y_accleration +
                            z_accleration * z_accleration);
                    double MagnitudeDelta = Magnitude - MagnitudePreview;
                    MagnitudePreview = Magnitude;

                    if (MagnitudeDelta > 3) {
                        stepCount++;
                    }
                    walkCountOnKM = stepCount*0.65*0.001;
                    String walkCountOnKMText = Double.toString(Math.round(walkCountOnKM));
                    kmText.setText(walkCountOnKMText+getString(R.string.km));
                    //walkingText.setText(stepCount+"km");
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        sensorManager.registerListener(stepDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        //Control RecordCardView OnClick
        RecordButton = findViewById((R.id.RecordCardView));
        InfoButton = findViewById((R.id.InfoCardView));
        GoogleMapButton = findViewById((R.id.GoogleMapCardView));
        LanguageButton = findViewById((R.id.LanguageCardView));
        LocalPosButton = findViewById((R.id.LocalposCardView));
        RecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRecordActivity();
            }
        });
        InfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInfoMenu();
            }
        });
        GoogleMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGoogleMapActivity();
            }
        });
        LanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { openLanguageMenu();
            }
        });
        LocalPosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { openLocalPosition();
            }
        });
    }
    public void openRecordActivity(){
        Intent intent = new Intent(this, RecordActivity.class);
        startActivity(intent);
    }
    public void openInfoMenu(){
        InfoDialog infoDialog = new InfoDialog();
        infoDialog.show(getSupportFragmentManager(), "info dialog");
    }
    public void openGoogleMapActivity(){
        Intent intent = new Intent(this, GoogleMapActivity.class);
        startActivity(intent);
    }
    public void openLanguageMenu(){
        Dialog languageDialog = new Dialog(MainActivity.this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View language_dialog = layoutInflater.inflate(R.layout.language_dialog, null);

        Button TcnButton = language_dialog.findViewById(R.id.TCN);
        Button ScnButton = language_dialog.findViewById(R.id.SCN);
        Button EngButton = language_dialog.findViewById(R.id.ENG);

        TcnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changeLanguage("zh", "HK");
                languageDialog.dismiss();
            }
        });

        ScnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLanguage("zh", "CN");
                languageDialog.dismiss();
            }
        });

        EngButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLanguage("en", "AS");
                languageDialog.dismiss();
            }
        });

        languageDialog.setContentView(language_dialog);
        languageDialog.show();
    }
    public void openLocalPosition(){
        Dialog LocalposDialog = new Dialog(MainActivity.this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View localpos_dialog = layoutInflater.inflate(R.layout.localpos_dialog, null);

        TextView lattitude = localpos_dialog.findViewById(R.id.lattitude);
        TextView longitude = localpos_dialog.findViewById(R.id.longitude);
        TextView address = localpos_dialog.findViewById(R.id.address);
        TextView city = localpos_dialog.findViewById(R.id.city);
        TextView country = localpos_dialog.findViewById(R.id.country);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null){
                                try {
                                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    lattitude.setText(getString(R.string.lattitude)+addresses.get(0).getLatitude());
                                    longitude.setText(getString(R.string.longitude)+addresses.get(0).getLongitude());
                                    address.setText(getString(R.string.address)+addresses.get(0).getAddressLine(0));
                                    city.setText(getString(R.string.city)+addresses.get(0).getLocality());
                                    country.setText(getString(R.string.country)+addresses.get(0).getCountryName());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        }else {
            askPermission();
        }
        LocalposDialog.setContentView(localpos_dialog);
        LocalposDialog.show();
    }

    private void getLastLocation(){

    }

    private void askPermission() {
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {
        if (requestCode == REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }else {
                Toast.makeText(MainActivity.this,"Please provide the required permission",Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
        protected void onStop() {
            super.onStop();
            SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.putInt("stepCount", stepCount);
            editor.apply();
        }
        protected void onResume() {
            super.onResume();

            SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
            stepCount = sharedPreferences.getInt("stepCount", stepCount);
        }
    public void changeLanguage(String lan, String con) {
        Locale locale = new Locale(lan, con);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
