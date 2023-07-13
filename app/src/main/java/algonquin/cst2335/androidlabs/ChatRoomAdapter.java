package algonquin.cst2335.androidlabs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import algonquin.cst2335.androidlabs.databinding.ChatOtherLayoutBinding;
import algonquin.cst2335.androidlabs.databinding.ChatOwnLayoutBinding;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.MyRowHolder> {
    private ArrayList<ChatMessage> messages;

    ChatRoomAdapter(ArrayList<ChatMessage> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            ChatOwnLayoutBinding binding = ChatOwnLayoutBinding.bind(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_own_layout, parent, false));
            return new MyRowHolder(binding.getRoot());
        } else {
            ChatOtherLayoutBinding binding = ChatOtherLayoutBinding.bind(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_other_layout, parent, false));
            return new MyRowHolder(binding.getRoot());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
        holder.messageText.setText(messages.get(position).getMessage());
        holder.timeText.setText(messages.get(position).getTimeSent());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).isSentButton == true ? 0 : 1;
    }

    static class MyRowHolder extends RecyclerView.ViewHolder {
        private TextView messageText, timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
            timeText = itemView.findViewById(R.id.timeText);
        }
    }
}
