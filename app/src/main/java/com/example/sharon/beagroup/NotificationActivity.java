package com.example.sharon.beagroup;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class NotificationActivity extends AppCompatActivity {

    private TextView mNotiData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        String dataMessage = getIntent().getStringExtra("message");
        String dataFrom = getIntent().getStringExtra("from_user_id");
        String groupID = getIntent().getStringExtra("from_group_id");
        String user_id = SaveSharedPreference.getID(this);

        //mNotiData = (TextView)findViewById(R.id.noti_text);
        //mNotiData.setText(" FROM : " + dataFrom + " | MESSAGE : " + dataMessage);
        //showNewDialog(0);
        AlertDialog.Builder alertDialogBuilder =
                new AlertDialog.Builder(this)
                        .setTitle("確認邀請")
                        .setMessage("是否同意邀請")
                        .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                AddFriend addFriend = new AddFriend(NotificationActivity.this);
                                addFriend.execute(groupID, user_id);
                                PushNotification pushNotification = new PushNotification(NotificationActivity.this);
                                pushNotification.onPushBack(dataFrom);
                                Intent intent = new Intent(NotificationActivity.this, lockFriend.class);
                                startActivity(intent);
                                NotificationActivity.this.finish();


                            }
                        })
                        .setNegativeButton("拒絕", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(NotificationActivity.this, lockFriend.class);
                                startActivity(intent);
                                NotificationActivity.this.finish();
                            }
                        });

        // Show the AlertDialog.
        AlertDialog alertDialog = alertDialogBuilder.show();
    }
    public void showNewDialog(int id) {
        // TODO : Code to show the new dialog
        Context context = App.getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(NotificationActivity.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle("邀請確認")
                .setMessage("是否同意邀請")
                .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNegativeButton("拒絕", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }

}
