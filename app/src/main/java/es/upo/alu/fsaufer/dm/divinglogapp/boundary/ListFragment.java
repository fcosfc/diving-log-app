package es.upo.alu.fsaufer.dm.divinglogapp.boundary;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import es.upo.alu.fsaufer.dm.divinglogapp.R;
import es.upo.alu.fsaufer.dm.divinglogapp.control.DiveRepository;
import es.upo.alu.fsaufer.dm.divinglogapp.entity.Dive;
import es.upo.alu.fsaufer.dm.divinglogapp.util.Constant;

/**
 * Clase fragmento para mostrar la lista de inmersiones
 */
public class ListFragment extends Fragment implements DiveClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new DiveListAdapter(this));

        return view;
    }

    @Override
    public void onClick(View view, int position) {
        Dive dive = DiveRepository.getDiveList().get(position);

        Intent intent = new Intent(getActivity(), DiveDetail.class);
        intent.putExtra(Constant.LOCATION, dive.getLocation());
        intent.putExtra(Constant.SPOT, dive.getSpot());
        intent.putExtra(Constant.DIVE_DATE, dive.getFormatedDiveDate());
        intent.putExtra(Constant.MINUTES, dive.getMinutes());
        intent.putExtra(Constant.MAX_DEPTH, dive.getMaxDepth());
        intent.putExtra(Constant.REMARKS, dive.getRemarks());

        startActivity(intent);
    }
}
