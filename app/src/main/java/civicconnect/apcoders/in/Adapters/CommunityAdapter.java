package civicconnect.apcoders.in.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import civicconnect.apcoders.in.R;
import civicconnect.apcoders.in.models.ChatModel;

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.ViewHolder> {
    Context context;
    ArrayList<ChatModel> ProblemDataList;

    public CommunityAdapter(Context context, ArrayList<ChatModel> ProblemDataList) {
        this.context = context;
        this.ProblemDataList = ProblemDataList;
    }

    @NonNull
    @Override
    public CommunityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_layout,null,true);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CommunityAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.MessagetextView.setText(ProblemDataList.get(position).getMessage());
        holder.SenderNameTextView.setText(ProblemDataList.get(position).getSenderName());
    }

    @Override
    public int getItemCount() {
        return ProblemDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView SenderNameTextView, MessagetextView;
        ImageView userprofilePic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            SenderNameTextView = itemView.findViewById(R.id.SenderName);
            userprofilePic = itemView.findViewById(R.id.userprofilePic);
            MessagetextView = itemView.findViewById(R.id.message);
        }
    }
}
