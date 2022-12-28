package es.upo.alu.fsaufer.dm.divinglogapp.boundary;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import es.upo.alu.fsaufer.dm.divinglogapp.R;
import es.upo.alu.fsaufer.dm.divinglogapp.entity.Dive;
import es.upo.alu.fsaufer.dm.divinglogapp.util.Constant;

/**
 * Actividad para mostrar el detalle de una inmersión
 */
public class DiveDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dive_detail);

        Dive dive = (Dive) getIntent().getSerializableExtra(Constant.DIVE);

        TextView textView = (TextView) findViewById(R.id.locationTextViewContent);
        textView.setText(dive.getLocation());
        textView = (TextView) findViewById(R.id.spotTextViewContent);
        textView.setText(dive.getSpot());
        textView = (TextView) findViewById(R.id.diveDateTextViewContent);
        textView.setText(dive.getFormatedDiveDate());
        textView = (TextView) findViewById(R.id.minutesTextViewContent);
        textView.setText(Integer.toString(dive.getMinutes()));
        textView = (TextView) findViewById(R.id.maxDepthTextViewContent);
        textView.setText(Float.toString(dive.getMaxDepth()));
        textView = (TextView) findViewById(R.id.remarksTextViewContent);
        textView.setText(dive.getRemarks());
    }
}