package com.m3das.biomech.design;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MachineAdapter extends RecyclerView.Adapter<MachineAdapter.MachineHolder> {

    private List<Machines> machinesList = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public MachineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.machine_item, parent, false);

        return new MachineHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MachineHolder holder, int position) {
        Machines currentMachine = machinesList.get(position);
        holder.Type.setText(currentMachine.getMachine_type());
        holder.QRCode.setText(currentMachine.getMachine_qrcode());
        holder.LatLong.setText(currentMachine.getMachine_latitude() + ", " + currentMachine.getMachine_longitude());
    }

    @Override
    public int getItemCount() {
        return machinesList.size();
    }

    public void setMachinesList(List<Machines> machines) {

        this.machinesList = machines;
        notifyDataSetChanged();
    }

    class MachineHolder extends RecyclerView.ViewHolder {
        private TextView LatLong, Type, QRCode;

        public MachineHolder(View itemView) {
            super(itemView);
            LatLong = itemView.findViewById(R.id.tvLatLongitemC);
            Type = itemView.findViewById(R.id.tvMachineTypeitemC);
            QRCode = itemView.findViewById(R.id.tvMachineCodeitemC);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(machinesList.get(position));
                    }
                }
            });

        }

    }

    public interface OnItemClickListener {
        void onItemClick(Machines machines);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
