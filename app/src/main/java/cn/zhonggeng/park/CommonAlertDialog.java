package cn.zhonggeng.park;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created  on 2016/6/29 16:44:47.
 * Author:  Yangkang
 * Description:
 * 通用的提醒对话框 圆角的背景
 * 自己设置标题 -- 可以不设置
 * 内容 -- 必须设置 圆角问题
 * 确认对话框
 * 取消对话框
 * <p>
 * A  B  C 三种样式
 * 首先需要设置样式, 如果是A默认样式则不需要设置样式
 * A样式: 为默认样式  可以设置左右按钮的文字, 可以设置字体的颜色
 * B样式 : 自己设置 可以设置左右按钮的文字, 可以设置字体的颜色
 * C样式 : 只能设置内容
 * 标题 统一为  温馨提示(不可设置,白底红字)
 * <p>
 * 左右两侧的点击事件
 * 左侧(取消) {@link OnLeftCancelListener}
 * 右侧(确认) {@link OnRightConfirmListener}
 */
public class CommonAlertDialog extends Dialog {
    /**
     * A样式 没有 温馨提示标题
     * 提示内容  取消 和 确定 按钮
     * A样式 可以设置左右按钮的文字, 可以设置字体的颜色
     */
    public final static int STYLE_A = 0x00000001;

    /**
     * B样式 有 温馨提示标题
     * 有 提示内容
     * B样式 可以设置左右按钮的文字, 可以设置字体的颜色
     */
    public final static int STYLE_B = 0x00000002;

    /**
     * C样式 有 温馨提示标题
     * 有 提示内容  没有左右文字
     */
    public final static int STYLE_C = 0x00000003;

    /**
     * D样式 有 温馨提示标题
     * 有 提示内容
     * 有 内容说明
     * B样式 可以设置左右按钮的文字, 可以设置字体的颜色
     */
    public final static int STYLE_D = 0x00000004;

    /**
     * E样式
     * 只有温馨提示标题 + 一个确定按钮
     */
    public final static int STYLE_E = 0x00000005;

    /**
     * F样式 没有 温馨提示标题
     * 没有 提示内容
     * 没有 内容说明
     * 有图片内容
     * 可以设置左右按钮的文字, 可以设置字体的颜色
     */
    public final static int STYLE_F = 0x00000006;

    /**
     * 自定义标题与内容
     * 没有取消按钮
     */
    public final static int STYLE_G = 0x00000007;

    /**
     * 没有标题，没有取消按钮
     */
    public final static int STYLE_H = 0x00000008;


    /**
     * 默认样式为 STYLE_A 样式
     */
    public final static int STYLE_DEFAULT = STYLE_A;

    public CommonAlertDialog(Context context) {
        super(context);
    }

    public CommonAlertDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public CommonAlertDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @IntDef({STYLE_A, STYLE_B, STYLE_C, STYLE_D, STYLE_E, STYLE_F, STYLE_G,STYLE_H})
    @Retention(RetentionPolicy.SOURCE)
    public @interface StyleType {
    }

    public interface OnLeftCancelListener extends DialogInterface.OnCancelListener {
    }

    public interface OnRightConfirmListener  {
        void onCancel(String content,Dialog dialog);
    }

    public interface OnSelectedTextListener {
        void onClick(View view, String mUrl);
    }

    public static class Builder {
        private Activity mActivity;
        private float mTitleTextSize;
        private float mContentTextSize;
        private float mHintTextSize;
        private float mButtonTextSize;
        private String mContent;
        private boolean isHTML;
        private String mContentHint;
        private String mContentHintColor;
        private String mContentImgUrl;
        private float imgRatio; // 高度/宽度 比例
        private int mGravity = Gravity.CENTER;
        private int mHintGravity = Gravity.LEFT;
        private OnLeftCancelListener mOnLeftCancelListener;
        private OnRightConfirmListener mOnRightConfirmListener;
        private OnSelectedTextListener mOnSelectedTextListener;
        private String mTitleText;
        private String mLeftButtonText;
        private String mLeftButtonColor;
        private String mRightButtonText;
        private String mRightButtonColor;
        private boolean mBoldText;
        /**
         * 默认样式
         */
        @StyleType
        private int defaultStyle = STYLE_DEFAULT;

        public Builder(@NonNull Activity activity) {
            this.mActivity = activity;
        }

