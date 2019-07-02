package com.huyu.imageeditlibrary.editimage.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.huyu.imageeditlibrary.R;
import com.huyu.imageeditlibrary.editimage.EditImageActivity;
import com.huyu.imageeditlibrary.editimage.ModuleConfig;


/**
 * 工具栏主菜单
 *
 * @author panyi
 */
public class MainMenuFragment extends BaseEditFragment implements View.OnClickListener {
    public static final int INDEX = ModuleConfig.INDEX_MAIN;

    public static final String TAG = MainMenuFragment.class.getName();
    private View mainView;

    private ImageView stickerBtn;// 贴图按钮
    private ImageView fliterBtn;// 滤镜按钮
    private ImageView cropBtn;// 剪裁按钮
    private ImageView rotateBtn;// 旋转按钮
    private ImageView mTextBtn;//文字型贴图添加
    private ImageView mPaintBtn;//编辑按钮
    private ImageView mBeautyBtn;//美颜按钮

    public static MainMenuFragment newInstance() {
        MainMenuFragment fragment = new MainMenuFragment();
        return fragment;
    }

    public void setMenuUi(){
        mPaintBtn.setImageDrawable(activity.getResources().getDrawable(R.drawable.edit_ico1_1));
        stickerBtn.setImageDrawable(activity.getResources().getDrawable(R.drawable.edit_ico2_1));
        fliterBtn.setImageDrawable(activity.getResources().getDrawable(R.drawable.edit_ico3_1));
        cropBtn.setImageDrawable(activity.getResources().getDrawable(R.drawable.edit_ico4_1));
        rotateBtn.setImageDrawable(activity.getResources().getDrawable(R.drawable.edit_ico5_1));
        mTextBtn.setImageDrawable(activity.getResources().getDrawable(R.drawable.edit_ico6_1));
        mBeautyBtn.setImageDrawable(activity.getResources().getDrawable(R.drawable.edit_ico7_1));
        switch (activity.mode){
            case 6:
                mPaintBtn.setImageDrawable(activity.getResources().getDrawable(R.drawable.edit_ico1_2));
                break;
            case 1:
                stickerBtn.setImageDrawable(activity.getResources().getDrawable(R.drawable.edit_ico2_2));
                break;
            case 2:
                fliterBtn.setImageDrawable(activity.getResources().getDrawable(R.drawable.edit_ico3_2));
                break;
            case 3:
                cropBtn.setImageDrawable(activity.getResources().getDrawable(R.drawable.edit_ico4_2));
                break;
            case 4:
                rotateBtn.setImageDrawable(activity.getResources().getDrawable(R.drawable.edit_ico5_2));
                break;
            case 5:
                mTextBtn.setImageDrawable(activity.getResources().getDrawable(R.drawable.edit_ico6_2));
                break;
            case 7:
                mBeautyBtn.setImageDrawable(activity.getResources().getDrawable(R.drawable.edit_ico7_2));
                break;
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_edit_image_main_menu,
                null);
        return mainView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        stickerBtn = mainView.findViewById(R.id.btn_stickers);
        fliterBtn = mainView.findViewById(R.id.btn_filter);
        cropBtn = mainView.findViewById(R.id.btn_crop);
        rotateBtn = mainView.findViewById(R.id.btn_rotate);
        mTextBtn = mainView.findViewById(R.id.btn_text);
        mPaintBtn = mainView.findViewById(R.id.btn_paint);
        mBeautyBtn = mainView.findViewById(R.id.btn_beauty);

        stickerBtn.setOnClickListener(this);
        fliterBtn.setOnClickListener(this);
        cropBtn.setOnClickListener(this);
        rotateBtn.setOnClickListener(this);
        mTextBtn.setOnClickListener(this);
        mPaintBtn.setOnClickListener(this);
        mBeautyBtn.setOnClickListener(this);
    }

    @Override
    public void onShow() {
        // do nothing
    }

    @Override
    public void backToMain() {
        //do nothing
    }

    @Override
    public void onClick(View v) {
        if (v == stickerBtn) {
            onStickClick();
        } else if (v == fliterBtn) {
            onFilterClick();
        } else if (v == cropBtn) {
            onCropClick();
        } else if (v == rotateBtn) {
            onRotateClick();
        } else if (v == mTextBtn) {
            onAddTextClick();
        } else if (v == mPaintBtn) {
            onPaintClick();
        }else if(v == mBeautyBtn){
            onBeautyClick();
        }
    }

    /**
     * 贴图模式
     *
     * @author panyi
     */
    private void onStickClick() {
//        activity.bottomGallery.setCurrentItem(StickerFragment.INDEX);
//        activity.mStickerFragment.onShow();
        activity.mode = EditImageActivity.MODE_STICKERS;
        activity.replaceMode();
    }

    /**
     * 滤镜模式
     *
     * @author panyi
     */
    private void onFilterClick() {
//        activity.bottomGallery.setCurrentItem(FilterListFragment.INDEX);
//        activity.mFilterListFragment.onShow();
        activity.mode = EditImageActivity.MODE_FILTER;
        activity.replaceMode();
    }

    /**
     * 裁剪模式
     *
     * @author panyi
     */
    private void onCropClick() {
//        activity.bottomGallery.setCurrentItem(CropFragment.INDEX);
//        activity.mCropFragment.onShow();
        activity.mode = EditImageActivity.MODE_CROP;
        activity.replaceMode();
    }

    /**
     * 图片旋转模式
     *
     * @author panyi
     */
    private void onRotateClick() {
//        activity.bottomGallery.setCurrentItem(RotateFragment.INDEX);
//        activity.mRotateFragment.onShow();
        activity.mode = EditImageActivity.MODE_ROTATE;
        activity.replaceMode();
    }

    /**
     * 插入文字模式
     *
     * @author panyi
     */
    private void onAddTextClick() {
//        activity.bottomGallery.setCurrentItem(AddTextFragment.INDEX);
//        activity.mAddTextFragment.onShow();
        activity.mode = EditImageActivity.MODE_TEXT;
        activity.replaceMode();
    }

    /**
     * 自由绘制模式
     */
    private void onPaintClick() {
//        activity.bottomGallery.setCurrentItem(PaintFragment.INDEX);
//        activity.mPaintFragment.onShow();
        activity.mode = EditImageActivity.MODE_PAINT;
        activity.replaceMode();

    }

    private void onBeautyClick(){
//        activity.bottomGallery.setCurrentItem(BeautyFragment.INDEX);
//        activity.mBeautyFragment.onShow();
        activity.mode = EditImageActivity.MODE_BEAUTY;
        activity.replaceMode();
    }

}// end class
