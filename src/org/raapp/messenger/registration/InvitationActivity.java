package org.raapp.messenger.registration;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.raapp.messenger.R;
import org.raapp.messenger.client.Client;
import org.raapp.messenger.util.CompanyRolePreferenceUtil;
import org.raapp.messenger.client.datamodel.Company;
import org.raapp.messenger.client.datamodel.CompanyRoleDTO;
import org.raapp.messenger.client.datamodel.Responses.ResponseInvitationCode;
import org.raapp.messenger.database.Address;
import org.raapp.messenger.database.DatabaseFactory;
import org.raapp.messenger.database.MessagingDatabase;
import org.raapp.messenger.database.SmsDatabase;
import org.raapp.messenger.recipients.Recipient;
import org.raapp.messenger.sms.IncomingEncryptedMessage;
import org.raapp.messenger.sms.IncomingTextMessage;
import org.whispersystems.libsignal.util.guava.Optional;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvitationActivity extends AppCompatActivity {

    public static final int REGISTER_MODE_INVITE = 0;
    public static final int REGISTER_MODE_CODE = 1;

    // Components
    private View mInviteLL, mCodeLL;
    private Button mContinueBtn, mAcceptBtn;
    private EditText mCodeET;
    private TextView mSimpleTopbarTV;

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
        mSimpleTopbarTV = findViewById(R.id.tv_simpleTopbar);


        switch (mAccessMode) {
            case REGISTER_MODE_CODE:
                mInviteLL.setVisibility(View.GONE);
                mCodeLL.setVisibility(View.VISIBLE);
                mContinueBtn.setOnClickListener(view -> {
                    String code = mCodeET.getText().toString();

                    Client.buildBase(InvitationActivity.this);
                    Client.acceptInvitation(new Callback<ResponseInvitationCode>() {
                        @Override
                        public void onResponse(Call<ResponseInvitationCode> call, Response<ResponseInvitationCode> response) {
                            if (response.isSuccessful()) {
                                ResponseInvitationCode resp = response.body();

                                if (resp != null && resp.getSuccess()) {
                                    Company company = resp.getCompany();
                                    CompanyRoleDTO companyRoleDTO = new CompanyRoleDTO(company.getCompanyNumber().toString(), resp.getRole().trim());
                                    CompanyRolePreferenceUtil.addCompanyRole(InvitationActivity.this, companyRoleDTO);
                                    createCompanyChat(company.getCompanyNumber().toString(), company.getName());
                                } else{
                                    String errorMsg = resp !=null ? resp.getError() : "Unexpected error";
                                    Toast.makeText(InvitationActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                                }
                            } else{
                                Toast.makeText(InvitationActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseInvitationCode> call, Throwable t) {
                            Toast.makeText(InvitationActivity.this, "HTTP error", Toast.LENGTH_SHORT).show();
                        }
                    }, code);
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
                mSimpleTopbarTV.setText(R.string.app_name);
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
                mSimpleTopbarTV.setText(R.string.office_app_welcome);
                break;
        }

    }

    private void createCompanyChat(String companyId, String companyName) {
        // Build Recipient with company info
        Recipient recipient = Recipient.from(this, Address.fromExternal(this, companyId), true);
        DatabaseFactory.getRecipientDatabase(this).setSystemDisplayName(recipient, companyName);
        DatabaseFactory.getRecipientDatabase(this).setOfficeApp(recipient, true);

        // Insert or retrieve company chat
        long existingThread = DatabaseFactory.getThreadDatabase(this).getThreadIdFor(recipient);

        // Insert welcoming message
        String welcomeText = getResources().getString(R.string.welcome_to)+" "+companyName;
        SmsDatabase database  =  DatabaseFactory.getSmsDatabase(this);
        IncomingTextMessage textMessage = new IncomingTextMessage(Address.fromExternal(this, recipient.getAddress().toString()),
                0,
                System.currentTimeMillis(), welcomeText,
                Optional.absent(),
                0,
                false);
        textMessage = new IncomingEncryptedMessage(textMessage, welcomeText);
        Optional<MessagingDatabase.InsertResult> insertResult = database.insertMessageInbox(textMessage);

        finish();
    }

    private void goToEULA() {
        Intent i = new Intent(this, TermOfServiceActivity.class);
        startActivity(i);
        finish();
    }
}
