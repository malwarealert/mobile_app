package com.example.diplom;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button practiceButton = findViewById(R.id.Practice);
        practiceButton.setOnClickListener(v -> {
            // Создаем фрагмент практики
            TrainingFragment trainingFragment = new TrainingFragment();

            // Заменяем текущий фрагмент
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, trainingFragment)
                    .addToBackStack(null) // Добавляем в back stack для возврата кнопкой "Назад"
                    .commit();
        });

        // Открытие фрагмента категорий
        Button categoriesButton = findViewById(R.id.cs2);
        categoriesButton.setOnClickListener(v -> {
            CategoryFragment categoriesFragment = new CategoryFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, categoriesFragment)
                    .addToBackStack(null)
                    .commit();
        });


        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                int id = item.getItemId();

                if (id == R.id.Category) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new CategoryFragment())
                            .commit();
                    return true;
                }
                else if (id == R.id.Training) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new TrainingFragment())
                            .commit();
                    return true;
                }
                else if (id == R.id.Profile) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new ProfileFragment())
                            .commit();
                    return true;
                }

                return false;
            };
}