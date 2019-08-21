package org.bittube.messenger.jobmanager.migration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.bittube.messenger.jobs.AttachmentDownloadJob;
import org.bittube.messenger.jobs.AttachmentUploadJob;
import org.bittube.messenger.jobs.AvatarDownloadJob;
import org.bittube.messenger.jobs.CleanPreKeysJob;
import org.bittube.messenger.jobs.CreateSignedPreKeyJob;
import org.bittube.messenger.jobs.DirectoryRefreshJob;
import org.bittube.messenger.jobs.FcmRefreshJob;
import org.bittube.messenger.jobs.LocalBackupJob;
import org.bittube.messenger.jobs.MmsDownloadJob;
import org.bittube.messenger.jobs.MmsReceiveJob;
import org.bittube.messenger.jobs.MmsSendJob;
import org.bittube.messenger.jobs.MultiDeviceBlockedUpdateJob;
import org.bittube.messenger.jobs.MultiDeviceConfigurationUpdateJob;
import org.bittube.messenger.jobs.MultiDeviceContactUpdateJob;
import org.bittube.messenger.jobs.MultiDeviceGroupUpdateJob;
import org.bittube.messenger.jobs.MultiDeviceProfileKeyUpdateJob;
import org.bittube.messenger.jobs.MultiDeviceReadUpdateJob;
import org.bittube.messenger.jobs.MultiDeviceVerifiedUpdateJob;
import org.bittube.messenger.jobs.PushContentReceiveJob;
import org.bittube.messenger.jobs.PushDecryptJob;
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
import org.bittube.messenger.jobs.ServiceOutageDetectionJob;
import org.bittube.messenger.jobs.SmsReceiveJob;
import org.bittube.messenger.jobs.SmsSendJob;
import org.bittube.messenger.jobs.SmsSentJob;
import org.bittube.messenger.jobs.TrimThreadJob;
import org.bittube.messenger.jobs.TypingSendJob;
import org.bittube.messenger.jobs.UpdateApkJob;

import java.util.HashMap;
import java.util.Map;

public class WorkManagerFactoryMappings {

  private static final Map<String, String> FACTORY_MAP = new HashMap<String, String>() {{
    put(AttachmentDownloadJob.class.getName(), AttachmentDownloadJob.KEY);
    put(AttachmentUploadJob.class.getName(), AttachmentUploadJob.KEY);
    put(AvatarDownloadJob.class.getName(), AvatarDownloadJob.KEY);
    put(CleanPreKeysJob.class.getName(), CleanPreKeysJob.KEY);
    put(CreateSignedPreKeyJob.class.getName(), CreateSignedPreKeyJob.KEY);
    put(DirectoryRefreshJob.class.getName(), DirectoryRefreshJob.KEY);
    put(FcmRefreshJob.class.getName(), FcmRefreshJob.KEY);
    put(LocalBackupJob.class.getName(), LocalBackupJob.KEY);
    put(MmsDownloadJob.class.getName(), MmsDownloadJob.KEY);
    put(MmsReceiveJob.class.getName(), MmsReceiveJob.KEY);
    put(MmsSendJob.class.getName(), MmsSendJob.KEY);
    put(MultiDeviceBlockedUpdateJob.class.getName(), MultiDeviceBlockedUpdateJob.KEY);
    put(MultiDeviceConfigurationUpdateJob.class.getName(), MultiDeviceConfigurationUpdateJob.KEY);
    put(MultiDeviceContactUpdateJob.class.getName(), MultiDeviceContactUpdateJob.KEY);
    put(MultiDeviceGroupUpdateJob.class.getName(), MultiDeviceGroupUpdateJob.KEY);
    put(MultiDeviceProfileKeyUpdateJob.class.getName(), MultiDeviceProfileKeyUpdateJob.KEY);
    put(MultiDeviceReadUpdateJob.class.getName(), MultiDeviceReadUpdateJob.KEY);
    put(MultiDeviceVerifiedUpdateJob.class.getName(), MultiDeviceVerifiedUpdateJob.KEY);
    put(PushContentReceiveJob.class.getName(), PushContentReceiveJob.KEY);
    put(PushDecryptJob.class.getName(), PushDecryptJob.KEY);
    put(PushGroupSendJob.class.getName(), PushGroupSendJob.KEY);
    put(PushGroupUpdateJob.class.getName(), PushGroupUpdateJob.KEY);
    put(PushMediaSendJob.class.getName(), PushMediaSendJob.KEY);
    put(PushNotificationReceiveJob.class.getName(), PushNotificationReceiveJob.KEY);
    put(PushTextSendJob.class.getName(), PushTextSendJob.KEY);
    put(RefreshAttributesJob.class.getName(), RefreshAttributesJob.KEY);
    put(RefreshPreKeysJob.class.getName(), RefreshPreKeysJob.KEY);
    put(RefreshUnidentifiedDeliveryAbilityJob.class.getName(), RefreshUnidentifiedDeliveryAbilityJob.KEY);
    put(RequestGroupInfoJob.class.getName(), RequestGroupInfoJob.KEY);
    put(RetrieveProfileAvatarJob.class.getName(), RetrieveProfileAvatarJob.KEY);
    put(RetrieveProfileJob.class.getName(), RetrieveProfileJob.KEY);
    put(RotateCertificateJob.class.getName(), RotateCertificateJob.KEY);
    put(RotateProfileKeyJob.class.getName(), RotateProfileKeyJob.KEY);
    put(RotateSignedPreKeyJob.class.getName(), RotateSignedPreKeyJob.KEY);
    put(SendDeliveryReceiptJob.class.getName(), SendDeliveryReceiptJob.KEY);
    put(SendReadReceiptJob.class.getName(), SendReadReceiptJob.KEY);
    put(ServiceOutageDetectionJob.class.getName(), ServiceOutageDetectionJob.KEY);
    put(SmsReceiveJob.class.getName(), SmsReceiveJob.KEY);
    put(SmsSendJob.class.getName(), SmsSendJob.KEY);
    put(SmsSentJob.class.getName(), SmsSentJob.KEY);
    put(TrimThreadJob.class.getName(), TrimThreadJob.KEY);
    put(TypingSendJob.class.getName(), TypingSendJob.KEY);
    put(UpdateApkJob.class.getName(), UpdateApkJob.KEY);
  }};

  public static @Nullable String getFactoryKey(@NonNull String workManagerClass) {
    return FACTORY_MAP.get(workManagerClass);
  }
}
