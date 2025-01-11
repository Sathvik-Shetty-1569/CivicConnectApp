package civicconnect.apcoders.in.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import civicconnect.apcoders.in.R;
import civicconnect.apcoders.in.models.ProblemModel;
import es.dmoral.toasty.Toasty;

public class ShowProblemRecyclerViewAdapter extends RecyclerView.Adapter<ShowProblemRecyclerViewAdapter.ViewHolder> {
    Context context;
    ArrayList<ProblemModel> ProblemDataList;

    public ShowProblemRecyclerViewAdapter(Context context, ArrayList<ProblemModel> ProblemDataList) {
        this.context = context;
        this.ProblemDataList = ProblemDataList;
    }

    @NonNull
    @Override
    public ShowProblemRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.problem_recyclerview_layout, null);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ShowProblemRecyclerViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.ProblemName.setText(ProblemDataList.get(position).getProblemName());
        holder.ProblemDescription.setText(ProblemDataList.get(position).getProblemDescription());
        holder.ProblemStatus.setText("Status : " + ProblemDataList.get(position).getStatus());
        holder.ProblemVotes.setText("Votes : " + ProblemDataList.get(position).getUpvotes());
        holder.ProblemReporterName.setText(ProblemDataList.get(position).getUserId());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.success(context, ProblemDataList.get(position).getProblemName(), Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return ProblemDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ProblemName, ProblemDescription, ProblemStatus, ProblemVotes, ProblemReporterName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ProblemName = itemView.findViewById(R.id.ProblemNameTextView);
            ProblemDescription = itemView.findViewById(R.id.ProblemDescriptionTextView);
            ProblemStatus = itemView.findViewById(R.id.ProblemStatusTextView);
            ProblemVotes = itemView.findViewById(R.id.ProblemVotesTextView);
            ProblemReporterName = itemView.findViewById(R.id.ProblemReporterNameTextView);
        }
    }
}
