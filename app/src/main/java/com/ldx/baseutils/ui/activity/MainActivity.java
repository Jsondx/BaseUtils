package com.ldx.baseutils.ui.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.ldx.baseutils.mvp.base.BaseActivity;
import com.ldx.baseutils.R;
import com.ldx.baseutils.mvp.presenter.MainPresenter;
import com.ldx.baseutils.mvp.view.MainView;
import com.ldx.baseutils.ui.bean.ContactInfo;
import com.ldx.baseutils.utils.LogUtils;
import com.ldx.baseutils.utils.WordUtil;

import java.util.ArrayList;
import java.util.List;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * @author babieta
 */
public class MainActivity extends BaseActivity<MainPresenter> implements MainView {
    private boolean flag = false;

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

        final EditText text = findViewById(R.id.textI);

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    String inputString = "" + text.getText().toString();
                    String firstLetterCapString = WordUtil.capitalize(inputString);
                    if (!firstLetterCapString.equals("" + text.getText().toString())) {
                        text.setText("" + firstLetterCapString);
                        text.setSelection(text.getText().length());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private String firstLetterToBig(String str) {
        if (str.isEmpty()) {
            return "";
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1, str.length());
    }

    @Override
    protected void loadDate() {

    }

    public void requestPermission() {
        PermissionGen.with(MainActivity.this)
                .addRequestCode(100)
                .permissions(Manifest.permission.READ_CONTACTS)
                .request();
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
