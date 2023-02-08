package com.example.socialgaming.ui.Details;

import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.socialgaming.data.User;

public class DetailsFragment extends Fragment {

    class UserLiveData extends LiveData<User> {
        // ...
    }

    UserLiveData liveData = new UserLiveData();

    User user = new User(1)
    liveData.setValue(user)

    liveData.observe(this, new Observer<User>() {
        @Override
        public void onChanged(@Nullable User user) {
            // Do something with the changed value
        }
    });

    TextView textView = textView.findViewById();
    liveData.observe(this, new Observer<User>() {
        @Override
        public void onChanged(@Nullable User user) {
            textView.setText(user.getName());
        }
    })

}
