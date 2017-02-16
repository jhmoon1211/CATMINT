package kr.ac.kpu.catmint_1;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
    String l_direction = "l", r_direction="r", d_direction = "d", u_direction = "u", sound = "sound";
    String IP="192.168.0.89";
    Integer PORT = 8888;
    TextView recieveText;
    EditText editTextAddress, editTextPort, messageText;
    ImageButton upBtn, downBtn, leftBtn, rightBtn, soundBtn, settingBtn;
    Button clearBtn, offBtn;
    WebView webView;

    ServerTask motorTask, soundTask, offTask;

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


        upBtn = (ImageButton) findViewById(R.id.button_up);
        downBtn = (ImageButton) findViewById(R.id.button_down);
        leftBtn = (ImageButton) findViewById(R.id.button_left);
        rightBtn = (ImageButton) findViewById(R.id.button_right);
        clearBtn = (Button) findViewById(R.id.button_clear);
        soundBtn = (ImageButton) findViewById(R.id.button_sound);
        offBtn = (Button) findViewById(R.id.button_off);
        settingBtn = (ImageButton) findViewById(R.id.imageButton_set);
        editTextAddress = (EditText) findViewById(R.id.host_ip);
        editTextPort = (EditText) findViewById(R.id.port);
        recieveText = (TextView) findViewById(R.id.textView_rcv);
        webView = (WebView) findViewById(R.id.web_stream);

        int default_zoom_level = 50;
        webView.setInitialScale(default_zoom_level);

        webView.post(new Runnable() {
            @Override
            public void run() {
                int width = webView.getWidth();
                int height = webView.getHeight();
                webView.loadUrl("http://192.168.0.89:8080" + "?width="+width+"&height="+height);
            }
        });

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motorTask = new ServerTask(IP, PORT, l_direction);
                motorTask.execute();
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motorTask = new ServerTask(IP, PORT, r_direction);
                motorTask.execute();
            }
        });

        upBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                motorTask = new ServerTask(IP, PORT, u_direction);
                motorTask.execute();
            }
        });

        downBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                motorTask = new ServerTask(IP, PORT, d_direction);
                motorTask.execute();
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motorTask = new ServerTask(IP, PORT, clearBtn.getText().toString());
                motorTask.execute();
            }
        });

        settingBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setting = new Intent(MainActivity.this, SettingActivity.class);
                setting.putExtra("HOST", IP);
                setting.putExtra("PORT", PORT);
                startActivity(setting);
            }
        });

        soundBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                soundTask = new ServerTask(IP, PORT, sound);
                soundTask.execute();
                Toast.makeText(MainActivity.this, "wait..", Toast.LENGTH_SHORT).show();
            }
        });

        offBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                offTask = new ServerTask(IP, PORT, offBtn.getText().toString());
                offTask.execute();
            }
        });
    }
}
