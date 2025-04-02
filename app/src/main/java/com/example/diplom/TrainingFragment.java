package com.example.diplom;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.widget.ImageButton;
import java.util.Collections;
import java.util.List;

public class TrainingFragment extends Fragment {
    private DatabaseHelper databaseHelper;
    private TextView wordTextView;
    private Button[] answerButtons = new Button[6];
    private MediaPlayer correctSoundPlayer;
    private MediaPlayer wrongSoundPlayer;
    private static final long FEEDBACK_DURATION = 1000; // 1 секунда для фидбека
    private static final long NEW_WORD_DELAY = 1500; // 1.5 секунды до нового слова
    private com.example.diplom.WordPair currentWord;
    private ProgressBar progressBar;
    private CountDownTimer countDownTimer;
    private static final long TIMER_DURATION = 10000; // 10 секунд

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getActivity());
        String dbPath = DatabaseHelper.getDatabasePath();
    }

    private void loadNewWord() {
        resetButtonsColor();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        // Получаем случайное слово из базы
        com.example.diplom.WordPair randomWord = databaseHelper.getRandomWord();
        if (randomWord == null) {
            // Если база пуста, показываем сообщение
            wordTextView.setText("Нет слов в базе");
            return;
        }

        currentWord = randomWord;
        wordTextView.setText(currentWord.getForeignWord());

        // Получаем 5 случайных неправильных переводов
        List<String> possibleAnswers = databaseHelper.getRandomTranslations(5, currentWord.getTranslation());

        // Добавляем правильный ответ
        possibleAnswers.add(currentWord.getTranslation());

        // Перемешиваем ответы
        Collections.shuffle(possibleAnswers);

        // Устанавливаем текст на кнопках
        for (int i = 0; i < 6 && i < possibleAnswers.size(); i++) {
            final String answer = possibleAnswers.get(i);
            answerButtons[i].setText(answer);
            answerButtons[i].setOnClickListener(v -> checkAnswer(answer));

        // Удаляем предыдущие слушатели
        answerButtons[i].setOnClickListener(null);

        // Добавляем новый слушатель
        answerButtons[i].setOnClickListener(v -> {
            checkAnswer(answer);
        });
        // Запускаем таймер для нового слова
            startTimer();
    }};



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_practice, container, false);

        // Инициализация UI элементов
        wordTextView = view.findViewById(R.id.textView7);
        answerButtons[0] = view.findViewById(R.id.English1);
        answerButtons[1] = view.findViewById(R.id.English2);
        answerButtons[2] = view.findViewById(R.id.English3);
        answerButtons[3] = view.findViewById(R.id.English4);
        answerButtons[4] = view.findViewById(R.id.English5);
        answerButtons[5] = view.findViewById(R.id.English6);
        ImageButton closeButton = view.findViewById(R.id.imageButton2);


        // Инициализация звуков
        correctSoundPlayer = MediaPlayer.create(getContext(), R.raw.correct_sound);
        wrongSoundPlayer = MediaPlayer.create(getContext(), R.raw.wrong_sound);

        // Настройка кнопки закрытия
        closeButton.setOnClickListener(v -> {
            // Возврат на главный экран
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setMax(100);

        // Загрузка нового слова
        loadNewWord();

        return view;
    }

    private void playSound(MediaPlayer mediaPlayer) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(0); // Перемотка на начало, если звук уже играл
            mediaPlayer.start();
        }
    }

    private void checkAnswer(String selectedAnswer) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        // Блокируем кнопки на время фидбека
        setButtonsEnabled(false);

        boolean isCorrect = selectedAnswer.equals(currentWord.getTranslation());

        // Воспроизводим звук
        playSound(isCorrect ? correctSoundPlayer : wrongSoundPlayer);

        // Подсвечиваем правильный ответ зеленым
        highlightCorrectAnswer();

        // Если ответ неверный, подсвечиваем выбранную кнопку красным и вибрируем
        if (!isCorrect) {
            highlightSelectedAnswer(selectedAnswer);
            Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null) {
                vibrator.vibrate(200);}
        }

        // Показываем общий фидбек
        showFeedback(isCorrect);

        // Загружаем новое слово после задержки
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            setButtonsEnabled(true);
            loadNewWord();
        }, NEW_WORD_DELAY);
    }

    private void showFeedback(boolean isCorrect) {
        // Меняем цвет фона на короткое время
        View rootView = getView();
        if (rootView != null) {
            int color = isCorrect ? Color.GREEN : Color.RED;
            rootView.setBackgroundColor(color);

            // Возвращаем обычный цвет через 1 секунду
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                rootView.setBackgroundColor(Color.TRANSPARENT);
            }, 1000);
        }
    }

    private void setButtonsEnabled(boolean enabled) {
        for (Button button : answerButtons) {
            button.setEnabled(enabled);
        }
    }

    private void highlightCorrectAnswer() {
        for (Button button : answerButtons) {
            if (button.getText().toString().equals(currentWord.getTranslation())) {
                button.setBackgroundColor(Color.GREEN);
                break;
            }
        }
    }

    private void highlightSelectedAnswer(String selectedAnswer) {
        for (Button button : answerButtons) {
            if (button.getText().toString().equals(selectedAnswer)) {
                button.setBackgroundColor(Color.RED);
                break;
            }
        }
    }

    private void resetButtonsColor() {
        for (Button button : answerButtons) {
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.default_button_color));
        }
    }

    @Override
    public void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        super.onDestroy();
        // Освобождаем ресурсы MediaPlayer
        if (correctSoundPlayer != null) {
            correctSoundPlayer.release();
        }
        if (wrongSoundPlayer != null) {
            wrongSoundPlayer.release();
        }
    }

    // Вспомогательный класс для хранения пар слов
    private static class WordPair {
        private String foreignWord;
        private String translation;

        public WordPair(String foreignWord, String translation) {
            this.foreignWord = foreignWord;
            this.translation = translation;
        }

        public String getForeignWord() {
            return foreignWord;
        }

        public String getTranslation() {
            return translation;
        }
    }

    private void startTimer() {
        // Отменяем предыдущий таймер, если он был
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Анимация прогресс-бара
        ObjectAnimator animator;
        animator = ObjectAnimator.ofInt(progressBar, "progress", 100, 0);
        animator.setDuration(TIMER_DURATION);
        animator.start();

        // Таймер обратного отсчета
        countDownTimer = new CountDownTimer(TIMER_DURATION, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Можно обновлять текст таймера, если нужно
                // timerTextView.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                // Время вышло
                playSound(wrongSoundPlayer);
                showFeedback(false);
                Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator != null) {
                    vibrator.vibrate(200);}

                // Задержка перед новым словом
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    loadNewWord();
                }, NEW_WORD_DELAY);
            }
        }.start();
    }

}


