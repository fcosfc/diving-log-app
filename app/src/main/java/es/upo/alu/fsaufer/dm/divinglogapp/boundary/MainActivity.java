package es.upo.alu.fsaufer.dm.divinglogapp.boundary;

import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import es.upo.alu.fsaufer.dm.divinglogapp.R;
import es.upo.alu.fsaufer.dm.divinglogapp.control.db.AppRepository;
import es.upo.alu.fsaufer.dm.divinglogapp.control.service.DiveSharingService;
import es.upo.alu.fsaufer.dm.divinglogapp.entity.Dive;
import es.upo.alu.fsaufer.dm.divinglogapp.util.Constant;

/**
 * Actividad principal que contiene el listado de inmersiones.
 * <p>
 * Pulsando sobre un elemento de la lista se abre el detalle de la inmersión.
 */
public class MainActivity extends AppCompatActivity implements DiveClickListener {

    private DiveListAdapter adapter;

    private ActivityResultLauncher<Intent> appActivityLauncher;

    private Dive selectedDive = null;

    private final ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.dive_action_context_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.view_dive:
                    viewDive(selectedDive);

                    mode.finish();
                    return true;
                case R.id.edit_dive:
                    editDive(selectedDive);

                    mode.finish();
                    return true;
                case R.id.delete_dive:
                    deleteDive(selectedDive);

                    mode.finish();
                    return true;
                case R.id.share_dive:
                    shareDive(selectedDive);

                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            selectedDive = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DiveListAdapter(this);
        recyclerView.setAdapter(adapter);

        appActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        adapter.notifyDataSetChanged();
                    }
                }
        );

        AppRepository.init(this);

        if (AppRepository.getDiveList() == null || AppRepository.getDiveList().isEmpty()) {
            Snackbar.make(recyclerView, R.string.notification_data_reset_text, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.new_dive) {
            editDive(null);
        } else {
            viewConfig();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view, int position) {
        selectedDive = AppRepository.getDiveList().get(position);

        this.startActionMode(actionModeCallback);
    }

    private void editDive(Dive dive) {
        Intent intent = new Intent(this, EditDiveActivity.class);
        if (dive != null) {
            intent.putExtra(Constant.DIVE, dive);
        }

        appActivityLauncher.launch(intent);

        adapter.clearSelectedItem();
    }

    private void viewDive(Dive dive) {
        if (dive != null) {
            adapter.clearSelectedItem();

            Intent intent = new Intent(this, DiveDetailActivity.class);
            intent.putExtra(Constant.DIVE, dive);

            startActivity(intent);
        }
    }

    private void deleteDive(Dive dive) {
        if (dive != null) {
            adapter.clearSelectedItem();

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(R.string.delete_dive_confirmation);
            alert.setMessage(R.string.delete_dive_confirmation_message);
            alert.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                AppRepository.delete(dive);

                adapter.notifyDataSetChanged();
            });
            alert.setNegativeButton(android.R.string.no, (dialog, which) -> dialog.cancel());
            alert.show();
        }
    }

    private void shareDive(Dive dive) {
        if (dive != null) {
            adapter.clearSelectedItem();

            DiveSharingService.share(this, dive);
        }
    }

    private void viewConfig() {
        Intent intent = new Intent(this, ConfigActivity.class);

        appActivityLauncher.launch(intent);
    }
}