package com.example.socialgaming.ui.Build;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.socialgaming.R;
import com.example.socialgaming.data.Build;
import com.example.socialgaming.data.ComponentBase;
import com.example.socialgaming.data.User;
import com.example.socialgaming.data.types.ComponentType;
import com.example.socialgaming.repository.callbacks.IUserCallback;
import com.example.socialgaming.ui.Lists.ComponentsFragment;
import com.example.socialgaming.view.MainActivity;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.logging.Level;
import java.util.logging.Logger;

import okhttp3.OkHttpClient;

public class BuildFragment extends Fragment implements IUserCallback {

    public static final int COMPONENT_PER_VIEW = 10;

    private BuildViewModel buildViewModel;
    private CardView[] cardviews;
    private User user;
    private Build build;

    private Fragment startFragment;
    private Fragment componentFragment;
    private View currentView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_build, container, false);
        buildViewModel = new BuildViewModel(this.getActivity().getApplication());

        user = new User();
        buildViewModel.getUserRepository().getUserData(
                buildViewModel.getAuthRepository().getUserLiveData().getValue().getDisplayName(), this);

        build = new Build();

        MainActivity activity = (MainActivity) getActivity();
        activity.setNightMode(AppCompatDelegate.getDefaultNightMode());

        startCardViews(view);
        setCardviewsListeners();
        Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);

        startFragment = this;
        currentView = view;

        return currentView;
    }

    private void startCardViews(View view) {
        cardviews = new CardView[8];
        cardviews[0] = view.findViewById(R.id.motherboardBuild);
        cardviews[1] = view.findViewById(R.id.cpuBuild);
        cardviews[2] = view.findViewById(R.id.ramBuild);
        cardviews[3] = view.findViewById(R.id.cpuFanBuild);
        cardviews[4] = view.findViewById(R.id.gpuBuild);
        cardviews[5] = view.findViewById(R.id.storageBuild);
        cardviews[6] = view.findViewById(R.id.psuBuild);
        cardviews[7] = view.findViewById(R.id.caseBuild);
    }

    private void setCardviewsListeners() {
        cardviews[0].setOnClickListener(view -> {
            switchToComponentView(ComponentType.MOTHERBOARD);
        });
        cardviews[1].setOnClickListener(view -> {
            switchToComponentView(ComponentType.CPU);
        });
        cardviews[2].setOnClickListener(view -> {
            switchToComponentView(ComponentType.RAM);
        });
        cardviews[3].setOnClickListener(view -> {
            switchToComponentView(ComponentType.CPU_FAN);
        });
        cardviews[4].setOnClickListener(view -> {
            switchToComponentView(ComponentType.GPU);
        });
        cardviews[5].setOnClickListener(view -> {
            switchToComponentView(ComponentType.MEMORY);
        });
        cardviews[6].setOnClickListener(view -> {
            switchToComponentView(ComponentType.PSU);
        });
        cardviews[7].setOnClickListener(view -> {
            switchToComponentView(ComponentType.CASE);
        });
    }

    private void switchToComponentView(ComponentType type) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("type", type);
        bundle.putSerializable("build", build);

        componentFragment = new ComponentsFragment();
        componentFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = this.getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.buildView, componentFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onUserReceived(DocumentSnapshot documentSnapshot) {
        if(user == null)
            user = new User();
        user.updateWithDocument(documentSnapshot);
    }
}
