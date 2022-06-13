package com.example.zuposandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.zuposandroid.Classes.Functions.MessagesFunctions;
import com.example.zuposandroid.Classes.Parameters.DeviceParameters;
import com.example.zuposandroid.Classes.Parameters.MessagesParameters;
import com.example.zuposandroid.Classes.Parameters.UserParameters;

public class RVC extends AppCompatActivity {
    Context context;

    TextView textUserName;

    LinearLayout rvcLayout;

    LinearLayout ActivePanel;
    RelativeLayout loadingPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DeviceParameters.DeviceType.equals("5inc"))
            setContentView(R.layout.activity_rvc5inc);
        else if (DeviceParameters.DeviceType.equals("5incCheck"))
            setContentView(R.layout.activity_rvc5inc);
        else if (DeviceParameters.DeviceType.equals("8inc"))
            setContentView(R.layout.activity_rvc);

        LoadView();
    }

    void LoadView() {
        context = this;
        textUserName = (TextView) findViewById(R.id.textUserName);
        try {
            textUserName.setText(UserParameters.UserName.toString());
        } catch (Exception ex) {
        }

        ActivePanel = (LinearLayout) findViewById(R.id.ActivePanel);
        loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);

        rvcLayout = (LinearLayout) findViewById(R.id.menuLayout);
        for (int i = 0; i < UserParameters.userModelArrayList.size(); i++) {
            Button rvcButton = new Button(context);
            rvcButton.setBackgroundColor(Color.BLACK);
            rvcButton.setTextColor(0xFF009bff);
            rvcButton.setText(UserParameters.userModelArrayList.get(i).rvc_def_name);
            rvcButton.setBackgroundResource(R.drawable.back_button_rvc);
            rvcButton.setId(Integer.parseInt(UserParameters.userModelArrayList.get(i).rvc_def_seq));
            rvcButton.setOnClickListener(RVCClick);
            TableRow.LayoutParams paramsButton = new TableRow.LayoutParams(500, TableLayout.LayoutParams.WRAP_CONTENT);
            paramsButton.topMargin = 25;
            paramsButton.leftMargin = 5;
            paramsButton.bottomMargin = 25;
            paramsButton.rightMargin = 5;
            rvcButton.setLayoutParams(paramsButton);
            rvcLayout.addView(rvcButton);
        }
    }

    private View.OnClickListener RVCClick = new View.OnClickListener() {
        public void onClick(View v) {

            ActivePanel.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.VISIBLE);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < UserParameters.userModelArrayList.size(); i++) {
                        if (Integer.parseInt(UserParameters.userModelArrayList.get(i).rvc_def_seq) == v.getId()) {
                            UserParameters.UserID = UserParameters.userModelArrayList.get(i).user_seq;
                            UserParameters.UserName = UserParameters.userModelArrayList.get(i).userName;
                            UserParameters.RoleID = UserParameters.userModelArrayList.get(i).role_seq;
                            UserParameters.RoleName = UserParameters.userModelArrayList.get(i).role_name;
                            UserParameters.SelectedRVCID = UserParameters.userModelArrayList.get(i).rvc_def_seq;
                            UserParameters.SelectedRVCName = UserParameters.userModelArrayList.get(i).rvc_def_name;
                        }
                    }
                    Intent intent = new Intent(RVC.this, MainMenu.class);
                    startActivity(intent);
                    finish();
                }
            }).start();
        }
    };

    @Override
    public void onBackPressed() {
        MessagesFunctions.Message(MessagesParameters.BackClick, context);
    }
}