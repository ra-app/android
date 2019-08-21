package org.bittube.messenger.database.loaders;

import android.content.Context;
import android.database.Cursor;

import org.bittube.messenger.database.DatabaseFactory;
import org.bittube.messenger.util.AbstractCursorLoader;

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
