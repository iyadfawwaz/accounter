package io.github.iyado.accounterx;


import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class SpinnerAddapterx extends RecyclerView.Adapter<SpinnerAddapterx.MyString> {



        private ArrayList<String> mStrings;
        private final TextView textViewx;
        private int key;
        private Dialog dialog;

        public SpinnerAddapterx(TextView textView, ArrayList<String> strings) {
            mStrings = strings;
            this.textViewx = textView;
        }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public Dialog getDialog() {
        return dialog;
    }

    @NonNull
    @Override
    public MyString onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_item,parent,false);
        return new MyString(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyString holder, int position) {
            String s = mStrings.get(position);
            holder.textView.setText(s);
            holder.textView.setOnClickListener(view ->
            {

                textViewx.setText(mStrings.get(position));
                getDialog().dismiss();
                //reset(mStrings);
            });

    }
    public void reset(ArrayList<String> stringArrayList){
            mStrings = stringArrayList;
    }


    @Override
    public long getItemId(int position) {
        return mStrings.get(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return mStrings.size();
    }

    public void setmBackupStrings(ArrayList<String> mBackupStrings) {
        mStrings = mBackupStrings;
        notifyDataSetChanged();
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    static class MyString extends RecyclerView.ViewHolder{

        public CardView cardView;
        public TextView textView;
        public MyString(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.c1);
            textView = itemView.findViewById(R.id.TxtVw_DisplayName);

        }
    }
}
