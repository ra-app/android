package org.raapp.messenger.blackboard;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import org.raapp.messenger.R;
import org.raapp.messenger.conversation.ConversationTitleView;
import org.raapp.messenger.mms.GlideApp;
import org.raapp.messenger.mms.GlideRequests;
import org.raapp.messenger.recipients.Recipient;
import org.raapp.messenger.util.DynamicLanguage;
import org.raapp.messenger.util.DynamicNoActionBarTheme;

public class BlacboardActivity extends AppCompatActivity {


    public static final String ADDRESS_EXTRA = "address";

    private ConversationTitleView titleView;
    private GlideRequests glideRequests;
    private Recipient recipient;

    private final DynamicNoActionBarTheme dynamicTheme = new DynamicNoActionBarTheme();
    private final DynamicLanguage dynamicLanguage = new DynamicLanguage();

    private RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dynamicTheme.onCreate(this);
        dynamicLanguage.onCreate(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacboard);

        initComponents();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initComponents(){
        initializeActionBar();
        initViews();
        initializeResources();
    }

    protected void initializeActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar == null) throw new AssertionError();

        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowTitleEnabled(false);
    }

    private void initViews(){
        titleView = findViewById(R.id.conversation_title_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new GridLayoutManager(this,3);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void initializeResources() {
        recipient = Recipient.from(this, getIntent().getParcelableExtra(ADDRESS_EXTRA), true);
        glideRequests = GlideApp.with(this);
        titleView.setTitle(glideRequests, recipient);


        // Initialize a new instance of RecyclerView Adapter instance
        String[] notes = {
                "Aardvark",
                "Albatross",
                "Alligator",
                "Alpaca",
                "Ant",
                "Anteater",
                "Antelope",
                "Ape",
                "Armadillo",
                "Donkey",
                "Baboon",
                "Badger",
                "Barracuda",
                "Bear",
                "Beaver",
                "Bee"
        };
        mAdapter = new BlackboardAdapter(this,notes);
        // Set the adapter for RecyclerView
        mRecyclerView.setAdapter(mAdapter);
    }
}
