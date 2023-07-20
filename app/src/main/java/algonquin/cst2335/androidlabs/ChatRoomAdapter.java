package algonquin.cst2335.androidlabs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Dao;
import androidx.room.Room;



import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.androidlabs.databinding.ChatOtherLayoutBinding;
import algonquin.cst2335.androidlabs.databinding.ChatOwnLayoutBinding;


public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.MyRowHolder> {
    private ArrayList<ChatMessage> messages;
    private Context context;
    private ChatMessageDAO mDAO;


    ChatRoomAdapter(Context context,ArrayList<ChatMessage> messages) {
        this.messages = messages;
        this.context=context;
        MessageDatabase db = Room.databaseBuilder(context.getApplicationContext(), MessageDatabase.class, "database-name").allowMainThreadQueries().build();
         mDAO = db.cmDAO();
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

     class MyRowHolder extends RecyclerView.ViewHolder {
        private TextView messageText, timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.messageText);
            timeText = itemView.findViewById(R.id.timeText);
            itemView.setOnClickListener(clk->{
                int position = getAbsoluteAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder( context );

                builder.setMessage("Do you want to delete the message: "+messageText.getText())
                        .setTitle("Question:")
                        .setNegativeButton("No", (dialog,cl)->{})
                        .setPositiveButton("Yes", (dialog,cl)->{

                            ChatMessage removedMessage = messages.get(position);
                            mDAO.deleteMessage(removedMessage.id);//add to database;
                            messages.remove(removedMessage);
                            notifyItemRemoved(position);
                            Snackbar.make(messageText,"You deleted message #"+position, Snackbar.LENGTH_LONG)
                                    .setAction("Undo",click->{
                                        messages.add(position, removedMessage);
                                        notifyItemInserted(position);
                                        mDAO.insertMessage(removedMessage);

                                    })
                                    .show();
                        })
                        .create().show();
            });
        }
    }
}
