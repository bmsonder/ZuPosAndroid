package com.example.zuposandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.zuposandroid.Classes.Functions.GeneralleFunctions;
import com.example.zuposandroid.Classes.Functions.MessagesFunctions;
import com.example.zuposandroid.Classes.Parameters.DeviceParameters;
import com.example.zuposandroid.Classes.Parameters.MessagesParameters;
import com.example.zuposandroid.Classes.Parameters.UserParameters;

public class MainActivity extends AppCompatActivity {

    Context context;
    EditText passText;
    Button SettingButton;
    Button loginButton;
    LinearLayout ActivePanel;
    RelativeLayout loadingPanel;

    public static boolean loginFailed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        GeneralleFunctions.LoadAppFirstOpenParameters(context);

        if (DeviceParameters.DeviceType.equals("5inc"))
            setContentView(R.layout.activity_main5inc);
        else if (DeviceParameters.DeviceType.equals("5incCheck"))
            setContentView(R.layout.activity_main5inc);
        else if (DeviceParameters.DeviceType.equals("8inc"))
            setContentView(R.layout.activity_main);

        loadView();

        if (loginFailed) {
            loginFailed = false;
            MessagesFunctions.Message(MessagesParameters.IncorrectEntry, context);
        }
    }

    void loadView() {

        passText = (EditText) findViewById(R.id.PassText);
        SettingButton = (Button) findViewById(R.id.SettingButton);
        loginButton = (Button) findViewById(R.id.loginButton);
        SettingButton.setOnClickListener(SettingClick);
        loginButton.setOnClickListener(LoginClick);
        ActivePanel = (LinearLayout) findViewById(R.id.ActivePanel);
        loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);
    }

    View.OnClickListener SettingClick = new View.OnClickListener() {
        public void onClick(View v) {

            ActivePanel.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.VISIBLE);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, Setting.class);
                    startActivity(intent);
                    finish();
                }
            }).start();
        }
    };

    View.OnClickListener LoginClick = new View.OnClickListener() {
        public void onClick(View v) {
            ActivePanel.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.VISIBLE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean reult = GeneralleFunctions.Login(passText.getText().toString(), context);
                    if (reult) {
                        if (UserParameters.userModelArrayList.size() == 1) {
                            Intent intent = new Intent(MainActivity.this, MainMenu.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(MainActivity.this, RVC.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        MainActivity.loginFailed = true;
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }
            }).start();
        }
    };

    @Override
    public void onBackPressed() {
        MessagesFunctions.Message(MessagesParameters.BackClick, context);
    }
}