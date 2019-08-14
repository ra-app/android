package org.thoughtcrime.securesms.util;

import android.app.Activity;

import org.raapp.messenger.R;

public class DynamicNoActionBarTheme extends DynamicTheme {
  @Override
  protected int getSelectedTheme(Activity activity) {
    String theme = TextSecurePreferences.getTheme(activity);

    if (theme.equals("dark")) return R.style.TextSecure_DarkNoActionBar;

    return R.style.TextSecure_LightNoActionBar;
  }
}
