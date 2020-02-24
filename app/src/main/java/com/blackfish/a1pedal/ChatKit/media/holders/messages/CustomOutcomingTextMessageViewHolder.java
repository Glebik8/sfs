package com.blackfish.a1pedal.ChatKit.media.holders.messages;

import android.view.View;
import android.widget.TextView;

import com.blackfish.a1pedal.ChatKit.model.Message;
import com.blackfish.a1pedal.R;
import com.stfalcon.chatkit.messages.MessageHolders;

public class CustomOutcomingTextMessageViewHolder
        extends MessageHolders.OutcomingTextMessageViewHolder<Message> {

    public CustomOutcomingTextMessageViewHolder(View itemView, Object payload) {
        super(itemView, payload);
    }

    @Override
    public void onBind(Message message) {
        super.onBind(message);
    TextView messageTime= itemView.findViewById(R.id.messageTime);
      String k =  message.getStatus() + " " + time.getText();
        messageTime.setText(k);

    }
}
