package algonquin.cst2335.androidlabs;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;


import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.androidlabs.databinding.ActivityChatRoomBinding;

public class ChatRoom extends AppCompatActivity {
    private ActivityChatRoomBinding binding;
    private ChatRoomAdapter adapter;
    private ArrayList<ChatMessage> messages;
    private ChatRoomViewModel chatRoomViewModel;
    private ChatMessageDAO mDAO;
    private FragmentManager fMgr = getSupportFragmentManager();
    private FragmentTransaction tx = fMgr.beginTransaction();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
          mDAO = db.cmDAO();
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        chatRoomViewModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatRoomViewModel.messages.getValue();
        if (messages == null) {
            chatRoomViewModel.messages.setValue(new ArrayList<ChatMessage>());
        }
        messages = chatRoomViewModel.messages.getValue();
        if(messages == null)
        {
            chatRoomViewModel.messages.setValue(messages = new ArrayList<>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                messages.addAll( mDAO.getAllMessages() ); //Once you get the data from database

                runOnUiThread( () ->  binding.rvList.setAdapter( adapter )); //You can then load the RecyclerView
            });
        }

        binding.rvList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatRoomAdapter(ChatRoom.this,messages,chatRoomViewModel);

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
        chatRoomViewModel.selectedMessage.observe(this, (newMessageValue) -> {


            MessageDetailsFragment chatFragment = new MessageDetailsFragment(newMessageValue);
            chatFragment.selected.getMessage();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentLocation, chatFragment)
                    .commit();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.my_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

            if(item.getItemId()==R.id.item_1) {
                ChatMessage selectedMessage = chatRoomViewModel.selectedMessage.getValue();
                if (selectedMessage != null) {
                    int position = messages.indexOf(selectedMessage);
                    if (position != -1) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("Do you want to delete the message: " + selectedMessage.getMessage())
                                .setTitle("Question:")
                                .setPositiveButton("Yes", (dialogI, cl) -> {
                                    ChatMessage m = messages.get(position);
                                    messages.remove(position);
                                    adapter.notifyItemRemoved(position);

                                    Executor thread = Executors.newSingleThreadExecutor();
                                    thread.execute(() -> {
                                        mDAO.deleteMessage(m.id);
                                    });

                                    Snackbar.make(binding.getRoot(), "You deleted message #" + position, Snackbar.LENGTH_LONG)
                                            .setAction("Undo", snackbar -> {
                                                messages.add(position, m);
                                                adapter.notifyItemInserted(position);
                                                Executor thread1 = Executors.newSingleThreadExecutor();
                                                thread1.execute(() -> {
                                                    mDAO.insertMessage(m);
                                                });
                                            })
                                            .show();
                                })
                                .setNegativeButton("No", (dialogI, cl) -> {
                                })
                                .create()
                                .show();
                    }
                }
            }

            if(item.getItemId()==R.id.item_2)
            {
                Toast.makeText(this, "Version 1.0, created by Loveleen ", Toast.LENGTH_SHORT).show();

        }

        return true;
    }


    private String getDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    }
