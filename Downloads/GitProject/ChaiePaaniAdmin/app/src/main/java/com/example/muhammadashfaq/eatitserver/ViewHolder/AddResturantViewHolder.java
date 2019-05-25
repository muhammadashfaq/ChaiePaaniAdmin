package com.example.muhammadashfaq.eatitserver.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.example.muhammadashfaq.eatitserver.Interface.ItemClickListner;
import com.example.muhammadashfaq.eatitserver.R;

public class AddResturantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener {
    public TextView txtVuOrderid,txtVuOrderStatus,txtVuOrderPhone,txtVuOrderAdress,txtVuOrderPrice;

    private ItemClickListner itemClickListner;
    public AddResturantViewHolder(@NonNull View itemView) {
        super(itemView);
        txtVuOrderStatus=itemView.findViewById(R.id.order_status);
        txtVuOrderPhone=itemView.findViewById(R.id.order_phone);
        txtVuOrderAdress=itemView.findViewById(R.id.order_adress);

        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v,getAdapterPosition(),false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Choose Action");
        menu.add(0,0,getAdapterPosition(),"Update");
        menu.add(0,0,getAdapterPosition(),"Remove");
    }
}
