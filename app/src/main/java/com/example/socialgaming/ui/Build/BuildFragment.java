package com.example.socialgaming.ui.Build;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.socialgaming.R;
import com.example.socialgaming.data.Build;
import com.example.socialgaming.data.BuildFirestore;
import com.example.socialgaming.data.ComponentBase;
import com.example.socialgaming.data.User;
import com.example.socialgaming.data.types.ComponentType;
import com.example.socialgaming.repository.callbacks.IUserCallback;
import com.example.socialgaming.ui.Lists.ComponentsFragment;
import com.example.socialgaming.ui.Settings.SettingsFragment;
import com.example.socialgaming.ui.profile.ProfileFragment;
import com.example.socialgaming.view.MainActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;

public class BuildFragment extends Fragment implements IUserCallback {

    public static final int COMPONENT_PER_VIEW = 20;
    private static final int PICK_IMAGE_REQUEST_CODE = 1;
    public static boolean darkMode;

    private BuildViewModel buildViewModel;
    private CardView[] cardviews;
    private User user;
    private Build build;

    private View currentView;
    private Button saveBuild;
    private TextInputEditText buildName;
    private CircleImageView image;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            build = (Build) getArguments().getSerializable("build");
            AppCompatDelegate.setDefaultNightMode(getArguments().getInt("mode"));
            for(Fragment f : this.getActivity().getSupportFragmentManager().getFragments())
                if(!this.equals(f))
                    this.getActivity().getSupportFragmentManager().beginTransaction().remove(f);
        }
        else {
            MainActivity activity = (MainActivity) getActivity();
            activity.setNightMode(AppCompatDelegate.getDefaultNightMode());
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container.removeAllViews();
        currentView = inflater.inflate(R.layout.fragment_build, container, false);
        buildViewModel = new BuildViewModel(this.getActivity().getApplication());

        user = new User();
        buildViewModel.getUserRepository().getUserData(
                buildViewModel.getAuthRepository().getUserLiveData().getValue().getDisplayName(), this);

        buildName = currentView.findViewById(R.id.editTxtBuildName);
        image = currentView.findViewById(R.id.imageView);
        image.setBorderWidth(0);
        image.setOnClickListener(listener ->{
            Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST_CODE);
        });

        startCardViews(currentView);

        if(build == null)
            build = new Build();
        else {
            updateCardViews();
            if(build.getName() != null)
                buildName.setText(build.getName());
            if(build.getImage() != null)
                image.setImageBitmap(build.getImage());

        }

        setCardviewsListeners();

        saveBuild = currentView.findViewById(R.id.saveBuild);
        saveBuild.setOnClickListener(v -> {
            if(build.getImage() == null && User.DEFAULT_IMAGE != null) {
                build.setImage(User.DEFAULT_IMAGE);
            }
            build.setCreator(user.getUsername());
            if(build.isFinished()) {
                BuildFirestore buildFirestore = new BuildFirestore(build);
                buildFirestore.setCreator(user.getUsername());
                user.addBuild(buildFirestore);
                buildViewModel.getBuildRepository().setBuild(buildFirestore, user, buildViewModel.getUserRepository());

                FragmentTransaction fragmentTransaction =
                        this.getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.buildView, new BuildFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            else {
                Toast.makeText(getActivity().getApplicationContext(),
                        "You have to complete your build!", Toast.LENGTH_SHORT).show();
            }
        });

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

        if(build.getBoard() != null) {

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
    }

    private void switchToComponentView(ComponentType type) {

        if(!buildName.getText().equals(""))
            build.setName(buildName.getText().toString());

        Bundle bundle = new Bundle();
        bundle.putSerializable("type", type);
        bundle.putSerializable("build", build);
        bundle.putInt("mode", AppCompatDelegate.getDefaultNightMode());

        ComponentsFragment componentFragment = new ComponentsFragment();
        componentFragment.setArguments(bundle);

        currentView.setFocusable(false);
        currentView.setClickable(false);

        FragmentTransaction fragmentTransaction =
                this.getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.buildView, componentFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void updateCardViews() {
        boolean isNightModeOn = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
        if(build.getBoard() != null) {
            TextView text = cardviews[0].findViewById(R.id.motherboard);
            text.setText(build.getBoard().getBrand() + " " + build.getBoard().getModel());
            for(int i = 1; i < cardviews.length; i++) {
                if(!isNightModeOn) {
                    cardviews[i].setCardBackgroundColor(ProfileFragment.BLUE);
                    cardviews[i].setClickable(true);
                } else {
                    cardviews[i].setCardBackgroundColor(ProfileFragment.RED);
                    cardviews[i].setClickable(true);
                }
            }
        }

        if(build.getCpu() != null) {
            TextView text = cardviews[1].findViewById(R.id.cpu);
            text.setText(build.getCpu().getBrand() + " " + build.getCpu().getModel());
        }

        if(build.getRams().size() != 0) {
            TextView text = cardviews[2].findViewById(R.id.ram);
            text.setText("RAM Memory = " + build.getRam() + " GB");
        }

        if(build.getFan() != null) {
            TextView text = cardviews[3].findViewById(R.id.cpuFan);
            text.setText(build.getFan().getBrand() + " " + build.getFan().getModel());
        }

        if(build.getGpu() != null) {
            TextView text = cardviews[4].findViewById(R.id.gpu);
            text.setText(build.getGpu().getBrand() + " " + build.getGpu().getModel());
        }

        if(build.getHarddisks().size() != 0) {
            TextView text = cardviews[5].findViewById(R.id.harddisk);
            text.setText("HD Memory = " + build.getMemory() + " GB");
        }

        if(build.getPsu() != null) {
            TextView text = cardviews[6].findViewById(R.id.psu);
            text.setText(build.getPsu().getBrand() + " " + build.getPsu().getModel());
        }

        if(build.getHouse() != null) {
            TextView text = cardviews[7].findViewById(R.id.house);
            text.setText(build.getHouse().getBrand() + " " + build.getHouse().getModel());
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                image.setImageBitmap(bitmap);
                build.setImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onUserReceived(DocumentSnapshot documentSnapshot) {
        if(user == null)
            user = new User();
        user.updateWithDocument(documentSnapshot);
    }
}