        public CommonAlertDialog create() {
            // 设置对话框布局
            LayoutInflater inflater = LayoutInflater.from(mActivity);
            View rootView = inflater.inflate(R.layout.layout_dialog_input_name, null);
            final CommonAlertDialog dialog = new CommonAlertDialog(mActivity, R.style.MyDialogStyleBottomtishi);
            dialog.setContentView(rootView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            dialog.setOwnerActivity(mActivity);

            final EditText tvContent = rootView.findViewById(R.id.edit_text);

            Button btnCancel = rootView.findViewById(R.id.btn_cancel);
            Button btnConfirm = rootView.findViewById(R.id.btn_confirm);


            // 爱设置内容
            if (!TextUtils.isEmpty(mContent)) {
                if (isHTML) {
                    tvContent.setText(Html.fromHtml(mContent));
                    if (null != mOnSelectedTextListener) {
                        tvContent.setMovementMethod(LinkMovementMethod.getInstance());
                        CharSequence text = tvContent.getText();
                        if (text instanceof Spannable) {
                            int end = text.length();
                            Spannable sp = (Spannable) tvContent.getText();
                            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
                            SpannableStringBuilder style = new SpannableStringBuilder(text);
                            style.clearSpans();
                            for (URLSpan url : urls) {
                                HtmlURLSpan myURLSpan = new HtmlURLSpan(url.getURL(), mOnSelectedTextListener);
                                style.setSpan(myURLSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                            tvContent.setText(style);
                        }
                    }
                } else {
                    tvContent.setText(mContent);
                }
            }

            // 取消
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mOnLeftCancelListener) {
                        mOnLeftCancelListener.onCancel(dialog);
                    }
                }
            });

