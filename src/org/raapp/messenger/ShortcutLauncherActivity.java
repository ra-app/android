package org.raapp.messenger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.TaskStackBuilder;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import org.raapp.messenger.database.Address;
import org.raapp.messenger.recipients.Recipient;
import org.raapp.messenger.util.CommunicationActions;

public class ShortcutLauncherActivity extends AppCompatActivity {

  private static final String KEY_SERIALIZED_ADDRESS = "serialized_address";

  public static Intent createIntent(@NonNull Context context, @NonNull Address address) {
    Intent intent = new Intent(context, ShortcutLauncherActivity.class);
    intent.setAction(Intent.ACTION_MAIN);
    intent.putExtra(KEY_SERIALIZED_ADDRESS, address.serialize());

    return intent;
  }

  @SuppressLint("StaticFieldLeak")
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    String serializedAddress = getIntent().getStringExtra(KEY_SERIALIZED_ADDRESS);

    if (serializedAddress == null) {
      Toast.makeText(this, R.string.ShortcutLauncherActivity_invalid_shortcut, Toast.LENGTH_SHORT).show();
      startActivity(new Intent(this, ConversationListActivity.class));
      finish();
      return;
    }

    Address          address   = Address.fromSerialized(serializedAddress);
    Recipient        recipient = Recipient.from(this, address, true);
    TaskStackBuilder backStack = TaskStackBuilder.create(this)
                                                 .addNextIntent(new Intent(this, ConversationListActivity.class));

    CommunicationActions.startConversation(this, recipient, null, backStack);
    finish();
  }
}
