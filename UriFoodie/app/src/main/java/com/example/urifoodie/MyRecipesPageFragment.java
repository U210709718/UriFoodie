package com.example.urifoodie;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyRecipesPageFragment extends Fragment {

    private RecyclerView recipeRecyclerView;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipeList = new ArrayList<>();
    private FirebaseFirestore firestore;
    private CollectionReference recipesCollection;

    private EditText inputIngredients;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_recipes_page, container, false);

        recipeRecyclerView = view.findViewById(R.id.recipeRecyclerView);
        inputIngredients = view.findViewById(R.id.editRecipeInput);
        ImageView addRecipeButton = view.findViewById(R.id.addRecipeButton);

        // Set up RecyclerView
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recipeAdapter = new RecipeAdapter(recipeList);
        recipeRecyclerView.setAdapter(recipeAdapter);

        // Set up Firestore
        firestore = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        recipesCollection = firestore.collection("Users").document(userId).collection("Recipes");

        // Load recipes from Firestore
        loadRecipesFromFirestore();

        // Add recipe to Firestore and local list
        addRecipeButton.setOnClickListener(v -> {
            String ingredients = inputIngredients.getText().toString().trim();

            if (!TextUtils.isEmpty(ingredients)) {
                addRecipeToFirestore(ingredients);
                inputIngredients.setText("");
            }
        });

        return view;
    }

    private void loadRecipesFromFirestore() {
        recipesCollection.get().addOnSuccessListener(querySnapshot -> {
            recipeList.clear();
            for (com.google.firebase.firestore.DocumentSnapshot document : querySnapshot.getDocuments()) {
                Recipe recipe = document.toObject(Recipe.class);
                recipeList.add(recipe);
            }
            recipeAdapter.notifyDataSetChanged();
        });
    }

    private void addRecipeToFirestore(String ingredients) {
        Map<String, String> recipeData = new HashMap<>();
        recipeData.put("ingredients", ingredients);

        recipesCollection.add(recipeData).addOnSuccessListener(documentReference -> {
            Recipe recipe = new Recipe(ingredients);
            recipeList.add(recipe);
            recipeAdapter.notifyDataSetChanged();
        });
    }
}