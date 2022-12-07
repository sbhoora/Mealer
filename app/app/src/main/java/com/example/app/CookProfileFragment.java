package com.example.app;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

import org.w3c.dom.Text;

import java.util.Map;

public class CookProfileFragment extends Fragment {

    View view;

    // Firebase database reference
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    // the fragment initialization parameter
    private static final String EMAIL = "email";
    // the string instance variable storing the email
    private String email;

    public CookProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param email Parameter 1.
     * @return A new instance of fragment CookProfileFragment.
     */
    public static CookProfileFragment newInstance(String email) {
        CookProfileFragment fragment = new CookProfileFragment();
        Bundle args = new Bundle();
        args.putString(EMAIL, email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            email = getArguments().getString(EMAIL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cook_profile, container, false);

        // Logout Button
        ImageButton logoutButton = view.findViewById(R.id.cookLogoutButton);

        // Logout functionality on button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        // Cook information TextViews from XML file
        TextView cookProfileName = (TextView) view.findViewById(R.id.cookProfileName);
        TextView cookProfileEmail = (TextView) view.findViewById(R.id.cookProfileEmail);
        TextView cookProfileAddress = (TextView) view.findViewById(R.id.cookProfileAddress);
        TextView cookProfileDescription = (TextView) view.findViewById(R.id.cookProfileDescription);
        TextView cookProfileRating = (TextView) view.findViewById(R.id.cookProfileRating);
        TextView ratingNumberMessage = (TextView) view.findViewById(R.id.cookProfileNumberOfRatings);

        // Updating cook information on xml from database
        database.getReference("Accounts").child("Cooks").child(email).child("AccountInfo").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    Log.i("Firebase", "Cook Retrieval Successful for cook: " + email);
                    Map<String, Object> accountInfo = (Map<String, Object>) snapshot.getValue();
                    // Setting cook name
                    cookProfileName.setText(accountInfo.get("firstName") + " " + accountInfo.get("lastName"));
                    //Setting cook email
                    cookProfileEmail.setText( (String) accountInfo.get("email"));
                    // Setting cook address
                    cookProfileAddress.setText(snapshot.child("address").getValue(Address.class).toString());
                    // Setting cook description
                    cookProfileDescription.append( (String) accountInfo.get("description"));
                    // Setting stars' appearance
                    Double rating = Double.parseDouble(String.valueOf(accountInfo.get("rating")));
                    updateRatingStars(rating);
                    if (rating != 0) {
                        cookProfileRating.setText(rating + " / 5");
                    }
                    // Setting number of ratings
                    ratingNumberMessage.append(String.valueOf(accountInfo.get("numberOfRatings")) + " ratings.");

                } else {
                    Log.e("Firebase", "Cook Retrieval Failed for cook:" + email);
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
        ImageView star1 = (ImageView) view.findViewById(R.id.cookProfileStar1);
        ImageView star2 = (ImageView) view.findViewById(R.id.cookProfileStar2);
        ImageView star3 = (ImageView) view.findViewById(R.id.cookProfileStar3);
        ImageView star4 = (ImageView) view.findViewById(R.id.cookProfileStar4);
        ImageView star5 = (ImageView) view.findViewById(R.id.cookProfileStar5);

        ImageView[] stars = new ImageView[5];
        stars[0] = star1;
        stars[1] = star2;
        stars[2] = star3;
        stars[3] = star4;
        stars[4] = star5;

        // Deciding which to turn on/off
        for (int i = 4; i >= rating; i--) {
            stars[i].setImageResource(R.drawable.ic_baseline_star_24);
            stars[i].setColorFilter(Color.LTGRAY);
        }
        if (rating != 0) {
            if ((rating * 10) % 10 != 0) {
                stars[(int) Math.ceil(rating) - 1].setImageResource(R.drawable.ic_baseline_star_half_24);
            }
        }
    }
}