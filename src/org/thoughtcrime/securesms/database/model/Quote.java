package org.bittube.messenger.database.model;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.bittube.messenger.database.Address;
import org.bittube.messenger.mms.SlideDeck;

public class Quote {

  private final long      id;
  private final Address   author;
  private final String    text;
  private final boolean   missing;
  private final SlideDeck attachment;

  public Quote(long id, @NonNull Address author, @Nullable String text, boolean missing, @NonNull SlideDeck attachment) {
    this.id         = id;
    this.author     = author;
    this.text       = text;
    this.missing    = missing;
    this.attachment = attachment;
  }

  public long getId() {
    return id;
  }

  public @NonNull Address getAuthor() {
    return author;
  }

  public @Nullable String getText() {
    return text;
  }

  public boolean isOriginalMissing() {
    return missing;
  }

  public @NonNull SlideDeck getAttachment() {
    return attachment;
  }
}
