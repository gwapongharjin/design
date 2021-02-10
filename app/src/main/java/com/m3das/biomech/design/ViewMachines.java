package com.m3das.biomech.design;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.m3das.biomech.design.machinedb.Machines;
import com.m3das.biomech.design.viewmodels.MachineListViewModel;

import java.util.List;

public class ViewMachines extends AppCompatActivity {
    private TextView tvOutput;
    private String machList;
    private MachineListViewModel machineListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_machines_activity);

        LinearLayout MainLL = findViewById(R.id.viewMachLayout);
        machineListViewModel = new ViewModelProvider(this).get(MachineListViewModel.class);

        machineListViewModel.getAllMachines().observe(this, new Observer<List<Machines>>() {
            @Override
            public void onChanged(List<Machines> machines) {
                for (int i = 0; i < machines.size(); i++) {
                    machList = new String();

                    machList = machList + "Machine QR Code: " + machines.get(i).getMachine_qrcode();
                    machList = machList + "\nRespondent for Machine: " + machines.get(i).getResName();
                    machList = machList + "\nMachine type: " + machines.get(i).getMachine_type();

                    TextView text = new TextView(getApplicationContext());
                    text.setTextSize(14);
//                    text.setPaddingRelative(32, 8, 0, 8);
                    text.setBackground(getDrawable(R.drawable.custom_view_data_background));
                    text.setGravity(Gravity.CENTER_VERTICAL);
                    text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    text.setText(machList); // <-- does it really compile without the + sign?
                    MainLL.addView(text);
                }
            }
        });
    }
}