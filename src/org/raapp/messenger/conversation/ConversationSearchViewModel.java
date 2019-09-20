package org.raapp.messenger.conversation;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import android.content.Context;
import androidx.annotation.NonNull;

import org.raapp.messenger.contacts.ContactAccessor;
import org.raapp.messenger.database.CursorList;
import org.raapp.messenger.database.DatabaseFactory;
import org.raapp.messenger.search.SearchRepository;
import org.raapp.messenger.search.model.MessageResult;
import org.raapp.messenger.util.CloseableLiveData;
import org.raapp.messenger.util.Debouncer;
import org.raapp.messenger.util.Util;
import org.raapp.messenger.util.concurrent.SignalExecutors;

import java.io.Closeable;
import java.util.List;

public class ConversationSearchViewModel extends AndroidViewModel {

  private final SearchRepository                searchRepository;
  private final CloseableLiveData<SearchResult> result;
  private final Debouncer                       debouncer;

  private boolean firstSearch;
  private boolean searchOpen;
  private String  activeQuery;
  private long    activeThreadId;

  public ConversationSearchViewModel(@NonNull Application application) {
    super(application);
    Context context = application.getApplicationContext();
    result           = new CloseableLiveData<>();
    debouncer        = new Debouncer(500);
    searchRepository = new SearchRepository(context,
                                            DatabaseFactory.getSearchDatabase(context),
                                            DatabaseFactory.getContactsDatabase(context),
                                            DatabaseFactory.getThreadDatabase(context),
                                            ContactAccessor.getInstance(),
                                            SignalExecutors.SERIAL);
  }

  LiveData<SearchResult> getSearchResults() {
    return result;
  }

  void onQueryUpdated(@NonNull String query, long threadId) {
    if (firstSearch && query.length() < 2) {
      result.postValue(new SearchResult(CursorList.emptyList(), 0));
      return;
    }

    if (query.equals(activeQuery)) {
      return;
    }

    updateQuery(query, threadId);
  }

  void onMissingResult() {
    if (activeQuery != null) {
      updateQuery(activeQuery, activeThreadId);
    }
  }

  void onMoveUp() {
    debouncer.clear();

    CursorList<MessageResult> messages = (CursorList<MessageResult>) result.getValue().getResults();
    int                       position = Math.min(result.getValue().getPosition() + 1, messages.size() - 1);

    result.setValue(new SearchResult(messages, position), false);
  }

  void onMoveDown() {
    debouncer.clear();

    CursorList<MessageResult> messages = (CursorList<MessageResult>) result.getValue().getResults();
    int                       position = Math.max(result.getValue().getPosition() - 1, 0);

    result.setValue(new SearchResult(messages, position), false);
  }


  void onSearchOpened() {
    searchOpen  = true;
    firstSearch = true;
  }

  void onSearchClosed() {
    searchOpen = false;
    debouncer.clear();
    result.close();
  }

  @Override
  protected void onCleared() {
    super.onCleared();
    result.close();
  }

  private void updateQuery(@NonNull String query, long threadId) {
    activeQuery    = query;
    activeThreadId = threadId;

    debouncer.publish(() -> {
      firstSearch = false;

      searchRepository.query(query, threadId, messages -> {
        Util.runOnMain(() -> {
          if (searchOpen && query.equals(activeQuery)) {
            result.setValue(new SearchResult(messages, 0));
          } else {
            messages.close();
          }
        });
      });
    });
  }

  static class SearchResult implements Closeable {

    private final CursorList<MessageResult> results;
    private final int                       position;

    SearchResult(CursorList<MessageResult> results, int position) {
      this.results  = results;
      this.position = position;
    }

    public List<MessageResult> getResults() {
      return results;
    }

    public int getPosition() {
      return position;
    }

    @Override
    public void close() {
      results.close();
    }
  }
}
