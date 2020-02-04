package org.raapp.messenger;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.core.view.ViewCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.CheckBoxPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.PhoneNumberUtils;

import org.raapp.messenger.components.SwitchPreferenceCompat;
import org.raapp.messenger.contacts.avatars.ContactPhoto;
import org.raapp.messenger.contacts.avatars.FallbackContactPhoto;
import org.raapp.messenger.contacts.avatars.ProfileContactPhoto;
import org.raapp.messenger.contacts.avatars.ResourceContactPhoto;
import org.raapp.messenger.conversation.ConversationActivity;
import org.raapp.messenger.database.GroupDatabase;
import org.raapp.messenger.jobs.RotateProfileKeyJob;
import org.raapp.messenger.logging.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.raapp.messenger.components.ThreadPhotoRailView;
import org.raapp.messenger.crypto.IdentityKeyParcelable;
import org.raapp.messenger.database.Address;
import org.raapp.messenger.database.DatabaseFactory;
import org.raapp.messenger.database.IdentityDatabase;
import org.raapp.messenger.database.IdentityDatabase.IdentityRecord;
import org.raapp.messenger.database.RecipientDatabase;
import org.raapp.messenger.database.RecipientDatabase.VibrateState;
import org.raapp.messenger.database.loaders.ThreadMediaLoader;
import org.raapp.messenger.jobs.MultiDeviceBlockedUpdateJob;
import org.raapp.messenger.mms.GlideApp;
import org.raapp.messenger.mms.GlideRequests;
import org.raapp.messenger.mms.OutgoingGroupMediaMessage;
import org.raapp.messenger.notifications.NotificationChannels;
import org.raapp.messenger.permissions.Permissions;
import org.raapp.messenger.preferences.CorrectedPreferenceFragment;
import org.raapp.messenger.preferences.widgets.ContactPreference;
import org.raapp.messenger.preferences.widgets.UserListAdapter;
import org.raapp.messenger.recipients.Recipient;
import org.raapp.messenger.recipients.RecipientExporter;
import org.raapp.messenger.recipients.RecipientModifiedListener;
import org.raapp.messenger.sms.MessageSender;
import org.raapp.messenger.util.CommunicationActions;
import org.raapp.messenger.util.Dialogs;
import org.raapp.messenger.util.DynamicLanguage;
import org.raapp.messenger.util.DynamicNoActionBarTheme;
import org.raapp.messenger.util.DynamicTheme;
import org.raapp.messenger.util.GroupUtil;
import org.raapp.messenger.util.IdentityUtil;
import org.raapp.messenger.util.RoleUtil;
import org.raapp.messenger.util.TextSecurePreferences;
import org.raapp.messenger.util.Util;
import org.raapp.messenger.util.ViewUtil;
import org.raapp.messenger.util.concurrent.ListenableFuture;
import org.whispersystems.libsignal.util.guava.Optional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@SuppressLint("StaticFieldLeak")
public class RecipientPreferenceActivity extends PassphraseRequiredActionBarActivity implements RecipientModifiedListener, LoaderManager.LoaderCallbacks<Cursor>
{
  private static final String TAG = RecipientPreferenceActivity.class.getSimpleName();

  public static final String ADDRESS_EXTRA                = "recipient_address";
  public static final String CAN_HAVE_SAFETY_NUMBER_EXTRA = "can_have_safety_number";

  private static final String PREFERENCE_MUTED                 = "pref_key_recipient_mute";
  private static final String PREFERENCE_MESSAGE_TONE          = "pref_key_recipient_ringtone";
  private static final String PREFERENCE_CALL_TONE             = "pref_key_recipient_call_ringtone";
  private static final String PREFERENCE_MESSAGE_VIBRATE       = "pref_key_recipient_vibrate";
  private static final String PREFERENCE_CALL_VIBRATE          = "pref_key_recipient_call_vibrate";
  private static final String PREFERENCE_BLOCK                 = "pref_key_recipient_block";
  private static final String PREFERENCE_IDENTITY              = "pref_key_recipient_identity";
  private static final String PREFERENCE_ABOUT                 = "pref_key_number";
  private static final String PREFERENCE_CUSTOM_NOTIFICATIONS  = "pref_key_recipient_custom_notifications";

