package io.github.iyado.accounterx;


import static io.github.iyado.accounterx.JobActivity.stringHashMap;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AddingActivity extends AppCompatActivity {

    DatabaseReference reference;
    FirebaseAuth firebaseAuth;
    Spinner exs,exy;
    SpinnerAddapterx adapterF,adapterAll;
    ExAdapter exAdapter,eyAdapter;
    TextView searchF,searchT;
    EditText cy1,cy2,nz1,nz2;
    EditText prof1,prof2,customer;

    Button adder;
    Map<String, Object> map;

    ArrayList<String> arrayList,arrayListex;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding);
        adder = findViewById(R.id.button2);
        cy1 = findViewById(R.id.count1);
        cy2 = findViewById(R.id.count2);
        nz1 = findViewById(R.id.notice1);
        nz2 = findViewById(R.id.notice2);
        exs = findViewById(R.id.spinner3);
        exy = findViewById(R.id.spinner4);

        prof1 = findViewById(R.id.profit1);
        prof2 = findViewById(R.id.profit2);
        customer = findViewById(R.id.customer);

        searchF = findViewById(R.id.searchF);
        searchT = findViewById(R.id.searchT);
        map = new HashMap<>();

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            reference = FirebaseDatabase.getInstance().getReference().child(firebaseAuth.getCurrentUser().getUid())
                    .child("users");
        }
        arrayList = new ArrayList<>();
        arrayListex = new ArrayList<>();

        adapterAll = new SpinnerAddapterx(searchF,arrayList);
        exAdapter = new ExAdapter(this);
        eyAdapter = new ExAdapter(this);
        exs.setAdapter(exAdapter);
        exy.setAdapter(eyAdapter);
        exs.setEnabled(true);
        exy.setEnabled(true);

        insertInfo();


        exs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                exy.setSelection(i,true);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
       searchF.setOnClickListener(view -> dothis(searchF));
       searchT.setOnClickListener(view -> dothis(searchT));


        cy1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                cy2.setText(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        adder.setOnClickListener(view ->

                doAdd(
                        this,
                reference,
                searchF,
                searchT,
                exs,
                exy,
                cy1,
                cy2,
                prof1,
                prof2,
                customer,
                nz1, nz2));



    }

    private void dothis(TextView search) {
        adapterF = new SpinnerAddapterx(search,arrayList);
        Dialog alertDialog = new Dialog(AddingActivity.this);
        alertDialog.setContentView(R.layout.dialog_x);
        alertDialog.setCancelable(true);
        Window window = alertDialog.getWindow();
        if (window != null) {
            window.setLayout(getWindow().getAttributes().width, getWindow().getAttributes().height);

            window.setNavigationBarColor(Color.RED);
        }
        alertDialog.show();
        adapterF.setDialog(alertDialog);
        SearchView searchView = alertDialog.findViewById(R.id.serx);
        RecyclerView listView = alertDialog.findViewById(R.id.listax);

        listView.setLayoutManager(new LinearLayoutManager(alertDialog.getContext()));
        listView.setAdapter(adapterF);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                ArrayList<String> searchString = new ArrayList<>();
                for (String string : arrayList){
                    if (string.toLowerCase().contains(newText.toLowerCase())){
                        searchString.add(string);
                    }
                }
                adapterF.setmBackupStrings(searchString);

                return true;
            }
        });


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {



            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public static void doAdd(@NonNull Activity activity, @NonNull DatabaseReference reference, @NonNull TextView sender,
                             @NonNull TextView reciever,
                             @NonNull Spinner exs,
                             @NonNull Spinner exy,
                             @NonNull EditText countF,
                             @NonNull EditText countT,
                             @NonNull EditText profitF,
                             @NonNull EditText profitT,
                             @NonNull EditText customer,
                             @NonNull EditText nz1,
                             @NonNull EditText nz2) {

        long yourmilliseconds = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy");
        Date resultdate = new Date(yourmilliseconds);

        String time = String.valueOf(yourmilliseconds);
        String date = sdf.format(resultdate);

        String countf = countF.getText().toString();
        String countt = countT.getText().toString();
        String profitf = profitF.getText().toString();
        String profitt = profitT.getText().toString();
        //String countf = countF.getText().toString();
        // String countf = countF.getText().toString();

        if (countf.isEmpty()) {
            countf = "0";
        }
        if (countt.isEmpty()) {
            countt = "0";
        }
        if (profitf.isEmpty()) {
            profitf = "0";
        }
        if (profitt.isEmpty()) {
            profitt = "0";
        }
        String s1 = String.valueOf(Double.parseDouble(countf) + Double.parseDouble(profitf));
        String s2 = String.valueOf(0 - (Double.parseDouble(countt) + Double.parseDouble(profitt)));
        if (!sender.getText().toString().equals(activity.getString(R.string.sendcenter)) &&
                !reciever.getText().toString().equals(activity.getString(R.string.recievecenter)))
        {


            Prog progF = new Prog(sender.getText().toString(), reciever.getText().toString(), countf,
                    exs.getSelectedItem().toString(),
                    profitf, profitt, customer.getText().toString(), nz1.getText().toString(), s1,
                    time, date);

            Prog progT = new Prog(sender.getText().toString(), reciever.getText().toString(), countt,
                    exs.getSelectedItem().toString(),
                    profitf, profitt, customer.getText().toString(), nz2.getText().toString(), s2,
                    time, date);

            reference.child(sender.getText().toString())
                    .child("accounts")
                    //التاريخ هنا
                    .child(date)
                    // الوقت هنا
                    .child(time)

                    .setValue(progF)
                    .addOnCompleteListener(runnable -> {
                        if (runnable.isSuccessful()) {
                            reference.child(reciever.getText().toString())
                                    .child("accounts")
                                    .child(date)
                                    .child(time)
                                    .setValue(progT)
                                    .addOnCompleteListener(runnable1 -> {
                                        if (runnable1.isSuccessful()) {
                                            DatabaseReference databaseReferencex = reference.child(sender.getText().toString())
                                                    .child("account")
                                                    .child(stringHashMap.get(exs.getSelectedItem().toString()));
                                            databaseReferencex.addListenerForSingleValueEvent(
                                                    new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                            double ss = Double.parseDouble(snapshot.getValue(String.class));
                                                            databaseReferencex.setValue(String.valueOf(ss + Double.parseDouble(s1)));
                                                            DatabaseReference databaseReferencey = reference.child(reciever.getText().toString())
                                                                    .child("account")
                                                                    .child(stringHashMap.get(exs.getSelectedItem().toString()));
                                                            databaseReferencey.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    double sss = Double.parseDouble((String) snapshot.getValue());
                                                                    databaseReferencey.setValue(String.valueOf(sss + Double.parseDouble(s2)));
                                                                    Toast.makeText(activity, s1 + "تمت العملية ", Toast.LENGTH_SHORT).show();
                                                                    activity.finish();
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    }
                                            );


                                        }
                                    });

                        }


                    });

        } else {
            Toast.makeText(activity,"اضف مركز مرسل و مركز مستقبل",Toast.LENGTH_SHORT).show();
        }
    }


    private void insertInfo() {
        FirebaseDatabase.getInstance().getReference().child(JobActivity.COMPANY_NAME)
                .addChildEventListener(new ChildEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            arrayList.add(dataSnapshot.getKey());
                        }
                        adapterAll.notifyDataSetChanged();
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            arrayList.add(dataSnapshot.getKey());

                        }
                        adapterAll.notifyDataSetChanged();

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
}
