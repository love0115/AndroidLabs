package algonquin.cst2335.androidlabs;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
    public interface ChatMessageDAO {

        @Insert
        public long insertMessage(ChatMessage message);

        @Query("SELECT * FROM ChatMessage")
        List<ChatMessage> getAllMessages();

        @Query("DELETE FROM ChatMessage WHERE id = :messageId")
        void deleteMessage(long messageId);
    }


