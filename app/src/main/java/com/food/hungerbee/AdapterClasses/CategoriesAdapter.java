package com.food.hungerbee.AdapterClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.food.hungerbee.ModelClasses.CategoriesModelClass;
import com.food.hungerbee.R;

import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {
    ArrayList<CategoriesModelClass> categoriesList;

    public CategoriesAdapter(ArrayList<CategoriesModelClass> categoriesList) {
        this.categoriesList = categoriesList;
    }

    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_list_food,parent,false);
        return new CategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdapter.CategoriesViewHolder holder, int position) {
        CategoriesModelClass categoriesModelClass = categoriesList.get(position);
        holder.CategorieImg.setImageResource(categoriesModelClass.getImg());
        holder.CategorieName.setText(categoriesModelClass.getName());
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }


    public static class CategoriesViewHolder extends RecyclerView.ViewHolder{
        ImageView CategorieImg;
        TextView CategorieName;
        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            CategorieImg = itemView.findViewById(R.id.CategorieImg);
            CategorieName = itemView.findViewById(R.id.CategorieName);
        }
    }

}
