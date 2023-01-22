package es.upo.alu.fsaufer.dm.divinglogapp.boundary;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.upo.alu.fsaufer.dm.divinglogapp.R;
import es.upo.alu.fsaufer.dm.divinglogapp.control.db.AppRepository;
import es.upo.alu.fsaufer.dm.divinglogapp.control.rest.WeatherService;
import es.upo.alu.fsaufer.dm.divinglogapp.control.sensor.DivingLocationService;
import es.upo.alu.fsaufer.dm.divinglogapp.dto.WeatherServiceResponse;
import es.upo.alu.fsaufer.dm.divinglogapp.entity.Dive;
import es.upo.alu.fsaufer.dm.divinglogapp.entity.DiveLocation;
import es.upo.alu.fsaufer.dm.divinglogapp.entity.WeatherConditions;
import es.upo.alu.fsaufer.dm.divinglogapp.util.Constant;
import es.upo.alu.fsaufer.dm.divinglogapp.util.DateUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Actividad para guardar nuevas inmersiones o editar existentes
 */
public class EditDiveActivity extends AppCompatActivity {

    private int formResult = RESULT_CANCELED;

    private Spinner location;
    private EditText spot, diveDate, minutes, maxDepth, remarks;
    private RadioButton goodWeatherConditions, tolerableWeatherConditions, badWeatherConditions;
    private CheckBox nitroxUse;

    private Dive dive = null;

