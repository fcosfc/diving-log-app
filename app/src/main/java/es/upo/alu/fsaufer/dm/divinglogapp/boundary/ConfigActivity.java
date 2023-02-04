package es.upo.alu.fsaufer.dm.divinglogapp.boundary;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import es.upo.alu.fsaufer.dm.divinglogapp.R;
import es.upo.alu.fsaufer.dm.divinglogapp.control.db.AppRepository;
import es.upo.alu.fsaufer.dm.divinglogapp.control.service.ConfigService;

public class ConfigActivity extends AppCompatActivity {

    private int formResult = RESULT_CANCELED;

    private CheckBox demoMode, nitroxUseByDefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        demoMode = findViewById(R.id.demoModeCheckBox);
        nitroxUseByDefault = findViewById(R.id.nitroxUseByDefaultCheckBox);

        demoMode.setChecked(ConfigService.getConfigParameters(this).isDemoMode());
        nitroxUseByDefault.setChecked(ConfigService.getConfigParameters(this).isNitroxUseByDefault());

        Button saveButton = findViewById(R.id.saveConfigButton);
        saveButton.setOnClickListener(this::save);

        Button cancelButton = findViewById(R.id.cancelConfigButton);
        cancelButton.setOnClickListener(this::cancel);
    }

    public void cancel(View view) {
        finish();
    }

    public void save(View view) {
        if (ConfigService.getConfigParameters(this).isDemoMode() &&
                !demoMode.isChecked()) {
            AppRepository.reset();

            formResult = RESULT_OK;
        } else if (!ConfigService.getConfigParameters(this).isDemoMode() &&
                demoMode.isChecked()) {
            AppRepository.loadDemoData();

            formResult = RESULT_OK;
        }

        ConfigService.getConfigParameters(this).setDemoMode(demoMode.isChecked());
        ConfigService.getConfigParameters(this).setNitroxUseByDefault(nitroxUseByDefault.isChecked());

        ConfigService.saveConfigParameters(this);

        Toast.makeText(this, getString(R.string.config_saved), Toast.LENGTH_SHORT).show();

        finish();
    }

    @Override
    public void finish() {
        setResult(formResult);

        super.finish();
    }
}