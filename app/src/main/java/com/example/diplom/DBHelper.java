package com.example.diplom;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

public class DBHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "db_diplom.db";
    private static String DB_LOCATION;
    private static final int DB_VERSION =1;
    // Имя таблицы
    public static final String TABLE_WORDS = "words";
    // Название колонки
    public static final String COLUMN_WORD = "word";

    private TrainingFragment myContext;

    public DBHelper(TrainingFragment fragment) {
        super(fragment.getContext(), DB_NAME, null, DB_VERSION);
    }

    private boolean checkDB() { //проверка на существование бд
        File fileDB = new File(DB_LOCATION + DB_NAME);
        return fileDB.exists();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
