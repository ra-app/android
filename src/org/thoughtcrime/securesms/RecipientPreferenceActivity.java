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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;

import org.raapp.messenger.components.SwitchPreferenceCompat;
import org.raapp.messenger.logging.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.raapp.messenger.color.MaterialColor;
import org.raapp.messenger.color.MaterialColors;
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
import org.raapp.messenger.jobs.MultiDeviceContactUpdateJob;
import org.raapp.messenger.mms.GlideApp;
import org.raapp.messenger.mms.GlideRequests;
import org.raapp.messenger.notifications.NotificationChannels;
import org.raapp.messenger.permissions.Permissions;
import org.raapp.messenger.preferences.CorrectedPreferenceFragment;
import org.raapp.messenger.preferences.widgets.ColorPickerPreference;
import org.raapp.messenger.preferences.widgets.ContactPreference;
import org.raapp.messenger.recipients.Recipient;
import org.raapp.messenger.recipients.RecipientModifiedListener;
import org.raapp.messenger.util.CommunicationActions;
import org.raapp.messenger.util.Dialogs;
import org.raapp.messenger.util.DynamicLanguage;
import org.raapp.messenger.util.DynamicNoActionBarTheme;
import org.raapp.messenger.util.DynamicTheme;
import org.raapp.messenger.util.IdentityUtil;
import org.raapp.messenger.util.TextSecurePreferences;
import org.raapp.messenger.util.Util;
import org.raapp.messenger.util.ViewUtil;
import org.raapp.messenger.util.concurrent.ListenableFuture;
import org.whispersystems.libsignal.util.guava.Optional;

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
  private static final String PREFERENCE_COLOR                 = "pref_key_recipient_color";
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

    Recipient recipient = Recipient.from(this, address, true);

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
    }
  }

  private void setHeader(@NonNull Recipient recipient) {
    glideRequests.load(recipient.getContactPhoto())
                 .fallback(recipient.getFallbackContactPhoto().asCallCard(this))
                 .error(recipient.getFallbackContactPhoto().asCallCard(this))
                 .diskCacheStrategy(DiskCacheStrategy.ALL)
                 .into(this.avatar);

    if (recipient.getContactPhoto() == null) this.avatar.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    else                                     this.avatar.setScaleType(ImageView.ScaleType.CENTER_CROP);

    this.avatar.setBackgroundColor(recipient.getColor().toActionBarColor(this));
    this.toolbarLayout.setTitle(recipient.toShortString());
    this.toolbarLayout.setContentScrimColor(recipient.getColor().toActionBarColor(this));
  }

  @Override
  public void onModified(final Recipient recipient) {
    Util.runOnMain(() -> setHeader(recipient));
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new ThreadMediaLoader(this, address, true);
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
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
    initFragment(R.id.preference_fragment, new RecipientPreferenceFragment(), null, bundle);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
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
              return null;
            }
          }.execute();
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
      this.findPreference(PREFERENCE_COLOR)
          .setOnPreferenceChangeListener(new ColorChangeListener());
      ((ContactPreference)this.findPreference(PREFERENCE_ABOUT))
          .setListener(new AboutNumberClickedListener());
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
      Preference            ringtoneMessagePreference = this.findPreference(PREFERENCE_MESSAGE_TONE);
      Preference            ringtoneCallPreference    = this.findPreference(PREFERENCE_CALL_TONE);
      ListPreference        vibrateMessagePreference  = (ListPreference) this.findPreference(PREFERENCE_MESSAGE_VIBRATE);
      ListPreference        vibrateCallPreference     = (ListPreference) this.findPreference(PREFERENCE_CALL_VIBRATE);
      ColorPickerPreference colorPreference           = (ColorPickerPreference) this.findPreference(PREFERENCE_COLOR);
      Preference            blockPreference           = this.findPreference(PREFERENCE_BLOCK);
      Preference            identityPreference        = this.findPreference(PREFERENCE_IDENTITY);
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

      if (recipient.isGroupRecipient()) {
        if (colorPreference    != null) colorPreference.setVisible(false);
        if (blockPreference    != null) blockPreference.setVisible(false);
        if (identityPreference != null) identityPreference.setVisible(false);
        if (privacyCategory    != null) privacyCategory.setVisible(false);
        if (divider            != null) divider.setVisible(false);
        if (aboutCategory      != null) getPreferenceScreen().removePreference(aboutCategory);
        if (aboutDivider       != null) getPreferenceScreen().removePreference(aboutDivider);
      } else {
        colorPreference.setColors(MaterialColors.CONVERSATION_PALETTE.asConversationColorArray(getActivity()));
        colorPreference.setColor(recipient.getColor().toActionBarColor(getActivity()));

        aboutPreference.setTitle(formatAddress(recipient.getAddress()));
        aboutPreference.setSummary(recipient.getCustomLabel());
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
        Uri value = (Uri)newValue;

        Uri defaultValue;

        if (calls) defaultValue = TextSecurePreferences.getCallNotificationRingtone(getContext());
        else       defaultValue = TextSecurePreferences.getNotificationRingtone(getContext());

        if (defaultValue.equals(value)) value = null;
        else if (value == null)         value = Uri.EMPTY;

        new AsyncTask<Uri, Void, Void>() {
          @Override
          protected Void doInBackground(Uri... params) {
            if (calls) {
              DatabaseFactory.getRecipientDatabase(getActivity()).setCallRingtone(recipient, params[0]);
            } else {
              DatabaseFactory.getRecipientDatabase(getActivity()).setMessageRingtone(recipient, params[0]);
              NotificationChannels.updateMessageRingtone(getActivity(), recipient, params[0]);
            }
            return null;
          }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, value);

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

        new AsyncTask<Void, Void, Void>() {
          @Override
          protected Void doInBackground(Void... params) {
            if (call) {
              DatabaseFactory.getRecipientDatabase(getActivity()).setCallVibrate(recipient, vibrateState);
            }
            else {
              DatabaseFactory.getRecipientDatabase(getActivity()).setMessageVibrate(recipient, vibrateState);
              NotificationChannels.updateMessageVibrate(getActivity(), recipient, vibrateState);
            }
            return null;
          }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        return false;
      }
    }

    private class ColorChangeListener implements Preference.OnPreferenceChangeListener {

      @Override
      public boolean onPreferenceChange(Preference preference, Object newValue) {
        final Context context = getContext();
        if (context == null) return true;

        final int           value         = (Integer) newValue;
        final MaterialColor selectedColor = MaterialColors.CONVERSATION_PALETTE.getByColor(context, value);
        final MaterialColor currentColor  = recipient.getColor();

        if (selectedColor == null) return true;

        if (preference.isEnabled() && !currentColor.equals(selectedColor)) {
          new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
              DatabaseFactory.getRecipientDatabase(context).setColor(recipient, selectedColor);

              if (recipient.resolve().getRegistered() == RecipientDatabase.RegisteredState.REGISTERED) {
                ApplicationContext.getInstance(context)
                                  .getJobManager()
                                  .add(new MultiDeviceContactUpdateJob(context, recipient.getAddress()));
              }
              return null;
            }
          }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        return true;
      }
    }

    private class MuteClickedListener implements Preference.OnPreferenceClickListener {
      @Override
      public boolean onPreferenceClick(Preference preference) {
        if (recipient.isMuted()) handleUnmute();
        else                     handleMute();

        return true;
      }

      private void handleMute() {
        MuteDialog.show(getActivity(), until -> setMuted(recipient, until));

        setSummaries(recipient);
      }

      private void handleUnmute() {
        setMuted(recipient, 0);
      }

      private void setMuted(final Recipient recipient, final long until) {
        recipient.setMuted(until);

        new AsyncTask<Void, Void, Void>() {
          @Override
          protected Void doInBackground(Void... params) {
            DatabaseFactory.getRecipientDatabase(getActivity())
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
        Intent verifyIdentityIntent = new Intent(getActivity(), VerifyIdentityActivity.class);
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
        if (recipient.isBlocked()) handleUnblock();
        else                       handleBlock();

        return true;
      }

      private void handleBlock() {
        new AlertDialog.Builder(getActivity())
            .setTitle(R.string.RecipientPreferenceActivity_block_this_contact_question)
            .setMessage(R.string.RecipientPreferenceActivity_you_will_no_longer_receive_messages_and_calls_from_this_contact)
            .setCancelable(true)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.RecipientPreferenceActivity_block, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                setBlocked(recipient, true);
              }
            }).show();
      }

      private void handleUnblock() {
        new AlertDialog.Builder(getActivity())
            .setTitle(R.string.RecipientPreferenceActivity_unblock_this_contact_question)
            .setMessage(R.string.RecipientPreferenceActivity_you_will_once_again_be_able_to_receive_messages_and_calls_from_this_contact)
            .setCancelable(true)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.RecipientPreferenceActivity_unblock, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                setBlocked(recipient, false);
              }
            }).show();
      }

      private void setBlocked(final Recipient recipient, final boolean blocked) {
        new AsyncTask<Void, Void, Void>() {
          @Override
          protected Void doInBackground(Void... params) {
            Context context = getActivity();

            DatabaseFactory.getRecipientDatabase(context)
                           .setBlocked(recipient, blocked);

            ApplicationContext.getInstance(context)
                              .getJobManager()
                              .add(new MultiDeviceBlockedUpdateJob(context));
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
    }

    private class CustomNotificationsChangedListener implements Preference.OnPreferenceChangeListener {

      @Override
      public boolean onPreferenceChange(Preference preference, Object newValue) {
        final boolean enabled = (boolean) newValue;

        new AsyncTask<Void, Void, Void>() {
          @Override
          protected Void doInBackground(Void... params) {
            if (enabled) {
              String channel = NotificationChannels.createChannelFor(getActivity(), recipient);
              DatabaseFactory.getRecipientDatabase(getActivity()).setNotificationChannel(recipient, channel);
            } else {
              NotificationChannels.deleteChannelFor(getActivity(), recipient);
              DatabaseFactory.getRecipientDatabase(getActivity()).setNotificationChannel(recipient, null);
            }
            return null;
          }
        }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);

        return true;
      }
    }
  }
}
