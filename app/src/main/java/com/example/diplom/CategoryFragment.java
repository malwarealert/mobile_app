package com.example.diplom;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.widget.ImageButton;
import java.util.Collections;
import java.util.List;
import androidx.fragment.app.Fragment;

public class CategoryFragment extends Fragment {
    private DatabaseHelper databaseHelper;
    private ListView wordsListView;
    private ArrayAdapter<String> adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        // Инициализация кнопок
        Button foodButton = view.findViewById(R.id.Food);
        Button transportButton = view.findViewById(R.id.Transport);
        Button clothesButton = view.findViewById(R.id.Clothes);
        Button houseButton = view.findViewById(R.id.Home);

        // Настройка ListView
        wordsListView = view.findViewById(R.id.wordsListview);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        wordsListView.setAdapter(adapter);

        // Обработчики нажатий
        foodButton.setOnClickListener(v -> showWordsForCategory(4));     // 4 - Еда
        transportButton.setOnClickListener(v -> showWordsForCategory(3)); // 3 - Транспорт
        clothesButton.setOnClickListener(v -> showWordsForCategory(2));  // 2 - Одежда
        houseButton.setOnClickListener(v -> showWordsForCategory(1));    // 1 - Дом

        return view;
    }

    private void showWordsForCategory(int categoryId) {
        List<WordPair> words = databaseHelper.getWordsByCategory(categoryId);
        adapter.clear();

        for (WordPair word : words) {
            adapter.add(word.getForeignWord() + " - " + word.getTranslation());
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }

    private void setupWordClickListener() {
        wordsListView.setOnItemClickListener((parent, view, position, id) -> {
            // Получаем выбранное слово (без перевода)
            String selectedItem = (String) parent.getItemAtPosition(position);
            String selectedWord = selectedItem.split(" - ")[0].trim();

            // Получаем детали слова из БД
            WordDetail wordDetail = databaseHelper.getWordDetails(selectedWord);

            // Показываем PopupWindow
            showWordDetailsPopup(wordDetail);
        });
    }

    private void showWordDetailsPopup(WordDetail wordDetail) {
        // Надуваем layout
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View popupView = inflater.inflate(R.layout.popup_window, null);

        // Настраиваем PopupWindow
        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);

        // Настройка фона и анимации
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Заполняем данными
        TextView tvWord = popupView.findViewById(R.id.wordText);
        TextView tvTranslation = popupView.findViewById(R.id.wordText2);
        LinearLayout layoutExamples = popupView.findViewById(R.id.wordText5);

        tvWord.setText(wordDetail.getWord());
        tvTranslation.setText(wordDetail.getTranslation());

        // Добавляем примеры
        layoutExamples.removeAllViews();
        for (int i = 0; i < wordDetail.getExamples().size(); i++) {
            addExampleToLayout(layoutExamples,
                    wordDetail.getExamples().get(i),
                    wordDetail.getExampleTranslations().get(i));
        }

        // Кнопка закрытия
        popupView.findViewById(R.id.imageButton3).setOnClickListener(v -> {
            popupWindow.dismiss();
        });

        // Показываем PopupWindow
        popupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);
    }

    private void addExampleToLayout(LinearLayout layout, String example, String translation) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        View exampleView = inflater.inflate(R.layout.popup_window, layout, false);

        TextView tvExample = exampleView.findViewById(R.id.wordText5);
        TextView tvExampleTranslation = exampleView.findViewById(R.id.wordText6);

        tvExample.setText(example);
        tvExampleTranslation.setText(translation);

        layout.addView(exampleView);
    }
}