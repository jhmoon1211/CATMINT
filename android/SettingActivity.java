package kr.ac.kpu.catmint_1;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class SettingActivity extends AppCompatActivity {

    Switch set_mode;
    String host, port;
    Intent intent;
    final String AUTO_MODE = "auto";
    final String PLAY_MODE = "play";
    ServerTask set_modeTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        intent = getIntent();

        host = intent.getStringExtra("HOST");
        port = intent.getStringExtra("PORT");


        set_mode = (Switch) findViewById(R.id.switch_mode);

        set_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    Toast.makeText(SettingActivity.this, "switch_on", Toast.LENGTH_SHORT).show();
                    set_modeTask = new ServerTask(host, Integer.parseInt(port), AUTO_MODE);
                    set_modeTask.execute();
                }else{
                    Toast.makeText(getApplicationContext(), "switch_off", Toast.LENGTH_SHORT).show();
                    set_modeTask = new ServerTask(host, Integer.parseInt(port), PLAY_MODE);
                    set_modeTask.execute();
                }
            }
        });
    }
}