    private boolean isEdition = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dive);

        List<String> locationList = AppRepository.getLocationList();
        location = findViewById(R.id.locationSpinner);
        ArrayAdapter<String> locationAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, locationList);
        location.setAdapter(locationAdapter);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PERMISSION_GRANTED) {
            location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (!isEdition) {
                        readCurrentWeatherConditions(AppRepository.getLocation(locationList.get(position)));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), R.string.internet_permission, Toast.LENGTH_LONG).show();
        }

        spot = findViewById(R.id.spotEditText);
        diveDate = findViewById(R.id.diveDateEditTextDate);
        diveDate.setText(DateUtil.formatDate(new Date()));
        minutes = findViewById(R.id.minutesEditTextNumber);
        maxDepth = findViewById(R.id.maxDepthEditTextNumberDecimal);
        goodWeatherConditions = findViewById(R.id.goodWeatherRadioButton);
        tolerableWeatherConditions = findViewById(R.id.tolerableWeatherRadioButton);
        badWeatherConditions = findViewById(R.id.badWeatherRadioButton);
        nitroxUse = findViewById(R.id.nitroxUseCheckBox);
        remarks = findViewById(R.id.remarksEditTextTextMultiLine);

        if (getIntent().getSerializableExtra(Constant.DIVE) == null) {
            dive = new Dive();

            DivingLocationService.init(this);
            if (DivingLocationService.isLocationAvailable()) {
                String nearestLocationName = DivingLocationService.getNearestLocationName();
                if (nearestLocationName != null) {
                    location.setSelection(locationList.indexOf(nearestLocationName));
                }
            }
        } else {
            isEdition = true;
            dive = (Dive) getIntent().getSerializableExtra(Constant.DIVE);
            location.setSelection(locationList.indexOf(dive.getLocation().getName()));
            spot.setText(dive.getSpot());
            diveDate.setText(dive.getFormattedDiveDate());
            minutes.setText(Integer.toString(dive.getMinutes()));
            maxDepth.setText(Float.toString(dive.getMaxDepth()));
            setWeatherConditions(dive.getWeatherConditions());
            nitroxUse.setChecked(dive.isNitroxUse());
            remarks.setText(dive.getRemarks());
        }

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this::save);

        Button cancelButton = findViewById(R.id.canceButton);
        cancelButton.setOnClickListener(this::cancel);

        diveDate.setOnClickListener(v -> showDatePicker());
    }

    @Override
    public void finish() {
        setResult(formResult);

        super.finish();
    }

    public void save(View view) {
        String textValue;
        boolean formHasErrors = false;

        textValue = (String) location.getSelectedItem();
        dive.setLocation(AppRepository.getLocation(textValue));

        textValue = spot.getText().toString().trim();
        if (textValue.isEmpty()) {
            spot.setError(getString(R.string.not_null));
            spot.setFocusable(true);
            formHasErrors = true;
        } else {
            dive.setSpot(spot.getText().toString().trim());
        }

        textValue = diveDate.getText().toString().trim();
        if (textValue.isEmpty()) {
            diveDate.setError(getString(R.string.not_null));
            diveDate.setFocusable(true);
            formHasErrors = true;
        } else {
            DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);

            Date date;

            try {
                date = dateFormat.parse(textValue);

                dive.setDiveDate(date);
            } catch (ParseException e) {
                diveDate.setError(getString(R.string.date_not_valid));
                diveDate.setFocusable(true);
                formHasErrors = true;
            }
        }

        textValue = minutes.getText().toString().trim();
        if (textValue.isEmpty()) {
            minutes.setError(getString(R.string.not_null));
            minutes.setFocusable(true);
            formHasErrors = true;
        } else {
            dive.setMinutes(Integer.parseInt(textValue));
        }

        textValue = maxDepth.getText().toString().trim();
        if (textValue.isEmpty()) {
            maxDepth.setError(getString(R.string.not_null));
            maxDepth.setFocusable(true);
            formHasErrors = true;
        } else {
            dive.setMaxDepth(Float.parseFloat(textValue));
        }

        if (goodWeatherConditions.isChecked()) {
            dive.setWeatherConditions(WeatherConditions.GOOD);
        } else if (tolerableWeatherConditions.isChecked()) {
            dive.setWeatherConditions(WeatherConditions.TOLERABLE);
        } else if (badWeatherConditions.isChecked()) {
            dive.setWeatherConditions(WeatherConditions.BAD);
        }

        dive.setNitroxUse(nitroxUse.isChecked());

        dive.setRemarks(remarks.getText().toString().trim());

        if (!formHasErrors) {
            AppRepository.save(dive);

            formResult = RESULT_OK;

            Toast.makeText(getApplicationContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();

            finish();
        }
    }

    public void cancel(View view) {
        finish();
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDayOfMonth) ->
                        diveDate.setText(
                                DateUtil.formatDate(
                                        DateUtil.parseDate(
                                                String.format("%d-%d-%d", selectedYear, ++selectedMonth, selectedDayOfMonth)))),
                year, month, dayOfMonth);

        datePickerDialog.show();
    }

    private void readCurrentWeatherConditions(DiveLocation diveLocation) {
        Call<WeatherServiceResponse> weatherServiceCall =
                WeatherService.getApi(this).getCurrentWeather(Constant.WEATHER_SERVICE_API_KEY,
                        Constant.WEATHER_SERVICE_UNITS,
                        diveLocation.getLocation().getFormattedLongitude(),
                        diveLocation.getLocation().getFormattedLatitude());

        weatherServiceCall.enqueue(new Callback<WeatherServiceResponse>() {
            @Override
            public void onResponse(Call<WeatherServiceResponse> call, Response<WeatherServiceResponse> response) {
                WeatherServiceResponse weatherServiceResponse = response.body();

                Log.i("WeatherService", weatherServiceResponse.toString());

                setWeatherConditions(inferWeatherConditions(weatherServiceResponse));

                Toast.makeText(getApplicationContext(), getCurrentWeatherMessage(weatherServiceResponse), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<WeatherServiceResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.weather_service_error, Toast.LENGTH_LONG).show();

                Log.e("WeatherService", call.toString(), t);
            }

            private String getCurrentWeatherMessage(WeatherServiceResponse weatherServiceResponse) {
                StringBuilder builder = new StringBuilder();

                builder.append(getString(R.string.temperature)).append(": ").append(weatherServiceResponse.getMain().getTemperature()).append("º, ");
                builder.append(getString(R.string.wind_speed)).append(": ").append(weatherServiceResponse.getWind().getSpeed()).append("Km/h, ");
                builder.append(getString(R.string.clouds)).append(": ").append(weatherServiceResponse.getClouds().getPercentaje()).append("%");

                return builder.toString();
            }
        });
    }

    private WeatherConditions inferWeatherConditions(WeatherServiceResponse weatherServiceResponse) {
        int globalWeather = 0;

        if (weatherServiceResponse.getMain().getTemperature() > 20 &&
                weatherServiceResponse.getMain().getTemperature() > 30) {
            globalWeather = 30;
        } else if (weatherServiceResponse.getMain().getTemperature() > 15 &&
                weatherServiceResponse.getMain().getTemperature() <= 20) {
            globalWeather = 15;
        }

        if (weatherServiceResponse.getWind().getSpeed() < 10) {
            globalWeather += 50;
        } else if (weatherServiceResponse.getWind().getSpeed() < 20) {
            globalWeather += 25;
        }

        if (weatherServiceResponse.getClouds().getPercentaje() < 25) {
            globalWeather += 20;
        } else if (weatherServiceResponse.getClouds().getPercentaje() < 50) {
            globalWeather += 10;
        }

        if (globalWeather >= 70) {
            return WeatherConditions.GOOD;
        } else if (globalWeather >= 40) {
            return WeatherConditions.TOLERABLE;
        } else {
            return WeatherConditions.BAD;
        }
    }

    private void setWeatherConditions(WeatherConditions weatherConditions) {
        switch (weatherConditions) {
            case GOOD:
                goodWeatherConditions.setChecked(true);
                break;
            case TOLERABLE:
                tolerableWeatherConditions.setChecked(true);
                break;
            case BAD:
                badWeatherConditions.setChecked(true);
                break;
        }
    }
}