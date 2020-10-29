package com.example.myfirebaseapps.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EnrollAdapter extends RecyclerView.Adapter<EnrollAdapter.CardViewViewHolder>{

    @NonNull
    @Override
    public EnrollAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull EnrollAdapter.CardViewViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class CardViewViewHolder extends RecyclerView.ViewHolder {
        public CardViewViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
