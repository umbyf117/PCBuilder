package com.example.socialgaming.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.socialgaming.R;
import com.example.socialgaming.data.BuildFirestore;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private List<BuildFirestore> builds;
    private View view;

    public HomeAdapter(List<BuildFirestore> builds){
        this.builds = builds;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder holder, int position){
        BuildFirestore buildFirestore = builds.get(position);
    }

    @Override
    public int getItemCount() {
        return builds.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View buildView){
            super(buildView);
        }
    }

}
