package kr.ac.kpu.catmint_1;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

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
    String l_direction = "left", r_direction="right", d_direction = "down", u_direction = "up", sound = "sound";
    String IP="192.168.0.214";
    Integer PORT = 8888;
    TextView recieveText;
    EditText editTextAddress, editTextPort, messageText;
    ImageButton upBtn, downBtn, leftBtn, rightBtn, soundBtn, settingBtn;
    Button clearBtn, offBtn, laserBtn;
    WebView webView;

    ServerTask motorTask, soundTask, laserTask;

    Socket socket = null;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(checkPlayServices())
        {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
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
        settingBtn = (ImageButton) findViewById(R.id.imageButton_set);
        soundBtn = (ImageButton) findViewById(R.id.button_sound);
        clearBtn = (Button) findViewById(R.id.button_clear);
        laserBtn = (Button) findViewById(R.id.button_laser_power);

        //editTextAddress = (EditText) findViewById(R.id.host_ip);
        //editTextPort = (EditText) findViewById(R.id.port);
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

        laserBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                laserTask = new ServerTask(IP, PORT, "laser_power".toString());
                laserTask.execute();
            }
        });
    }
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS) {
            if(apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG,"This device is not supported");
                finish();
            }
            return false;
        }
        return true;
    }
}
