package com.example.socialgaming.utils;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.socialgaming.PcBuilder;
import com.example.socialgaming.R;

public class FragmentUtils {

    public static void startActivity(AppCompatActivity activity, Intent intent, boolean kill) {
        activity.startActivity(intent);
        if (kill)
            activity.finish(); // Kill and disable back button when logged
    }

    public static void loadFragment(Fragment fragment, FragmentManager fragmentManager) {
        loadFragment(fragment, fragmentManager, R.id.main_activity);
    }

    public static void loadFragment(Fragment fragment, FragmentManager fragmentManager, @IdRes int fragmentContainer) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(fragmentContainer, fragment);
        fragmentTransaction.commit(); // save the changes
    }
    public static void loadFragment(Fragment fragment, FragmentManager fragmentManager, String fragmentTag, Context context) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (Fragment f: fragmentManager.getFragments())
            fragmentTransaction.hide(f);
        fragmentTransaction.commitNow();
        if(fragmentManager.findFragmentByTag(fragmentTag)==null)
            fragmentTransaction.add(R.id.login_activity, fragment, fragmentTag);
        else
            fragmentTransaction.show(fragmentManager.findFragmentByTag(fragmentTag));
        fragmentTransaction.commitNow();
    }
/*
    public static void backToListFragment(FragmentManager fragmentManager, Context context, String backToTag){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(fragmentManager.findFragmentByTag(context.getString(R.string.recipe_fragment))!=null) {
            fragmentTransaction.remove(fragmentManager.findFragmentByTag(context.getString(R.string.recipe_fragment)));
            fragmentTransaction.show(fragmentManager.findFragmentByTag(backToTag));
            fragmentTransaction.commitNow();
        }else
            ((PcBuilder)context).backPress();
    }
*/
}
