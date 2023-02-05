package com.example.socialgaming.ui.Lists;

import static com.example.socialgaming.data.types.ComponentType.MOTHERBOARD;

import android.content.Context;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.socialgaming.Interfaces.OnCardSelectedListener;
import com.example.socialgaming.R;
import com.example.socialgaming.data.Motherboard;
import com.example.socialgaming.utils.BuildUtils;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class MotherboardFragment extends Fragment {

    private int numCards;
    private OnCardSelectedListener listener;
    private final int NUM_CARDS = 50;

    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof OnCardSelectedListener){
            listener = (OnCardSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement OnCardSelectedListener");
        }
    }

    public void onDetach(){
        super.onDetach();
        listener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            numCards = getArguments().getInt("numCardViews");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_motherboard, container, false);
        LinearLayout ll = view.findViewById(R.id.llmb);

        BuildUtils.getComponentsJSON(MOTHERBOARD, NUM_CARDS, 0);

        Motherboard[] mbs = (Motherboard[]) BuildUtils.getComponents(BuildUtils.getComponentsJSON(MOTHERBOARD, NUM_CARDS, 0), MOTHERBOARD);


        for(int i=0; i<numCards; i++){
            CardView cardView = new CardView(getContext());
            cardView.setCardElevation(8);
            cardView.setRadius(8);
            cardView.setContentPadding(16,16,16,16);
            cardView.setMaxCardElevation(15);

            TextView textView = new TextView(getContext());
            textView.setText(mbs[i].getModel());
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
        }
        return view;
    }

    public void setOnCardSelectedListener(OnCardSelectedListener listener){
        this.listener = listener;
    }


}