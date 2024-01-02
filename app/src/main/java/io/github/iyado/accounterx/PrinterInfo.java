package io.github.iyado.accounterx;


import static io.github.iyado.accounterx.CutAndDiscount.exchanges;
import static io.github.iyado.accounterx.JobActivity.insertP;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.jetbrains.annotations.Contract;
import java.util.ArrayList;


public class PrinterInfo extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView sumd,sumkd,sumr,sumt,sum;
    ArrayList<Prog> progs;
    PrinterViewAdapter printerViewAdapter;
    private DatabaseReference databaseReference;
    private ArrayList<Prog> progME;
    TextView textView11;
    private static double h;
    private Spinner spinnercur;
    private Button kshf;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer);

        recyclerView = findViewById(R.id.rec);
        sum = findViewById(R.id.sum);
        sumd = findViewById(R.id.sumd);
        sumr = findViewById(R.id.sumr);
        sumkd = findViewById(R.id.sumdk);
        sumt = findViewById(R.id.sumt);

        spinnercur = findViewById(R.id.spinnercur);

        textView11 = findViewById(R.id.textView11);

        kshf = findViewById(R.id.kshf);

        String s = getIntent().getStringExtra("user");
        h = Double.parseDouble(getIntent().getStringExtra("sum"));

        spinnercur.setAdapter(new ExAdapter(this));
        progs = new ArrayList<>();
        printerViewAdapter = new PrinterViewAdapter(s,progs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(printerViewAdapter);
        databaseReference = JobActivity.databaseReference;
        if (databaseReference == null){
            databaseReference = FirebaseDatabase.getInstance().getReference().child(JobActivity.COMPANY_NAME);
        }

        if (s != null && s.isEmpty()){
            progs.add(new Prog("فارغ","0",
                    "0","","","",
                    "","","","",""));
        }else {

            setTitle("كشف حساب السيد : "+s + "\n");
            loadUserInfo(s);
            if (h < 0){
                textView11.setText(" دائن علينا");
                textView11.setTextColor(Color.RED);
                sum.setTextColor(Color.RED);
            }else {
                textView11.setText(" مدين لنا");
                textView11.setTextColor(getColor(R.color.x500));
                sum.setTextColor(getColor(R.color.x500));
            }
        }
        kshf.setOnClickListener(v -> {
            ArrayList<Prog> xprogArrayList = new ArrayList<>();
            for (Prog xprog : progs){
                if (xprog.getEx().equals(spinnercur.getSelectedItem())){
                    xprogArrayList.add(xprog);
                }
            }
            printerViewAdapter.setProgs(xprogArrayList);
            printerViewAdapter.notifyDataSetChanged();
        });

    }

    private void loadUserInfo(String user) {


        databaseReference.child("users")
                .child(user)
                .child("accounts")
               // .child("يوليو ١٧,٢٠٢٣")
                .addChildEventListener(new ChildEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        progME = new ArrayList<>();
                        String s ;
                        double n;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            progME.add(dataSnapshot.getValue(Prog.class));
                            Prog prog = dataSnapshot.getValue(Prog.class);

                                s = prog.getEx();

                                n = Double.parseDouble(prog.getSumAll());


                                prog.setCount(String.valueOf(JobActivity.divver(s, n)));

                            progs.add(prog);
                        }
                        printerViewAdapter.notifyDataSetChanged();
                        sumd.setText(String.valueOf(sum(progs)[0]));
                        sumt.setText(String.valueOf(sum(progs)[1]));
                        sumr.setText(String.valueOf(sum(progs)[2]));
                        sumkd.setText(String.valueOf(sum(progs)[3]));
                        sum.setText(Math.round( sum(progs)[4])+"\n"+exchanges[0]);
                        Currency currency = new Currency(String.valueOf(sum(progs)[0]),
                                String.valueOf(sum(progs)[3]),
                                "0",
                                String.valueOf(sum(progs)[1]),
                                String.valueOf(sum(progs)[2]),
                                "0");
                       //insertP(user);
                        //databaseReference.child("users")
                               // .child(user)
                             //  .child("account")
                             //   .setValue(currency);
                      //  Toast.makeText(PrinterInfo.this,currency.getDolar(),Toast.LENGTH_SHORT).show();
                      //  Profits.sumProfits(getIntent().getStringExtra("user"),sum);

                    }

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        printerViewAdapter.notifyDataSetChanged();
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                        printerViewAdapter.notifyDataSetChanged();
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        printerViewAdapter.notifyDataSetChanged();
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        printerViewAdapter.notifyDataSetChanged();
                    }
                });




    }
    @NonNull
    @Contract("_ -> new")
    public static double[] sum(@NonNull ArrayList<Prog> progsx){

        double d = 0,t = 0,r = 0,k = 0,sumall = 0;
        for (Prog prog : progsx){
            String ex = prog.getEx();

            if (ex.contains("دولار")) {
                d +=  Double.parseDouble(prog.getSumAll());
            }
            if (ex.contains("تركي")){
                t += Double.parseDouble(prog.getSumAll());
            }
            if (ex.contains("ريال")){
                r += Double.parseDouble(prog.getSumAll());
            }
            if (ex.contains("يورو")){
                k += Double.parseDouble(prog.getSumAll());
            }
            sumall +=  JobActivity.divver(ex, Double.parseDouble(prog.getSumAll()));
        }

        return new double[]{d,t,r,k,sumall};
    }

}
