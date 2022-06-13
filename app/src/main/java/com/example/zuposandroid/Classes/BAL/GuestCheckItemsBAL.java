package com.example.zuposandroid.Classes.BAL;

import com.example.zuposandroid.Classes.BAL.Models.InsertGuestCheckItemModel;
import com.example.zuposandroid.Classes.BAL.Models.InsertGuestCheckItemRelationDetailModel;
import com.example.zuposandroid.Classes.DAL.DBConncetion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GuestCheckItemsBAL {
    public String guest_check_item_seq;
    public String item_def_seq;
    public String item_def_name;
    public String count;
    public String Price;
    public String totalPrice;
    public String guest_check_item_type;
    public String void_return_line_number;
    public String guest_check_seq;
    public String isFromDB;
    public String isRequite;
    public String related_void_return_line_number;

    private ArrayList<GuestCheckItemsBAL> LoadResuldSet(ResultSet rs) throws SQLException {
        ArrayList<GuestCheckItemsBAL> modelList = new ArrayList<GuestCheckItemsBAL>();
        while (rs.next()) {
            GuestCheckItemsBAL model = new GuestCheckItemsBAL();
            try {
                model.guest_check_item_seq = rs.getString("guest_check_item_seq");
            } catch (Exception ex) {
            }
            try {
                model.item_def_seq = rs.getString("item_def_seq");
            } catch (Exception ex) {
            }
            try {
                model.item_def_name = rs.getString("item_def_name");
            } catch (Exception ex) {
            }
            try {
                model.count = rs.getString("count");
            } catch (Exception ex) {
            }
            try {
                model.Price = rs.getString("Price");
            } catch (Exception ex) {
            }
            try {
                model.totalPrice = rs.getString("totalPrice");
            } catch (Exception ex) {
            }
            try {
                model.guest_check_item_type = rs.getString("guest_check_item_type");
            } catch (Exception ex) {
            }
            try {
                model.void_return_line_number = rs.getString("void_return_line_number");
            } catch (Exception ex) {
            }
            try {
                model.guest_check_seq = rs.getString("guest_check_seq");
            } catch (Exception ex) {
            }
            modelList.add(model);
        }
        return modelList;
    }

    public ArrayList<GuestCheckItemsBAL> SelectAllItemsByGuestCheckSeq(String guest_check_seq) {
        ArrayList<GuestCheckItemsBAL> result = new ArrayList<GuestCheckItemsBAL>();
        DBConncetion dbConncetion = new DBConncetion();
        try {
            ResultSet resultSet;
            String sql = " select x.guest_check_item_seq, x.item_def_seq, x.item_def_name, x.guest_check_item_type, x.void_return_line_number, x.guest_check_seq, x.[count]" +
                    ", x.Price, cast(cast(x.[Count] * x.Price as decimal(18,2))as nvarchar(20)) as 'totalPrice' " +
                    "from ( " +
                    "select gi.guest_check_item_seq, gi.item_def_seq, gi.item_def_name, gi.guest_check_item_type, gi.void_return_line_number, gi.guest_check_seq, " +
                    "case when gi.guest_check_item_quantity is null then gi.guest_check_item_count else gi.guest_check_item_quantity end as 'count'" +
                    ", case when gi.guest_check_item_price is null then 0 else gi.guest_check_item_price end as 'Price' " +
                    "from GuestCheckItems gi where gi.guest_check_item_status not in (2,6, 12) and gi.guest_check_seq= " + guest_check_seq +
                    " )x ";
            resultSet = dbConncetion.connectionRun(sql);
            result = LoadResuldSet(resultSet);
        } catch (Exception ex) {
        }
        return result;
    }

    public void InsertGuestCheckItemWihtGuestCheckItemRelationDetail(InsertGuestCheckItemModel item, InsertGuestCheckItemRelationDetailModel relationItem) {
        String sql = "insert into GuestCheckItems " +
                "(" +
                "      item_def_seq" +
                "      ,item_def_name" +
                "      ,guest_check_item_price" +
                "      ,guest_check_item_type" +
                "      ,guest_check_item_count" +
                "      ,guest_check_item_date" +
                "      ,guest_check_item_time" +
                "      ,guest_check_item_employee" +
                "      ,business_date" +
                "      ,guest_check_item_unit_type" +
                "      ,guest_check_item_status" +
                "      ,void_return_line_number" +
                "      ,void_return_count" +
                "      ,guest_check_seq" +
                "      ,guest_check_line_print_status" +
                "      ,IsTransfer" +
                "      ,TransferSeqID" +
                "      ,DiscountitemSeq" +
                "      ,NRACode" +
                "      ,mi_barcode_id" +
                ") " +
                " values" +
                "( "
                + item.item_def_seq + " , "
                +"N"+ item.item_def_name + " , "
                + item.guest_check_item_price + " , "
                + item.guest_check_item_type + " , "
                + item.guest_check_item_count + " , "
                + item.guest_check_item_date + " , "
                + item.guest_check_item_time + " , "
                + item.guest_check_item_employee + " , "
                + item.business_date + " , "
                + item.guest_check_item_unit_type + " , "
                + item.guest_check_item_status + " , "
                + item.void_return_line_number + " , "
                + item.void_return_count + " , "
                + item.guest_check_seq + " , "
                + item.guest_check_line_print_status + " , "
                + item.IsTransfer + " , "
                + item.TransferSeqID + " , "
                + item.DiscountitemSeq + " , "
                + item.NRACode + " , "
                + item.mi_barcode_id + " ) ";
        if (!relationItem.item_def_seq.equals("")) {
            sql = sql + " Insert into GuestCheckItemRelationDetail ( [item_def_seq] " +
                    "      ,[guest_check_seq] " +
                    "      ,[requirmend] " +
                    "      ,[guest_check_item_type] " +
                    "      ,[related_void_return_line_number] " +
                    "      ,[referance_item_seq] )  values ( "
                    + relationItem.item_def_seq
                    + " , " + relationItem.guest_check_seq
                    + " , " + relationItem.requirmend
                    + " , '" + relationItem.guest_check_item_type
                    + "' , " + relationItem.related_void_return_line_number
                    + " , " + relationItem.referance_item_seq
                    + ") ";
        }
        try {
            DBConncetion dbConncetion = new DBConncetion();
            dbConncetion.connectionRunSet(sql);
            dbConncetion.close();
        } catch (Exception ex) {
        }
    }
}
