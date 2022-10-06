package tel.skylab.skylabtel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import tel.skylab.skylabtel.R;
import tel.skylab.skylabtel.linphone.LinphoneManager;
import tel.skylab.skylabtel.roomDatabase.entity.RecentCallsEntity;
import tel.skylab.skylabtel.roomDatabase.models.RecentCallModel;

public class AdapterRecentCalls extends RecyclerView.Adapter<AdapterRecentCalls.BookedViewHolder> {
    private Context mContext;
    private List<RecentCallModel> recentCallsEntities;

    public AdapterRecentCalls(Context mContext, List<RecentCallModel> recentCallsEntities) {
        this.mContext = mContext;
        this.recentCallsEntities = recentCallsEntities;
    }

    @NonNull
    @Override
    public BookedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookedViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.recent_call_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final BookedViewHolder holder, int position) {
        RecentCallModel Info = recentCallsEntities.get(position);

        holder.name_user.setText(Info.getName());
        holder.number_user.setText(Info.getNumber());
        holder.call_time.setText(Info.getDate());

        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinphoneManager.getCallManager()
                                .newOutgoingCall(Info.getNumber(), Info.getName());
                    }
                });
    }

    @Override
    public int getItemCount() {
        return recentCallsEntities != null ? recentCallsEntities.size() : 0;
    }

    public class BookedViewHolder extends RecyclerView.ViewHolder {
        private TextView name_user, number_user, call_time;

        public BookedViewHolder(@NonNull View itemView) {
            super(itemView);

            name_user = itemView.findViewById(R.id.name_user);
            number_user = itemView.findViewById(R.id.number_user);
            call_time = itemView.findViewById(R.id.call_time);
        }
    }

    public void setCalls(List<RecentCallsEntity> recentCallsEntities) {
        recentCallsEntities = recentCallsEntities;
        notifyDataSetChanged();
    }
}
