package com.example.zuposandroid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.zuposandroid.Classes.BAL.GuestCheckItemsBAL;
import com.example.zuposandroid.Classes.BAL.MenuItemsBAL;
import com.example.zuposandroid.Classes.Functions.GeneralleFunctions;
import com.example.zuposandroid.Classes.Functions.MessagesFunctions;
import com.example.zuposandroid.Classes.Parameters.DeviceParameters;
import com.example.zuposandroid.Classes.Parameters.GuestCheckParameters;
import com.example.zuposandroid.Classes.Parameters.MenuItemsParamters;
import com.example.zuposandroid.Classes.Parameters.MessagesParameters;
import com.example.zuposandroid.Classes.ViewAdapter.SluItemListViewAdapter;

import java.util.ArrayList;

public class AddCondimant extends AppCompatActivity {

    public static ArrayList<MenuItemsBAL> CondemantList;
    ArrayList<GuestCheckItemsBAL> SelectedCondemantList;

    Context context;

    LinearLayout ActivePanel;
    RelativeLayout loadingPanel;

    Button buttonAccept;
    Button buttonReject;

    TableLayout.LayoutParams p;
    TableRow.LayoutParams params;

    ListView SecilenlerList;
    SluItemListViewAdapter guestCheckItemModelArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DeviceParameters.DeviceType.equals("5inc"))
            setContentView(R.layout.activity_add_condimant5inc);
        else if (DeviceParameters.DeviceType.equals("5incCheck"))
            setContentView(R.layout.activity_add_condimant5inc);
        else if (DeviceParameters.DeviceType.equals("8inc"))
            setContentView(R.layout.activity_add_condimant);
        LoadViews();
    }

    void LoadViews() {
        context = this;

        SelectedCondemantList = new ArrayList<>();

        SecilenlerList = (ListView) findViewById(R.id.SecilenlerList);

        TextView masaNo = (TextView) findViewById(R.id.masaNo);
        masaNo.setText(GuestCheckParameters.ActiveTableName);

        ActivePanel = (LinearLayout) findViewById(R.id.ActivePanel);
        loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);

        buttonAccept = (Button) findViewById(R.id.buttonAccept);
        buttonReject = (Button) findViewById(R.id.buttonReject);
        buttonReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivePanel.setVisibility(View.GONE);
                loadingPanel.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SelectedTable.NewMemo = "";
                        SelectedTable.SelectedCondemantList = new ArrayList<>();
                        Intent intent = new Intent(AddCondimant.this, SelectedTable.class);
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
                        SelectedTable.NewMemo = "";
                        SelectedTable.SelectedCondemantList = new ArrayList<>();
                        if (SelectedCondemantList.size() > 0) {
                            AddParentItem(CondemantList.get(0));
                            SelectedTable.SelectedCondemantList = SelectedCondemantList;
                        }
                        Intent intent = new Intent(AddCondimant.this, SelectedTable.class);
                        startActivity(intent);
                        finish();
                    }
                }).start();
            }
        });

        params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        p = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, 100);
        p.setMargins(5, 1, 5, 1);


        LoadMainMenu();
    }

    void LoadMainMenu() {
        LinearLayout menuLayout = (LinearLayout) findViewById(R.id.menuLayout);
        for (int i = 0; i < CondemantList.size(); i++) {
            TableRow tableRow = new TableRow(context);
            tableRow.setId(Integer.parseInt(CondemantList.get(i).ID));
            tableRow.setTag(i);
            tableRow.setClickable(true);
            tableRow.setOnClickListener(MainMenuClick);
            tableRow.setGravity(Gravity.CENTER);
            menuLayout.addView(tableRow, p);
            tableRow.setBackgroundResource(R.drawable.back_menu);
            TextView mainMenu = new TextView(context);
            mainMenu.setText(CondemantList.get(i).Name);
            mainMenu.setTextColor(0xFF009bff);
            mainMenu.setTextSize(14);
            mainMenu.setLayoutParams(params);
            mainMenu.setGravity(Gravity.CENTER);
            tableRow.addView(mainMenu);
        }
    }

    View.OnClickListener MainMenuClick = new View.OnClickListener() {
        public void onClick(View v) {
            int position = Integer.parseInt(String.valueOf(v.getTag()));
            AddItem(CondemantList.get(position));
        }
    };

    void AddParentItem(MenuItemsBAL model) {
        GuestCheckItemsBAL newItem = new GuestCheckItemsBAL();
        newItem.item_def_seq = model.ParentID;
        newItem.item_def_name = model.ParentName;
        newItem.Price = model.ParentPrice;
        newItem.guest_check_item_type = "menuitem";
        newItem.isFromDB = "0";
        newItem.isRequite = "0";
        newItem.count = "1";
        newItem.totalPrice = String.valueOf(Double.parseDouble(newItem.Price) * Double.parseDouble(newItem.count));
        newItem.void_return_line_number = "0";
        newItem.related_void_return_line_number = newItem.void_return_line_number;
        SelectedCondemantList.add(0, newItem);
        LoadSelectedItem();
    }

    void AddItem(MenuItemsBAL model) {
        GuestCheckItemsBAL newItem = new GuestCheckItemsBAL();
        newItem.item_def_seq = model.ID;
        newItem.item_def_name = model.Name;
        newItem.Price = model.Price;
        newItem.guest_check_item_type = model.ItemType;
        newItem.isFromDB = "0";
        newItem.isRequite = "0";
        newItem.count = "1";
        newItem.totalPrice = String.valueOf(Double.parseDouble(newItem.Price) * Double.parseDouble(newItem.count));
        newItem.void_return_line_number = "0";
        newItem.related_void_return_line_number = newItem.void_return_line_number;
        SelectedCondemantList.add(0, newItem);
        LoadSelectedItem();
    }

    void LoadSelectedItem() {

        guestCheckItemModelArrayAdapter = new SluItemListViewAdapter(context, SelectedCondemantList);
        SecilenlerList.setAdapter(guestCheckItemModelArrayAdapter);
        SecilenlerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int positoin, long l) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE: {
                                GeneralleFunctions.DeleteItemInSelectetItemList(SelectedCondemantList, positoin, true);
                                guestCheckItemModelArrayAdapter = new SluItemListViewAdapter(context, SelectedCondemantList);
                                SecilenlerList.setAdapter(guestCheckItemModelArrayAdapter);
                                break;
                            }
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(MessagesParameters.DeleteProduct).setPositiveButton(MessagesParameters.Yes, dialogClickListener)
                        .setNegativeButton(MessagesParameters.No, dialogClickListener).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        MessagesFunctions.Message(MessagesParameters.BackClick, context);
    }

}