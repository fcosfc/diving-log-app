package es.upo.alu.fsaufer.dm.divinglogapp.boundary;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;

import es.upo.alu.fsaufer.dm.divinglogapp.R;
import es.upo.alu.fsaufer.dm.divinglogapp.control.DiveRepository;
import es.upo.alu.fsaufer.dm.divinglogapp.entity.Dive;
import es.upo.alu.fsaufer.dm.divinglogapp.util.Constant;

/**
 * Clase fragmento para mostrar la lista de inmersiones
 */
public class ListFragment extends Fragment implements DiveClickListener, Serializable {

    private static final long serialVersionUID = 5799654478675716638L;

    private DiveListAdapter adapter;

    public DiveListAdapter getAdapter() {
        return adapter;
    }

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
        adapter = new DiveListAdapter(this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onClick(View view, int position) {
        Dive dive = DiveRepository.getDiveList().get(position);

        Intent intent = new Intent(getActivity(), DiveDetail.class);
        intent.putExtra(Constant.DIVE, dive);

        startActivity(intent);
    }
}
