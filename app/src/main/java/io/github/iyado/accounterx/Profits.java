package io.github.iyado.accounterx;


import static io.github.iyado.accounterx.JobActivity.COMPANY_NAME;

import android.widget.TextView;

import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profits {

    public static void sumProfits(@NonNull String user, TextView all){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(COMPANY_NAME)
                .child("users")
                .child(user);
                databaseReference.child("accounts")
                        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                double s = 0.0d;
                for (DataSnapshot y : snapshot.getChildren()) {

                    for (DataSnapshot ss : y.getChildren()) {
                        s +=  Double.parseDouble(ss.getValue(Prog.class).getSumAll());
                    }
                }

                editAccount(s);


            }
            private void editAccount(double d) {

              //  databaseReference.setValue(""+d);
                all.setText(String.valueOf(d));



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
