package com.example.zuposandroid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
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

import java.lang.reflect.Parameter;
import java.sql.SQLException;
import java.util.ArrayList;

public class SelectedTable extends AppCompatActivity {

    Context context;

    TextView textViewCount;
    LinearLayout menuLayout;
    LinearLayout menuIcerikLayout;
    TableLayout.LayoutParams p;
    TableRow.LayoutParams params;

    LinearLayout ActivePanel;
    RelativeLayout loadingPanel;

    ArrayList<MenuItemsBAL> itemList;

    public static ArrayList<GuestCheckItemsBAL> SelectedItemList = new ArrayList<>();
    public static String NewMemo = "";
    public static ArrayList<GuestCheckItemsBAL> SelectedCondemantList;

    ListView SecilenlerList;
    SluItemListViewAdapter guestCheckItemModelArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DeviceParameters.DeviceType.equals("5inc"))
            setContentView(R.layout.activity_selected_table5inc);
        else if (DeviceParameters.DeviceType.equals("5incCheck"))
            setContentView(R.layout.activity_selected_table5inc);
        else if (DeviceParameters.DeviceType.equals("8inc"))
            setContentView(R.layout.activity_selected_table);
        LoadView();
        ControlNewItems();
    }

    void LoadView() {
        context = this;

        GeneralleFunctions.WriteLastCheck(GuestCheckParameters.ActiveCheckSeq, context);

        TextView masaNo = (TextView) findViewById(R.id.masaNo);
        masaNo.setText(GuestCheckParameters.ActiveTableName);

        ActivePanel = (LinearLayout) findViewById(R.id.ActivePanel);
        loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);

        ImageView imgMain = (ImageView) findViewById(R.id.imgMain);
        imgMain.setClickable(true);
        imgMain.setOnClickListener(AnaMenuClick);

        Button anaMenu = (Button) findViewById(R.id.anaMenu);
        anaMenu.setOnClickListener(AnaMenuClick);

        Button notEkle = (Button) findViewById(R.id.notEkle);
        notEkle.setOnClickListener(NotEkleClick);

        Button siparisOnay = (Button) findViewById(R.id.siparisOnay);
        siparisOnay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SelectedItemList.size() > 0) {
                    ActivePanel.setVisibility(View.GONE);
                    loadingPanel.setVisibility(View.VISIBLE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SendKitchen.SelectedItemList = SelectedItemList;
                            Intent intent = new Intent(SelectedTable.this, SendKitchen.class);
                            startActivity(intent);
                            finish();
                        }
                    }).start();
                } else {
                    MessagesFunctions.Message(MessagesParameters.ProductNotFound, context);
                }

            }
        });


        TextView bir = (TextView) findViewById(R.id.bir);
        bir.setClickable(true);
        bir.setOnClickListener(NumberClick);

        TextView iki = (TextView) findViewById(R.id.iki);
        iki.setClickable(true);
        iki.setOnClickListener(NumberClick);

        TextView uc = (TextView) findViewById(R.id.uc);
        uc.setClickable(true);
        uc.setOnClickListener(NumberClick);

        TextView dort = (TextView) findViewById(R.id.dort);
        dort.setClickable(true);
        dort.setOnClickListener(NumberClick);

        TextView bes = (TextView) findViewById(R.id.bes);
        bes.setClickable(true);
        bes.setOnClickListener(NumberClick);

        TextView alti = (TextView) findViewById(R.id.alti);
        alti.setClickable(true);
        alti.setOnClickListener(NumberClick);

        TextView yedi = (TextView) findViewById(R.id.yedi);
        yedi.setClickable(true);
        yedi.setOnClickListener(NumberClick);

        TextView sekiz = (TextView) findViewById(R.id.sekiz);
        sekiz.setClickable(true);
        sekiz.setOnClickListener(NumberClick);

        TextView dokuz = (TextView) findViewById(R.id.dokuz);
        dokuz.setClickable(true);
        dokuz.setOnClickListener(NumberClick);

        TextView sifir = (TextView) findViewById(R.id.sifir);
        sifir.setClickable(true);
        sifir.setOnClickListener(NumberClick);

        TextView sil = (TextView) findViewById(R.id.sil);
        sil.setClickable(true);
        sil.setOnClickListener(NumberClick);

        TextView ok = (TextView) findViewById(R.id.ok);
        ok.setClickable(true);
        ok.setOnClickListener(NumberClick);


        itemList = new ArrayList<>();
        SecilenlerList = (ListView) findViewById(R.id.SecilenlerList);
        textViewCount = (TextView) findViewById(R.id.textViewCount);
        menuLayout = (LinearLayout) findViewById(R.id.menuLayout);
        menuIcerikLayout = (LinearLayout) findViewById(R.id.menuIcerikLayout);
        params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        p = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, 100);
        p.setMargins(5, 1, 5, 1);
        LoadMainMenu();

    }

    void ControlNewItems() {
        AddMemo(null);
        AddCondimant(false);
        LoadSelectedItem();
    }

    View.OnClickListener NumberClick = new View.OnClickListener() {
        public void onClick(View v) {
            String input = ((AppCompatTextView) v).getText().toString();
            if (input.equals("SİL")) {
                textViewCount.setText("");
            } else if (input.equals("OK")) {
            } else {
                if (String.valueOf(textViewCount.getText()).equals("")) {
                    textViewCount.setText(input);
                } else {
                    textViewCount.setText(String.valueOf(textViewCount.getText()) + input);
                }
            }
        }
    };

    View.OnClickListener AnaMenuClick = new View.OnClickListener() {
        public void onClick(View v) {
            ActivePanel.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.VISIBLE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    GeneralleFunctions.WriteLastCheck("0", context);
                    if (SelectedItemList.size() <= 0) {
                        GeneralleFunctions.CheckChangeStatusChange(GuestCheckParameters.ActiveCheckSeq, "1");
                    } else {
                        boolean changeStatus = true;
                        for (int i = 0; i < SelectedItemList.size(); i++) {
                            if (SelectedItemList.get(i).isFromDB.equals("1")) {
                                changeStatus = false;
                                break;
                            }
                        }
                        if (changeStatus)
                            GeneralleFunctions.CheckChangeStatusChange(GuestCheckParameters.ActiveCheckSeq, "1");
                    }
                    GeneralleFunctions.CheckChangeState(GuestCheckParameters.ActiveCheckSeq, "0");
                    Intent intent = new Intent(SelectedTable.this, MainMenu.class);
                    startActivity(intent);
                    finish();
                }
            }).start();
        }
    };

    void LoadMainMenu() {

        for (int i = 0; i < MenuItemsParamters.MenuItemList.size(); i++) {
            TableRow tableRow = new TableRow(context);
            tableRow.setId(Integer.parseInt(MenuItemsParamters.MenuItemList.get(i).ID));
            tableRow.setTag(i);
            tableRow.setClickable(true);
            tableRow.setOnClickListener(MainMenuClick);
            tableRow.setGravity(Gravity.CENTER);
            menuLayout.addView(tableRow, p);
            tableRow.setBackgroundResource(R.drawable.back_menu);
            TextView mainMenu = new TextView(context);
            mainMenu.setText(MenuItemsParamters.MenuItemList.get(i).Name);
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
            itemList = MenuItemsParamters.MenuItemList.get(position).MenuItemList;
            LoadMenuItems(false);
        }
    };

    View.OnClickListener NotEkleClick = new View.OnClickListener() {
        public void onClick(View v) {
            itemList = MenuItemsParamters.MenuItemNotList;
            LoadMenuItems(true);
        }
    };


    void LoadMenuItems(boolean isNot) {
        menuIcerikLayout.removeAllViews();
        for (int i = 0; i < itemList.size(); i++) {
            TableRow tableRow = new TableRow(context);
            tableRow.setId(Integer.parseInt(itemList.get(i).ID));
            tableRow.setTag(i);
            tableRow.setClickable(true);
            if (isNot)
                tableRow.setOnClickListener(NotItemClick);
            else
                tableRow.setOnClickListener(MenuItemClick);
            tableRow.setGravity(Gravity.CENTER);
            tableRow.setBackgroundResource(R.drawable.back_menu_icerik);
            menuIcerikLayout.addView(tableRow, p);
            TextView menuItem = new TextView(context);
            menuItem.setText(itemList.get(i).Name.toString().trim());
            menuItem.setLayoutParams(params);
            menuItem.setTextColor(0xFFbc80e6);
            menuItem.setTextSize(14);
            menuItem.setGravity(Gravity.CENTER);
            tableRow.addView(menuItem, params);
        }
    }

    View.OnClickListener MenuItemClick = new View.OnClickListener() {
        public void onClick(View v) {
            int position = Integer.parseInt(String.valueOf(v.getTag()));
            if (itemList.get(position).ItemType.equals("menuitem")) {
                ArrayList<MenuItemsBAL> condimantItemList = GeneralleFunctions.GetCondimants(itemList.get(position).ID, itemList.get(position).Name, itemList.get(position).Price);
                if (condimantItemList.size() <= 0)
                    AddItem(itemList.get(position));
                else {
                    itemList = condimantItemList;
                    AddCondimant(true);
                }
            } else
            {
                itemList=itemList.get(position).MenuItemList;
                LoadMenuItems(false);
            }
        }
    };

    View.OnClickListener NotItemClick = new View.OnClickListener() {
        public void onClick(View v) {
            int position = Integer.parseInt(String.valueOf(v.getTag()));
            AddMemo(itemList.get(position));
        }
    };

    int MaxVoidLine() {
        int maxParent = 0;
        for (int i = 0; i < SelectedItemList.size(); i++) {
            if (Integer.parseInt(SelectedItemList.get(i).void_return_line_number) > maxParent)
                maxParent = Integer.parseInt(SelectedItemList.get(i).void_return_line_number);
        }
        return maxParent;
    }

    String ItemCount() {
        String count = "";
        if (textViewCount.getText().toString().equals(""))
            count = "1";
        else
            count = textViewCount.getText().toString();
        return count;
    }

    void AddItem(MenuItemsBAL model) {
        GuestCheckItemsBAL newItem = new GuestCheckItemsBAL();
        newItem.item_def_seq = model.ID;
        newItem.item_def_name = model.Name;
        newItem.guest_check_item_type = model.ItemType;
        newItem.isFromDB = "0";
        newItem.isRequite = "0";
        newItem.count = ItemCount();
        newItem.Price = model.Price;
        newItem.totalPrice = String.valueOf(Double.parseDouble(newItem.Price) * Double.parseDouble(newItem.count));
        newItem.void_return_line_number = String.valueOf(MaxVoidLine() + 1);
        newItem.related_void_return_line_number = newItem.void_return_line_number;
        SelectedItemList.add(0, newItem);
        LoadSelectedItem();
    }

    void AddMemo(MenuItemsBAL model) {
        if (model == null) {
            if (!NewMemo.equals("")) {
                String memo = NewMemo;
                NewMemo = "";
                GuestCheckItemsBAL newItem = new GuestCheckItemsBAL();
                newItem.item_def_seq = "669";
                newItem.item_def_name = memo;
                newItem.guest_check_item_type = "memo";
                newItem.isFromDB = "0";
                newItem.isRequite = "0";
                newItem.void_return_line_number = String.valueOf(MaxVoidLine() + 1);
                newItem.related_void_return_line_number = newItem.void_return_line_number;
                SelectedItemList.add(0, newItem);
            }
        } else {
            if (model.Name.equals("Not Ekle")) {
                Intent intent = new Intent(SelectedTable.this, CreateMemo.class);
                startActivity(intent);
                finish();
            } else {
                GuestCheckItemsBAL newItem = new GuestCheckItemsBAL();
                newItem.item_def_seq = model.ID;
                newItem.item_def_name = model.Name;
                newItem.guest_check_item_type = model.ItemType;
                newItem.isFromDB = "0";
                newItem.isRequite = "0";
                newItem.void_return_line_number = String.valueOf(MaxVoidLine() + 1);
                newItem.related_void_return_line_number = newItem.void_return_line_number;
                SelectedItemList.add(0, newItem);
            }
        }
        LoadSelectedItem();
    }

    void AddCondimant(boolean OpenCondimantView) {
        if (OpenCondimantView) {
            ActivePanel.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.VISIBLE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    AddCondimant.CondemantList = itemList;
                    Intent intent = new Intent(SelectedTable.this, AddCondimant.class);
                    startActivity(intent);
                    finish();
                }
            }).start();
        } else {
            if (SelectedCondemantList != null) {
                if (SelectedCondemantList.size() > 0) {
                    ArrayList<GuestCheckItemsBAL> NewSelectedCondemantList = SelectedCondemantList;
                    SelectedCondemantList = new ArrayList<>();
                    GuestCheckItemsBAL newMenuItem = new GuestCheckItemsBAL();
                    newMenuItem.item_def_seq = NewSelectedCondemantList.get(0).item_def_seq;
                    newMenuItem.item_def_name = NewSelectedCondemantList.get(0).item_def_name;
                    newMenuItem.guest_check_item_type = "menuitem";
                    newMenuItem.count = ItemCount();
                    newMenuItem.Price = NewSelectedCondemantList.get(0).Price;
                    newMenuItem.totalPrice = String.valueOf(Double.parseDouble(newMenuItem.Price) * Double.parseDouble(newMenuItem.count));
                    newMenuItem.void_return_line_number = String.valueOf(MaxVoidLine() + 1);
                    newMenuItem.related_void_return_line_number = newMenuItem.void_return_line_number;
                    newMenuItem.isFromDB = "0";
                    newMenuItem.isRequite = "0";
                    SelectedItemList.add(0, newMenuItem);

                    for (int i = 1; i < NewSelectedCondemantList.size(); i++) {
                        GuestCheckItemsBAL newCondimantItem = new GuestCheckItemsBAL();
                        newCondimantItem.item_def_seq = NewSelectedCondemantList.get(i).item_def_seq;
                        newCondimantItem.item_def_name = NewSelectedCondemantList.get(i).item_def_name;
                        newCondimantItem.guest_check_item_type = NewSelectedCondemantList.get(i).guest_check_item_type;
                        newCondimantItem.isFromDB = "0";
                        newCondimantItem.isRequite = "0";
                        newCondimantItem.count = ItemCount();
                        newCondimantItem.Price = NewSelectedCondemantList.get(i).Price;
                        newCondimantItem.totalPrice = String.valueOf(Double.parseDouble(newCondimantItem.Price) * Double.parseDouble(newCondimantItem.count));
                        newCondimantItem.void_return_line_number = String.valueOf(MaxVoidLine() + 1);
                        newCondimantItem.related_void_return_line_number = newMenuItem.related_void_return_line_number;
                        SelectedItemList.add(1, newCondimantItem);
                    }
                }
            }
        }
        LoadSelectedItem();
    }


    void LoadSelectedItem() {

        guestCheckItemModelArrayAdapter = new SluItemListViewAdapter(context, SelectedItemList);
        SecilenlerList.setAdapter(guestCheckItemModelArrayAdapter);
        SecilenlerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int positoin, long l) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE: {
                                GeneralleFunctions.DeleteItemInSelectetItemList(SelectedItemList, positoin, false);
                                guestCheckItemModelArrayAdapter = new SluItemListViewAdapter(context, SelectedItemList);
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