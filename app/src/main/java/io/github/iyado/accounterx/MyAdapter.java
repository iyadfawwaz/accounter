package io.github.iyado.accounterx;


import static io.github.iyado.accounterx.CutAndDiscount.eurocut;
import static io.github.iyado.accounterx.CutAndDiscount.realcut;
import static io.github.iyado.accounterx.CutAndDiscount.tlcut;
import static io.github.iyado.accounterx.JobActivity.COMPANY_NAME;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private final Context context;
    private List<Informations> dataList;


    public MyAdapter(Context context, List<Informations> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
            return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        Informations informations = dataList.get(position);

        Currency currency = informations.getAccount();
        double re = Double.parseDouble(currency.getDolar())+
                (Double.parseDouble(currency.getEuro())*eurocut)+
                (Double.parseDouble(currency.getReal())/realcut)+
                (Double.parseDouble(currency.getTl())/tlcut);
                double rere = Math.round(re);
            holder.userid.setText(informations.getUid());
            holder.username.setText(informations.getUsername());
            holder.useraccount.setText(String.valueOf(rere));
            if ( rere < 0 ){
                holder.useraccount.setTextColor(Color.RED);
            }

        holder.recCard.setOnClickListener(view -> {
            Intent intent = new Intent( context,PrinterInfo.class);
            intent.putExtra("user",dataList.get(position).getUsername());
            intent.putExtra("sum",String.valueOf(rere));
           // Toast.makeText(holder.itemView.getContext(), dataList.get(position).getUsername(),Toast.LENGTH_LONG).show();
           // alertDialog.dismiss();
            context.startActivity(intent);

        });

            holder.wa.setOnClickListener(v ->
                    {
                        AlertDialog alertDialog = new AlertDialog.Builder(context)
                                .setMessage("هل انت متأكد من الحذف")
                                .setNeutralButton("تأكيد",(dialog, which) ->
                                        removeItem(dataList.get(position).getUsername())
                                        )
                                .setTitle("حذف القيد المحدد")
                                .setNegativeButton("الغاء",(dialog, which) ->
                                        dialog.dismiss())
                                .setIcon(android.R.drawable.ic_delete)
                                .show();
                    }
                            );
/*
        holder.recCard.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("username", dataList.get(holder.getAdapterPosition()).getUsername());
            intent.putExtra("uid", dataList.get(holder.getAdapterPosition()).getUid());
            intent.putExtra("Key", dataList.get(holder.getAdapterPosition()).getKey());
            intent.putExtra("account", dataList.get(holder.getAdapterPosition()).getUid());
            context.startActivity(intent);
        });

 */


    }

    private void removeItem(String username) {
        FirebaseDatabase.getInstance().getReference()
                .child(COMPANY_NAME)
                .child("users")
                .child(username).removeValue((error, ref) ->
                {
                    if (error != null) {
                        Toast.makeText(context,error.getMessage(),Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context,"تم الحذف"+username+" بنجاح",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {


            return dataList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void searchDataList(ArrayList<Informations> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }





}

class MyViewHolder extends RecyclerView.ViewHolder{

    TextView userid, username, useraccount;
    CardView recCard;
    ImageView wa;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        recCard = itemView.findViewById(R.id.recCard);
        username = itemView.findViewById(R.id.useridd);
        useraccount = itemView.findViewById(R.id.usernamed);
        userid = itemView.findViewById(R.id.useraccountd);
        wa = itemView.findViewById(R.id.wa);
    }
}
