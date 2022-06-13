package com.example.zuposandroid.Classes.ViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zuposandroid.Classes.BAL.GuestCheckItemsBAL;
import com.example.zuposandroid.Classes.BAL.MenuItemsBAL;
import com.example.zuposandroid.Classes.Parameters.DeviceParameters;
import com.example.zuposandroid.R;

import java.util.ArrayList;

public class SluItemListViewAdapter extends BaseAdapter {
    Context context;
    ArrayList<GuestCheckItemsBAL> itemList;
    LayoutInflater inflter;

    public SluItemListViewAdapter(Context applicationContext, ArrayList<GuestCheckItemsBAL> itemList) {
        this.context = context;
        this.itemList = itemList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return itemList.size();
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
            view = inflter.inflate(R.layout.list_view_item5inc, null);
        else if (DeviceParameters.DeviceType.equals("5incCheck"))
            view = inflter.inflate(R.layout.list_view_item5inc, null);
        else if (DeviceParameters.DeviceType.equals("8inc"))
            view = inflter.inflate(R.layout.list_view_item, null);
        TextView countText = (TextView) view.findViewById(R.id.countText);
        TextView nameText = (TextView) view.findViewById(R.id.nameText);
        countText.setText(itemList.get(i).count);
        nameText.setText(itemList.get(i).item_def_name);
        return view;
    }
}
