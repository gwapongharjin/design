package com.m3das.biomech.design;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.m3das.biomech.design.machinedb.Machines;

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
        holder.NameResp.setText(currentMachine.getResName());

        if (currentMachine.getOwnership().contains("Acquired") || currentMachine.getYear_acquired().contains("Acquired") || currentMachine.getCondition_acquired().contains("Acquired") ||
                currentMachine.getRental().contains("Acquired") || currentMachine.getCondition().contains("Acquired")) {
            holder.WarningItem.setVisibility(View.VISIBLE);
        } else {
            holder.WarningItem.setVisibility(View.INVISIBLE);
        }

        if (currentMachine.getMachine_image_base64().contains("Acquired")) {
            holder.ImageItem.setVisibility(View.VISIBLE);
        } else {
            holder.ImageItem.setVisibility(View.INVISIBLE);
        }

        if (currentMachine.getAccuracy().contains("Acquired")) {
            holder.MapItem.setVisibility(View.VISIBLE);
        } else {
            holder.MapItem.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return machinesList.size();
    }

    public void setMachinesList(List<Machines> machines) {

        this.machinesList = machines;
        notifyDataSetChanged();
    }

    public Machines getMachineAt(int position) {
        return machinesList.get(position);
    }

    class MachineHolder extends RecyclerView.ViewHolder {
        private final TextView LatLong, Type, QRCode, NameResp;
        private final ImageView WarningItem, ImageItem, MapItem;


        public MachineHolder(View itemView) {
            super(itemView);
            LatLong = itemView.findViewById(R.id.tvMachineLatLongItem);
            Type = itemView.findViewById(R.id.tvMachineTypeItem);
            QRCode = itemView.findViewById(R.id.tvMacineQRCodeItem);
            NameResp = itemView.findViewById(R.id.tvNameRespitemC);
            WarningItem = itemView.findViewById(R.id.imgWarningMachineListItem);
            MapItem = itemView.findViewById(R.id.imgMapMachineListItem);
            ImageItem = itemView.findViewById(R.id.imgImageMachineListItem);


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
