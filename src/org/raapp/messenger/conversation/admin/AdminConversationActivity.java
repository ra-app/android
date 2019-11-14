package org.raapp.messenger.conversation.admin;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.raapp.messenger.PassphraseRequiredActionBarActivity;
import org.raapp.messenger.R;
import org.raapp.messenger.conversation.ConversationTitleView;
import org.raapp.messenger.database.ThreadDatabase;
import org.raapp.messenger.logging.Log;
import org.raapp.messenger.mms.GlideApp;
import org.raapp.messenger.mms.GlideRequests;
import org.raapp.messenger.recipients.Recipient;
import org.raapp.messenger.recipients.RecipientModifiedListener;
import org.raapp.messenger.util.DynamicLanguage;
import org.raapp.messenger.util.DynamicNoActionBarTheme;
import org.raapp.messenger.util.Util;

public class AdminConversationActivity extends PassphraseRequiredActionBarActivity implements RecipientModifiedListener {

    private static final String TAG = AdminConversationActivity.class.getSimpleName();

    private final DynamicNoActionBarTheme dynamicTheme = new DynamicNoActionBarTheme();
    private final DynamicLanguage dynamicLanguage = new DynamicLanguage();

    public static final String ADDRESS_EXTRA = "address";

    private Recipient recipient;
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

        pageAdapter = new AdminPageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initializeResources() {
        if (recipient != null) recipient.removeListener(this);
        recipient = Recipient.from(this, getIntent().getParcelableExtra(ADDRESS_EXTRA), true);
        glideRequests = GlideApp.with(this);

        recipient.addListener(this);
    }

    @Override
    public void onModified(final Recipient recipient) {
        Log.i(TAG, "onModified(" + recipient.getAddress().serialize() + ")");
        Util.runOnMain(() -> {
            Log.i(TAG, "onModifiedRun(): " + recipient.getRegistered());
            titleView.setTitle(glideRequests, recipient);
        });
    }
}
