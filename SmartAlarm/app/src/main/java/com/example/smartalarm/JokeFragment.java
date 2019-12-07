package com.example.smartalarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Random;

public class JokeFragment extends Fragment {

    private static String[] jokes = new String[]{
            "What do you call an alligator in a vest?\n\nAn investigator",
            "What did one snowman say to the other?\n\nDo you smell carrots?",
            "How do robots eat guacamole?\n\nWith computer chips.",
            "Why did the banana go to the doctor?\n\nIt didn't peel well!",
            "What do you call a sleeping bull?\n\nA bulldozer",
            "What has four wheels and flies?\n\nA garbage truck.",
            "What do you call a fly with no wings?\n\nA walk."

    };

    private TextView jokeText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_joke, container, false);

        jokeText = view.findViewById(R.id.joke_text);

        int jokeNumber = new Random().nextInt(jokes.length);
        jokeText.setText(jokes[jokeNumber]);

        return view;
    }
}
