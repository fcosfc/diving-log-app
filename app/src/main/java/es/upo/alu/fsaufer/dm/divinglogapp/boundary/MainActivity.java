package es.upo.alu.fsaufer.dm.divinglogapp.boundary;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import es.upo.alu.fsaufer.dm.divinglogapp.R;
import es.upo.alu.fsaufer.dm.divinglogapp.control.DiveRepository;
import es.upo.alu.fsaufer.dm.divinglogapp.entity.Dive;
import es.upo.alu.fsaufer.dm.divinglogapp.util.Constant;

/**
 * Actividad principal que contiene el listado de inmersiones.
 *
 * Pulsando sobre un elemento de la lista se abre el detalle de la inmersión.
 */
public class MainActivity extends AppCompatActivity implements DiveClickListener {

    private DiveListAdapter adapter;

    ActivityResultLauncher<Intent> editDiveLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DiveListAdapter(this);
        recyclerView.setAdapter(adapter);

        DiveRepository.init(this);

        editDiveLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(this, EditDiveActivity.class);

        editDiveLauncher.launch(intent);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view, int position) {
        Dive dive = DiveRepository.getDiveList().get(position);

        Intent intent = new Intent(this, DiveDetailActivity.class);
        intent.putExtra(Constant.DIVE, dive);

        startActivity(intent);
    }
}