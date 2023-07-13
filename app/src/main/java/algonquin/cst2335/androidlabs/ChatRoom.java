package algonquin.cst2335.androidlabs;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import algonquin.cst2335.androidlabs.databinding.ActivityChatRoomBinding;

public class ChatRoom extends AppCompatActivity {
    private ActivityChatRoomBinding binding;
    private ChatRoomAdapter adapter;
    private ArrayList<ChatMessage> messages;
    private ChatRoomViewModel chatRoomViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chatRoomViewModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatRoomViewModel.messages.getValue();
        if (messages == null) {
            chatRoomViewModel.messages.setValue(new ArrayList<ChatMessage>());
        }
        messages = chatRoomViewModel.messages.getValue();

        binding.rvList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatRoomAdapter(messages);
        binding.rvList.setAdapter(adapter);

        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setMessage(binding.editMessage.getText().toString());
                chatMessage.isSentButton = true;
                chatMessage.setTimeSent(getDateTime());
                messages.add(chatMessage);
                binding.editMessage.setText("");
                adapter.notifyItemInserted(messages.size() - 1);
            }
        });

        binding.btnReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setMessage(binding.editMessage.getText().toString());
                chatMessage.isSentButton = false;
                chatMessage.setTimeSent(getDateTime());
                messages.add(chatMessage);
                binding.editMessage.setText("");
                adapter.notifyItemInserted(messages.size() - 1);
            }
        });
    }

    private String getDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }
}