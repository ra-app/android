package org.raapp.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import org.raapp.messenger.registration.TermOfServiceActivity;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> start(), 1000);
    }

    private void start(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences(OfficeAppConstants.RA_PREFERENCES, 0);
        boolean isEULAOK = pref.getBoolean(OfficeAppConstants.PREFERENCE_EULA_ACCEPTATION, false);
        if(isEULAOK){
            goToChatList();
        }else {
            goToEULA();
        }
    }

    private void goToChatList(){
        Intent i  = new Intent(this, ConversationListActivity.class);
        startActivity(i);
        finish();
    }

    private void goToEULA(){
        Intent i  = new Intent(this, TermOfServiceActivity.class);
        startActivity(i);
        finish();
    }
}
