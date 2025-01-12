package civicconnect.apcoders.in;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import civicconnect.apcoders.in.Adapters.CommunityAdapter;
import civicconnect.apcoders.in.models.ChatModel;

public class CommunityActivity extends AppCompatActivity {
    RecyclerView communityChattingRecyclerView;
    ArrayList<ChatModel> chatModels = new ArrayList<>();
    EditText messageEdittext;
    ImageView AddMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_community);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        AddMessage = findViewById(R.id.addMessage);
        messageEdittext = findViewById(R.id.message);

        chatModels.add(new ChatModel("Atul Dubal", "Hello Community"));
        chatModels.add(new ChatModel("Omkar Gajaapti", "Hello Community"));
        chatModels.add(new ChatModel("Atul Dubal", "I want to ask something"));
        chatModels.add(new ChatModel("Omkar Gajaapti", "Hello Community"));
        chatModels.add(new ChatModel("Amit More", "Hello Community"));


        communityChattingRecyclerView = findViewById(R.id.communityChattingRecyclerView);
        communityChattingRecyclerView.setHasFixedSize(true);
        communityChattingRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        CommunityAdapter adapter = new CommunityAdapter(CommunityActivity.this, chatModels);
        communityChattingRecyclerView.setAdapter(adapter);

        AddMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("share_prefs", MODE_PRIVATE);
                String UserFullName = sharedPreferences.getString("UserFullName", "UnKnown");

                chatModels.add(new ChatModel(UserFullName, messageEdittext.getText().toString()));
                adapter.notifyDataSetChanged();
            }
        });


    }
}