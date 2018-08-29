package com.openmessenger.supermiware.openmessenger;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.provider.SyncStateContract;
import android.sax.StartElementListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.TextKeyListener;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    // Buttons

    private Button loginTabButton;
    private Button registerTabButton;
    private Button doneButton;

        // EditTexts
        private EditText emailField;
        private EditText passwordField;
        private EditText passwordConfirmField;
        private EditText firstNameField;
        private EditText lastNameField;
        private EditText phoneNumberField;

            // Switchs
            private Switch keepLoggedInSwitch;

                // Intents
                private Intent passToMain;

                    // Animations
                    private Animation slideDownAnimation;

                        // Firebase Vars
                        private FirebaseAuth mAuth;
                        private FirebaseDatabase mFirebaseDatabase;
                        private DatabaseReference mDataRef;

                            // TextViews
                            private TextView openTutText;

                                // Strings
                                private String emailFieldString;
                                private String passwordConfirmFieldString;
                                private String passwordFieldString;

                                    // Booleans
                                    private Boolean isLogState;

    // _____________________________________________________________________________________________

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // This private method loads all the UI objects.
        // METHOD PLUGIN
        loadUIGeneralVars();

        firstNameField.setVisibility(View.GONE);
        lastNameField.setVisibility(View.GONE);
        phoneNumberField.setVisibility(View.GONE);

        // We're going to figure out if the user has already register their account.
        // and if they have we're going to cause the registered user to go to the Main2Activities.

        loginTabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO: make Visible and Invisible functions
                slideDownAnimation.setFillAfter(false);
                passwordConfirmField.setVisibility(View.GONE);
                firstNameField.setVisibility(View.GONE);
                lastNameField.setVisibility(View.GONE);
                phoneNumberField.setVisibility(View.GONE);

                // Inside the code for this instance if the doneButton, the app should check
                // to see if the user has credentials as logged by the user.
                // if so.  Go to Main2Activity.
                // if not. Stay on MainActivity and deliver Toast to user stayting that the user
                // must either register or repair potentially broken credentials.

                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                    }
                });

            }
        });

        // _________________________________________________________________________________________


        // RegisterTabButton sets UI to set user credentials by adding password confirmField
        // Username must be less than 10 characters and password must be less than 20 characters
        // Username will be annexed with the @open.com extension.
        // NOTE: The username annex system has not been set up yet

        registerTabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.animation_slide_down);

                slideDownAnimation.setFillAfter(true);
                slideDownAnimation.setDuration(90);

                passwordConfirmField.startAnimation(slideDownAnimation);
                firstNameField.startAnimation(slideDownAnimation);
                lastNameField.startAnimation(slideDownAnimation);
                phoneNumberField.startAnimation(slideDownAnimation);

                passwordConfirmField.setVisibility(View.VISIBLE);
                firstNameField.setVisibility(View.VISIBLE);
                lastNameField.setVisibility(View.VISIBLE);
                phoneNumberField.setVisibility(View.VISIBLE);

            }
        });

        // _________________________________________________________________________________________


        // DoneButton checks username and password credentials and stores
        // username and password to Firebase Database, if approved.
        // doneButton will create strings for the emailField and for the passwordField
        // donebutton will then check the information for matches and credentials and store them in
        // Firebase.  After, then intent will pass the user to the main page.

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                emailFieldString = emailField.getText().toString();
                passwordConfirmFieldString = passwordConfirmField.getText().toString();
                passwordFieldString = passwordField.getText().toString();

                // This method will check to make sure the user has entered @open.com.
                // And if the user hasn't than Open will add it for the user.

                if(!emailFieldString.contains("@open.com") & passwordFieldString.equals(passwordConfirmFieldString)) {

                    emailFieldString = emailFieldString + "@Open.com";
                    Log.d("checkOpenCom", "We added @open.com for you.");
                    Log.d("checkString", emailFieldString);

                    // Firebase Database will receive all initial user data.
                    // Keys are stored with each data piece and then the system will store
                    // user Auth data to Firebase.
                    storeUserFirebaseData();
                    storeUser();

                    // Toast will let the user know that the everything is working just fine.
                    Toast.makeText(MainActivity.this, "Yep", Toast.LENGTH_SHORT).show();

                    passToMain = new Intent(MainActivity.this, Main2Activity.class);
                    passToMain.putExtra("emailPass", emailFieldString + "@Open.com");
                    startActivity(passToMain);

                    // If keepLoggedInSwitch is checked the app needs to remember this and
                    // store that state in the local memory.

                    if (keepLoggedInSwitch.isChecked()) {

                        Boolean switchIsChecked = true;

                        mFirebaseDatabase = FirebaseDatabase.getInstance();
                        mDataRef = mFirebaseDatabase.getReference();
                        mDataRef.child("allUsers").push().setValue(emailField + " = " + switchIsChecked);

                        Toast.makeText(MainActivity.this, "Okay I'll keep you logged in as " + emailFieldString, Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(MainActivity.this, "Okay. We won't keep you looged in for now.", Toast.LENGTH_SHORT).show();

                    }
                }else if(emailFieldString.contains("@open.com") & passwordFieldString.equals(passwordConfirmFieldString)){

                        mFirebaseDatabase = FirebaseDatabase.getInstance();
                        mDataRef = mFirebaseDatabase.getReference();
                        mDataRef.child("allUsers").push().setValue(emailFieldString);
                        mDataRef.child(emailFieldString).push().setValue("test");

                        storeUser();

                        Toast.makeText(MainActivity.this, "Yep", Toast.LENGTH_SHORT).show();

                        passToMain = new Intent(MainActivity.this, Main2Activity.class);
                        passToMain.putExtra("emailPass", emailFieldString);
                        startActivity(passToMain);

                    }else{

                    }

                }
        });
    }

        // _________________________________________________________________________________________

    // onStart Method

    @Override
    public void onStart(){
        super.onStart();

        FirebaseUser User = mAuth.getCurrentUser();

    }

        // _________________________________________________________________________________________

    // The loadUIGeneralVars method will be responsible for loading all general variables.
    private void loadUIGeneralVars(){

        loginTabButton = findViewById(R.id.loginTabButton);
        registerTabButton = findViewById(R.id.registerTabButton);
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        passwordConfirmField = findViewById(R.id.passwordConfirmField);
        doneButton = findViewById(R.id.doneButton);
        keepLoggedInSwitch = findViewById(R.id.keepLoggedInSwitch);
        firstNameField = findViewById(R.id.firstNameField);
        lastNameField = findViewById(R.id.lastNameField);
        phoneNumberField = findViewById(R.id.phoneNumberField);

        // Sets passwordConfirmField and openTutText to invisible.
        mAuth = FirebaseAuth.getInstance();
        passwordConfirmField.setVisibility(View.GONE);

    }

        // _________________________________________________________________________________________


    private void storeUser(){

        emailFieldString = emailField.getText().toString();
        passwordFieldString = passwordField.getText().toString();

        mAuth.createUserWithEmailAndPassword(emailFieldString, passwordFieldString)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        FirebaseUser user = mAuth.getCurrentUser();

                    }
                });
    }

    // Store all the data for user interaction
    private void storeUserFirebaseData(){

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDataRef = mFirebaseDatabase.getReference();
        mDataRef.child(emailField.getText().toString());
        mDataRef.child(emailField.getText().toString()).child("Username").setValue(emailField.getText().toString());
        mDataRef.child(emailField.getText().toString()).child("FirstName").setValue(firstNameField.getText().toString());
        mDataRef.child(emailField.getText().toString()).child("LastName").setValue(lastNameField.getText().toString());
        mDataRef.child(emailField.getText().toString()).child("PhoneNumber").setValue(phoneNumberField.getText().toString());
        mDataRef.child(emailField.getText().toString()).child("Password").setValue(passwordField.getText().toString());

    }

}
