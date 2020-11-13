package com.m3das.biomech.design;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.m3das.biomech.design.machinedb.Machines;
import com.m3das.biomech.design.profiledb.Profile;

import java.util.ArrayList;
import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileHolder> {

    private List<Profile> profileList = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public ProfileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_item, parent, false);

        return new ProfileHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileHolder holder, int position) {
        Profile currentProfile = profileList.get(position);
        holder.Name.setText(currentProfile.getName_respondent());
        holder.Location.setText(currentProfile.getAddress());
    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }
    public void setProfileList(List<Profile> profileList) {
        this.profileList = profileList;
        notifyDataSetChanged();
    }
    public Profile getProfileAt(int position){
        return profileList.get(position);
    }

    class  ProfileHolder extends RecyclerView.ViewHolder{

    private final TextView Name, Location;

    public ProfileHolder(@NonNull View itemView) {
        super(itemView);

        Name = itemView.findViewById(R.id.tvProfileNameItem);
        Location = itemView.findViewById(R.id.tvProfileLocationItem);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(profileList.get(position));
                }
            }
        });

    }
}public interface OnItemClickListener {
        void onItemClick(Profile profile);
    }

    public void setOnItemClickListener(ProfileAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
