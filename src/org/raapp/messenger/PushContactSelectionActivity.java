/*
 * Copyright (C) 2011 Whisper Systems
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

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.raapp.messenger.components.ContactFilterToolbar;
import org.raapp.messenger.util.DynamicLanguage;
import org.raapp.messenger.util.DynamicNoActionBarTheme;
import org.raapp.messenger.util.DynamicTheme;
import org.raapp.messenger.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity container for selecting a list of contacts.
 *
 * @author Moxie Marlinspike
 *
 */
public class PushContactSelectionActivity extends ContactSelectionActivity {

  @SuppressWarnings("unused")
  private final static String TAG = PushContactSelectionActivity.class.getSimpleName();

  private final DynamicTheme dynamicTheme    = new DynamicTheme();
  private final DynamicLanguage dynamicLanguage = new DynamicLanguage();

  private ContactFilterToolbar toolbar;

  @Override
  protected void onPreCreate() {
    dynamicTheme.onCreate(this);
    dynamicLanguage.onCreate(this);
  }

  @Override
  protected void onCreate(Bundle icicle, boolean ready) {
    getIntent().putExtra(ContactSelectionListFragment.MULTI_SELECT, true);
    super.onCreate(icicle, ready);

    getSupportActionBar().setTitle(R.string.kontakte_auswahlen);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_white);

    this.toolbar = ViewUtil.findById(this, R.id.toolbar);
    toolbar.setNavigationIcon(R.drawable.ic_search_blue);
  }

  @Override
  public void onResume() {
    super.onResume();
    dynamicTheme.onResume(this);
    dynamicLanguage.onResume(this);
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    MenuInflater inflater = this.getMenuInflater();
    menu.clear();

    inflater.inflate(R.menu.group_create, menu);
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
        saveContacts();
        return true;
    }

    return false;
  }

  private void saveContacts() {
    Intent resultIntent = getIntent();
    List<String> selectedContacts = contactsFragment.getSelectedContacts();

    if (selectedContacts != null) {
      resultIntent.putStringArrayListExtra("contacts", new ArrayList<>(selectedContacts));
    }

    setResult(RESULT_OK, resultIntent);
    finish();
  }
}
