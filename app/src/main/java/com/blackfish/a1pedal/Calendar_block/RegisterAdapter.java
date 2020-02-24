package com.blackfish.a1pedal.Calendar_block;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.blackfish.a1pedal.R;

public class RegisterAdapter extends RecyclerView.Adapter<RegisterAdapter.Card> {

    int hours = 8;
    int minutes = 0;
    boolean[] ready = new boolean[8];

    public RegisterAdapter() {
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
            array[i].setText(hours + ":" + f(minutes));
            minutes += 15;
            hours += minutes / 60;
            minutes %= 60;
            array[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
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
