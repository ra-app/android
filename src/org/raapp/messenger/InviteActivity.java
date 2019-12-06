package org.raapp.messenger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.AnimRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.raapp.messenger.components.ContactFilterToolbar;
import org.raapp.messenger.components.ContactFilterToolbar.OnFilterChangedListener;
import org.raapp.messenger.components.PushRecipientsPanel;
import org.raapp.messenger.contacts.ContactsCursorLoader.DisplayMode;
import org.raapp.messenger.database.Address;
import org.raapp.messenger.database.DatabaseFactory;
import org.raapp.messenger.database.RecipientDatabase;
import org.raapp.messenger.recipients.Recipient;
import org.raapp.messenger.sms.MessageSender;
import org.raapp.messenger.sms.OutgoingTextMessage;
import org.raapp.messenger.util.DynamicLanguage;
import org.raapp.messenger.util.DynamicTheme;
import org.raapp.messenger.util.SelectedRecipientsAdapter;
import org.raapp.messenger.util.TextSecurePreferences;
import org.raapp.messenger.util.ViewUtil;
import org.raapp.messenger.util.concurrent.ListenableFuture.Listener;
import org.raapp.messenger.util.task.ProgressDialogAsyncTask;
import org.whispersystems.libsignal.util.guava.Optional;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class InviteActivity extends PassphraseRequiredActionBarActivity implements ContactSelectionListFragment.OnContactSelectedListener, SelectedRecipientsAdapter.OnRecipientDeletedListener,
        PushRecipientsPanel.RecipientsPanelChangedListener {

  public static final String INVITE_MODE = "INVITE_MODE";
  public static final String COMPANY_TO_INVITE = "COMPANY_TO_INVITE";

  public static final int INVITE_MODE_COMPANY_USERS = 0;
  public static final int INVITE_MODE_COMPANY_ADMINS = 1;

  private ContactSelectionListFragment contactsFragment;
  private EditText                     inviteText;
  private ViewGroup                    smsSendFrame;
  private Animation                    slideInAnimation;
  private Animation                    slideOutAnimation;
  private ListView lv;

  private int invitationMode;
  private String companyToInvite;

  private final DynamicTheme dynamicTheme    = new DynamicTheme();
  private final DynamicLanguage dynamicLanguage = new DynamicLanguage();

  @NonNull private Optional<InviteActivity.GroupData> groupToUpdate = Optional.absent();

  @Override
  protected void onPreCreate() {
    dynamicTheme.onCreate(this);
    dynamicLanguage.onCreate(this);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState, boolean ready) {
    getIntent().putExtra(ContactSelectionListFragment.DISPLAY_MODE, DisplayMode.FLAG_ALL);
    getIntent().putExtra(ContactSelectionListFragment.MULTI_SELECT, true);
    getIntent().putExtra(ContactSelectionListFragment.REFRESHABLE, false);

    // Get intent params
    invitationMode = getIntent().getIntExtra(INVITE_MODE, INVITE_MODE_COMPANY_USERS);
    companyToInvite = getIntent().getStringExtra(COMPANY_TO_INVITE);

    setContentView(R.layout.invite_activity);

    initToolbar();

    initializeResources();
  }

  @Override
  public void onResume() {
    super.onResume();
    dynamicTheme.onResume(this);
    dynamicLanguage.onResume(this);
    invalidateOptionsMenu();
  }

  private void initToolbar(){
    // Init toolbar
    int titleID = invitationMode == INVITE_MODE_COMPANY_USERS ? R.string.users_invitation : invitationMode == INVITE_MODE_COMPANY_ADMINS ? R.string.admins_invitation : R.string.users_invitation;
    assert getSupportActionBar() != null;
    getSupportActionBar().setTitle(titleID);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_white);

  }

  private void initializeResources() {
    slideInAnimation  = loadAnimation(R.anim.slide_from_bottom);
    slideOutAnimation = loadAnimation(R.anim.slide_to_bottom);
    lv           = ViewUtil.findById(this, R.id.selected_contacts_list);

    View                 shareButton     = ViewUtil.findById(this, R.id.share_button);
    View                 smsButton       = ViewUtil.findById(this, R.id.contacts_button);
    ContactFilterToolbar contactFilter   = ViewUtil.findById(this, R.id.contact_filter);

    inviteText        = ViewUtil.findById(this, R.id.invite_text);
    smsSendFrame      = ViewUtil.findById(this, R.id.sms_send_frame);
    contactsFragment  = (ContactSelectionListFragment)getSupportFragmentManager().findFragmentById(R.id.contact_selection_list_fragment);

    PushRecipientsPanel recipientsPanel = ViewUtil.findById(this, R.id.recipients);
    recipientsPanel.setPanelChangeListener(this);

    inviteText.setText(getString(R.string.InviteActivity_lets_switch_to_signal, getString(R.string.install_url)));
    updateSmsButtonText();

    contactsFragment.setOnContactSelectedListener(this);
    shareButton.setOnClickListener(new ShareClickListener());
    smsButton.setOnClickListener(new SmsClickListener());
    contactFilter.setOnFilterChangedListener(new ContactFilterChangedListener());
    contactFilter.setNavigationIcon(R.drawable.ic_search_blue);

    SelectedRecipientsAdapter adapter = new SelectedRecipientsAdapter(this);
    adapter.setOnRecipientDeletedListener(this);
    lv.setAdapter(adapter);
  }

  private SelectedRecipientsAdapter getAdapter() {
    return (SelectedRecipientsAdapter)lv.getAdapter();
  }


  private Animation loadAnimation(@AnimRes int animResId) {
    final Animation animation = AnimationUtils.loadAnimation(this, animResId);
    animation.setInterpolator(new FastOutSlowInInterpolator());
    return animation;
  }

  @Override
  public void onContactSelected(String number) {
    updateSmsButtonText();
  }

  @Override
  public void onContactDeselected(String number) {
    updateSmsButtonText();
  }

  private void sendSmsInvites() {
    new SendSmsInvitesAsyncTask(this, inviteText.getText().toString())
        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                           contactsFragment.getSelectedContacts()
                                           .toArray(new String[contactsFragment.getSelectedContacts().size()]));
  }

  private void updateSmsButtonText() {
  }

  @Override public void onBackPressed() {
    if (smsSendFrame.getVisibility() == View.VISIBLE) {
      cancelSmsSelection();
    } else {
      super.onBackPressed();
    }
  }

  private void cancelSmsSelection() {
    contactsFragment.reset();
    updateSmsButtonText();
    ViewUtil.animateOut(smsSendFrame, slideOutAnimation, View.GONE).addListener(new Listener<Boolean>() {
      @Override
      public void onSuccess(Boolean result) {
        initToolbar();
        invalidateOptionsMenu();
      }

      @Override
      public void onFailure(ExecutionException e) {}
    });
  }

  @Override
  public void onRecipientDeleted(Recipient recipient) {
    getAdapter().remove(recipient);
  }

  @Override
  public void onRecipientsPanelUpdate(List<Recipient> recipients) {
    if (recipients != null && !recipients.isEmpty()) addSelectedContacts(recipients);
  }

  private void addSelectedContacts(@NonNull Collection<Recipient> recipients) {
    addSelectedContacts(recipients.toArray(new Recipient[recipients.size()]));
  }

  private void addSelectedContacts(@NonNull Recipient... recipients) {
    new InviteActivity.AddMembersTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, recipients);
  }

  private class ShareClickListener implements OnClickListener {
    @Override
    public void onClick(View v) {

        // TODO: GET SELECTED CONTACTS TO CREATE INVITATIONS

        // TODO: CREATE INVITATIONS TO COMPANY : this.companyToInvite

        // TODO: SEND ALL INVITATIONS TO COMPANY : this.companyToInvite with selected contacts

        if(invitationMode == INVITE_MODE_COMPANY_USERS){
          Toast.makeText(InviteActivity.this, "TODO: CREATE USER INVITE AND CALL SEND INVITATION API", Toast.LENGTH_LONG).show();
        }else if(invitationMode == INVITE_MODE_COMPANY_ADMINS){
          Toast.makeText(InviteActivity.this, "TODO: CREATE ADMIN INVITE AND CALL SEND INVITATION API", Toast.LENGTH_LONG).show();
        }

    }
  }

  private class SmsClickListener implements OnClickListener {
    @Override
    public void onClick(View v) {
      getSupportActionBar().setTitle(R.string.kontakte_auswahlen);
      ViewUtil.animateIn(smsSendFrame, slideInAnimation);
      invalidateOptionsMenu();
    }
  }

  private class SmsCancelClickListener implements OnClickListener {
    @Override
    public void onClick(View v) {
      cancelSmsSelection();
    }
  }

  private void clickConfirmContactsAdd() {
    for (String number : contactsFragment.getSelectedContacts()) {
      Recipient recipient      = Recipient.from(InviteActivity.this, Address.fromExternal(InviteActivity.this, number), false);
      getAdapter().add(recipient, false);
    }
    cancelSmsSelection();
  }

  private class SmsSendClickListener implements OnClickListener {
    @Override
    public void onClick(View v) {
    }
  }

  private class ContactFilterChangedListener implements OnFilterChangedListener {
    @Override
    public void onFilterChanged(String filter) {
      contactsFragment.setQueryFilter(filter);
    }
  }

  @SuppressLint("StaticFieldLeak")
  private class SendSmsInvitesAsyncTask extends ProgressDialogAsyncTask<String,Void,Void> {
    private final String message;

    SendSmsInvitesAsyncTask(Context context, String message) {
      super(context, R.string.InviteActivity_sending, R.string.InviteActivity_sending);
      this.message = message;
    }

    @Override
    protected Void doInBackground(String... numbers) {
      final Context context = getContext();
      if (context == null) return null;

      for (String number : numbers) {
        Recipient recipient      = Recipient.from(context, Address.fromExternal(context, number), false);
        int       subscriptionId = recipient.getDefaultSubscriptionId().or(-1);

        MessageSender.send(context, new OutgoingTextMessage(recipient, message, subscriptionId), -1L, true, null);

        if (recipient.getContactUri() != null) {
          DatabaseFactory.getRecipientDatabase(context).setSeenInviteReminder(recipient, true);
        }
      }

      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
      final Context context = getContext();
      if (context == null) return;

      ViewUtil.animateOut(smsSendFrame, slideOutAnimation, View.GONE).addListener(new Listener<Boolean>() {
        @Override
        public void onSuccess(Boolean result) {
          getSupportActionBar().setTitle(R.string.office_app_invite_title);
          contactsFragment.reset();
          invalidateOptionsMenu();
        }

        @Override
        public void onFailure(ExecutionException e) {}
      });
      Toast.makeText(context, R.string.InviteActivity_invitations_sent, Toast.LENGTH_LONG).show();
    }
  }


  private static boolean isActiveInDirectory(Recipient recipient) {
    return recipient.resolve().getRegistered() == RecipientDatabase.RegisteredState.REGISTERED;
  }

  private static class AddMembersTask extends AsyncTask<Recipient,Void,List<InviteActivity.AddMembersTask.Result>> {
    static class Result {
      Optional<Recipient> recipient;
      boolean             isPush;
      String              reason;

      public Result(@Nullable Recipient recipient, boolean isPush, @Nullable String reason) {
        this.recipient = Optional.fromNullable(recipient);
        this.isPush    = isPush;
        this.reason    = reason;
      }
    }

    private InviteActivity activity;
    private boolean             failIfNotPush;

    public AddMembersTask(@NonNull InviteActivity activity) {
      this.activity      = activity;
      this.failIfNotPush = activity.groupToUpdate.isPresent();
    }

    @Override
    protected List<InviteActivity.AddMembersTask.Result> doInBackground(Recipient... recipients) {
      final List<InviteActivity.AddMembersTask.Result> results = new LinkedList<>();

      for (Recipient recipient : recipients) {
        boolean isPush = isActiveInDirectory(recipient);

        if (failIfNotPush && !isPush) {
          results.add(new InviteActivity.AddMembersTask.Result(null, false, activity.getString(R.string.GroupCreateActivity_cannot_add_non_push_to_existing_group,
                  recipient.toShortString())));
        } else if (TextUtils.equals(TextSecurePreferences.getLocalNumber(activity), recipient.getAddress().serialize())) {
          results.add(new InviteActivity.AddMembersTask.Result(null, false, activity.getString(R.string.GroupCreateActivity_youre_already_in_the_group)));
        } else {
          results.add(new InviteActivity.AddMembersTask.Result(recipient, isPush, null));
        }
      }
      return results;
    }

    @Override
    protected void onPostExecute(List<InviteActivity.AddMembersTask.Result> results) {
      if (activity.isFinishing()) return;

      for (InviteActivity.AddMembersTask.Result result : results) {
        if (result.recipient.isPresent()) {
          activity.getAdapter().add(result.recipient.get(), result.isPush);
        } else {
          Toast.makeText(activity, result.reason, Toast.LENGTH_SHORT).show();
        }
      }
    }
  }

  private static class GroupData {
    String         id;
    Set<Recipient> recipients;
    Bitmap avatarBmp;
    byte[]         avatarBytes;
    String         name;

    public GroupData(String id, Set<Recipient> recipients, Bitmap avatarBmp, byte[] avatarBytes, String name) {
      this.id          = id;
      this.recipients  = recipients;
      this.avatarBmp   = avatarBmp;
      this.avatarBytes = avatarBytes;
      this.name        = name;
    }
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    MenuInflater inflater = this.getMenuInflater();
    menu.clear();

    inflater.inflate(R.menu.group_create, menu);
    MenuItem item = menu.findItem(R.id.menu_create_group);
    item.setVisible(smsSendFrame.getVisibility() != View.GONE);
    super.onPrepareOptionsMenu(menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    super.onOptionsItemSelected(item);
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        return true;
      case R.id.menu_create_group:
        clickConfirmContactsAdd();
        return true;
    }

    return false;
  }
}
