package com.example.socialgaming.ui.Lists;

import static com.example.socialgaming.data.types.ComponentType.MOTHERBOARD;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import com.example.socialgaming.data.ComponentBase;
import com.example.socialgaming.data.types.ComponentType;
import com.example.socialgaming.repository.callbacks.IComponentCallback;
import com.example.socialgaming.ui.Build.BuildFragment;
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

    private ComponentBase componentToReturn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ComponentsViewModel.class);

        if(getArguments() != null){
            type = (ComponentType) getArguments().getSerializable("type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        currentView = inflater.inflate(R.layout.fragment_component, container, false);
        containerComponents = currentView.findViewById(R.id.containerComponents);

        TextView componentName = currentView.findViewById(R.id.componentName);
        componentName.setText(type.toCapitalCase());

        Button saveButton = currentView.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("component", componentToReturn);
            getParentFragmentManager().popBackStack();
        });

        //setComponents(BuildUtils.getComponentsJSON(MOTHERBOARD, BuildFragment.COMPONENT_PER_VIEW, 0, this));
        setComponents(new ComponentsFetcher().fetchItems(MOTHERBOARD, BuildFragment.COMPONENT_PER_VIEW, 0));
        setupCardViews();
        /*
        for(int i=0; i<numCards; i++){
            CardView cardView = new CardView(requireContext());
            cardView.setCardElevation(8);
            cardView.setRadius(8);
            cardView.setContentPadding(16,16,16,16);
            cardView.setMaxCardElevation(15);

            TextView textView = new TextView(getContext());
            textView.setText(mbs[i].getBrand());
            textView.setTextSize(20);
            textView.setGravity(Gravity.CENTER);

            cardView.addView(textView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        listener.onCardSelected(textView.getText().toString());
                    }
                }
            });
            ll.addView(cardView);
        }*/
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