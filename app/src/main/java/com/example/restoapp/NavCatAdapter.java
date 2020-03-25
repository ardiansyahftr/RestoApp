package com.example.restoapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NavCatAdapter extends RecyclerView.Adapter<NavCatAdapter.ViewHolder> {
    Context context;
    List<CategoryModel> listCat;

    public NavCatAdapter(Context context, List<CategoryModel> listCat) {
        this.context = context;
        this.listCat = listCat;
    }

    @NonNull
    @Override
    public NavCatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_navcat, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NavCatAdapter.ViewHolder holder, int position) {
        holder.bind(listCat.get(position),context);
    }

    @Override
    public int getItemCount() {
        return listCat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button btNavCat;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btNavCat = itemView.findViewById(R.id.btNavCat);
        }

        public void bind(final CategoryModel categoryModel, final Context context) {
            btNavCat.setText(categoryModel.getName());
            btNavCat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, categoryModel.getName()+categoryModel.getId(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
