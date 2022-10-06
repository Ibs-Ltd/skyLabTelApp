package tel.skylab.skylabtel.roomDatabase.models;

import androidx.lifecycle.LiveData;
import java.util.List;
import tel.skylab.skylabtel.roomDatabase.entity.MessagesEntity;

public class MessagesViewModel /*extends AndroidViewModel*/ {

    //    private DataRepository mRepository;

    private LiveData<List<MessagesEntity>> mAllRecentMessages;

    /* public MessagesViewModel(Application application) {
        super(application);
        mRepository = new DataRepository(application);
        mAllRecentMessages = mRepository.getmAllmsgs();
    }*/

    LiveData<List<MessagesEntity>> getmAllRecentMessages() {
        return mAllRecentMessages;
    }

    /* public void insert(MessagesEntity messagesEntity) {
        mRepository.insertMsgs(messagesEntity);
    }*/
}
