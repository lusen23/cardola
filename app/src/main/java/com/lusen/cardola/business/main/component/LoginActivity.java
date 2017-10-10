package com.lusen.cardola.business.main.component;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.lusen.cardola.R;
import com.lusen.cardola.business.base.CardolaBaseActivity;
import com.lusen.cardola.business.network.CardolaApiManager;
import com.lusen.cardola.business.network.resp.BaseResponse;
import com.lusen.cardola.business.network.resp.LoginResp;
import com.lusen.cardola.business.scheme.SchemeUrlConstant;
import com.lusen.cardola.framework.network.BaseSubscriber;
import com.lusen.cardola.framework.uibase.UiModel;
import com.lusen.cardola.framework.uibase.stack.back.Back;
import com.lusen.cardola.framework.uikit.LoadingDialog;
import com.lusen.cardola.framework.util.KeyboardUtil;
import com.lusen.cardola.framework.util.ToastUtil;
import com.lusen.cardola.framework.util.UiUtil;

import navigator.Nav;

/**
 * Created by leo on 2017/7/23.
 */

public class LoginActivity extends CardolaBaseActivity implements View.OnClickListener {

    private EditText mEtvAccount;
    private EditText mEtvPassword;
    private TextView mBtnLogin;
    private TextView mBtnFindPassword;
    private LoadingDialog mLoadingDialog;

    @Override
    protected View onContentViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflaterView(inflater, R.layout.activity_login, container);
    }

    @Override
    protected void onContentViewCreated(View view) {
        super.onContentViewCreated(view);
        mEtvAccount = UiUtil.findViewById(this, R.id.etv_account, EditText.class);
        mEtvPassword = UiUtil.findViewById(this, R.id.etv_password, EditText.class);
        mBtnLogin = UiUtil.findViewById(this, R.id.btn_login, TextView.class);
        mBtnFindPassword = UiUtil.findViewById(this, R.id.btn_find_password, TextView.class);
        UiUtil.bindClickListener(this, mBtnLogin, mBtnFindPassword);
    }

    @Override
    protected int initUiModel() {
        return UiModel.UI_MODEL_IMMERSIVE;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBtnLogin.getId()) {
            login();
        } else if (id == mBtnFindPassword.getId()) {
            findPassword();
        }
    }

    private void login() {
        String inputAccount = null != mEtvAccount.getEditableText() ? mEtvAccount.getEditableText().toString() : null;
        String inputPassword = null != mEtvPassword.getEditableText() ? mEtvPassword.getEditableText().toString() : null;
        if (null == inputAccount || inputAccount.trim().equals("")) {
            ToastUtil.toast("请输入账户");
            return;
        }
        if (null == inputPassword || inputPassword.trim().equals("")) {
            ToastUtil.toast("请输入密码");
            return;
        }
        // 开始登录流程
        updateLoadingDialog(true);
        KeyboardUtil.hideKeybBoard(this, mEtvAccount);
        CardolaApiManager.getInstance().login(inputAccount, inputPassword, new BaseSubscriber<BaseResponse<LoginResp>>() {

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                updateLoadingDialog(false);
                ToastUtil.toast("登录异常");
            }

            @Override
            public void onNext(BaseResponse<LoginResp> response) {
                updateLoadingDialog(false);
                Nav.fromHost(SchemeUrlConstant.Host.HOME).nav();
            }
        });
    }

    private void findPassword() {
        Nav.fromHost(SchemeUrlConstant.Host.FIND_PASSWORD).nav();
    }

    private void updateLoadingDialog(boolean show) {
        if (null == mLoadingDialog) {
            mLoadingDialog = new LoadingDialog();
            mLoadingDialog.setLoadingTitle("登录中...");
        }
        if (show) {
            showDialog(mLoadingDialog);
        } else {
            hideDialog(mLoadingDialog);
        }
    }

    @Override
    public boolean onBaseBackPressed(Back back) {
        finishSelfActivity();
        System.exit(0);
        return true;
    }

}
