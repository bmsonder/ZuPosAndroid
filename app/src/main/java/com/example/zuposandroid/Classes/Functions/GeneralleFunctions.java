package com.example.zuposandroid.Classes.Functions;

import android.content.Context;
import android.os.Build;
import android.widget.TableRow;

import com.example.zuposandroid.Classes.BAL.AccessRightBAL;
import com.example.zuposandroid.Classes.BAL.ErrorLogBAL;
import com.example.zuposandroid.Classes.BAL.GuestCheckBAL;
import com.example.zuposandroid.Classes.BAL.GuestCheckItemsBAL;
import com.example.zuposandroid.Classes.BAL.MenuItemsBAL;
import com.example.zuposandroid.Classes.BAL.Models.InsertGuestCheckItemModel;
import com.example.zuposandroid.Classes.BAL.Models.InsertGuestCheckItemRelationDetailModel;
import com.example.zuposandroid.Classes.BAL.Models.InsertGuestCheckModel;
import com.example.zuposandroid.Classes.BAL.Models.InsertPrintServerModel;
import com.example.zuposandroid.Classes.BAL.PropertiesBAL;
import com.example.zuposandroid.Classes.BAL.TableDesignBAL;
import com.example.zuposandroid.Classes.BAL.UserBAL;
import com.example.zuposandroid.Classes.Parameters.DBParameters;
import com.example.zuposandroid.Classes.Parameters.DeviceParameters;
import com.example.zuposandroid.Classes.Parameters.GuestCheckParameters;
import com.example.zuposandroid.Classes.Parameters.MenuItemsParamters;
import com.example.zuposandroid.Classes.Parameters.PrintServerParameters;
import com.example.zuposandroid.Classes.Parameters.Properties;
import com.example.zuposandroid.Classes.Parameters.TablesParameters;
import com.example.zuposandroid.Classes.Parameters.UserParameters;
import com.example.zuposandroid.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Parameter;
import java.sql.SQLException;
import java.util.ArrayList;

public class GeneralleFunctions {

    public static void LoadAppFirstOpenParameters(Context context) {
        GeneralleFunctions.LoadDBParameters(context);
        GeneralleFunctions.LoadTerminalParameters();
        MessagesFunctions.LoadMessages();
    }

    public static boolean Login(String Password, Context context) {
        boolean result = false;
        try {
            UserBAL userDBFunctions = new UserBAL();
            UserParameters.userModelArrayList = userDBFunctions.SearchUser(Password);
            if (UserParameters.userModelArrayList != null) {
                if (UserParameters.userModelArrayList.size() > 0) {
                    UserParameters.UserID = UserParameters.userModelArrayList.get(0).user_seq;
                    UserParameters.UserName = UserParameters.userModelArrayList.get(0).userName;
                    if (UserParameters.userModelArrayList.size() == 1) {
                        UserParameters.RoleID = UserParameters.userModelArrayList.get(0).role_seq;
                        UserParameters.RoleName = UserParameters.userModelArrayList.get(0).role_name;
                        UserParameters.SelectedRVCID = UserParameters.userModelArrayList.get(0).rvc_def_seq;
                        UserParameters.SelectedRVCName = UserParameters.userModelArrayList.get(0).rvc_def_name;
                    }
                    GeneralleFunctions.LoadAfterLoginParameters(context);
                    result = true;
                }
            }
        } catch (Exception ex) {
        }
        return result;
    }

    public static void LoadAllActiveGuestCheck() {
        GuestCheckBAL guestCheckBAL = new GuestCheckBAL();
        GuestCheckParameters.AllActiveGuestCheck = guestCheckBAL.SelectAllActiveCheck(UserParameters.SelectedRVCID, "");
    }

    public static ArrayList<GuestCheckItemsBAL> LoadGuestCheckItems(String guest_check_seq) {
        ArrayList<GuestCheckItemsBAL> result = new ArrayList<>();
        GuestCheckItemsBAL guestCheckItemsBAL = new GuestCheckItemsBAL();
        result = guestCheckItemsBAL.SelectAllItemsByGuestCheckSeq(guest_check_seq);
        return result;
    }

    public static void TransferTable(String NewTableNumber) {
        GuestCheckBAL guestCheckBAL = new GuestCheckBAL();
        String newGuestCheck = guestCheckBAL.TableTransfer(GuestCheckParameters.ActiveCheckSeq, NewTableNumber);
        GeneralleFunctions.CheckChangeState(newGuestCheck, "1");
    }

