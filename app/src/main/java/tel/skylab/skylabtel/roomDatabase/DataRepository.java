package tel.skylab.skylabtel.roomDatabase;

import androidx.lifecycle.LiveData;
import java.util.List;
import tel.skylab.skylabtel.roomDatabase.dao.MessageDao;
import tel.skylab.skylabtel.roomDatabase.dao.RecentCallsDao;
import tel.skylab.skylabtel.roomDatabase.entity.MessagesEntity;
import tel.skylab.skylabtel.roomDatabase.entity.RecentCallsEntity;

public class DataRepository {

    private MessageDao messageDao;
    private RecentCallsDao recentCallsDao;
    private LiveData<List<MessagesEntity>> mAllmsgs;
    private LiveData<List<RecentCallsEntity>> mAllCalls;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    /*public DataRepository(Application application) {
        SkylabRoomDatabase db = SkylabRoomDatabase.getDatabase(application);
        messageDao = db.messageDao();
        recentCallsDao = db.recentCallsDao();
        mAllmsgs = (LiveData<List<MessagesEntity>>) messageDao.getAlphabetizedMsgs();
        mAllCalls = (LiveData<List<RecentCallsEntity>>) recentCallsDao.getAlphabetizedWords();
    }*/

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<MessagesEntity>> getmAllmsgs() {
        return mAllmsgs;
    }

    public LiveData<List<RecentCallsEntity>> getmAllCalls() {
        return mAllCalls;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    /*public void insertMsgs(MessagesEntity messagesEntity) {
        SkylabRoomDatabase.databaseWriteExecutor.execute(
                () -> {
                    messageDao.insert(messagesEntity);
                });
    }

    public void insertCalls(RecentCallsEntity recentCallsEntity) {
        SkylabRoomDatabase.databaseWriteExecutor.execute(
                () -> {
                    recentCallsDao.insert(recentCallsEntity);
                });
    }*/
}
