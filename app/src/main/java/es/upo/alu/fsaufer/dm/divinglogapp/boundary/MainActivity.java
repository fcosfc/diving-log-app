package es.upo.alu.fsaufer.dm.divinglogapp.boundary;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import es.upo.alu.fsaufer.dm.divinglogapp.R;
import es.upo.alu.fsaufer.dm.divinglogapp.control.DiveRepository;
import es.upo.alu.fsaufer.dm.divinglogapp.util.Constant;

/**
 * Actividad principal
 */
public class MainActivity extends AppCompatActivity {

    private ListFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment = new ListFragment();
        transaction.replace(R.id.sample_content_fragment, fragment);
        transaction.commit();

        DiveRepository.init(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(this, EditDive.class);
        intent.putExtra(Constant.ADAPTER, fragment.getAdapter());
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }
}