package com.lusen.cardola.framework.manager;

import android.app.Activity;
import android.app.Application;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.lusen.cardola.R;
import com.lusen.cardola.framework.uibase.util.UIBaseUtil;
import com.lusen.cardola.framework.util.ContextUtil;

import java.util.List;

/**
 * Created by leo on 16/8/4.
 * JumperManager
 * >>activity开启及结束
 * >>service开启及结束
 * >>dialog开启及结束
 */
public class JumperManager {

    private static final String TAG = JumperManager.class.getSimpleName();

    public static final int ANIM_NONE = 0;

    /**
     * 启动Activity,类启动,默认context
     *
     * @param activityClazz activity启动类
     * @return 是否成功
     */
    public static boolean launchActivity(Class<? extends Activity> activityClazz) {
        return launchActivity(activityClazz, null);
    }

    /**
     * 启动Activity,类启动,默认context
     *
     * @param activityClazz activity启动类
     * @param bundle        bundle数据
     * @return 是否成功
     */
    public static boolean launchActivity(Class<? extends Activity> activityClazz, Bundle bundle) {
        return launchActivity(activityClazz, bundle, ANIM_NONE, ANIM_NONE);
    }

    /**
     * 启动Activity,类启动,默认context
     *
     * @param activityClazz activity启动类
     * @param bundle        bundle数据
     * @param enterAnim     进入动画(进入和退出均为null则采用系统默认，小于等于0采用框架默认，大于0采用传入的)
     * @param exitAnim      退出动画(进入和退出均为null则采用系统默认，小于等于0采用框架默认，大于0采用传入的)
     * @return 是否成功
     */
    public static boolean launchActivity(Class<? extends Activity> activityClazz, Bundle bundle, Integer enterAnim, Integer exitAnim) {
        if (null != activityClazz) {
            Intent intent = new Intent(ContextUtil.getContext(), activityClazz);
            if (null != bundle) {
                intent.putExtras(bundle);
            }
            return launchActivity(intent, enterAnim, exitAnim);
        }
        return false;
    }

    /**
     * 启动Activity,类启动
     *
     * @param context       context上下文
     * @param activityClazz activity启动类
     * @param bundle        bundle数据
     * @return 是否成功
     */
    public static boolean launchActivity(Context context, Class<? extends Activity> activityClazz, Bundle bundle) {
        return launchActivity(context, activityClazz, bundle, ANIM_NONE, ANIM_NONE);
    }

    /**
     * 启动Activity,类启动
     *
     * @param context       context上下文
     * @param activityClazz activity启动类
     * @param bundle        bundle数据
     * @param enterAnim     进入动画(进入和退出均为null则采用系统默认，小于等于0采用框架默认，大于0采用传入的)
     * @param exitAnim      退出动画(进入和退出均为null则采用系统默认，小于等于0采用框架默认，大于0采用传入的)
     * @return 是否成功
     */
    public static boolean launchActivity(Context context, Class<? extends Activity> activityClazz, Bundle bundle, Integer enterAnim, Integer exitAnim) {
        if (null != activityClazz) {
            Intent intent = new Intent(ContextUtil.getContext(), activityClazz);
            if (null != bundle) {
                intent.putExtras(bundle);
            }
            return launchActivity(context, intent, enterAnim, exitAnim);
        }
        return false;
    }

    /**
     * 启动Activity,默认context
     *
     * @param intent intent意图
     * @return 是否成功
     */
    public static boolean launchActivity(Intent intent) {
        return launchActivity(intent, ANIM_NONE, ANIM_NONE);
    }

    /**
     * 启动Activity,默认context
     *
     * @param intent    intent意图
     * @param enterAnim 进入动画(进入和退出均为null则采用系统默认，小于等于0采用框架默认，大于0采用传入的)
     * @param exitAnim  退出动画(进入和退出均为null则采用系统默认，小于等于0采用框架默认，大于0采用传入的)
     * @return 是否成功
     */
    public static boolean launchActivity(Intent intent, Integer enterAnim, Integer exitAnim) {
        return launchActivity(ContextUtil.getContext(), intent, enterAnim, exitAnim);
    }

    /**
     * 启动Activity
     *
     * @param context context上下文
     * @param intent  intent意图
     * @return 是否成功
     */
    public static boolean launchActivity(Context context, Intent intent) {
        return launchActivity(context, intent, ANIM_NONE, ANIM_NONE);
    }

