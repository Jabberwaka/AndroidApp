package com.example.tyler.trafficapp;
//
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//
//public class CameraAdapter extends RecyclerView.Adapter<CameraAdapter.CamViewHolder> {
//
//    private String[][] cameraDataset;
//    private int nameIndex = 1;          //the name of a camera is the 2nd item in the array
//
//    //when called, creates a textview using the view supplied
//    public static class CamViewHolder extends RecyclerView.ViewHolder {
//        public TextView camTextView;
//        public CamViewHolder(TextView v) {
//            super(v);
//            camTextView = itemView.findViewById(R.id.camera_info);
//            camTextView = v;
//        }
//    }
//
//    public CameraAdapter(String[][] camDataset){
//        cameraDataset = camDataset;
//    }
//
//    @Override
//    public CamViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        //note to whomever, the r.layout.textview is a little textview holder to pop the info into
//        TextView v = (TextView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.text_view, viewGroup, false);
//        return new CamViewHolder(v);
//    }
//
//
//    @Override
//    public void onBindViewHolder(CamViewHolder holder, int i) {
//        holder.camTextView.setText(cameraDataset[i][nameIndex]);
//    }
//
//    @Override
//    public int getItemCount() {
//        return cameraDataset.length;
//    }
//}
//

public class CameraAdapter extends RecyclerView.Adapter<CameraAdapter.ViewHolder> {

    private ArrayList<Camera> cameraList = new ArrayList<>();
    private LayoutInflater inflater;
    //private View.OnClickListener onItemClickListener;
    //private static AdapterView.OnItemClickListener listener;

    //private int itemLayout;
    // List<String> list = new ArrayList<String>();

    public CameraAdapter(Context context, ArrayList<Camera> cameras) {
        this.cameraList = cameras;
        this.inflater = LayoutInflater.from(context);//convertToList();
    }


    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.text_view, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        //ViewMo//del item = cameraDataset[position][1];
        //for (position = 0; position < getItemCount(); position++)
        //int pos2 = 0;

        //String cameraName = list.get(position);
        Camera camera = cameraList.get(position);
        TextView textView = holder.text;
        textView.setText(camera.getCameraName());
        //holder.text.setText(String.valueOf(cameraList.size()));

        //  ++position;
    }
    public void filteredList(ArrayList<Camera> list){
        cameraList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return cameraList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //public ImageView image;
        public TextView text;
        Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            //itemView.setTag(this);
            //itemView.setOnClickListener(onItemClickListener);
            //image = (ImageView) itemView.findViewById(R.id.image);
            text = (TextView) itemView.findViewById(R.id.camera_info);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DisplayCameraActivity.class);
                    intent.putExtra("cameraId", cameraList.get(getLayoutPosition()).getCameraId());
                    context.startActivity(intent);

                }
            });

        }


    }

}
