package org.bittube.messenger.mms;


import android.support.annotation.Nullable;

import org.bittube.messenger.attachments.Attachment;
import org.bittube.messenger.database.Address;

import java.util.List;

public class QuoteModel {

  private final long             id;
  private final Address          author;
  private final String           text;
  private final boolean          missing;
  private final List<Attachment> attachments;

  public QuoteModel(long id, Address author, String text, boolean missing, @Nullable List<Attachment> attachments) {
    this.id          = id;
    this.author      = author;
    this.text        = text;
    this.missing     = missing;
    this.attachments = attachments;
  }

  public long getId() {
    return id;
  }

  public Address getAuthor() {
    return author;
  }

  public String getText() {
    return text;
  }

  public boolean isOriginalMissing() {
    return missing;
  }

  public List<Attachment> getAttachments() {
    return attachments;
  }
}
