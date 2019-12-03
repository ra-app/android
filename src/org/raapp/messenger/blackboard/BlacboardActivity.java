package org.raapp.messenger.blackboard;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.raapp.messenger.R;
import org.raapp.messenger.client.Client;
import org.raapp.messenger.client.datamodel.Note;
import org.raapp.messenger.client.datamodel.Responses.ResponseBlackboardList;
import org.raapp.messenger.client.datamodel.Responses.ResponseNote;
import org.raapp.messenger.conversation.ConversationTitleView;
import org.raapp.messenger.mms.GlideApp;
import org.raapp.messenger.mms.GlideRequests;
import org.raapp.messenger.recipients.Recipient;
import org.raapp.messenger.util.DynamicLanguage;
import org.raapp.messenger.util.DynamicNoActionBarTheme;
import org.raapp.messenger.util.RoleUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlacboardActivity extends AppCompatActivity implements BlackboardInterface{


    public static final String ADDRESS_EXTRA = "address";

    public static final String NOTE_TYPE_NORMAL = "normal";
    public static final String NOTE_TYPE_CALENDAR = "calender";

    private ConversationTitleView titleView;
    private GlideRequests glideRequests;
    private Recipient recipient;

    private final DynamicNoActionBarTheme dynamicTheme = new DynamicNoActionBarTheme();
    private final DynamicLanguage dynamicLanguage = new DynamicLanguage();

    private RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View mNoteDetailSV;
    private RadioGroup mNoteTypeRadioGroup;
    private RadioButton mStandardTypeRB, mCalendarTypeRB;
    private ProgressBar progressBar;

    private Note selectedNote;
    private View mNoteDetailViewItem;
    private EditText noteTitle;
    private EditText noteBody;
    private TextView noteDate;
    private boolean isNoteUpdated = false;
    private Menu menu;
    private List<Object> notes;
    private List<Object> emptyNotes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dynamicTheme.onCreate(this);
        dynamicLanguage.onCreate(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacboard);

        initComponents();
    }

    private void initComponents(){
        initializeActionBar();
        initViews();
        initializeResources();
    }

    private void initRole(boolean enterEditMode) {
        if (RoleUtil.isAdminInCompany(this, recipient.getAddress().toString())) {
            menu.findItem(R.id.edit_note).setVisible(true);
            if(enterEditMode){
                editNoteClick();
            }
        }
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
        mNoteDetailSV = findViewById(R.id.rl_detail_pin);
        mNoteTypeRadioGroup = findViewById(R.id.rg_note_type);
        mStandardTypeRB = findViewById(R.id.rb_standard);
        mCalendarTypeRB = findViewById(R.id.rb_calendar);

        progressBar = findViewById(R.id.progress_bar);

        mNoteDetailViewItem = mNoteDetailSV.findViewById(R.id.rl_note_item_detail);
        noteTitle = mNoteDetailSV.findViewById(R.id.note_title);
        noteBody = mNoteDetailSV.findViewById(R.id.note_body);
        noteDate = mNoteDetailSV.findViewById(R.id.note_date);

        notes = new ArrayList<>();
        emptyNotes = new ArrayList<>();

        toTextView(noteTitle);
        toTextView(noteBody);

        showNoteList();
    }

    private void initializeResources() {
        recipient = Recipient.from(this, getIntent().getParcelableExtra(ADDRESS_EXTRA), true);
        glideRequests = GlideApp.with(this);
        titleView.setTitle(glideRequests, recipient);

        progressBar.setVisibility(View.VISIBLE);
        Client.buildBase(this);
        Client.getBlackboard(new Callback<ResponseBlackboardList>() {
            @Override
            public void onResponse(Call<ResponseBlackboardList> call, Response<ResponseBlackboardList> response) {
                if (response.isSuccessful()) {
                    ResponseBlackboardList resp = response.body();
                    if (resp != null && resp.getSuccess()) {
                        notes.clear();
                        notes.addAll(resp.getNote());
                        emptyNotes.clear();
                        for (Object note : notes) {
                            if (((Note)note).getTitle().isEmpty() && ((Note)note).getContent().isEmpty()) {
                                emptyNotes.add(note);
                            }
                        }
                        notes.removeAll(emptyNotes);

                        if (RoleUtil.isAdminInCompany(BlacboardActivity.this, recipient.getAddress().toString()) &&
                            emptyNotes.size() > 0) {
                            notes.add(new Object());
                        }

                        if (notes != null) {
                            mAdapter = new BlackboardAdapter(BlacboardActivity.this, new ArrayList<>(notes));
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

        mStandardTypeRB.setOnClickListener(view -> updateUINoteType());
        mCalendarTypeRB.setOnClickListener(view -> updateUINoteType());
    }

    private void showNoteDetail(){
        mRecyclerView.setVisibility(View.GONE);
        mNoteDetailSV.setVisibility(View.VISIBLE);
    }

    private void showNoteList(){
        mRecyclerView.setVisibility(View.VISIBLE);
        mNoteDetailSV.setVisibility(View.GONE);
        mNoteTypeRadioGroup.setVisibility(View.GONE);
    }

    private void bindSelectedNote(@Nullable String type){
        ImageView triangle = mNoteDetailViewItem.findViewById(R.id.triangle);
        mNoteDetailViewItem.setBackgroundColor(getResources().getColor(R.color.ra_yellow_note));
        triangle.setColorFilter(ContextCompat.getColor(this, R.color.ra_yellow_dark_note), android.graphics.PorterDuff.Mode.SRC_IN);
        String  t = type !=null ? type : selectedNote.getNoteType();
        if( t != null && NOTE_TYPE_CALENDAR.equals(t)){
            mNoteDetailViewItem.setBackgroundColor(getResources().getColor(R.color.ra_blue_note));
            triangle.setColorFilter(ContextCompat.getColor(this, R.color.ra_blue_dark_note), android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }

    private void updateUINoteType(){
        String type = mStandardTypeRB.isChecked() ? NOTE_TYPE_NORMAL : NOTE_TYPE_CALENDAR;
        bindSelectedNote(type);
    }

    @Override
    public void onNoteClick(Object object) {
        selectedNote = (Note) object;
        noteTitle.setText(selectedNote.getTitle());
        noteBody.setText(selectedNote.getContent());

        bindSelectedNote(null);

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
        initRole(selectedNote.isEmpty());
    }

    @Override
    public void onAddNoteClick() {
        if (emptyNotes.size() >  0) {
            selectedNote = (Note)emptyNotes.get(0);
            onNoteClick(selectedNote);
        }
    }

    private void editNoteClick() {
        toEditText(noteTitle);
        toEditText(noteBody);

        menu.findItem(R.id.edit_note).setVisible(false);
        menu.findItem(R.id.save_note).setVisible(true);
        mNoteTypeRadioGroup.setVisibility(View.VISIBLE);
        String type = selectedNote.getNoteType();
        if(type != null && NOTE_TYPE_NORMAL.equals(type)){
            mStandardTypeRB.setChecked(true);
        }else if(type != null && NOTE_TYPE_CALENDAR.equals(type)){
            mCalendarTypeRB.setChecked(true);
        }

    }

    private void saveNoteClick() {
        toViewMode();

        selectedNote.setTitle(noteTitle.getText().toString());
        selectedNote.setContent(noteBody.getText().toString());
        String type = mStandardTypeRB.isChecked() ? NOTE_TYPE_NORMAL : NOTE_TYPE_CALENDAR;
        selectedNote.setNoteType(type);

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
                        bindSelectedNote(null);
                        initializeResources();
                    } else{
                        String errorMsg = resp !=null ? resp.getError() : "Unexpected error";
                        Toast.makeText(BlacboardActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                } else{
                    Toast.makeText(BlacboardActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

                menu.findItem(R.id.edit_note).setVisible(true);
                menu.findItem(R.id.save_note).setVisible(false);
            }

            @Override
            public void onFailure(Call<ResponseNote> call, Throwable t) {
                Toast.makeText(BlacboardActivity.this, "HTTP error", Toast.LENGTH_SHORT).show();
            }
        }, selectedNote, selectedNote.getCompanyId().toString());
    }

    private void toViewMode() {
        hideKeyboard();
        toTextView(noteTitle);
        toTextView(noteBody);

    }

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
        textView.setGravity(Gravity.TOP | Gravity.START);
    }

    @Override
    public void onBackPressed() {
        toViewMode();
        if(mNoteDetailSV.getVisibility() == View.VISIBLE){
            if (isNoteUpdated) {
                isNoteUpdated = false;
                initializeResources();
            }
            menu.findItem(R.id.save_note).setVisible(false);
            menu.findItem(R.id.edit_note).setVisible(false);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.edit_note:
                editNoteClick();
                return true;
            case R.id.save_note:
                saveNoteClick();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        menu.clear();

        inflater.inflate(R.menu.blackboard_admin, menu);
        menu.findItem(R.id.save_note).setVisible(false);
        menu.findItem(R.id.edit_note).setVisible(false);

        this.menu = menu;
        super.onPrepareOptionsMenu(menu);
        return true;
    }
}
