package com.muugi.shortcut.sample;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.muugi.shortcut.Shortcut;
import com.muugi.shortcut.sample.base.MultipleTypeSupport;
import com.muugi.shortcut.sample.bean.Group;

import java.lang.reflect.Field;
import java.util.List;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView mRvContact;
    private ContactAdapter mContactAdapter;
    private List<Group> mContacts;

    private MultipleTypeSupport<Group> mMultipleTypeSupport = new MultipleTypeSupport<Group>() {
        @Override
        public int getLayoutId(List<Group> list, Group group, int position) {
            if (group.getPosition() != -1) {
                return R.layout.item_title;
            }
            return R.layout.item_contact;
        }
    };

    private static final String ACTION = "com.muugi.shortcut";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initRvContact();
    }

    private void initRvContact() {
        mContacts = MockData.loadFriendList();
        mContactAdapter = new ContactAdapter(mContacts,this, mMultipleTypeSupport);
        mRvContact = findViewById(R.id.rv_contact);
        mRvContact.setAdapter(mContactAdapter);
        mRvContact.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mRvContact.addItemDecoration(new DividerItemDecoration(this, VERTICAL));
        mContactAdapter.setOnItemCreateClickListener(new ContactAdapter.OnItemCreateClickListener() {
            @Override
            public void onClick(View view, Group contact, Drawable drawable) {
                Shortcut.get()
                        .pin(MainActivity.this)
                        .info(contact.getUid())
                        .setAlwaysBadge()
                        .setIcon(drawable)
                        .setShortLabel(contact.getNickname())
                        .setLongLabel(contact.getNickname())
                        .setDisabledMessage(contact.getNickname())
                        .updateIfExist(true)
                        .createWithSameName(true)
                        .iconShapeWithLauncher(true)
                        .setIntent(MainActivity.class)
                        .onCreated(result -> {
                            Log.d(TAG, "onCreated: " + result);
                            Toast.makeText(MainActivity.this.getApplicationContext(),"onCreated", Toast.LENGTH_LONG).show();
                        })
                        .onAsyncCreate(result -> {
                            Log.d(TAG, "onAsyncCreate: " + result);
                            Toast.makeText(MainActivity.this.getApplicationContext(),"onAsyncCreate", Toast.LENGTH_LONG).show();
                        })
                        .onUpdated(result -> {
                            Log.d(TAG, "onUpdated: " + result);
                            Toast.makeText(MainActivity.this.getApplicationContext(),"onUpdated", Toast.LENGTH_LONG).show();
                        })
                        .onAutoCreate(result -> {
                            Log.d(TAG, "onAutoCreate: " + result);
                            Toast.makeText(MainActivity.this.getApplicationContext(),"onAutoCreate", Toast.LENGTH_LONG).show();
                        })
                        .onAsyncAutoCreate(result -> {
                            Log.d(TAG, "onAsyncAutoCreate: " + result);
                            Toast.makeText(MainActivity.this.getApplicationContext(),"onAsyncAutoCreate", Toast.LENGTH_LONG).show();
                        })
                        .start();


            }
        });
    }

    private void initView() {

        findViewById(R.id.btn_to_permission_v1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Shortcut.get()
                        .setting(MainActivity.this)
                        .start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fixInputMethodManagerLeak(this);
        Shortcut.get().release();
    }
    public static void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView", "mLastSrvView"};
        Field f = null;
        Object obj_get = null;
        for (int i = 0; i < arr.length; i++) {
            String param = arr[i];
            try {
                f = imm.getClass().getDeclaredField(param);
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
                obj_get = f.get(imm);
                if (obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    if (v_get.getContext() == destContext || param.equals("mLastSrvView")) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
