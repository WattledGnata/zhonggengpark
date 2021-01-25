package cn.zhonggeng.park;

import android.content.res.Resources;

public class Utils {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)  不需要传context
     */
    public static int dp2px(float dpValue) {
        return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }

}