    public static void OpenNewCheck(String TableNo) {
        InsertGuestCheckModel model = new InsertGuestCheckModel();
        model.guest_check_number = " (select (case when max(guest_check_number) is null then 100 else max(guest_check_number)+1  end ) " +
                " from GuestCheck where bussiness_date=(SELECT BussinesDate FROM [EndOfDay] where  EndOfDay_seq_id=(select max(EndOfDay_seq_id) from [EndOfDay]))) ";
        model.guest_check_id = "";
        model.guest_check_status = "0";
        model.guest_check_state = "1";
        model.guest_check_order_type_no = "1";
        model.guest_check_open_time = "CONVERT(VARCHAR(5), GETDATE(), 108)";
        model.guest_check_open_date = "getDate()";
        model.guest_check_open_by_cashier = UserParameters.UserID;
        model.guest_check_open_by_employee = UserParameters.UserID;
        model.guest_check_total = "0";
        model.guest_check_discount_total = "0";
        model.bussiness_date = "(SELECT BussinesDate  FROM [EndOfDay] where  EndOfDay_seq_id=(select max(EndOfDay_seq_id) from [EndOfDay]))";
        model.rvc_def_seq = UserParameters.SelectedRVCID;
        model.terminal_def_seq = DeviceParameters.SerialNo;
        model.customer_def_seq = "00000000-0000-0000-0000-000000000000";
        model.guest_check_close_time = "''";
        model.guest_check_close_date = "getDate()";
        model.guest_check_payment_total = "0";
        model.guest_check_table_number = TableNo;
        model.guest_check_group_number = "1";
        model.standalone_guest_check_number = "(select newid())";
        model.UpdateDate = "getDate()";
        model.TransactionStatus = "0";
        model.isStockTransfer = "0";
        GuestCheckBAL guestCheckBAL = new GuestCheckBAL();
        GeneralleFunctions.CheckChangeState(guestCheckBAL.InsertGuestCheck(model).get(0).guest_check_seq, "1");
    }

