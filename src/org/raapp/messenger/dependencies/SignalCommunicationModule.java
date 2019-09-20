package org.raapp.messenger.dependencies;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;
import org.raapp.messenger.BuildConfig;
import org.raapp.messenger.CreateProfileActivity;
import org.raapp.messenger.DeviceListFragment;
import org.raapp.messenger.crypto.storage.SignalProtocolStoreImpl;
import org.raapp.messenger.events.ReminderUpdateEvent;
import org.raapp.messenger.gcm.FcmJobService;
import org.raapp.messenger.gcm.FcmService;
import org.raapp.messenger.jobs.AttachmentDownloadJob;
import org.raapp.messenger.jobs.AttachmentUploadJob;
import org.raapp.messenger.jobs.AvatarDownloadJob;
import org.raapp.messenger.jobs.CleanPreKeysJob;
import org.raapp.messenger.jobs.CreateSignedPreKeyJob;
import org.raapp.messenger.jobs.FcmRefreshJob;
import org.raapp.messenger.jobs.MultiDeviceBlockedUpdateJob;
import org.raapp.messenger.jobs.MultiDeviceConfigurationUpdateJob;
import org.raapp.messenger.jobs.MultiDeviceContactUpdateJob;
import org.raapp.messenger.jobs.MultiDeviceGroupUpdateJob;
import org.raapp.messenger.jobs.MultiDeviceProfileKeyUpdateJob;
import org.raapp.messenger.jobs.MultiDeviceReadUpdateJob;
import org.raapp.messenger.jobs.MultiDeviceStickerPackOperationJob;
import org.raapp.messenger.jobs.MultiDeviceStickerPackSyncJob;
import org.raapp.messenger.jobs.MultiDeviceVerifiedUpdateJob;
import org.raapp.messenger.jobs.PushGroupSendJob;
import org.raapp.messenger.jobs.PushGroupUpdateJob;
import org.raapp.messenger.jobs.PushMediaSendJob;
import org.raapp.messenger.jobs.PushNotificationReceiveJob;
import org.raapp.messenger.jobs.PushTextSendJob;
import org.raapp.messenger.jobs.RefreshAttributesJob;
import org.raapp.messenger.jobs.RefreshPreKeysJob;
import org.raapp.messenger.jobs.RefreshUnidentifiedDeliveryAbilityJob;
import org.raapp.messenger.jobs.RequestGroupInfoJob;
import org.raapp.messenger.jobs.RetrieveProfileAvatarJob;
import org.raapp.messenger.jobs.RetrieveProfileJob;
import org.raapp.messenger.jobs.RotateCertificateJob;
import org.raapp.messenger.jobs.RotateProfileKeyJob;
import org.raapp.messenger.jobs.RotateSignedPreKeyJob;
import org.raapp.messenger.jobs.SendDeliveryReceiptJob;
import org.raapp.messenger.jobs.SendReadReceiptJob;
import org.raapp.messenger.jobs.StickerDownloadJob;
import org.raapp.messenger.jobs.StickerPackDownloadJob;
import org.raapp.messenger.jobs.TypingSendJob;
import org.raapp.messenger.linkpreview.LinkPreviewRepository;
import org.raapp.messenger.logging.Log;
import org.raapp.messenger.preferences.AppProtectionPreferenceFragment;
import org.raapp.messenger.push.SecurityEventListener;
import org.raapp.messenger.push.SignalServiceNetworkAccess;
import org.raapp.messenger.service.IncomingMessageObserver;
import org.raapp.messenger.service.WebRtcCallService;
import org.raapp.messenger.stickers.StickerPackPreviewRepository;
import org.raapp.messenger.stickers.StickerRemoteUriLoader;
import org.raapp.messenger.util.TextSecurePreferences;
import org.whispersystems.libsignal.util.guava.Optional;
import org.whispersystems.signalservice.api.SignalServiceAccountManager;
import org.whispersystems.signalservice.api.SignalServiceMessageReceiver;
import org.whispersystems.signalservice.api.SignalServiceMessageSender;
import org.whispersystems.signalservice.api.util.CredentialsProvider;
import org.whispersystems.signalservice.api.util.RealtimeSleepTimer;
import org.whispersystems.signalservice.api.util.SleepTimer;
import org.whispersystems.signalservice.api.util.UptimeSleepTimer;
import org.whispersystems.signalservice.api.websocket.ConnectivityListener;

import dagger.Module;
import dagger.Provides;

