package org.raapp.messenger.blackboard;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.raapp.messenger.R;
import org.raapp.messenger.client.Client;
import org.raapp.messenger.client.datamodel.Note;
import org.raapp.messenger.client.datamodel.Responses.ResponseBlackboardList;
import org.raapp.messenger.conversation.ConversationTitleView;
import org.raapp.messenger.mms.GlideApp;
import org.raapp.messenger.mms.GlideRequests;
import org.raapp.messenger.recipients.Recipient;
import org.raapp.messenger.registration.InvitationActivity;
import org.raapp.messenger.util.DynamicLanguage;
import org.raapp.messenger.util.DynamicNoActionBarTheme;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlacboardActivity extends AppCompatActivity implements BlackboardInterface{


    public static final String ADDRESS_EXTRA = "address";

    private ConversationTitleView titleView;
    private GlideRequests glideRequests;
    private Recipient recipient;

    private final DynamicNoActionBarTheme dynamicTheme = new DynamicNoActionBarTheme();
    private final DynamicLanguage dynamicLanguage = new DynamicLanguage();

    private RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View mNoteDetailView;
    private ProgressBar progressBar;


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
        mNoteDetailView = findViewById(R.id.rl_detail_pin);
        progressBar = findViewById(R.id.progress_bar);

        showNoteList();
    }

    private void initializeResources() {
        recipient = Recipient.from(this, getIntent().getParcelableExtra(ADDRESS_EXTRA), true);
        glideRequests = GlideApp.with(this);
        titleView.setTitle(glideRequests, recipient);

        Client.buildBase(this);
        Client.getBlackboard(new Callback<ResponseBlackboardList>() {
            @Override
            public void onResponse(Call<ResponseBlackboardList> call, Response<ResponseBlackboardList> response) {
                if (response.isSuccessful()) {
                    ResponseBlackboardList resp = response.body();
                    if (resp != null && resp.getSuccess()) {
                        List<Note> notes = resp.getNote();
                        if (notes != null) {
                            mAdapter = new BlackboardAdapter(BlacboardActivity.this, notes);
                            mRecyclerView.setAdapter(mAdapter);
                        }
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ResponseBlackboardList> call, Throwable t) {
                Toast.makeText(BlacboardActivity.this, "Error retrieving notes", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }, recipient.getAddress().toString());
    }

    private void showNoteDetail(){
        mRecyclerView.setVisibility(View.GONE);
        mNoteDetailView.setVisibility(View.VISIBLE);
    }

    private void showNoteList(){
        mRecyclerView.setVisibility(View.VISIBLE);
        mNoteDetailView.setVisibility(View.GONE);
    }

    @Override
    public void onNoteClick(Object object) {
        TextView noteTitle = mNoteDetailView.findViewById(R.id.note_title);
        TextView noteBody = mNoteDetailView.findViewById(R.id.note_body);
        TextView noteDate = mNoteDetailView.findViewById(R.id.note_date);

        Note note = (Note) object;
        noteTitle.setText(note.getTitle());
        noteBody.setText(note.getContent());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date myDate = null;
        try {
            myDate = dateFormat.parse(note.getTsCreated());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd / HH:mm");
        String finalDate = timeFormat.format(myDate);

        noteDate.setText(finalDate);

        showNoteDetail();
    }

    @Override
    public void onBackPressed() {
        if(mNoteDetailView.getVisibility() == View.VISIBLE){
            showNoteList();
        }else{
            super.onBackPressed();
        }
    }
}
