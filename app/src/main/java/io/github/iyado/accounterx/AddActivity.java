package io.github.iyado.accounterx;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddActivity extends AppCompatActivity {

    /*

        DatabaseReference reference;
        Spinner exs,exy;
        SpinnerAddapterx adapterF,adapterAll;
        ExAdapter exAdapter,eyAdapter;
        TextView searchF,searchT;
        EditText cy1,cy2,nz1,nz2;
        EditText prof1,prof2,customer;

        Button adder;
        Map<String, Object> map;
        HashMap<String,String> stringHashMap;
        Double newAcc;
        ArrayList<String> arrayList,arrayListex;

     */
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_adding);
            /*
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
            stringHashMap = new HashMap<>();

            reference = FirebaseDatabase.getInstance().getReference().child(JobActivity.COMPANY_NAME)
                    .child("users");
            arrayList = new ArrayList<>();
            arrayListex = new ArrayList<>();

            adapterAll = new SpinnerAddapterx(searchF,arrayList);
            exAdapter = new ExAdapter(this);
            eyAdapter = new ExAdapter(this);
            exs.setAdapter(exAdapter);
            exy.setAdapter(eyAdapter);
            exs.setEnabled(true);
            exy.setEnabled(true);


            stringHashMap.put(exchanges[0],"dolar" );
            stringHashMap.put(exchanges[1],"tl" );
            stringHashMap.put(exchanges[2],"euro" );
            stringHashMap.put(exchanges[3],"real" );
            stringHashMap.put(exchanges[4],"dinar" );


            //enumMap = new EnumMap(CurrEx);
            //CurrEx currEx = CurrEx.TL;

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
            finish();

            adder.setOnLongClickListener(view -> {
                cy2.setText(String.valueOf(reloadAccount(searchF)+12));
                return true;
            });

        }

        private void dothis(TextView search) {
            adapterF = new SpinnerAddapterx(search,arrayList);
            Dialog alertDialog = new Dialog(this);
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

        private double reloadAccount(@NonNull TextView spinner){
            final double[] m = {0.0};
            FirebaseDatabase.getInstance().getReference().child(JobActivity.COMPANY_NAME).child("users")
                    .child(spinner.getText().toString()).get()
                    .addOnCompleteListener(runnable -> {
                        if (runnable.isSuccessful()){
                            m[0] = Double.parseDouble(runnable.getResult().getValue(Informations.class).getAccount().getDolar());

                        }

                    });
            return m[0];

        }

        public void doAdd(  @NonNull TextView sender,
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

            if (countf.isEmpty() ){
                countf = "0";
            }
            if (countt.isEmpty()){
                countt = "0";
            }
            if (profitf.isEmpty()){
                profitf = "0";
            }
            if (profitt.isEmpty()){
                profitt = "0";
            }
            String s1 = String.valueOf(Double.parseDouble( countf) + Double.parseDouble( profitf));
            String s2 = String.valueOf(0 - (Double.parseDouble( countt) + Double.parseDouble( profitt)));
            Prog progF = new Prog( sender.getText().toString(),reciever.getText().toString(),countf,
                    exs.getSelectedItem().toString(),
                    profitf,profitt,customer.getText().toString(),nz1.getText().toString(),s1,
                    time,date);

            Prog progT = new Prog( sender.getText().toString(),reciever.getText().toString(),countt,
                    exs.getSelectedItem().toString(),
                    profitf,profitt,customer.getText().toString(),nz2.getText().toString(),s2,
                    time,date);

            reference.child( sender.getText().toString())
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
                                            DatabaseReference databaseReferencex =  reference.child(sender.getText().toString())
                                                    .child("account")
                                                    .child(stringHashMap.get(exs.getSelectedItem().toString()));
                                            databaseReferencex.addListenerForSingleValueEvent(
                                                    new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                            double ss = Double.parseDouble( snapshot.getValue(String.class));
                                                            databaseReferencex.setValue(String.valueOf(ss+Double.parseDouble(s1)));
                                                            DatabaseReference databaseReferencey = reference.child(reciever.getText().toString())
                                                                    .child("account")
                                                                    .child(stringHashMap.get(exs.getSelectedItem().toString()));
                                                            databaseReferencey.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    double sss = Double.parseDouble((String) snapshot.getValue());
                                                                    databaseReferencey.setValue(String.valueOf(sss+Double.parseDouble(s2)));
                                                                    Toast.makeText(AddActivity.this,s1+"تمت العملية ",Toast.LENGTH_SHORT).show();
                                                                    finish();
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

             */
    }
}

