package tel.skylab.skylabtel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import tel.skylab.skylabtel.R;
import tel.skylab.skylabtel.models.RecentCallsModel;

public class AdapterRecentChats extends RecyclerView.Adapter<AdapterRecentChats.BookedViewHolder> {
    private Context mContext;
    ArrayList<RecentCallsModel> mData;
    private CallbackInterface callbackInterface;

    public AdapterRecentChats(
            Context mContext,
            ArrayList<RecentCallsModel> mData,
            CallbackInterface callbackInterface) {
        this.mContext = mContext;
        this.mData = mData;
        this.callbackInterface = callbackInterface;
    }

    @NonNull
    @Override
    public BookedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookedViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.recent_chat_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final BookedViewHolder holder, int position) {

        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callbackInterface.callBack();
                    }
                });
    }

    @Override
    public int getItemCount() {
        //        return mData != null ? mData.size() : 0;
        return 12;
    }

    public class BookedViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_set;

        public BookedViewHolder(@NonNull View itemView) {
            super(itemView);
            //            img_set = itemView.findViewById(R.id.img_set);

        }
    }

    public interface CallbackInterface {
        void callBack();
    }
}
