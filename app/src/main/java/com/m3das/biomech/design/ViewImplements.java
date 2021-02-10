package com.m3das.biomech.design;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.m3das.biomech.design.implementdb.Implements;
import com.m3das.biomech.design.viewmodels.ImplementViewModel;
import com.m3das.biomech.design.viewmodels.MachineListViewModel;

import java.util.List;

public class ViewImplements extends AppCompatActivity {
    private TextView tvOutput;

    private String impList;
    private ImplementViewModel implementViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_implements_activity);

//        tvOutput = findViewById(R.id.tvListForImplements);

//        impList = "";

        LinearLayout MainLL = findViewById(R.id.viewImpLayout);

        implementViewModel = new ViewModelProvider(this).get(ImplementViewModel.class);

        implementViewModel.getAllImplements().observe(this, new Observer<List<Implements>>() {
            @Override
            public void onChanged(List<Implements> anImplements) {
                for (int i = 0; i <anImplements.size(); i++) {
                    impList = new String();

//                    impList = impList + "Implement ID: " + (i + 1);
                    impList = impList + "Implement QR Code: " + anImplements.get(i).getImplement_qrcode();
                    impList = impList + "\nAttached to Machine: " + anImplements.get(i).getUsed_on_machine();
                    impList = impList + "\nImplement type: " + anImplements.get(i).getImplement_type();

                    TextView text = new TextView(getApplicationContext());
                    text.setTextSize(14);
//                    text.setPaddingRelative(32, 8, 0, 8);
                    text.setBackground(getDrawable(R.drawable.custom_view_data_background));
                    text.setGravity(Gravity.CENTER_VERTICAL);
                    text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    text.setText(impList); // <-- does it really compile without the + sign?
                    MainLL.addView(text);
                }
            }
        });

//        Log.d("DEBIMPL", "of profile List" + impList);
//
//        if (impList.trim().isEmpty()) {
//            tvOutput.setText("No Implements yet");
//        } else {
//            tvOutput.setText(impList);
//        }
    }
}