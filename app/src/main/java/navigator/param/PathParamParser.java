package navigator.param;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Create on 2017/03/28.
 *
 * 解析path中的id ， 诸如  xiami://album/123  中的123
 *
 * @author xinpeng.xiao
 * @version 1.0.0
 */
public class PathParamParser {
    public static final String KEY_OLD_SCHEME_ARG = "oldSchemeArg";
    private static final Pattern PATH_ID_PATTERN = Pattern.compile("([a-z0-9A-Z_]+)://([a-z0-9A-Z_]+)/([0-9]+)(.*)");
    private static final Pattern PATH_ID_STR_PATTERN = Pattern.compile("([a-z0-9A-Z_]+)://([a-z0-9A-Z_]+)/([a-z0-9A-Z_]+)(.*)");

    /**
     * 比如：
     *     输入 xiami://album/123
     *     返回123   <-- oldSchemeArg
     */
    public static @Nullable
    Object getOldSchemeArg(@NonNull Uri uri) {
        Matcher m = PATH_ID_PATTERN.matcher(uri.toString());
        if (m.find()) {
            try {
                return Long.valueOf(m.group(3));
            } catch (NumberFormatException e) {
                //
            }
        } else {
            Matcher matcherStr = PATH_ID_STR_PATTERN.matcher(uri.toString());
            if (matcherStr.find()) {
                return matcherStr.group(3);
            }
        }

        return null;
    }
}