    /**
     * 启动Activity
     *
     * @param context   context上下文
     * @param intent    intent意图
     * @param enterAnim 进入动画(进入和退出均为null则采用系统默认，小于等于0采用框架默认，大于0采用传入的)
     * @param exitAnim  退出动画(进入和退出均为null则采用系统默认，小于等于0采用框架默认，大于0采用传入的)
     * @return 是否成功
     */
    public static boolean launchActivity(Context context, Intent intent, Integer enterAnim, Integer exitAnim) {
        boolean result = false;
        // Context优化(主要解决动画问题)
        context = AppManager.getInstance().getCurrentActivityIfExist();
        if (null == context) {
            context = ContextUtil.getContext();
        }
        UIBaseUtil.log("%s launchActivity start (context,intent,enterAnim,exitAnim) = %s,%s,%s,%s", TAG, context, intent, enterAnim, exitAnim);
        // 启动Activity
        if (null != context && null != intent) {
            try {
                if (context instanceof Application)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                result = true;
            } catch (Exception e) {
                UIBaseUtil.log("%s launchActivity 异常-启动", TAG);
                e.printStackTrace();
            }
            // 设置动画
            try {
                if (context instanceof Activity) {
                    if (null != enterAnim || null != exitAnim) {
                        if (null == enterAnim || enterAnim <= 0) {
                            enterAnim = R.anim.uibase_anim_default;
                        }
                        if (null == exitAnim || exitAnim <= 0) {
                            exitAnim = R.anim.uibase_anim_default;
                        }
                        ((Activity) context).overridePendingTransition(enterAnim, exitAnim);
                    }
                }
            } catch (Exception e) {
                UIBaseUtil.log("%s launchActivity 异常-动画", TAG);
                e.printStackTrace();
            }
        }
        UIBaseUtil.log("%s launchActivity end = %s", TAG, result);
        return result;
    }

    /**
     * 启动Activity(需要处理返回结果)
     *
     * @param activity    activity对象
     * @param intent      intent意图
     * @param requestCode 请求码
     * @return 是否成功
     */
    public static boolean launchActivityForResult(Activity activity, Intent intent, int requestCode) {
        return launchActivityForResult(activity, intent, requestCode, ANIM_NONE, ANIM_NONE);
    }

    /**
     * 启动Activity(需要处理返回结果),可设置动画
     *
     * @param activity    activity对象
     * @param intent      intent意图
     * @param requestCode 请求码
     * @param enterAnim   进入动画(进入和退出均为null则采用系统默认，小于等于0采用框架默认，大于0采用传入的)
     * @param exitAnim    退出动画(进入和退出均为null则采用系统默认，小于等于0采用框架默认，大于0采用传入的)
     * @return 是否成功
     */
    public static boolean launchActivityForResult(Activity activity, Intent intent, int requestCode, Integer enterAnim, Integer exitAnim) {
        UIBaseUtil.log("%s launchActivityForResult start (activity,intent,enterAnim,exitAnim) = %s,%s,%s,%s", TAG, activity, intent, enterAnim, exitAnim);
        boolean result = false;
        if (null != activity && null != intent) {
            // 启动Activity
            try {
                activity.startActivityForResult(intent, requestCode);
                result = true;
            } catch (Exception e) {
                UIBaseUtil.log("%s launchActivityForResult 异常-启动", TAG);
                e.printStackTrace();
            }
            // 设置动画
            try {
                if (null != enterAnim || null != exitAnim) {
                    if (null == enterAnim || enterAnim <= 0) {
                        enterAnim = R.anim.uibase_anim_default;
                    }
                    if (null == exitAnim || exitAnim <= 0) {
                        exitAnim = R.anim.uibase_anim_default;
                    }
                    activity.overridePendingTransition(enterAnim, exitAnim);
                }
            } catch (Exception e) {
                UIBaseUtil.log("%s launchActivityForResult 异常-动画", TAG);
                e.printStackTrace();
            }
        }
        UIBaseUtil.log("%s launchActivityForResult end = %s", TAG, result);
        return result;
    }

    /**
     * 结束Activity,可设置动画
     *
     * @param activity activity对象
     * @return 是否成功
     */
    public static boolean finishActivity(Activity activity) {
        return finishActivity(activity, ANIM_NONE, ANIM_NONE);
    }

