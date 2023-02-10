package com.example.socialgaming.utils;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.socialgaming.R;
import com.example.socialgaming.data.BuildFirestore;

public class BubbleUtils {

    @SuppressLint("SetTextI18n")
    public static void setupInformation(BuildFirestore b, ConstraintLayout information, View templateView) {

        int elements = 8;

        TextView[] textViewsTitle = new TextView[elements];
        TextView[] textViewsPrice = new TextView[elements];

        textViewsTitle[0] = information.findViewById(R.id.title1);
        textViewsPrice[0] = information.findViewById(R.id.price1);
        textViewsTitle[1] = information.findViewById(R.id.title2);
        textViewsPrice[1] = information.findViewById(R.id.price2);
        textViewsPrice[2] = information.findViewById(R.id.price3);
        textViewsPrice[3] = information.findViewById(R.id.price4);
        textViewsTitle[4] = information.findViewById(R.id.title5);
        textViewsPrice[4] = information.findViewById(R.id.price5);
        textViewsTitle[5] = information.findViewById(R.id.title6);
        textViewsPrice[5] = information.findViewById(R.id.price6);
        textViewsTitle[6] = information.findViewById(R.id.title7);
        textViewsPrice[6] = information.findViewById(R.id.price7);
        textViewsTitle[7] = information.findViewById(R.id.title8);
        textViewsPrice[7] = information.findViewById(R.id.price8);

        textViewsTitle[0].setText("Motherboard: \n" + b.getBoardTitle());
        textViewsPrice[0].setText("€ " + b.getBoardPrice());
        textViewsTitle[1].setText("CPU: \n" + b.getCpuTitle());
        textViewsPrice[1].setText("€ " + b.getCpuPrice());
        textViewsPrice[2].setText(b.getRamsTotalDimension() + " GB");
        textViewsPrice[3].setText(b.getMemoriesTotalDimension() + " GB");
        textViewsTitle[4].setText("GPU: \n" + b.getGpuTitle());
        textViewsPrice[4].setText("€ " + b.getGpuPrice());
        textViewsTitle[5].setText("Case: \n" + b.getHouseTitle());
        textViewsPrice[5].setText("€ " + b.getHousePrice());
        textViewsTitle[6].setText("Power Supply Unit: \n" + b.getPsuTitle());
        textViewsPrice[6].setText("€ " + b.getPsuPrice());
        textViewsTitle[7].setText("CPU Fan: \n" + b.getFanTitle());
        textViewsPrice[7].setText("€ " + b.getFanPrice());

        TextView formFactor = information.findViewById(R.id.formFactorValue);
        formFactor.setText(b.getFormFactor());
        TextView chipset = information.findViewById(R.id.chipsetValue);
        chipset.setText(b.getChipset());
        TextView cpuClock = information.findViewById(R.id.cpuClockSpeedValue);
        cpuClock.setText(b.getSpeedCpu());
        TextView gpuSpeed = information.findViewById(R.id.gpuSpeedValue);
        gpuSpeed.setText(b.getSpeedGpu());
        TextView gpuMemory = information.findViewById(R.id.gpuMemoryValue);
        gpuMemory.setText(b.getMemoryGpu());
        TextView power = information.findViewById(R.id.psuPowerValue);
        power.setText(b.getPowerPsu());
        TextView rpm = information.findViewById(R.id.fanRpmValue);
        rpm.setText(b.getRpmFan());

        TextView rams = information.findViewById(R.id.ram1);
        rams.setText(b.getRamsTitle().get(0) +
                "\n\t\tQuantity:" +
                "\n\t\tSize:" +
                "\n\t\tType:");
        for(int i = 1; i < b.getRamsTitle().size(); i++) {
            rams.setText(rams.getText() +
                    "\n" + b.getRamsTitle().get(i) +
                    "\n\t\tQuantity:" +
                    "\n\t\tSize:" +
                    "\n\t\tType:");
        }

        TextView ramsValues = information.findViewById(R.id.ram1Price);
        ramsValues.setText("€" + b.getRamsPrice().get(0) +
                "\n\t\t" + b.getQuantityRams().get(0) +
                "\n\t\t" + b.getSizeRams().get(0) + " GB" +
                "\n\t\t" + b.getRamsType().get(0));

        for(int i = 1; i < b.getRamsTitle().size(); i++) {
            ramsValues.setText(ramsValues.getText() +
                    "\n€" + b.getRamsPrice().get(i) +
                    "\n" + b.getQuantityRams().get(i) +
                    "\n" + b.getSizeRams().get(i) + " GB" +
                    "\n" + b.getRamsType().get(i));
        }

        TextView hds = information.findViewById(R.id.memory1);
        hds.setText(b.getRamsTitle().get(0) +
                "\n\t\tType:" +
                "\n\t\tSize:");
        for(int i = 1; i < b.getMemoriesTitle().size(); i++) {
            hds.setText(hds.getText() +
                    "\n" + b.getMemoriesTitle().get(i) +
                    "\n\t\tType:" +
                    "\n\t\tSize:");
        }

        TextView hdsValues = information.findViewById(R.id.memory1Price);
        hdsValues.setText("€" + b.getMemoriesPrice().get(0) +
                "\n\t\t" + b.getMemoriesType().get(0) +
                "\n\t\t" + b.getMemoriesDimension().get(0) + " GB");

        for(int i = 1; i < b.getRamsTitle().size(); i++) {
            hdsValues.setText(hdsValues.getText() +
                    "\n€" + b.getMemoriesPrice().get(i) +
                    "\n\t\t" + b.getMemoriesType().get(i) +
                    "\n\t\t" + b.getMemoriesDimension().get(i) + " GB");
        }

        TextView value = templateView.findViewById(R.id.value);
        value.setText(b.getValue() + "");

    }

    public static void setBubbleListener(View view, ConstraintLayout layout) {

        ViewGroup.LayoutParams params1 = layout.getLayoutParams();
        params1.height = 0;

        layout.setLayoutParams(params1);
        layout.requestLayout();

        view.setOnClickListener(view1 -> {

            if (params1.height == 0) {
                params1.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
            }
            else {
                params1.height = 0;
            }
            layout.setLayoutParams(params1);
            layout.requestLayout();

        });

    }

}
