package com.m3das.biomech.design;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    private EditText addressResp, nameResp, mobileNum, telNum, mobileNum2, telNum2, age, edtSpecifyPofile, edtAreaCode1, edtAreaCode2;
    private TextView tvMobNum1, tvMobNum2, tvTelNum1, tvTelNum2, tvAreaCode1, tvAreaCode2;
    private RadioButton rbMale, rbFemale;
    private Button btnSave;
    private Integer contactNumValue;
    private boolean profileCheck, nameRespCheck, contactNumCheck, sexCheck, ageCheck, addressCheck, educCheck;
    private String sex, ownerType, contactNumType, profileType;
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exitByBackKey() {
        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_profile_activity);

        showPrivacyAndConsent();

        initViews();

        hideByDefaultViews();

        sex = "";
        ownerType = "";
        contactNumValue = 0;

        sex = "";
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
        multspinContact.setHintText("Please Select...");
        multspinContact.setItems(pairingOfList(Arrays.asList(getResources().getStringArray(R.array.contact_number))), new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
                String pos = "";
                for (int i = 0; i < selectedItems.size(); i++) {
                    //Log.d("MULT SPIN", i + " : " + selectedItems.get(i).getName() + " : " + selectedItems.get(i).isSelected());
                    pos = selectedItems.get(i).getName() + " : " + pos;
                }
                contactNum(pos);
                contactNumType = pos;
            }
        });

        multspinOwner.setHintText("Select Ownership Type");

        multspinOwner.setItems(pairingOfList(Arrays.asList(getResources().getStringArray(R.array.subowner))), new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
                String pos = "";
                for (int i = 0; i < selectedItems.size(); i++) {
                    Log.d("MULT SPIN", i + " : " + selectedItems.get(i).getName() + " : " + selectedItems.get(i).isSelected());
                    pos = pos + " : " + selectedItems.get(i).getName();
                    Log.d("POS VALUE", pos);
                }
                ownerType = pos;
                Log.d("POS VAL", ownerType);


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

    private void showPrivacyAndConsent() {
        Log.d("SHOWING", "Privacy and Consent");
        Intent intent = new Intent(getApplicationContext(), PrivacyAndConsentActivity.class);
        startActivity(intent);
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
        btnSave = findViewById(R.id.btnSaveProfile);
        tvMobNum1 = findViewById(R.id.tvPhoneNum1);
        tvMobNum2 = findViewById(R.id.tvPhoneNum2);
        tvTelNum1 = findViewById(R.id.tvTelNum1);
        tvTelNum2 = findViewById(R.id.tvTelNum2);
        tvAreaCode1 = findViewById(R.id.tvAreaCode1);
        tvAreaCode2 = findViewById(R.id.tvAreaCode2);
        edtAreaCode1 = findViewById(R.id.edtAreaCodeTelNum1);
        edtAreaCode2 = findViewById(R.id.edtAreaCodeTelNum2);
    }

    private void contactNum(String string) {
        String pos = string;
        contactNumValue = 0;
        ConstraintLayout.LayoutParams paramsTvEduc = (ConstraintLayout.LayoutParams) tvEduc.getLayoutParams();
        ConstraintLayout.LayoutParams paramsTelNum = (ConstraintLayout.LayoutParams) telNum.getLayoutParams();

        if (pos.contains("TELEPHONE NUMBER") || pos.contains("MOBILE NUMBER")) {
            if (pos.contains("TELEPHONE NUMBER") && pos.contains("MOBILE NUMBER")) {
                Log.d("MULT SPIN BOTH", pos);
                contactNumValue = 4;
                telNum.setVisibility(View.VISIBLE);
                telNum2.setVisibility(View.VISIBLE);
                tvAreaCode1.setVisibility(View.VISIBLE);
                tvAreaCode2.setVisibility(View.VISIBLE);
                edtAreaCode1.setVisibility(View.VISIBLE);
                edtAreaCode2.setVisibility(View.VISIBLE);
                mobileNum.setVisibility(View.VISIBLE);
                mobileNum2.setVisibility(View.VISIBLE);
                tvMobNum1.setVisibility(View.VISIBLE);
                tvMobNum2.setVisibility(View.VISIBLE);
                tvTelNum1.setVisibility(View.VISIBLE);
                tvTelNum2.setVisibility(View.VISIBLE);
                paramsTvEduc.topToBottom = R.id.edtTelephoneNumber2;
                paramsTelNum.topToBottom = R.id.edtMobileNumber2;
            } else {
                if (pos.contains("TELEPHONE NUMBER")) {
                    Log.d("MULT SPIN TL", pos);
                    contactNumValue = 3;
                    telNum.setVisibility(View.VISIBLE);
                    telNum2.setVisibility(View.VISIBLE);
                    tvTelNum1.setVisibility(View.VISIBLE);
                    tvTelNum2.setVisibility(View.VISIBLE);
                    tvAreaCode1.setVisibility(View.VISIBLE);
                    tvAreaCode2.setVisibility(View.VISIBLE);
                    edtAreaCode1.setVisibility(View.VISIBLE);
                    edtAreaCode2.setVisibility(View.VISIBLE);
                    mobileNum.setVisibility(View.GONE);
                    mobileNum2.setVisibility(View.GONE);
                    tvMobNum1.setVisibility(View.GONE);
                    tvMobNum2.setVisibility(View.GONE);
                    paramsTelNum.topToBottom = R.id.multspinContactNumber;
                    paramsTvEduc.topToBottom = R.id.edtTelephoneNumber2;
                } else if (pos.contains("MOBILE NUMBER")) {
                    Log.d("MULT SPIN MB", pos);
                    contactNumValue = 2;
                    mobileNum.setVisibility(View.VISIBLE);
                    mobileNum2.setVisibility(View.VISIBLE);
                    tvMobNum1.setVisibility(View.VISIBLE);
                    tvMobNum2.setVisibility(View.VISIBLE);
                    telNum.setVisibility(View.GONE);
                    telNum2.setVisibility(View.GONE);
                    tvTelNum1.setVisibility(View.GONE);
                    tvTelNum2.setVisibility(View.GONE);
                    tvAreaCode1.setVisibility(View.GONE);
                    tvAreaCode2.setVisibility(View.GONE);
                    edtAreaCode1.setVisibility(View.GONE);
                    edtAreaCode2.setVisibility(View.GONE);
                    paramsTelNum.topToBottom = R.id.edtMobileNumber2;
                    paramsTvEduc.topToBottom = R.id.edtMobileNumber2;
                }
            }

        } else if (pos.contains("NOT APPLICABLE")) {
            Log.d("MULT SPIN NA", pos);
            contactNumValue = 1;
            mobileNum.setVisibility(View.GONE);
            mobileNum2.setVisibility(View.GONE);
            telNum.setVisibility(View.GONE);
            telNum2.setVisibility(View.GONE);
            tvMobNum1.setVisibility(View.GONE);
            tvMobNum2.setVisibility(View.GONE);
            tvTelNum1.setVisibility(View.GONE);
            tvTelNum2.setVisibility(View.GONE);
            tvAreaCode1.setVisibility(View.GONE);
            tvAreaCode2.setVisibility(View.GONE);
            edtAreaCode1.setVisibility(View.GONE);
            edtAreaCode2.setVisibility(View.GONE);
            paramsTelNum.topToBottom = R.id.edtMobileNumber2;
            paramsTvEduc.topToBottom = R.id.multspinContactNumber;
        } else {
            Log.d("MULT SPIN ELSE", pos);
            contactNumValue = 0;
            telNum.setVisibility(View.GONE);
            telNum2.setVisibility(View.GONE);
            mobileNum.setVisibility(View.GONE);
            mobileNum2.setVisibility(View.GONE);
            tvMobNum1.setVisibility(View.GONE);
            tvMobNum2.setVisibility(View.GONE);
            tvTelNum1.setVisibility(View.GONE);
            tvTelNum2.setVisibility(View.GONE);
            tvAreaCode1.setVisibility(View.GONE);
            tvAreaCode2.setVisibility(View.GONE);
            edtAreaCode1.setVisibility(View.GONE);
            edtAreaCode2.setVisibility(View.GONE);
            paramsTelNum.topToBottom = R.id.edtMobileNumber2;
            paramsTvEduc.topToBottom = R.id.multspinContactNumber;

        }
        paramsTvEduc.topMargin = (int) pxFromDp(AddProfile.this, 54);
        paramsTelNum.topMargin = (int) pxFromDp(AddProfile.this, 54);
        telNum.setLayoutParams(paramsTelNum);
        tvEduc.setLayoutParams(paramsTvEduc);

    }

    private void saveProfile() {

        List<String> listIncomplete = new ArrayList<>();

        if (infoCheck()) {
            Intent dataAddProfile = new Intent();

            String timeStamp = new SimpleDateFormat("MMddyyHHmmss").format(new Date());
            String resCode = "";
            String tempStr = nameResp.getText().toString().replaceAll("\\s", "").toUpperCase();


            int id = getIntent().getIntExtra(EXTRA_PROFILE_ID, -1);
            if (id != -1) {

            } else {

                resCode = tempStr.substring(0, 3) + tempStr.substring(Math.max(0, tempStr.length() - 3)) + timeStamp;
                Log.d("Rescode: ", resCode);

                dataAddProfile.putExtra(EXTRA_PROFILE_RESCODE, resCode);
                dataAddProfile.putExtra(EXTRA_PROFILE, spinProfile.getSelectedItem().toString().toUpperCase());
                dataAddProfile.putExtra(EXTRA_PROFILE_SPECIFY, edtSpecifyPofile.getText().toString().toUpperCase());
                dataAddProfile.putExtra(EXTRA_PROFILE_OWNER_TYPE, ownerType);
                dataAddProfile.putExtra(EXTRA_PROFILE_NAME, nameResp.getText().toString().toUpperCase());
                dataAddProfile.putExtra(EXTRA_PROFILE_ADDRESS, addressResp.getText().toString().toUpperCase());
                dataAddProfile.putExtra(EXTRA_PROFILE_AGE, age.getText().toString().toUpperCase());
                dataAddProfile.putExtra(EXTRA_PROFILE_SEX, sex.toUpperCase());
                dataAddProfile.putExtra(EXTRA_PROFILE_CONTACT_INFO, contactNumType);
                dataAddProfile.putExtra(EXTRA_PROFILE_MOB_NUM1, mobileNum.getText().toString().toUpperCase());
                dataAddProfile.putExtra(EXTRA_PROFILE_MOB_NUM2, mobileNum2.getText().toString().toUpperCase());
                dataAddProfile.putExtra(EXTRA_PROFILE_TEL_NUM1, edtAreaCode1.getText().toString() + " - " + telNum.getText().toString());
                dataAddProfile.putExtra(EXTRA_PROFILE_TEL_NUM2, edtAreaCode2.getText().toString() + " - " + telNum2.getText().toString());
                dataAddProfile.putExtra(EXTRA_PROFILE_EDUC_ATTAIN, spinEduc.getSelectedItem().toString().toUpperCase());
            }

            setResult(RESULT_OK, dataAddProfile);
            finish();
        } else {

            if (!profileCheck) {
                listIncomplete.add("Profile");
            }
            if (!nameRespCheck) {
                listIncomplete.add("Name of Respondent");
            }
            if (!addressCheck) {
                listIncomplete.add("Address");
            }
            if (!ageCheck) {
                listIncomplete.add("Age");
            }
            if (!sexCheck) {
                listIncomplete.add("Sex");
            }
            if (!contactNumCheck) {
                listIncomplete.add("Contact Number/s");
            }
            if (!educCheck) {
                listIncomplete.add("Educational Attainment");
            }
            String inc = "";
            for (int i = 0; i < listIncomplete.size(); i++) {
                inc = inc + listIncomplete.get(i) + "\n";
            }
            Toast.makeText(this, "Incomplete Data!\nPlease Check\n\n" + inc, Toast.LENGTH_LONG).show();
            Log.d("Prof Check", String.valueOf(listIncomplete) + ": " + inc);

        }


    }

    private boolean infoCheck() {

        switch (spinProfile.getSelectedItemPosition()) {
            default:
                profileCheck = true;
                break;
            case 1:
                profileCheck = ownerType.length() >= 3;
                break;
            case 8:
                profileCheck = !isNullOrEmpty(edtSpecifyPofile.getText().toString());
                break;
            case 0:
                profileCheck = false;
                break;
        }

        nameRespCheck = !isNullOrEmpty(nameResp.getText().toString()) && nameResp.length() >= 6;

        addressCheck = !isNullOrEmpty(addressResp.getText().toString());

        ageCheck = !isNullOrEmpty(age.getText().toString());

        sexCheck = !isNullOrEmpty(sex);

        switch (contactNumValue) {
            case 1:
                contactNumCheck = true;
                break;
            case 2:
                contactNumCheck = !isNullOrEmpty(mobileNum.getText().toString()) && mobileNum.length() >= 11;
                break;
            case 3:
                contactNumCheck = !isNullOrEmpty(edtAreaCode1.getText().toString()) && !isNullOrEmpty(telNum.getText().toString()) && edtAreaCode1.length() >= 2 && telNum.length() >= 7;
                break;
            case 4:
                contactNumCheck = !isNullOrEmpty(mobileNum.getText().toString()) && !isNullOrEmpty(edtAreaCode1.getText().toString()) && !isNullOrEmpty(telNum.getText().toString()) && edtAreaCode1.length() >= 2 && telNum.length() >= 7;
                break;
            default:
            case 0:
                contactNumCheck = false;
                break;
        }

        educCheck = spinEduc.getSelectedItemPosition() != 0;

        Log.d("Prof Check", "Profile: " + profileCheck);
        Log.d("Prof Check", "Name: " + nameRespCheck);
        Log.d("Prof Check", "Address: " + addressCheck);
        Log.d("Prof Check", "Age: " + ageCheck);
        Log.d("Prof Check", "Sex: " + sexCheck);
        Log.d("Prof Check", "Contact: " + contactNumCheck);
        Log.d("Prof Check", "Education: " + educCheck);

        return (profileCheck && nameRespCheck && addressCheck && ageCheck && sexCheck && contactNumCheck && educCheck);

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
            edtSpecifyPofile.setVisibility(View.GONE);
            params.topToBottom = R.id.multspinOwner;

        } else if (spinProfile.getSelectedItemPosition() == 8) {
            multspinOwner.setVisibility(View.GONE);
            edtSpecifyPofile.setVisibility(View.VISIBLE);
            params.topToBottom = R.id.edtSpecifyProfile;
        } else {
            multspinOwner.setVisibility(View.GONE);
            edtSpecifyPofile.setVisibility(View.GONE);
            params.topToBottom = R.id.spinProfile;
        }
        profileType = spinProfile.getSelectedItem().toString();
        params.topMargin = (int) pxFromDp(AddProfile.this, 54);
        nameResp.setLayoutParams(params);
    }

    private void hideByDefaultViews() {
        mobileNum.setVisibility(View.GONE);
        telNum.setVisibility(View.GONE);
        mobileNum2.setVisibility(View.GONE);
        telNum2.setVisibility(View.GONE);
        multspinOwner.setVisibility(View.GONE);
        edtSpecifyPofile.setVisibility(View.GONE);
        tvMobNum1.setVisibility(View.GONE);
        tvMobNum2.setVisibility(View.GONE);
        tvTelNum1.setVisibility(View.GONE);
        tvTelNum2.setVisibility(View.GONE);
        tvAreaCode1.setVisibility(View.GONE);
        tvAreaCode2.setVisibility(View.GONE);
        edtAreaCode1.setVisibility(View.GONE);
        edtAreaCode2.setVisibility(View.GONE);
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

}