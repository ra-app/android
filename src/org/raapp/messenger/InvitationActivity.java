package org.raapp.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.raapp.messenger.database.DatabaseContentProviders;
import org.raapp.messenger.registration.WelcomeActivity;

import androidx.appcompat.app.AppCompatActivity;

public class InvitationActivity extends AppCompatActivity {

    public static final int REGISTER_MODE_INVITE = 0;
    public static final int REGISTER_MODE_CODE = 1;

    // Components
    private View mInviteLL, mCodeLL;
    private Button mContinueBtn, mAcceptBtn;
    private EditText mCodeET;

    // Vars
    private int mAccessMode = REGISTER_MODE_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);

        //TODO: Get access mode (INVITATION or CODE)

        initComponents();
    }


    private void initComponents() {
        mCodeLL = findViewById(R.id.ll_code_layout);
        mInviteLL = findViewById(R.id.ll_invite_layout);
        mAcceptBtn = findViewById(R.id.btn_accept_invite);
        mContinueBtn = findViewById(R.id.btn_continue);
        mCodeET = findViewById(R.id.et_code);

        switch (mAccessMode) {
            case REGISTER_MODE_CODE:
                mInviteLL.setVisibility(View.GONE);
                mCodeLL.setVisibility(View.VISIBLE);
                mContinueBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //TODO: Verify code (server call?)
                        String code = mCodeET.getText().toString();
                        // verifyCode(code);
                        goToEULA();
                    }
                });
                mCodeET.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        mContinueBtn.setEnabled(!editable.toString().matches(""));
                    }
                });

                break;
            case REGISTER_MODE_INVITE:
                mInviteLL.setVisibility(View.VISIBLE);
                mCodeLL.setVisibility(View.GONE);
                mAcceptBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //TODO: Accept invite (server call?)
                        // Accept();

                        goToEULA();
                    }
                });
                break;
        }

    }

    private void goToEULA() {
        Intent i = new Intent(this, ConversationListActivity.class);
        startActivity(i);
        finish();
    }
}
