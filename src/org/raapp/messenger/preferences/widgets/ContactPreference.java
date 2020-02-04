package org.raapp.messenger.preferences.widgets;


import android.content.Context;
import android.graphics.PorterDuff;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import org.raapp.messenger.R;

public class ContactPreference extends Preference {

  private ImageView messageButton;
  private ImageView callButton;
  private ImageView secureCallButton;
  private ImageView editButton;

  private Listener listener;
  private boolean isBroadcastGroup;
  private boolean isAdmin;
  private boolean isOfficeApp;

  private boolean secure;

  public ContactPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    initialize();
  }

  public ContactPreference(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initialize();
  }

  public ContactPreference(Context context, AttributeSet attrs) {
    super(context, attrs);
    initialize();
  }

  public ContactPreference(Context context) {
    super(context);
    initialize();
  }

  private void initialize() {
    setWidgetLayoutResource(R.layout.recipient_preference_contact_widget);
  }

  @Override
  public void onBindViewHolder(PreferenceViewHolder view) {
    super.onBindViewHolder(view);

    this.messageButton    = (ImageView) view.findViewById(R.id.message);
    this.callButton       = (ImageView) view.findViewById(R.id.call);
    this.secureCallButton = (ImageView) view.findViewById(R.id.secure_call);
    this.editButton    = (ImageView) view.findViewById(R.id.edit);

    if (listener != null) setListener(listener);
    setSecure(secure);

    if(isBroadcastGroup){
      if(isAdmin){
        this.initGroupAdminView();
      }else{
        this.initGroupUserView();
      }
    }

    if(isOfficeApp){
      this.initOfficeAppView();
    }

  }

  public void setSecure(boolean secure) {
    this.secure = secure;

    if (secureCallButton != null) secureCallButton.setVisibility(secure ? View.VISIBLE : View.GONE);
    if (callButton != null)       callButton.setVisibility(secure ? View.GONE : View.VISIBLE);

    int color;

    if (secure) {
      color = getContext().getResources().getColor(R.color.textsecure_primary);
    } else {
      color = getContext().getResources().getColor(R.color.grey_600);
    }

    if (messageButton != null)    messageButton.setColorFilter(color, PorterDuff.Mode.SRC_IN);
    if (secureCallButton != null) secureCallButton.setColorFilter(color, PorterDuff.Mode.SRC_IN);
    if (callButton != null)       callButton.setColorFilter(color, PorterDuff.Mode.SRC_IN);
    if (editButton != null)       editButton.setColorFilter(color, PorterDuff.Mode.SRC_IN);
  }

  public void initGroupAdminView(){
    this.isBroadcastGroup = true;
    this.isAdmin = true;
    if (messageButton != null)    messageButton.setVisibility(View.GONE);
    if (callButton != null)       callButton.setVisibility(View.GONE);
    if (editButton != null)       editButton.setVisibility(View.VISIBLE);
  }

  public void initGroupUserView(){
    this.isBroadcastGroup = true;
    this.isAdmin = false;
    if (messageButton != null)    messageButton.setVisibility(View.GONE);
    if (callButton != null)       callButton.setVisibility(View.GONE);
    if (editButton != null)       editButton.setVisibility(View.GONE);
  }

  public void initOfficeAppView(){
    this.isOfficeApp = true;
    if (callButton != null)       callButton.setVisibility(View.GONE);
    if (secureCallButton != null) secureCallButton.setVisibility(View.GONE);
    if (editButton != null)       editButton.setVisibility(View.GONE);
    if (messageButton != null)    messageButton.setVisibility(View.GONE);
  }


  public void setListener(Listener listener) {
    this.listener = listener;

    if (this.messageButton != null)    this.messageButton.setOnClickListener(v -> listener.onMessageClicked());
    if (this.secureCallButton != null) this.secureCallButton.setOnClickListener(v -> listener.onSecureCallClicked());
    if (this.callButton != null)       this.callButton.setOnClickListener(v -> listener.onInSecureCallClicked());
    if (this.editButton != null)       this.editButton.setOnClickListener(v -> listener.onEditClicked());
  }

  public interface Listener {
    public void onMessageClicked();
    public void onSecureCallClicked();
    public void onInSecureCallClicked();
    public void onEditClicked();
  }

}
