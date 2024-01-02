package io.github.iyado.accounterx;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    Button button;
    //EditText cname;
    TextView textView;
    EditText editText;
  //  EditText cnamex;
    Spinner spinnerCname;
    CnameAdapter cnameAdapter;
    Button repass;
    String cname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       button = findViewById(R.id.button);
       textView = findViewById(R.id.textView);
       editText = findViewById(R.id.pass);
       spinnerCname = findViewById(R.id.cname);
       repass = findViewById(R.id.repass);


       button.setVisibility(View.INVISIBLE);

        FirebaseApp.initializeApp(this);

        cname = getIntent().getStringExtra("cname");


        initializeSpinner();
       //auth = FirebaseAuth.getInstance();

       editText.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

               textView.setText(charSequence);

                   button.setVisibility(View.VISIBLE);

                        //   CompanyRef.saveCompanyData(MainActivity.this, company);

                   doLogin();
                       //}
                  // }
               }


           @Override
           public void afterTextChanged(Editable editable) {

           }
       });
       button.setOnClickListener(view -> doLogin());

       repass.setOnClickListener(v -> {
           AlertDialog alertDialog = new AlertDialog.Builder(this)
                   .setCancelable(false)
                   .setIcon(R.drawable.user)
                   .setView(R.layout.repass)
                   .setNeutralButton("ok", (dialog, which) -> {
                       //   EditText xx = this.findViewById(R.id.editTextText);

                   })
                   .show();
           EditText editText1 = alertDialog.findViewById(R.id.editTextText);
           Button button1 = alertDialog.findViewById(R.id.button3);
           button1.setOnClickListener(v1 -> {
               String newpass = editText1.getText().toString();
               if (newpass.trim().length()>4){
                   FirebaseDatabase.getInstance().getReference()
                           .child(spinnerCname.getSelectedItem().toString())
                           .child("password")
                           .setValue(newpass);
               }
           });


       });

    }
    private void initializeSpinner(){
        ArrayList<String> cnames = new ArrayList<>();
        cnameAdapter = new CnameAdapter(this,cnames);
        spinnerCname.setAdapter(cnameAdapter);
        FirebaseDatabase.getInstance().getReference()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            cnames.add(dataSnapshot.getKey());
                        }
                        if (cnames.size()==0){
                            cnames.add(
                                    cname
                            );
                        }
                        cnameAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void doLogin(){

        FirebaseDatabase.getInstance().getReference().child(spinnerCname.getSelectedItem().toString())
                .child("password").get().addOnSuccessListener(dataSnapshot -> {
                    if (editText.getText().toString().equals( dataSnapshot.getValue())){
                        Intent intent = new Intent(this,JobActivity.class);
                        //   if (cnamex.getText().toString().trim().length()>0) {
                        intent.putExtra("cname", spinnerCname.getSelectedItem().toString());
                        startActivity(intent);
                    }else {
                        Toast.makeText(this,"كلمة مرور خاطئة... اعد المحاولة",Toast.LENGTH_SHORT).show();
                    }
                });

    //    }else {
       //     Toast.makeText(this,"ادخل اسم الشركة",Toast.LENGTH_SHORT).show();
      //  }

    }

    @Override
    protected void onStart() {
        /*
        if (auth.getCurrentUser()!=null){
            startActivity(new Intent(this,JobActivity.class));
        }

         */
        super.onStart();
    }
}