    /**
     * 结束Activity,可设置动画
     *
     * @param activity  activity对象
     * @param enterAnim 进入动画(进入和退出均为null则采用系统默认，小于等于0采用框架默认，大于0采用传入的)
     * @param exitAnim  退出动画(进入和退出均为null则采用系统默认，小于等于0采用框架默认，大于0采用传入的)
     * @return 是否成功
     */
    public static boolean finishActivity(Activity activity, Integer enterAnim, Integer exitAnim) {
        UIBaseUtil.log("%s finishActivity start (activity,enterAnim,exitAnim) = %s,%s,%s", TAG, activity, enterAnim, exitAnim);
        boolean result = false;
        if (null != activity) {
            // 结束Activity
            try {
                activity.finish();
                result = true;
            } catch (Exception e) {
                UIBaseUtil.log("%s finishActivity 异常-关闭", TAG);
                e.printStackTrace();
            }
            // 设置动画
            try {
                if (null != enterAnim || null != exitAnim) {
                    if (null == enterAnim || enterAnim <= 0) {
                        enterAnim = R.anim.uibase_anim_default;
                    }
                    if (null == exitAnim || exitAnim <= 0) {
                        exitAnim = R.anim.uibase_anim_default;
                    }
                    activity.overridePendingTransition(enterAnim, exitAnim);
                }
            } catch (Exception e) {
                UIBaseUtil.log("%s finishActivity 异常-动画", TAG);
                e.printStackTrace();
            }
        }
        UIBaseUtil.log("%s finishActivity end = %s", TAG, result);
        return result;
    }

