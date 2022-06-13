package com.example.zuposandroid.Classes.Functions;

import android.content.Context;

import com.example.zuposandroid.Classes.Parameters.DBParameters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileFunctions {
    public static String ReadFile(Context context, String FileName) {
        String result = "";
        try {
            FileInputStream fr = context.openFileInput(FileName);
            InputStreamReader isr = new InputStreamReader(fr);
            BufferedReader buf = new BufferedReader(isr);
            result = buf.readLine();
        } catch (FileNotFoundException e) {
            // TODO: handle exception
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean Write(Context context, String FileName, String Intent) {
        boolean result = false;
        try {
            File directory = context.getFilesDir();
            File file = new File(directory, FileName);
            FileOutputStream fileOutput = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutput);
            outputStreamWriter.write(Intent);
            outputStreamWriter.flush();
            fileOutput.getFD().sync();
            outputStreamWriter.close();
            result = true;
        } catch (Exception e) {
        }
        return result;
    }
}
