package cc.linkedme.linkaccountdemo;

import android.content.Context;
import android.util.TypedValue;

/**
 * Android系统相关工具类
 *
 * Created by LinkedME06 on 13/01/2017.
 */

public class AndroidUtils {

    /**
     * dp转px
     *
     * @param context Context
     * @param dp      dp
     * @return px
     */
    public static int convertDpToPixels(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     *
     * @param context Context
     * @param px      pixel
     * @return dp
     */
    public static int convertPxToDp(Context context, float px) {
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }

    /**
     * sp转px
     *
     * @param context Context
     * @param sp      sp
     * @return px
     */
    public static int convertSpToPixels(Context context, float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

}
