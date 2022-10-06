package tel.skylab.skylabtel.roomDatabase.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
import tel.skylab.skylabtel.roomDatabase.entity.RecentCallsEntity;
import tel.skylab.skylabtel.roomDatabase.models.RecentCallModel;

@Dao
public interface RecentCallsDao {

    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert /*(onConflict = OnConflictStrategy.IGNORE)*/
    void insert(RecentCallsEntity newsArticlesDbDataList);

    @Query("DELETE FROM recent_calls_table")
    void deleteAll();

    @Query("SELECT * FROM recent_calls_table ORDER BY id DESC")
    List<RecentCallModel> getAll();

    /* @Query("SELECT * from recent_calls_table ORDER BY id ASC")
    List<RecentCallModel> getAlphabetizedWords();*/
}
