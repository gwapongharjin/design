package com.m3das.biomech.design.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.m3das.biomech.design.AddImplementActivity;
import com.m3das.biomech.design.AddMachineActivity;
import com.m3das.biomech.design.AddProfile;
import com.m3das.biomech.design.PrivacyAndConsentActivity;
import com.m3das.biomech.design.ProfileAdapter;
import com.m3das.biomech.design.R;
import com.m3das.biomech.design.Variable;
import com.m3das.biomech.design.implementdb.Implements;
import com.m3das.biomech.design.profiledb.Profile;
import com.m3das.biomech.design.viewmodels.ProfileViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private FloatingActionButton fabAddNewProfile, fabDeleteProfile;
    private RecyclerView recyclerView;
    public static final int ADD_PROFILE_REQUEST = 279;
    public static final int EDIT_PROFILE_REQUEST = 927;
    private int num;
    private ProfileViewModel profileViewModel;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.profile_fragment, container, false);
        num = 3;
        fabAddNewProfile = v.findViewById(R.id.floatingActionButtonAddProfile);
        fabDeleteProfile = v.findViewById(R.id.floatingActionButtonDeleteProfile);

        recyclerView = v.findViewById(R.id.recyclerViewProfileList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);

        ProfileAdapter profileAdapter = new ProfileAdapter();
        recyclerView.setAdapter(profileAdapter);

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);


        profileViewModel.getAllProfiles().observe(getActivity(), new Observer<List<Profile>>() {
            @Override
            public void onChanged(List<Profile> profiles) {
//                String profileList = "";
//                for (int i = profiles.size() - 1; i > -1; i--) {
//                    profileList = profileList + "Profile ID: " + profiles.get(i).getId();
//                    profileList = profileList + "\nName of Respondent:" + profiles.get(i).getName_respondent() + "\n\n";
//
//                }

                ArrayList<String> profileListAfterSet = new ArrayList<>();
                for (int i = 0; i < profiles.size(); i++) {
                    profileListAfterSet.add(profiles.get(i).getResCode());
                }


                Variable.setListResCode(profileListAfterSet);


//                Variable.setProfileList(profileList);

                profileAdapter.setProfileList(profiles);
            }
        });

        fabAddNewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddProfile.class);
                startActivityForResult(intent, ADD_PROFILE_REQUEST);
            }
        });

        fabDeleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = num + 1;

                if (num % 2 == 0) {
                    fabDeleteProfile.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
                    fabDeleteProfile.setRippleColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    Toast.makeText(getContext(), "Now you can delete items by swiping", Toast.LENGTH_SHORT).show();

                } else {

                    fabDeleteProfile.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    fabDeleteProfile.setRippleColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));

                }

                Log.d("Value of Num CLICK", String.valueOf(num));
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Deleting Item")
                        .setMessage("You will be deleting this: " + profileAdapter.getProfileAt(viewHolder.getAdapterPosition()).getName_respondent())
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                profileViewModel.delete(profileAdapter.getProfileAt(viewHolder.getAdapterPosition()));
                            }

                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                profileAdapter.notifyDataSetChanged();
                            }
                        });
                builder.show();

            }

            @Override
            public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                Log.d("Value of Num ITH", String.valueOf(num));
                if (num % 2 != 0) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }
        }).attachToRecyclerView(recyclerView);
        return v;
    }

    public void addNewProfile() {
        Intent intent = new Intent(getContext(), PrivacyAndConsentActivity.class);
        startActivityForResult(intent, ADD_PROFILE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_PROFILE_REQUEST && resultCode == Activity.RESULT_OK) {

            String resCode = data.getStringExtra(AddProfile.EXTRA_PROFILE_RESCODE);
            String profileofres = data.getStringExtra(AddProfile.EXTRA_PROFILE);
            String profile_specify = data.getStringExtra(AddProfile.EXTRA_PROFILE_SPECIFY);
            String owner_type = data.getStringExtra(AddProfile.EXTRA_PROFILE_OWNER_TYPE);
            String name_respondent = data.getStringExtra(AddProfile.EXTRA_PROFILE_NAME);
            String address = data.getStringExtra(AddProfile.EXTRA_PROFILE_ADDRESS);
            String age = data.getStringExtra(AddProfile.EXTRA_PROFILE_AGE);
            String sex = data.getStringExtra(AddProfile.EXTRA_PROFILE_SEX);
            String contact_number = data.getStringExtra(AddProfile.EXTRA_PROFILE_CONTACT_INFO);
            String mobnum1 = data.getStringExtra(AddProfile.EXTRA_PROFILE_MOB_NUM1);
            String mobnum2 = data.getStringExtra(AddProfile.EXTRA_PROFILE_MOB_NUM2);
            String telnum1 = data.getStringExtra(AddProfile.EXTRA_PROFILE_TEL_NUM1);
            String telnum2 = data.getStringExtra(AddProfile.EXTRA_PROFILE_TEL_NUM2);
            String educational_attainment = data.getStringExtra(AddProfile.EXTRA_PROFILE_EDUC_ATTAIN);

            Profile profile = new Profile(resCode, profileofres, profile_specify, owner_type, name_respondent,
                    address, age, sex, contact_number, mobnum1, mobnum2, telnum1, telnum2, educational_attainment);

            profileViewModel.insert(profile);
        } else if (requestCode == EDIT_PROFILE_REQUEST && resultCode == Activity.RESULT_OK) {
            int id = data.getIntExtra(AddProfile.EXTRA_PROFILE_ID, -1);
            if (id == -1) {
                Toast.makeText(getActivity(), "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String resCode = data.getStringExtra(AddProfile.EXTRA_PROFILE_RESCODE);
            String profileofres = data.getStringExtra(AddProfile.EXTRA_PROFILE);
            String profile_specify = data.getStringExtra(AddProfile.EXTRA_PROFILE_SPECIFY);
            String owner_type = data.getStringExtra(AddProfile.EXTRA_PROFILE_OWNER_TYPE);
            String name_respondent = data.getStringExtra(AddProfile.EXTRA_PROFILE_NAME);
            String address = data.getStringExtra(AddProfile.EXTRA_PROFILE_ADDRESS);
            String age = data.getStringExtra(AddProfile.EXTRA_PROFILE_AGE);
            String sex = data.getStringExtra(AddProfile.EXTRA_PROFILE_SEX);
            String contact_number = data.getStringExtra(AddProfile.EXTRA_PROFILE_CONTACT_INFO);
            String mobnum1 = data.getStringExtra(AddProfile.EXTRA_PROFILE_MOB_NUM1);
            String mobnum2 = data.getStringExtra(AddProfile.EXTRA_PROFILE_MOB_NUM2);
            String telnum1 = data.getStringExtra(AddProfile.EXTRA_PROFILE_TEL_NUM1);
            String telnum2 = data.getStringExtra(AddProfile.EXTRA_PROFILE_TEL_NUM2);
            String educational_attainment = data.getStringExtra(AddProfile.EXTRA_PROFILE_EDUC_ATTAIN);

            Profile profile = new Profile(resCode, profileofres, profile_specify, owner_type, name_respondent,
                    address, age, sex, contact_number,mobnum1, mobnum2,telnum1, telnum2, educational_attainment );

            profile.setId(id);
            profileViewModel.update(profile);

            Toast.makeText(getActivity(), "Note updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Note not saved", Toast.LENGTH_SHORT).show();
            Log.d("Is note saved", "Note Not Saved");
        }

    }
}