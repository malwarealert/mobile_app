package com.example.diplom;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.database.Cursor;


public class TrainingFragment extends Fragment {

    public TrainingFragment() {
        // Required empty public constructor
    }
    private TextView currentWord;
    private Button buttonAnswer;
    private DBHelper dbHelper;
    private SQLiteDatabase database;

    @Override

    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState){
            View view = inflater.inflate(R.layout.fragment_practice, container, false);
            buttonAnswer = view.findViewById(R.id.dummy_button);

            //String[] food={"Арбуз", "Яблоко", "Груша", "Капуста", "Банан", "Апельсин"};
            //boolean[] food_c ={false, false, false, false, false, false};

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT Words.word FROM Words" + DBHelper.TABLE_WORDS + " ORDER BY RANDOM() LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            int indexWord = cursor.getColumnIndex(DBHelper.COLUMN_WORD);
            String randomWord = cursor.getString(indexWord);

            // Получаем ссылку на TextView
            currentWord = view.findViewById(R.id.textView7);
            currentWord.setText(randomWord); // Устанавливаем текст в TextView
        }

        cursor.close();
        db.close();

        // Inflate the layout for this fragment
            return view;
    }



}
