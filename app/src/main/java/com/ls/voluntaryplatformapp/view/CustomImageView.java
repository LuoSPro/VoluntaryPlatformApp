package com.ls.voluntaryplatformapp.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ls.libcommon.utils.PixUtils;

/**
 * ImageView不能直接使用DataBinding去设置URL，所以我们这里自定义一个ImageView，并且使用 @BindingAdapter 注解，这是dataBinding给的注解
 * 然后在括号里面写入要接收的参数
 */
public class CustomImageView extends AppCompatImageView {
    public CustomImageView(@NonNull Context context) {
        this(context, null);
    }

    public CustomImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * requireAll = true
     * 表明，我们要把value的两个值都在xml中写上，才会调用到这个方法里面
     *
     * @param view
     * @param imageUrl
     * @param isCircle
     */
    @BindingAdapter(value = {"image_url", "isCircle"}, requireAll = false)
    public static void setImageUrl(CustomImageView view, String imageUrl, boolean isCircle) {
        RequestBuilder<Drawable> builder = Glide.with(view).load(imageUrl);
        if (isCircle) {//圆形图片
            builder.transform(new CircleCrop());
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (view.getLayoutParams() != null && layoutParams.width > 0 && layoutParams.height > 0) {
            //防止图片很大，而我们所需的图片大小很小的情况
            builder.override(layoutParams.width, layoutParams.height);
        }
        builder.into(view);
    }

    /**
     *
     * 根据经验：dataBinding在绑定数据的时候，不是立刻立马执行的，而是延迟了一帧
     * 因为在绑定的时候，内部调用了mChoreographer.postFrameCallback(mFrameCallback)
     * 也就是说，他post了一个callback，然后回调的方法中绑定的数据，所以在列表中，有的组件宽高都是
     * 不确定的，需要动态计算的，这时候就不建议使用dataBinding了，而推荐使用原生的方式
     * @param widthPx
     * @param heightPx
     * @param marginLeft
     * @param imageUrl
     */
    public void bindData(int widthPx, int heightPx, int marginLeft, String imageUrl) {
        this.bindData(widthPx, heightPx, marginLeft, PixUtils.getScreenWidth(), PixUtils.getScreenHeight(),imageUrl);
    }


    /**
     * 如果图片高度大于宽度，左边需要margin，
     * 如果图片宽度大于高度，就不需要边界的margin
     * @param widthPx 图片的宽度
     * @param heightPx 图片的高度
     * @param marginLeft 高度大于宽度时所需的margin值
     * @param maxWidth
     * @param maxHeight
     * @param imageUrl 图片url
     */
    public void bindData(int widthPx, int heightPx, int marginLeft, int maxWidth, int maxHeight, String imageUrl) {
        //如果没有url，说明是纯文本的说说，不需要展示
        if (TextUtils.isEmpty(imageUrl)) {
            setVisibility(GONE);
            return;
        } else {
            setVisibility(VISIBLE);
        }
        if (widthPx <= 0 || heightPx <= 0) {//如果图片的宽高小于0，就根据图片的资源的设置
            Glide.with(this).load(imageUrl).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    int height = resource.getIntrinsicHeight();
                    int width = resource.getIntrinsicWidth();
                    setSize(width, height, marginLeft, maxWidth, maxHeight);

                    setImageDrawable(resource);
                }
            });
            return;
        }
        setSize(widthPx,heightPx,marginLeft,maxWidth,maxHeight);
        setImageUrl(this,imageUrl,false);
    }

    /**
     * 去真正设置CustomImageView的真正宽高
     * @param width
     * @param height
     * @param marginLeft
     * @param maxWidth
     * @param maxHeight
     */
    private void setSize(int width, int height, int marginLeft, int maxWidth, int maxHeight) {
        int finalWidth, finalHeight;
        if (width > height) {//如果宽度大于高度
            finalWidth = maxWidth;
            finalHeight = (int) (height / (width * 1.0f / finalWidth));//高度按比例缩小
        } else {
            finalHeight = maxHeight;
            finalWidth = (int) (width / (height * 1.0f / finalHeight));//宽度按比例缩小
        }
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = finalWidth;
        params.height = finalHeight;
        //这里要做强制类型转换，因为当params为FrameLayout.LayoutParams的子类实例时，将其强制转换为LinearLayout.LayoutParams
        //会报错：android.view.ViewGroup$MarginLayoutParams cannot be cast to android.widget.LinearLayout$LayoutParams
        if (params instanceof FrameLayout.LayoutParams) {
            ((FrameLayout.LayoutParams) params).leftMargin = height > width ? PixUtils.dp2px(marginLeft) : 0;
        } else if (params instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) params).leftMargin = height > width ? PixUtils.dp2px(marginLeft) : 0;
        }
//        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(finalWidth, finalHeight);
//        params.leftMargin = height > width ? PixUtils.dp2px(marginLeft) : 0;//当高度大于宽度的时候，设置旁边的margin，当宽度大于高度的时候，占满屏幕宽度
        setLayoutParams(params);
    }

    public void setBlurImageUrl(String coverUrl, int radius) {
        //override:不需要太大的图片，而是一个小尺寸的,这样性能更高一些
        Glide.with(this).load(coverUrl).override(50)
                .transform()
                .dontAnimate()
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        //使用setImageDrawable();可能会撑不满
                        setBackground(resource);
                    }
                });
    }
}
