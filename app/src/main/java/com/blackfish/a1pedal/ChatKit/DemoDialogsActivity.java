package com.blackfish.a1pedal.ChatKit;

import android.os.Bundle;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.blackfish.a1pedal.ChatKit.model.Dialog;
import com.blackfish.a1pedal.ChatKit.utils.AppUtils;
import com.blackfish.a1pedal.R;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

/*
 * Created by troy379 on 05.04.17.
 */
public abstract class DemoDialogsActivity extends Fragment
        implements DialogsListAdapter.OnDialogClickListener<Dialog>,
        DialogsListAdapter.OnDialogLongClickListener<Dialog> {

    protected ImageLoader imageLoader;
    protected DialogsListAdapter<Dialog> dialogsAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url, Object payload) {
             if (!url.equals("")){
             //   Picasso.get().load(url).into(imageView);
                 }
            }
        };
    }

    @Override
    public void onDialogLongClick(Dialog dialog) {
        AppUtils.showToast(
                getActivity(),
                dialog.getDialogName(),
                false);
    }

    public abstract void onBackPressed();
}
