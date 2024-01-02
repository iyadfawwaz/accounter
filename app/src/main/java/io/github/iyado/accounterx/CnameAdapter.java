package io.github.iyado.accounterx;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CnameAdapter extends ArrayAdapter<String> {

    private Context context;
    private final ArrayList<String> cnames;
    public CnameAdapter(@NonNull Context context,@NonNull ArrayList<String> cnames){
        super(context, R.layout.view_list_no_selection_item,R.id.TxtVw_NoSelection);
        this.cnames = cnames;
    }

    public ArrayList<String> getCnames() {
        return cnames;
    }

    @NonNull
    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public int getCount() {
        return cnames.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return cnames.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_no_selection_item,parent,false);
            TextView textView = convertView.findViewById(R.id.TxtVw_NoSelection);
            textView.setText(cnames.get(position));
        }
        return convertView;
    }
}
