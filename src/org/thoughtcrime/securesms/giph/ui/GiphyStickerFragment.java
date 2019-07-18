package org.bittube.messenger.giph.ui;


import android.os.Bundle;
import android.support.v4.content.Loader;

import org.bittube.messenger.giph.model.GiphyImage;
import org.bittube.messenger.giph.net.GiphyStickerLoader;

import java.util.List;

public class GiphyStickerFragment extends GiphyFragment {
  @Override
  public Loader<List<GiphyImage>> onCreateLoader(int id, Bundle args) {
    return new GiphyStickerLoader(getActivity(), searchString);
  }
}
