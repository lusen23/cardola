package com.lusen.cardola.framework.uibase.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.lusen.cardola.framework.uibase.stack.StackHelperOfActivity;
import com.lusen.cardola.framework.uibase.stack.back.Back;
import com.lusen.cardola.framework.uibase.stack.back.BackOrigin;
import com.lusen.cardola.framework.uibase.util.UIBaseUtil;

/**
 * Created by leo on 16/7/22.
 * 基类Activity(v4-support)
 */
public class BaseActivity extends FragmentActivity {

    public final String mTag = UIBaseUtil.generateActivityTag(this);

    private StackHelperOfActivity mStackHelperOfActivity = new StackHelperOfActivity(this);

    /************************************************************************************
     * 生命周期
     ************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIBaseUtil.log("LifeCycle#%s#onCreate", mTag);
    }

    @Override
    protected void onStart() {
        super.onStart();
        UIBaseUtil.log("LifeCycle#%s#onStart", mTag);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UIBaseUtil.log("LifeCycle#%s#onResume", mTag);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UIBaseUtil.log("LifeCycle#%s#onPause", mTag);
    }

    @Override
    protected void onStop() {
        super.onStop();
        UIBaseUtil.log("LifeCycle#%s#onStop", mTag);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UIBaseUtil.log("LifeCycle#%s#onDestroy", mTag);
    }

    /**
     * 系统回调onBackPressed
     */
    @Override
    public void onBackPressed() {
        mStackHelperOfActivity.performBackPressed(new Back(BackOrigin.BACK_FROM_SYSTEM));
    }

    /**
     * BackPressed事件回调
     *
     * @param back back数据
     * @return true表示拦截处理, 不finish;false表示不处理, finish掉
     */
    public boolean onBaseBackPressed(Back back) {
        return false;
    }

    /**
     * BackPressed事件拦截
     *
     * @param back back数据
     * @return true表示拦截处理, 交予{@link this#onBaseBackPressed(Back)}处理;false表示不拦截,交予下层处理
     */
    public boolean onBaseInterceptBackPressed(Back back) {
        return false;
    }

    /************************************************************************************
     * getter、setter
     ************************************************************************************/

    public StackHelperOfActivity getStackHelperOfActivity() {
        return mStackHelperOfActivity;
    }

}
