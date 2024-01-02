package io.github.iyado.accounterx;


import static io.github.iyado.accounterx.JobActivity.databaseReference;
import static io.github.iyado.accounterx.JobActivity.initHasMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class CutActivity extends AppCompatActivity {

    Button cut;
    EditText pr1,pr2,cutpr;
    Spinner userspinner,ex1,ex2;
    CnameAdapter cnameAdapter;
    String user;
    ExAdapter exAdapter1,exAdapter2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cut);
        pr1 = findViewById(R.id.price1);
        pr2 = findViewById(R.id.price2);
        cutpr = findViewById(R.id.cutPrice);
        cut = findViewById(R.id.cuto);
        ex1 = findViewById(R.id.spinner01);
        ex2 = findViewById(R.id.spinner02);
        userspinner = findViewById(R.id.usercut);

        initializeSpinner();

        cutpr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               // double x = s;
                if (pr1.getText().toString().trim().length()>0 && s.length()>0) {
                    double h = Double.parseDouble(pr1.getText().toString())/Double.parseDouble(String.valueOf(s));

                    pr2.setText(String.valueOf(h));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cut.setOnClickListener(v -> doCut());
    }

    private void doCut() {
        user = userspinner.getSelectedItem().toString();
        if (user.trim().length()>0) {
            String p1 = pr1.getText().toString();
            String p2 = pr2.getText().toString();
            String cutp = cutpr.getText().toString();
            String e1 = ex1.getSelectedItem().toString();
            String e2 = ex2.getSelectedItem().toString();

            if (p1.trim().length() > 0 && p2.trim().length() > 0 && cutp.trim().length() > 0) {
                DatabaseReference db1 = databaseReference.child("users")
                        .child(user).child("account");
                        db1.child(initHasMap().get(e1))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                               // Currency currency = snapshot.getValue(Currency.class);
                                double x = Double.parseDouble((String) snapshot.getValue());
                                String h = String.valueOf(x-Double.parseDouble(p1));
                                db1.child(initHasMap().get(e1)).setValue(h);
                                db1.child(initHasMap().get(e2)).
                                        addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshotx) {
                                                double xi = Double.parseDouble((String) snapshotx.getValue());
                                                String hi = String.valueOf(xi+Double.parseDouble(p2));
                                                db1.child(initHasMap().get(e2)).setValue(hi);

                                                Toast.makeText(CutActivity.this, h +"converted to.: "+hi, Toast.LENGTH_SHORT).show();
                                                uploadnewData(CutActivity.this,user,p1,p2,cutp,e1,e2);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        }
    }

    //cut kayit;
    public void uploadnewData(@NonNull Activity activity, @NonNull String username,
                                  @NonNull String pr1,
                                  @NonNull String pr2 ,
                                  @NonNull String cut,
                                     @NonNull String cur1,
                                     @NonNull String cur2){

        long yourmilliseconds = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy");
        Date resultdate = new Date(yourmilliseconds);
        String time = String.valueOf(yourmilliseconds);
        String date = sdf.format(resultdate);

        Prog progF = new Prog( username,username,pr1,
                cur1,
                "0","0","سعر القص "+cut,"",
                String.valueOf(0-Double.parseDouble(pr1)),
                time,date);
        Prog progT = new Prog( username,username,pr2,
                cur2,
                "0","0", "سعر القص "+cut,"",
               pr2,
                time,date);
        DatabaseReference databaseReference1 =databaseReference
                .child("users")
                .child(username)
                .child("accounts")
                .child(date);
                databaseReference1.child(time)
                .setValue(progF);
                databaseReference1.child(String.valueOf(System.currentTimeMillis()))
                        .setValue(progT)
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(CutActivity.this,"تمت اضافة"+username+" بنجاح", Toast.LENGTH_LONG).show();
                                   finish();

                    })

                .addOnFailureListener(e -> Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT)
                        .show());
    }


    private void initializeSpinner(){

        exAdapter1 = new ExAdapter(this){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView==null){
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_no_selection_item,parent,false);
                }
                TextView textView = convertView.findViewById(R.id.TxtVw_NoSelection);
                textView.setText(getItem(position));
                textView.setOnClickListener(v -> {
                    setCountInPrice(position);
                   // Toast.makeText(parent.getContext(), exAdapter1.getItem(position),Toast.LENGTH_SHORT).show();
                });
                return super.getView(position, convertView, parent);
            }

            private void setCountInPrice(int pos) {
               databaseReference.child("users").child(userspinner.getSelectedItem().toString())
                       .child("account").child(initHasMap().get(getItem(pos)))
                       .get().addOnSuccessListener(dataSnapshot -> {

                                   pr1.setText((CharSequence) dataSnapshot.getValue());
                       });
            }
        };
        exAdapter2 = new ExAdapter(this);
        ex1.setAdapter(exAdapter1);
        ex2.setAdapter(exAdapter2);
        ArrayList<String> cnames = new ArrayList<>();
        cnameAdapter = new CnameAdapter(this,cnames);
        userspinner.setAdapter(cnameAdapter);

                databaseReference.child("users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            cnames.add(dataSnapshot.getKey());
                        }

                        if (cnames.size()==0){
                            cnames.add(
                                    "no users found....."
                            );
                        }


                        cnameAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
