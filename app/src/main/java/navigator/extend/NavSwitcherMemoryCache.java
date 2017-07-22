package navigator.extend;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import navigator.util.NavLogger;

/**
 * NavSwitcher --> NavSwitcherMemoryCache
 * <p>
 * Created by leo on 16/6/1.
 * 导航转换器
 * 用于源地址、目标地址之间的正则匹配动态切换
 */
@Deprecated
public class NavSwitcherMemoryCache {

    private static final String TAG = "NavSwitcherMemoryCache";

    /**
     * 目前用于保存orange下发的路由映射表
     * <p>
     * 地址映射表(用于Url转换)
     * <p/>
     * key:源地址,完整表达式or正则表达式,目前匹配四种来源(Native页面、Rn页面、H5页面、SchemeUrl)
     * 1.Native页面,页面Class类全限定名
     * 2.Rn页面,页面Class类全限定名
     * 3.H5页面,页面Url字符地址
     * 4.SchemeUrl字符串
     * <p/>
     * value:目标地址,字符串,强烈建议SchemeUrl,统一所有目标输出方式
     */
    private static HashMap<String, String> mUrlSwitcherMapComplete = new HashMap<>();
    private static HashMap<String, String> mUrlSwitcherMapRegular = new HashMap<>();

    /**
     * 尝试切换(优先完整匹配,再正则匹配)
     *
     * @param url 源地址
     * @return 目标地址, 如果null, 则未匹配上, 否则匹配切换成目标地址
     */
    public static String get(String url) {
        if (null != url) {
            // 完整映射匹配
            for (Map.Entry<String, String> entry : mUrlSwitcherMapComplete.entrySet()) {
                String key = entry.getKey();
                if (key.equals(url)) {
                    String value = entry.getValue();
                    if (NavLogger.mEnable) {
                        NavLogger.log("%s trySwitch(完整) (url,key,value) = %s >> %s >> %s", TAG, url, key, value);
                    }
                    return value;
                }
            }
            // 正则映射匹配
            for (Map.Entry<String, String> entry : mUrlSwitcherMapRegular.entrySet()) {
                String key = entry.getKey();
                if (Pattern.matches(key, url)) {
                    String value = entry.getValue();
                    if (NavLogger.mEnable) {
                        NavLogger.log("%s trySwitch(正则) (url,key,value) = %s >> %s >> %s", TAG, url, key, value);
                    }
                    return value;
                }
            }
        }

        if (NavLogger.mEnable) {
            NavLogger.log("%s trySwitch(失败) (url) = %s", TAG, url);
        }
        return null;
    }

    /**
     * 增加UrlSwitcher(增加至complete或regular队列)
     *
     * @param key     源地址
     * @param value   切换地址
     * @param regular 正则模式
     * @return 是否成功
     */
    public static void put(String key, String value, boolean regular) {
        if (null == key || null == value) {
            return;
        }

        if (regular) {
            mUrlSwitcherMapRegular.put(key, value);
            if (NavLogger.mEnable) {
                NavLogger.log("%s addUrlSwitcher(正则) (key,value) = %s >> %s", TAG, key, value);
            }
        } else {
            if (NavLogger.mEnable) {
                NavLogger.log("%s addUrlSwitcher(完整) (key,value) = %s >> %s", TAG, key, value);
            }
            mUrlSwitcherMapComplete.put(key, value);
        }
    }


    /**
     * 清除所有UrlSwitcher(complete/regular)
     */
    public static void clear() {
        mUrlSwitcherMapRegular.clear();
        mUrlSwitcherMapComplete.clear();

        if (NavLogger.mEnable) {
            NavLogger.log("%s clearUrlSwitchers", TAG);
        }
    }
}
