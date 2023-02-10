package com.example.socialgaming.ui.Lists;

import static com.example.socialgaming.data.types.ComponentType.MOTHERBOARD;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
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
import com.example.socialgaming.data.CPUFan;
import com.example.socialgaming.data.Case;
import com.example.socialgaming.data.ComponentBase;
import com.example.socialgaming.data.GPU;
import com.example.socialgaming.data.Memory;
import com.example.socialgaming.data.Motherboard;
import com.example.socialgaming.data.PSU;
import com.example.socialgaming.data.RAM;
import com.example.socialgaming.data.types.ComponentType;
import com.example.socialgaming.repository.callbacks.IComponentCallback;
import com.example.socialgaming.ui.Build.BuildFragment;
import com.example.socialgaming.ui.home.HomeFragment;
import com.example.socialgaming.ui.profile.ProfileFragment;
import com.example.socialgaming.utils.BuildUtils;
import com.example.socialgaming.utils.ImageUtils;
import com.example.socialgaming.view.MainActivity;
import com.google.firebase.firestore.DocumentSnapshot;

public class ComponentsFragment extends Fragment {

    private static final int PX_VALUE = 400;

    private MainActivity activity;
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

        activity = (MainActivity) getActivity();
        activity.setNightMode(AppCompatDelegate.getDefaultNightMode());
        viewModel = new ViewModelProvider(this).get(ComponentsViewModel.class);

        if (getArguments() != null) {
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
        if (json == null || json.isEmpty())
            components = new ComponentBase[0];
        else {
            components = BuildUtils.getComponents(json, type);
        }
    }

    public void setupCardViews() {

        LayoutInflater inflater = LayoutInflater.from(containerComponents.getContext());

        if (components != null)
            for (int i = 0; i < components.length; i++) {
                setupCardView(components[i], inflater);
            }

    }

