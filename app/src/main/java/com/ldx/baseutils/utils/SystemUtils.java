package com.ldx.baseutils.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Instrumentation;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author Administrator
 */
public class SystemUtils {


    private SystemUtils() {
        throw new AssertionError();
    }

    /**
     * 异步线程
     *
     * @param runnable
     */
    public static void asyncThread(Runnable runnable) {
        new Thread(runnable).start();
    }


    public static boolean isNetWorkActive(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.isConnected();
    }

    /**
     * 发送按键按下事件
     *
     * @param keyCode
     */
    public static void sendKeyCode(final int keyCode) {
        asyncThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(keyCode);
                } catch (Exception e) {
                    LogUtils.e("Exception when sendPointerSync" + e);
                }
            }
        });
    }

    /**
     * 退格删除键
     *
     * @param et
     */
    public static void deleteClick(EditText et) {
        final KeyEvent keyEventDown = new KeyEvent(KeyEvent.ACTION_DOWN,
                KeyEvent.KEYCODE_DEL);
        et.onKeyDown(KeyEvent.KEYCODE_DEL, keyEventDown);
    }


    /**
     * 调用打电话界面
     *
     * @param context
     * @param phoneNumber
     */
    public static void call(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(String.format("tel:%s", phoneNumber)));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 直接拨打电话
     *
     * @param context
     * @param phoneNumber
     */
    @SuppressLint("MissingPermission")
    public static void directCall(Context context, String phoneNumber) {
        Intent intentPhone = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        intentPhone.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentPhone);
    }


    /**
     * 调用发短信界面
     *
     * @param context
     * @param phoneNumber
     */
    public static void sendSMS(Context context, String phoneNumber) {

        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(String.format("smsto:%s", phoneNumber)));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }

    /**
     * 发短信
     *
     * @param phoneNumber
     * @param msg
     */
    public static void sendSMS(String phoneNumber, String msg) {

        SmsManager sm = SmsManager.getDefault();

        List<String> msgs = sm.divideMessage(msg);

        for (String text : msgs) {
            sm.sendTextMessage(phoneNumber, null, text, null, null);
        }

    }

    /**
     * 检测权限
     *
     * @param context
     * @param permission
     * @return
     */
    public static boolean checkSelfPermission(Context context, @NonNull String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 多组权限检测
     *
     * @param context
     * @param strings
     * @return
     */
    public static boolean chekSelfPermissions(Context context, String[] strings) {
        for (String permission : strings) {
            if (checkSelfPermission(context, permission)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 申请权限
     *
     * @param activity
     * @param permission
     * @param requestCode
     */
    public static void requestPermission(Activity activity, @NonNull String permission, int requestCode) {
        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
    }

    /**
     * 申请多组权限
     *
     * @param activity
     * @param permission
     * @param requestCode
     */
    public static void requestPermissions(Activity activity, @NonNull String[] permission, int requestCode) {
        ActivityCompat.requestPermissions(activity, permission, requestCode);
    }

    /**
     * 显示申请授权
     *
     * @param activity
     * @param permission
     */
    public static void shouldShowRequestPermissionRationale(Activity activity, @NonNull String permission) {
        ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }


    /**
     * 隐藏软键盘
     *
     * @param context
     * @param v
     */
    public static void hideInputMethod(Context context, EditText v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

    }

    /**
     * 显示软键盘
     *
     * @param context
     * @param v
     */
    public static void showInputMethod(Context context, EditText v) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 强制关闭键盘
     *
     * @param context
     */
    public static void hintKeyBoard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != imm) {
            boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
            if (isOpen) {
//                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                if (context.getCurrentFocus() != null) {//强制关闭软键盘
                    imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
    }

    /**
     * 复制文本
     *
     * @param context
     * @param text
     */
    public static void copyText(Context context, String text) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", text);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);

    }

    /**
     * 获取当前系统前后第几天
     */
    public static String getNextDay(int i) {
        String curDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar c = new GregorianCalendar();
            c.add(Calendar.DAY_OF_MONTH, i);
            curDateTime = mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return curDateTime;
    }


    private static final int MIN_DELAY_TIME = 1000;
    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }


    /**
     * 通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
     *
     * @param activity
     * @return
     */
    public static boolean checkDeviceHasNavigationBar2(Context activity) {
        boolean hasMenuKey = ViewConfiguration.get(activity).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        if (!hasMenuKey && !hasBackKey) {
            // 做任何你需要做的,这个设备有一个导航栏
            return true;
        }
        return false;
    }

    /**
     * 隐藏虚拟按键，并且全屏
     *
     * @param activity
     */
    public static void hideBottomUIMenu(Activity activity) {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            // lower api
            View v = activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 正则表达
     * 166，
     * 176，177，178,175,170,171
     * 180，181，182，183，184，185，186，187，188，189
     * 145，147
     * 198，199
     * <p>
     * 130，131，132，133，134，135，136，137，138，139
     * 150，151，152，153，155，156，157，158，159
     */

    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(166)|(17[0-8])|(18[0-9])|(19[8-9])|(147,145))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 判断当前应用是否是debug状态
     */

    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取当前本地apk的版本
     *
     * @param mContext
     * @return
     */
    public static int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取版本号名称
     *
     * @param context 上下文
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    /**
     * 匹配车牌号
     *
     * @param carnumber
     * @return
     */
    public static boolean isCarnumberNO(String carnumber) {
        /*
         车牌号格式：汉字 + A-Z + 5位A-Z或0-9
        （只包括了普通车牌号，教练车和部分部队车等车牌号不包括在内）
         */
        String carnumRegex = "[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{6}";
        if (TextUtils.isEmpty(carnumber)) return false;
        else return carnumber.matches(carnumRegex);
    }

    /**
     * 匹配车架号
     *
     * @param carnumber
     * @return
     */
    public static boolean isFrameNumber(String carnumber) {
        String carnumRegex = "^(\\d{17})$";
        if (TextUtils.isEmpty(carnumber)) return false;
        else return carnumber.matches(carnumRegex);
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPen(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }

        return false;
    }

    /**
     * 强制帮用户打开GPS
     *
     * @param context
     */
    public static final void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检测gps是否开启
     *
     * @param context
     * @return
     */
    public static boolean checkGpsStatus(Context context) {
        boolean result = false;
        LocationManager lManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
          /* 确认GPS是否开启 */
        if (!lManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || !lManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            LogUtils.e("GPS状态：未启动");
            result = false;
        } else {
            LogUtils.e("GPS状态：已启动");
            result = true;
        }
        return result;
    }

    /**
     * 获取assets 下的json
     *
     * @param fileName
     * @param context
     * @return
     */
    public static String getJson(String fileName, Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 判断是否有招商app
     *
     * @param context
     * @return
     */
    public static boolean isCMBAppInstalled(Context context) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo("cmb.pb", 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 跳转招商app
     */
    public static void callCMBApp(Context context, String url) {
        try {
            Intent intent = new Intent();
            Uri data = Uri.parse(url);
            intent.setData(data);
            intent.setAction("android.intent.action.VIEW");
            context.startActivity(intent);
        } catch (Exception e) {
            LogUtils.e("Exception", e);
        }
    }

    /**
     * 打开浏览器
     *
     * @param context
     * @param url
     */
    public static void openBrowserUrl(Context context, String url) {
        Intent intent = new Intent();
        //Url 就是你要打开的网址
        intent.setData(Uri.parse(url));
        intent.setAction(Intent.ACTION_VIEW);
        //启动浏览器
        context.startActivity(intent);
    }

    /**
     * 打开该app的详情页
     *
     * @param context
     */
    public static void toSelfSetting(Context context) {
        Intent mIntent = new Intent();
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            mIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            mIntent.setAction(Intent.ACTION_VIEW);
            mIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
            mIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(mIntent);
    }

    /**
     * px转换dip
     */
    public static int px2dip(Context context, int px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 设置 bitmap 大小
     *
     * @param context
     * @param resources
     * @param Width
     * @param Height
     * @return
     */
    public static Bitmap retrunBitmap(Context context, int resources, int Width, int Height) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resources);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
//        Log.e("width", "width:" + width);
//        Log.e("height", "height:" + height);
        //设置想要的大小
        int newWidth = Width;
        int newHeight = Height;

        //计算压缩的比率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        //获取想要缩放的matrix
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        //获取新的bitmap
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        bitmap.getWidth();
        bitmap.getHeight();
        return bitmap;
    }

    /**
     * 获取当前系统前后第几天
     */
    public static String getNextDay(int i, long time) {
        String curDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("MM/dd HH:mm");
            Calendar c = new GregorianCalendar();
            c.setTime(new Date(time));
            c.add(Calendar.DAY_OF_MONTH, (i - 1));
            curDateTime = mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return curDateTime;
    }

    /**
     * 判断服务是否开启
     *
     * @return
     */
    public static boolean isServiceRunning(Context context, String ServiceName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceInfoList = manager.getRunningServices(200);
        if (serviceInfoList.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningServiceInfo info : serviceInfoList) {
            if (info.service.getClassName().equals(ServiceName)) {
                return true;
            }
        }
        return false;
    }


    /**
     * get image from network
     * url 转 bitmap
     *
     * @return [BitMap]image
     */
    public static Bitmap returnBitMap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    /**
     * 保存地址
     *
     * @param context
     * @param bmp
     */
    public static void saveImageToGallery(Context context, Bitmap bmp) {
        if (bmp == null) {
            Log.e("TAG", "bitmap---为空");
            return;
        }
        String galleryPath = Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_DCIM
                + File.separator + "Camera" + File.separator;
        String fileName = +System.currentTimeMillis() + ".png";
        File file = new File(galleryPath, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                Log.e("TAG", "图片保存成功 保存在:" + file.getPath());
//                ToastUtils.showShortSafe("图片保存成功 保存在:" + file.getPath());
            } else {
                Log.e("TAG", "图片保存失败");
//                ToastUtils.showShortSafe("图片保存失败");
            }
        } catch (IOException e) {
            Log.e("TAG", "保存图片找不到文件夹");
            e.printStackTrace();
        }
    }

    public static boolean cleanInternalSP(Context context) {
        return deleteFilesInDir(context.getFilesDir().getParent() + File.separator + "shared_prefs");
    }

    public static boolean deleteFilesInDir(String dirPath) {
        return deleteFilesInDir(getFileByPath(dirPath));
    }

    public static boolean deleteFilesInDir(File dir) {
        if (dir == null) {
            return false;
        } else if (!dir.exists()) {
            return true;
        } else if (!dir.isDirectory()) {
            return false;
        } else {
            File[] files = dir.listFiles();
            if (files != null && files.length != 0) {
                File[] var2 = files;
                int var3 = files.length;

                for (int var4 = 0; var4 < var3; ++var4) {
                    File file = var2[var4];
                    if (file.isFile()) {
                        if (!deleteFile(file)) {
                            return false;
                        }
                    } else if (file.isDirectory() && !deleteDir(file)) {
                        return false;
                    }
                }
            }

            return true;
        }
    }

    public static boolean deleteFile(File file) {
        return file != null && (!file.exists() || file.isFile() && file.delete());
    }

    public static boolean deleteDir(File dir) {
        if (dir == null) {
            return false;
        } else if (!dir.exists()) {
            return true;
        } else if (!dir.isDirectory()) {
            return false;
        } else {
            File[] files = dir.listFiles();
            File[] var2 = files;
            int var3 = files.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                File file = var2[var4];
                if (file.isFile()) {
                    if (!deleteFile(file)) {
                        return false;
                    }
                } else if (file.isDirectory() && !deleteDir(file)) {
                    return false;
                }
            }

            return dir.delete();
        }
    }

    public static File getFileByPath(String filePath) {
        return isNullString(filePath) ? null : new File(filePath);
    }

    public static boolean isNullString(@Nullable String str) {
        return str == null || str.length() == 0 || "null".equals(str);
    }
}
