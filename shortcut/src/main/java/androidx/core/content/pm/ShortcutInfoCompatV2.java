package androidx.core.content.pm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.IconCompat;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Created by ZP on 2019-10-24.
 */
public class ShortcutInfoCompatV2 extends ShortcutInfoCompat implements Cloneable {

    private boolean mIconShapeWithLauncher = true;

    private boolean mUpdateIfExist = true;

    private boolean mAutoCreateWithSameName = true;

    @Nullable
    private Bitmap mIconBitmap;
    @Nullable
    private Drawable mIconDrawable;

    public boolean isIconShapeWithLauncher() {
        return mIconShapeWithLauncher;
    }

    public boolean isUpdateIfExist() {
        return mUpdateIfExist;
    }

    public boolean isAutoCreateWithSameName() {
        return mAutoCreateWithSameName;
    }

    public void setIconCompat(IconCompat iconCompat) {
        mIcon = iconCompat;
    }

    public void setShortLabel(CharSequence label) {
        mLabel = label;
    }

    @Nullable
    public Bitmap getIconBitmap() {
        return mIconBitmap;
    }

    @Nullable
    public Drawable getIconDrawable() {
        return mIconDrawable;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @NotNull
    @Override
    public String toString() {
        return "ShortcutInfoCompatV2{" +
                "mIconShapeWithLauncher=" + mIconShapeWithLauncher +
                ", mUpdateIfExist=" + mUpdateIfExist +
                ", mAutoCreateWithSameName=" + mAutoCreateWithSameName +
                ", mIconBitmap=" + mIconBitmap +
                ", mIconDrawable=" + mIconDrawable +
                ", mContext=" + mContext +
                ", mId='" + mId + '\'' +
                ", mIntents=" + Arrays.toString(mIntents) +
                ", mActivity=" + mActivity +
                ", mLabel=" + mLabel +
                ", mLongLabel=" + mLongLabel +
                ", mDisabledMessage=" + mDisabledMessage +
                ", mIcon=" + mIcon +
                ", mIsAlwaysBadged=" + mIsAlwaysBadged +
                ", mPersons=" + Arrays.toString(mPersons) +
                ", mCategories=" + mCategories +
                ", mIsLongLived=" + mIsLongLived +
                '}';
    }

    public static class Builder {

        final ShortcutInfoCompatV2 mInfo;

        public Builder(@NonNull Context context, @NonNull String id) {
            mInfo = new ShortcutInfoCompatV2();
            mInfo.mContext = context;
            mInfo.mId = id;
        }

        /**
         * Sets the short title of a shortcut.
         *
         * <p>This is a mandatory field when publishing a new shortcut.
         *
         * <p>This field is intended to be a concise description of a shortcut.
         *
         * <p>The recommended maximum length is 10 characters.
         */
        @NonNull
        public Builder setShortLabel(@NonNull CharSequence shortLabel) {
            mInfo.mLabel = shortLabel;
            return this;
        }

        /**
         * Sets the text of a shortcut.
         *
         * <p>This field is intended to be more descriptive than the shortcut title. The launcher
         * shows this instead of the short title when it has enough space.
         *
         * <p>The recommend maximum length is 25 characters.
         */
        @NonNull
        public Builder setLongLabel(@NonNull CharSequence longLabel) {
            mInfo.mLongLabel = longLabel;
            return this;
        }

        /**
         * Sets the message that should be shown when the user attempts to start a shortcut that
         * is disabled.
         *
         * @see ShortcutInfo#getDisabledMessage()
         */
        @NonNull
        public Builder setDisabledMessage(@NonNull CharSequence disabledMessage) {
            mInfo.mDisabledMessage = disabledMessage;
            return this;
        }

        /**
         * Sets the intent of a shortcut.  Alternatively, {@link #setIntents(Intent[])} can be used
         * to launch an activity with other activities in the back stack.
         *
         * <p>This is a mandatory field when publishing a new shortcut.
         *
         * <p>The given {@code intent} can contain extras, but these extras must contain values
         * of primitive types in order for the system to persist these values.
         */
        @NonNull
        public Builder setIntent(@NonNull Intent intent) {
            if (TextUtils.isEmpty(intent.getAction())) {
                intent.setAction(Intent.ACTION_VIEW);
            }
            return setIntents(new Intent[]{intent});
        }

        /**
         * Sets multiple intents instead of a single intent, in order to launch an activity with
         * other activities in back stack.  Use {@link android.app.TaskStackBuilder} to build
         * intents. The last element in the list represents the only intent that doesn't place
         * an activity on the back stack.
         */
        @NonNull
        public Builder setIntents(@NonNull Intent[] intents) {
            mInfo.mIntents = intents;
            return this;
        }

        /**
         * Sets an icon of a shortcut.
         */
        @NonNull
        public Builder setIcon(IconCompat icon) {
            mInfo.mIcon = icon;
            return this;
        }

        /**
         * Sets the target activity. A shortcut will be shown along with this activity's icon
         * on the launcher.
         *
         * @see ShortcutInfo#getActivity()
         * @see ShortcutInfo.Builder#setActivity(ComponentName)
         */
        @NonNull
        public Builder setActivity(@NonNull ComponentName activity) {
            mInfo.mActivity = activity;
            return this;
        }

        /**
         * Badges the icon before passing it over to the Launcher.
         * <p>
         * Launcher automatically badges {@link ShortcutInfo}, so only the legacy shortcut icon,
         * {@link Intent.ShortcutIconResource} is badged. This field is ignored when using
         * {@link ShortcutInfo} on API 25 and above.
         * <p>
         * If the shortcut is associated with an activity, the activity icon is used as the badge,
         * otherwise application icon is used.
         *
         * @see #setActivity(ComponentName)
         */
        public Builder setAlwaysBadged() {
            mInfo.mIsAlwaysBadged = true;
            return this;
        }

        public Builder iconShapeWithLauncher(boolean iconShapeWithLauncher) {
            mInfo.mIconShapeWithLauncher = iconShapeWithLauncher;
            return this;
        }

        public Builder updateIfExist(boolean updateIfExist) {
            mInfo.mUpdateIfExist = updateIfExist;
            return this;
        }

        public Builder autoCreateWithSameName(boolean autoCreateWithSameName) {
            mInfo.mAutoCreateWithSameName = autoCreateWithSameName;
            return this;
        }

        @NonNull
        public Builder setIcon(Bitmap val) {
            mInfo.mIconBitmap = val;
            mInfo.mIconDrawable = null;
            return this;
        }

        @NonNull
        public Builder setIcon(Drawable val) {
            mInfo.mIconBitmap = null;
            mInfo.mIconDrawable = val;
            return this;
        }

        @Nullable
        public Bitmap getIconBitmap() {
            return mInfo.mIconBitmap;
        }

        @Nullable
        public Drawable getIconDrawable() {
            return mInfo.mIconDrawable;
        }

        @NonNull
        public Intent getIntent() {
            return mInfo.getIntent();
        }

        public boolean isIconShapeWithLauncher() {
            return mInfo.isIconShapeWithLauncher();
        }

        /**
         * Creates a {@link ShortcutInfoCompat} instance.
         */
        @NonNull
        public ShortcutInfoCompatV2 build() {
            // Verify the arguments
            if (TextUtils.isEmpty(mInfo.mLabel)) {
                throw new IllegalArgumentException("Shortcut must have a non-empty label");
            }
            if (mInfo.mIntents == null || mInfo.mIntents.length == 0) {
                throw new IllegalArgumentException("Shortcut must have an intent");
            }
            return mInfo;
        }
    }
}
