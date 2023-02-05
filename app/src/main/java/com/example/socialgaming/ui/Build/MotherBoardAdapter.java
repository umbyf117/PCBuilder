package com.example.socialgaming.ui.Build;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialgaming.R;
import com.example.socialgaming.data.Motherboard;

import java.util.ArrayList;

public class MotherBoardAdapter extends RecyclerView.Adapter<ViewHolder> {
    private ArrayList<Motherboard> mbs;
    private int selectedPosition = -1;
    
    public MotherBoardAdapter(ArrayList<Motherboard> mbs){
        this.mbs = mbs;
    }
    
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_motherboard_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mbs.get(position), position == selectedPosition);
    }

    @Override
    public int getItemCount() {
        return 0;
    }
    
    public void setSelectedPosition(int position){
        selectedPosition = position;
        notifyDataSetChanged();
    }
}

class ViewHolder extends RecyclerView.ViewHolder{
    private TextView description;
    private CardView cv;
    
    public ViewHolder(View mbView){
        super(mbView);
        description = mbView.findViewById(R.id.description);
        cv = mbView.findViewById(R.id.cardView);
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedPosition(getAbsoluteAdapterPosition());
            }
        });
    }

    private void setSelectedPosition(int absoluteAdapterPosition) {
    }

    public void bind(Motherboard motherboard, boolean isSelected) {
        description.setText(motherboard.getId());
        description.setText(motherboard.getChipset());
        description.setText(motherboard.getBrand());
        description.setText(motherboard.getModel());
        description.setText(motherboard.getFormFactor());
        description.setText(motherboard.getSocketType());
        cv.setCardBackgroundColor((isSelected ? Color.LTGRAY : Color.WHITE));
        
    }
}
