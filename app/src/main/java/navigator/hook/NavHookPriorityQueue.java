package navigator.hook;

import android.support.annotation.NonNull;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;


/**
 * 处理hook优先级的。
 * <p>
 * Created by leo on 16/11/10.
 */

public class NavHookPriorityQueue {

    private LinkedHashMap<NavHook.HookType, HashSet<NavHook>> mType2HookListMap = new LinkedHashMap<>();

    private LinkedHashSet<NavHook> mSchemeHostNavHookList = new LinkedHashSet<>();
    private LinkedHashSet<NavHook> mSchemeNavHookList = new LinkedHashSet<>();
    private LinkedHashSet<NavHook> mHostNavHookList = new LinkedHashSet<>();
    private LinkedHashSet<NavHook> mPatternNavHookList = new LinkedHashSet<>();

    public NavHookPriorityQueue() {
        mType2HookListMap.put(NavHook.HookType.SCHEME_HOST, mSchemeHostNavHookList);
        mType2HookListMap.put(NavHook.HookType.SCHEME, mSchemeNavHookList);
        mType2HookListMap.put(NavHook.HookType.HOST, mHostNavHookList);
        mType2HookListMap.put(NavHook.HookType.PATTERN, mPatternNavHookList);
    }

    public boolean contains(NavHook hook) {
        if (null != hook) {
            for (Map.Entry<NavHook.HookType, HashSet<NavHook>> entry : mType2HookListMap.entrySet()) {
                boolean exist = entry.getValue().contains(hook);
                if (exist) {
                    return true;
                }
            }
        }
        return false;
    }

    public void add(@NonNull NavHook hook) {
        mType2HookListMap.get(hook.getType())
                .add(hook);
    }

    public void remove(@NonNull NavHook hook) {
        mType2HookListMap.get(hook.getType())
                .remove(hook);
    }

    public void clear() {
        mSchemeHostNavHookList.clear();
        mSchemeNavHookList.clear();
        mHostNavHookList.clear();
        mPatternNavHookList.clear();
    }

    public Set<NavHook> getQueue() {
        HashSet<NavHook> queue = new HashSet<>();
        for (Map.Entry<NavHook.HookType, HashSet<NavHook>> entry : mType2HookListMap.entrySet()) {
            queue.addAll(entry.getValue());
        }
        return queue;
    }

}