    public static void AddGuestCheckItem(ArrayList<GuestCheckItemsBAL> itemList, String PrintFormat) {
        boolean AddPrintServer = true;
        GuestCheckItemsBAL guestCheckItemsBAL = new GuestCheckItemsBAL();
        GuestCheckBAL guestCheckBAL = new GuestCheckBAL();
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).isFromDB.equals("0")) {
                AddPrintServer = true;
                InsertGuestCheckItemModel model = new InsertGuestCheckItemModel();
                model.item_def_seq = itemList.get(i).item_def_seq;
                model.item_def_name = "'" + itemList.get(i).item_def_name.trim() + "'";
                model.guest_check_item_count = itemList.get(i).count;
                model.guest_check_item_price = itemList.get(i).Price;
                model.guest_check_item_type = "'" + itemList.get(i).guest_check_item_type + "'";
                model.guest_check_item_date = "GETDATE()";
                model.guest_check_item_time = "CONVERT(VARCHAR(5), GETDATE(), 108)";
                model.guest_check_item_employee = UserParameters.UserID;
                model.business_date = "(select (case when max(guest_check_number) is null then 100 else max(guest_check_number)+1  end )" +
                        "  from GuestCheck where bussiness_date=(SELECT BussinesDate FROM [EndOfDay] where  EndOfDay_seq_id=(select max(EndOfDay_seq_id) from [EndOfDay])))";
                model.guest_check_item_unit_type = "'pcs'";
                model.guest_check_item_status = "1";
                model.void_return_line_number = itemList.get(i).void_return_line_number;
                model.void_return_count = "0";
                model.guest_check_seq = GuestCheckParameters.ActiveCheckSeq;
                model.guest_check_line_print_status = "0";
                model.IsTransfer = "0";
                model.TransferSeqID = "0";
                model.DiscountitemSeq = "0";
                model.NRACode = "'1111'";
                model.mi_barcode_id = "'0'";
                InsertGuestCheckItemRelationDetailModel detailMode = new InsertGuestCheckItemRelationDetailModel();
                detailMode.item_def_seq = "";
                if (itemList.get(i).guest_check_item_type.equals("condiment")) {
                    for (int j = 0; j < itemList.size(); j++) {
                        if (itemList.get(i).related_void_return_line_number.equals(itemList.get(j).related_void_return_line_number)) {
                            if (itemList.get(j).guest_check_item_type.equals("condiment"))
                                detailMode = GeneralleFunctions.AddGuestCheckItemRelationDetail(itemList.get(j), itemList.get(i));
                        }
                    }
                }
                guestCheckItemsBAL.InsertGuestCheckItemWihtGuestCheckItemRelationDetail(model, detailMode);
            }
        }
        if (AddPrintServer) {
            GeneralleFunctions.AddPrintServer(PrintFormat);
            if (PrintFormat.equals(PrintServerParameters.Fatura))
                guestCheckBAL.UpdateGuestCheckTypeID(GuestCheckParameters.ActiveCheckSeq, "4");
        }

    }

   /* public static void AddLog(String Message) {
        ErrorLogBAL errorLogBAL = new ErrorLogBAL();
        errorLogBAL.AddLog(Message);
    }*/

    private static InsertGuestCheckItemRelationDetailModel AddGuestCheckItemRelationDetail(GuestCheckItemsBAL condimantItem, GuestCheckItemsBAL menuItem) {
        InsertGuestCheckItemRelationDetailModel model = new InsertGuestCheckItemRelationDetailModel();
        model.item_def_seq = " (select max(guest_check_item_seq) from GuestCheckItems) ";
        model.guest_check_seq = GuestCheckParameters.ActiveCheckSeq;
        model.requirmend = condimantItem.isRequite;
        model.guest_check_item_type = condimantItem.guest_check_item_type;
        model.related_void_return_line_number = condimantItem.related_void_return_line_number;
        model.referance_item_seq = menuItem.item_def_seq;
        return model;
    }

    private static void AddPrintServer(String PrintFormat) {
        GuestCheckBAL guestCheckBAL = new GuestCheckBAL();
        InsertPrintServerModel model = new InsertPrintServerModel();
        model.guest_check_seq = GuestCheckParameters.ActiveCheckSeq;
        model.format_profile_def_seq = "12";
        model.printer_profile_def_seq = "1";
        model.PrintFunction = "'" + PrintFormat + "'";
        model.TerminalDefSeq = DeviceParameters.SerialNo;
        model.Status = "1";
        model.UpdateDate = "GetDate()";
        model.CreatedDate = "GetDate()";
        guestCheckBAL.InserPrintServer(model);
    }


    public static void CheckChangeState(String guest_check_seq, String State) {
        if (State.equals("0")) {
            GuestCheckParameters.ActiveCheckSeq = "0";
            GuestCheckParameters.ActiveTableNumber = "0";
            GuestCheckParameters.ActiveTableName = "";
        } else {
            GeneralleFunctions.LoadAllActiveGuestCheck();
            for (int i = 0; i < TablesParameters.TableList.size(); i++) {
                for (int j = 0; j < TablesParameters.TableList.get(i).tableDesignDetailBALList.size(); j++) {
                    for (int g = 0; g < GuestCheckParameters.AllActiveGuestCheck.size(); g++) {
                        if (GuestCheckParameters.AllActiveGuestCheck.get(g).guest_check_seq.equals(guest_check_seq)) {
                            if (GuestCheckParameters.AllActiveGuestCheck.get(g).guest_check_table_number.equals(
                                    TablesParameters.TableList.get(i).tableDesignDetailBALList.get(j).table_design_detail_table_number)) {
                                GuestCheckParameters.ActiveCheckSeq = GuestCheckParameters.AllActiveGuestCheck.get(g).guest_check_seq;
                                GuestCheckParameters.ActiveTableNumber = TablesParameters.TableList.get(i).tableDesignDetailBALList.get(j).table_design_detail_table_number;
                                GuestCheckParameters.ActiveTableName = TablesParameters.TableList.get(i).tableDesignDetailBALList.get(j).table_design_detail_table_name;
                                break;
                            }
                        }
                    }
                }
            }
        }
        GuestCheckBAL guestCheckBAL = new GuestCheckBAL();
        guestCheckBAL.UpdateState(guest_check_seq, State);
    }

    public static void CheckChangeStatusChange(String guest_check_seq, String Status) {
        GuestCheckBAL guestCheckBAL = new GuestCheckBAL();
        guestCheckBAL.UpdateStatus(guest_check_seq, Status);
    }

    public static ArrayList<GuestCheckItemsBAL> DeleteItemInSelectetItemList(ArrayList<GuestCheckItemsBAL> itemList, int positoin, boolean canDelisRequite) {
        if (itemList.get(positoin).isFromDB.equals("0")) {
            if (itemList.get(positoin).guest_check_item_type.equals("menuitem")) {
                String lineNo = itemList.get(positoin).related_void_return_line_number;
                boolean loop = true;
                while (loop) {
                    try {
                        if (itemList.size() <= 0)
                            break;
                        for (int i = 0; i < itemList.size(); i++) {
                            if (itemList.get(i).related_void_return_line_number.equals(lineNo)) {
                                itemList.remove(i);
                                loop = true;
                                break;
                            } else {
                                loop = false;
                            }
                        }
                    } catch (Exception ex) {
                        loop = false;
                    }
                }
            } else {
                if (!itemList.get(positoin).isRequite.equals("1") || canDelisRequite) {
                    itemList.remove(positoin);
                }
            }
        }
        return itemList;
    }

    public static void WriteLastCheck(String GuestCheck, Context context) {
        try {
            FileFunctions.Write(context, "LastCheck.txt", GuestCheck);
        } catch (Exception e) {
        }
    }

    public static void ReadandUpdateLastCheck(Context context) {
        try {
            String GuestCheck = FileFunctions.ReadFile(context, "LastCheck.txt");
            GeneralleFunctions.CheckChangeState(GuestCheck, "0");
        } catch (Exception e) {
        }
    }

    public static ArrayList<MenuItemsBAL> GetCondimants(String mi_master_def_seq, String mi_master_def_name, String mi_price) {
        ArrayList<MenuItemsBAL> result = new ArrayList<>();
        MenuItemsBAL menuItemsBAL = new MenuItemsBAL();
        result = menuItemsBAL.SelectCondimantItems(mi_master_def_seq, UserParameters.SelectedRVCID, mi_master_def_name, mi_price);
        return result;
    }

    private static void LoadAllMenuItems() {
        MenuItemsBAL menuItemsBAL = new MenuItemsBAL();
        MenuItemsParamters.MenuItemList = menuItemsBAL.SelectAllMenuItems(UserParameters.SelectedRVCID);
        MenuItemsParamters.MenuItemNotList = menuItemsBAL.LoadMenuItemsNot(UserParameters.SelectedRVCID);
    }

    public static void LoadUserAccessRight() {
        try {
            AccessRightBAL accessRightBAL = new AccessRightBAL();
            UserParameters.isCanEditOtherCheck = accessRightBAL.SelectAllActiveCheck("access_right_group_check_code27", UserParameters.RoleID).get(0).status;
        } catch (Exception e) {
        }
    }

    public static boolean ControlUserCanEditCheck(String guest_check_open_by_employee) {
        boolean result = false;
        if (!UserParameters.isCanEditOtherCheck.equals("1")) {
            if (UserParameters.UserID.equals(guest_check_open_by_employee)) {
                result = true;
            }
        } else {
            result = true;
        }
        return result;
    }

    private static void LoadAfterLoginParameters(Context context) {
        GeneralleFunctions.LoadProperties();
        GeneralleFunctions.LoadTables();
        GeneralleFunctions.LoadAllMenuItems();
        GeneralleFunctions.LoadUserAccessRight();
    }

    private static void LoadTables() {
        TableDesignBAL tableDesignBAL = new TableDesignBAL();
        TablesParameters.TableList = tableDesignBAL.SelectAll();
        if (DeviceParameters.DeviceType.equals("5inc") || DeviceParameters.DeviceType.equals("5incCheck")) {
            TablesParameters.TableTextSize = 14;
            TablesParameters.ColumnCount = 2;
            TablesParameters.Params = new TableRow.LayoutParams(290, 280);
            TablesParameters.Params.setMargins(15, 30, 15, 15);
        } else if (DeviceParameters.DeviceType.equals("8inc")) {
            TablesParameters.TableTextSize = 20;
            TablesParameters.ColumnCount = 2;
            TablesParameters.Params = new TableRow.LayoutParams(190, 190);
            TablesParameters.Params.setMargins(25, 25, 25, 25);
        }
    }

    private static void LoadDBParameters(Context context) {
        try {
            String s = FileFunctions.ReadFile(context, "dosya.txt");
            if (!s.equals("")) {
                String[] result = s.split("@");
                DBParameters.IP = result[0];
                DBParameters.DATABASE_NAME = result[1];
                DBParameters.DATABASE_USERNAME = result[2];
                DBParameters.DATABASE_PASSWORD = result[3];
                DeviceParameters.DeviceType = result[4];
            } else {
                FileFunctions.Write(context, "dosya.txt"
                        , DBParameters.IP + "@" + DBParameters.DATABASE_NAME + "@"
                                + DBParameters.DATABASE_USERNAME + "@" + DBParameters.DATABASE_PASSWORD + "@" + DeviceParameters.DeviceType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void LoadTerminalParameters() {
        try {
            DeviceParameters.SerialNo = "73";// Build.SERIAL;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void LoadProperties() {
        PropertiesBAL propertiesBAL = new PropertiesBAL();
        try {
            Properties.PropertiesList = propertiesBAL.SelectAll();
            if (Properties.PropertiesList != null) {
                if (Properties.PropertiesList.size() > 0) {
                    for (int i = 0; i < Properties.PropertiesList.size(); i++) {
                        if (Properties.PropertiesList.get(i).Name.equals("TabletItemDoubleCick")) {
                            if (Properties.PropertiesList.get(i).Value.equals("True")) {
                                Properties.DoubleCick = true;
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }
    }
}
