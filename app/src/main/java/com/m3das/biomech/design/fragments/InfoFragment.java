package com.m3das.biomech.design.fragments;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.m3das.biomech.design.PrivacyAndConsentActivity;
import com.m3das.biomech.design.R;
import com.m3das.biomech.design.viewmodels.InfoViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InfoFragment extends Fragment {

    private InfoViewModel mViewModel;
    private Spinner spinner, spinProfile;
    private TextView tvEduc;
    private Switch aSwitch;
    private EditText addressResp, nameResp, mobileNum, telNum, mobileNum2, telNum2, age, edtSpecifyPofile;
    private RadioButton rbMale, rbFemale;
    Button btnSave;
    private ConstraintLayout constraintLayout;
    private MultiSpinnerSearch multspinOwner, multspinContact;
    public static final String EXTRA_DATE = "EXTRA_DATE";

    public static InfoFragment newInstance() {
        return new InfoFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.info_fragment, container, false);

        spinner = v.findViewById(R.id.spinEduc);
        aSwitch = v.findViewById(R.id.switchEditSave);
        addressResp = v.findViewById(R.id.edtAddress);
        nameResp = v.findViewById(R.id.edtNameResp);
        age = v.findViewById(R.id.edtAGE);
        rbMale = v.findViewById(R.id.radioButton);
        rbFemale = v.findViewById(R.id.radioButton2);
        mobileNum = v.findViewById(R.id.edtMobileNumber);
        telNum = v.findViewById(R.id.edtTelephoneNumber);
        mobileNum2 = v.findViewById(R.id.edtMobileNumber2);
        telNum2 = v.findViewById(R.id.edtTelephoneNumber2);
        constraintLayout = v.findViewById(R.id.constraintLayoutInfo);
        tvEduc = v.findViewById(R.id.tvEduc);
        multspinOwner = v.findViewById(R.id.multspinOwner);
        edtSpecifyPofile = v.findViewById(R.id.edtSpecifyProfile);
        spinProfile = v.findViewById(R.id.spinProfile);
        multspinContact = v.findViewById(R.id.multspinContactNumber);
        btnSave = v.findViewById(R.id.btnClearSave);

        hideByDefaultViews();

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    aSwitch.setText("Saved");
                    spinner.setEnabled(false);
                    spinProfile.setEnabled(false);
                    addressResp.setEnabled(false);
                    nameResp.setEnabled(false);
                    rbMale.setEnabled(false);
                    rbFemale.setEnabled(false);
                    multspinContact.setEnabled(false);
                    mobileNum.setEnabled(false);
                    mobileNum2.setEnabled(false);
                    telNum.setEnabled(false);
                    telNum2.setEnabled(false);
                    age.setEnabled(false);
                    btnSave.setEnabled(false);

                } else {
                    aSwitch.setText("Edit");
                    spinner.setEnabled(true);
                    spinProfile.setEnabled(true);
                    addressResp.setEnabled(true);
                    nameResp.setEnabled(true);
                    rbMale.setEnabled(true);
                    rbFemale.setEnabled(true);
                    multspinContact.setEnabled(true);
                    mobileNum.setEnabled(true);
                    mobileNum2.setEnabled(false);
                    telNum.setEnabled(true);
                    telNum2.setEnabled(true);
                    age.setEnabled(true);
                    btnSave.setEnabled(true);
                }
            }
        });



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                Intent intent = new Intent(getContext(), PrivacyAndConsentActivity.class);
                                startActivity(intent);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();



            }
        });

        spinProfile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                hideOrShowSubProfile(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        multspinContact.setItems(pairingOfList(Arrays.asList(getResources().getStringArray(R.array.contact_number))), new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
                String pos = "";
                for (int i = 0; i < selectedItems.size(); i++) {
                    //Log.d("MULT SPIN", i + " : " + selectedItems.get(i).getName() + " : " + selectedItems.get(i).isSelected());
                    pos = pos + " " + selectedItems.get(i).getName();
                }
                ConstraintLayout.LayoutParams paramsTvEduc = (ConstraintLayout.LayoutParams) tvEduc.getLayoutParams();
                ConstraintLayout.LayoutParams paramsTelNum = (ConstraintLayout.LayoutParams) telNum.getLayoutParams();

                if (pos.contains("TELEPHONE NUMBER") || pos.contains("MOBILE NUMBER")) {
                    if (pos.contains("TELEPHONE NUMBER") && pos.contains("MOBILE NUMBER")) {
                        Log.d("MULT SPIN BOTH", pos);
                        telNum.setVisibility(View.VISIBLE);
                        telNum2.setVisibility(View.VISIBLE);
                        mobileNum.setVisibility(View.VISIBLE);
                        mobileNum2.setVisibility(View.VISIBLE);
                        paramsTvEduc.topToBottom = R.id.edtTelephoneNumber2;
                        paramsTelNum.topToBottom = R.id.edtMobileNumber2;
                    } else {
                        if (pos.contains("TELEPHONE NUMBER")) {
                            Log.d("MULT SPIN TL", pos);
                            telNum.setVisibility(View.VISIBLE);
                            telNum2.setVisibility(View.VISIBLE);
                            mobileNum.setVisibility(View.INVISIBLE);
                            mobileNum2.setVisibility(View.INVISIBLE);
                            paramsTelNum.topToBottom = R.id.multspinContactNumber;
                            paramsTvEduc.topToBottom = R.id.edtTelephoneNumber2;
                        } else if (pos.contains("MOBILE NUMBER")) {
                            Log.d("MULT SPIN MB", pos);
                            mobileNum.setVisibility(View.VISIBLE);
                            mobileNum2.setVisibility(View.VISIBLE);
                            telNum.setVisibility(View.INVISIBLE);
                            telNum2.setVisibility(View.INVISIBLE);
                            paramsTelNum.topToBottom = R.id.edtMobileNumber2;
                            paramsTvEduc.topToBottom = R.id.edtMobileNumber2;
                        }
                    }

                } else if (pos.contains("NOT APPLICABLE")) {
                    Log.d("MULT SPIN NA", pos);
                    mobileNum.setVisibility(View.INVISIBLE);
                    mobileNum2.setVisibility(View.INVISIBLE);
                    telNum.setVisibility(View.INVISIBLE);
                    telNum2.setVisibility(View.INVISIBLE);
                    paramsTelNum.topToBottom = R.id.edtMobileNumber2;
                    paramsTvEduc.topToBottom = R.id.multspinContactNumber;
                } else {
                    Log.d("MULT SPIN ELSE", pos);
                    telNum.setVisibility(View.INVISIBLE);
                    telNum2.setVisibility(View.INVISIBLE);
                    mobileNum.setVisibility(View.INVISIBLE);
                    mobileNum2.setVisibility(View.INVISIBLE);
                    paramsTelNum.topToBottom = R.id.edtMobileNumber2;
                    paramsTvEduc.topToBottom = R.id.multspinContactNumber;

                }
                paramsTvEduc.topMargin = (int) pxFromDp(getContext(), 32);
                paramsTelNum.topMargin = (int) pxFromDp(getContext(), 32);
                telNum.setLayoutParams(paramsTelNum);
                tvEduc.setLayoutParams(paramsTvEduc);
            }
        });

        multspinOwner.setHintText("Select Ownership Type");
        multspinOwner.setItems(

                pairingOfList(Arrays.asList(getResources().

                        getStringArray(R.array.subowner))), new

                        MultiSpinnerListener() {
                            @Override
                            public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
                                for (int i = 0; i < selectedItems.size(); i++) {
                                    //Log.d("MULT SPIN", i + " : " + selectedItems.get(i).getName() + " : " + selectedItems.get(i).isSelected());
                                }
                            }

                        });


        return v;
    }

    public List<KeyPairBoolData> pairingOfList(List<String> stringList) {
        final List<KeyPairBoolData> listArray1 = new ArrayList<>();
        List<String> list = stringList;
        for (int i = 0; i < list.size(); i++) {
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(i + 1);
            h.setName(list.get(i));
            listArray1.add(h);
        }
        return listArray1;
    }

    public void hideOrShowSubProfile(int position) {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) nameResp.getLayoutParams();
        if (spinProfile.getSelectedItemPosition() == 1) {
            multspinOwner.setVisibility(View.VISIBLE);
            edtSpecifyPofile.setVisibility(View.INVISIBLE);
            params.topToBottom = R.id.multspinOwner;

        } else if (spinProfile.getSelectedItemPosition() == 8) {
            multspinOwner.setVisibility(View.INVISIBLE);
            edtSpecifyPofile.setVisibility(View.VISIBLE);
            params.topToBottom = R.id.edtSpecifyProfile;
        } else {
            multspinOwner.setVisibility(View.INVISIBLE);
            edtSpecifyPofile.setVisibility(View.INVISIBLE);
            params.topToBottom = R.id.spinProfile;
        }
        params.topMargin = (int) pxFromDp(getContext(), 32);
        nameResp.setLayoutParams(params);
    }

    private void hideByDefaultViews() {
        mobileNum.setVisibility(View.INVISIBLE);
        telNum.setVisibility(View.INVISIBLE);
        mobileNum2.setVisibility(View.INVISIBLE);
        telNum2.setVisibility(View.INVISIBLE);
        multspinOwner.setVisibility(View.INVISIBLE);
        edtSpecifyPofile.setVisibility(View.INVISIBLE);
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(InfoViewModel.class);
        // TODO: Use the ViewModel
    }


}