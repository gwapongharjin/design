package com.m3das.biomech.design;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.m3das.biomech.design.implementdb.Implements;

import java.util.ArrayList;
import java.util.List;

public class ImplementAdapter extends RecyclerView.Adapter<ImplementAdapter.ImplementHolder> {
    private List<Implements> implementsArrayList = new ArrayList<>();
    private ImplementAdapter.OnItemClickListener listener;

    @NonNull
    @Override
    public ImplementAdapter.ImplementHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.implement_item, parent, false);

        return new ImplementAdapter.ImplementHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImplementAdapter.ImplementHolder holder, int position) {
        Implements currentImplement = implementsArrayList.get(position);
        holder.Type.setText(currentImplement.getImplement_type());
        holder.ImpQr.setText(currentImplement.getImplement_qrcode());
        holder.LatLong.setText(currentImplement.getLatitude() + ", " + currentImplement.getLongitude());
        holder.MachineAttachedto.setText(currentImplement.getUsed_on_machine());

        if (currentImplement.getOwnership().contains("Acquired") || currentImplement.getYear_acquired().contains("Acquired") || currentImplement.getCondition().contains("Acquired") ||
                currentImplement.getCondition_present().contains("Acquired") || currentImplement.getAccuracy().contains("Acquired") || currentImplement.getImage_base64().contains("Acquired")) {
            holder.WarningItem.setVisibility(View.VISIBLE);
        } else {
            holder.WarningItem.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return implementsArrayList.size();
    }

    public void setImplementsArrayListList(List<Implements> implementsArrayList) {
        this.implementsArrayList = implementsArrayList;
        notifyDataSetChanged();
    }

    public Implements getImplement(int position) {
        return implementsArrayList.get(position);
    }

    class ImplementHolder extends RecyclerView.ViewHolder {

        private final TextView Type, ImpQr, LatLong, MachineAttachedto;
        private final ImageView WarningItem;

        public ImplementHolder(@NonNull View itemView) {
            super(itemView);

            Type = itemView.findViewById(R.id.tvImplementTypeItem);
            LatLong = itemView.findViewById(R.id.tvImplementLatLongItem);
            ImpQr = itemView.findViewById(R.id.tvImplmentQRCodeItem);
            MachineAttachedto = itemView.findViewById(R.id.tvAttachedToMachineItem);
            WarningItem = itemView.findViewById(R.id.imgWarningImplementItem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(implementsArrayList.get(position));
                    }
                }
            });

        }
    }

    public interface OnItemClickListener {
        void onItemClick(Implements implement);
    }

    public void setOnItemClickListener(ImplementAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
