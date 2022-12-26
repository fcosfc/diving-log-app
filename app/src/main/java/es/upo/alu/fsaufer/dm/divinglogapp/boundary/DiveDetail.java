package es.upo.alu.fsaufer.dm.divinglogapp.boundary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import es.upo.alu.fsaufer.dm.divinglogapp.R;
import es.upo.alu.fsaufer.dm.divinglogapp.util.Constant;

/**
 * Actividad para mostrar el detalle de una inmersión
 */
public class DiveDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dive_detail);

        TextView textView = (TextView) findViewById(R.id.locationTextViewContent);
        textView.setText(getIntent().getStringExtra(Constant.LOCATION));
        textView = (TextView) findViewById(R.id.spotTextViewContent);
        textView.setText(getIntent().getStringExtra(Constant.SPOT));
        textView = (TextView) findViewById(R.id.diveDateTextViewContent);
        textView.setText(getIntent().getStringExtra(Constant.DIVE_DATE));
        textView = (TextView) findViewById(R.id.minutesTextViewContent);
        textView.setText(Integer.toString(getIntent().getIntExtra(Constant.MINUTES, 0)));
        textView = (TextView) findViewById(R.id.maxDepthTextViewContent);
        textView.setText(Float.toString(getIntent().getFloatExtra(Constant.MAX_DEPTH, 0)));
        textView = (TextView) findViewById(R.id.remarksTextViewContent);
        textView.setText(getIntent().getStringExtra(Constant.REMARKS));
    }
}