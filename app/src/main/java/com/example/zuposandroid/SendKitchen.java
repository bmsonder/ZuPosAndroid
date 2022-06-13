package com.example.zuposandroid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.zuposandroid.Classes.BAL.GuestCheckItemsBAL;
import com.example.zuposandroid.Classes.Functions.GeneralleFunctions;
import com.example.zuposandroid.Classes.Functions.MessagesFunctions;
import com.example.zuposandroid.Classes.Parameters.DeviceParameters;
import com.example.zuposandroid.Classes.Parameters.GuestCheckParameters;
import com.example.zuposandroid.Classes.Parameters.MessagesParameters;
import com.example.zuposandroid.Classes.Parameters.PrintServerParameters;
import com.example.zuposandroid.Classes.Parameters.UserParameters;
import com.example.zuposandroid.Classes.ViewAdapter.SluItemListViewAdapterKitchen;

import java.util.ArrayList;

public class SendKitchen extends AppCompatActivity {

    LinearLayout ActivePanel;
    RelativeLayout loadingPanel;

    Context context;

    TextView textViewTotal;

    public static ArrayList<GuestCheckItemsBAL> SelectedItemList = new ArrayList<>();

    ListView SecilenlerList;
    SluItemListViewAdapterKitchen guestCheckItemModelArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DeviceParameters.DeviceType.equals("5inc"))
            setContentView(R.layout.activity_send_kitchen5inc);
        else if (DeviceParameters.DeviceType.equals("5incCheck"))
            setContentView(R.layout.activity_send_kitchen5inc);
        else if (DeviceParameters.DeviceType.equals("8inc"))
            setContentView(R.layout.activity_send_kitchen);
        LoadViews();
    }

    void LoadViews() {
        context = this;

        GeneralleFunctions.WriteLastCheck(GuestCheckParameters.ActiveCheckSeq, context);

        ActivePanel = (LinearLayout) findViewById(R.id.ActivePanel);
        loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);

        try {
            TextView textUserName = (TextView) findViewById(R.id.textUserName);
            textUserName.setText(UserParameters.UserName.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView masaNo = (TextView) findViewById(R.id.masaNo);
        masaNo.setText(GuestCheckParameters.ActiveTableName);

        ImageView imgMain = (ImageView) findViewById(R.id.imgMain);
        imgMain.setClickable(true);
        imgMain.setOnClickListener(AnaMenuClick);
        TableRow ekle = (TableRow) findViewById(R.id.ekle);
        ekle.setClickable(true);
        ekle.setOnClickListener(EkleClick);
        TableRow iptal = (TableRow) findViewById(R.id.iptal);
        iptal.setClickable(true);
        iptal.setOnClickListener(AnaMenuClick);
        TableRow mutfak = (TableRow) findViewById(R.id.mutfak);
        mutfak.setClickable(true);
        mutfak.setOnClickListener(MutfakClick);
        TableRow transfer = (TableRow) findViewById(R.id.transfer);
        transfer.setClickable(true);
        transfer.setOnClickListener(TransferClick);
        TableRow hesap = (TableRow) findViewById(R.id.hesap);
        hesap.setClickable(true);
        hesap.setOnClickListener(HesapClick);

        textViewTotal = (TextView) findViewById(R.id.textViewTotal);
        ChangeTotal();

        SecilenlerList = (ListView) findViewById(R.id.SecilenlerList);
        LoadSelectedItem();
    }

    void MainMenu() {
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
        Intent intent = new Intent(SendKitchen.this, MainMenu.class);
        startActivity(intent);
        finish();
    }

    View.OnClickListener AnaMenuClick = new View.OnClickListener() {
        public void onClick(View v) {
            ActivePanel.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.VISIBLE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MainMenu();
                }
            }).start();
        }
    };

    View.OnClickListener EkleClick = new View.OnClickListener() {
        public void onClick(View v) {
            ActivePanel.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.VISIBLE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SelectedTable.SelectedItemList = SelectedItemList;
                    Intent intent = new Intent(SendKitchen.this, SelectedTable.class);
                    startActivity(intent);
                    finish();
                }
            }).start();
        }
    };

    View.OnClickListener TransferClick = new View.OnClickListener() {
        public void onClick(View v) {
            if (SelectedItemList.size() > 0) {
                ActivePanel.setVisibility(View.GONE);
                loadingPanel.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SendKitchen.this, Transfer.class);
                        startActivity(intent);
                        finish();
                    }
                }).start();
            } else {
                MessagesFunctions.Message(MessagesParameters.ProductNotFound, context);
            }
        }
    };

    View.OnClickListener HesapClick = new View.OnClickListener() {
        public void onClick(View v) {
            if (SelectedItemList.size() > 0) {
                ActivePanel.setVisibility(View.GONE);
                loadingPanel.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        GeneralleFunctions.WriteLastCheck("0", context);
                        GeneralleFunctions.AddGuestCheckItem(SelectedItemList, PrintServerParameters.Fatura);
                        for (int i = 0; i < SelectedItemList.size(); i++)
                            SelectedItemList.get(i).isFromDB = "1";
                        MainMenu();
                    }
                }).start();
            } else {
                MessagesFunctions.Message(MessagesParameters.ProductNotFound, context);
            }

        }
    };

    View.OnClickListener MutfakClick = new View.OnClickListener() {
        public void onClick(View v) {
            if (SelectedItemList.size() > 0) {
                ActivePanel.setVisibility(View.GONE);
                loadingPanel.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        GeneralleFunctions.WriteLastCheck("0", context);
                        GeneralleFunctions.AddGuestCheckItem(SelectedItemList, PrintServerParameters.SiparisFisi);
                        for (int i = 0; i < SelectedItemList.size(); i++)
                            SelectedItemList.get(i).isFromDB = "1";
                        MainMenu();
                    }
                }).start();
            } else {
                MessagesFunctions.Message(MessagesParameters.ProductNotFound, context);
            }
        }
    };


    void LoadSelectedItem() {
        guestCheckItemModelArrayAdapter = new SluItemListViewAdapterKitchen(context, SelectedItemList);
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
                                guestCheckItemModelArrayAdapter = new SluItemListViewAdapterKitchen(context, SelectedItemList);
                                SecilenlerList.setAdapter(guestCheckItemModelArrayAdapter);
                                ChangeTotal();
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

    void ChangeTotal() {
        double Total = 0;
        for (int i = 0; i < SelectedItemList.size(); i++) {
            Total += Double.parseDouble(SelectedItemList.get(i).totalPrice);
        }
        String total = String.valueOf(Total) + "0";
        if (GuestCheckParameters.Curency.equals("£"))
            total = GuestCheckParameters.Curency + " " + total;
        else
            total = " " + total + GuestCheckParameters.Curency;
        textViewTotal.setText(total);
    }

    @Override
    public void onBackPressed() {
        MessagesFunctions.Message(MessagesParameters.BackClick, context);
    }
}