package tel.skylab.skylabtel.roomDatabase;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import tel.skylab.skylabtel.roomDatabase.dao.MessageDao;
import tel.skylab.skylabtel.roomDatabase.dao.RecentCallsDao;
import tel.skylab.skylabtel.roomDatabase.entity.MessagesEntity;
import tel.skylab.skylabtel.roomDatabase.entity.RecentCallsEntity;

@Database(
        entities = {RecentCallsEntity.class, MessagesEntity.class},
        version = 2)
public abstract class SkylabRoomDatabase extends RoomDatabase {

    private static SkylabRoomDatabase INSTANCE;

    public abstract RecentCallsDao recentCallsDao();

    public abstract MessageDao messageDao();

    public static SkylabRoomDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    SkylabRoomDatabase.class,
                                    "skylab-database")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an
                            // example.
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
