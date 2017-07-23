package com.lusen.cardola.business.main.component;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.lusen.cardola.R;
import com.lusen.cardola.business.base.CardolaBaseActivity;
import com.lusen.cardola.framework.util.ContextUtil;
import com.lusen.cardola.framework.util.UiUtil;

/**
 * Created by leo on 2017/7/23.
 */

public class FindPasswordActivity extends CardolaBaseActivity implements View.OnClickListener {

    private EditText mEtvAccount;
    private EditText mEtvPassword;
    private EditText mEtvPasswordConfirm;
    private EditText mEtvVerifyCode;
    private TextView mBtnSendVerifyCode;
    private TextView mBtnResetPassword;
    private View mLoadingView;

    @Override
    protected View onContentViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflaterView(inflater, R.layout.activity_find_password, container);
    }

    @Override
    protected void onContentViewCreated(View view) {
        super.onContentViewCreated(view);
        mEtvAccount = UiUtil.findViewById(this, R.id.etv_account, EditText.class);
        mEtvPassword = UiUtil.findViewById(this, R.id.etv_password, EditText.class);
        mEtvPasswordConfirm = UiUtil.findViewById(this, R.id.etv_password_confirm, EditText.class);
        mEtvVerifyCode = UiUtil.findViewById(this, R.id.etv_verify_code, EditText.class);
        mBtnSendVerifyCode = UiUtil.findViewById(this, R.id.btn_send_verify_code, TextView.class);
        mBtnResetPassword = UiUtil.findViewById(this, R.id.btn_reset_password, TextView.class);
        mLoadingView = UiUtil.findViewById(this, R.id.layout_loading, View.class);
        UiUtil.bindClickListener(this, mBtnSendVerifyCode, mBtnResetPassword);
    }

    @Override
    protected String initActionBarTitle() {
        return ContextUtil.getContext().getResources().getString(R.string.actionbar_title_find_password);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBtnSendVerifyCode.getId()) {
            sendVerifyCode();
        } else if (id == mBtnSendVerifyCode.getId()) {
            resetPassword();
        }
    }

    private void updateLoadingViewState(boolean show) {
        mLoadingView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    private void sendVerifyCode() {

    }

    private void resetPassword() {

    }

}
