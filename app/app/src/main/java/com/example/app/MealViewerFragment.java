package com.example.app;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MealViewerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MealViewerFragment extends Fragment {

    View view;

    // Firebase database reference
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    // the fragment initialization parameter
    //private static final String EMAIL = "email";
    private static final String MEAL_INDEX = "meal_index";
    // the string instance variable storing the cook's email
    //private String email;
    // the int instance variable storing the index of the meal in the array from the arrayAdpater
    private int mealIndex;

    public MealViewerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mealIndex index of the meal in the ArrayAdapter.
     * @return A new instance of fragment MealFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MealViewerFragment newInstance(int mealIndex) {
        MealViewerFragment fragment = new MealViewerFragment();
        Bundle args = new Bundle();
        //args.putString(EMAIL, email);
        args.putInt(MEAL_INDEX, mealIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //email = getArguments().getString(EMAIL);
            mealIndex = getArguments().getInt(MEAL_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_meal_viewer, container, false);

        // Close meal button from XML file
        ImageButton closeButton = (ImageButton) view.findViewById(R.id.mealViewerCloseButton);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ClientSearchFragment());
            }
        });

        // Meal information TextViews from XML file
        TextView mealName = (TextView) view.findViewById(R.id.mealViewerName);
        TextView price = (TextView) view.findViewById(R.id.mealViewerPrice);
        TextView cookName = (TextView) view.findViewById(R.id.mealViewerCookName);
        TextView type = (TextView) view.findViewById(R.id.mealViewerType);
        TextView cuisine = (TextView) view.findViewById(R.id.mealViewerCuisine);
        TextView ingredients = (TextView) view.findViewById(R.id.mealViewerIngredients);
        TextView allergens = (TextView) view.findViewById(R.id.mealViewerAllergens);
        TextView description = (TextView) view.findViewById(R.id.mealViewerDescription);
        TextView cookRating = (TextView) view.findViewById(R.id.mealViewerRating);
        TextView ratingNumberMessage = (TextView) view.findViewById(R.id.mealViewerNumberOfRatings);

        // Updating cook information on xml from database
        database.getReference("Meals").child(String.valueOf(mealIndex)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    Log.i("Firebase", "Meal retrieval successful.");
                    Map<String, Object> meal = (Map<String, Object>) snapshot.getValue();
                    // Setting meal name
                    mealName.setText((String) meal.get("name"));
                    //Setting meal price
                    price.append( (String) Long.toString( (Long) meal.get("price")));
                    // Setting meal cook name and rating
                    database.getReference("Accounts").child("Cooks").child((String) meal.get("cookEmail")).child("AccountInfo").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                Log.i("Firebase", "Cook name for meal retrieval successful.");
                                DataSnapshot snapshot1 = task.getResult();
                                Map<String, Object> accountInfo = (Map<String, Object>) snapshot1.getValue();
                                // Setting cook name
                                String firstName = (String) accountInfo.get("firstName");
                                String lastName = (String) accountInfo.get("lastName");
                                cookName.append( firstName + " " + lastName);
                                // Setting cook rating
                                Double rating = Double.parseDouble(String.valueOf(accountInfo.get("rating")));
                                updateRatingStars(rating);
                                if (rating != 0) {
                                    cookRating.setText(rating + " / 5");
                                }
                                // Setting number of ratings
                                ratingNumberMessage.append(String.valueOf(accountInfo.get("numberOfRatings")) + " ratings.");
                            } else {
                                Log.e("Firebase", "Cook name for meal retrieval failed.");
                            }
                        }
                    });
                    // Setting meal type
                    type.setText(Html.fromHtml("<b>Type:</b> ", Html.FROM_HTML_MODE_LEGACY));
                    type.append( (String) meal.get("type"));
                    // Setting meal cuisine type
                    cuisine.setText(Html.fromHtml("<b>Cuisine:</b> ", Html.FROM_HTML_MODE_LEGACY));
                    cuisine.append( (String) meal.get("cuisineType"));
                    // Setting meal ingredients
                    ArrayList<String> ingredientsArrayList = (ArrayList<String>) meal.get("ingredients");
                    ingredients.setText(Html.fromHtml("<b>Ingredients:</b> ", Html.FROM_HTML_MODE_LEGACY));
                    for (int i = 0; i < ingredientsArrayList.size()-1; i++) {
                        String text = ingredientsArrayList.get(i) + ", ";
                        ingredients.append(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
                    }
                    ingredients.append(ingredientsArrayList.get(ingredientsArrayList.size()-1));
                    // Setting meal allergens
                    ArrayList<String> allergensArrayList = (ArrayList<String>) meal.get("allergens");
                    allergens.setText(Html.fromHtml("<b>Allergens:</b> ", Html.FROM_HTML_MODE_LEGACY));
                    for (int i = 0; i < allergensArrayList.size()-1; i++) {
                        allergens.append(allergensArrayList.get(i) + ", ");
                    }
                    allergens.append(allergensArrayList.get(allergensArrayList.size()-1));
                    // Setting meal description
                    description.setText(Html.fromHtml("<b>Description:</b> ", Html.FROM_HTML_MODE_LEGACY));
                    description.append( (String) meal.get("description"));
                } else {
                    Log.e("Firebase", "Meal retrieval failed.");
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Fragment", getClass().getName() + " Destroyed");
    }

    private void updateRatingStars(double rating) {
        // Stars from XML from left to right
        ImageView star1 = (ImageView) view.findViewById(R.id.mealViewerStar1);
        ImageView star2 = (ImageView) view.findViewById(R.id.mealViewerStar2);
        ImageView star3 = (ImageView) view.findViewById(R.id.mealViewerStar3);
        ImageView star4 = (ImageView) view.findViewById(R.id.mealViewerStar4);
        ImageView star5 = (ImageView) view.findViewById(R.id.mealViewerStar5);

        // Deciding which to turn on/off
        if (rating > 4 && rating < 5) {
            star5.setImageResource(R.drawable.ic_baseline_star_half_24);
        } else if (rating < 5) {
            star5.setColorFilter(Color.LTGRAY);
        }
        if (rating > 3 && rating < 4) {
            star4.setImageResource(R.drawable.ic_baseline_star_half_24);
        } else if (rating < 4) {
            star4.setColorFilter(Color.LTGRAY);
        }
        if (rating > 2 && rating < 3) {
            star3.setImageResource(R.drawable.ic_baseline_star_half_24);
        } else if (rating < 3) {
            star3.setColorFilter(Color.LTGRAY);
        }
        if (rating > 1 && rating < 2) {
            star2.setImageResource(R.drawable.ic_baseline_star_half_24);
        } else if (rating < 2) {
            star2.setColorFilter(Color.LTGRAY);
        }
        if (rating == 0) {
            star1.setColorFilter(Color.LTGRAY);
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.clientContentFrame, fragment);
        fragmentTransaction.commit();
    }
}