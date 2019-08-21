package org.bittube.messenger.dependencies;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;
import org.bittube.messenger.BuildConfig;
import org.bittube.messenger.CreateProfileActivity;
import org.bittube.messenger.DeviceListFragment;
import org.bittube.messenger.crypto.storage.SignalProtocolStoreImpl;
import org.bittube.messenger.events.ReminderUpdateEvent;
import org.bittube.messenger.gcm.FcmJobService;
import org.bittube.messenger.gcm.FcmService;
import org.bittube.messenger.jobs.AttachmentDownloadJob;
import org.bittube.messenger.jobs.AttachmentUploadJob;
import org.bittube.messenger.jobs.AvatarDownloadJob;
import org.bittube.messenger.jobs.CleanPreKeysJob;
import org.bittube.messenger.jobs.CreateSignedPreKeyJob;
import org.bittube.messenger.jobs.FcmRefreshJob;
import org.bittube.messenger.jobs.MultiDeviceBlockedUpdateJob;
import org.bittube.messenger.jobs.MultiDeviceConfigurationUpdateJob;
import org.bittube.messenger.jobs.MultiDeviceContactUpdateJob;
import org.bittube.messenger.jobs.MultiDeviceGroupUpdateJob;
import org.bittube.messenger.jobs.MultiDeviceProfileKeyUpdateJob;
import org.bittube.messenger.jobs.MultiDeviceReadUpdateJob;
import org.bittube.messenger.jobs.MultiDeviceStickerPackOperationJob;
import org.bittube.messenger.jobs.MultiDeviceStickerPackSyncJob;
import org.bittube.messenger.jobs.MultiDeviceVerifiedUpdateJob;
import org.bittube.messenger.jobs.PushGroupSendJob;
import org.bittube.messenger.jobs.PushGroupUpdateJob;
import org.bittube.messenger.jobs.PushMediaSendJob;
import org.bittube.messenger.jobs.PushNotificationReceiveJob;
import org.bittube.messenger.jobs.PushTextSendJob;
import org.bittube.messenger.jobs.RefreshAttributesJob;
import org.bittube.messenger.jobs.RefreshPreKeysJob;
import org.bittube.messenger.jobs.RefreshUnidentifiedDeliveryAbilityJob;
import org.bittube.messenger.jobs.RequestGroupInfoJob;
import org.bittube.messenger.jobs.RetrieveProfileAvatarJob;
import org.bittube.messenger.jobs.RetrieveProfileJob;
import org.bittube.messenger.jobs.RotateCertificateJob;
import org.bittube.messenger.jobs.RotateProfileKeyJob;
import org.bittube.messenger.jobs.RotateSignedPreKeyJob;
import org.bittube.messenger.jobs.SendDeliveryReceiptJob;
import org.bittube.messenger.jobs.SendReadReceiptJob;
import org.bittube.messenger.jobs.StickerDownloadJob;
import org.bittube.messenger.jobs.StickerPackDownloadJob;
import org.bittube.messenger.jobs.TypingSendJob;
import org.bittube.messenger.linkpreview.LinkPreviewRepository;
import org.bittube.messenger.logging.Log;
import org.bittube.messenger.preferences.AppProtectionPreferenceFragment;
import org.bittube.messenger.push.SecurityEventListener;
import org.bittube.messenger.push.SignalServiceNetworkAccess;
import org.bittube.messenger.service.IncomingMessageObserver;
import org.bittube.messenger.service.WebRtcCallService;
import org.bittube.messenger.stickers.StickerPackPreviewRepository;
import org.bittube.messenger.stickers.StickerRemoteUriLoader;
import org.bittube.messenger.util.TextSecurePreferences;
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
