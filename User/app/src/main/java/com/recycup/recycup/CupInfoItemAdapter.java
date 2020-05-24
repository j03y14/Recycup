package com.recycup.recycup;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class CupInfoItemAdapter extends RecyclerView.Adapter<CupInfoItemAdapter.ViewHolder> {

    private ArrayList<CupInfo> items = new ArrayList<>();
    CupInfoActivity activity;

    public CupInfoItemAdapter(CupInfoActivity activity) {
        this.activity = activity;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView cafeName;

        TextView cupMaterial;
        CircleImageView cafeLogo;
        ImageView cafeLocation;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cafeName = (TextView) itemView.findViewById(R.id.cafeName);

            cupMaterial = (TextView) itemView.findViewById(R.id.cupMaterial);
            cafeLogo = (CircleImageView) itemView.findViewById(R.id.cafeLogo);
            cafeLocation = (ImageView) itemView.findViewById(R.id.cafeLocationButton);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cup_info_item, parent, false);

        CupInfoItemAdapter.ViewHolder viewHolder = new CupInfoItemAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CupInfo cupInfo = items.get(position);

        holder.cafeName.setText(cupInfo.getHeadName());
        holder.cupMaterial.setText(cupInfo.cupMeterial);
        holder.cafeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.cafeLogo.getContext(), MapActivity.class);

                intent.putExtra("cafeName", cupInfo.headName);
                intent.putExtra("material", cupInfo.cupMeterial);


                activity.startActivity(intent);
            }
        });
        Glide.with(holder.itemView.getContext()).load("http://"+cupInfo.cafeLogo).into(holder.cafeLogo);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(CupInfo cupInfo){
        items.add(cupInfo);
    }
}
