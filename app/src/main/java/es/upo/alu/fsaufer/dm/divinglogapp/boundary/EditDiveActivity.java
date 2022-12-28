package es.upo.alu.fsaufer.dm.divinglogapp.boundary;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.upo.alu.fsaufer.dm.divinglogapp.R;
import es.upo.alu.fsaufer.dm.divinglogapp.control.DiveRepository;
import es.upo.alu.fsaufer.dm.divinglogapp.entity.Dive;
import es.upo.alu.fsaufer.dm.divinglogapp.util.Constant;
import es.upo.alu.fsaufer.dm.divinglogapp.util.DateUtil;

/**
 * Actividad para guardar nuevas inmersiones o editar existentes
 */
public class EditDiveActivity extends AppCompatActivity {

    private int formResult = RESULT_CANCELED;

    private EditText location, spot, diveDate, minutes, maxDepth, remarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dive);

        location = findViewById(R.id.locationEditText);
        spot = findViewById(R.id.spotEditText);
        diveDate = findViewById(R.id.diveDateEditTextDate);
        diveDate.setText(DateUtil.formatDate(new Date()));
        minutes = findViewById(R.id.minutesEditTextNumber);
        maxDepth = findViewById(R.id.maxDepthEditTextNumberDecimal);
        remarks = findViewById(R.id.remarksEditTextTextMultiLine);

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> save(v));

        Button cancelButton = findViewById(R.id.canceButton);
        cancelButton.setOnClickListener(v -> cancel(v));
    }

    @Override
    public void finish() {
        setResult(formResult);

        super.finish();
    }

    public void save(View view) {
        Dive dive = new Dive();
        String value;
        boolean formHasErrors = false;

        value = location.getText().toString().trim();
        if (value.isEmpty()) {
            location.setError(getString(R.string.not_null));
            location.setFocusable(true);
            formHasErrors = true;
        } else {
            dive.setLocation(value);
        }

        value = spot.getText().toString().trim();
        if (value.isEmpty()) {
            spot.setError(getString(R.string.not_null));
            spot.setFocusable(true);
            formHasErrors = true;
        } else {
            dive.setSpot(spot.getText().toString().trim());
        }

        value = diveDate.getText().toString().trim();
        if (value.isEmpty()) {
            diveDate.setError(getString(R.string.not_null));
            diveDate.setFocusable(true);
            formHasErrors = true;
        } else {
            DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);

            Date date;

            try {
                date = dateFormat.parse(value);

                dive.setDiveDate(date);
            } catch (ParseException e) {
                diveDate.setError(getString(R.string.date_not_valid));
                diveDate.setFocusable(true);
                formHasErrors = true;
            }
        }

        value = minutes.getText().toString().trim();
        if (value.isEmpty()) {
            minutes.setError(getString(R.string.not_null));
            minutes.setFocusable(true);
            formHasErrors = true;
        } else {
            dive.setMinutes(Integer.parseInt(value));
        }

        value = maxDepth.getText().toString().trim();
        if (value.isEmpty()) {
            maxDepth.setError(getString(R.string.not_null));
            maxDepth.setFocusable(true);
            formHasErrors = true;
        } else {
            dive.setMaxDepth(Float.parseFloat(value));
        }

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
}