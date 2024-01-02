package io.github.iyado.accounterx;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    Button login;
    TextView alert;
    EditText user,pass;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login);
        alert = findViewById(R.id.alert);
        user = findViewById(R.id.userText2);
        pass = findViewById(R.id.passtText3);

        firebaseAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(v -> dologin());

    }

    private void dologin() {

        if (user.getText().toString().trim().length()> 0 && pass.getText().toString().trim().length()>0){
            firebaseAuth.signInWithEmailAndPassword(user.getText().toString(),
                    pass.getText().toString())
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()){
                            jump();
                        }
                    }).addOnFailureListener(e -> {
                        alert.setText(e.getMessage());
                    });
        }
    }
    private void jump(){
        Intent intent = new Intent(this,JobActivity.class);
        intent.putExtra("cname",firebaseAuth.getCurrentUser().getUid());
        startActivity(intent);
    }

    @Override
    protected void onStart() {

        if (firebaseAuth.getCurrentUser() != null) {
            jump();
        }
        super.onStart();
    }
}
