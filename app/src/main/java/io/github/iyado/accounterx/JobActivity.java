package io.github.iyado.accounterx;


import static io.github.iyado.accounterx.CutAndDiscount.eurocut;
import static io.github.iyado.accounterx.CutAndDiscount.exchanges;
import static io.github.iyado.accounterx.CutAndDiscount.kdcut;
import static io.github.iyado.accounterx.CutAndDiscount.realcut;
import static io.github.iyado.accounterx.CutAndDiscount.tlcut;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class JobActivity extends AppCompatActivity {
    public static String COMPANY_NAME ;
    public static HashMap<String,String> stringHashMap;
    FloatingActionButton fab;
    public static DatabaseReference databaseReference;
    ValueEventListener eventListener;
    RecyclerView recyclerView;
    ImageButton adder,printer,cutter;
    List<Informations> users;
    MyAdapter adapter;
    SearchView searchView;
    ArrayList<String> stringArrayList;
    SearchAdapter arrayAdapter;

    TextView profitv;
    String profit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);

        COMPANY_NAME = getIntent().getStringExtra("cname");
        initHasMap();

        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);

        adder = findViewById(R.id.adder);
        printer = findViewById(R.id.printer);
        cutter = findViewById(R.id.players);

        profitv = findViewById(R.id.profit);

        searchView = findViewById(R.id.search);
        searchView.clearFocus();



        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        users = new ArrayList<>();

        adapter = new MyAdapter(this, users);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference(COMPANY_NAME);
        dialog.show();
        eventListener = databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    Informations informations = itemSnapshot.getValue(Informations.class);
                    //updateStatusOnLoad(itemSnapshot.getKey());



                    if (informations != null) {
                        informations.setUsername(itemSnapshot.getKey());
                    }

                    users.add(informations);
                }

                adapter.notifyDataSetChanged();
                loadProfit();
               // insertP();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });
     //   loadProfit();
       // updateStatusOnLoad();

        adder.setOnClickListener(view -> startActivity(new Intent(JobActivity.this,AddingActivity.class)));


        printer.setOnClickListener(view ->
                {
                    Intent intent = new Intent(this,AddActivity.class);
                    intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                );

        cutter.setOnClickListener(v -> {
            Intent intent = new Intent(this,CutActivity.class);
            startActivity(intent);
        });

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, UploadActivity.class);
            startActivity(intent);
        });
        fab.setOnLongClickListener(v -> {
            printUserInfo();
            return true;
        });




    }


    public static HashMap<String,String> initHasMap() {
        stringHashMap = new HashMap<>();
        stringHashMap.put(exchanges[0],"dolar" );
        stringHashMap.put(exchanges[1],"tl" );
        stringHashMap.put(exchanges[2],"euro" );
        stringHashMap.put(exchanges[3],"real" );
        stringHashMap.put(exchanges[4],"dinar" );
        return stringHashMap;
    }

    /*
    public static void updateStatusOnLoad(@NonNull String user){
        DatabaseReference databaseReferencex =
               databaseReference.child("users").child(user);


                            databaseReferencex.child("accounts")
                     .addChildEventListener(new ChildEventListener() {
                         @Override
                         public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                             ArrayList<Prog> progArrayList = new ArrayList<>();
                             for (DataSnapshot dataSnapshoty : snapshot.getChildren()){
                                 Prog prog = dataSnapshoty.getValue(Prog.class);
                                // Toast.makeText(this,)
                                 progArrayList.add(prog);
                             }
                           //  Toast.makeText(JobActivity.this,progArrayList.get(0).getSumAll()+"",Toast.LENGTH_SHORT).show();
                             databaseReferencex.child("account")
                                     .setValue(new Currency(String.valueOf(sum(progArrayList)[0]),
                                             String.valueOf(sum(progArrayList)[3]),
                                            "0",
                                             String.valueOf(sum(progArrayList)[1]),
                                             String.valueOf(sum(progArrayList)[2]),
                                             "0"));
                         }

                         @Override
                         public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                         }

                         @Override
                         public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                         }

                         @Override
                         public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError error) {

                         }
                     });


    }


     */
    private void loadProfit() {
        double d = 0.0d;
        double t = 0.0d;
        double e = 0.0d;
        double r = 0.0d;
        for (Informations informations: users){
            //Toast.makeText(this,informations.getAccount()+"",Toast.LENGTH_SHORT).show();
            d = d + Double.parseDouble(informations.getAccount().getDolar());
            t = t + Double.parseDouble(informations.getAccount().getTl());
            e = e + Double.parseDouble(informations.getAccount().getEuro());
            r = r + Double.parseDouble(informations.getAccount().getReal());
        }

        double re = d+
                (e*eurocut)+
                (r/realcut)+
                (t/tlcut);
        double rere = Math.round(re);
        profit = String.valueOf(rere);
        profitv.setText(profit);
        if (re >= 0){
            profitv.setTextColor(Color.GREEN);
        }else {
            profitv.setTextColor(Color.RED);
        }
    }

    /*
    * @param
     */
    public static void insertP(@NonNull String user){

       // for (int i = 0; i < users.size(); i++) {
           // int finalI = i;
            databaseReference.child("users")
                    .child(user)
                    .child("accounts")
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            double ss = 0.0d;

                            double d = 0.0d;
                            double t = 0.0d;
                            double e = 0.0d;
                            double r = 0.0d;

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                Prog xp = dataSnapshot.getValue(Prog.class);
                                if (xp.getEx().equals(exchanges[0])) {
                                    d += Double.parseDouble(xp.getSumAll());
                                }
                                if (xp.getEx().equals(exchanges[1])) {
                                    t += Double.parseDouble(xp.getSumAll());
                                }
                                if (xp.getEx().equals(exchanges[2])) {
                                    e += Double.parseDouble(xp.getSumAll());
                                }
                                if (xp.getEx().equals(exchanges[3])) {
                                    r += Double.parseDouble(xp.getSumAll());
                                }

                            }
                            databaseReference.child("users").child(user)
                                    .child("account")
                                    .child(initHasMap().get(exchanges[0]))
                                    .setValue(stringo(d));
                            databaseReference.child("users").child(user)
                                    .child("account")
                                    .child(initHasMap().get(exchanges[1]))
                                    .setValue(stringo(t));
                            databaseReference.child("users").child(user)
                                    .child("account")
                                    .child(initHasMap().get(exchanges[2]))
                                    .setValue(stringo(e));
                            databaseReference.child("users").child(user)
                                    .child("account")
                                    .child(initHasMap().get(exchanges[3]))
                                    .setValue(stringo(r));

                           // databaseReference.removeEventListener(this);

                            //Toast.makeText(JobActivity.this, "ok", Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }


                    });
      //  }
    }
    @SuppressLint("NotifyDataSetChanged")
    private void printUserInfo() {
        Dialog alertDialog = new Dialog(this);
        alertDialog.setContentView(R.layout.dialog_me);

        alertDialog.setCancelable(true);
        Window window = alertDialog.getWindow();
        if (window != null) {
            window.setIcon(R.drawable.baseline_arrow_circle_down_24);

            window.setLayout(getWindow().getAttributes().width, getWindow().getAttributes().height);
        }
        alertDialog.show();

        SearchView searchView1 = alertDialog.findViewById(R.id.ser);
        RecyclerView listView = alertDialog.findViewById(R.id.lista);

        alertDialog.findViewById(R.id.floatingActionButton).setOnClickListener(view ->
                alertDialog.dismiss()
        );

        stringArrayList = new ArrayList<>();
        ArrayList<ArrayList<String>> cutArrayList = new ArrayList<>();
        HashMap<Integer, ArrayList<String>> hashMap = new HashMap<>();

        for (int i = 0; i < users.size(); i++) {
            stringArrayList.add(users.get(i).getUsername());
        }
        for (int i = 0; i < users.size(); i++) {

            int finalI = i;
            databaseReference.child("users")
                    .child(users.get(i).getUsername())
                    .child("accounts")
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            double ss = 0.0d;

                            double d = 0.0d;
                            double t = 0.0d;
                            double e = 0.0d;
                            double r = 0.0d;

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                Prog xp = dataSnapshot.getValue(Prog.class);
                                if (xp.getEx().equals(exchanges[0])) {
                                    d += Double.parseDouble(xp.getSumAll());
                                }
                                if (xp.getEx().equals(exchanges[1])) {
                                    t += Double.parseDouble(xp.getSumAll());
                                }
                                if (xp.getEx().equals(exchanges[2])) {
                                    e += Double.parseDouble(xp.getSumAll());
                                }
                                if (xp.getEx().equals(exchanges[3])) {
                                    r += Double.parseDouble(xp.getSumAll());
                                }

                            }
                           databaseReference.child("users").child(users.get(finalI).getUsername())
                                   .child("account")
                                   .child(initHasMap().get(exchanges[0]))
                                   .setValue(stringo(d));
                            databaseReference.child("users").child(users.get(finalI).getUsername())
                                    .child("account")
                                    .child(initHasMap().get(exchanges[1]))
                                    .setValue(stringo(t));
                            databaseReference.child("users").child(users.get(finalI).getUsername())
                                    .child("account")
                                    .child(initHasMap().get(exchanges[2]))
                                    .setValue(stringo(e));
                            databaseReference.child("users").child(users.get(finalI).getUsername())
                                    .child("account")
                                    .child(initHasMap().get(exchanges[3]))
                                    .setValue(stringo(r));
                            Toast.makeText(JobActivity.this,"ok",Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }


                    });


        }

        /*
        arrayAdapter =
                new SearchAdapter(alertDialog.getContext(), hashMap.get(0));
        arrayAdapter.setAlertDialog(alertDialog);
        listView.setLayoutManager(new LinearLayoutManager(alertDialog.getContext()));
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();


         */

    searchView1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            searchInLista(newText);
            return true;
        }
    });

    }

    @NonNull
    public static String stringo(double x){
        return String.valueOf(x);
    }
    @NonNull
    public static Double doublito(String s){
        return Double.parseDouble(s);
    }
    public void searchInLista(String s) {

        ArrayList<String> searchList = new ArrayList<>();

        for (String s1: stringArrayList){
            if (s1.toLowerCase().contains(s.toLowerCase())){

                searchList.add(s1);
            }
        }
        arrayAdapter.setArrayList(searchList);
    }

    public void searchList(String text){
        ArrayList<Informations> searchList = new ArrayList<>();
        for (Informations informations: users){
            if (informations.getUsername().toLowerCase().contains(text.toLowerCase())){
                searchList.add(informations);
            }
        }
        adapter.searchDataList(searchList);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.cut) {
            setcut();
            return true;
        }
        if (item.getItemId() == R.id.logout){
            FirebaseAuth.getInstance().signOut();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setcut() {
        Dialog dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.addcut);
        EditText kdcut = dialog.findViewById(R.id.kdcut);
        EditText realcut = dialog.findViewById(R.id.realcut);
        EditText eurocut = dialog.findViewById(R.id.eurocut);
        EditText tlcut = dialog.findViewById(R.id.trcut);
        Button setcut = dialog.findViewById(R.id.setcut);

        kdcut.setText(String.valueOf(CutAndDiscount.kdcut));
        eurocut.setText(String.valueOf(CutAndDiscount.eurocut));
        realcut.setText(String.valueOf(CutAndDiscount.realcut));
        tlcut.setText(String.valueOf(CutAndDiscount.tlcut));

        setcut.setOnClickListener(view -> {
            //dinar
            if (kdcut.getText().length()>0){
                String kd = kdcut.getText().toString();
                if (!kd.isEmpty()){
                    CutAndDiscount.kdcut = Double.parseDouble(kd);
                }
            }
            // real
            if (realcut.getText().length()>0){
                String rs = realcut.getText().toString();
                if (!rs.isEmpty()){
                    CutAndDiscount.realcut = Double.parseDouble(rs);
                }
            }
            // euro
            if (eurocut.getText().length()>0){
                String er = eurocut.getText().toString();
                if (!er.isEmpty()){
                    CutAndDiscount.eurocut = Double.parseDouble(er);
                }
            }
            // tl
            if (tlcut.getText().length()>0){
                String tl = tlcut.getText().toString();
                if (!tl.isEmpty()){
                    CutAndDiscount.tlcut = Double.parseDouble(tl);
                }
            }
            dialog.dismiss();

        });
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting,menu);
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        return super.onMenuOpened(featureId, menu);
    }

    public static double divver(@NonNull String s, double x){
        double dolar = 0 ;
        if (s.contains("دولار")){
            dolar = x;
        }
        else if (s.contains("دينار")){
                dolar = kdcut / x ;
        }
        else if (s.contains("ريال")){
            dolar = x /realcut;
        }else if (s.contains("تركي")){
            dolar = x / tlcut;
        } else if (s.contains("يورو")){
            dolar = x * eurocut;
        }
        return dolar;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}