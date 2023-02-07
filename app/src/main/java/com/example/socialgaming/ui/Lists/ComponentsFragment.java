package com.example.socialgaming.ui.Lists;

import static com.example.socialgaming.data.types.ComponentType.MOTHERBOARD;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.socialgaming.Interfaces.OnCardSelectedListener;
import com.example.socialgaming.R;
import com.example.socialgaming.api.ComponentsFetcher;
import com.example.socialgaming.data.Build;
import com.example.socialgaming.data.CPU;
import com.example.socialgaming.data.Case;
import com.example.socialgaming.data.ComponentBase;
import com.example.socialgaming.data.Motherboard;
import com.example.socialgaming.data.RAM;
import com.example.socialgaming.data.types.ComponentType;
import com.example.socialgaming.repository.callbacks.IComponentCallback;
import com.example.socialgaming.ui.Build.BuildFragment;
import com.example.socialgaming.ui.home.HomeFragment;
import com.example.socialgaming.ui.profile.ProfileFragment;
import com.example.socialgaming.utils.BuildUtils;
import com.example.socialgaming.utils.ImageUtils;
import com.google.firebase.firestore.DocumentSnapshot;

public class ComponentsFragment extends Fragment implements IComponentCallback {

    private static final int PX_VALUE = 400;

    private ComponentType type;
    private OnCardSelectedListener listener;
    private ComponentsViewModel viewModel;
    private LinearLayout containerComponents;
    private ComponentBase[] components;
    private View currentView;
    private Build build;

    private int mode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ComponentsViewModel.class);

        if(getArguments() != null) {
            type = (ComponentType) getArguments().getSerializable("type");
            build = (Build) getArguments().getSerializable("build");
            mode = getArguments().getInt("mode");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        currentView = inflater.inflate(R.layout.fragment_component, container, false);
        containerComponents = currentView.findViewById(R.id.containerComponents);

        TextView componentName = currentView.findViewById(R.id.componentName);
        componentName.setText(type.toCapitalCase());

        setComponents(new ComponentsFetcher().fetchItems(type, BuildFragment.COMPONENT_PER_VIEW, 0));
        setupCardViews();

        Button saveButton = currentView.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("build", build);
            bundle.putSerializable("mode", mode);

            BuildFragment fragment = new BuildFragment();
            fragment.setArguments(bundle);

            FragmentTransaction transaction =
                    this.getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return currentView;
    }

    public void setComponents(String json) {
        if(json == null || json.isEmpty())
            components = new ComponentBase[0];
        else {
            components = BuildUtils.getComponents(json, type);
        }
    }

    public void setupCardViews() {

        LayoutInflater inflater = LayoutInflater.from(containerComponents.getContext());

        if(components != null)
            for(int i = 0; i < components.length; i++) {
                setupCardView(components[i], inflater);
            }

    }

    public void setupCardView(ComponentBase component, LayoutInflater inflater) {

        if(component == null) {
            return;
        }

        ComponentType type = ComponentBase.getComponentType(component);

        if(type == ComponentType.CPU && !build.getBoard().compatibleCPU(((CPU)component)))
            return;

        if(type == ComponentType.CASE && !build.getBoard().compatibleCase((Case) component))
            return;


        View templateView = inflater.inflate(R.layout.card_component, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 16, 0, 16);

        templateView.setLayoutParams(params);

        TextView title = templateView.findViewById(R.id.nameComponent);
        title.setText(component.getBrand() + " " + component.getModel());

        ImageView image = templateView.findViewById(R.id.buildImage);
        image.setImageBitmap(ImageUtils.getBitmapFromURL(component.getImg()));
        image.getLayoutParams().width = PX_VALUE;
        image.getLayoutParams().height = PX_VALUE;

        TextView price = templateView.findViewById(R.id.price);
        price.setText(component.getPrice() + "€");

        templateView.setVisibility(View.VISIBLE);
        templateView.setClickable(true);

        templateView.setOnClickListener(view -> {

            if(type == ComponentType.RAM) {
                RAM ram = (RAM) component;
                if(!title.getTextColors().equals(HomeFragment.GOLD)) {
                    if(build.canAddRam(ram)) {
                        build.addRam(ram);
                        title.setTextColor(HomeFragment.GOLD);
                    }
                }
                else {
                    title.setTextColor(ProfileFragment.TEXT_LIGHT);
                    build.removeRam(ram);
                }
            }

            else if (type == ComponentType.MEMORY) {
                if(!title.getTextColors().equals(HomeFragment.GOLD)) {
                    title.setTextColor(HomeFragment.GOLD);
                    build.addComponent(component, type);
                }
                else {
                    title.setTextColor(ProfileFragment.TEXT_LIGHT);
                    build.removeComponent(component, type);
                }
            }
            else {
                if (!title.getTextColors().equals(HomeFragment.GOLD)) {
                    title.setTextColor(HomeFragment.GOLD);
                    ComponentBase c = build.getComponent(type);
                    if (build.addComponent(component, type) && c != null && type != ComponentType.MEMORY)
                        for (int i = 0; i < containerComponents.getChildCount(); i++) {
                            View v = containerComponents.getChildAt(i);
                            if (!templateView.equals(v)) {
                                TextView t = containerComponents.getChildAt(i).findViewById(R.id.nameComponent);
                                t.setTextColor(ProfileFragment.TEXT_LIGHT);
                            }
                        }

                } else {
                    title.setTextColor(ProfileFragment.TEXT_LIGHT);
                    build.removeComponent(component, type);
                }
            }
        });

        containerComponents.addView(templateView);

    }

    /*public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnCardSelectedListener) {
            listener = (OnCardSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement OnCardSelectedListener");
        }

    }*/

    public void onDetach(){
        super.onDetach();
        listener = null;
    }

    public void setOnCardSelectedListener(OnCardSelectedListener listener){
        this.listener = listener;
    }

    @Override
    public void onBuildReceived(String result) {
        if(result == null) {
            Log.e("[CallBack]", "Data not retived");
            return;
        }

    }
}