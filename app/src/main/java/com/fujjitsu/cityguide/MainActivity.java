package com.fujjitsu.cityguide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity{

    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findById();
        btn.setOnClickListener(new clickhandle());
    }



    private void findById(){
        btn = findViewById(R.id.button4);
    }


    private class clickhandle implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.button4:
                Intent intent = new Intent(MainActivity.this,MapsActivity.class);
                startActivity(intent);
                    break;
            }
        }
    }
}
