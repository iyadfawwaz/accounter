package io.github.iyado.accounterx;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;


public class DetailActivity extends AppCompatActivity {

    TextView detailDesc, detailTitle, detailLang;
    ImageView detailImage;
    FloatingActionButton deleteButton, editButton;
    String key = "";
    String imageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detials);

        detailDesc = findViewById(R.id.detailDesc);
        detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);
        detailLang = findViewById(R.id.detailLang);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            detailDesc.setText(bundle.getString("uid"));
            detailTitle.setText(bundle.getString("username"));
            detailLang.setText(bundle.getString("account"));
            key = bundle.getString("Key");
            imageUrl = bundle.getString("Image");
            Glide.with(this).load(bundle.getString("Image")).into(detailImage);
        }
        deleteButton.setOnClickListener(view -> {
        });
        editButton.setOnClickListener(view -> {
            Intent intent = new Intent(DetailActivity.this, UpdateActivity.class)
                    .putExtra("Title", detailTitle.getText().toString())
                    .putExtra("Description", detailDesc.getText().toString())
                    .putExtra("Language", detailLang.getText().toString())
                    .putExtra("Image", imageUrl)
                    .putExtra("Key", key);
            startActivity(intent);
        });
    }
}
