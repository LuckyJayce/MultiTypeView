package com.shizhefei.mutitypedemo.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.shizhefei.mutitypedemo.R;
import com.shizhefei.mutitypedemo.type.Message;
import com.shizhefei.mutitypedemo.type.MessageLeftProvider;
import com.shizhefei.mutitypedemo.type.MessageRightProvider;
import com.shizhefei.view.multitype.ItemBinderFactory;
import com.shizhefei.view.multitype.ItemViewProviderSet;
import com.shizhefei.view.multitype.MultiTypeAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemBinderFactory itemBinderFactory;
    private String myUserId = "1";
    private MultiTypeAdapter multiTypeAdapter;
    private EditText messageEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));

        itemBinderFactory = new ItemBinderFactory();
        //有时候需要根据TextItem里面的某个字段，生成不同的布局.比如聊天界面的message是一样的，但是有区分左右布局
        //ItemProviderSet可以通过数据类型区分无数种情况的Provider
        itemBinderFactory.registerProvider(Message.class, new ItemViewProviderSet<Message>(new MessageLeftProvider(), new MessageRightProvider()) {
            @Override
            protected int selectIndex(Message message) {
                return myUserId.equals(message.userId) ? 1 : 0;
            }
        });

        List<Object> list = load();
        Collections.reverse(list);
        recyclerView.setAdapter(multiTypeAdapter = new MultiTypeAdapter(list, itemBinderFactory));

        messageEditText = (EditText) findViewById(R.id.message_editText);
        findViewById(R.id.send_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = messageEditText.getText().toString();
                multiTypeAdapter.getData().add(0, new Message(myUserId, text));
                multiTypeAdapter.notifyItemInserted(0);

                messageEditText.getText().clear();
                recyclerView.scrollToPosition(0);
            }
        });
    }

    @Override
    public void onStateNotSaved() {
        super.onStateNotSaved();
        FragmentManager fragmentManager;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private List<Object> load() {
        List<Object> message = new ArrayList<>();
        message.add(new Message("1", "58秒95"));
        message.add(new Message("2", "58秒95？"));
        message.add(new Message("2", "自己都没想到自己"));
        message.add(new Message("2", "我以为是59秒"));
        message.add(new Message("2", "啊～～我有这么快？？"));
        message.add(new Message("2", "我很满意"));
        message.add(new Message("1", "今天这个状态有所保留么？"));
        message.add(new Message("2", "没有保留！我已经，我已经用了洪荒之力啦！"));
        message.add(new Message("1", "是不是对明天的决赛充满希望"));
        message.add(new Message("2", "没有，我已经很满意啦。"));
        message.add(new Message("1", "明天加油"));
        message.add(new Message("2", "啦。。。啦。。。"));
        return message;
    }
}