    /**
     * 回退至Activity
     *
     * @param targetActivity activity目标
     * @return 是否成功
     */
    public static boolean back2Activity(Activity targetActivity) {
        boolean result = false;
        if (null != targetActivity) {
            List<Activity> activityList = AppManager.getInstance().getAllActivity();
            if (null != activityList && activityList.contains(targetActivity)) {
                int count = activityList.size();
                for (int index = count - 1; index >= 0; index--) {
                    Activity activity = activityList.get(index);
                    if (null != activity) {
                        if (activity == targetActivity) {
                            result = true;
                            break;
                        } else {
                            finishActivity(activity, JumperManager.ANIM_NONE, JumperManager.ANIM_NONE);
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * 回退至Activity
     *
     * @param targetActivity activity目标类
     * @param inherit        继承性,false严格匹配class,true会考虑继承关系
     * @return 是否成功
     */
    public static boolean back2Activity(Class<? extends Activity> targetActivity, boolean inherit) {
        boolean result = false;
        if (null != targetActivity) {
            List<Activity> activityList = AppManager.getInstance().getAllActivity();
            int count = activityList.size();
            for (int index = count - 1; index >= 0; index--) {
                Activity activity = activityList.get(index);
                boolean isHit = AppManager.getInstance().isActivityHit(targetActivity, activity, inherit);
                if (isHit) {
                    result = back2Activity(activity);
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 绑定Service
     *
     * @param context           context上下文
     * @param intent            intent意图
     * @param serviceConnection connection服务
     * @param flags             标识位
     * @return 是否成功
     */
    public static boolean bindService(Context context, Intent intent, ServiceConnection serviceConnection, int flags) {
        UIBaseUtil.log("%s bindService start (context,intent,connection,flag) = %s,%s,%s,%s", TAG, context, intent, serviceConnection, flags);
        boolean result = false;
        if (null != context && null != intent && null != serviceConnection) {
            try {
                context.bindService(intent, serviceConnection, flags);
                result = true;
            } catch (Exception e) {
                UIBaseUtil.log("%s bindService 异常-绑定", TAG);
                e.printStackTrace();
            }
        }
        UIBaseUtil.log("%s bindService end = %s", TAG, result);
        return result;
    }

    /**
     * 解绑Service
     *
     * @param context           context上下文
     * @param serviceConnection connection服务
     * @return 是否成功
     */
    public static boolean unbindService(Context context, ServiceConnection serviceConnection) {
        UIBaseUtil.log("%s unbindService start (context,connection) = %s,%s", TAG, context, serviceConnection);
        boolean result = false;
        if (null != context && null != serviceConnection) {
            try {
                context.unbindService(serviceConnection);
                result = true;
            } catch (Exception e) {
                UIBaseUtil.log("%s unbindService 异常-解绑", TAG);
                e.printStackTrace();
            }
        }
        UIBaseUtil.log("%s unbindService end = %s", TAG, result);
        return result;
    }

    /**
     * 启动Service
     *
     * @param context context上下文
     * @param intent  intent意图
     * @return 是否成功
     */
    public static boolean startService(Context context, Intent intent) {
        UIBaseUtil.log("%s startService start (context,intent) = %s,%s", TAG, context, intent);
        boolean result = false;
        if (null != context && null != intent) {
            try {
                context.startService(intent);
                result = true;
            } catch (Exception e) {
                UIBaseUtil.log("%s startService 异常-开启", TAG);
                e.printStackTrace();
            }
        }
        UIBaseUtil.log("%s startService end = %s", TAG, result);
        return result;
    }


    /**
     * 停止Service
     *
     * @param context context上下文
     * @param intent  intent意图
     */
    public static boolean stopService(Context context, Intent intent) {
        UIBaseUtil.log("%s stopService start (context,intent) = %s,%s", TAG, context, intent);
        boolean result = false;
        if (null != context && null != intent) {
            try {
                context.stopService(intent);
                result = true;
            } catch (Exception e) {
                UIBaseUtil.log("%s stopService 异常-停止", TAG);
                e.printStackTrace();
            }
        }
        UIBaseUtil.log("%s stopService end = %s", TAG, result);
        return result;
    }

    /**
     * 显示Dialog至顶部Activity
     *
     * @param dialog dialog对象
     * @return 是否成功
     */
    public static boolean showDialogWithTopActivity(DialogFragment dialog) {
        return showDialogWithTopActivity(dialog, true);
    }

    /**
     * 显示Dialog至顶部Activity
     *
     * @param dialog dialog对象
     * @return 是否成功
     */
    public static boolean showDialogWithTopActivity(android.support.v4.app.DialogFragment dialog) {
        return showDialogWithTopActivity(dialog, true);
    }

    /**
     * 显示Dialog至顶部Activity
     *
     * @param dialog dialog对象
     * @return 是否强制打开
     */
    public static boolean showDialogWithTopActivity(DialogFragment dialog, boolean force) {
        boolean result = false;
        Activity activity = AppManager.getInstance().getCurrentActivityIfExist();
        UIBaseUtil.log("%s showDialogWithTopActivity-System start (activity,dialog) = %s,%s", TAG, activity, dialog);
        if (null != activity) {
            result = showDialog(activity, dialog, force);
        }
        UIBaseUtil.log("%s showDialogWithTopActivity-System end = %s", TAG, result);
        return result;
    }

    /**
     * 显示Dialog至顶部Activity
     *
     * @param dialog dialog对象
     * @return 是否强制打开
     */
    public static boolean showDialogWithTopActivity(android.support.v4.app.DialogFragment dialog, boolean force) {
        boolean result = false;
        Activity activity = AppManager.getInstance().getCurrentActivityIfExist();
        UIBaseUtil.log("%s showDialogWithTopActivity-Support start (activity,dialog) = %s,%s", TAG, activity, dialog);
        if (null != activity && activity instanceof FragmentActivity) {
            result = showDialog((FragmentActivity) activity, dialog, force);
        }
        UIBaseUtil.log("%s showDialogWithTopActivity-Support end = %s", TAG, result);
        return result;
    }

    /**
     * 显示Dialog至指定Activity
     *
     * @param activity activity对象
     * @param dialog   dialog对象
     * @return 是否成功
     */
    public static boolean showDialog(Activity activity, DialogFragment dialog) {
        return showDialog(activity, dialog, true);
    }

    /**
     * 显示Dialog至指定Activity
     *
     * @param activity activity对象
     * @param dialog   dialog对象
     * @return 是否成功
     */
    public static boolean showDialog(FragmentActivity activity, android.support.v4.app.DialogFragment dialog) {
        return showDialog(activity, dialog, true);
    }

    /**
     * 显示Dialog至指定Activity
     *
     * @param activity activity对象
     * @param dialog   dialog对象
     * @param force    是否强制打开
     * @return 是否成功
     */
    public static boolean showDialog(Activity activity, DialogFragment dialog, boolean force) {
        UIBaseUtil.log("%s showDialog start-System (activity,dialog,force) = %s,%s,%s", TAG, activity, dialog, force);
        boolean result = false;
        if (null != activity && null != dialog) {
            try {
                FragmentManager fragmentManager = activity.getFragmentManager();
                if (force) {
                    // 忽略Activity的onSaveInstanceState
                    fragmentManager.beginTransaction().add(dialog, null).commitAllowingStateLoss();
                } else {
                    dialog.show(fragmentManager, null);
                }
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
                UIBaseUtil.log("%s showDialog-System 异常-显示", TAG);
            }
        }
        UIBaseUtil.log("%s showDialog-System end = %s", TAG, result);
        return result;
    }

    /**
     * 显示Dialog至指定Activity
     *
     * @param activity activity对象
     * @param dialog   dialog对象
     * @param force    是否强制打开
     * @return 是否成功
     */
    public static boolean showDialog(FragmentActivity activity, android.support.v4.app.DialogFragment dialog, boolean force) {
        UIBaseUtil.log("%s showDialog-Support start (activity,dialog,force) = %s,%s,%s", TAG, activity, dialog, force);
        boolean result = false;
        if (null != activity && null != dialog) {
            try {
                android.support.v4.app.FragmentManager fragmentManager = activity.getSupportFragmentManager();
                if (force) {
                    // 忽略Activity的onSaveInstanceState
                    fragmentManager.beginTransaction().add(dialog, null).commitAllowingStateLoss();
                } else {
                    dialog.show(fragmentManager, null);
                }
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
                UIBaseUtil.log("%s showDialog-Support 异常-显示", TAG);
            }
        }
        UIBaseUtil.log("%s showDialog-Support end = %s", TAG, result);
        return result;
    }

    /**
     * 隐藏Dialog
     *
     * @param dialog dialog对象
     * @return 是否成功
     */
    public static boolean hideDialog(DialogFragment dialog) {
        UIBaseUtil.log("%s hideDialog-System start (dialog) = %s", TAG, dialog);
        boolean result = false;
        if (null != dialog) {
            try {
                dialog.dismissAllowingStateLoss();
                result = true;
            } catch (Exception e) {
                UIBaseUtil.log("%s hideDialog-System 异常-隐藏", TAG);
                e.printStackTrace();
            }
        }
        UIBaseUtil.log("%s hideDialog-System end = %s", TAG, result);
        return result;
    }

    /**
     * 隐藏Dialog
     *
     * @param dialog dialog对象
     * @return 是否成功
     */
    public static boolean hideDialog(android.support.v4.app.DialogFragment dialog) {
        UIBaseUtil.log("%s hideDialog-Support start (dialog) = %s", TAG, dialog);
        boolean result = false;
        if (null != dialog) {
            try {
                dialog.dismissAllowingStateLoss();
                result = true;
            } catch (Exception e) {
                UIBaseUtil.log("%s hideDialog-Support 异常-隐藏", TAG);
                e.printStackTrace();
            }
        }
        UIBaseUtil.log("%s hideDialog-Support end = %s", TAG, result);
        return result;
    }

    /**
     * Dialog是否显示
     *
     * @param dialog dialog对象
     * @return 是否显示
     */
    public static boolean isDialogShowing(DialogFragment dialog) {
        boolean result = false;
        if (null != dialog && null != dialog.getDialog() && dialog.getDialog().isShowing()) {
            result = true;
        }
        UIBaseUtil.log("%s isDialogShowing-System (dialog) = %s", TAG, dialog);
        return result;
    }

    /**
     * Dialog是否显示
     *
     * @param dialog dialog对象
     * @return 是否显示
     */
    public static boolean isDialogShowing(android.support.v4.app.DialogFragment dialog) {
        boolean result = false;
        if (null != dialog && null != dialog.getDialog() && dialog.getDialog().isShowing()) {
            result = true;
        }
        UIBaseUtil.log("%s isDialogShowing-Support (dialog) = %s", TAG, dialog);
        return result;
    }

    /**
     * 发送Broadcast
     *
     * @param context    context上下文
     * @param intent     intent意图
     * @param permission receiver权限
     * @return 是否成功
     */
    public static boolean sendBroadcast(Context context, Intent intent, String permission) {
        UIBaseUtil.log("%s sendBroadcast start (context,intent,permission) = %s,%s,%s", TAG, context, intent, permission);
        boolean result = false;
        if (null != context && null != intent) {
            try {
                if (null != permission) {
                    context.sendBroadcast(intent, permission);
                } else {
                    context.sendBroadcast(intent);
                }
            } catch (Exception e) {
                UIBaseUtil.log("%s sendBroadcast 异常-发送", TAG);
                e.printStackTrace();
            }
        }
        UIBaseUtil.log("%s sendBroadcast end = %s", TAG, result);
        return result;
    }

}
