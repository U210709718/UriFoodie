package com.example.urifoodie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyRecipesPageFragment extends Fragment {

    private RecyclerView recipeRecyclerView;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipeList;
    private EditText editRecipeInput;


//    public MyRecipesPageFragment() {
//        // Required empty public constructor
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_recipes_page, container, false);

        recipeRecyclerView = view.findViewById(R.id.recipeRecyclerView);
        editRecipeInput = view.findViewById(R.id.editRecipeInput);
        ImageView addRecipeButton = view.findViewById(R.id.addRecipeButton);

        recipeList = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(recipeList);

        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recipeRecyclerView.setAdapter(recipeAdapter);

        addRecipeButton.setOnClickListener(v -> {
            String recipeText = editRecipeInput.getText().toString().trim();
            if (!recipeText.isEmpty()) {
                recipeList.add(new Recipe(recipeText, "Ingredients: TBD"));
                recipeAdapter.notifyItemInserted(recipeList.size() - 1);
                editRecipeInput.setText("");
            }
        });

        return view;
    }
}