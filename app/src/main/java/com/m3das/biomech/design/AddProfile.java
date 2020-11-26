package com.m3das.biomech.design;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AddProfile extends AppCompatActivity {

    private Spinner spinEduc, spinProfile;
    private TextView tvEduc;
    private EditText addressResp, nameResp, mobileNum, telNum, mobileNum2, telNum2, age, edtSpecifyPofile;
    private RadioButton rbMale, rbFemale;
    private ImageButton btnSave;
    private String sex;
    private ConstraintLayout constraintLayout;
    private MultiSpinnerSearch multspinOwner, multspinContact;
    public static final String EXTRA_PROFILE_ID = "ADDPROFILE_EXTRA_ID";
    public static final String EXTRA_PROFILE_RESCODE = "ADDPROFILE_EXTRA_RESCODE";
    public static final String EXTRA_PROFILE = "ADDPROFILE_EXTRA_PROFILE";
    public static final String EXTRA_PROFILE_SPECIFY = "ADDPROFILE_EXTRA_PROFILE_SPECIFY";
    public static final String EXTRA_PROFILE_OWNER_TYPE = "ADDPROFILE_EXTRA_OWNER_TYPE";
    public static final String EXTRA_PROFILE_NAME = "ADDPROFILE_EXTRA_NAME";
    public static final String EXTRA_PROFILE_ADDRESS = "ADDPROFILE_EXTRA_ADDRESS";
    public static final String EXTRA_PROFILE_AGE = "ADDPROFILE_EXTRA_AGE";
    public static final String EXTRA_PROFILE_SEX = "ADDPROFILE_EXTRA_SEX";
    public static final String EXTRA_PROFILE_CONTACT_INFO = "ADDPROFILE_EXTRA_CONTACT_INFO";
    public static final String EXTRA_PROFILE_MOB_NUM1 = "ADDPROFILE_EXTRA_MOB_NUM1";
    public static final String EXTRA_PROFILE_MOB_NUM2 = "ADDPROFILE_EXTRA_MOB_NUM2";
    public static final String EXTRA_PROFILE_TEL_NUM1 = "ADDPROFILE_EXTRA_TEL_NUM1";
    public static final String EXTRA_PROFILE_TEL_NUM2 = "ADDPROFILE_EXTRA_TEL_NUM2";
    public static final String EXTRA_PROFILE_EDUC_ATTAIN = "ADDPROFILE_EXTRA_EDUC_ATTAIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_profile_activity);

        initViews();

        hideByDefaultViews();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveProfile();
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
                paramsTvEduc.topMargin = (int) pxFromDp(AddProfile.this, 32);
                paramsTelNum.topMargin = (int) pxFromDp(AddProfile.this, 32);
                telNum.setLayoutParams(paramsTelNum);
                tvEduc.setLayoutParams(paramsTvEduc);
            }
        });

        multspinOwner.setHintText("Select Ownership Type");

        multspinOwner.setItems(pairingOfList(Arrays.asList(getResources().getStringArray(R.array.subowner))), new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
                for (int i = 0; i < selectedItems.size(); i++) {
                    //Log.d("MULT SPIN", i + " : " + selectedItems.get(i).getName() + " : " + selectedItems.get(i).isSelected());
                }
            }

        });

        rbMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbMale.isChecked()) {
                    sex = "MALE";
                } else {
                    sex = "";
                }
            }
        });
        rbFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbFemale.isChecked()) {
                    sex = "FEMALE";
                } else {
                    sex = "";
                }
            }
        });


    }

    private void initViews() {

        spinEduc = findViewById(R.id.spinEduc);
        addressResp = findViewById(R.id.edtAddress);
        nameResp = findViewById(R.id.edtNameResp);
        age = findViewById(R.id.edtAGE);
        rbMale = findViewById(R.id.radioButton);
        rbFemale = findViewById(R.id.radioButton2);
        mobileNum = findViewById(R.id.edtMobileNumber);
        telNum = findViewById(R.id.edtTelephoneNumber);
        mobileNum2 = findViewById(R.id.edtMobileNumber2);
        telNum2 = findViewById(R.id.edtTelephoneNumber2);
        constraintLayout = findViewById(R.id.constraintLayoutInfo);
        tvEduc = findViewById(R.id.tvEduc);
        multspinOwner = findViewById(R.id.multspinOwner);
        edtSpecifyPofile = findViewById(R.id.edtSpecifyProfile);
        spinProfile = findViewById(R.id.spinProfile);
        multspinContact = findViewById(R.id.multspinContactNumber);
        btnSave = findViewById(R.id.btnSaveProfileIcon);
    }

    private void saveProfile() {

        Intent dataAddProfile = new Intent();

        String timeStamp = new SimpleDateFormat("MMddyyHHmmss").format(new Date());
        String resCode = "";
        String tempStr = nameResp.getText().toString().replaceAll("\\s", "").toUpperCase();


        int id = getIntent().getIntExtra(EXTRA_PROFILE_ID, -1);
        if (id != -1) {

        }
        else {

            resCode = tempStr.substring(0, 3) + tempStr.substring(Math.max(0, tempStr.length() - 3)) + timeStamp;
            Log.d("Rescode: ", resCode);

            if(isNullOrEmpty(sex)){
                sex = "null";
            }

            dataAddProfile.putExtra(EXTRA_PROFILE_RESCODE, resCode);
            dataAddProfile.putExtra(EXTRA_PROFILE, spinProfile.getSelectedItem().toString().toUpperCase());
            dataAddProfile.putExtra(EXTRA_PROFILE_SPECIFY, edtSpecifyPofile.getText().toString().toUpperCase());
            dataAddProfile.putExtra(EXTRA_PROFILE_OWNER_TYPE, multspinOwner.getSelectedItem().toString().toUpperCase());//TODO get multspin
            dataAddProfile.putExtra(EXTRA_PROFILE_NAME, nameResp.getText().toString().toUpperCase());
            dataAddProfile.putExtra(EXTRA_PROFILE_ADDRESS, addressResp.getText().toString().toUpperCase());
            dataAddProfile.putExtra(EXTRA_PROFILE_AGE, age.getText().toString().toUpperCase());
            dataAddProfile.putExtra(EXTRA_PROFILE_SEX, sex.toUpperCase());
            dataAddProfile.putExtra(EXTRA_PROFILE_CONTACT_INFO, multspinContact.getSelectedItem().toString().toUpperCase());//TODO get multspin
            dataAddProfile.putExtra(EXTRA_PROFILE_MOB_NUM1, mobileNum.getText().toString().toUpperCase());
            dataAddProfile.putExtra(EXTRA_PROFILE_MOB_NUM2, mobileNum2.getText().toString().toUpperCase());
            dataAddProfile.putExtra(EXTRA_PROFILE_TEL_NUM1, telNum.getText().toString().toUpperCase());
            dataAddProfile.putExtra(EXTRA_PROFILE_TEL_NUM2, telNum2.getText().toString().toUpperCase());
            dataAddProfile.putExtra(EXTRA_PROFILE_EDUC_ATTAIN, spinEduc.getSelectedItem().toString().toUpperCase());

        }

        setResult(RESULT_OK, dataAddProfile);
        finish();

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

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
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
        params.topMargin = (int) pxFromDp(AddProfile.this, 32);
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
}