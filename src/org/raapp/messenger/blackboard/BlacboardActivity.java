package org.raapp.messenger.blackboard;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.raapp.messenger.R;
import org.raapp.messenger.client.Client;
import org.raapp.messenger.client.CompanyRolePreferenceUtil;
import org.raapp.messenger.client.datamodel.CompanyRoleDTO;
import org.raapp.messenger.client.datamodel.Note;
import org.raapp.messenger.client.datamodel.Responses.ResponseBlackboardList;
import org.raapp.messenger.client.datamodel.Responses.ResponseNote;
import org.raapp.messenger.conversation.ConversationTitleView;
import org.raapp.messenger.logging.Log;
import org.raapp.messenger.mms.GlideApp;
import org.raapp.messenger.mms.GlideRequests;
import org.raapp.messenger.recipients.Recipient;
import org.raapp.messenger.util.DynamicLanguage;
import org.raapp.messenger.util.DynamicNoActionBarTheme;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    private Note selectedNote;
    private EditText noteTitle;
    private EditText noteBody;
    private TextView noteDate;
    private ImageView editSaveIV;
    private boolean isNoteUpdated = false;


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
        initRole();
    }

    private void initRole() {
        String role = "";
        List<CompanyRoleDTO> companyRoleDTOS = CompanyRolePreferenceUtil.getCompanyRolList(this);
        for (CompanyRoleDTO companyRoleDTO: companyRoleDTOS) {
            if (companyRoleDTO.getCompanyId().equals(recipient.getAddress().toString())) {
                role = companyRoleDTO.getRole();
            }
        }
        Log.i("Role", "" + role);

        if ("admin".equalsIgnoreCase(role)) {
            editSaveIV.setVisibility(View.VISIBLE);
            editSaveIV.setOnClickListener(editNoteListener);
        }
    }

    private View.OnClickListener editNoteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!noteBody.isClickable()) {
                toEditText(noteTitle);
                toEditText(noteBody);
                editSaveIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_save_white_24dp));
                editSaveIV.setOnClickListener(saveNoteListener);
            } else {
                toTextView(noteTitle);
                toTextView(noteBody);
                editSaveIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit_black_24dp));
            }
        }
    };

    private void toEditMode() {
        editSaveIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit_black_24dp));
        editSaveIV.setOnClickListener(editNoteListener);
        toTextView(noteTitle);
        toTextView(noteBody);
    }

    private View.OnClickListener saveNoteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            selectedNote.setTitle(noteTitle.getText().toString());
            selectedNote.setContent(noteBody.getText().toString());

            progressBar.setVisibility(View.VISIBLE);
            Client.buildBase(BlacboardActivity.this);
            Client.updateBlackboardNote(new Callback<ResponseNote>() {
                @Override
                public void onResponse(Call<ResponseNote> call, Response<ResponseNote> response) {
                    if (response.isSuccessful()) {
                        ResponseNote resp = response.body();

                        if (resp != null && resp.getSuccess()) {
                            Note note = resp.getNote();
                            isNoteUpdated = true;

                            progressBar.setVisibility(View.GONE);
                            editSaveIV.setOnClickListener(editNoteListener);
                            editSaveIV.performClick();
                        } else{
                            String errorMsg = resp !=null ? resp.getError() : "Unexpected error";
                            Toast.makeText(BlacboardActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    } else{
                        Toast.makeText(BlacboardActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<ResponseNote> call, Throwable t) {
                    Toast.makeText(BlacboardActivity.this, "HTTP error", Toast.LENGTH_SHORT).show();
                }
            }, selectedNote, selectedNote.getCompanyId().toString());
        }
    };

    private void toTextView(EditText editText) {
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        editText.setClickable(false);
        editText.setBackground(null);
        editText.setTextSize(16);
        editText.setTextColor(getResources().getColor(R.color.ra_text));
        editText.setGravity(Gravity.TOP | Gravity.START);
    }

    private void toEditText(EditText textView) {
        textView.setFocusable(true);
        textView.setFocusableInTouchMode(true);
        textView.setClickable(true);

        TypedArray a = getTheme().obtainStyledAttributes(R.style.RaApp_MainTheme, new int[] {android.R.attr.editTextBackground});
        int attributeResourceId = a.getResourceId(0, 0);
        Drawable drawable = getResources().getDrawable(attributeResourceId);
        a.recycle();

        textView.setBackground(drawable);
        textView.setTextSize(18);
        textView.setTextColor(getResources().getColor(R.color.primary));
        textView.setGravity(Gravity.TOP | Gravity.START);
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

        noteTitle = mNoteDetailView.findViewById(R.id.note_title);
        noteBody = mNoteDetailView.findViewById(R.id.note_body);
        noteDate = mNoteDetailView.findViewById(R.id.note_date);
        editSaveIV = mNoteDetailView.findViewById(R.id.iv_edit_save);

        toTextView(noteTitle);
        toTextView(noteBody);

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

        selectedNote = (Note) object;
        noteTitle.setText(selectedNote.getTitle());
        noteBody.setText(selectedNote.getContent());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date myDate = null;
        try {
            myDate = dateFormat.parse(selectedNote.getTsCreated());

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
        toEditMode();
        hideKeyboard();
        if(mNoteDetailView.getVisibility() == View.VISIBLE){
            if (isNoteUpdated) {
                isNoteUpdated = false;
                initializeResources();
            }
            showNoteList();
        }else{
            super.onBackPressed();
        }
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
