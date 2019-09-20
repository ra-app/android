package org.raapp.messenger.sms;


import org.raapp.messenger.recipients.Recipient;

public class OutgoingIdentityDefaultMessage extends OutgoingTextMessage {

  public OutgoingIdentityDefaultMessage(Recipient recipient) {
    this(recipient, "");
  }

  private OutgoingIdentityDefaultMessage(Recipient recipient, String body) {
    super(recipient, body, -1);
  }

  @Override
  public boolean isIdentityDefault() {
    return true;
  }

  public OutgoingTextMessage withBody(String body) {
    return new OutgoingIdentityDefaultMessage(getRecipient());
  }
}
