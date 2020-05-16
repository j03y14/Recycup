package com.recycup.recycup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MapViewItemAdapter extends RecyclerView.Adapter<MapViewItemAdapter.ViewHolder> implements Filterable {

    ArrayList<CupInfo> cafeList = new ArrayList<>();
    ArrayList<CupInfo> filteredList = new ArrayList<>();
    @NonNull
    @Override
    public MapViewItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.map_view_item, parent, false) ;
        MapViewItemAdapter.ViewHolder vh = new MapViewItemAdapter.ViewHolder(view) ;


        return vh ;
    }

    @Override
    public void onBindViewHolder(@NonNull MapViewItemAdapter.ViewHolder holder, int position) {
        CupInfo item = filteredList.get(position) ;

        Glide.with(holder.imageView.getContext()).load(item.cafeLogo).into(holder.imageView);
        holder.textView.setText(item.headName);

    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void addItem(CupInfo cupInfo){
        cafeList.add(cupInfo);
        filteredList.add(cupInfo);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if(charString.isEmpty()) {
                    filteredList = cafeList;
                } else {
                    ArrayList<CupInfo> filteringList = new ArrayList<>();
                    for(CupInfo cupInfo : cafeList) {
                        if(cupInfo.headName.toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(cupInfo);
                        }
                    }
                    filteredList = filteringList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<CupInfo>)results.values;
                notifyDataSetChanged();
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView ;
        TextView textView ;


        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            imageView = itemView.findViewById(R.id.itemImageView) ;
            textView = itemView.findViewById(R.id.itemTextView) ;
        }
    }


}
