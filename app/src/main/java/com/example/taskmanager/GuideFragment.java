package com.example.taskmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Фрагмент для отображения руководства пользователя.
 */
public class GuideFragment extends Fragment {

    /**
     * Создает и настраивает пользовательский интерфейс фрагмента.
     *
     * @param inflater           объект, который может преобразовать XML-файл макета в объекты View.
     * @param container          родительский объект View, к которому будет присоединен макет фрагмента.
     * @param savedInstanceState объект, содержащий данные о состоянии фрагмента.
     * @return View, представляющий пользовательский интерфейс фрагмента.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Инфлейтим layout фрагмента
        return inflater.inflate(R.layout.fragment_guide, container, false);
    }
}
