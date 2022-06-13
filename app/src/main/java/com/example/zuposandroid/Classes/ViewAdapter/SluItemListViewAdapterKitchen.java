package com.example.zuposandroid.Classes.ViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zuposandroid.Classes.BAL.GuestCheckItemsBAL;
import com.example.zuposandroid.Classes.Parameters.DeviceParameters;
import com.example.zuposandroid.Classes.Parameters.GuestCheckParameters;
import com.example.zuposandroid.R;

import java.util.ArrayList;

public class SluItemListViewAdapterKitchen extends BaseAdapter {

    Context context;
    ArrayList<GuestCheckItemsBAL> guestCheckItemModelArrayList;
    LayoutInflater inflter;

    public SluItemListViewAdapterKitchen(Context applicationContext, ArrayList<GuestCheckItemsBAL> guestCheckItemModelArrayList) {
        this.context = context;
        this.guestCheckItemModelArrayList = guestCheckItemModelArrayList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return guestCheckItemModelArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (DeviceParameters.DeviceType.equals("5inc"))
            view = inflter.inflate(R.layout.kitchen_list_view_item5inc, null);
        else if (DeviceParameters.DeviceType.equals("8inc"))
            view = inflter.inflate(R.layout.kitchen_list_view_item, null);

        TextView nameText = (TextView) view.findViewById(R.id.nameText);
        TextView countText = (TextView) view.findViewById(R.id.countText);
        //ImageView ImgAzalt = (ImageView) view.findViewById(R.id.ImgAzalt);
        TextView priceText = (TextView) view.findViewById(R.id.priceText);
        //TextView totalPriceText = (TextView) view.findViewById(R.id.totalPriceText);

        nameText.setText(guestCheckItemModelArrayList.get(i).item_def_name);
        countText.setText(guestCheckItemModelArrayList.get(i).count);
        try {
            String itemPrice = String.format("%.02f", Float.parseFloat(guestCheckItemModelArrayList.get(i).Price)
                    * Float.parseFloat(guestCheckItemModelArrayList.get(i).count));
            if (GuestCheckParameters.Curency.equals("£"))
                priceText.setText(GuestCheckParameters.Curency + " " + itemPrice);
            else if (GuestCheckParameters.Curency.equals("TL"))
                priceText.setText(itemPrice + " " + GuestCheckParameters.Curency);
        } catch (Exception e) {
            priceText.setText("");
        }
        return view;
    }
}
