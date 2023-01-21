package es.upo.alu.fsaufer.dm.divinglogapp.boundary;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.upo.alu.fsaufer.dm.divinglogapp.R;
import es.upo.alu.fsaufer.dm.divinglogapp.control.DiveRepository;
import es.upo.alu.fsaufer.dm.divinglogapp.entity.Dive;
import es.upo.alu.fsaufer.dm.divinglogapp.entity.WeatherConditions;
import es.upo.alu.fsaufer.dm.divinglogapp.util.Constant;
import es.upo.alu.fsaufer.dm.divinglogapp.util.DateUtil;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dive);

        List<String> locationList = DiveRepository.getLocationList();
        location = findViewById(R.id.locationSpinner);
        ArrayAdapter<String> locationAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, locationList);
        location.setAdapter(locationAdapter);

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
        } else {
            dive = (Dive) getIntent().getSerializableExtra(Constant.DIVE);
            location.setSelection(locationList.indexOf(dive.getLocation().getName()));
            spot.setText(dive.getSpot());
            diveDate.setText(dive.getFormatedDiveDate());
            minutes.setText(Integer.toString(dive.getMinutes()));
            maxDepth.setText(Float.toString(dive.getMaxDepth()));
            switch (dive.getWeatherConditions()) {
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
            nitroxUse.setChecked(dive.isNitroxUse());
            remarks.setText(dive.getRemarks());
        }

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> save(v));

        Button cancelButton = findViewById(R.id.canceButton);
        cancelButton.setOnClickListener(v -> cancel(v));

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
        dive.setLocation(DiveRepository.getLocation(textValue));

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
            DiveRepository.save(dive);

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
}