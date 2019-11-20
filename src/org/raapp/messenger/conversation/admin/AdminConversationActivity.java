package org.raapp.messenger.conversation.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.raapp.messenger.ConversationListActivity;
import org.raapp.messenger.ConversationListArchiveActivity;
import org.raapp.messenger.PassphraseRequiredActionBarActivity;
import org.raapp.messenger.R;
import org.raapp.messenger.RecipientPreferenceActivity;
import org.raapp.messenger.client.Client;
import org.raapp.messenger.client.datamodel.Request.RequestTicket;
import org.raapp.messenger.client.datamodel.Responses.ResponseTickets;
import org.raapp.messenger.client.datamodel.Ticket;
import org.raapp.messenger.conversation.ConversationTitleView;
import org.raapp.messenger.logging.Log;
import org.raapp.messenger.mms.GlideApp;
import org.raapp.messenger.mms.GlideRequests;
import org.raapp.messenger.recipients.Recipient;
import org.raapp.messenger.recipients.RecipientModifiedListener;
import org.raapp.messenger.util.DynamicLanguage;
import org.raapp.messenger.util.DynamicNoActionBarTheme;
import org.raapp.messenger.util.TextSecurePreferences;
import org.raapp.messenger.util.Util;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminConversationActivity extends PassphraseRequiredActionBarActivity implements RecipientModifiedListener {

    private static final String TAG = AdminConversationActivity.class.getSimpleName();

    private final DynamicNoActionBarTheme dynamicTheme = new DynamicNoActionBarTheme();
    private final DynamicLanguage dynamicLanguage = new DynamicLanguage();

    public static final String ADDRESS_EXTRA = "address";
    public static final String IS_ARCHIVED_EXTRA = "is_archived";

    private Recipient recipient;
    private boolean archived;
    private GlideRequests glideRequests;

    protected ConversationTitleView titleView;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AdminPageAdapter pageAdapter;

    @Override
    protected void onPreCreate() {
        dynamicTheme.onCreate(this);
        dynamicLanguage.onCreate(this);
    }

    @Override
    protected void onCreate(Bundle state, boolean ready) {
        Log.i(TAG, "onCreate()");
        setContentView(R.layout.activity_admin_conversation);

        initializeActionBar();
        initializeViews();
        initializeResources();
    }


    @Override
    protected void onResume() {
        super.onResume();
        dynamicTheme.onResume(this);
        dynamicLanguage.onResume(this);
        titleView.setTitle(glideRequests, recipient);
    }

    protected void initializeActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar == null) throw new AssertionError();

        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowTitleEnabled(false);
    }

    private void initializeViews() {
        titleView = findViewById(R.id.conversation_title_view);
        tabLayout = findViewById(R.id.tb_admin_conversation);
        viewPager = findViewById(R.id.vp_admin_conversation);

        titleView.setOnClickListener(v -> handleConversationSettings());
    }

    private void initializeResources() {
        if (recipient != null) recipient.removeListener(this);
        recipient = Recipient.from(this, getIntent().getParcelableExtra(ADDRESS_EXTRA), true);
        archived = getIntent().getBooleanExtra(IS_ARCHIVED_EXTRA, false);
        glideRequests = GlideApp.with(this);

        recipient.addListener(this);

        List<AdminConversationFragment> fragments = new ArrayList<>();
        fragments.add(AdminConversationFragment.newInstance(0, recipient.getAddress().toString()));
        fragments.add(AdminConversationFragment.newInstance(1, recipient.getAddress().toString()));
        fragments.add(AdminConversationFragment.newInstance(2, recipient.getAddress().toString()));

        pageAdapter = new AdminPageAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(pageAdapter);
        viewPager.setOffscreenPageLimit(fragments.size());
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onModified(final Recipient recipient) {
        Log.i(TAG, "onModified(" + recipient.getAddress().serialize() + ")");
        Util.runOnMain(() -> {
            Log.i(TAG, "onModifiedRun(): " + recipient.getRegistered());
            titleView.setTitle(glideRequests, recipient);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                handleReturnToConversationList();
                return true;
        }
        return false;
    }

    private void handleReturnToConversationList() {
        Intent intent = new Intent(this, (archived ? ConversationListArchiveActivity.class : ConversationListActivity.class));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void handleConversationSettings() {
        Intent intent = new Intent(this, RecipientPreferenceActivity.class);
        intent.putExtra(RecipientPreferenceActivity.ADDRESS_EXTRA, recipient.getAddress());
        intent.putExtra(RecipientPreferenceActivity.CAN_HAVE_SAFETY_NUMBER_EXTRA, !isSelfConversation());

        startActivitySceneTransition(intent, titleView.findViewById(R.id.contact_photo_image), "avatar");
    }

    private boolean isSelfConversation() {
        if (!TextSecurePreferences.isPushRegistered(this)) return false;
        if (recipient.isGroupRecipient()) return false;

        return Util.isOwnNumber(this, recipient.getAddress());
    }
}
