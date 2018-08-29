package com.openmessenger.supermiware.openmessenger;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Main3Activity extends AppCompatActivity {

    // ImageButtons
    private Button sendButton;

    // LayoutInflaters
    private LayoutInflater inflater;

    // Views
    private View addSendTextLabel;

    // LinearLayout
    private LinearLayout linearLayout;

    // TextViews
    private TextView sentMessege;
    private TextView whosMessaging;

    // EditTexts
    private EditText messageText;

    // Animations
    private Animation slideDownAnimation;

    // Strings
    private String messageTextString;
    private String bundleRecIntentString;

    //Intents
    private Intent intentFrom;

    // Bundles
    private Bundle dataPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        recContactInformation();

        sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                messageText = findViewById(R.id.messageText);
                messageTextString = messageText.getText().toString();

                // Cause instance of setSendMessageContent to be used
                SendMessageContent();

                slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_slide_down);

                slideDownAnimation.setFillAfter(true);
                slideDownAnimation.setDuration(800);
                sentMessege = findViewById(R.id.messageSendText);
                sentMessege.startAnimation(slideDownAnimation);

                // Set the text of the message
                sentMessege.setText(messageTextString);

            }
        });
    }

    // _____________________________________________________________________________________________


    // Method will be responsible for creating instance of sent messages and will send them back and forth
    // to the firebase server.

    private void SendMessageContent() {

        inflater = getLayoutInflater();
        addSendTextLabel = inflater.inflate(R.layout.labeltext, null);
        linearLayout = findViewById(R.id.messageScrollViewMaster);
        linearLayout.addView(addSendTextLabel, 0);

        SmsManager SmsManager = android.telephony.SmsManager.getDefault();
        SmsManager.sendTextMessage(bundleRecIntentString, null, "Message: " + messageTextString, null, null);

    }

    // _____________________________________________________________________________________________


    // Method will be responsible to create instances of received messages and will recieve them from
    // Firebase database.
    private void setRecMessageContent() {

        inflater = getLayoutInflater();
        addSendTextLabel = inflater.inflate(R.layout.labeltext, null);
        linearLayout = findViewById(R.id.messageScrollViewMaster);
        linearLayout.addView(addSendTextLabel, 0);

    }

    private void recContactInformation(){

        whosMessaging = findViewById(R.id.whoseMessaging);
        intentFrom = getIntent();
        dataPass = intentFrom.getExtras();
        bundleRecIntentString = dataPass.getString("contactIDPass", "Whoops");
        whosMessaging.setText(bundleRecIntentString);

    }

}
