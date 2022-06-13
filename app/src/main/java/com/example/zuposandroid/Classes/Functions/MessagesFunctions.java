package com.example.zuposandroid.Classes.Functions;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.example.zuposandroid.Classes.Parameters.DeviceParameters;
import com.example.zuposandroid.Classes.Parameters.MessagesParameters;

public class MessagesFunctions {
    public static void LoadMessages() {
        try {
            if (DeviceParameters.Language.equals("eng")) {
                MessagesParameters.BackClick = "THE BACK KEY CANNOT BE USED!";
                MessagesParameters.Yes = "YES";
                MessagesParameters.No = "NO";
                MessagesParameters.IncorrectEntry = "INCORRECT ENTRY!";
                MessagesParameters.CheckNotFound = "CHECK NOT FOUND OR USED BY SOMEONE ELSE!";
                MessagesParameters.DeleteProduct = "ARE YOU SURE YOU WANT TO DELETE?";
                MessagesParameters.ProductConnotDelete = "THIS PRODUCT CANNOT BE DELETED!";
                MessagesParameters.ProductNotFound = "PRODUCT NOT FOUND!";
                MessagesParameters.ProductAdded = "PRODUCT ADDED.";
                MessagesParameters.IPEmpty = "DATABASE FIELD CANNOT BE EMPTY!";
                MessagesParameters.DBEmpty = "IP FIELD CANNOT BE EMPTY!";
                MessagesParameters.UserNameEmpty = "USERNAME FIELD CANNOT BE EMPTY!";
                MessagesParameters.PasswordEmpty = "PASSWORD FIELD CANNOT BE EMPTY!";
                MessagesParameters.ChooseAnotherTable = "CHOOSE ANOTHER TABLE!";
                MessagesParameters.SureToTransfer = "ARE YOU SURE YOU WANT TO TRANSFER?";
                MessagesParameters.InsufficientAuthority = "YOU ARE NOT AUTHORIZATION FOR THIS TRANSACTION!";
            } else {
                MessagesParameters.BackClick = "GERİ TUŞU KULLANILAMAZ!";
                MessagesParameters.Yes = "EVET";
                MessagesParameters.No = "HAYIR";
                MessagesParameters.IncorrectEntry = "HATALI GİRİŞ !";
                MessagesParameters.CheckNotFound = "ÇEK BULUNAMADI YADA BAŞKASI TARAFINDAN KULLANILIYOR!";
                MessagesParameters.DeleteProduct = "SİLMEK İSTEDİĞİNİZE EMİN MİSİNİZ?";
                MessagesParameters.ProductConnotDelete = "BU ÜRÜN SİLİNEMEZ!";
                MessagesParameters.ProductNotFound = "ÜRÜN BULUNAMADI!";
                MessagesParameters.ProductAdded = "ÜRÜN EKLENDİ.";
                MessagesParameters.IPEmpty = "DATABASE ALANI BOŞ OLAMAZ!";
                MessagesParameters.DBEmpty = "İP ALANI BOŞ OLAMAZ!";
                MessagesParameters.UserNameEmpty = "USERNAME ALANI BOŞ OLAMAZ!";
                MessagesParameters.PasswordEmpty = "PASSWORD ALANI BOŞ OLAMAZ!";
                MessagesParameters.ChooseAnotherTable = "BAŞKA MASA SEÇİNİZ!";
                MessagesParameters.SureToTransfer = "TRANSFER ETMEK İSTEDİĞİNİZDEN EMİN MİSİNİZ?";
                MessagesParameters.InsufficientAuthority = "BU İŞLEM İÇİN YETKİNİZ BULUNMAMAKTADIR!";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void Message(String message, Context context) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setMessage(message);
            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
