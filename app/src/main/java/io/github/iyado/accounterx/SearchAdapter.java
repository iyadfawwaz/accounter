package io.github.iyado.accounterx;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SString> {

    private ArrayList<String> arrayList;
    private final Context context;
    private Dialog alertDialog;

    @NonNull
    @Override
    public SString onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SString(LayoutInflater.from(parent.getContext()).inflate(R.layout.stringi, parent, false));
    }

    public void setAlertDialog(Dialog alertDialog) {
        this.alertDialog = alertDialog;
    }

    @Override
    public void onBindViewHolder(@NonNull SString holder, int position) {

        String s = arrayList.get(position);
        holder.textView.setText(s);
        holder.textView.setOnClickListener(view -> {
            Intent intent = new Intent( context,PrinterInfo.class);
            intent.putExtra("user",arrayList.get(position));
            intent.putExtra("sum",arrayList.get(position));
           // Toast.makeText(holder.itemView.getContext(), s,Toast.LENGTH_LONG).show();
            alertDialog.dismiss();
            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    static class SString extends RecyclerView.ViewHolder{

        TextView textView;
        public SString(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView8);
        }
    }

    public SearchAdapter(@NonNull Context context, ArrayList<String> arrayList) {
        super();
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public long getItemId(int position) {
        return arrayList.get(position).hashCode();
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setArrayList(ArrayList<String> dataList) {
        arrayList = dataList;
        notifyDataSetChanged();
    }
}

