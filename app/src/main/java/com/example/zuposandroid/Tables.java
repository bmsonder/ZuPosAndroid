package com.example.zuposandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.zuposandroid.Classes.BAL.GuestCheckItemsBAL;
import com.example.zuposandroid.Classes.BAL.MenuItemsBAL;
import com.example.zuposandroid.Classes.BAL.TableDesignBAL;
import com.example.zuposandroid.Classes.Functions.GeneralleFunctions;
import com.example.zuposandroid.Classes.Functions.MessagesFunctions;
import com.example.zuposandroid.Classes.Parameters.DeviceParameters;
import com.example.zuposandroid.Classes.Parameters.GuestCheckParameters;
import com.example.zuposandroid.Classes.Parameters.MessagesParameters;
import com.example.zuposandroid.Classes.Parameters.TablesParameters;
import com.example.zuposandroid.Classes.Parameters.UserParameters;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Tables extends AppCompatActivity {

    Context context;

    final Handler handler = new Handler();
    Timer timer;

    TextView textUserName;
    Spinner tableDesignDefSpinner;

    TableLayout tablesLayout;

    ImageView imgMain;
    Button anaMenu2;

    LinearLayout ActivePanel;
    RelativeLayout loadingPanel;

    String table_design_def_seq = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DeviceParameters.DeviceType.equals("5inc"))
            setContentView(R.layout.activity_tables5inc);
        else if (DeviceParameters.DeviceType.equals("5incCheck"))
            setContentView(R.layout.activity_tables5inc);
        else if (DeviceParameters.DeviceType.equals("8inc"))
            setContentView(R.layout.activity_tables);


        LoadViews();
    }

    void LoadViews() {
        context = this;
        GeneralleFunctions.ReadandUpdateLastCheck(context);
        ActivePanel = (LinearLayout) findViewById(R.id.ActivePanel);
        loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);
        try {
            textUserName = (TextView) findViewById(R.id.textUserName);
            textUserName.setText(UserParameters.UserName.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        imgMain = (ImageView) findViewById(R.id.imgMain);
        imgMain.setClickable(true);
        imgMain.setOnClickListener(AnaMenuClick);
        anaMenu2 = (Button) findViewById(R.id.anaMenu2);
        anaMenu2.setOnClickListener(AnaMenuClick);

        timer = new Timer();
        timer.schedule(timerTask, 0, 20000);

        tablesLayout = (TableLayout) findViewById(R.id.menuLayout);
        ArrayAdapter<TableDesignBAL> tableDesignDefModelArrayAdapter;
        tableDesignDefSpinner = (Spinner) findViewById(R.id.tableDesignDefSpinner);
        tableDesignDefModelArrayAdapter = new ArrayAdapter<TableDesignBAL>(this, R.layout.spinner_item_table_def5inc, TablesParameters.TableList);
        tableDesignDefSpinner.setAdapter(tableDesignDefModelArrayAdapter);
        tableDesignDefSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                table_design_def_seq = TablesParameters.TableList.get(i).table_design_def_seq;
                LoadTables();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        LoadTables();
    }

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    LoadTables();
                }
            });
        }
    };

    View.OnClickListener AnaMenuClick = new View.OnClickListener() {
        public void onClick(View v) {
            ActivePanel.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.VISIBLE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Tables.this, MainMenu.class);
                    startActivity(intent);
                    finish();
                }
            }).start();
        }
    };

    void LoadTables() {
        tablesLayout.removeAllViews();
        GeneralleFunctions.LoadAllActiveGuestCheck();
        TableLayout.LayoutParams p = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        TableRow tableRow = new TableRow(context);
        for (int i = 0; i < TablesParameters.TableList.size(); i++) {
            if (Integer.parseInt(TablesParameters.TableList.get(i).table_design_def_seq) == Integer.parseInt(table_design_def_seq) || table_design_def_seq.equals("0")) {
                for (int j = 0; j < TablesParameters.TableList.get(i).tableDesignDetailBALList.size(); j++) {
                    if (j % TablesParameters.ColumnCount == 0) {
                        tableRow = new TableRow(context);
                        tablesLayout.addView(tableRow, p);
                    }
                    TextView tableButon = new TextView(context);
                    tableButon.setLayoutParams(TablesParameters.Params);
                    tableButon.setText(TablesParameters.TableList.get(i).tableDesignDetailBALList.get(j).table_design_detail_table_name + "\n");
                    tableButon.setId(Integer.parseInt(TablesParameters.TableList.get(i).tableDesignDetailBALList.get(j).table_design_detail_table_number));
                    tableButon.setTextSize(TablesParameters.TableTextSize);
                    tableButon.setBackgroundResource(R.drawable.back_button);
                    tableButon.setTextColor(0xFF009bff);
                    tableButon.setGravity(Gravity.CENTER);
                    tableButon.setClickable(true);
                    tableButon.setOnClickListener(OpenNewCheckClick);
                    for (int g = 0; g < GuestCheckParameters.AllActiveGuestCheck.size(); g++) {
                        try {
                            if (Integer.parseInt(GuestCheckParameters.AllActiveGuestCheck.get(g).guest_check_table_number)
                                    == Integer.parseInt(TablesParameters.TableList.get(i).tableDesignDetailBALList.get(j).table_design_detail_table_number)) {
                                tableButon.setBackgroundResource(R.drawable.back_button_red);
                                tableButon.setTextColor(Color.RED);
                                if (GuestCheckParameters.Curency.equals("£"))
                                    tableButon.setText(tableButon.getText() + GuestCheckParameters.Curency + " " + GuestCheckParameters.AllActiveGuestCheck.get(g).Total);
                                else
                                    tableButon.setText(tableButon.getText() + GuestCheckParameters.AllActiveGuestCheck.get(g).Total + " " + GuestCheckParameters.Curency);
                                if (GuestCheckParameters.AllActiveGuestCheck.get(g).guest_check_state.equals("1")) {
                                    tableButon.setOnClickListener(CantOpenCheckClick);
                                } else {
                                    tableButon.setTag(g);
                                    tableButon.setOnClickListener(OpenCheckClick);
                                    tableButon.setId(Integer.parseInt(GuestCheckParameters.AllActiveGuestCheck.get(g).guest_check_seq));
                                }
                                if (GuestCheckParameters.AllActiveGuestCheck.get(g).GuestCheckTypeID.equals("4")) {
                                    tableButon.setBackgroundResource(R.drawable.back_button_white);
                                    tableButon.setTextColor(Color.WHITE);
                                }
                            }
                        } catch (Exception ex) {

                        }
                    }
                    tableRow.addView(tableButon);
                }
                break;
            }
        }
    }

    View.OnClickListener OpenNewCheckClick = new View.OnClickListener() {
        public void onClick(View v) {
            ActivePanel.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.VISIBLE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SelectedTable.SelectedItemList = new ArrayList<>();
                    GeneralleFunctions.OpenNewCheck(String.valueOf(v.getId()));
                    Intent intent = new Intent(Tables.this, SelectedTable.class);
                    startActivity(intent);
                    finish();
                }
            }).start();
        }
    };
    View.OnClickListener OpenCheckClick = new View.OnClickListener() {
        public void onClick(View v) {
            if (GeneralleFunctions.ControlUserCanEditCheck(GuestCheckParameters.AllActiveGuestCheck.get(Integer.parseInt(String.valueOf(v.getTag()))).guest_check_open_by_employee)) {
                ActivePanel.setVisibility(View.GONE);
                loadingPanel.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int position = Integer.parseInt(String.valueOf(v.getTag()));
                        GeneralleFunctions.CheckChangeState(String.valueOf(v.getId()), "1");
                        SendKitchen.SelectedItemList = new ArrayList<>();
                        GuestCheckParameters.AllActiveGuestCheck.get(position).checkItemsBALList = GeneralleFunctions.LoadGuestCheckItems(GuestCheckParameters.AllActiveGuestCheck.get(position).guest_check_seq);
                        for (int i = 0; i < GuestCheckParameters.AllActiveGuestCheck.get(position).checkItemsBALList.size(); i++) {
                            GuestCheckItemsBAL newItem = new GuestCheckItemsBAL();
                            newItem.item_def_name = GuestCheckParameters.AllActiveGuestCheck.get(position).checkItemsBALList.get(i).item_def_name;
                            newItem.count = GuestCheckParameters.AllActiveGuestCheck.get(position).checkItemsBALList.get(i).count;
                            newItem.Price = GuestCheckParameters.AllActiveGuestCheck.get(position).checkItemsBALList.get(i).Price;
                            newItem.totalPrice = GuestCheckParameters.AllActiveGuestCheck.get(position).checkItemsBALList.get(i).totalPrice;
                            newItem.void_return_line_number = GuestCheckParameters.AllActiveGuestCheck.get(position).checkItemsBALList.get(i).void_return_line_number;
                            newItem.isFromDB = "1";
                            SendKitchen.SelectedItemList.add(newItem);
                        }
                        Intent intent = new Intent(Tables.this, SendKitchen.class);
                        startActivity(intent);
                        finish();
                    }
                }).start();
            } else {
                MessagesFunctions.Message(MessagesParameters.ChooseAnotherTable, context);
            }
        }
    };
    View.OnClickListener CantOpenCheckClick = new View.OnClickListener() {
        public void onClick(View v) {
            MessagesFunctions.Message(MessagesParameters.ChooseAnotherTable, context);
        }
    };

    @Override
    public void onBackPressed() {
        MessagesFunctions.Message(MessagesParameters.BackClick, context);
    }
}