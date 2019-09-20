package org.raapp.messenger.database.loaders;

import android.content.Context;
import android.database.Cursor;

import org.raapp.messenger.database.DatabaseFactory;
import org.raapp.messenger.util.AbstractCursorLoader;

public class BlockedContactsLoader extends AbstractCursorLoader {

  public BlockedContactsLoader(Context context) {
    super(context);
  }

  @Override
  public Cursor getCursor() {
    return DatabaseFactory.getRecipientDatabase(getContext())
                          .getBlocked();
  }

}
