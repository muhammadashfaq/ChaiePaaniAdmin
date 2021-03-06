package com.example.muhammadashfaq.eatitserver;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.muhammadashfaq.eatitserver.Common.Common;
import com.example.muhammadashfaq.eatitserver.Interface.ItemClickListner;
import com.example.muhammadashfaq.eatitserver.Model.AddResturant;
import com.example.muhammadashfaq.eatitserver.Model.Request;
import com.example.muhammadashfaq.eatitserver.ViewHolder.AddResturantViewHolder;
import com.example.muhammadashfaq.eatitserver.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class AddRestrurantStatus extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase db;
    DatabaseReference reference;

    FirebaseRecyclerAdapter<AddResturant, AddResturantViewHolder> recyclerAdapter;

    MaterialSpinner materialSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restrurant_status);

        //Firebase init
        db=FirebaseDatabase.getInstance();
        reference=db.getReference("Resturants");

        //Init
        recyclerView=findViewById(R.id.recycler_list_orders);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders();
    }

    private void loadOrders() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Resturants Requests");
        progressDialog.show();

        recyclerAdapter = new FirebaseRecyclerAdapter<AddResturant,AddResturantViewHolder>(AddResturant.class,R.layout.add_resturant_request_layout,AddResturantViewHolder.class,
                reference) {
            @Override
            protected void populateViewHolder(AddResturantViewHolder viewHolder, AddResturant model, int position) {
                progressDialog.dismiss();
                viewHolder.txtVuOrderStatus.setText(model.getName());
                viewHolder.txtVuOrderPhone.setText(model.getLatitude());
                viewHolder.txtVuOrderAdress.setText(model.getLongitude());
               // viewHolder.txtVuOrderPrice.setText(model.getTotal());

                viewHolder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Toast.makeText(AddRestrurantStatus.this, "Long press to Update the status", Toast.LENGTH_SHORT).show();
                        //Maps integration
                        //   Intent trackingOrder=new Intent(this,TrackingOrder.class);
                        //    Common.currentRequest=model;
                        //     startActivity(trackingOrder);
                    }
                });
            }
        };
        recyclerAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        if(item.getTitle().equals(Common.UPDATE))
        {
            showUpdateDailog(recyclerAdapter.getRef(item.getOrder()).getKey(),recyclerAdapter.getItem(item.getOrder()));

        }else if(item.getTitle().equals(Common.DELETE)){
            deleteCategory(recyclerAdapter.getRef(item.getOrder()).getKey());
        }
        return true;
    }

    private void showUpdateDailog(final String key, final AddResturant item)
    {
        final AlertDialog.Builder alertDailog=new AlertDialog.Builder(this);
        alertDailog.setTitle("Update Order");
        alertDailog.setMessage("Choose new Order Status");

        LayoutInflater inflater=this.getLayoutInflater();
        View view=inflater.inflate(R.layout.update_order_layout,null);

        materialSpinner=view.findViewById(R.id.spinner_status);

        materialSpinner.setItems("Select","Approved");

        alertDailog.setView(view);
        final String localKey=key;

        alertDailog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();



                item.setStatus(String.valueOf(materialSpinner.getSelectedIndex()));
                if(String.valueOf(materialSpinner.getSelectedIndex()).equals("0")){
                    Toast.makeText(AddRestrurantStatus.this, "Please Select Status First", Toast.LENGTH_SHORT).show();
                }else {
                    reference.child(key).setValue(item);
                    Toast.makeText(AddRestrurantStatus.this, "Status Updated", Toast.LENGTH_SHORT).show();
                }




            }
        });
        alertDailog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDailog.show();


    }

    private void deleteCategory(String key)
    {
        reference.child(key).removeValue();
        Toast.makeText(this, "Order rejected and removed.", Toast.LENGTH_SHORT).show();
    }

}
