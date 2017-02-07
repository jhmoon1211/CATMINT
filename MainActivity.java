package kr.ac.kpu.catmint_1;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends Activity {


    boolean login_status = false;   //로그인상태 확인
//    Intent login = new Intent(MainActivity.this, LoginActivity.class);   //로그인 화면
    Intent setting;   //설정 화면

    TextView recieveText;
    EditText editTextAddress, editTextPort, messageText;
    Button clearBtn, upBtn, downBtn, leftBtn, rightBtn;
    ImageButton settingBtn;

    ServerTask motorTask;

    Socket socket = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*
        startActivity(new Intent(this, LoadingActivity.class));

        if(login_status == false){
            startActivity(login);
        }
*/
        setContentView(R.layout.activity_main);

        //getSupportActionBar().setElevation(0);


        upBtn = (Button) findViewById(R.id.button_up);
        downBtn = (Button) findViewById(R.id.button_down);
        leftBtn = (Button) findViewById(R.id.button_left);
        rightBtn = (Button) findViewById(R.id.button_right);
        clearBtn = (Button) findViewById(R.id.button_clear);
        settingBtn = (ImageButton) findViewById(R.id.imageButton_set);
        editTextAddress = (EditText) findViewById(R.id.host_ip);
        editTextPort = (EditText) findViewById(R.id.port);
        recieveText = (TextView) findViewById(R.id.textView_rcv);


        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motorTask = new ServerTask(editTextAddress.getText().toString(), Integer.parseInt(editTextPort.getText().toString()), leftBtn.getText().toString());
                motorTask.execute();
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motorTask = new ServerTask(editTextAddress.getText().toString(), Integer.parseInt(editTextPort.getText().toString()), rightBtn.getText().toString());
                motorTask.execute();
            }
        });

        upBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                motorTask = new ServerTask(editTextAddress.getText().toString(), Integer.parseInt(editTextPort.getText().toString()), upBtn.getText().toString());
                motorTask.execute();
            }
        });

        downBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                motorTask = new ServerTask(editTextAddress.getText().toString(), Integer.parseInt(editTextPort.getText().toString()), downBtn.getText().toString());
                motorTask.execute();
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motorTask = new ServerTask(editTextAddress.getText().toString(), Integer.parseInt(editTextPort.getText().toString()), clearBtn.getText().toString());
                motorTask.execute();
            }
        });

        settingBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setting = new Intent(MainActivity.this, SettingActivity.class);
                setting.putExtra("HOST", editTextAddress.getText().toString());
                setting.putExtra("PORT", editTextPort.getText().toString());
                startActivity(setting);
            }
        });
    }
}
