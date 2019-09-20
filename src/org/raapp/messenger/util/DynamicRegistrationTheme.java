package org.raapp.messenger.util;

import android.app.Activity;

import org.raapp.messenger.R;

public class DynamicRegistrationTheme extends DynamicTheme {
  @Override
  protected int getSelectedTheme(Activity activity) {
    String theme = TextSecurePreferences.getTheme(activity);

    if (theme.equals("dark")) return R.style.TextSecure_DarkRegistrationTheme;

    return R.style.TextSecure_LightRegistrationTheme;
  }
}
