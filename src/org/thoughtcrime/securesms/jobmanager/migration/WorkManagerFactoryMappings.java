package org.raapp.messenger.jobmanager.migration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.raapp.messenger.jobs.AttachmentDownloadJob;
import org.raapp.messenger.jobs.AttachmentUploadJob;
import org.raapp.messenger.jobs.AvatarDownloadJob;
import org.raapp.messenger.jobs.CleanPreKeysJob;
import org.raapp.messenger.jobs.CreateSignedPreKeyJob;
import org.raapp.messenger.jobs.DirectoryRefreshJob;
import org.raapp.messenger.jobs.FcmRefreshJob;
import org.raapp.messenger.jobs.LocalBackupJob;
import org.raapp.messenger.jobs.MmsDownloadJob;
import org.raapp.messenger.jobs.MmsReceiveJob;
import org.raapp.messenger.jobs.MmsSendJob;
import org.raapp.messenger.jobs.MultiDeviceBlockedUpdateJob;
import org.raapp.messenger.jobs.MultiDeviceConfigurationUpdateJob;
import org.raapp.messenger.jobs.MultiDeviceContactUpdateJob;
import org.raapp.messenger.jobs.MultiDeviceGroupUpdateJob;
import org.raapp.messenger.jobs.MultiDeviceProfileKeyUpdateJob;
import org.raapp.messenger.jobs.MultiDeviceReadUpdateJob;
import org.raapp.messenger.jobs.MultiDeviceVerifiedUpdateJob;
import org.raapp.messenger.jobs.PushContentReceiveJob;
import org.raapp.messenger.jobs.PushDecryptJob;
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
import org.raapp.messenger.jobs.ServiceOutageDetectionJob;
import org.raapp.messenger.jobs.SmsReceiveJob;
import org.raapp.messenger.jobs.SmsSendJob;
import org.raapp.messenger.jobs.SmsSentJob;
import org.raapp.messenger.jobs.TrimThreadJob;
import org.raapp.messenger.jobs.TypingSendJob;
import org.raapp.messenger.jobs.UpdateApkJob;

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
