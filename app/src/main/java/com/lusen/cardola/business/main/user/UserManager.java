package com.lusen.cardola.business.main.user;

import android.text.TextUtils;

/**
 * Created by leo on 2017/7/23.
 */

public class UserManager {

    private static UserManager sInstance;
    private static final byte[] mLock = new byte[0];

    private User mUser;

    public static UserManager getInstance() {
        if (null == sInstance) {
            synchronized (mLock) {
                if (null != sInstance) {
                    return sInstance;
                }
                sInstance = new UserManager();
            }
        }
        return sInstance;
    }

    private UserManager() {
    }

    public boolean isUserLogin() {
        return (null != mUser && !TextUtils.isEmpty(mUser.mAccessToken));
    }

    public void login() {

    }

    public void logout() {
        mUser = null;

    }

    public String getUserId() {
        // TODO 暂时写死32，待完善login模块
        return "32";
    }

}
