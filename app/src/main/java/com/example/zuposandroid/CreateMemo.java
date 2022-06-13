package com.example.zuposandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zuposandroid.Classes.Functions.MessagesFunctions;
import com.example.zuposandroid.Classes.Parameters.DeviceParameters;
import com.example.zuposandroid.Classes.Parameters.GuestCheckParameters;
import com.example.zuposandroid.Classes.Parameters.MessagesParameters;

public class CreateMemo extends AppCompatActivity {
    Context context;

    Button buttonAccept;
    Button buttonReject;
    EditText memoText;
    TextView masaNo;
    Button btnCloseKeyBoard;

    LinearLayout ActivePanel;
    RelativeLayout loadingPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DeviceParameters.DeviceType.equals("5inc"))
            setContentView(R.layout.activity_create_memo5inc);
        else if (DeviceParameters.DeviceType.equals("5incCheck"))
            setContentView(R.layout.activity_create_memo5inc);
        else if (DeviceParameters.DeviceType.equals("8inc"))
            setContentView(R.layout.activity_create_memo);

        LoadlView();
    }

    void LoadlView() {
        context = this;
        btnCloseKeyBoard = (Button) findViewById(R.id.btnCloseKeyBoard);
        btnCloseKeyBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyBoard();
            }
        });

        ActivePanel = (LinearLayout) findViewById(R.id.ActivePanel);
        loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);

        buttonAccept = (Button) findViewById(R.id.buttonAccept);
        buttonReject = (Button) findViewById(R.id.buttonReject);
        memoText = (EditText) findViewById(R.id.memoText);
        masaNo = (TextView) findViewById(R.id.masaNo);
        masaNo.setText(GuestCheckParameters.ActiveTableName);
        buttonReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivePanel.setVisibility(View.GONE);
                loadingPanel.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SelectedTable.NewMemo = "";
                        Intent intent = new Intent(CreateMemo.this, SelectedTable.class);
                        startActivity(intent);
                        finish();
                    }
                }).start();
            }
        });
        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivePanel.setVisibility(View.GONE);
                loadingPanel.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SelectedTable.NewMemo = memoText.getText().toString();
                        Intent intent = new Intent(CreateMemo.this, SelectedTable.class);
                        startActivity(intent);
                        finish();
                    }
                }).start();
            }
        });
    }

    @Override
    public void onBackPressed() {
        MessagesFunctions.Message(MessagesParameters.BackClick, context);
    }

    private void closeKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}