package com.example.android.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.toString() ;
    private EditText etName;
    private Button btHello;
    private TextView tvHelloString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btHello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: lol");
                //Get Name
                String name = etName.getText().toString();

                //Create Hello String
                String hello = String.format("Hello %s!", name);

                //Display Hello String
                tvHelloString.setText(hello);
            }
        });
    }

}
