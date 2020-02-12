/*
 * Copyright (C) 2014-2017 Open Whisper Systems
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.raapp.messenger;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.TooltipCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.raapp.messenger.blackboard.BlacboardActivity;
import org.raapp.messenger.client.Client;
import org.raapp.messenger.client.datamodel.CompanyRole;
import org.raapp.messenger.client.datamodel.Responses.ResponseCompanyList;
import org.raapp.messenger.color.MaterialColor;
import org.raapp.messenger.components.RatingManager;
import org.raapp.messenger.components.SearchToolbar;
import org.raapp.messenger.contacts.avatars.ContactColors;
import org.raapp.messenger.contacts.avatars.GeneratedContactPhoto;
import org.raapp.messenger.contacts.avatars.ProfileContactPhoto;
import org.raapp.messenger.conversation.ConversationActivity;
import org.raapp.messenger.conversation.admin.AdminConversationActivity;
import org.raapp.messenger.database.Address;
import org.raapp.messenger.database.DatabaseFactory;
import org.raapp.messenger.database.MessagingDatabase.MarkedMessageInfo;
import org.raapp.messenger.lock.RegistrationLockDialog;
import org.raapp.messenger.logging.Log;
import org.raapp.messenger.mms.GlideApp;
import org.raapp.messenger.notifications.MarkReadReceiver;
import org.raapp.messenger.notifications.MessageNotifier;
import org.raapp.messenger.permissions.Permissions;
import org.raapp.messenger.recipients.Recipient;
import org.raapp.messenger.search.SearchFragment;
import org.raapp.messenger.service.KeyCachingService;
import org.raapp.messenger.util.DynamicLanguage;
import org.raapp.messenger.util.RoleUtil;
import org.raapp.messenger.util.TextSecurePreferences;
import org.raapp.messenger.util.concurrent.SimpleTask;
import org.whispersystems.libsignal.util.guava.Optional;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConversationListActivity extends PassphraseRequiredActionBarActivity
    implements ConversationListFragment.ConversationSelectedListener
{
  @SuppressWarnings("unused")
  private static final String TAG = ConversationListActivity.class.getSimpleName();

  private final DynamicLanguage dynamicLanguage = new DynamicLanguage();

  private ConversationListFragment conversationListFragment;
  private SearchFragment           searchFragment;
  private SearchToolbar            searchToolbar;
  private ImageView                searchAction;
  private ViewGroup                fragmentContainer;

  @Override
  protected void onPreCreate() {
    dynamicLanguage.onCreate(this);
  }

  @Override
  protected void onCreate(Bundle icicle, boolean ready) {
    setContentView(R.layout.conversation_list_activity);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    searchToolbar            = findViewById(R.id.search_toolbar);
    searchAction             = findViewById(R.id.search_action);
    fragmentContainer        = findViewById(R.id.fragment_container);
    conversationListFragment = initFragment(R.id.fragment_container, new ConversationListFragment(), dynamicLanguage.getCurrentLocale());

    initializeSearchListener();

    RatingManager.showRatingDialogIfNecessary(this);
    RegistrationLockDialog.showReminderIfNecessary(this);

    TooltipCompat.setTooltipText(searchAction, getText(R.string.SearchToolbar_search_for_conversations_contacts_and_messages));


    Client.buildBase(this);
    Client.getCompanyList(new Callback<ResponseCompanyList>() {
      @Override
      public void onResponse(Call<ResponseCompanyList> call, Response<ResponseCompanyList> response) {
          if (response.isSuccessful()) {
              ResponseCompanyList resp = response.body();

              if (resp != null && resp.getSuccess()) {
                  List<CompanyRole> adminCompanyList = resp.getAdminCompanies();
                  List<CompanyRole> clientCompanyList = resp.getClientCompanies();

              } else{
                  String errorMsg = resp !=null ? resp.getError() : "Unexpected error";
                  Toast.makeText(ConversationListActivity.this, errorMsg, Toast.LENGTH_LONG).show();
              }
          } else{
              Toast.makeText(ConversationListActivity.this, "Server error", Toast.LENGTH_SHORT).show();
          }
      }

      @Override
      public void onFailure(Call<ResponseCompanyList> call, Throwable t) {
          Toast.makeText(ConversationListActivity.this, "HTTP error", Toast.LENGTH_SHORT).show();
      }
    });
  }

  @Override
  public void onResume() {
    super.onResume();
    dynamicLanguage.onResume(this);

    String token = this.getSharedPreferences(OfficeAppConstants.RA_PREFERENCES, MODE_PRIVATE).getString(OfficeAppConstants.TOKEN, "");
    Log.d("TOKEN:", token);

    SimpleTask.run(getLifecycle(), () -> {
      return Recipient.from(this, Address.fromSerialized(TextSecurePreferences.getLocalNumber(this)), false);
    }, this::initializeProfileIcon);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    MenuInflater inflater = this.getMenuInflater();
    menu.clear();

    inflater.inflate(R.menu.text_secure_normal, menu);

    menu.findItem(R.id.menu_clear_passphrase).setVisible(!TextSecurePreferences.isPasswordDisabled(this));

    menu.findItem(R.id.menu_new_group).setVisible(false);
    menu.findItem(R.id.menu_invite).setVisible(false);

    super.onPrepareOptionsMenu(menu);
    return true;
  }

  private void initializeSearchListener() {
    searchAction.setOnClickListener(v -> {
      Permissions.with(this)
                 .request(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
                 .ifNecessary()
                 .onAllGranted(() -> searchToolbar.display(searchAction.getX() + (searchAction.getWidth() / 2),
                                                           searchAction.getY() + (searchAction.getHeight() / 2)))
                 .withPermanentDenialDialog(getString(R.string.ConversationListActivity_signal_needs_contacts_permission_in_order_to_search_your_contacts_but_it_has_been_permanently_denied))
                 .execute();
    });

    searchToolbar.setListener(new SearchToolbar.SearchListener() {
      @Override
      public void onSearchTextChange(String text) {
        String trimmed = text.trim();

        if (trimmed.length() > 0) {
          if (searchFragment == null) {
            searchFragment = SearchFragment.newInstance(dynamicLanguage.getCurrentLocale());
            getSupportFragmentManager().beginTransaction()
                                       .add(R.id.fragment_container, searchFragment, null)
                                       .commit();
          }
          searchFragment.updateSearchQuery(trimmed);
        } else if (searchFragment != null) {
          getSupportFragmentManager().beginTransaction()
                                     .remove(searchFragment)
                                     .commit();
          searchFragment = null;
        }
      }

      @Override
      public void onSearchClosed() {
        if (searchFragment != null) {
          getSupportFragmentManager().beginTransaction()
                                     .remove(searchFragment)
                                     .commit();
          searchFragment = null;
        }
      }
    });
  }

  private void initializeProfileIcon(@NonNull Recipient recipient) {
    ImageView     icon          = findViewById(R.id.toolbar_icon);
    String        name          = Optional.fromNullable(recipient.getName()).or(Optional.fromNullable(TextSecurePreferences.getProfileName(this))).or("");
    MaterialColor fallbackColor = recipient.getColor();

    if (fallbackColor == ContactColors.UNKNOWN_COLOR && !TextUtils.isEmpty(name)) {
      fallbackColor = ContactColors.generateFor(name);
    }

    Drawable fallback = new GeneratedContactPhoto(name, R.drawable.ic_profile_default).asDrawable(this, fallbackColor.toAvatarColor(this));

    GlideApp.with(this)
            .load(new ProfileContactPhoto(recipient.getAddress(), String.valueOf(TextSecurePreferences.getProfileAvatarId(this))))
            .error(fallback)
            .circleCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(icon);

    icon.setOnClickListener(v -> handleDisplaySettings());
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    super.onOptionsItemSelected(item);

    switch (item.getItemId()) {
    case R.id.menu_new_group:         createGroup();           return true;
    case R.id.menu_settings:          handleDisplaySettings(); return true;
    case R.id.menu_clear_passphrase:  handleClearPassphrase(); return true;
    case R.id.menu_mark_all_read:     handleMarkAllRead();     return true;
    case R.id.menu_invite:            handleInvite();          return true;
    case R.id.menu_help:              handleHelp();            return true;
    }

    return false;
  }

  @Override
  public void onCreateConversation(long threadId, Recipient recipient, int distributionType, long lastSeen) {
    openConversation(threadId, recipient, distributionType, lastSeen, -1);
  }

  @Override
  public void onConversationBlackboardClick(long threadId, Recipient recipient) {
    openBlackboard(threadId, recipient);
  }

  public void openConversation(long threadId, Recipient recipient, int distributionType, long lastSeen, int startingPosition) {
    searchToolbar.clearFocus();

    Class startClass = ConversationActivity.class;
    if (RoleUtil.isAdminInCompany(this, recipient.getAddress().toString())) {
      startClass = AdminConversationActivity.class;
    }

    Intent intent = new Intent(this, startClass);
    intent.putExtra(ConversationActivity.ADDRESS_EXTRA, recipient.getAddress());
    intent.putExtra(ConversationActivity.THREAD_ID_EXTRA, threadId);
    intent.putExtra(ConversationActivity.DISTRIBUTION_TYPE_EXTRA, distributionType);
    intent.putExtra(ConversationActivity.TIMING_EXTRA, System.currentTimeMillis());
    intent.putExtra(ConversationActivity.LAST_SEEN_EXTRA, lastSeen);
    intent.putExtra(ConversationActivity.STARTING_POSITION_EXTRA, startingPosition);

    startActivity(intent);
    overridePendingTransition(R.anim.slide_from_right, R.anim.fade_scale_out);
  }

  private void openBlackboard(long threadId, Recipient recipient){
    Intent intent = new Intent(this, BlacboardActivity.class);
    intent.putExtra(ConversationActivity.ADDRESS_EXTRA, recipient.getAddress());
    intent.putExtra(ConversationActivity.THREAD_ID_EXTRA, threadId);
    startActivity(intent);
  }

  @Override
  public void onSwitchToArchive() {
    Intent intent = new Intent(this, ConversationListArchiveActivity.class);
    startActivity(intent);
  }

  @Override
  public void onBackPressed() {
    if (searchToolbar.isVisible()) searchToolbar.collapse();
    else                           super.onBackPressed();
  }

  private void createGroup() {
    Intent intent = new Intent(this, GroupCreateActivity.class);
    startActivity(intent);
  }

  private void handleDisplaySettings() {
    Intent preferencesIntent = new Intent(this, ApplicationPreferencesActivity.class);
    startActivity(preferencesIntent);
  }

  private void handleClearPassphrase() {
    Intent intent = new Intent(this, KeyCachingService.class);
    intent.setAction(KeyCachingService.CLEAR_KEY_ACTION);
    startService(intent);
  }

  @SuppressLint("StaticFieldLeak")
  private void handleMarkAllRead() {
    new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground(Void... params) {
        Context                 context    = ConversationListActivity.this;
        List<MarkedMessageInfo> messageIds = DatabaseFactory.getThreadDatabase(context).setAllThreadsRead();

        MessageNotifier.updateNotification(context);
        MarkReadReceiver.process(context, messageIds);

        return null;
      }
    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
  }

  private void handleInvite() {
    startActivity(new Intent(this, InviteActivity.class));
  }

  private void handleHelp() {
    try {
      startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://support.raapp.com")));
    } catch (ActivityNotFoundException e) {
      Toast.makeText(this, R.string.ConversationListActivity_there_is_no_browser_installed_on_your_device, Toast.LENGTH_LONG).show();
    }
  }
}
