package com.example.wordlyclone;

import android.content.res.AssetManager;
import android.icu.text.LocaleDisplayNames;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Dictionary {

    Dictionary () {}

    public static ArrayList<String> readerToList(InputStream is) {
        ArrayList<String> list = new ArrayList<String>();

        try {
                BufferedReader br = new BufferedReader(new InputStreamReader(is,StandardCharsets.UTF_8));
                String line;

                while ((line = br.readLine()) != null) {
                    Log.d("test",line);
                    list.add(line);
                }
                br.close();
        } catch (IOException e) {
            Log.d("what",e.toString());
            e.printStackTrace();
        }
        return list;
    }
}
