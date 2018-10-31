package com.example.tyler.trafficapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

public class CameraAdapter extends RecyclerView.Adapter<CameraAdapter.CamViewHolder> {

    private String[][] cameraDataset;
    private int nameIndex = 1;          //the name of a camera is the 2nd item in the array

    //when called, creates a textview using the view supplied
    public static class CamViewHolder extends RecyclerView.ViewHolder {
        public TextView camTextView;
        public CamViewHolder(TextView v) {
            super(v);
            camTextView = v;
        }
    }

    public CameraAdapter(String[][] camDataset){
        cameraDataset = camDataset;
    }

    @Override
    public CameraAdapter.CamViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //note to whomever, the r.layout.textview is a little textview holder to pop the info into
        TextView v = (TextView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.text_view, viewGroup, false);
        return new CamViewHolder(v);
    }


    @Override
    public void onBindViewHolder(CameraAdapter.CamViewHolder holder, int i) {
        holder.camTextView.setText(cameraDataset[i][nameIndex]);
    }

    @Override
    public int getItemCount() {
        return cameraDataset.length;
    }
}

