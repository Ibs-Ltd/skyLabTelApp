package tel.skylab.skylabtel.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import tel.skylab.skylabtel.R;
import tel.skylab.skylabtel.models.MessageModel;
import tel.skylab.skylabtel.ui.activities.ViewMessageActivity;

/** Created by faizan on 18/10/19. */
public class AdapterChatMessage extends RecyclerView.Adapter<AdapterChatMessage.ViewHolder> {
    private Context mContext;
    private List<MessageModel> mData = new ArrayList<>();

    public AdapterChatMessage(Context mContext, List<MessageModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public AdapterChatMessage.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.message_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterChatMessage.ViewHolder holder, int position) {

        MessageModel Info = mData.get(holder.getAdapterPosition());

        holder.from_name.setText(Info.getFrom());
        holder.msg_main.setText(Info.getMessage());
        holder.date.setText(Info.getCreatedAt());

        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ViewMessageActivity.class);
                        intent.putExtra("EXTRA_FROM", Info.getFrom());
                        intent.putExtra("EXTRA_MSG", Info.getMessage());
                        mContext.startActivity(intent);
                    }
                });
    }

    @Override
    public int getItemCount() {
        //        return 12;
        return mData != null ? mData.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView from_name, msg_main, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            from_name = itemView.findViewById(R.id.from_name);
            msg_main = itemView.findViewById(R.id.msg_main);
            date = itemView.findViewById(R.id.date);
        }
    }
}
