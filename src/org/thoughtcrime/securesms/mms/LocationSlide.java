package org.raapp.messenger.mms;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;

import org.raapp.messenger.components.location.SignalPlace;
import org.whispersystems.libsignal.util.guava.Optional;

public class LocationSlide extends ImageSlide {

  @NonNull
  private final SignalPlace place;

  public LocationSlide(@NonNull  Context context, @NonNull  Uri uri, long size, @NonNull SignalPlace place)
  {
    super(context, uri, size, 0, 0);
    this.place = place;
  }

  @Override
  @NonNull
  public Optional<String> getBody() {
    return Optional.of(place.getDescription());
  }

  @NonNull
  public SignalPlace getPlace() {
    return place;
  }

  @Override
  public boolean hasLocation() {
    return true;
  }

}
