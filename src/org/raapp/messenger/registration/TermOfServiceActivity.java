package org.raapp.messenger.registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.Toast;

import org.raapp.messenger.ConversationListActivity;
import org.raapp.messenger.R;

import androidx.appcompat.app.AppCompatActivity;

public class TermOfServiceActivity extends AppCompatActivity {

    private Button mContinueButton;
    private CheckBox mAcceptCheck;
    private ScrollView mTermsSV;

    private boolean mIsScrollFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_of_service);

        initComponents();
    }

    private void initComponents() {
        mTermsSV = findViewById(R.id.sv_terms);
        mContinueButton = findViewById(R.id.btn_continue);
        mAcceptCheck = findViewById(R.id.cb_accept);
        mAcceptCheck.setEnabled(false);

        mTermsSV.getViewTreeObserver().addOnScrollChangedListener(() -> {
            int maxScroll = mTermsSV.getChildAt(0).getBottom();
            int currentScroll = (mTermsSV.getHeight() + mTermsSV.getScrollY());
            if (maxScroll - currentScroll == 0) {
                mIsScrollFinished = true;
                mAcceptCheck.setEnabled(true);
                mAcceptCheck.setChecked(true);
            }
        });

        mAcceptCheck.setOnCheckedChangeListener((compoundButton, b) -> {
                mContinueButton.setEnabled(b);
        });

        mContinueButton.setOnClickListener(view -> {
            if(mIsScrollFinished && mAcceptCheck.isChecked()){
                goToWelcomeScreen();
            }else{
                Toast.makeText(this, "Accept Terms of Service to continue", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToWelcomeScreen(){
        Intent i = new Intent(this, ConversationListActivity.class);
        startActivity(i);
        finish();
    }
}
