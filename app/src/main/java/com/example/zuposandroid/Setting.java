package com.example.zuposandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zuposandroid.Classes.Functions.FileFunctions;
import com.example.zuposandroid.Classes.Functions.GeneralleFunctions;
import com.example.zuposandroid.Classes.Functions.MessagesFunctions;
import com.example.zuposandroid.Classes.Parameters.DBParameters;
import com.example.zuposandroid.Classes.Parameters.DeviceParameters;
import com.example.zuposandroid.Classes.Parameters.MessagesParameters;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class Setting extends AppCompatActivity {

    Context context;

    TextView lblVersion;

    Button SaveButton;
    Button vazgecButton;
    EditText ipText;
    EditText dbNameText;
    EditText userNameText;
    EditText passwordText;

    LinearLayout ActivePanel;
    RelativeLayout loadingPanel;

    RadioButton radio5Inc;
    RadioButton radio8Inc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DeviceParameters.DeviceType.equals("5inc"))
            setContentView(R.layout.activity_setting5inc);
        else if (DeviceParameters.DeviceType.equals("5incCheck"))
            setContentView(R.layout.activity_setting5inc);
        else if (DeviceParameters.DeviceType.equals("8inc"))
            setContentView(R.layout.activity_setting);


        context = this;

        lblVersion = (TextView) findViewById(R.id.lblVersion);
        lblVersion.setText("V1.0.0.7");

        ActivePanel = (LinearLayout) findViewById(R.id.ActivePanel);
        loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);

        ipText = (EditText) findViewById(R.id.ipText);
        dbNameText = (EditText) findViewById(R.id.dbNameText);
        userNameText = (EditText) findViewById(R.id.userNameText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        radio5Inc = (RadioButton) findViewById(R.id.radio5Inc);
        radio8Inc = (RadioButton) findViewById(R.id.radio8Inc);

        if (DeviceParameters.DeviceType.equals("5inc"))
            radio5Inc.setChecked(true);
        if (DeviceParameters.DeviceType.equals("8inc"))
            radio8Inc.setChecked(true);

        String FILENAME = "dosya.txt";

        ipText.setText(DBParameters.IP);
        dbNameText.setText(DBParameters.DATABASE_NAME);
        userNameText.setText(DBParameters.DATABASE_USERNAME);
        passwordText.setText(DBParameters.DATABASE_PASSWORD);


        vazgecButton = (Button) findViewById(R.id.vazgecButton);
        vazgecButton.setOnClickListener(vazgecButtonClick);
        SaveButton = (Button) findViewById(R.id.SaveButton);
        SaveButton.setOnClickListener(SaveButtonClick);
    }

    View.OnClickListener vazgecButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            ActivePanel.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.VISIBLE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Setting.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).start();

        }
    };

    View.OnClickListener SaveButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            if (!ipText.getText().equals(null) && !ipText.getText().equals("")) {
                if (!dbNameText.getText().equals(null) && !dbNameText.getText().equals("")) {
                    if (!userNameText.getText().equals(null) && !userNameText.getText().equals("")) {
                        if (!passwordText.getText().equals(null) && !passwordText.getText().equals("")) {

                            ActivePanel.setVisibility(View.GONE);
                            loadingPanel.setVisibility(View.VISIBLE);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    DBParameters.IP = ipText.getText().toString();
                                    DBParameters.DATABASE_NAME = dbNameText.getText().toString();
                                    DBParameters.DATABASE_USERNAME = userNameText.getText().toString();
                                    DBParameters.DATABASE_PASSWORD = passwordText.getText().toString();
                                    if (radio5Inc.isChecked())
                                        DeviceParameters.DeviceType = "5inc";
                                    if (radio8Inc.isChecked())
                                        DeviceParameters.DeviceType = "8inc";
                                    FileFunctions.Write(context, "dosya.txt"
                                            , DBParameters.IP + "@" + DBParameters.DATABASE_NAME + "@"
                                                    + DBParameters.DATABASE_USERNAME + "@" + DBParameters.DATABASE_PASSWORD + "@" + DeviceParameters.DeviceType);
                                    Intent intent = new Intent(Setting.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }).start();
                        } else {
                            MessagesFunctions.Message(MessagesParameters.PasswordEmpty, context);
                        }
                    } else {
                        MessagesFunctions.Message(MessagesParameters.UserNameEmpty, context);
                    }
                } else {
                    MessagesFunctions.Message(MessagesParameters.DBEmpty, context);
                }
            } else {
                MessagesFunctions.Message(MessagesParameters.IPEmpty, context);
            }
        }
    };

    @Override
    public void onBackPressed() {
        MessagesFunctions.Message(MessagesParameters.BackClick, context);
    }
}