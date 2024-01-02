package io.github.iyado.accounterx;


import static io.github.iyado.accounterx.CutAndDiscount.exchanges;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class ExAdapter extends ArrayAdapter<String> {

    Context context;
    ArrayList<String> arrayList;
    public ExAdapter(@NonNull Context context) {
        super(context, R.layout.view_list_no_selection_item,R.id.TxtVw_NoSelection);
        arrayList = new ArrayList<>();
      //  String[] strings = {"دولار امريكي","ليرة تركية","يورو اوربي","ريال سعودي","دينار كويتي"};


        for (String v : exchanges){
            arrayList.add(v);
        }

          //  arrayList.add("دولار امريكي");
       // arrayList.add("ليرة تركية");
       // arrayList.add("يورو اوربي");
      //  arrayList.add("ريال سعودي");
      //  arrayList.add("دينار كويتي");

       notifyDataSetChanged();
        context = context;



    }

    @Nullable
    @Override
    public String getItem(int position) {
        return arrayList.get(position);
    }

    public ArrayList<String> getArrayList() {
        return arrayList;
    }

    @NonNull
    @Override
    public Context getContext() {
        return context;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_no_selection_item,parent,false);
            TextView textView = convertView.findViewById(R.id.TxtVw_NoSelection);
            textView.setText(arrayList.get(position));
        }
        return convertView;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }
}
