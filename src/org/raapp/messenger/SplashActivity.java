package org.raapp.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import org.raapp.messenger.registration.InvitationActivity;

import static org.raapp.messenger.registration.TermOfServiceActivity.PREFERENCE_EULA_ACCEPTATION;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> start(), 1000);
    }

    private void start(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("ra-preferences", 0);
        boolean isEULAOK = pref.getBoolean(PREFERENCE_EULA_ACCEPTATION, false);
        if(isEULAOK){
            goToChatList();
        }else {
            goToInvitationScreen();
        }
    }

    private void goToChatList(){
        Intent i  = new Intent(this, ConversationListActivity.class);
        startActivity(i);
        finish();
    }

    private void goToInvitationScreen(){
        Intent i  = new Intent(this, InvitationActivity.class);
        startActivity(i);
        finish();
    }
}
