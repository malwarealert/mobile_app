package com.example.diplom;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "db_diplom.db";
    private static final int DATABASE_VERSION = 1;
    private static String DB_LOCATION;
    private static String DB_PATH = "";

    // Таблица слов
    public static final String TABLE_WORDS = "Words";
    public static final String COLUMN_ID = "word_id";
    public static final String COLUMN_FOREIGN = "word";
    public static final String COLUMN_TRANSLATION = "translation";
    public static final String COLUMN_WORD_CATEGORY = "word_category";

    private SQLiteDatabase mDataBase;
    private final Context mContext;
    private boolean mNeedUpdate = false;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        DB_LOCATION = context.getDatabasePath(DATABASE_NAME).getPath();
        Log.d("Database", "Database path: " + DB_LOCATION);

        DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        this.mContext = context;

        copyDataBase();

        this.getReadableDatabase();
    }

    private void copyDataBase() {
        if (!checkDataBase()) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDBFile();
            } catch (IOException mIOException) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DATABASE_NAME);
        return dbFile.exists();
    }

    private void copyDBFile() throws IOException {
        InputStream mInput = mContext.getAssets().open(DATABASE_NAME);
        OutputStream mOutput = new FileOutputStream(DB_PATH + DATABASE_NAME);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_WORDS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FOREIGN + " TEXT NOT NULL, " +
                COLUMN_TRANSLATION + " TEXT NOT NULL, " +
                COLUMN_WORD_CATEGORY + " INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static String getDatabasePath() {
        return DB_LOCATION;
    }

    public List<WordPair> getAllWords() {
        List<WordPair> wordList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_WORDS,
                new String[]{COLUMN_FOREIGN, COLUMN_TRANSLATION},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                wordList.add(new WordPair(
                        cursor.getString(0),
                        cursor.getString(1)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return wordList;
    }

    public List<WordPair> getWordsByCategory(int categoryId) {
        List<WordPair> wordList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_WORDS,
                new String[]{COLUMN_FOREIGN, COLUMN_TRANSLATION},
                COLUMN_WORD_CATEGORY + "=?",
                new String[]{String.valueOf(categoryId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                wordList.add(new WordPair(
                        cursor.getString(0),
                        cursor.getString(1)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return wordList;
    }


    public WordPair getRandomWord() {
        SQLiteDatabase db = this.getReadableDatabase();
        WordPair wordPair = null;

        Cursor cursor = db.rawQuery("SELECT " + COLUMN_FOREIGN + ", " + COLUMN_TRANSLATION +
                " FROM " + TABLE_WORDS + " ORDER BY RANDOM() LIMIT 1", null);

        if (cursor.moveToFirst()) {
            wordPair = new WordPair(
                    cursor.getString(0),
                    cursor.getString(1));
        }
        cursor.close();
        db.close();
        return wordPair;
    }

    public List<String> getRandomTranslations(int count, String excludeTranslation) {
        List<String> translations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + COLUMN_TRANSLATION +
                        " FROM " + TABLE_WORDS +
                        " WHERE " + COLUMN_TRANSLATION + " != ?" +
                        " ORDER BY RANDOM() LIMIT ?",
                new String[]{excludeTranslation, String.valueOf(count)});

        if (cursor.moveToFirst()) {
            do {
                translations.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return translations;
    }
    public WordDetail getWordDetails(String word) {
        SQLiteDatabase db = this.getReadableDatabase();
        WordDetail wordDetail = new WordDetail();

        // Получаем основную информацию о слове
        Cursor wordCursor = db.query("Words",
                new String[]{"word", "translation"},
                "word = ?",
                new String[]{word},
                null, null, null);

        if (wordCursor.moveToFirst()) {
            wordDetail.setWord(wordCursor.getString(0));
            wordDetail.setTranslation(wordCursor.getString(1));
        }
        wordCursor.close();

        // Получаем примеры использования
        Cursor examplesCursor = db.query("Examples",
                new String[]{"example_sentence", "example_translation"},
                "word_id = (SELECT _id FROM Words WHERE word = ?)",
                new String[]{word},
                null, null, null);

        List<String> examples = new ArrayList<>();
        List<String> exampleTranslations = new ArrayList<>();

        while (examplesCursor.moveToNext()) {
            examples.add(examplesCursor.getString(0));
            exampleTranslations.add(examplesCursor.getString(1));
        }

        wordDetail.setExamples(examples);
        wordDetail.setExampleTranslations(exampleTranslations);
        examplesCursor.close();

        return wordDetail;
    }

}