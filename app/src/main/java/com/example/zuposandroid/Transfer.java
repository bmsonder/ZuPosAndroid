package com.example.zuposandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.zuposandroid.Classes.BAL.TableDesignDetailBAL;
import com.example.zuposandroid.Classes.Functions.GeneralleFunctions;
import com.example.zuposandroid.Classes.Functions.MessagesFunctions;
import com.example.zuposandroid.Classes.Parameters.DeviceParameters;
import com.example.zuposandroid.Classes.Parameters.GuestCheckParameters;
import com.example.zuposandroid.Classes.Parameters.MessagesParameters;
import com.example.zuposandroid.Classes.Parameters.TablesParameters;

import java.util.ArrayList;

public class Transfer extends AppCompatActivity {

    Context context;

    LinearLayout ActivePanel;
    RelativeLayout loadingPanel;

    Spinner tablesSpinner;
    Button buttonAccept;
    Button buttonReject;

    String SelectedTableNoFromSpinner = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DeviceParameters.DeviceType.equals("5inc"))
            setContentView(R.layout.activity_transfer5inc);
        else if (DeviceParameters.DeviceType.equals("5incCheck"))
            setContentView(R.layout.activity_transfer5inc);
        else if (DeviceParameters.DeviceType.equals("8inc"))
            setContentView(R.layout.activity_transfer);

        LoadViews();
    }

    void LoadViews() {
        context = this;

        ActivePanel = (LinearLayout) findViewById(R.id.ActivePanel);
        loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);

        buttonAccept = (Button) findViewById(R.id.buttonAccept);
        buttonAccept.setOnClickListener(buttonAcceptClick);

        buttonReject = (Button) findViewById(R.id.buttonReject);
        buttonReject.setOnClickListener(buttonRejectClick);

        TextView masaNo = (TextView) findViewById(R.id.masaNo);
        masaNo.setText(GuestCheckParameters.ActiveTableName);
        TextView masaNo1 = (TextView) findViewById(R.id.masaNo1);
        masaNo1.setText(GuestCheckParameters.ActiveTableName);

        GeneralleFunctions.LoadAllActiveGuestCheck();

        ArrayList<TableDesignDetailBAL> AllJoinTables = new ArrayList<>();
        for (int i = 0; i < TablesParameters.TableList.size(); i++) {
            for (int j = 0; j < TablesParameters.TableList.get(i).tableDesignDetailBALList.size(); j++) {
                boolean addTable = true;
                for (int g = 0; g < GuestCheckParameters.AllActiveGuestCheck.size(); g++) {
                    if (TablesParameters.TableList.get(i).tableDesignDetailBALList.get(j).table_design_detail_table_number.equals(GuestCheckParameters.AllActiveGuestCheck.get(g).guest_check_table_number)) {
                        addTable = false;
                        break;
                    }
                }
                if (addTable)
                    AllJoinTables.add(TablesParameters.TableList.get(i).tableDesignDetailBALList.get(j));
            }
        }

        tablesSpinner = (Spinner) findViewById(R.id.tablesSpinner);
        ArrayAdapter<TableDesignDetailBAL> tablesAdapter = new ArrayAdapter<TableDesignDetailBAL>(this, R.layout.spinner_item_table_def5inc, AllJoinTables);
        tablesSpinner.setAdapter(tablesAdapter);
        tablesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SelectedTableNoFromSpinner = AllJoinTables.get(i).table_design_detail_table_number;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    View.OnClickListener buttonRejectClick = new View.OnClickListener() {
        public void onClick(View v) {
            ActivePanel.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.VISIBLE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Transfer.this, SendKitchen.class);
                    startActivity(intent);
                    finish();
                }
            }).start();
        }
    };

    View.OnClickListener buttonAcceptClick = new View.OnClickListener() {
        public void onClick(View v) {
            if (!SelectedTableNoFromSpinner.equals(GuestCheckParameters.ActiveTableNumber)) {
                ActivePanel.setVisibility(View.GONE);
                loadingPanel.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        GeneralleFunctions.TransferTable(SelectedTableNoFromSpinner);
                        Intent intent = new Intent(Transfer.this, SendKitchen.class);
                        startActivity(intent);
                        finish();
                    }
                }).start();
            } else {
                MessagesFunctions.Message(MessagesParameters.ChooseAnotherTable, context);
            }
        }
    };

    @Override
    public void onBackPressed() {
        MessagesFunctions.Message(MessagesParameters.BackClick, context);
    }
}