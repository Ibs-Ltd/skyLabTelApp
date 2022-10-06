package tel.skylab.skylabtel.roomDatabase.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
import tel.skylab.skylabtel.models.MessageModel;
import tel.skylab.skylabtel.roomDatabase.entity.MessagesEntity;

@Dao
public interface MessageDao {

    @Insert
    void insert(MessagesEntity newsArticlesDbDataList);

    @Query("DELETE FROM received_messages_table")
    void deleteAll();

    @Query("SELECT * FROM received_messages_table ORDER BY id DESC")
    List<MessageModel> getAll();
}
