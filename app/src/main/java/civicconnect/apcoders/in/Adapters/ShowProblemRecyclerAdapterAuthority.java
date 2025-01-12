package civicconnect.apcoders.in.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import civicconnect.apcoders.in.R;
import civicconnect.apcoders.in.Utils.ProblemManagement;
import civicconnect.apcoders.in.models.ProblemModel;
import es.dmoral.toasty.Toasty;

public class ShowProblemRecyclerAdapterAuthority extends RecyclerView.Adapter<ShowProblemRecyclerAdapterAuthority.ViewHolder> {
    Context context;
    ArrayList<ProblemModel> ProblemDataList;
    String[] status = {"Select State", "Resolved", " Pending", "UnResolved"};
    String SelectedStatus;

    public ShowProblemRecyclerAdapterAuthority(Context context, ArrayList<ProblemModel> ProblemDataList) {
        this.context = context;
        this.ProblemDataList = ProblemDataList;
    }

    @NonNull
    @Override
    public ShowProblemRecyclerAdapterAuthority.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = View.inflate(context, R.layout.problem_layout_authority, null);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ShowProblemRecyclerAdapterAuthority.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.ProblemStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = LayoutInflater.from(context).inflate(R.layout.change_status_layout, null, false);
                builder.setView(view);

                Spinner changeStatusSpinner = view.findViewById(R.id.changeStatusSpinner);
                Button changeStatusBtn = view.findViewById(R.id.changeStatusBtn);

                changeStatusBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProblemManagement.updateProblemStatus(ProblemDataList.get(position).getProblemId(), SelectedStatus, new ProblemManagement.ProblemStatusUpdateCallback() {

                            @Override
                            public void onStatusUpdated(boolean success, String message) {
                                if (success) {
                                    Toasty.success(context, "Status updated", Toasty.LENGTH_LONG).show();
                                    notifyDataSetChanged();
                                    ProblemDataList.remove(ProblemDataList.get(position));
                                } else {
                                    Toasty.success(context, "Status Not updated", Toasty.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
                ArrayAdapter adapter = new ArrayAdapter<>(context, R.layout.dropdown_layout, status);

                changeStatusSpinner.setAdapter(adapter);

                // Optionally, you can set an item selected listener
                changeStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        SelectedStatus = (String) parent.getItemAtPosition(position);
                        // Handle the selected item
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Handle the case when nothing is selected
                        SelectedStatus = "Select State";
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        holder.ProblemName.setText(ProblemDataList.get(position).getProblemName());
        holder.ProblemDescription.setText(ProblemDataList.get(position).getProblemDescription());
        holder.ProblemStatus.setText("Status : " + ProblemDataList.get(position).getStatus());
        holder.ProblemVotes.setText("Votes : " + ProblemDataList.get(position).getUpvotes());
        holder.ProblemReporterName.setText(ProblemDataList.get(position).getUserId());
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
        ImageView ProblemImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ProblemImage = itemView.findViewById(R.id.ProblemImageView);
            ProblemName = itemView.findViewById(R.id.ProblemNameTextView);
            ProblemDescription = itemView.findViewById(R.id.ProblemDescriptionTextView);
            ProblemStatus = itemView.findViewById(R.id.ProblemStatusTextView);
            ProblemVotes = itemView.findViewById(R.id.ProblemVotesTextView);
            ProblemReporterName = itemView.findViewById(R.id.ProblemReporterNameTextView);
        }
    }
}
