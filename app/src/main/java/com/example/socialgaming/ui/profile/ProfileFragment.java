package com.example.socialgaming.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.socialgaming.R;
import com.example.socialgaming.data.User;
import com.example.socialgaming.databinding.FragmentProfileBinding;
import com.example.socialgaming.repository.callbacks.IUserCallback;
import com.example.socialgaming.repository.user.UserRepository;
import com.example.socialgaming.ui.Settings.SettingsFragment;
import com.example.socialgaming.ui.home.HomeFragmentViewModel;
import com.example.socialgaming.utils.FragmentUtils;
import com.example.socialgaming.view.auth.LoginFragment;
import com.example.socialgaming.view.auth.RegisterFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.type.Date;

import java.util.HashMap;

public class ProfileFragment extends Fragment implements IUserCallback {
    private FragmentProfileBinding binding;
    private ImageView image;
    private FirebaseUser FirebaseUser;
    private FirebaseDatabase FirebaseDatabase;
    private FirebaseStorage storageRef;
    private User user;
    private ProfileViewModel profileViewModel;
    private View view;

    private static final int PICK_IMAGE = 1;

    public ProfileFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        profileViewModel = new ProfileViewModel(getActivity().getApplication());

        user = new User();
        profileViewModel.getUserRepository().getUserData(
                profileViewModel.getAuthRepository().getUserLiveData().getValue().getDisplayName(),
                this);

        image = view.findViewById(R.id.prof_pic);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        return view;
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = data.getData();
            user.setImage(imageUri);
            image = view.findViewById(R.id.prof_pic);
            image.setImageURI(imageUri);
            profileViewModel.getUserRepository().updateImage(user, imageUri);
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public String getUserName() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user.getDisplayName();
    }


    @Override
    public void onUserReceived(DocumentSnapshot documentSnapshot) {
        if(user == null)
            user = new User();
        user.updateWithDocument(documentSnapshot);

        TextView tv1 = view.findViewById(R.id.profUser);
        tv1.setText(user.getUsername());
        TextView tv2 = view.findViewById(R.id.profMail);
        tv2.setText(user.getMail());

        image.setImageURI(user.getImage());
    }
}