package es.upo.alu.fsaufer.dm.divinglogapp.boundary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import es.upo.alu.fsaufer.dm.divinglogapp.R;
import es.upo.alu.fsaufer.dm.divinglogapp.util.Constant;

public class DiveDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dive_detail);

        TextView textViewLocation = (TextView) findViewById(R.id.textViewLocation);
        textViewLocation.setText(getIntent().getStringExtra(Constant.LOCATION));
    }
}