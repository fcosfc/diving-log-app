package es.upo.alu.fsaufer.dm.divinglogapp.boundary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.upo.alu.fsaufer.dm.divinglogapp.R;
import es.upo.alu.fsaufer.dm.divinglogapp.control.DiveRepository;
import es.upo.alu.fsaufer.dm.divinglogapp.entity.Dive;

/**
 * Clase adaptador para mostrar los elementos de la lista de inmersiones
 */
public class DiveListAdapter extends RecyclerView.Adapter<DiveListAdapter.ViewHolder> {

    private final List<Dive> diveList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView placeTextView;
        private final TextView dateTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            placeTextView = itemView.findViewById(R.id.placeTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }

        public TextView getPlaceTextView() {
            return placeTextView;
        }

        public TextView getDateTextView() {
            return dateTextView;
        }
    }

    public DiveListAdapter() {
        diveList = DiveRepository.getDiveList();
    }

    @NonNull
    @Override
    public DiveListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dive_item_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiveListAdapter.ViewHolder holder, int position) {
        holder.getPlaceTextView().setText(diveList.get(position).getPlace());
        holder.getDateTextView().setText(diveList.get(position).getFormatedDiveDate());
    }

    @Override
    public int getItemCount() {
        return diveList.size();
    }

}
