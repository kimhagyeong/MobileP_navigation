package com.example.navigation;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class reviseActivity extends AppCompatActivity {
    EditText departure;
    EditText destination;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //받는 뷰는 activity_revise
        setContentView(R.layout.activity_revise);

        departure = (EditText)findViewById(R.id.departure2);
        destination = (EditText)findViewById(R.id.destination2);

        Button okBtn = (Button)findViewById(R.id.button_revise);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("INPUT_TEXT1", departure.getText().toString());
                intent.putExtra("INPUT_TEXT2", destination.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });


    }
}
