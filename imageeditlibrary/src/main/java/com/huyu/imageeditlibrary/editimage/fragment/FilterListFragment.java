package com.huyu.imageeditlibrary.editimage.fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.huyu.imageeditlibrary.BaseActivity;
import com.huyu.imageeditlibrary.R;
import com.huyu.imageeditlibrary.editimage.ModuleConfig;
import com.huyu.imageeditlibrary.editimage.fliter.PhotoProcessing;
import com.huyu.imageeditlibrary.editimage.view.imagezoom.ImageViewTouchBase;

import java.util.ArrayList;


/**
 * 滤镜列表fragment
 *
 * @author panyi
 */
public class FilterListFragment extends BaseEditFragment {
    public static final int INDEX = ModuleConfig.INDEX_FILTER;
    public static final String TAG = FilterListFragment.class.getName();
    private ArrayList<ImageView> iviews = new ArrayList<>();
    private View mainView;
    private View backBtn;// 返回主菜单按钮

    private Bitmap fliterBit;// 滤镜处理后的bitmap

    private LinearLayout mFilterGroup;// 滤镜列表
    private String[] fliters;
    private Bitmap currentBitmap;// 标记变量

    public static FilterListFragment newInstance() {
        FilterListFragment fragment = new FilterListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_edit_image_fliter, null);
        return mainView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        backBtn = mainView.findViewById(R.id.back_to_main);
        mFilterGroup = (LinearLayout) mainView.findViewById(R.id.filter_group);

        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMain();
            }
        });
        setUpFliters();
    }

    @Override
    public void onResume() {
        super.onResume();
        onShow();
    }

    @Override
    public void onShow() {
        //activity.mode = EditImageActivity.MODE_FILTER;
        activity.mFilterListFragment.setCurrentBitmap(activity.getMainBit());
        activity.mainImage.setImageBitmap(activity.getMainBit());
        activity.mainImage.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        activity.mainImage.setScaleEnabled(false);
        activity.bannerFlipper.showNext();
    }

    /**
     * 返回主菜单
     */
    @Override
    public void backToMain() {
        currentBitmap = activity.getMainBit();
        fliterBit = null;
        activity.mainImage.setImageBitmap(activity.getMainBit());// 返回原图
        //activity.mode = EditImageActivity.MODE_NONE;
        //activity.bottomGallery.setCurrentItem(0);
        activity.mainImage.setScaleEnabled(true);
        //activity.bannerFlipper.showPrevious();
    }

    /**
     * 保存滤镜处理后的图片
     */
    public void applyFilterImage() {
        // System.out.println("保存滤镜处理后的图片");
        if (currentBitmap == activity.getMainBit()) {// 原始图片
            // System.out.println("原始图片");
            backToMain();
            return;
        } else {// 经滤镜处理后的图片
            // System.out.println("滤镜图片");
            activity.changeMainBitmap(fliterBit,true);
            backToMain();
        }// end if
    }

    /**
     * 装载滤镜
     */
    private void setUpFliters() {
        fliters = getResources().getStringArray(R.array.filters);
        if (fliters == null)
            return;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                dip2px(43),
                dip2px(43));
        params.gravity = Gravity.CENTER_VERTICAL;
        params.leftMargin = 10;
        params.rightMargin = 10;
        mFilterGroup.removeAllViews();
        for (int i = 0, len = fliters.length; i < len; i++) {
            ImageView imageView = new ImageView(activity);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY );
            switch (fliters[i]){
                case "原始":
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.lj01));
                    break;
                case "软化":
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.lj02));
                    break;
                case "黑白":
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.lj03));
                    break;
                case "经典":
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.lj04));
                    break;
                case "华丽":
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.lj05));
                    break;
                case "复古":
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.lj06));
                    break;
                case "优雅":
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.lj07));
                    break;
                case "电影":
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.lj08));
                    break;
                case "回忆":
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.lj09));
                    break;
                case "优格":
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.lj10));
                    break;
                case "流年":
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.lj11));
                    break;
                case "发光":
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.lj12));
                    break;
                case "马赛克":
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.lj13));
                    break;
            }
