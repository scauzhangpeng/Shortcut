package com.muugi.shortcut.sample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.content.pm.ShortcutInfoCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.muugi.shortcut.core.IntentSenderHelper;
import com.muugi.shortcut.core.ShortcutHelper;
import com.muugi.shortcut.core.ShortcutInfoHelper;
import com.muugi.shortcut.sample.base.MultipleTypeSupport;
import com.muugi.shortcut.sample.bean.Group;

import java.util.List;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

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

    private ShortcutReceiver mShortcutReceiver = new ShortcutReceiver();
    private static final String ACTION = "com.muugi.shortcut";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initRvContact();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION);
        registerReceiver(mShortcutReceiver, intentFilter);
    }

    private void initRvContact() {
        mContacts = MockData.loadFriendList();
        mContactAdapter = new ContactAdapter(mContacts,this, mMultipleTypeSupport);
        mRvContact = findViewById(R.id.rv_contact);
        mRvContact.setAdapter(mContactAdapter);
        mRvContact.setLayoutManager(new LinearLayoutManager(this, VERTICAL, false));
        mRvContact.addItemDecoration(new DividerItemDecoration(this, VERTICAL));
        mContactAdapter.setOnItemCreateClickListener(new ContactAdapter.OnItemCreateClickListener() {
            @Override
            public void onClick(View view, Group contact, Drawable drawable) {
                createShortcut(contact.getUid(),
                        contact.getNickname(),
                        contact.getNickname(),
                        contact.isShowBadged(),
                        ImageUtils.drawable2Bitmap(drawable),
                        R.drawable.hugh);
            }
        });
    }

    private void initView() {

        findViewById(R.id.btn_to_permission_v1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShortcutHelper.toPermissionSetting(MainActivity.this);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mShortcutReceiver);
    }

    private void createShortcut(String id, String label, String longLabel, boolean isBadged, Bitmap drawable, @DrawableRes int defaultDrawable) {

        ShortcutInfoHelper.Builder builder = new ShortcutInfoHelper.Builder(this, id)
                .setShortAndLongLabel(label)
                .setIcon(drawable)
                .setIntent(new Intent(this, MainActivity.class));
        if (isBadged) {
            builder.setAlwaysBadged();
        }
        ShortcutInfoCompat infoCompat = builder.build();
        IntentSender defaultIntentSender = IntentSenderHelper.getDefaultIntentSender(this, ACTION);
        ShortcutHelper.requestPinShortcut(this, infoCompat, defaultIntentSender, true, new ShortcutHelper.ShortcutCallback() {
            @Override
            public void isShortcutUpdate(boolean isSuccess) {
                Log.d(TAG, "isShortcutUpdate: 快捷方式已更新?" + isSuccess);
                Toast.makeText(getApplicationContext(), "快捷方式已更新？" + isSuccess, Toast.LENGTH_LONG).show();
            }

            @Override
            public void isShortcutCreate(boolean isSuccess) {
                Log.d(TAG, "isShortcutCreate: 快捷方式已尝试创建" + isSuccess);
                Toast.makeText(getApplicationContext(), "快捷方式已尝试创建？" + isSuccess, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static class ShortcutReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "onReceive: action = " + action);
            if (ACTION.equals(action)) {
                Log.d(TAG, "onReceive: 快捷方式创建结果广播回调");
                Toast.makeText(context,"快捷方式创建结果广播回调", Toast.LENGTH_LONG).show();
            }
        }
    }


    private Bitmap merge(Bitmap bitmap) {
        try {
            Bitmap mask = ImageUtils.drawable2Bitmap(getPackageManager().getApplicationIcon(getPackageName()));
            int width = mask.getWidth();
            int height = mask.getHeight();
            Bitmap bitmapScale = Bitmap.createScaledBitmap(bitmap, (int) (1.5 * width), (int) (1.5 * height), true);
            Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas();
            canvas.setBitmap(result);
            Paint paint = new Paint();
            canvas.drawBitmap(mask, 0, 0, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmapScale, -width / 14.0f - 40, -height / 14.0f - 40, paint);
//            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.save();
            canvas.restore();
            return result;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