  private final DynamicTheme    dynamicTheme    = new DynamicNoActionBarTheme();
  private final DynamicLanguage dynamicLanguage = new DynamicLanguage();

  private ImageView               avatar;
  private GlideRequests           glideRequests;
  private Address                 address;
  private TextView                threadPhotoRailLabel;
  private ThreadPhotoRailView     threadPhotoRailView;
  private CollapsingToolbarLayout toolbarLayout;

  private Recipient recipient;

  private LinearLayout userGroupLL;
  private LinearLayout addUserLL;
  private RecyclerView userRV;

  @Override
  public void onPreCreate() {
    dynamicTheme.onCreate(this);
    dynamicLanguage.onCreate(this);
  }

  @Override
  public void onCreate(Bundle instanceState, boolean ready) {
    setContentView(R.layout.recipient_preference_activity);
    this.glideRequests = GlideApp.with(this);
    this.address       = getIntent().getParcelableExtra(ADDRESS_EXTRA);

    recipient = Recipient.from(this, address, true);

    initializeToolbar();
    setHeader(recipient);
    recipient.addListener(this);

    getSupportLoaderManager().initLoader(0, null, this);
  }

  @Override
  public void onResume() {
    super.onResume();
    dynamicTheme.onResume(this);
    dynamicLanguage.onResume(this);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.preference_fragment);
    fragment.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    super.onOptionsItemSelected(item);
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        return true;
    }

    return false;
  }

  @Override
  public void onBackPressed() {
    finish();
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }

  private void initializeToolbar() {
    this.toolbarLayout        = ViewUtil.findById(this, R.id.collapsing_toolbar);
    this.avatar               = ViewUtil.findById(this, R.id.avatar);
    this.threadPhotoRailView  = ViewUtil.findById(this, R.id.recent_photos);
    this.threadPhotoRailLabel = ViewUtil.findById(this, R.id.rail_label);

    this.toolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.white));
    this.toolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));

    this.threadPhotoRailView.setListener(mediaRecord -> {
      Intent intent = new Intent(RecipientPreferenceActivity.this, MediaPreviewActivity.class);
      intent.putExtra(MediaPreviewActivity.ADDRESS_EXTRA, address);
      intent.putExtra(MediaPreviewActivity.OUTGOING_EXTRA, mediaRecord.isOutgoing());
      intent.putExtra(MediaPreviewActivity.DATE_EXTRA, mediaRecord.getDate());
      intent.putExtra(MediaPreviewActivity.SIZE_EXTRA, mediaRecord.getAttachment().getSize());
      intent.putExtra(MediaPreviewActivity.CAPTION_EXTRA, mediaRecord.getAttachment().getCaption());
      intent.putExtra(MediaPreviewActivity.LEFT_IS_RECENT_EXTRA, ViewCompat.getLayoutDirection(threadPhotoRailView) == ViewCompat.LAYOUT_DIRECTION_LTR);
      intent.setDataAndType(mediaRecord.getAttachment().getDataUri(), mediaRecord.getContentType());
      startActivity(intent);
    });

    this.threadPhotoRailLabel.setOnClickListener(v -> {
      Intent intent = new Intent(this, MediaOverviewActivity.class);
      intent.putExtra(MediaOverviewActivity.ADDRESS_EXTRA, address);
      startActivity(intent);
    });

    Toolbar toolbar = ViewUtil.findById(this, R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setLogo(null);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      getWindow().setStatusBarColor(Color.TRANSPARENT);
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
  }

  private void setHeader(@NonNull Recipient recipient) {
    ContactPhoto         contactPhoto  = recipient.isLocalNumber() ? new ProfileContactPhoto(recipient.getAddress(), String.valueOf(TextSecurePreferences.getProfileAvatarId(this)))
                                                                   : recipient.getContactPhoto();
    FallbackContactPhoto fallbackPhoto = recipient.isLocalNumber() ? new ResourceContactPhoto(R.drawable.ic_profile_default, R.drawable.ic_person_large)
                                                                   : recipient.getFallbackContactPhoto();

    glideRequests.load(contactPhoto)
                 .fallback(fallbackPhoto.asCallCard(this))
                 .error(fallbackPhoto.asCallCard(this))
                 .diskCacheStrategy(DiskCacheStrategy.ALL)
                 .into(this.avatar);

    if (contactPhoto == null) this.avatar.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    else                      this.avatar.setScaleType(ImageView.ScaleType.CENTER_CROP);

    this.avatar.setBackgroundColor(getResources().getColor(R.color.primary));
    this.toolbarLayout.setTitle(" ");
    this.toolbarLayout.setContentScrimColor(getResources().getColor(R.color.primary));
  }

  @Override
  public void onModified(final Recipient recipient) {
    Util.runOnMain(() -> setHeader(recipient));
  }

  @Override
  public @NonNull Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new ThreadMediaLoader(this, address, true);
  }

  @Override
  public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
    if (data != null && data.getCount() > 0) {
      this.threadPhotoRailLabel.setVisibility(View.VISIBLE);
      this.threadPhotoRailView.setVisibility(View.VISIBLE);
    } else {
      this.threadPhotoRailLabel.setVisibility(View.GONE);
      this.threadPhotoRailView.setVisibility(View.GONE);
    }

    this.threadPhotoRailView.setCursor(glideRequests, data);

    Bundle bundle = new Bundle();
    bundle.putParcelable(ADDRESS_EXTRA, address);
    initUserList();
    initFragment(R.id.preference_fragment, new RecipientPreferenceFragment(), null, bundle);
  }

  private void initUserList() {
    userGroupLL = findViewById(R.id.ll_user_group);
    addUserLL = findViewById(R.id.ll_add_user);
    userRV = findViewById(R.id.rv_user_list_preference);
    userRV.setLayoutManager(new LinearLayoutManager(this));
    Recipient recipient = Recipient.from(this, address, true);

    if (recipient.getAddress().isGroup()) {
      new GroupMembersAsyncTask(this, recipient).execute();
      if(RoleUtil.isAdminInCompany(this, recipient.getAddress().toString())){ //is ADMIN
        userGroupLL.setVisibility(View.VISIBLE);
      }else{
        userGroupLL.setVisibility(View.GONE);
      }
    }

    addUserLL.setOnClickListener(v -> {
      Intent intent = new Intent(this, GroupCreateActivity
              .class);
      intent.putExtra(GroupCreateActivity.GROUP_ADDRESS_EXTRA, recipient.getAddress());
      startActivity(intent);
    });
  }

  @Override
  public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    this.threadPhotoRailView.setCursor(glideRequests, null);
  }

  public static class RecipientPreferenceFragment
      extends    CorrectedPreferenceFragment
      implements RecipientModifiedListener
  {
    private Recipient recipient;
    private boolean   canHaveSafetyNumber;

    @Override
    public void onCreate(Bundle icicle) {
      Log.i(TAG, "onCreate (fragment)");
      super.onCreate(icicle);

      initializeRecipients();

      this.canHaveSafetyNumber = getActivity().getIntent()
                                 .getBooleanExtra(RecipientPreferenceActivity.CAN_HAVE_SAFETY_NUMBER_EXTRA, false);

      Preference customNotificationsPref  = this.findPreference(PREFERENCE_CUSTOM_NOTIFICATIONS);

      if (NotificationChannels.supported()) {
        ((SwitchPreferenceCompat) customNotificationsPref).setChecked(recipient.getNotificationChannel() != null);
        customNotificationsPref.setOnPreferenceChangeListener(new CustomNotificationsChangedListener());

        this.findPreference(PREFERENCE_MESSAGE_TONE).setDependency(PREFERENCE_CUSTOM_NOTIFICATIONS);
        this.findPreference(PREFERENCE_MESSAGE_VIBRATE).setDependency(PREFERENCE_CUSTOM_NOTIFICATIONS);

        if (recipient.getNotificationChannel() != null) {
          final Context context = getContext();
          new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
              RecipientDatabase db = DatabaseFactory.getRecipientDatabase(getContext());
              db.setMessageRingtone(recipient, NotificationChannels.getMessageRingtone(context, recipient));
              db.setMessageVibrate(recipient, NotificationChannels.getMessageVibrate(context, recipient) ? VibrateState.ENABLED : VibrateState.DISABLED);
              NotificationChannels.ensureCustomChannelConsistency(context);
              return null;
            }
          }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        }
      } else {
        customNotificationsPref.setVisible(false);
      }

      this.findPreference(PREFERENCE_MESSAGE_TONE)
          .setOnPreferenceChangeListener(new RingtoneChangeListener(false));
      this.findPreference(PREFERENCE_MESSAGE_TONE)
          .setOnPreferenceClickListener(new RingtoneClickedListener(false));
      this.findPreference(PREFERENCE_CALL_TONE)
          .setOnPreferenceChangeListener(new RingtoneChangeListener(true));
      this.findPreference(PREFERENCE_CALL_TONE)
          .setOnPreferenceClickListener(new RingtoneClickedListener(true));
      this.findPreference(PREFERENCE_MESSAGE_VIBRATE)
          .setOnPreferenceChangeListener(new VibrateChangeListener(false));
      this.findPreference(PREFERENCE_CALL_VIBRATE)
          .setOnPreferenceChangeListener(new VibrateChangeListener(true));
      this.findPreference(PREFERENCE_MUTED)
          .setOnPreferenceClickListener(new MuteClickedListener());
      this.findPreference(PREFERENCE_BLOCK)
          .setOnPreferenceClickListener(new BlockClickedListener());
      ContactPreference aboutPreference = (ContactPreference)this.findPreference(PREFERENCE_ABOUT);
      aboutPreference.setListener(new AboutNumberClickedListener());
    }

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, String rootKey) {
      Log.i(TAG, "onCreatePreferences...");
      addPreferencesFromResource(R.xml.recipient_preferences);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
      Permissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void onResume() {
      super.onResume();
      setSummaries(recipient);
    }

    @Override
    public void onDestroy() {
      super.onDestroy();
      this.recipient.removeListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
        Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

        findPreference(PREFERENCE_MESSAGE_TONE).getOnPreferenceChangeListener().onPreferenceChange(findPreference(PREFERENCE_MESSAGE_TONE), uri);
      } else if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
        Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

        findPreference(PREFERENCE_CALL_TONE).getOnPreferenceChangeListener().onPreferenceChange(findPreference(PREFERENCE_CALL_TONE), uri);
      }
    }

    private void initializeRecipients() {
      this.recipient = Recipient.from(getActivity(), getArguments().getParcelable(ADDRESS_EXTRA), true);
      this.recipient.addListener(this);
    }

    private void setSummaries(Recipient recipient) {
      CheckBoxPreference    mutePreference            = (CheckBoxPreference) this.findPreference(PREFERENCE_MUTED);
      Preference            customPreference          = this.findPreference(PREFERENCE_CUSTOM_NOTIFICATIONS);
      Preference            ringtoneMessagePreference = this.findPreference(PREFERENCE_MESSAGE_TONE);
      Preference            ringtoneCallPreference    = this.findPreference(PREFERENCE_CALL_TONE);
      ListPreference        vibrateMessagePreference  = (ListPreference) this.findPreference(PREFERENCE_MESSAGE_VIBRATE);
      ListPreference        vibrateCallPreference     = (ListPreference) this.findPreference(PREFERENCE_CALL_VIBRATE);
      Preference            blockPreference           = this.findPreference(PREFERENCE_BLOCK);
      Preference            identityPreference        = this.findPreference(PREFERENCE_IDENTITY);
      PreferenceCategory    callCategory              = (PreferenceCategory)this.findPreference("call_settings");
      PreferenceCategory    aboutCategory             = (PreferenceCategory)this.findPreference("about");
      PreferenceCategory    aboutDivider              = (PreferenceCategory)this.findPreference("about_divider");
      ContactPreference     aboutPreference           = (ContactPreference)this.findPreference(PREFERENCE_ABOUT);
      PreferenceCategory    privacyCategory           = (PreferenceCategory) this.findPreference("privacy_settings");
      PreferenceCategory    divider                   = (PreferenceCategory) this.findPreference("divider");

      mutePreference.setChecked(recipient.isMuted());

      ringtoneMessagePreference.setSummary(ringtoneMessagePreference.isEnabled() ? getRingtoneSummary(getContext(), recipient.getMessageRingtone()) : "");
      ringtoneCallPreference.setSummary(getRingtoneSummary(getContext(), recipient.getCallRingtone()));

      Pair<String, Integer> vibrateMessageSummary = getVibrateSummary(getContext(), recipient.getMessageVibrate());
      Pair<String, Integer> vibrateCallSummary    = getVibrateSummary(getContext(), recipient.getCallVibrate());

      vibrateMessagePreference.setSummary(vibrateMessagePreference.isEnabled() ? vibrateMessageSummary.first : "");
      vibrateMessagePreference.setValueIndex(vibrateMessageSummary.second);

      vibrateCallPreference.setSummary(vibrateCallSummary.first);
      vibrateCallPreference.setValueIndex(vibrateCallSummary.second);

      if (recipient.isLocalNumber()) {
        mutePreference.setVisible(false);
        customPreference.setVisible(false);
        ringtoneMessagePreference.setVisible(false);
        vibrateMessagePreference.setVisible(false);

        if (identityPreference != null) identityPreference.setVisible(false);
        if (aboutCategory      != null) aboutCategory.setVisible(false);
        if (aboutDivider       != null) aboutDivider.setVisible(false);
        if (privacyCategory    != null) privacyCategory.setVisible(false);
        if (divider            != null) divider.setVisible(false);
        if (callCategory       != null) callCategory.setVisible(false);
      } if (recipient.isGroupRecipient()) {
        aboutPreference.setTitle(recipient.getName());
        if (identityPreference != null) identityPreference.setVisible(false);
        if (callCategory       != null) callCategory.setVisible(false);
        if (aboutCategory      != null) aboutCategory.setVisible(false);
        if (aboutDivider       != null) aboutDivider.setVisible(false);
        if (divider            != null) divider.setVisible(false);

        if(RoleUtil.isAdminInCompany(getActivity(), recipient.getAddress().toString())){ //is ADMIN
          aboutPreference.initGroupAdminView();
        }else{
          aboutPreference.initGroupUserView();
        }

      } else {

        aboutPreference.setTitle(recipient.getName());
        aboutPreference.setSummary(formatAddress(recipient.getAddress()));
        aboutPreference.setSecure(recipient.getRegistered() == RecipientDatabase.RegisteredState.REGISTERED);

        if (recipient.isBlocked()) blockPreference.setTitle(R.string.RecipientPreferenceActivity_unblock);
        else                       blockPreference.setTitle(R.string.RecipientPreferenceActivity_block);

        IdentityUtil.getRemoteIdentityKey(getActivity(), recipient).addListener(new ListenableFuture.Listener<Optional<IdentityRecord>>() {
          @Override
          public void onSuccess(Optional<IdentityRecord> result) {
            if (result.isPresent()) {
              if (identityPreference != null) identityPreference.setOnPreferenceClickListener(new IdentityClickedListener(result.get()));
              if (identityPreference != null) identityPreference.setEnabled(true);
            } else if (canHaveSafetyNumber) {
              if (identityPreference != null) identityPreference.setSummary(R.string.RecipientPreferenceActivity_available_once_a_message_has_been_sent_or_received);
              if (identityPreference != null) identityPreference.setEnabled(false);
            } else {
              if (identityPreference != null) getPreferenceScreen().removePreference(identityPreference);
            }
          }

          @Override
          public void onFailure(ExecutionException e) {
            if (identityPreference != null) getPreferenceScreen().removePreference(identityPreference);
          }
        });
      }


      if(recipient.isOfficeApp()){
        aboutPreference.initOfficeAppView();
      }
    }

    private @NonNull String formatAddress(@NonNull Address address) {
      if      (address.isPhone()) return PhoneNumberUtils.formatNumber(address.toPhoneString());
      else if (address.isEmail()) return address.toEmailString();
      else                        return "";
    }

    private @NonNull String getRingtoneSummary(@NonNull Context context, @Nullable Uri ringtone) {
      if (ringtone == null) {
        return context.getString(R.string.preferences__default);
      } else if (ringtone.toString().isEmpty()) {
        return context.getString(R.string.preferences__silent);
      } else {
        Ringtone tone = RingtoneManager.getRingtone(getActivity(), ringtone);

        if (tone != null) {
          return tone.getTitle(context);
        }
      }

      return context.getString(R.string.preferences__default);
    }

    private @NonNull Pair<String, Integer> getVibrateSummary(@NonNull Context context, @NonNull VibrateState vibrateState) {
      if (vibrateState == VibrateState.DEFAULT) {
        return new Pair<>(context.getString(R.string.preferences__default), 0);
      } else if (vibrateState == VibrateState.ENABLED) {
        return new Pair<>(context.getString(R.string.RecipientPreferenceActivity_enabled), 1);
      } else {
        return new Pair<>(context.getString(R.string.RecipientPreferenceActivity_disabled), 2);
      }
    }

    @Override
    public void onModified(final Recipient recipient) {
      Util.runOnMain(() -> {
        if (getContext() != null && getActivity() != null && !getActivity().isFinishing()) {
          setSummaries(recipient);
        }
      });
    }

    private class RingtoneChangeListener implements Preference.OnPreferenceChangeListener {

      private final boolean calls;

      RingtoneChangeListener(boolean calls) {
        this.calls = calls;
      }

      @Override
      public boolean onPreferenceChange(Preference preference, Object newValue) {
        final Context context = preference.getContext();

        Uri value = (Uri)newValue;

        Uri defaultValue;

        if (calls) defaultValue = TextSecurePreferences.getCallNotificationRingtone(context);
        else       defaultValue = TextSecurePreferences.getNotificationRingtone(context);

        if (defaultValue.equals(value)) value = null;
        else if (value == null)         value = Uri.EMPTY;


        new AsyncTask<Uri, Void, Void>() {
          @Override
          protected Void doInBackground(Uri... params) {
            if (calls) {
              DatabaseFactory.getRecipientDatabase(context).setCallRingtone(recipient, params[0]);
            } else {
              DatabaseFactory.getRecipientDatabase(context).setMessageRingtone(recipient, params[0]);
              NotificationChannels.updateMessageRingtone(context, recipient, params[0]);
            }
            return null;
          }
        }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, value);

        return false;
      }
    }

    private class RingtoneClickedListener implements Preference.OnPreferenceClickListener {

      private final boolean calls;

      RingtoneClickedListener(boolean calls) {
        this.calls = calls;
      }

      @Override
      public boolean onPreferenceClick(Preference preference) {
        Uri current;
        Uri defaultUri;

        if (calls) {
          current    = recipient.getCallRingtone();
          defaultUri = TextSecurePreferences.getCallNotificationRingtone(getContext());
        } else  {
          current    = recipient.getMessageRingtone();
          defaultUri = TextSecurePreferences.getNotificationRingtone(getContext());
        }

        if      (current == null)              current = Settings.System.DEFAULT_NOTIFICATION_URI;
        else if (current.toString().isEmpty()) current = null;

        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, defaultUri);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, calls ? RingtoneManager.TYPE_RINGTONE : RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, current);

        startActivityForResult(intent, calls ? 2 : 1);

        return true;
      }
    }

    private class VibrateChangeListener implements Preference.OnPreferenceChangeListener {

      private final boolean call;

      VibrateChangeListener(boolean call) {
        this.call = call;
      }

      @Override
      public boolean onPreferenceChange(Preference preference, Object newValue) {
              int          value        = Integer.parseInt((String) newValue);
        final VibrateState vibrateState = VibrateState.fromId(value);
        final Context      context      = preference.getContext();

        new AsyncTask<Void, Void, Void>() {
          @Override
          protected Void doInBackground(Void... params) {
            if (call) {
              DatabaseFactory.getRecipientDatabase(context).setCallVibrate(recipient, vibrateState);
            }
            else {
              DatabaseFactory.getRecipientDatabase(context).setMessageVibrate(recipient, vibrateState);
              NotificationChannels.updateMessageVibrate(context, recipient, vibrateState);
            }
            return null;
          }
        }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);

        return false;
      }
    }

    private class MuteClickedListener implements Preference.OnPreferenceClickListener {

      @Override
      public boolean onPreferenceClick(Preference preference) {
        if (recipient.isMuted()) handleUnmute(preference.getContext());
        else                     handleMute(preference.getContext());

        return true;
      }

      private void handleMute(@NonNull Context context) {
        MuteDialog.show(context, until -> setMuted(context, recipient, until));

        setSummaries(recipient);
      }

      private void handleUnmute(@NonNull Context context) {
        setMuted(context, recipient, 0);
      }

      private void setMuted(@NonNull final Context context, final Recipient recipient, final long until) {
        recipient.setMuted(until);

        new AsyncTask<Void, Void, Void>() {
          @Override
          protected Void doInBackground(Void... params) {
            DatabaseFactory.getRecipientDatabase(context)
                           .setMuted(recipient, until);
            return null;
          }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
      }
    }

    private class IdentityClickedListener implements Preference.OnPreferenceClickListener {

      private final IdentityRecord identityKey;

      private IdentityClickedListener(IdentityRecord identityKey) {
        Log.i(TAG, "Identity record: " + identityKey);
        this.identityKey = identityKey;
      }

      @Override
      public boolean onPreferenceClick(Preference preference) {
        Intent verifyIdentityIntent = new Intent(preference.getContext(), VerifyIdentityActivity.class);
        verifyIdentityIntent.putExtra(VerifyIdentityActivity.ADDRESS_EXTRA, recipient.getAddress());
        verifyIdentityIntent.putExtra(VerifyIdentityActivity.IDENTITY_EXTRA, new IdentityKeyParcelable(identityKey.getIdentityKey()));
        verifyIdentityIntent.putExtra(VerifyIdentityActivity.VERIFIED_EXTRA, identityKey.getVerifiedStatus() == IdentityDatabase.VerifiedStatus.VERIFIED);
        startActivity(verifyIdentityIntent);

        return true;
      }
    }

    private class BlockClickedListener implements Preference.OnPreferenceClickListener {
      @Override
      public boolean onPreferenceClick(Preference preference) {
        if (recipient.isBlocked()) handleUnblock(preference.getContext());
        else                       handleBlock(preference.getContext());

        return true;
      }

      private void handleBlock(@NonNull final Context context) {
        new AsyncTask<Void, Void, Pair<Integer, Integer>>() {

          @Override
          protected Pair<Integer, Integer> doInBackground(Void... voids) {
            int titleRes = R.string.RecipientPreferenceActivity_block_this_contact_question;
            int bodyRes  = R.string.RecipientPreferenceActivity_you_will_no_longer_receive_messages_and_calls_from_this_contact;

            if (recipient.isGroupRecipient()) {
              bodyRes = R.string.RecipientPreferenceActivity_block_and_leave_group_description;

              if (recipient.isGroupRecipient() && DatabaseFactory.getGroupDatabase(context).isActive(recipient.getAddress().toGroupString())) {
                titleRes = R.string.RecipientPreferenceActivity_block_and_leave_group;
              } else {
                titleRes = R.string.RecipientPreferenceActivity_block_group;
              }
            }

            return new Pair<>(titleRes, bodyRes);
          }

          @Override
          protected void onPostExecute(Pair<Integer, Integer> titleAndBody) {
            new AlertDialog.Builder(context)
                           .setTitle(titleAndBody.first)
                           .setMessage(titleAndBody.second)
                           .setCancelable(true)
                           .setNegativeButton(android.R.string.cancel, null)
                           .setPositiveButton(R.string.RecipientPreferenceActivity_block, (dialog, which) -> {
                             setBlocked(context, recipient, true);
                           }).show();
          }
        }.execute();
      }

      private void handleUnblock(@NonNull Context context) {
        int titleRes = R.string.RecipientPreferenceActivity_unblock_this_contact_question;
        int bodyRes  = R.string.RecipientPreferenceActivity_you_will_once_again_be_able_to_receive_messages_and_calls_from_this_contact;

        if (recipient.isGroupRecipient()) {
          titleRes = R.string.RecipientPreferenceActivity_unblock_this_group_question;
          bodyRes  = R.string.RecipientPreferenceActivity_unblock_this_group_description;
        }

        new AlertDialog.Builder(context)
                       .setTitle(titleRes)
                       .setMessage(bodyRes)
                       .setCancelable(true)
                       .setNegativeButton(android.R.string.cancel, null)
                       .setPositiveButton(R.string.RecipientPreferenceActivity_unblock, (dialog, which) -> setBlocked(context, recipient, false)).show();
      }

      private void setBlocked(@NonNull final Context context, final Recipient recipient, final boolean blocked) {
        new AsyncTask<Void, Void, Void>() {
          @Override
          protected Void doInBackground(Void... params) {
            DatabaseFactory.getRecipientDatabase(context)
                           .setBlocked(recipient, blocked);

            if (recipient.isGroupRecipient() && DatabaseFactory.getGroupDatabase(context).isActive(recipient.getAddress().toGroupString())) {
              long                                threadId     = DatabaseFactory.getThreadDatabase(context).getThreadIdFor(recipient);
              Optional<OutgoingGroupMediaMessage> leaveMessage = GroupUtil.createGroupLeaveMessage(context, recipient);

              if (threadId != -1 && leaveMessage.isPresent()) {
                MessageSender.send(context, leaveMessage.get(), threadId, false, null);

                GroupDatabase groupDatabase = DatabaseFactory.getGroupDatabase(context);
                String        groupId       = recipient.getAddress().toGroupString();
                groupDatabase.setActive(groupId, false);
                groupDatabase.remove(groupId, Address.fromSerialized(TextSecurePreferences.getLocalNumber(context)));
              } else {
                Log.w(TAG, "Failed to leave group. Can't block.");
                Toast.makeText(context, R.string.RecipientPreferenceActivity_error_leaving_group, Toast.LENGTH_LONG).show();
              }
            }

            if (blocked && (recipient.resolve().isSystemContact() || recipient.resolve().isProfileSharing())) {
              ApplicationContext.getInstance(context)
                                .getJobManager()
                                .add(new RotateProfileKeyJob());
            }

            ApplicationContext.getInstance(context)
                              .getJobManager()
                              .add(new MultiDeviceBlockedUpdateJob());
            return null;
          }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
      }
    }

    private class AboutNumberClickedListener implements ContactPreference.Listener {

      @Override
      public void onMessageClicked() {
        CommunicationActions.startConversation(getContext(), recipient, null);
      }

      @Override
      public void onSecureCallClicked() {
        CommunicationActions.startVoiceCall(getActivity(), recipient);
      }

      @Override
      public void onInSecureCallClicked() {
        try {
          Intent dialIntent = new Intent(Intent.ACTION_DIAL,
                                         Uri.parse("tel:" + recipient.getAddress().serialize()));
          startActivity(dialIntent);
        } catch (ActivityNotFoundException anfe) {
          Log.w(TAG, anfe);
          Dialogs.showAlertDialog(getContext(),
                                  getString(R.string.ConversationActivity_calls_not_supported),
                                  getString(R.string.ConversationActivity_this_device_does_not_appear_to_support_dial_actions));
        }
      }

      @Override
      public void onEditClicked() {
        Intent intent = new Intent(getContext(), GroupCreateActivity
                .class);
        intent.putExtra(GroupCreateActivity.GROUP_ADDRESS_EXTRA, recipient.getAddress());
        startActivity(intent);
      }
    }

    private class CustomNotificationsChangedListener implements Preference.OnPreferenceChangeListener {

      @Override
      public boolean onPreferenceChange(Preference preference, Object newValue) {
        final Context context = preference.getContext();
        final boolean enabled = (boolean) newValue;

        new AsyncTask<Void, Void, Void>() {
          @Override
          protected Void doInBackground(Void... params) {
            if (enabled) {
              String channel = NotificationChannels.createChannelFor(context, recipient);
              DatabaseFactory.getRecipientDatabase(context).setNotificationChannel(recipient, channel);
            } else {
              NotificationChannels.deleteChannelFor(context, recipient);
              DatabaseFactory.getRecipientDatabase(context).setNotificationChannel(recipient, null);
            }
            return null;
          }
        }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);

        return true;
      }
    }
  }


  public class GroupMembersAsyncTask extends AsyncTask<Void, Void, List<Recipient>> {

    private final String TAG = GroupMembersAsyncTask.class.getSimpleName();

    private final Recipient recipient;
    private final Context context;

    public GroupMembersAsyncTask(Context context, Recipient recipient) {
      this.recipient = recipient;
      this.context = context;
    }

    @Override
    public void onPreExecute() {
    }

    @Override
    protected List<Recipient> doInBackground(Void... params) {
      return DatabaseFactory.getGroupDatabase(context).getGroupMembers(recipient.getAddress().toGroupString(), true);
    }

    @Override
    public void onPostExecute(List<Recipient> members) {
      UserListAdapter adapter = new UserListAdapter(context, members);
      userRV.setAdapter(adapter);
    }

    public void display() {
      executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
  }
}
