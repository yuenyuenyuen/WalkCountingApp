package hk.edu.hkmu.walkingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

private CardView RecordButton;
private CardView InfoButton;
private CardView GoogleMapButton;
private CardView LanguageButton;
private TextView walkingText;
private TextView kmText;

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
                    kmText.setText(walkCountOnKMText+"km");
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
        Intent intent = new Intent(this, RecordActivity.class);
        startActivity(intent);
    }
    public void openLanguageMenu(){
        Dialog languageDialog = new Dialog(MainActivity.this,R.style.CustomDialogTheme);
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

        languageDialog.setTitle("Select Language");

        languageDialog.show();

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