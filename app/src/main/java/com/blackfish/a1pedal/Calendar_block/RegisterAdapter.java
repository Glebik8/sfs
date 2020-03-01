package com.blackfish.a1pedal.Calendar_block;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.blackfish.a1pedal.API.Requests;
import com.blackfish.a1pedal.ProfileInfo.User;
import com.blackfish.a1pedal.R;
import com.blackfish.a1pedal.data.Response;

import static com.blackfish.a1pedal.CalendarViewFragment.friendsInfo;
import static com.blackfish.a1pedal.Calendar_block.CalendarActivity.clickedDate;
import static com.blackfish.a1pedal.tools_class.DataApdaterFriend.currentPosition;
import static com.blackfish.a1pedal.tools_class.DataApdaterFriend.friendLists;
import static com.blackfish.a1pedal.utils.CalendarDayKt.correctDate;

public class RegisterAdapter extends RecyclerView.Adapter<RegisterAdapter.Card> {

    int hours = 8;
    int minutes = 0;
    Context context;
    boolean[] ready = new boolean[8];

    public RegisterAdapter(Context context) {
        this.context = context;
        for (int i = 0; i < 8; i++) {
            ready[i] = false;
        }
    }

    @NonNull
    @Override
    public Card onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Card(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.reg_block_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Card holder, int position) {
        Button[] array = new Button[]{holder.b1, holder.b2, holder.b3};
        for (int i = 0; i < array.length; i++) {
            if (ready[position])
                continue;
            String time = hours + ":" + f(minutes);
            array[i].setText(time);
            array[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickedDate == null || currentPosition == -1)
                        return;
                    String pk = "";
                    if (friendLists != null)
                        pk = friendLists.get(currentPosition).getPk();
                    else
                        pk = friendsInfo.get(currentPosition).getPk();
                    Requests.Companion.updateCalendar(
                            correctDate(clickedDate), time,
                            pk,  User.getInstance().getPk(), "new",
                            (Response response) -> {
                                Toast.makeText(context,
                                        "Заявка №" + (response.getPk()) + " подана",
                                        Toast.LENGTH_LONG).show();
                                return null;
                            });
                }
            });
            minutes += 15;
            hours += minutes / 60;
            minutes %= 60;
        }
        ready[position] = true;
    }

    @Override
    public int getItemCount() {
        return 8;
    }

    String f(int a) {
        if (a <= 9) {
            return "0" + a;
        }
        return String.valueOf(a);
    }

    class Card extends RecyclerView.ViewHolder {

        Button b1;
        Button b2;
        Button b3;

        public Card(@NonNull View itemView) {
            super(itemView);
            b1 = itemView.findViewById(R.id.button);
            b2 = itemView.findViewById(R.id.button2);
            b3 = itemView.findViewById(R.id.button3);
        }
    }
}
