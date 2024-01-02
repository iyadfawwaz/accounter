package io.github.iyado.accounterx;


import static io.github.iyado.accounterx.CutAndDiscount.exchanges;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateActivity extends AppCompatActivity {

    ImageView updateImage;
    Button updateButton;
    EditText updateuname, updateuid, updateacc;
    Spinner spinner;
    String username, id, account;
    String imageUrl;
    String key, oldImageURL;
    Uri uri;
    DatabaseReference databaseReference;
    Currency currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        updateButton = findViewById(R.id.updateButton);
        updateacc = findViewById(R.id.updateaccount);
        updateImage = findViewById(R.id.updateImage);
        updateuid = findViewById(R.id.updateid);
        updateuname = findViewById(R.id.updatename);

        spinner = findViewById(R.id.currency);
        spinner.setAdapter(new ExAdapter(this));

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if (data != null) {
                            uri = data.getData();
                        }
                        updateImage.setImageURI(uri);
                    } else {
                        Toast.makeText(UpdateActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            Glide.with(UpdateActivity.this).load(bundle.getString("Image")).into(updateImage);
            updateuname.setText(bundle.getString("username"));
            updateuid.setText(bundle.getString("userid"));
            updateacc.setText(bundle.getString("useraccount"));
            key = bundle.getString("Key");
            oldImageURL = bundle.getString("Image");
        }
        databaseReference = FirebaseDatabase.getInstance().getReference(JobActivity.COMPANY_NAME).child("users");

        updateImage.setOnClickListener(view -> {
            Intent photoPicker = new Intent(Intent.ACTION_PICK);
            photoPicker.setType("image/*");
            activityResultLauncher.launch(photoPicker);
        });
        updateButton.setOnClickListener(view -> {
            saveData();
            Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
    public void saveData(){
      //  storageReference = FirebaseStorage.getInstance().getReference().child(JobActivity.COMPANY_NAME).child(uri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        /*
        storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isComplete());
            Uri urlImage = uriTask.getResult();
            imageUrl = urlImage.toString();
            updateData();
            dialog.dismiss();
        }).addOnFailureListener(e -> dialog.dismiss());

         */
    }
    public void updateData(){

        username = updateuname.getText().toString().trim();
        id = updateuid.getText().toString().trim();
        account = updateacc.getText().toString();
       String cur = spinner.getSelectedItem().toString();
       if (cur.equals(exchanges[0])){
           currency = new Currency(account,"0","0","0","0","0");
       }
        if (cur.equals(exchanges[1])){
            currency = new Currency("0","0","0",account,"0","0");
        }
        if (cur.equals(exchanges[2])){
            currency = new Currency("0",account,"0","0","0","0");
        }
        if (cur.equals(exchanges[3])){
            currency = new Currency("0","0","0","0",account,"0");
        }
        if (cur.equals(exchanges[4])){
            currency = new Currency("0","0","0","0","0",account);
        }

        int[] ints = new int[]{};
        ints[0] = Integer.parseInt(account);
        Informations informations = new Informations( id, currency);

        databaseReference.setValue(informations).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
             //   StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL);
               // reference.delete();
                Toast.makeText(UpdateActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(e -> Toast.makeText(UpdateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