            // 确定
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mOnRightConfirmListener) {
                        mOnRightConfirmListener.onCancel(tvContent.getText().toString(),dialog);
                    }
                }
            });

            return dialog;
        }

        /**
         * 设置字体大小
         */
        private void setTextSize(TextView title, TextView content, TextView hint,
                                 TextView buttonLeft, TextView buttonRight) {
            if (mTitleTextSize > 0) {
                title.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTitleTextSize);
            }
            if (mContentTextSize > 0) {
                content.setTextSize(TypedValue.COMPLEX_UNIT_SP, mContentTextSize);
            }
            if (mHintTextSize > 0) {
                hint.setTextSize(TypedValue.COMPLEX_UNIT_SP, mHintTextSize);
            }
            if (mButtonTextSize > 0) {
                buttonLeft.setTextSize(TypedValue.COMPLEX_UNIT_SP, mButtonTextSize);
                buttonRight.setTextSize(TypedValue.COMPLEX_UNIT_SP, mButtonTextSize);
            }
        }

        /**
         * 设置左右按钮的字体和颜色
         *
         * @param titleText  标题文字
         * @param btnCancel  左侧取消按钮
         * @param btnConfirm 右侧确认按钮
         */
        private void setButtonTextAndColor(TextView titleText, Button btnCancel, Button btnConfirm) {
            // 如果设置了颜色就显示颜色
            if (!TextUtils.isEmpty(mLeftButtonColor)) {
                btnCancel.setTextColor(Color.parseColor(mLeftButtonColor));
            }
            if (!TextUtils.isEmpty(mRightButtonColor)) {
                btnConfirm.setTextColor(Color.parseColor(mRightButtonColor));
            }
            // 设置标题的文字
            if (!TextUtils.isEmpty(mTitleText)) {
                titleText.setText(mTitleText);
            }
            // 设置左右按钮的字体
            if (!TextUtils.isEmpty(mLeftButtonText)) {
                btnCancel.setText(mLeftButtonText);
            }
            if (!TextUtils.isEmpty(mRightButtonText)) {
                btnConfirm.setText(mRightButtonText);
            }
        }

        /**
         * 设置对话框的默认样式 如果不设置 默认为true
         * 只有提示内容(自己设置)  左(取消 -- 固定写死)  右(确定 -- 固定写死)
         *
         * @param defaultStyle 是否是默认样式
         */
        public Builder setDefaultStyle(@StyleType int defaultStyle) {
            this.defaultStyle = defaultStyle;
            return this;
        }

        /**
         * 设置标题的字体大小  单位 sp
         */
        public Builder setTitleTextSize(float sp) {
            this.mTitleTextSize = sp;
            return this;
        }

        /**
         * 设置标题的字体大小  单位 sp
         */
        public Builder setContentTextSize(float sp) {
            this.mContentTextSize = sp;
            return this;
        }

        /**
         * 设置提示的字体大小  单位 sp
         */
        public Builder setHintTextSize(float sp) {
            this.mHintTextSize = sp;
            return this;
        }

        /**
         * 设置标题的字体大小  单位 sp
         */
        public Builder setButtonTextSize(float sp) {
            this.mButtonTextSize = sp;
            return this;
        }

        /**
         * 设置提示内容
         */
        public Builder setAlertContent(String content) {
            this.mContent = content;
            return this;
        }

        /**
         * 设置提示内容
         */
        public Builder isHTMLAlertContent(boolean isHTML) {
            this.isHTML = isHTML;
            return this;
        }

        /**
         * 设置内容位置
         */
        public Builder setAlertContentPosition(int gravity) {
            this.mGravity = gravity;
            return this;
        }

        /**
         * 设置提示的位置
         */
        public Builder setAlertHintPosition(int gravity) {
            this.mHintGravity = gravity;
            return this;
        }

        /**
         * 设置提示内容的提示
         *
         * @param contentHint 内容提示
         */
        public Builder setAlertContentHint(String contentHint) {
            this.mContentHint = contentHint;
            return this;
        }

        /**
         * 设置提示内容的提示
         *
         * @param contentHintTextColor 内容提示文字颜色
         */
        public Builder setAlertContentHintTextColor(String contentHintTextColor) {
            this.mContentHintColor = contentHintTextColor;
            return this;
        }

        /**
         * 设置弹窗内容图片
         *
         * @param mContentImgUrl 图片链接
         */
        public Builder setAlertContentImgUrl(String mContentImgUrl) {
            this.mContentImgUrl = mContentImgUrl;
            return this;
        }

        /**
         * 设置弹窗图片尺寸
         *
         * @param imgRatio 高度与宽度的比例
         */
        public Builder setImgRatio(float imgRatio) {
            this.imgRatio = imgRatio;
            return this;
        }

        /**
         * 设置左侧按钮的内容 默认是"取消"
         *
         * @param text 左侧按钮的内容
         */
        public Builder setLeftButtonText(String text) {
            switch (defaultStyle) {
                case STYLE_A:
                case STYLE_B:
                case STYLE_D:
                case STYLE_F:// ABD样式都可以设置左右两侧按钮的文字
                    this.mLeftButtonText = text;
                    break;

                case STYLE_C:
                case STYLE_H:
                default:
                    break;
            }
            return this;
        }

        /**
         * 设置设置标题文字 默认是"温馨提示"
         */
        public Builder setTitleText(String text) {
            switch (defaultStyle) {
                case STYLE_A:
                    break;
                case STYLE_B: // BC样式都可以设置标题文字
                case STYLE_C:
                case STYLE_D:
                case STYLE_G:
                    this.mTitleText = text;
                    break;
                case STYLE_H:
                default:
                    break;
            }
            return this;
        }

        /**
         * 设置左侧按钮的字体颜色 默认是黑色
         *
         * @param textColor 左侧按钮的文本颜色 #999999
         */
        public Builder setLeftButtonTextColor(String textColor) {
            switch (defaultStyle) {
                case STYLE_A: // AB样式都可以设置左右两侧文字的颜色
                case STYLE_B:
                    this.mLeftButtonColor = textColor;
                    break;

                case STYLE_C:
                case STYLE_H:
                default:
                    break;
            }
            return this;
        }

        /**
         * 设置右侧按钮的内容 默认是"确定"
         *
         * @param text 右侧按钮的内容
         */
        public Builder setRightButtonText(String text) {
            switch (defaultStyle) {
                case STYLE_A:
                case STYLE_B:
                case STYLE_D:
                case STYLE_E:
                case STYLE_F:
                case STYLE_G:// ABD样式都可以设置左右两侧按钮的文字
                case STYLE_H:
                    this.mRightButtonText = text;
                    break;

                case STYLE_C:
                default:
                    break;
            }
            return this;
        }

        /**
         * 设置右侧按钮的字体颜色 默认是红色
         *
         * @param textColor 右侧按钮的文本颜色 #999999
         */
        public Builder setRightButtonTextColor(String textColor) {
            switch (defaultStyle) {
                case STYLE_A:   // AB样式都可以设置左右两侧文字的颜色
                case STYLE_B:
                    this.mRightButtonColor = textColor;
                    break;

                case STYLE_C:
                default:
                    break;
            }
            return this;
        }

        /**
         * 设置左右字体加粗
         *
         * @param bold 是否加粗
         */
        public Builder setEnableTextBold(boolean bold) {
            switch (defaultStyle) {
                case STYLE_A:
                case STYLE_F:// 只有AF样式可以设置字体加粗
                    this.mBoldText = bold;
                    break;

                case STYLE_B:
                case STYLE_C:
                default:
                    break;
            }
            return this;
        }

        /**
         * 左侧"取消" 点击事件
         */
        public Builder setOnLeftCancelListener(OnLeftCancelListener listener) {
            this.mOnLeftCancelListener = listener;
            return this;
        }

        /**
         * 右侧"确定" 点击事件
         */
        public Builder setOnRightConfirmListener(OnRightConfirmListener listener) {
            this.mOnRightConfirmListener = listener;
            return this;
        }

        /**
         * 点击选中的文本 点击事件
         *
         * @return
         */
        public Builder setOnSelectedTextListener(OnSelectedTextListener listener) {
            this.mOnSelectedTextListener = listener;
            return this;
        }

        private class HtmlURLSpan extends ClickableSpan {

            private String mUrl;
            private OnSelectedTextListener mOnSelectedTextListener;

            HtmlURLSpan(String url, OnSelectedTextListener mOnSelectedTextListener) {
                mUrl = url;
                this.mOnSelectedTextListener = mOnSelectedTextListener;
            }

            @Override
            public void onClick(View widget) {
                mOnSelectedTextListener.onClick(widget,mUrl);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#527db0"));
                ds.setUnderlineText(false);
            }
        }
    }
}
