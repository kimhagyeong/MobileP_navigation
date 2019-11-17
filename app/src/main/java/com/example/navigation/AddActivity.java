package com.example.navigation;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class AddActivity extends AppCompatActivity {
    //출발지와 도착지를 저장할 전역변수 선언
    EditText departure;
    EditText destination;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //받는 뷰는 activity_add
        setContentView(R.layout.activity_add);

        //입력된 값 받아오고
        departure = (EditText)findViewById(R.id.departure);
        destination = (EditText)findViewById(R.id.destination);

        Button okBtn = (Button)findViewById(R.id.button_ok);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //intent에는 name에 따라 여러 변수를 넣을 수 있다.
                intent.putExtra("INPUT_TEXT1", departure.getText().toString());
                intent.putExtra("INPUT_TEXT2", destination.getText().toString());

                setResult(RESULT_OK, intent);
                finish();
            }
        });


    }
}
