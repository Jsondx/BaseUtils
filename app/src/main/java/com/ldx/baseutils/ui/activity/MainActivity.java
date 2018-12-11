package com.ldx.baseutils.ui.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.view.View;

import com.ldx.baseutils.demo.ConcreteBuilder;
import com.ldx.baseutils.demo.Director;
import com.ldx.baseutils.https.Http;
import com.ldx.baseutils.https.BaseCallBack;
import com.ldx.baseutils.mvp.base.BaseActivity;
import com.ldx.baseutils.R;
import com.ldx.baseutils.mvp.presenter.MainPresenter;
import com.ldx.baseutils.mvp.view.MainView;
import com.ldx.baseutils.ui.bean.Code;
import com.ldx.baseutils.ui.bean.ContactInfo;
import com.ldx.baseutils.utils.LogUtils;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * @author babieta
 */
public class MainActivity extends BaseActivity<MainPresenter> implements MainView {


    @Override
    protected MainPresenter createP() {
        return new MainPresenter();
    }

    @NonNull
    @Override
    public int setView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        ConcreteBuilder concreteBuilder = new ConcreteBuilder();
        Director director = new Director(concreteBuilder);
        director.Construct("i7-6700", "三星DDR4", "希捷1T");

    }

    @Override
    protected void loadDate() {

    }

    public void geJson(View view) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("phone", "15064875827");
        Http.post("http://testing,lailezhuanche.com/home/Register/sendCode", httpParams, new BaseCallBack<Code>() {
            @Override
            public void onSuccess(Response<Code> response) {
                Code body = response.body();


            }
        });

    }

    public void onSendCode(View view) {
        PermissionGen.with(MainActivity.this)
                .addRequestCode(100)
                .permissions(Manifest.permission.READ_CONTACTS)
                .request();
    }

    public void onViewChilck(View view) {

    }

    @PermissionSuccess(requestCode = 100)
    public void doSomething() {
        LogUtils.e("Contact permission is granted");
        try {
            List<ContactInfo> phoneContacts = getPhoneContacts();
            LogUtils.e("--> info  " + phoneContacts.size());
        } catch (Exception e) {
            //小米权限拒绝之后还能回调正确回调
            LogUtils.e("--> 有可能被拒绝了，有可能获取数据出错了");
            e.printStackTrace();
        }

    }

    @PermissionFail(requestCode = 100)
    public void doFailSomething() {
        LogUtils.e("Contact permission is not granted");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    /**
     * 获取系统联系人，获取1000个联系人0.2秒，最快速
     */
    private List<ContactInfo> getPhoneContacts() {
        //联系人集合
        List<ContactInfo> result = new ArrayList<>();
        ContentResolver contentResolver = getContentResolver();
        //搜索字段
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.Contacts.DISPLAY_NAME};

        // 获取手机联系人
        Cursor contactsCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);

        if (contactsCursor != null && contactsCursor.getCount() > 0) {
            if (contactsCursor.moveToFirst()) {
                do {
                    //获取联系人的姓名
                    String phoneName = contactsCursor.getString(2);
                    //获取联系人的号码
                    String phoneNumber = contactsCursor.getString(1);
                    //号码处理
                    //String phoneReplace = getReplaceString(phoneNumber);
                    ContactInfo contactInfo = new ContactInfo();
                    contactInfo.setName(phoneName);

                    contactInfo.setNumber(phoneNumber);

                    result.add(contactInfo);
                } while (contactsCursor.moveToNext());
                contactsCursor.close();
            }
        }

        return result;
    }


}
