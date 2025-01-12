package civicconnect.apcoders.in.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import civicconnect.apcoders.in.R;
import civicconnect.apcoders.in.Utils.ProblemManagement;
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


        if (!FirebaseAuth.getInstance().getCurrentUser().getUid().equals(ProblemDataList.get(position).getUserId())) {
            holder.upVoteBtn.setVisibility(View.VISIBLE);
            ProblemManagement.HasUserUpvoted(ProblemDataList.get(position).getProblemId(), new ProblemManagement.UserUpvoteCallback() {
                @SuppressLint("UseCompatLoadingForDrawables")
                @Override
                public void onCheckCompleted(boolean hasUpvoted, String documentId) {
                    if (hasUpvoted) {
                        holder.upVoteBtn.setImageDrawable(context.getDrawable(R.drawable.like_on));
                    } else {
                        holder.upVoteBtn.setImageDrawable(context.getDrawable(R.drawable.like_off));
                    }
                }
            });
            holder.upVoteBtn.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("UseCompatLoadingForDrawables")
                @Override
                public void onClick(View view) {
                    ProblemManagement.HasUserUpvoted(ProblemDataList.get(position).getProblemId(), new ProblemManagement.UserUpvoteCallback() {
                        @SuppressLint("UseCompatLoadingForDrawables")
                        @Override
                        public void onCheckCompleted(boolean hasUpvoted, String documentId) {
                            if (hasUpvoted) {
                                ProblemManagement.RemoveUpVoteProblem(ProblemDataList.get(position).getProblemId(), new ProblemManagement.UserUpvoteCallback() {
                                    @Override
                                    public void onCheckCompleted(boolean isUpVoteRemove, String documentId) {
                                        if (isUpVoteRemove) {
                                            Toasty.success(context, "UPVote Cancel", Toasty.LENGTH_LONG).show();
                                            holder.upVoteBtn.setImageDrawable(context.getDrawable(R.drawable.like_off));
                                        } else {
                                            Toasty.error(context, "Internet Problem", Toasty.LENGTH_LONG).show();
                                            holder.upVoteBtn.setImageDrawable(context.getDrawable(R.drawable.like_on));
                                        }
                                    }
                                });

                            } else {
                                ProblemManagement.UpVoteProblem(ProblemDataList.get(position).getProblemId(), new ProblemManagement.UserUpvoteCallback() {
                                    @Override
                                    public void onCheckCompleted(boolean isUpVoteDone, String documentId) {
                                        if (isUpVoteDone) {
                                            Toasty.success(context, "UPVote", Toasty.LENGTH_LONG).show();
                                            holder.upVoteBtn.setImageDrawable(context.getDrawable(R.drawable.like_on));
                                        } else {
                                            Toasty.error(context, "Internet Problem", Toasty.LENGTH_LONG).show();
                                            holder.upVoteBtn.setImageDrawable(context.getDrawable(R.drawable.like_off));
                                        }
                                    }
                                });

                            }
                        }
                    });

                }
            });
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("share_prefs", Context.MODE_PRIVATE);
        String UserFullName = sharedPreferences.getString("UserFullName", "Atul Dubal");
        holder.ProblemName.setText(ProblemDataList.get(position).getProblemName());
        holder.ProblemDescription.setText(ProblemDataList.get(position).getProblemDescription());
        holder.ProblemStatus.setText("Status : " + ProblemDataList.get(position).getStatus());
        holder.ProblemVotes.setText("Votes : " + ProblemDataList.get(position).getUpvotes());
        holder.ProblemReporterName.setText("Report Sender name : "+UserFullName);
        ProblemManagement.displayImageFromBase64(ProblemDataList.get(position).getPhotoUrl(), holder.ProblemImage);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toasty.success(context, ProblemDataList.get(position).getProblemName(), Toast.LENGTH_SHORT, true).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return ProblemDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ProblemName, ProblemDescription, ProblemStatus, ProblemVotes, ProblemReporterName;
        ImageView ProblemImage, upVoteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            upVoteBtn = itemView.findViewById(R.id.upVoteBtn);
            ProblemImage = itemView.findViewById(R.id.ProblemImageView);
            ProblemName = itemView.findViewById(R.id.ProblemNameTextView);
            ProblemDescription = itemView.findViewById(R.id.ProblemDescriptionTextView);
            ProblemStatus = itemView.findViewById(R.id.ProblemStatusTextView);
            ProblemVotes = itemView.findViewById(R.id.ProblemVotesTextView);
            ProblemReporterName = itemView.findViewById(R.id.ProblemReporterNameTextView);
        }
    }
}
