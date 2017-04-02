package com.moon.picam_test_21;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    Switch set_mode;
    String host;
    Integer port;
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
        port = intent.getIntExtra("PORT", 0);
       // port = intent.getStringExtra("PORT");


        set_mode = (Switch) findViewById(R.id.switch_mode);

        set_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    Toast.makeText(SettingActivity.this, "switch_on", Toast.LENGTH_SHORT).show();
                    set_modeTask = new ServerTask(host, port, AUTO_MODE);
                    set_modeTask.execute();
                }else{
                    Toast.makeText(getApplicationContext(), "switch_off", Toast.LENGTH_SHORT).show();
                    set_modeTask = new ServerTask(host, port, PLAY_MODE);
                    set_modeTask.execute();
                }
            }
        });
    }
}
