package es.upo.alu.fsaufer.dm.divinglogapp.boundary;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;

import es.upo.alu.fsaufer.dm.divinglogapp.R;
import es.upo.alu.fsaufer.dm.divinglogapp.control.db.DiveRepository;
import es.upo.alu.fsaufer.dm.divinglogapp.util.Constant;

/**
 * Clase adaptador para mostrar los elementos de la lista de inmersiones
 */
public class DiveListAdapter extends RecyclerView.Adapter<DiveListAdapter.ViewHolder> implements Serializable {

    private static final long serialVersionUID = 8799654478675716638L;

    private final DiveClickListener diveClickListener;

    private int selectedPosition = RecyclerView.NO_POSITION;
    private View selectedView = null;

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
                if (getAdapterPosition() == RecyclerView.NO_POSITION) return;
                clearSelectedItem();
                selectedPosition = getAdapterPosition();
                notifyItemChanged(selectedPosition);
                selectedView = view;

                diveClickListener.onClick(view, getAdapterPosition());
            }
        }
    }

    public DiveListAdapter(DiveClickListener diveClickListener) {
        this.diveClickListener = diveClickListener;
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
        holder.getPlaceTextView().setText(DiveRepository.getDiveList().get(position).getPlace());
        holder.getDateTextView().setText(DiveRepository.getDiveList().get(position).getFormattedDiveDate());

        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(
                    selectedPosition == position ? Color.DKGRAY :
                            Color.parseColor(Constant.PAR_ROWS_COLOR));
        } else {
            holder.itemView.setBackgroundColor(
                    selectedPosition == position ? Color.DKGRAY :
                            Color.parseColor(Constant.ODD_ROWS_COLOR));
        }

        holder.itemView.setSelected(selectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return DiveRepository.getDiveList().size();
    }

    public void clearSelectedItem() {
        if (selectedView != null) {
            int lastSelectedPosition = selectedPosition;
            selectedPosition = RecyclerView.NO_POSITION;
            selectedView = null;

            notifyItemChanged(lastSelectedPosition);
        }
    }

}
