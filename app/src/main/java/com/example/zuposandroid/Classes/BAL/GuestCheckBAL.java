package com.example.zuposandroid.Classes.BAL;

import com.example.zuposandroid.Classes.BAL.Models.InsertGuestCheckModel;
import com.example.zuposandroid.Classes.BAL.Models.InsertPrintServerModel;
import com.example.zuposandroid.Classes.DAL.DBConncetion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GuestCheckBAL {
    public String guest_check_seq;
    public String guest_check_number;
    public String guest_check_state;
    public String Total;
    public String guest_check_table_number;
    public String GuestCheckTypeID;
    public String guest_check_open_by_employee;
    public ArrayList<GuestCheckItemsBAL> checkItemsBALList;

    private ArrayList<GuestCheckBAL> LoadResuldSet(ResultSet rs) throws SQLException {
        ArrayList<GuestCheckBAL> modelList = new ArrayList<GuestCheckBAL>();
        while (rs.next()) {
            GuestCheckBAL model = new GuestCheckBAL();
            try {
                model.guest_check_seq = rs.getString("guest_check_seq");
            } catch (Exception ex) {
            }
            try {
                model.guest_check_number = rs.getString("guest_check_number");
            } catch (Exception ex) {
            }
            try {
                model.guest_check_state = rs.getString("guest_check_state");
            } catch (Exception ex) {
            }
            try {
                model.Total = rs.getString("Total");
            } catch (Exception ex) {
            }
            try {
                model.guest_check_table_number = rs.getString("guest_check_table_number");
            } catch (Exception ex) {
            }
            try {
                model.GuestCheckTypeID = rs.getString("GuestCheckTypeID");
            } catch (Exception ex) {
            }
            try {
                model.guest_check_open_by_employee = rs.getString("guest_check_open_by_employee");
            } catch (Exception ex) {
            }
            modelList.add(model);
        }
        return modelList;
    }

    public ArrayList<GuestCheckBAL> SelectAllActiveCheck(String RVC, String WhereCase) {
        ArrayList<GuestCheckBAL> result = new ArrayList<GuestCheckBAL>();
        GuestCheckItemsBAL guestCheckItemsBAL = new GuestCheckItemsBAL();
        DBConncetion dbConncetion = new DBConncetion();
        try {
            ResultSet resultSet;
            String sql = " select x.guest_check_seq, x.guest_check_number, x.guest_check_table_number, x.guest_check_state, x.guest_check_open_by_employee " +
                    ", cast(cast(sum(case when x.Count is null then 0 else x.Count end * x.Price) as decimal(18,2)) as nvarchar(20)) as 'Total', x.GuestCheckTypeID " +
                    " from(" +
                    " select " +
                    " g.guest_check_seq" +
                    ", g.guest_check_number " +
                    ", g.guest_check_table_number" +
                    ", g.guest_check_state" +
                    ", g.guest_check_open_by_employee" +
                    ", case when gi.guest_check_item_quantity is null then gi.guest_check_item_count else gi.guest_check_item_quantity end as 'Count'" +
                    ", case when gi.guest_check_item_price is null then 0 else gi.guest_check_item_price end as 'Price', g.GuestCheckTypeID " +
                    "from GuestCheck g " +
                    "left join GuestCheckItems gi on gi.guest_check_seq=g.guest_check_seq " +
                    "where g.guest_check_status = 0 and (gi.guest_check_item_status not in (2,6, 12) or gi.guest_check_item_status is null) " +
                    "and g.rvc_def_seq=  " + RVC + " " + WhereCase + " " +
                    " )x " +
                    "group by x.guest_check_seq, x.guest_check_state, x.guest_check_table_number,  x.guest_check_number, x.GuestCheckTypeID , x.guest_check_open_by_employee ";
            resultSet = dbConncetion.connectionRun(sql);
            result = LoadResuldSet(resultSet);
            /*if (result != null) {
                if (result.size() > 0) {
                    for (int i = 0; i < result.size(); i++) {
                        result.get(i).checkItemsBALList = guestCheckItemsBAL.SelectAllItemsByGuestCheckSeq(result.get(i).guest_check_seq);
                    }
                }
            }*/
        } catch (Exception ex) {
        }
        return result;
    }

    public void UpdateState(String guest_check_seq, String State) {
        String sql = "Update GuestCheck set guest_check_state= " + State + " where guest_check_seq= " + guest_check_seq;
       sql = sql + " Update GuestCheck set guest_check_total=(select sum((case when gi.guest_check_item_quantity is null then gi.guest_check_item_count else gi.guest_check_item_quantity end) * " +
                " (case when gi.guest_check_item_price is null then 0 else gi.guest_check_item_price end)) " +
                " from GuestCheckItems gi where (gi.guest_check_item_status not in (2,6, 12) or gi.guest_check_item_status is null) and gi.guest_check_seq=" + guest_check_seq + ") " +
               " where guest_check_seq= " + guest_check_seq;
        try {
            DBConncetion dbConncetion = new DBConncetion();
            dbConncetion.connectionRun(sql);
            dbConncetion.close();
        } catch (Exception ex) {

        }
    }

    public void UpdateStatus(String guest_check_seq, String Status) {
        String sql = "Update GuestCheck set guest_check_status= " + Status + " where guest_check_seq= " + guest_check_seq;
        try {
            DBConncetion dbConncetion = new DBConncetion();
            dbConncetion.connectionRun(sql);
            dbConncetion.close();
        } catch (Exception ex) {

        }
    }

    public ArrayList<GuestCheckBAL> InsertGuestCheck(InsertGuestCheckModel model) {
        ArrayList<GuestCheckBAL> list = new ArrayList<>();
        String sql = "insert into GuestCheck ( " +
                "       [guest_check_number]" +
                "      ,[guest_check_id]" +
                "      ,[guest_check_status]" +
                "      ,[guest_check_state]" +
                "      ,[guest_check_order_type_no]" +
                "      ,[guest_check_open_time]" +
                "      ,[guest_check_open_date]" +
                "      ,[guest_check_open_by_cashier]" +
                "      ,[guest_check_open_by_employee]" +
                "      ,[guest_check_total]" +
                "      ,[guest_check_discount_total]" +
                "      ,[bussiness_date]" +
                "      ,[rvc_def_seq]" +
                "      ,[terminal_def_seq]" +
                "      ,[customer_def_seq]" +
                "      ,[guest_check_close_time]" +
                "      ,[guest_check_close_date]" +
                "      ,[guest_check_payment_total]" +
                "      ,[guest_check_table_number]" +
                "      ,[guest_check_group_number]" +
                "      ,[standalone_guest_check_number]" +
                "      ,[UpdateDate]" +
                "      ,[TransactionStatus]" +
                "      ,[isStockTransfer]) "
                + " values ( "
                + model.guest_check_number
                + ", '" + model.guest_check_id
                + "', " + model.guest_check_status
                + ", " + model.guest_check_state
                + ", " + model.guest_check_order_type_no
                + ", " + model.guest_check_open_time
                + ", " + model.guest_check_open_date
                + ", " + model.guest_check_open_by_cashier
                + ", " + model.guest_check_open_by_employee
                + ", " + model.guest_check_total
                + ", " + model.guest_check_discount_total
                + ", " + model.bussiness_date
                + ", " + model.rvc_def_seq
                + ", " + model.terminal_def_seq
                + ", '" + model.customer_def_seq
                + "', " + model.guest_check_close_time
                + ", " + model.guest_check_close_date
                + ", " + model.guest_check_payment_total
                + ", " + model.guest_check_table_number
                + ", " + model.guest_check_group_number
                + ", " + model.standalone_guest_check_number
                + ", " + model.UpdateDate
                + ", " + model.TransactionStatus
                + ", " + model.isStockTransfer
                + ")";
        try {
            DBConncetion dbConncetion = new DBConncetion();
            dbConncetion.connectionRunSet(sql);
            dbConncetion.close();
            list = SelectAllActiveCheck(model.rvc_def_seq, " and g.guest_check_seq=(select max(guest_check_seq) from GuestCheck ) ");
        } catch (Exception ex) {

        }
        return list;
    }

    public void InserPrintServer(InsertPrintServerModel model) {
        String sql = " insert into PrintServer ( " +
                "[guest_check_seq], " +
                "[format_profile_def_seq], " +
                "[printer_profile_def_seq], " +
                "[PrintFunction], " +
                "[TerminalDefSeq], " +
                "[Status], " +
                "[UpdateDate], " +
                "[CreatedDate]) values( "
                + model.guest_check_seq
                + " , " + model.format_profile_def_seq
                + " , " + model.printer_profile_def_seq
                + " , " + model.PrintFunction
                + " , " + model.TerminalDefSeq
                + " , " + model.Status
                + " , " + model.UpdateDate
                + " , " + model.CreatedDate + " )";
        try {
            DBConncetion dbConncetion = new DBConncetion();
            dbConncetion.connectionRunSet(sql);
            dbConncetion.close();
        } catch (Exception ex) {
        }
    }

    public String TableTransfer(String guest_check_seq, String guest_check_table_number) {
        String NewGuestCheckSeq = "";
        String sql = "insert into GuestCheck \n" +
                "select [guest_check_number]\n" +
                "      ,[guest_check_id]\n" +
                "      ,[guest_check_status]\n" +
                "      ,[guest_check_state]\n" +
                "      ,[guest_check_order_type_no]\n" +
                "      ,[guest_check_open_time]\n" +
                "      ,[guest_check_open_date]\n" +
                "      ,[guest_check_open_by_cashier]\n" +
                "      ,[guest_check_open_by_employee]\n" +
                "      ,[guest_check_print_line_number]\n" +
                "      ,[guest_check_printed_page_number]\n" +
                "      ,[guest_check_kuver_count]\n" +
                "      ,[guest_check_total]\n" +
                "      ,[guest_check_kuver_total]\n" +
                "      ,[guest_check_discount_total]\n" +
                "      ,[guest_check_increaseTotal]\n" +
                "      ,[guest_check_service_charge_total]\n" +
                "      ,[guest_check_tip_total]\n" +
                "      ,[bussiness_date]\n" +
                "      ,[rvc_def_seq]\n" +
                "      ,[terminal_def_seq]\n" +
                "      ,[customer_def_seq]\n" +
                "      ,[invoice_number]\n" +
                "      ,[guest_check_close_time]\n" +
                "      ,[guest_check_close_date]\n" +
                "      ,[guest_check_closed_by_employee]\n" +
                "      ,[guest_check_tax_total]\n" +
                "      ,[guest_check_training_status]\n" +
                "      ,[guest_check_payment_total]\n" +
                "      , " + guest_check_table_number +
                "      ,[guest_check_group_number]\n" +
                "      ,[standalone_guest_check_number]\n" +
                "      ,[UpdateDate]\n" +
                "      ,[CustomerAccountNumber]\n" +
                "      ,[ProjectCodeID]\n" +
                "      ,[GuestCheckTypeID]\n" +
                "      ,[FiscalNumber]\n" +
                "      ,[EKUnumber]\n" +
                "      ,[ZNumber]\n" +
                "      ,[TerminalNumber]\n" +
                "      ,[GuestCheckFiscalResult]\n" +
                "      ,[InvoiceClient]\n" +
                "      ,[InvoiceTypeID]\n" +
                "      ,[InvoiceNumber]\n" +
                "      ,[IsTransfer]\n" +
                "      ,[TransferSeqID]\n" +
                "      ,[TransactionStatus]\n" +
                "      ,[Carrier]\n" +
                "      ,[PosSeq]\n" +
                "      ,[Reconciliation]\n" +
                "      ,[isStockTransfer] from GuestCheck where [guest_check_seq]= " + guest_check_seq +
                "\t  declare @NewGuestCheckSeq bigint\n" +
                "\t  set @NewGuestCheckSeq=(select max(guest_check_seq) from GuestCheck) \n" +
                " Update GuestCheck set guest_check_status=1 , TransactionStatus=1, TransferSeqID=@NewGuestCheckSeq where [guest_check_seq]= " + guest_check_seq +
                "\t  Update GuestCheckCardDetail set [guest_check_seq]=@NewGuestCheckSeq where [guest_check_seq]= " + guest_check_seq +
                "\t  Update GuestCheckDiscountDetail set [guest_check_seq]=@NewGuestCheckSeq where [guest_check_seq]= " + guest_check_seq +
                "\t  Update GuestCheckItemRelationDetail set [guest_check_seq]=@NewGuestCheckSeq where [guest_check_seq]= " + guest_check_seq +
                "\t  Update GuestCheckItems set [guest_check_seq]=@NewGuestCheckSeq where [guest_check_seq]= " + guest_check_seq +
                "\t   Update GuestCheckPaymentDetail set [guest_check_seq]=@NewGuestCheckSeq where [guest_check_seq]= " + guest_check_seq +
                "\t    Update GuestCheckReturnVoidDetail set [guest_check_seq]=@NewGuestCheckSeq where [guest_check_seq]= " + guest_check_seq +
                "\t\t Update GuestCheckSendKitchen set [guest_check_seq]=@NewGuestCheckSeq where [guest_check_seq]= " + guest_check_seq +
                "\t\t   Update GuestCheckWithRooms set [guest_check_seq]=@NewGuestCheckSeq where [guest_check_seq]= " + guest_check_seq +
                "\t\t   select @NewGuestCheckSeq as 'guest_check_seq'";
        try {
            ArrayList<GuestCheckBAL> result = new ArrayList<GuestCheckBAL>();
            DBConncetion dbConncetion = new DBConncetion();
            ResultSet resultSet = dbConncetion.connectionRun(sql);
            result = LoadResuldSet(resultSet);
            if (result != null) {
                if (result.size() > 0) {
                    NewGuestCheckSeq = result.get(0).guest_check_seq;
                }
            }
        } catch (Exception ex) {
        }
        return NewGuestCheckSeq;
    }

    public void UpdateGuestCheckTypeID(String guest_check_seq, String GuestCheckTypeID) {
        String sql = "Update GuestCheck set GuestCheckTypeID= " + GuestCheckTypeID + " where guest_check_seq= " + guest_check_seq;
        try {
            DBConncetion dbConncetion = new DBConncetion();
            dbConncetion.connectionRun(sql);
            dbConncetion.close();
        } catch (Exception ex) {

        }
    }
}
