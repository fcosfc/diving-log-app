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

    private final DiveClickListener diveClickListener;

    private final List<Dive> diveList;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView placeTextView;
        private final TextView dateTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            placeTextView = itemView.findViewById(R.id.placeTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }

        public TextView getPlaceTextView() {
            return placeTextView;
        }

        public TextView getDateTextView() {
            return dateTextView;
        }

        @Override
        public void onClick(View view) {
            if (diveClickListener != null) {
                diveClickListener.onClick(view, getAdapterPosition());
            }
        }
    }

    public DiveListAdapter(DiveClickListener diveClickListener) {
        this.diveClickListener = diveClickListener;
        diveList = DiveRepository.getDiveList();
    }

    @NonNull
    @Override
    public DiveListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dive_item_list, parent, false);

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
