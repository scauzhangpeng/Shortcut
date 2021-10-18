# 概览
&emsp;&emsp;桌面快捷快捷方式，可在受支持的启动器中显示，帮助用户快速访问应用的某些功能。例如点击桌面“扫一扫”快捷方式，便启动App并且跳转到扫一扫界面。例如添加小程序到桌面，用户点击桌面快捷方式便可启动App快速进入小程序。  

&emsp;&emsp;在 Android 8.0（API 级别 26）及更高版本中，才支持创建桌面快捷方式。与静态和动态快捷方式不同，桌面快捷方式在受支持的启动器中显示为单独的图标    
<image src="https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/1e7545ec98684c8b99433dce9de05597~tplv-k3u1fbpfcp-watermark.image"  width=300 align="center"/>
# 权限
创建桌面快捷方式权限不需要运行时请求，在AndroidManifest.xml声明即可。
```xml
    <uses-permission android:name="com.android.launcher.permission.INSTALL_SHORTCUT" />
    <uses-permission android:name="com.android.launcher.permission.UNINSTALL_SHORTCUT" />
```
可是，华为、小米、OPPO、Vivo等厂商的权限管理都将**创建桌面快捷方式**权限设计成运行时权限，默认权限是禁止。这样我们要解决两个问题：
1. 如何检测当前应用是否获取了**创建桌面快捷方式**权限  
     **华为、小米、OPPO、Vivo检测方法不相同，具体分析过程参考[《是否允许创建快捷方式的权限检测》](http://www.lefo.me/2017/05/19/shortcut-permission)，根据这篇文章本人已经整理好相关可运行代码 [github](url)**
     
| 厂商 | 检测方法 |机型有问题请issue|
| --- | --- |---|
|  华为  |反射com.huawei.hsm.permission.PermissionManager#canSendBroadcast|Android 7-11|
|  小米|反射AppOpsManager#checkOpNoThrow|Android 7-11|
|OPPO|ContentProvider查询URI：content://settings/secure/launcher_shortcut_permission_settings|Android 7-11|
|Vivo|ContentProvider查询URI：content://com.bbk.launcher2.settings/favorites|Android 7-11|

2. 是否可以按照运行时权限进行请求让用户进行授权同意  
     **不可以，需要引导用户跳转应用设置界面进行权限开启，可以跳转应用设置界面，亦可以跳转单独的创建桌面快捷方式权限设置界面**
  
| 应用设置界面 |创建桌面快捷方式权限界面  |
| --- | --- |
|  <image src='https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/fdd324b2e1ea446bb32e29670f7a0e2f~tplv-k3u1fbpfcp-watermark.image' width=200/>  |<image src='https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/2bc76255f2694771b83928c26235dfb4~tplv-k3u1fbpfcp-watermark.image' width=200/>|

# 参数
- 创建ShortcutInfoCompat对象，必须包含ID，Intent，ShortLable三个参数。ID是唯一性，Intent是点击桌面快捷方式图标后的跳转意图，ShortLable是所创建的快捷方式显示的名称。
```java
    /**
     * Builder class for {@link ShortcutInfoCompat} objects.
     */
    public static class Builder {

        private final ShortcutInfoCompat mInfo;

        public Builder(@NonNull Context context, @NonNull String id) {
            mInfo = new ShortcutInfoCompat();
            mInfo.mContext = context;
            mInfo.mId = id;
        }
        ...
        ...
        /**
         * Creates a {@link ShortcutInfoCompat} instance.
         */
        @NonNull
        public ShortcutInfoCompat build() {
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
```

- Icon不是必须的，如果没有主动设置则显示默认图标。这里特别提出Icon，是由于 Android 8.0（API 级别 26）引入了自适应启动器图标，在不同设备型号上显示为不同的形状。如果设置的Icon是圆形、圆角都可能被系统调整后包了一层白边。如果设置Icon是正方形则可能某些机型直接显示为正方形。

| 厂商 | 图标表现形式 |预期表现形式|
| --- | --- |---|
|  华为  |<image src='https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/ea5f8da088884899a924c0509c1cf022~tplv-k3u1fbpfcp-watermark.image' width=300, height=100/>|<image src='https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/51d9101dfd194cf08465485053961365~tplv-k3u1fbpfcp-watermark.image' width=300, height=100/>|
| 小米|<image src='https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/46cacbdf176a4003b4ed0205f4d44e38~tplv-k3u1fbpfcp-watermark.image' width=300, height=110/>|<image src='https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/a7072d2d65ed43aeafa33bb817ffefcf~tplv-k3u1fbpfcp-watermark.image' width=300, height=110/>|

这样我们要解决一个问题：  
1. 如何让创建的桌面快捷方式图标与App图标保持一致  
    **将设置图标缩放为App图标的大小，然后合并绘制两个图标，取两者重合保留设置图标内容**

# 唯一性
&emsp;&emsp;根据```ShortcutInfoCompat#Builder(@NonNull Context context, @NonNull String id)```设置的ID可以标识每一个快捷方式。可是，华为8.0 ~ 8.1出现了一个bug，名称一致也认为是相同的快捷方式，在9.0之后就已修复。  
这样我们要解决一个问题：  
1. 如何解决华为8.0 - 8.1这两个版本因为名称相同导致无法创建快捷方式的异常  
    **遍历已有快捷方式，是否存在相同名称的快捷方式，存在则对准备创建的快捷方式改名，目前在后面加一个“.”（当然可以UUID确保完全不一样），等待创建成功的回调后，再默默根据ID对快捷方式进行更新名称操作**

# 点击快捷方式打开App
&emsp;&emsp;点击快捷方式，打开的意图是所设置的Intent，Sample给的方法是跳转一个透明界面，然后进行逻辑处理。从抽屉式中的通知、桌面快捷方式、微件等端外进入App都可以跳转透明的界面再分发做处理。
