package com.example.socialgaming.utils;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.socialgaming.R;
import com.example.socialgaming.data.BuildFirestore;
import com.example.socialgaming.data.User;
import com.example.socialgaming.repository.user.UserRepository;
import com.example.socialgaming.ui.home.HomeFragment;
import com.example.socialgaming.ui.profile.ProfileFragment;
import com.example.socialgaming.view.MainActivity;

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

    public static void setBuildBubble(BuildFirestore b, User user, Fragment fragment, LinearLayout buildList) {

        MainActivity activity = (MainActivity) fragment.getActivity();
        LayoutInflater inflater = LayoutInflater.from(buildList.getContext());
        // Inflate il layout incluso (template.xml)
        View templateView = inflater.inflate(R.layout.bubble_template, null);

        // Imposta i parametri richiesti sulla vista inflata
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 16, 0, 16);

        ImageView image = templateView.findViewById(R.id.buildImage);
        image.setImageBitmap(b.getImage());
        image.setMaxWidth(125);
        image.setMaxHeight(125);


        TextView name = templateView.findViewById(R.id.nameBuild);
        name.setText(b.getName());

        TextView creator = templateView.findViewById(R.id.creator);
        creator.setText(b.getCreator());

        TextView rate = templateView.findViewById(R.id.value);
        rate.setText(b.getValue() + "");

        if(b.getCreator().equals(user.getUsername())) {
            ImageView save = templateView.findViewById(R.id.saveBuild);
            save.setForeground(activity.getResources().getDrawable(R.drawable.minus, fragment.getActivity().getTheme()));
            save.setForegroundTintList(activity.colorDark);

            save.setOnClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Are you sure you want to delete this build?")
                        .setPositiveButton("Yes", (dialog, id) -> {
                            Toast.makeText(activity, b.getName() + "\nSuccessfully deleted!", Toast.LENGTH_SHORT).show();
                            user.getCreated().remove(b.getUuid());
                            if(fragment instanceof HomeFragment) {
                                HomeFragment homeFragment = (HomeFragment) fragment;
                                homeFragment.getHomeViewModel().getBuildRepository().deleteBuild(b);
                                homeFragment.getHomeViewModel().getUserRepository().removeBuildsUpdate(b, user, fragment.getContext(), "Build successfully deleted");
                            }
                            else if (fragment instanceof ProfileFragment){
                                ProfileFragment profileFragment = (ProfileFragment) fragment;
                                profileFragment.getProfileViewModel().getBuildRepository().deleteBuild(b);
                                profileFragment.getProfileViewModel().getUserRepository().removeBuildsUpdate(b, user, fragment.getContext(), "Build successfully deleted");
                            }
                            buildList.removeView(templateView);
                        })
                        .setNegativeButton("No", (dialog, id) -> {
                        });
                AlertDialog alert = builder.create();
                alert.show();
            });

            ImageView like = templateView.findViewById(R.id.like);
            like.setVisibility(View.GONE);
            like.setClickable(false);

            ImageView dislike = templateView.findViewById(R.id.dislike);
            dislike.setVisibility(View.GONE);
            dislike.setClickable(false);
        }

        else {
            ImageView like = templateView.findViewById(R.id.like);
            ImageView dislike = templateView.findViewById(R.id.dislike);
            ImageView star = templateView.findViewById(R.id.saveBuild);

            if (b.getLike().contains(user.getUsername()))
                like.setForegroundTintList(activity.colorDark);
            else if (b.getDislike().contains(user.getUsername()))
                dislike.setForegroundTintList(activity.colorDark);
            if (user.getFavorite().contains(b))
                star.setForegroundTintList(activity.colorDark);

            like.setOnClickListener(v -> {
                boolean addLike = false;
                boolean removeDislike = false;
                boolean removeLike = false;

                if(b.getLike().contains(user.getUsername())) {
                    removeLike = true;
                    b.getLike().remove(user.getUsername());
                    like.setForegroundTintList(activity.color);

                }
                else {
                    addLike = true;
                    b.getLike().add(user.getUsername());
                    like.setForegroundTintList(activity.colorDark);
                    removeDislike = b.getDislike().remove(user.getUsername());
                    dislike.setForegroundTintList(activity.color);
                }

                activity.getViewModel().getBuildRepository().updateLikes(user, b, addLike, false, removeLike, removeDislike);

            });

            dislike.setOnClickListener(v -> {
                boolean addDislike = false;
                boolean removeDislike = false;
                boolean removeLike = false;
                if(b.getDislike().contains(user.getUsername())) {
                    b.getDislike().remove(user.getUsername());
                    dislike.setForegroundTintList(activity.color);
                    removeDislike = true;
                }
                else {
                    b.getDislike().add(user.getUsername());
                    dislike.setForegroundTintList(activity.colorDark);
                    addDislike = true;
                    removeLike = b.getLike().remove(user.getUsername());
                    like.setForegroundTintList(activity.color);
                }

                activity.getViewModel().getBuildRepository().updateLikes(user, b, false, addDislike, removeLike, removeDislike);

            });

            star.setOnClickListener(v -> {

                if(user.getFavorite().contains(b.getUuid().toString())) {
                    user.getFavorite().remove(b.getUuid().toString());
                    star.setForegroundTintList(activity.color);
                    activity.getViewModel().getUserRepository()
                            .updateUserFavorite(user, b, fragment.getContext(), "Build removed to favorite!", false);
                }
                else {
                    user.getFavorite().add(b.getUuid().toString());
                    star.setForegroundTintList(activity.colorDark);
                    activity.getViewModel().getUserRepository()
                            .updateUserFavorite(user, b, fragment.getContext(), "Build added to favorite!", true);
                }
            });

        }

        ConstraintLayout information = templateView.findViewById(R.id.elementsBuildLayout);

        BubbleUtils.setupInformation(b, information, templateView);
        BubbleUtils.setBubbleListener(templateView, information);

        templateView.setLayoutParams(params);

        buildList.addView(templateView);

    }

}