    public void setupCardView(ComponentBase component, LayoutInflater inflater) {

        if (component == null) {
            return;
        }

        ComponentType type = ComponentBase.getComponentType(component);

        if (type == ComponentType.CPU && !build.getBoard().compatibleCPU(((CPU) component)))
            return;

        if (type == ComponentType.CASE && !build.getBoard().compatibleCase((Case) component))
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

        if (type == ComponentType.MOTHERBOARD) {
            Motherboard motherboard = (Motherboard) component;
            TextView info1 = templateView.findViewById(R.id.info1);
            info1.setText("Chipset: " + motherboard.getChipset());
            TextView info2 = templateView.findViewById(R.id.info2);
            info2.setText("MemorySlot: " + motherboard.getMemorySlots());
            TextView info3 = templateView.findViewById(R.id.info3);
            info3.setText("SocketType: " + motherboard.getSocketType());
        } else if (type == ComponentType.RAM) {
            RAM ram = (RAM) component;
            TextView info1 = templateView.findViewById(R.id.info1);
            info1.setText("Size: " + ram.getSize());
            TextView info2 = templateView.findViewById(R.id.info2);
            info2.setText("Quantity: " + ram.getQuantity());
            TextView info3 = templateView.findViewById(R.id.info3);
            info3.setText("Type: " + ram.getType());
        } else if (type == ComponentType.MEMORY) {
            Memory memory = (Memory) component;
            TextView info1 = templateView.findViewById(R.id.info1);
            info1.setText("Rpm: " + memory.getRpm());
            TextView info2 = templateView.findViewById(R.id.info2);
            info2.setText("Cache: " + memory.getCache());
            TextView info3 = templateView.findViewById(R.id.info3);
            info3.setText("Type: " + memory.getType());
        } else if (type == ComponentType.CPU) {
            CPU cpu = (CPU) component;
            TextView info1 = templateView.findViewById(R.id.info1);
            info1.setText("Speed: " + cpu.getSpeed());
            TextView info2 = templateView.findViewById(R.id.info2);
            info2.setText("SocketType: " + cpu.getSocketType());
            TextView info3 = templateView.findViewById(R.id.info3);
            info3.setText("");
        } else if (type == ComponentType.CPU_FAN) {
            CPUFan cpuFan = (CPUFan) component;
            TextView info1 = templateView.findViewById(R.id.info1);
            info1.setText("Rpm: " + cpuFan.getRpm());
            TextView info2 = templateView.findViewById(R.id.info2);
            info2.setText("Color: " + cpuFan.getColor());
            TextView info3 = templateView.findViewById(R.id.info3);
            info3.setText("Noise Level: " + cpuFan.getNoiseLevel());
        } else if (type == ComponentType.GPU) {
            GPU gpu = (GPU) component;
            TextView info1 = templateView.findViewById(R.id.info1);
            info1.setText("Memory: " + gpu.getMemory());
            TextView info2 = templateView.findViewById(R.id.info2);
            info2.setText("Clock Speed: " + gpu.getClockSpeed());
            TextView info3 = templateView.findViewById(R.id.info3);
            info3.setText("Chipset: " + gpu.getChipset());
        } else if (type == ComponentType.PSU) {
            PSU psu = (PSU) component;
            TextView info1 = templateView.findViewById(R.id.info1);
            info1.setText("Power: " + psu.getPower());
            TextView info2 = templateView.findViewById(R.id.info2);
            info2.setText("Color: " + psu.getColor());
            TextView info3 = templateView.findViewById(R.id.info3);
            info3.setText("Efficency: " + psu.getEfficiency());
        } else if (type == ComponentType.CASE) {
            Case mcase = (Case) component;
            TextView info1 = templateView.findViewById(R.id.info1);
            info1.setText("Side Panel: " + mcase.getSidePanel());
            TextView info2 = templateView.findViewById(R.id.info2);
            info2.setText("Color: " + mcase.getColor());
            TextView info3 = templateView.findViewById(R.id.info3);
            info3.setText("Cabinet: " + mcase.getCabinet());
        }

        ImageView image = templateView.findViewById(R.id.buildImage);
        image.setImageBitmap(ImageUtils.getBitmapFromURL(component.getImg()));
        image.getLayoutParams().width = PX_VALUE;
        image.getLayoutParams().height = PX_VALUE;

        TextView price = templateView.findViewById(R.id.price);
        price.setText(component.getPrice() + "€");

        templateView.setVisibility(View.VISIBLE);
        templateView.setClickable(true);

        templateView.setOnClickListener(view -> {

            if (type == ComponentType.RAM) {
                RAM ram = (RAM) component;
                if (!title.getTextColors().equals(activity.gold)) {
                    if (build.canAddRam(ram)) {
                        build.addRam(ram);
                        title.setTextColor(activity.gold);
                    }
                } else {
                    title.setTextColor(activity.textColor);
                    build.removeRam(ram);
                }
            } else if (type == ComponentType.MEMORY) {
                if (!title.getTextColors().equals(activity.gold)) {
                    title.setTextColor(activity.gold);
                    build.addComponent(component, type);
                } else {
                    title.setTextColor(activity.textColor);
                    build.removeComponent(component, type);
                }
            } else {
                if (!title.getTextColors().equals(activity.gold)) {
                    title.setTextColor(activity.gold);
                    ComponentBase c = build.getComponent(type);
                    if (build.addComponent(component, type) && c != null && type != ComponentType.MEMORY)
                        for (int i = 0; i < containerComponents.getChildCount(); i++) {
                            View v = containerComponents.getChildAt(i);
                            if (!templateView.equals(v)) {
                                TextView t = containerComponents.getChildAt(i).findViewById(R.id.nameComponent);
                                t.setTextColor(activity.textColor);
                            }
                        }

                } else {
                    title.setTextColor(activity.textColor);
                    build.removeComponent(component, type);
                }
            }
        });

        containerComponents.addView(templateView);

    }

}