package io.github.iyado.accounterx;


import static io.github.iyado.accounterx.CutAndDiscount.exchanges;
import static io.github.iyado.accounterx.JobActivity.databaseReference;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class UploadActivity extends AppCompatActivity {

    Button saveButton;
   public EditText userid;
   EditText usernamee;
   EditText useraccounte;
    CheckBox chf,cho;

    Map<String,Object> map;
    Spinner spinner;
    Currency currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        userid = findViewById(R.id.userid);
        usernamee = findViewById(R.id.username);
        useraccounte = findViewById(R.id.useraccount);
         chf = findViewById(R.id.chf);
         cho = findViewById(R.id.cho);
        saveButton = findViewById(R.id.saveButton);

        spinner = findViewById(R.id.curr);
        spinner.setAdapter(new ExAdapter(this));
        map = new HashMap<>();
        saveButton.setOnClickListener(view -> saveData());
    }

    public double hello(double r){
        if (chf.isChecked()){
            return -r;
        }
        if (cho.isChecked()){
            return r;
        }
        return 0;
    }
    public void saveData(){


        AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();
         String id = userid.getText().toString().trim();
          String username = usernamee.getText().toString().trim();
          String account = useraccounte.getText().toString();

         String cur = spinner.getSelectedItem().toString();
        uploadData(this,id,username,account,cur);
        dialog.dismiss();
    }

    public void uploadData(@NonNull Activity activity,@NonNull String id,
                                  @NonNull String username,
                                  @NonNull String account ,
                                  @NonNull String cur){


        Currency currency = null;
        if (cur.equals(exchanges[0])){
            currency = new Currency(String.valueOf(hello(Double.parseDouble(account))),"0","0","0","0","0");
        }
        if (cur.equals(exchanges[1])){
            currency = new Currency("0","0","0",String.valueOf(hello(Double.parseDouble(account))),"0","0");
        }
        if (cur.equals(exchanges[2])){
            currency = new Currency("0",String.valueOf(hello(Double.parseDouble(account))),"0","0","0","0");
        }
        if (cur.equals(exchanges[3])){
            currency = new Currency("0","0","0","0",String.valueOf(hello(Double.parseDouble(account))),"0");
        }
        if (cur.equals(exchanges[4])){
            currency = new Currency("0","0","0","0","0",String.valueOf(hello(Double.parseDouble(account))));
        }



        Informations userinfo = new Informations(id,currency);


        //String currentDate = DateFormat.getDateInstance().format(new Date().getDate());

        long yourmilliseconds = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy");
        Date resultdate = new Date(yourmilliseconds);
        String time = String.valueOf(yourmilliseconds);
        String date = sdf.format(resultdate);

        Prog progF = new Prog( username,username,account,
                cur,
                "0","0","افتتاحي","",
                String.valueOf(hello(Double.parseDouble(account))),
                time,date);

        Currency finalCurrency = currency;
        FirebaseDatabase.getInstance().getReference(JobActivity.COMPANY_NAME)
                .child("users")
                .child(username)
                .setValue(userinfo).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseDatabase.getInstance().getReference(JobActivity.COMPANY_NAME)
                                .child("users")
                                .child(username)
                                .child("accounts")
                                .child(date)
                                .child(time)
                                .setValue(progF)
                                .addOnCompleteListener(runnable -> {
                                    if (runnable.isSuccessful()) {
                                        databaseReference
                                                .child("users")
                                                .child(username)
                                                .child("account")
                                                .setValue(finalCurrency);
                                        //all.setText(String.valueOf(d));
                                        Toast.makeText(activity,"تمت اضافة"+username+" بنجاح", Toast.LENGTH_LONG).show();
                                       // startActivity(new Intent(this,JobActivity.class));
                                        activity.finish();
                                    }
                                });
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT)
                        .show());
    }

}
