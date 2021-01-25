/**
 *
 */
package cn.zhonggeng.park;

import android.content.Context;
import android.util.Log;

import com.tencent.mmkv.MMKV;

public class PreferenceUtil {
    final static String TAG = "mmkv";
    public enum SP_KEY {
        TH_TABLE("zhonggeng_park");

        public final String value;
        public MMKV mInstance;

        SP_KEY(String value) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }

        public MMKV getInstance(){
            if(mInstance == null){
                mInstance = MMKV.mmkvWithID(value);
            }
            return mInstance;
        }
    }

    public static boolean getBoolean(Context context, String key, boolean defValue, SP_KEY name) {
        // 获取SharedPreferences对象
        boolean value = name.getInstance().getBoolean(key, defValue);
        Log.d(TAG, "mmkv decode key: " + key + "Val : " + value);
        return value;
    }

    public static void setBoolean(Context context, String key, boolean value, SP_KEY name) {
        Log.d(TAG, "mmkv encode key: " + key + "Val : " + value);
        name.getInstance().encode(key, value);
    }

    public static int getInt(Context context, String key, int defValue, SP_KEY name) {
        // 获取SharedPreferences对象
        int value = name.getInstance().getInt(key,defValue);
        Log.d(TAG, "mmkv decode key: " + key + "Val : " + value);
        return value;
    }

    public static void setInt(Context context, String key, int value, SP_KEY name) {
        name.getInstance().encode(key,value);
    }

    public static String getString(Context context, String key, String defValue, SP_KEY name) {
        // 获取SharedPreferences对象
        String value = name.getInstance().getString(key, defValue);
        Log.d(TAG, "mmkv decode key: " + key + "Val : " + value);
        return value;
    }

    public static void setString(Context context, String key, String value, SP_KEY name) {
        Log.d(TAG, "mmkv encode key: " + key + "Val : " + value);
        name.getInstance().encode(key, value);
    }


    public static long getLong(Context context, String key, long defValue, SP_KEY name) {
        // 获取SharedPreferences对象
        long value = name.getInstance().getLong(key, defValue);
        Log.d(TAG, "mmkv encode key: " + key + "Val : " + value);
        return value;
    }

    public static void setLong(Context context, String key, long value, SP_KEY name) {
        Log.d(TAG, "mmkv encode key: " + key + "Val : " + value);
        name.getInstance().encode(key, value);
    }

    public static void clearAll(Context context, SP_KEY name) {
        Log.d(TAG, "mmkv clear all " + name.toString() );
        name.getInstance().clearAll();
    }

    public static void remove(Context context, String key, int i, SP_KEY name) {
        Log.d(TAG, "mmkv remove " + name.toString() + " " + key);
        name.getInstance().remove(key);
    }
}
