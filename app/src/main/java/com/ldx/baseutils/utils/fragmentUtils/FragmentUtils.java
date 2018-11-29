package com.ldx.baseutils.utils.fragmentUtils;

import android.content.res.Resources;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.util.List;

public class FragmentUtils {

    private final FragmentManager supportFragmentManager;
    //根据注解创建fragment对象
    /**
     * add hide show的方法
     */
    public FragmentUtils(AppCompatActivity appCompatActivity) {
                supportFragmentManager = appCompatActivity.getSupportFragmentManager();
        Class<? extends AppCompatActivity> aClass = appCompatActivity.getClass();
        Field[] fields = aClass.getDeclaredFields();
        for( Field f:fields){
            boolean annotationPresent = f.isAnnotationPresent(Fragmentanntation.class);
            if(annotationPresent){
                //有注解
                f.setAccessible(true);
                try {
                    Package aPackage = f.getType().getPackage();
                    String name = aPackage.getName();
                    String name1 = f.getType().getName();
                    Class<?> aClass1 = Class.forName( name1);
                    Object o = aClass1.newInstance();
                    f.set(appCompatActivity,o);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private @IdRes Integer id;

    public void setResId(@IdRes int id) {
        this.id = id;
    }

    public void showFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        List<Fragment> fragments = supportFragmentManager.getFragments();
        hideallfragment(fragments, fragmentTransaction);
        if (fragments.contains(fragment)) {
            //有这个fragment，需要展示
            fragmentTransaction.show(fragment);
        } else {
            //没有这个fragment需要添加
            if(this.id==null){
                throw new Resources.NotFoundException("资源id未引用");
            }
            fragmentTransaction.add(this.id,fragment);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void hideallfragment(List<Fragment> fragments, FragmentTransaction fragmentTransaction) {
        for (Fragment fragmen : fragments) {
            fragmentTransaction.hide(fragmen);
        }
    }
}
