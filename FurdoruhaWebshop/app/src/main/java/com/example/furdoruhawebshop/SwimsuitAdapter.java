package com.example.furdoruhawebshop;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SwimsuitAdapter extends RecyclerView.Adapter<SwimsuitAdapter.ViewHolder> implements Filterable {

    private ArrayList<Swimsuit> swimsuits;
    private ArrayList<Swimsuit> swimsuitsAll;

    private Context context;

    private int lastPosition = -1;

    SwimsuitAdapter(Context context, ArrayList<Swimsuit> swimsuits){
        this.swimsuits = swimsuits;
        this.swimsuitsAll = swimsuits;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_swimsuits,parent,false));
    }

    @Override
    public void onBindViewHolder( SwimsuitAdapter.ViewHolder holder, int position) {
        Swimsuit currentSwimsuit = swimsuits.get(position);

        holder.bindTo(currentSwimsuit);
    }

    @Override
    public int getItemCount() {
        return swimsuits.size();
    }

    @Override
    public Filter getFilter() {
        return SwimsuitFilter;
    }

    private Filter SwimsuitFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Swimsuit> filtered = new ArrayList<>();
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0 ){
                results.count = swimsuitsAll.size();
                results.values = swimsuitsAll;
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Swimsuit item : swimsuitsAll){
                    if (item.getName().toLowerCase().contains(filterPattern)){
                        filtered.add(item);
                    }
                }
                results.count = filtered.size();
                results.values = filtered;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            swimsuits = (ArrayList) results.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView details;
        private TextView price;
        private ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            details = itemView.findViewById(R.id.details);
            price = itemView.findViewById(R.id.price);
            image = itemView.findViewById(R.id.image);
        };

        public void bindTo(Swimsuit currentSwimsuit) {
            name.setText(currentSwimsuit.getName());
            details.setText(currentSwimsuit.getDetails());
            price.setText(currentSwimsuit.getPrice()+" Ft");

            Glide.with(context).load(currentSwimsuit.getImage()).into(image);

            itemView.findViewById(R.id.delete).setOnClickListener(v -> ((ShopActivity)context).delete(currentSwimsuit));
        }
    }
}