@Module(complete = false, injects = {CleanPreKeysJob.class,
                                     CreateSignedPreKeyJob.class,
                                     PushGroupSendJob.class,
                                     PushTextSendJob.class,
                                     PushMediaSendJob.class,
                                     AttachmentDownloadJob.class,
                                     RefreshPreKeysJob.class,
                                     IncomingMessageObserver.class,
                                     PushNotificationReceiveJob.class,
                                     MultiDeviceContactUpdateJob.class,
                                     MultiDeviceGroupUpdateJob.class,
                                     MultiDeviceReadUpdateJob.class,
                                     MultiDeviceBlockedUpdateJob.class,
                                     DeviceListFragment.class,
                                     RefreshAttributesJob.class,
                                     FcmRefreshJob.class,
                                     RequestGroupInfoJob.class,
                                     PushGroupUpdateJob.class,
                                     AvatarDownloadJob.class,
                                     RotateSignedPreKeyJob.class,
                                     WebRtcCallService.class,
                                     RetrieveProfileJob.class,
                                     MultiDeviceVerifiedUpdateJob.class,
                                     CreateProfileActivity.class,
                                     RetrieveProfileAvatarJob.class,
                                     MultiDeviceProfileKeyUpdateJob.class,
                                     SendReadReceiptJob.class,
                                     AppProtectionPreferenceFragment.class,
                                     FcmService.class,
                                     RotateCertificateJob.class,
                                     SendDeliveryReceiptJob.class,
                                     RotateProfileKeyJob.class,
                                     MultiDeviceConfigurationUpdateJob.class,
                                     RefreshUnidentifiedDeliveryAbilityJob.class,
                                     TypingSendJob.class,
                                     AttachmentUploadJob.class,
                                     StickerDownloadJob.class,
                                     StickerPackPreviewRepository.class,
                                     StickerRemoteUriLoader.Factory.class,
                                     StickerPackDownloadJob.class,
                                     MultiDeviceStickerPackOperationJob.class,
                                     MultiDeviceStickerPackSyncJob.class,
                                     LinkPreviewRepository.class,
                                     FcmJobService.class})
public class SignalCommunicationModule {

  private static final String TAG = SignalCommunicationModule.class.getSimpleName();

  private final Context                      context;
  private final SignalServiceNetworkAccess   networkAccess;

  private SignalServiceAccountManager  accountManager;
  private SignalServiceMessageSender   messageSender;
  private SignalServiceMessageReceiver messageReceiver;

  public SignalCommunicationModule(Context context, SignalServiceNetworkAccess networkAccess) {
    this.context       = context;
    this.networkAccess = networkAccess;
  }

  @Provides
  synchronized SignalServiceAccountManager provideSignalAccountManager() {
    if (this.accountManager == null) {
      this.accountManager = new SignalServiceAccountManager(networkAccess.getConfiguration(context),
                                                            new DynamicCredentialsProvider(context),
                                                            BuildConfig.USER_AGENT);
    }

    return this.accountManager;
  }

  @Provides
  synchronized SignalServiceMessageSender provideSignalMessageSender() {
    if (this.messageSender == null) {
      this.messageSender = new SignalServiceMessageSender(networkAccess.getConfiguration(context),
                                                          new DynamicCredentialsProvider(context),
                                                          new SignalProtocolStoreImpl(context),
                                                          BuildConfig.USER_AGENT,
                                                          TextSecurePreferences.isMultiDevice(context),
                                                          Optional.fromNullable(IncomingMessageObserver.getPipe()),
                                                          Optional.fromNullable(IncomingMessageObserver.getUnidentifiedPipe()),
                                                          Optional.of(new SecurityEventListener(context)));
    } else {
      this.messageSender.setMessagePipe(IncomingMessageObserver.getPipe(), IncomingMessageObserver.getUnidentifiedPipe());
      this.messageSender.setIsMultiDevice(TextSecurePreferences.isMultiDevice(context));
    }

    return this.messageSender;
  }

  @Provides
  synchronized SignalServiceMessageReceiver provideSignalMessageReceiver() {
    if (this.messageReceiver == null) {
      SleepTimer sleepTimer =  TextSecurePreferences.isFcmDisabled(context) ? new RealtimeSleepTimer(context) : new UptimeSleepTimer();

      this.messageReceiver = new SignalServiceMessageReceiver(networkAccess.getConfiguration(context),
                                                              new DynamicCredentialsProvider(context),
                                                              BuildConfig.USER_AGENT,
                                                              new PipeConnectivityListener(),
                                                              sleepTimer);
    }

    return this.messageReceiver;
  }

  @Provides
  synchronized SignalServiceNetworkAccess provideSignalServiceNetworkAccess() {
    return networkAccess;
  }

  private static class DynamicCredentialsProvider implements CredentialsProvider {

    private final Context context;

    private DynamicCredentialsProvider(Context context) {
      this.context = context.getApplicationContext();
    }

    @Override
    public String getUser() {
      return TextSecurePreferences.getLocalNumber(context);
    }

    @Override
    public String getPassword() {
      return TextSecurePreferences.getPushServerPassword(context);
    }

    @Override
    public String getSignalingKey() {
      return TextSecurePreferences.getSignalingKey(context);
    }
  }

  private class PipeConnectivityListener implements ConnectivityListener {

    @Override
    public void onConnected() {
      Log.i(TAG, "onConnected()");
    }

    @Override
    public void onConnecting() {
      Log.i(TAG, "onConnecting()");
    }

    @Override
    public void onDisconnected() {
      Log.w(TAG, "onDisconnected()");
    }

    @Override
    public void onAuthenticationFailure() {
      Log.w(TAG, "onAuthenticationFailure()");
      TextSecurePreferences.setUnauthorizedReceived(context, true);
      EventBus.getDefault().post(new ReminderUpdateEvent());
    }

  }

}
