package com.openmessenger.supermiware.openmessenger;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Main2Activity extends AppCompatActivity {

    // Main2Activity's function will be contact management.  Creating contacts as well as creating
    // the link to those contacts to start and stop communication.
    // The over all objective of Open Messenger will be to stay simple and clean.

    // _____________________________________________________________________________________________

    // TextViews
    private TextView emailIntentPass;

    // LayoutInflaters
    private LayoutInflater inflater;

    // LinearLayouts
    private LinearLayout linearLayout;

    // Views
    private View buttonXMLViewer;

    // EditTexts
    private EditText addContactsField;

    // Strings
    private String nothing;
    private String contactNameString;
    private String addContactsButtonString;
    private String emailRec;
    private String textFromButton;

    // Buttons
    private Button xmlButton;
    private Button addContactsButton;
    private Button contactButton;

    // Firebase Database
    private FirebaseDatabase database;
    private DatabaseReference mDataRef;

    // _________________________________________________________________________________________

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.contacts_activity);

            // Initialize main variables.

            nothing = "";
            addContactsField = findViewById(R.id.contactNameField);
            emailIntentPass = findViewById(R.id.emailPassedOver);
            addContactsButton = findViewById(R.id.addContactButton);
            database = FirebaseDatabase.getInstance();
            mDataRef = database.getReference();

            Intent recEmail = getIntent();
            Bundle bundleRecEbail = recEmail.getExtras();
            final String bundleRecEmailString = bundleRecEbail.getString("emailPass", "Whoops");
            emailIntentPass.setText(bundleRecEmailString);


            // _________________________________________________________________________________________

            // Receive the emailRec intent push from MainActivity and store that data as a string
            // Once a string the emailIntentPass label will be set to that passed email address.

            if (bundleRecEbail != null) {

                emailRec = getIntent().getExtras().getString("emailPass");
                emailIntentPass.setText(emailRec);

            } else {

                Toast.makeText(this, "Hmm. You sure you're not missing something?", Toast.LENGTH_SHORT).show();

            }

            // Initialize the addContactButton Button and create its' rules.
            // addContactsButton when tapped with consider weather or not the contatsNameField has
            // anything entered and if it dose a button will appear that will give access to the next
            // Activity (Main3Activity) which will all the user to contact that user back and forth.

            addContactsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // turn addContactsField into a string
                    contactNameString = addContactsField.getText().toString();
                    addContactsButtonString = addContactsField.getText().toString();

                    if (!contactNameString.equals(nothing)) {

                        addContactButton();
                        mDataRef.child(emailIntentPass.getText().toString().replace("@Open.com", "")).child("Contacts");
                        mDataRef.child(emailIntentPass.getText().toString().replace("@Open.com", "")).child("Contacts").child(addContactsField.getText().toString()).setValue("");

                        contactButton = findViewById(R.id.button_small_left);
                        contactButton.setText(addContactsButtonString);
                        contactButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                xmlButton = findViewById(R.id.button_small_left);
                                xmlButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        textFromButton = xmlButton.getText().toString();
                                        Intent toMessage = new Intent(Main2Activity.this, Main3Activity.class);
                                        toMessage.putExtra("contactIDPass", textFromButton);
                                        Log.d("intentTest", "" + textFromButton);
                                        startActivity(toMessage);
                                    }
                                });

                            }
                        });

                    } else {

                        Toast.makeText(Main2Activity.this, "You have to add a contact... duh.", Toast.LENGTH_SHORT).show();
                    }

                    // End of 1st if statement within addContatButton
                    // NOTE: xmlButton onClickListener is still inside the addContactButton onClickListener
                    // NOTEExt: I don't want any pre-user compiler issues to arise from the button not being
                    // NOTEExt: available to the user before the addContactButton is tapped.

                    // _________________________________________________________________________________


                }
            });
        }

        // _____________________________________________________________________________________________

        // This method pulls the xmlButton from the addContactButton layout xml file.
        // NOTE: index must be applied to the end of add view parameter field in order for the button
        // NOTEExt: to be repeatedly used.  If index parameter is not present, the xml button will only apply
        // NOTEExt: changes to the first instance of the button and not any of the other instances.

        private void addContactButton() {

            inflater = getLayoutInflater();
            buttonXMLViewer = inflater.inflate(R.layout.addcontactbutton, null);
            linearLayout = findViewById(R.id.linearScrolView);
            linearLayout.addView(buttonXMLViewer, 0);


        }
    }