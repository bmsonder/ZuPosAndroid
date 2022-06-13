package com.example.zuposandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.zuposandroid.Classes.BAL.GuestCheckItemsBAL;
import com.example.zuposandroid.Classes.Functions.GeneralleFunctions;
import com.example.zuposandroid.Classes.Functions.MessagesFunctions;
import com.example.zuposandroid.Classes.Parameters.DeviceParameters;
import com.example.zuposandroid.Classes.Parameters.GuestCheckParameters;
import com.example.zuposandroid.Classes.Parameters.MessagesParameters;
import com.example.zuposandroid.Classes.Parameters.UserParameters;

import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {
    TextView textUserName;
    TextView textMasaNo;
    TextView bir;
    TextView iki;
    TextView uc;
    TextView dort;
    TextView bes;
    TextView alti;
    TextView yedi;
    TextView sekiz;
    TextView dokuz;
    TextView sifir;
    TextView sil;
    TextView ok;

    TableRow masalar;
    TableRow masaCagir;
    TableRow cekCagir;

    Context context;

    LinearLayout ActivePanel;
    RelativeLayout loadingPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DeviceParameters.DeviceType.equals("5inc"))
            setContentView(R.layout.activity_main_menu5inc);
        else if (DeviceParameters.DeviceType.equals("5incCheck"))
            setContentView(R.layout.activity_main_menu5inc);
        else if (DeviceParameters.DeviceType.equals("8inc"))
            setContentView(R.layout.activity_main_menu);

        LoadViews();
    }

    void LoadViews() {
        context = this;

        GeneralleFunctions.ReadandUpdateLastCheck(context);

        try {
            textUserName = (TextView) findViewById(R.id.textUserName);
            textUserName.setText(UserParameters.UserName.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ActivePanel = (LinearLayout) findViewById(R.id.ActivePanel);
        loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);

        textMasaNo = (TextView) findViewById(R.id.textCheckNo);

        bir = (TextView) findViewById(R.id.bir);
        bir.setClickable(true);
        bir.setOnClickListener(NumberClick);

        iki = (TextView) findViewById(R.id.iki);
        iki.setClickable(true);
        iki.setOnClickListener(NumberClick);

        uc = (TextView) findViewById(R.id.uc);
        uc.setClickable(true);
        uc.setOnClickListener(NumberClick);

        dort = (TextView) findViewById(R.id.dort);
        dort.setClickable(true);
        dort.setOnClickListener(NumberClick);

        bes = (TextView) findViewById(R.id.bes);
        bes.setClickable(true);
        bes.setOnClickListener(NumberClick);

        alti = (TextView) findViewById(R.id.alti);
        alti.setClickable(true);
        alti.setOnClickListener(NumberClick);

        yedi = (TextView) findViewById(R.id.yedi);
        yedi.setClickable(true);
        yedi.setOnClickListener(NumberClick);

        sekiz = (TextView) findViewById(R.id.sekiz);
        sekiz.setClickable(true);
        sekiz.setOnClickListener(NumberClick);

        dokuz = (TextView) findViewById(R.id.dokuz);
        dokuz.setClickable(true);
        dokuz.setOnClickListener(NumberClick);

        sifir = (TextView) findViewById(R.id.sifir);
        sifir.setClickable(true);
        sifir.setOnClickListener(NumberClick);

        sil = (TextView) findViewById(R.id.sil);
        sil.setClickable(true);
        sil.setOnClickListener(NumberClick);

        ok = (TextView) findViewById(R.id.ok);
        ok.setClickable(true);
        ok.setOnClickListener(NumberClick);

        masalar = (TableRow) findViewById(R.id.NewCheck);
        masalar.setClickable(true);
        masalar.setOnClickListener(TablesClick);

        masaCagir = (TableRow) findViewById(R.id.masaCagir);
        masaCagir.setClickable(true);
        masaCagir.setOnClickListener(masaCagirClick);

        cekCagir = (TableRow) findViewById(R.id.cekCagir);
        cekCagir.setClickable(true);
        cekCagir.setOnClickListener(cekCagirClick);
    }

    View.OnClickListener NumberClick = new View.OnClickListener() {
        public void onClick(View v) {
            String input = ((AppCompatTextView) v).getText().toString();
            if (input.equals("SİL")) {
                textMasaNo.setText("");
            } else if (input.equals("OK")) {
            } else {
                if (String.valueOf(textMasaNo.getText()).equals("")) {
                    textMasaNo.setText(input);
                } else {
                    textMasaNo.setText(String.valueOf(textMasaNo.getText()) + input);
                }
            }
        }
    };

    View.OnClickListener TablesClick = new View.OnClickListener() {
        public void onClick(View v) {
            ActivePanel.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.VISIBLE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainMenu.this, Tables.class);
                    startActivity(intent);
                    finish();
                }
            }).start();
        }
    };

    View.OnClickListener masaCagirClick = new View.OnClickListener() {
        public void onClick(View v) {

            ActivePanel.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.VISIBLE);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (!SearchCheck(textMasaNo.getText().toString(), "")) {
                        Intent intent = new Intent(MainMenu.this, MainMenu.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }).start();
        }
    };

    View.OnClickListener cekCagirClick = new View.OnClickListener() {
        public void onClick(View v) {

            ActivePanel.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.VISIBLE);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (!SearchCheck("", textMasaNo.getText().toString())) {
                        Intent intent = new Intent(MainMenu.this, MainMenu.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }).start();
        }
    };

    boolean SearchCheck(String TableNo, String CheckNo) {
        boolean result = false;
        GeneralleFunctions.LoadAllActiveGuestCheck();
        for (int i = 0; i < GuestCheckParameters.AllActiveGuestCheck.size(); i++) {
            try {
                if (GuestCheckParameters.AllActiveGuestCheck.get(i).guest_check_table_number.equals(TableNo)) {
                    if (GuestCheckParameters.AllActiveGuestCheck.get(i).guest_check_state.equals("0")) {
                        if (GeneralleFunctions.ControlUserCanEditCheck(GuestCheckParameters.AllActiveGuestCheck.get(i).guest_check_open_by_employee)) {
                            result = true;
                            GeneralleFunctions.CheckChangeState(
                                    GuestCheckParameters.AllActiveGuestCheck.get(i).guest_check_seq, "1");
                            OpenCheckDetail(i);
                        }
                    }
                } else if (GuestCheckParameters.AllActiveGuestCheck.get(i).guest_check_number.equals(CheckNo)) {
                    if (GuestCheckParameters.AllActiveGuestCheck.get(i).guest_check_state.equals("0")) {
                        if (GeneralleFunctions.ControlUserCanEditCheck(GuestCheckParameters.AllActiveGuestCheck.get(i).guest_check_open_by_employee)) {
                            result = true;
                            GeneralleFunctions.CheckChangeState(
                                    GuestCheckParameters.AllActiveGuestCheck.get(i).guest_check_seq, "1");
                            OpenCheckDetail(i);
                        }
                    }
                }
            } catch (Exception ex) {
            }
        }
        return result;
    }

    void OpenCheckDetail(int position) {
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
        Intent intent = new Intent(MainMenu.this, SendKitchen.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        MessagesFunctions.Message(MessagesParameters.BackClick, context);
    }
}