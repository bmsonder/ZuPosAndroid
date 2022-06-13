package com.example.zuposandroid.Classes.BAL;

import com.example.zuposandroid.Classes.DAL.DBConncetion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MenuItemsBAL {
    public String ID;
    public String ItemType;
    public String IsRequite;
    public String Name;
    public String Price;
    public String ParentID;
    public String ParentName;
    public String ParentPrice;
    public ArrayList<MenuItemsBAL> MenuItemList;

    private ArrayList<MenuItemsBAL> LoadResuldSet(ResultSet rs) throws SQLException {
        ArrayList<MenuItemsBAL> modelList = new ArrayList<MenuItemsBAL>();
        while (rs.next()) {
            MenuItemsBAL model = new MenuItemsBAL();
            try {
                model.ID = rs.getString("ID");
            } catch (Exception ex) {
            }
            try {
                model.ItemType = rs.getString("ItemType");
            } catch (Exception ex) {
            }
            try {
                model.IsRequite = rs.getString("IsRequite");
            } catch (Exception ex) {
            }
            try {
                model.Name = rs.getString("Name");
            } catch (Exception ex) {
            }
            try {
                model.ParentID = rs.getString("ParentID");
            } catch (Exception ex) {
            }
            try {
                model.ParentName = rs.getString("ParentName");
            } catch (Exception ex) {
            }
            try {
                model.ParentPrice = rs.getString("ParentPrice");
            } catch (Exception ex) {
            }
            try {
                model.Price = rs.getString("Price");
            } catch (Exception ex) {
            }
            modelList.add(model);
        }
        return modelList;
    }


    public ArrayList<MenuItemsBAL> SelectAllMenuItems(String RVCSeq) {
        ArrayList<MenuItemsBAL> result = new ArrayList<MenuItemsBAL>();
        DBConncetion dbConncetion = new DBConncetion();
        try {
            ResultSet resultSet;
            String sql = " Select [slu_def_seq] as ID, [slu_def_name] as [Name], 'MainMenu' as 'ItemType' " +
                    "From SluDef where  slu_type in ('MI', 'MIGroup') and parent_slu_def_no is null " +
                    "  and slu_def_no not in (select case when d.slu_number is null then 0 else d.slu_number end from DiscountDef d)  " +
                    " ORDER BY  [slu_def_name] asc ";
            resultSet = dbConncetion.connectionRun(sql);
            result = LoadResuldSet(resultSet);
            if (result != null) {
                if (result.size() > 0) {
                    for (int i = 0; i < result.size(); i++) {
                        result.get(i).MenuItemList = SelectSubItems(result.get(i).ID, RVCSeq);
                        if (result.get(i).MenuItemList.size() <= 0)
                            result.get(i).MenuItemList = SelectMenuItems(result.get(i).ID, RVCSeq);
                    }
                }
            }
        } catch (Exception ex) {
        }
        return result;
    }

    private ArrayList<MenuItemsBAL> SelectSubItems(String slu_seq, String RVCSeq) {
        ArrayList<MenuItemsBAL> result = new ArrayList<MenuItemsBAL>();
        DBConncetion dbConncetion = new DBConncetion();
        try {
            ResultSet resultSet;
            String sql = " Select [slu_def_seq] as ID, [slu_def_name] as [Name], 'SubMenu' as 'ItemType' " +
                    "From SluDef where  slu_type in ('MI', 'MIGroup') and parent_slu_def_no = " + slu_seq +
                    "  and slu_def_no not in (select case when d.slu_number is null then 0 else d.slu_number end from DiscountDef d)  " +
                    " ORDER BY  [slu_def_name] asc ";
            resultSet = dbConncetion.connectionRun(sql);
            result = LoadResuldSet(resultSet);
            if (result != null) {
                if (result.size() > 0) {
                    for (int i = 0; i < result.size(); i++) {
                        result.get(i).MenuItemList = SelectMenuItems(result.get(i).ID, RVCSeq);
                    }
                }
            }
        } catch (Exception ex) {
        }
        return result;
    }


    private ArrayList<MenuItemsBAL> SelectMenuItems(String slu_seq, String RVCSeq) {
        ArrayList<MenuItemsBAL> result = new ArrayList<MenuItemsBAL>();
        DBConncetion dbConncetion = new DBConncetion();
        try {
            ResultSet resultSet;
            String sql = " select " +
                    " r.mi_master_def_seq as 'ID'" +
                    ", r.slu_seq as 'ParentID'" +
                    ", 'menuitem' as 'ItemType'" +
                    ", r.mi_master_def_name as 'Name'" +
                    ", rp.mi_price as 'Price' " +
                    " from  RvcMenuItemDef r " +
                    " inner join RvcMenuItemPrice rp on rp.mi_master_def_seq=r.mi_master_def_seq " +
                    " where rp.mi_price_number=1 and R.mi_is_active=1 and  r.mi_master_def_type='menuitem' and  r.slu_seq = " + slu_seq +
                    " and r.rvc_def_seq= " + RVCSeq + " and rp.rvc_def_seq= " + RVCSeq +
                    " ORDER BY mi_master_def_name asc ";
            resultSet = dbConncetion.connectionRun(sql);
            result = LoadResuldSet(resultSet);
            if (result != null) {
                if (result.size() > 0) {
                    for (int i = 0; i < result.size(); i++) {
                        result.get(i).MenuItemList = new ArrayList<>(); //SelectCondimantItems(result.get(i).ID, RVCSeq, result.get(i).Name, result.get(i).Price);
                    }
                }
            }
        } catch (Exception ex) {
        }
        return result;
    }

    public ArrayList<MenuItemsBAL> SelectCondimantItems(String mi_master_def_seq, String RVCSeq, String ParentName, String ParentPrice) {
        ArrayList<MenuItemsBAL> result = new ArrayList<MenuItemsBAL>();
        DBConncetion dbConncetion = new DBConncetion();
        try {
            ResultSet resultSet;
            String sql = "select rd.mi_master_def_seq as 'ID'" +
                    ", " + mi_master_def_seq + " as 'ParentID'" +
                    ", '" + ParentName + "' as 'ParentName'" +
                    ", '" + ParentPrice + "' as 'ParentPrice'" +
                    ", 'condiment' as 'ItemType'" +
                    ", x.requirmend as 'IsRequite'" +
                    ", rd.mi_master_def_name as 'Name'" +
                    ", cast(case when rp.mi_price is null then 0 else rp.mi_price end as decimal(18,2)) as 'Price' " +
                    "from CondimentMenuItemRelation cmi " +
                    "left join RvcMenuItemDef rd on  rd.mi_master_def_seq=cmi.mi_master_def_seq " +
                    "inner join ( " +
                    " select (case when CPD.condiment_profile_def_seq=RMID.condiment_main_group_def_seq then 1 else 0 end) as requirmend" +
                    ", CSG.condiment_sub_group_def_name" +
                    ", CSG.condiment_sub_group_def_seq " +
                    "from CondimentProfileDef CPD " +
                    "left join CondimentRelation CR on CPD.condiment_profile_def_seq=CR.relation_seq " +
                    "left join CondimentSubGroupDef CSG on CSG.condiment_sub_group_def_seq=CR.condiment_sub_group_def_seq " +
                    "left join RvcMenuItemDef RMID on CPD.condiment_profile_def_seq in (RMID.condiment_main_group_def_seq,RMID.condiment_profile_def_seq) " +
                    "where RMID.mi_master_def_seq= " + mi_master_def_seq + " and RMID.rvc_def_seq = " + RVCSeq +
                    " ) x on x.condiment_sub_group_def_seq=cmi.condiment_sub_group_def_seq  " +
                    "left join  RvcMenuItemPrice rp on rp.mi_master_def_seq=rd.mi_master_def_seq  " +
                    "where (rp.mi_price_number=1 or rp.mi_price is null) and rd.rvc_def_seq= " + RVCSeq + " and rp.rvc_def_seq= " + RVCSeq;
            resultSet = dbConncetion.connectionRun(sql);
            result = LoadResuldSet(resultSet);
            if (result != null) {
                if (result.size() > 0) {
                    for (int i = 0; i < result.size(); i++) {
                        result.get(i).MenuItemList = new ArrayList<>();
                    }
                }
            }
        } catch (Exception ex) {
        }
        return result;
    }

    public ArrayList<MenuItemsBAL> LoadMenuItemsNot(String rvc_def_seq) {
        ArrayList<MenuItemsBAL> result = new ArrayList<MenuItemsBAL>();
        try {
            String sql = "select mi_master_def_seq as 'ID', 'memo' as 'ItemType', mi_master_def_name as 'Name' " +
                    "from RvcMenuItemDef where [mi_master_def_type] ='memo' and [mi_is_active] =1 and rvc_def_seq = " + rvc_def_seq;
            DBConncetion dbConncetion = new DBConncetion();
            ResultSet resultSet = dbConncetion.connectionRun(sql);
            result = LoadResuldSet(resultSet);
        } catch (Exception ex) {
        }
        return result;
    }

}
