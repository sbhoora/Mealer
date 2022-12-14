package com.example.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

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
    private static final String CREATED_BY = "creating_by";
    private static final String MEAL_INDEX = "meal_index";
    private static final String EMAIL = "email";
    // the string instance variable storing the fragment that called MealViewer
    private String createdBy;
    // the int instance variable storing the index of the meal in the array from the arrayAdpater
    private int mealIndex;
    // the string instance variable storing the email
    private String email;

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
    public static MealViewerFragment newInstance(int mealIndex, String createdBy, String email) {
        MealViewerFragment fragment = new MealViewerFragment();
        Bundle args = new Bundle();
        args.putString(CREATED_BY, createdBy);
        args.putInt(MEAL_INDEX, mealIndex);
        args.putString(EMAIL, email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            createdBy = getArguments().getString(CREATED_BY);
            mealIndex = getArguments().getInt(MEAL_INDEX);
            email = getArguments().getString(EMAIL);
        }
        Log.i("Fragment", getClass().getCanonicalName() + " Created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_meal_viewer, container, false);
        ImageButton closeButton = (ImageButton) view.findViewById(R.id.mealViewerCloseButton);

        System.out.println("The client email is: " + email);

        // Close meal button from XML file
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Bundle info to send to activity to end MealViewerFragment
                Bundle result = new Bundle();
                result.putBoolean("finished", true);
                // Setting result in parent fragment manager
                getParentFragmentManager().setFragmentResult("closeFragment", result);
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
        TextView rateMessage = (TextView) view.findViewById(R.id.mealViewerRateMessage);

        // Stores whether the client has already rated the cook
        Boolean rated = false;
        ImageButton star1 = (ImageButton) view.findViewById(R.id.mealViewerStar1);
        ImageButton star2 = (ImageButton) view.findViewById(R.id.mealViewerStar2);
        ImageButton star3 = (ImageButton) view.findViewById(R.id.mealViewerStar3);
        ImageButton star4 = (ImageButton) view.findViewById(R.id.mealViewerStar4);
        ImageButton star5 = (ImageButton) view.findViewById(R.id.mealViewerStar5);

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
                    // Setting meal cook name
                    DatabaseReference cookFromDatabase = database.getReference("Accounts").child("Cooks").child((String) meal.get("cookEmail"));
                    DatabaseReference clientFromDatabase = database.getReference("Accounts").child("Clients").child(email);

                    cookFromDatabase.child("AccountInfo").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot1) {
                            Log.i("Firebase", "Cook name for meal retrieval successful.");
                            Log.i("Firebase", "Cook rating update successful.");
                            Map<String, Object> accountInfo = (Map<String, Object>) snapshot1.getValue();
                            // Setting cook name
                            String firstName = (String) accountInfo.get("firstName");
                            String lastName = (String) accountInfo.get("lastName");
                            cookName.setText( "Offered by " + firstName + " " + lastName);
                            // Setting cook rating
                            Double rating = Double.parseDouble(String.valueOf(accountInfo.get("rating")));
                            updateRatingStars(rating);
                            if (rating != 0) {
                                cookRating.setText(rating + " / 5");
                            }
                            // Setting number of ratings
                            int numberOfRatings = Integer.parseInt(String.valueOf(accountInfo.get("numberOfRatings")));
                            ratingNumberMessage.setText("out of " + numberOfRatings + " rating(s).");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("Firebase", "Cook name for meal retrieval failed.");
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

                    if (createdBy.equals("ClientSearchFragment")){
                        Button submitButton = (Button) view.findViewById(R.id.submitRequest);
                        submitButton.setVisibility(view.VISIBLE);
                        submitButton.setText("Submit Meal Request");
                        submitButton.setEnabled(true);

                        submitButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                cookFromDatabase.child("Requests").child(email).child(meal.get("name").toString()).setValue("");
                                clientFromDatabase.child("History").child(meal.get("cookEmail").toString()).child(meal.get("name").toString()).setValue("Pending");
                            }

                        });
                    }

                    if (createdBy.equals("ClientHistoryFragment")) {
                        // Click on star rating
                        // Message for client to rate the cook
                        rateMessage.setText("Click on a star to leave a rating.");

                        Button submitButton = (Button) view.findViewById(R.id.submitRequest);
                        submitButton.setVisibility(view.VISIBLE);
                        submitButton.setEnabled(true);
                        submitButton.setText("Write a Complaint");

                        submitButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Bundle info to send to activity to end MealViewerFragment
                                Bundle result = new Bundle();
                                result.putBoolean("writeComplaint", true);
                                result.putString("cookEmail", (String) meal.get("cookEmail"));
                                // Setting result in parent fragment manager
                                getParentFragmentManager().setFragmentResult("goToClientComplaintFragment", result);
                            }
                        });


                        // Listeners for clicking on stars and updating rating value on database
                        star1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cookFromDatabase.child("AccountInfo").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            Log.i("Firebase", "Cook info retrieval successful for rating.");
                                            DataSnapshot snapshot1 = task.getResult();
                                            // map items of accountInfo
                                            Map<String, Object> accountInfo = (Map<String, Object>) snapshot1.getValue();
                                            // rating values
                                            Double rating = Double.parseDouble(String.valueOf(accountInfo.get("rating")));
                                            int numberOfRatings = Integer.parseInt(String.valueOf(accountInfo.get("numberOfRatings")));
                                            // Calculating new rating based on star that was clicked
                                            Double newRating = (( rating * numberOfRatings ) + 1 ) / (numberOfRatings + 1);
                                            newRating = round(newRating, 1);
                                            // Saving new rating back to database
                                            cookFromDatabase.child("AccountInfo").child("rating").setValue(newRating);
                                            cookFromDatabase.child("AccountInfo").child("numberOfRatings").setValue(numberOfRatings+1);
                                        } else {
                                            Log.e("Firebase", "Cook info retrieval failed for rating.");
                                        }
                                    }
                                });
                            }
                        });
                        star2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cookFromDatabase.child("AccountInfo").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            Log.i("Firebase", "Cook info retrieval successful for rating.");
                                            DataSnapshot snapshot1 = task.getResult();
                                            // map items of accountInfo
                                            Map<String, Object> accountInfo = (Map<String, Object>) snapshot1.getValue();
                                            // rating values
                                            Double rating = Double.parseDouble(String.valueOf(accountInfo.get("rating")));
                                            int numberOfRatings = Integer.parseInt(String.valueOf(accountInfo.get("numberOfRatings")));
                                            // Calculating new rating based on star that was clicked
                                            Double newRating = (( rating * numberOfRatings ) + 2 ) / (numberOfRatings + 1);
                                            newRating = round(newRating, 1);
                                            // Saving new rating back to database
                                            cookFromDatabase.child("AccountInfo").child("rating").setValue(newRating);
                                            cookFromDatabase.child("AccountInfo").child("numberOfRatings").setValue(numberOfRatings+1);
                                        } else {
                                            Log.e("Firebase", "Cook info retrieval failed for rating.");
                                        }
                                    }
                                });
                            }
                        });
                        star3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cookFromDatabase.child("AccountInfo").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            Log.i("Firebase", "Cook info retrieval successful for rating.");
                                            DataSnapshot snapshot1 = task.getResult();
                                            // map items of accountInfo
                                            Map<String, Object> accountInfo = (Map<String, Object>) snapshot1.getValue();
                                            // rating values
                                            Double rating = Double.parseDouble(String.valueOf(accountInfo.get("rating")));
                                            int numberOfRatings = Integer.parseInt(String.valueOf(accountInfo.get("numberOfRatings")));
                                            // Calculating new rating based on star that was clicked
                                            Double newRating = (( rating * numberOfRatings ) + 3 ) / (numberOfRatings + 1);
                                            newRating = round(newRating, 1);
                                            // Saving new rating back to database
                                            cookFromDatabase.child("AccountInfo").child("rating").setValue(newRating);
                                            cookFromDatabase.child("AccountInfo").child("numberOfRatings").setValue(numberOfRatings+1);
                                        } else {
                                            Log.e("Firebase", "Cook info retrieval failed for rating.");
                                        }
                                    }
                                });
                            }
                        });
                        star4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cookFromDatabase.child("AccountInfo").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            Log.i("Firebase", "Cook info retrieval successful for rating.");
                                            DataSnapshot snapshot1 = task.getResult();
                                            // map items of accountInfo
                                            Map<String, Object> accountInfo = (Map<String, Object>) snapshot1.getValue();
                                            // rating values
                                            Double rating = Double.parseDouble(String.valueOf(accountInfo.get("rating")));
                                            int numberOfRatings = Integer.parseInt(String.valueOf(accountInfo.get("numberOfRatings")));
                                            // Calculating new rating based on star that was clicked
                                            Double newRating = (( rating * numberOfRatings ) + 4 ) / (numberOfRatings + 1);
                                            newRating = round(newRating, 1);
                                            // Saving new rating back to database
                                            cookFromDatabase.child("AccountInfo").child("rating").setValue(newRating);
                                            cookFromDatabase.child("AccountInfo").child("numberOfRatings").setValue(numberOfRatings+1);
                                        } else {
                                            Log.e("Firebase", "Cook info retrieval failed for rating.");
                                        }
                                    }
                                });
                            }
                        });
                        star5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cookFromDatabase.child("AccountInfo").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            Log.i("Firebase", "Cook info retrieval successful for rating.");
                                            DataSnapshot snapshot1 = task.getResult();
                                            // map items of accountInfo
                                            Map<String, Object> accountInfo = (Map<String, Object>) snapshot1.getValue();
                                            // rating values
                                            Double rating = Double.parseDouble(String.valueOf(accountInfo.get("rating")));
                                            int numberOfRatings = Integer.parseInt(String.valueOf(accountInfo.get("numberOfRatings")));
                                            // Calculating new rating based on star that was clicked
                                            Double newRating = (( rating * numberOfRatings ) + 5 ) / (numberOfRatings + 1);
                                            newRating = round(newRating, 1);
                                            // Saving new rating back to database
                                            cookFromDatabase.child("AccountInfo").child("rating").setValue(newRating);
                                            cookFromDatabase.child("AccountInfo").child("numberOfRatings").setValue(numberOfRatings+1);
                                        } else {
                                            Log.e("Firebase", "Cook info retrieval failed for rating.");
                                        }
                                    }
                                });
                            }
                        });
                    }

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
        Log.i("Fragment", getClass().getCanonicalName() + " Destroyed");
    }

    private void rated(Boolean rated) {
        rated = true;
    }

    private static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    private void updateRatingStars(double rating) {
        // Stars from XML from left to right
        ImageButton star1 = (ImageButton) view.findViewById(R.id.mealViewerStar1);
        ImageButton star2 = (ImageButton) view.findViewById(R.id.mealViewerStar2);
        ImageButton star3 = (ImageButton) view.findViewById(R.id.mealViewerStar3);
        ImageButton star4 = (ImageButton) view.findViewById(R.id.mealViewerStar4);
        ImageButton star5 = (ImageButton) view.findViewById(R.id.mealViewerStar5);

        // Rate Message
        TextView rateMessage = (TextView) view.findViewById(R.id.mealViewerRateMessage);

        if (isAdded()) {
            // Drawables
            Drawable fullStar = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_baseline_star_24);
            Drawable halfStar = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_baseline_star_half_24);

            // Grey ColorFilter
            PorterDuffColorFilter lightGrey = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);

            // Making sure all stars are default sized yellow first
            star5.setColorFilter(null);
            star4.setColorFilter(null);
            star3.setColorFilter(null);
            star2.setColorFilter(null);
            star1.setColorFilter(null);

            ImageButton[] stars = new ImageButton[5];
            stars[0] = star1;
            stars[1] = star2;
            stars[2] = star3;
            stars[3] = star4;
            stars[4] = star5;

            for (int i = 4; i >= rating; i--) {
                stars[i].setBackground(fullStar);
                stars[i].getBackground().setColorFilter(lightGrey);
            }
            if (rating != 0) {
                if ((rating * 10) % 10 != 0) {
                    stars[(int) Math.ceil(rating) - 1].setBackground(halfStar);
                }
            }
        } else {
            rateMessage.setText("Rating could not be updated.");
        }
    }
}