//            TextView text = new TextView(activity);
//            text.setTextColor(Color.WHITE);
//            text.setTextSize(20);
//            text.setText(fliters[i]);
            mFilterGroup.addView(imageView, params);
            imageView.setTag(i);
            iviews.add(imageView);
            imageView.setOnClickListener(new FliterClick());
        }// end for i
    }

    @Override
    public void onDestroy() {
        if (fliterBit != null && (!fliterBit.isRecycled())) {
            fliterBit.recycle();
        }
        super.onDestroy();
        backToMain();
    }

    /**
     * 选择滤镜效果
     */
    private final class FliterClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            for (ImageView imageView:iviews){
                int x = ((Integer) imageView.getTag()).intValue();
                switch (fliters[x]){
                    case "原始":
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.lj01));
                        break;
                    case "软化":
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.lj02));
                        break;
                    case "黑白":
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.lj03));
                        break;
                    case "经典":
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.lj04));
                        break;
                    case "华丽":
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.lj05));
                        break;
                    case "复古":
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.lj06));
                        break;
                    case "优雅":
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.lj07));
                        break;
                    case "电影":
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.lj08));
                        break;
                    case "回忆":
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.lj09));
                        break;
                    case "优格":
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.lj10));
                        break;
                    case "流年":
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.lj11));
                        break;
                    case "发光":
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.lj12));
                        break;
                    case "马赛克":
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.lj13));
                        break;
                }
            }
            int position = ((Integer) v.getTag()).intValue();
            ImageView targetView = (ImageView)v;
            switch (fliters[position]){
                case "原始":
                    targetView.setImageDrawable(getResources().getDrawable(R.drawable.lj01_1));
                    break;
                case "软化":
                    targetView.setImageDrawable(getResources().getDrawable(R.drawable.lj02_1));
                    break;
                case "黑白":
                    targetView.setImageDrawable(getResources().getDrawable(R.drawable.lj03_1));
                    break;
                case "经典":
                    targetView.setImageDrawable(getResources().getDrawable(R.drawable.lj04_1));
                    break;
                case "华丽":
                    targetView.setImageDrawable(getResources().getDrawable(R.drawable.lj05_1));
                    break;
                case "复古":
                    targetView.setImageDrawable(getResources().getDrawable(R.drawable.lj06_1));
                    break;
                case "优雅":
                    targetView.setImageDrawable(getResources().getDrawable(R.drawable.lj07_1));
                    break;
                case "电影":
                    targetView.setImageDrawable(getResources().getDrawable(R.drawable.lj08_1));
                    break;
                case "回忆":
                    targetView.setImageDrawable(getResources().getDrawable(R.drawable.lj09_1));
                    break;
                case "优格":
                    targetView.setImageDrawable(getResources().getDrawable(R.drawable.lj10_1));
                    break;
                case "流年":
                    targetView.setImageDrawable(getResources().getDrawable(R.drawable.lj11_1));
                    break;
                case "发光":
                    targetView.setImageDrawable(getResources().getDrawable(R.drawable.lj12_1));
                    break;
                case "马赛克":
                    targetView.setImageDrawable(getResources().getDrawable(R.drawable.lj13_1));
                    break;
            }
            if (position == 0) {// 原始图片效果
                activity.mainImage.setImageBitmap(activity.getMainBit());
                currentBitmap = activity.getMainBit();
                return;
            }
            // 滤镜处理
            ProcessingImage task = new ProcessingImage();
            task.execute(position);
        }
    }// end inner class
    /**
     * 图片滤镜处理任务
     *
     * @author panyi
     */
    private final class ProcessingImage extends AsyncTask<Integer, Void, Bitmap> {
        private Dialog dialog;
        private Bitmap srcBitmap;

        @Override
        protected Bitmap doInBackground(Integer... params) {
            int type = params[0];
            if (srcBitmap != null && !srcBitmap.isRecycled()) {
                srcBitmap.recycle();
            }

            srcBitmap = Bitmap.createBitmap(activity.getMainBit().copy(
                    Bitmap.Config.ARGB_8888, true));
            return PhotoProcessing.filterPhoto(srcBitmap, type);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            dialog.dismiss();
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        protected void onCancelled(Bitmap result) {
            super.onCancelled(result);
            dialog.dismiss();
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if (result == null)
                return;
            if (fliterBit != null && (!fliterBit.isRecycled())) {
                fliterBit.recycle();
            }
            fliterBit = result;
            activity.mainImage.setImageBitmap(fliterBit);
            currentBitmap = fliterBit;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = BaseActivity.getLoadingDialog(getActivity(), R.string.handing,
                    false);
            dialog.show();
        }

    }// end inner class

    public Bitmap getCurrentBitmap() {
        return currentBitmap;
    }

    public void setCurrentBitmap(Bitmap currentBitmap) {
        this.currentBitmap = currentBitmap;
    }

    public  int dip2px( float dpValue) {
        float scale = activity.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}// end class
