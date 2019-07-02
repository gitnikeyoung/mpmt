package com.huyu.imageeditlibrary.editimage;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.gyf.barlibrary.ImmersionBar;
import com.huyu.imageeditlibrary.BaseActivity;
import com.huyu.imageeditlibrary.R;
import com.huyu.imageeditlibrary.editimage.fragment.AddTextFragment;
import com.huyu.imageeditlibrary.editimage.fragment.BeautyFragment;
import com.huyu.imageeditlibrary.editimage.fragment.CropFragment;
import com.huyu.imageeditlibrary.editimage.fragment.FilterListFragment;
import com.huyu.imageeditlibrary.editimage.fragment.MainMenuFragment;
import com.huyu.imageeditlibrary.editimage.fragment.PaintFragment;
import com.huyu.imageeditlibrary.editimage.fragment.RotateFragment;
import com.huyu.imageeditlibrary.editimage.fragment.StickerFragment;
import com.huyu.imageeditlibrary.editimage.model.RatioItem;
import com.huyu.imageeditlibrary.editimage.utils.FileUtil;
import com.huyu.imageeditlibrary.editimage.view.CropImageView;
import com.huyu.imageeditlibrary.editimage.view.CustomPaintView;
import com.huyu.imageeditlibrary.editimage.view.CustomViewPager;
import com.huyu.imageeditlibrary.editimage.view.RotateImageView;
import com.huyu.imageeditlibrary.editimage.view.StickerView;
import com.huyu.imageeditlibrary.editimage.view.TextStickerView;
import com.huyu.imageeditlibrary.editimage.view.imagezoom.ImageViewTouch;
import com.huyu.imageeditlibrary.editimage.utils.BitmapUtils;
import com.huyu.imageeditlibrary.editimage.view.imagezoom.ImageViewTouchBase;
import com.huyu.imageeditlibrary.editimage.widget.RedoUndoController;

import java.util.List;

/**
 * <p>
 * 图片编辑 主页面
 *
 * @author panyi
 *         <p>
 *         包含 1.贴图 2.滤镜 3.剪裁 4.底图旋转 功能
 *         add new modules
 */
public class EditImageActivity extends BaseActivity {
    public static final String FILE_PATH = "file_path";
    public static final String EXTRA_OUTPUT = "extra_output";
    public static final String SAVE_FILE_PATH = "save_file_path";

    public static final String IMAGE_IS_EDIT = "image_is_edit";
    public static final int SELECT_GALLERY_IMAGE_CODE = 7;

    public static final int MODE_NONE = 0;
    public static final int MODE_STICKERS = 1;// 贴图模式
    public static final int MODE_FILTER = 2;// 滤镜模式
    public static final int MODE_CROP = 3;// 剪裁模式
    public static final int MODE_ROTATE = 4;// 旋转模式
    public static final int MODE_TEXT = 5;// 文字模式
    public static final int MODE_PAINT = 6;//绘制模式
    public static final int MODE_BEAUTY = 7;//美颜模式

    public String filePath;// 需要编辑图片路径
    public String saveFilePath;// 生成的新图片路径
    private int imageWidth, imageHeight;// 展示图片控件 宽 高
    private LoadImageTask mLoadImageTask;

    public int mode = MODE_NONE;// 当前操作模式

    protected int mOpTimes = 0;
    protected boolean isBeenSaved = false;

    private FrameLayout menuwrapper;
    private EditImageActivity mContext;
    private Bitmap mainBitmap;// 底层显示Bitmap
    public ImageViewTouch mainImage;
    private View backBtn;

    private ConstraintLayout last_wrapper;
    private ImageViewTouch show_image_wrapper;
    private Boolean loadlast =false;
    private ImageView gohomex;
    private ImageView gobackx;
    private TextView title_cont;

    public ViewFlipper bannerFlipper;
    private View applyBtn;// 应用按钮
    private View saveBtn;// 保存按钮

    public StickerView mStickerView;// 贴图层View
    public CropImageView mCropPanel;// 剪切操作控件
    public RotateImageView mRotatePanel;// 旋转操作控件
    public TextStickerView mTextStickerView;//文本贴图显示View
    public CustomPaintView mPaintView;//涂鸦模式画板

    public CustomViewPager bottomGallery;// 底部gallery
    public FrameLayout bottomGalleryCont;// 底部gallery
    private BottomGalleryAdapter mBottomGalleryAdapter;// 底部gallery
    private MainMenuFragment mMainMenuFragment;// Menu
    public StickerFragment mStickerFragment;// 贴图Fragment
    public FilterListFragment mFilterListFragment;// 滤镜FliterListFragment
    public CropFragment mCropFragment;// 图片剪裁Fragment
    public RotateFragment mRotateFragment;// 图片旋转Fragment
    public AddTextFragment mAddTextFragment;//图片添加文字
    public PaintFragment mPaintFragment;//绘制模式Fragment
    public BeautyFragment mBeautyFragment;//美颜模式Fragment
    private SaveImageTask mSaveImageTask;
    private LinearLayout cj_wrapper;//裁剪选项

    private RedoUndoController mRedoUndoController;//撤销操作
    private ImageView openfilebtn;

    /**
     * @param context
     * @param editImagePath
     * @param outputPath
     * @param requestCode
     */
    public static void start(Activity context, final String editImagePath, final String outputPath, final int requestCode,final int modeint) {
        if (TextUtils.isEmpty(editImagePath)) {
            Toast.makeText(context, R.string.no_choose, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent it = new Intent(context, EditImageActivity.class);
        it.putExtra(EditImageActivity.FILE_PATH, editImagePath);
        it.putExtra(EditImageActivity.EXTRA_OUTPUT, outputPath);
        it.putExtra("mode",modeint);
        context.startActivityForResult(it, requestCode);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkInitImageLoader();
        setContentView(R.layout.activity_image_edit);
        LinearLayout tit_bar = findViewById(R.id.bar_wrapper);
        ImmersionBar.with(this)
                .statusBarColor(R.color.materialcolorpicker__grey1)//状态栏颜色
                .statusBarDarkFont(false)//图标颜色，false白色，true黑色
                .titleBar(tit_bar)//状态栏占位组件
                .init();
        Intent intent = getIntent();
        int intent_mode = intent.getIntExtra("mode",1);
        mode = intent_mode;
        initView();
        getData();
    }

    private void getData() {
        filePath = getIntent().getStringExtra(FILE_PATH);
        saveFilePath = getIntent().getStringExtra(EXTRA_OUTPUT);// 保存图片路径
        loadImage(filePath);
    }

    private void reSetBaseImage(String path){
        filePath = path;
        loadImage(filePath);
        bannerFlipper.showNext();
    }
    private void initView() {
        mContext = this;
        menuwrapper = findViewById(R.id.menu_wrapper);
        openfilebtn = findViewById(R.id.open_file_btn);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        imageWidth = metrics.widthPixels / 2;
        imageHeight = metrics.heightPixels / 2;
        cj_wrapper = findViewById(R.id.cj_wrapper);

        bannerFlipper = (ViewFlipper) findViewById(R.id.banner_flipper);
        bannerFlipper.setInAnimation(this, R.anim.in_bottom_to_top);
        bannerFlipper.setOutAnimation(this, R.anim.out_bottom_to_top);
        applyBtn = findViewById(R.id.apply);
        applyBtn.setOnClickListener(new ApplyBtnClick());
        saveBtn = findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(new SaveBtnClick());

        mainImage = (ImageViewTouch) findViewById(R.id.main_image);
        backBtn = findViewById(R.id.open_file_btn);// 退出按钮
        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mStickerView = (StickerView) findViewById(R.id.sticker_panel);
        mCropPanel = (CropImageView) findViewById(R.id.crop_panel);
        mRotatePanel = (RotateImageView) findViewById(R.id.rotate_panel);
        mTextStickerView = (TextStickerView) findViewById(R.id.text_sticker_panel);
        mPaintView = (CustomPaintView) findViewById(R.id.custom_paint_view);
        title_cont = findViewById(R.id.title_cont);
        // 底部gallery
        //bottomGallery = (CustomViewPager) findViewById(R.id.bottom_gallery);
        bottomGalleryCont = (FrameLayout) findViewById(R.id.bottom_gallery_cont);
        //bottomGallery.setOffscreenPageLimit(7);
        mMainMenuFragment = MainMenuFragment.newInstance();
        replaceFragment(R.id.menu_wrapper,mMainMenuFragment);
//        mBottomGalleryAdapter = new BottomGalleryAdapter(
//                this.getSupportFragmentManager());
        mStickerFragment = StickerFragment.newInstance();
        mFilterListFragment = FilterListFragment.newInstance();
        mCropFragment = CropFragment.newInstance();
        mRotateFragment = RotateFragment.newInstance();
        mAddTextFragment = AddTextFragment.newInstance();
        mPaintFragment = PaintFragment.newInstance();
        mBeautyFragment = BeautyFragment.newInstance();
        //bottomGallery.setAdapter(mBottomGalleryAdapter);
        //bottomGalleryCont.setAdapter(mBottomGalleryAdapter);
        //bottomGalleryCont.setCurrentItem(StickerFragment.INDEX);
        mainImage.setFlingListener(new ImageViewTouch.OnImageFlingListener() {
            @Override
            public void onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //System.out.println(e1.getAction() + " " + e2.getAction() + " " + velocityX + "  " + velocityY);
                if (velocityY > 1) {
                    closeInputMethod();
                }
            }
        });
        mRedoUndoController = new RedoUndoController(this, findViewById(R.id.redo_uodo_panel));

        last_wrapper = findViewById(R.id.change_end_wrapper);
        last_wrapper.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        show_image_wrapper = findViewById(R.id.show_image);
        gohomex = findViewById(R.id.gohome_btn);
        gohomex.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        openfilebtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void changeCjBl(View v,int i){
        List<RatioItem> d = CropFragment.getDataList();
        if (i!=10){
            v.setTag(d.get(i+1));
        }else{
            v.setTag(d.get(0));
        }
    }
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 关闭输入法
     */
    private void closeInputMethod() {
        if (mAddTextFragment.isAdded()) {
            mAddTextFragment.hideInput();
        }
    }

    /**
     * @author panyi
     */
    private final class BottomGalleryAdapter extends FragmentPagerAdapter {
        public BottomGalleryAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {
             System.out.println("createFragment-->"+index);
            switch (index+1) {
//                case MainMenuFragment.INDEX:// 主菜单
//                    return mMainMenuFragment;
                case StickerFragment.INDEX:// 贴图
                    return mStickerFragment;
                case FilterListFragment.INDEX:// 滤镜
                    return mFilterListFragment;
                case CropFragment.INDEX://剪裁
                    return mCropFragment;
                case RotateFragment.INDEX://旋转
                    return mRotateFragment;
                case AddTextFragment.INDEX://添加文字
                    return mAddTextFragment;
                case PaintFragment.INDEX:
                    return mPaintFragment;//绘制
                case BeautyFragment.INDEX://美颜
                    return mBeautyFragment;
            }//end switch
            return StickerFragment.newInstance();
        }

        @Override
        public int getCount() {
            return 7;
        }
    }// end inner class

    /**
     * 异步载入编辑图片
     *
     * @param filepath
     */
    public void loadImage(String filepath) {
        if (mLoadImageTask != null) {
            mLoadImageTask.cancel(true);
        }
        mLoadImageTask = new LoadImageTask();
        mLoadImageTask.execute(filepath);
    }

    /**
     * 导入文件图片任务
     */
    private final class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            return BitmapUtils.getSampledBitmap(params[0], imageWidth,
                    imageHeight);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (loadlast){
                changeMainBitmap2(result,false);
                loadlast = false;
            }else{
                changeMainBitmap(result, false);
                replaceMode();
            }
        }
    }// end inner class

    @Override
    public void onBackPressed() {
//        switch (mode) {
//            case MODE_STICKERS:
//                mStickerFragment.backToMain();
//                return;
//            case MODE_FILTER:// 滤镜编辑状态
//                mFilterListFragment.backToMain();// 保存滤镜贴图
//                return;
//            case MODE_CROP:// 剪切图片保存
//                mCropFragment.backToMain();
//                return;
//            case MODE_ROTATE:// 旋转图片保存
//                mRotateFragment.backToMain();
//                return;
//            case MODE_TEXT:
//                mAddTextFragment.backToMain();
//                return;
//            case MODE_PAINT:
//                mPaintFragment.backToMain();
//                return;
//            case MODE_BEAUTY://从美颜模式中返回
//                mBeautyFragment.backToMain();
//                return;
//        }// end switch
        if (getShowCjState()){
            hideCjWrapper();
            return;
        }
        if (canAutoExit()) {
            //onSaveTaskDone();
            finish();
        } else {//图片还未被保存    弹出提示框确认
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(R.string.exit_without_save)
                    .setCancelable(false).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mContext.finish();
                }
            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    /**
     * 应用按钮点击
     *
     * @author panyi
     */
    private final class ApplyBtnClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            switch (mode) {
                case MODE_STICKERS:
                    mStickerFragment.applyStickers();// 保存贴图
                    break;
                case MODE_FILTER:// 滤镜编辑状态
                    mFilterListFragment.applyFilterImage();// 保存滤镜贴图
                    break;
                case MODE_CROP:// 剪切图片保存
                    mCropFragment.applyCropImage();
                    break;
                case MODE_ROTATE:// 旋转图片保存
                    mRotateFragment.applyRotateImage();
                    break;
                case MODE_TEXT://文字贴图 图片保存
                    mAddTextFragment.applyTextImage();
                    break;
                case MODE_PAINT://保存涂鸦
                    mPaintFragment.savePaintImage();
                    break;
                case MODE_BEAUTY://保存美颜后的图片
                    mBeautyFragment.applyBeauty();
                    break;
                default:
                    break;
            }// end switch
            mode = MODE_NONE;
            replaceMode();
        }
    }// end inner class

    /**
     * 保存按钮 点击退出
     *
     * @author panyi
     */
    private final class SaveBtnClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (mOpTimes == 0) {//并未修改图片
                onSaveTaskDone();
            } else {
                loadlast = true;
                doSaveImage();
            }
        }
    }// end inner class

    protected void doSaveImage() {
        if (mOpTimes <= 0)
            return;

        if (mSaveImageTask != null) {
            mSaveImageTask.cancel(true);
        }

        mSaveImageTask = new SaveImageTask();
        mSaveImageTask.execute(mainBitmap);
    }

    /**
     * @param newBit
     * @param needPushUndoStack
     */
    public void changeMainBitmap(Bitmap newBit, boolean needPushUndoStack) {
        if (newBit == null)
            return;

        if (mainBitmap == null || mainBitmap != newBit) {
            if (needPushUndoStack) {
                mRedoUndoController.switchMainBit(mainBitmap,newBit);
                increaseOpTimes();
            }
            mainBitmap = newBit;
            mainImage.setImageBitmap(mainBitmap);
            mainImage.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        }
    }

    /**
     * @param newBit
     * @param needPushUndoStack
     */
    public void changeMainBitmap2(Bitmap newBit, boolean needPushUndoStack) {
        if (newBit == null)
            return;

        if (mainBitmap == null || mainBitmap != newBit) {
            if (needPushUndoStack) {
                mRedoUndoController.switchMainBit(mainBitmap,newBit);
                increaseOpTimes();
            }
            mainBitmap = newBit;
            show_image_wrapper.setImageBitmap(mainBitmap);
            show_image_wrapper.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoadImageTask != null) {
            mLoadImageTask.cancel(true);
        }

        if (mSaveImageTask != null) {
            mSaveImageTask.cancel(true);
        }

        if (mRedoUndoController != null) {
            mRedoUndoController.onDestroy();
        }
    }

    public void increaseOpTimes() {
        mOpTimes++;
        isBeenSaved = false;
    }

    public void resetOpTimes() {
        isBeenSaved = true;
    }

    public boolean canAutoExit() {
        return isBeenSaved || mOpTimes == 0;
    }

    protected void onSaveTaskDone() {

        last_wrapper.setVisibility(View.VISIBLE);
        loadImage(saveFilePath);
//        Intent returnIntent = new Intent();
//        returnIntent.putExtra(FILE_PATH, filePath);
//        returnIntent.putExtra(EXTRA_OUTPUT, saveFilePath);
//        returnIntent.putExtra(IMAGE_IS_EDIT, mOpTimes > 0);

        FileUtil.ablumUpdate(this, saveFilePath);
        //setResult(RESULT_OK, returnIntent);
        //finish();
    }

    /**
     * 保存图像
     * 完成后退出
     */
    private final class SaveImageTask extends AsyncTask<Bitmap, Void, Boolean> {
        private Dialog dialog;

        @Override
        protected Boolean doInBackground(Bitmap... params) {
            if (TextUtils.isEmpty(saveFilePath))
                return false;

            return BitmapUtils.saveBitmap(params[0], saveFilePath);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            dialog.dismiss();
        }

        @Override
        protected void onCancelled(Boolean result) {
            super.onCancelled(result);
            dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = EditImageActivity.getLoadingDialog(mContext, R.string.saving_image, false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            dialog.dismiss();

            if (result) {
                resetOpTimes();
                onSaveTaskDone();
            } else {
                Toast.makeText(mContext, R.string.save_error, Toast.LENGTH_SHORT).show();
            }
        }
    }//end inner class

    public Bitmap getMainBit() {
        return mainBitmap;
    }

    private void replaceFragment(int fragmentint,Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(fragmentint,fragment);
        transaction.commit();
    }

    public void replaceMode(){
        mMainMenuFragment.setMenuUi();
        if (getShowCjState()){
            hideCjWrapper();
        }
        if (bottomGalleryCont.getVisibility()==View.GONE){
            bottomGalleryCont.setVisibility(View.VISIBLE);
        }
        switch (mode){
            case MODE_PAINT:
                replaceFragment(R.id.bottom_gallery_cont,mPaintFragment);
                title_cont.setText("绘制");
                break;
            case MODE_STICKERS:
                replaceFragment(R.id.bottom_gallery_cont,mStickerFragment);
                title_cont.setText("贴图");
                break;
            case MODE_FILTER:
                replaceFragment(R.id.bottom_gallery_cont,mFilterListFragment);
                title_cont.setText("滤镜");
                break;
            case MODE_CROP:
                replaceFragment(R.id.bottom_gallery_cont,mCropFragment);
                title_cont.setText("剪裁");
                break;
            case MODE_ROTATE:
                replaceFragment(R.id.bottom_gallery_cont,mRotateFragment);
                title_cont.setText("旋转");
                break;
            case MODE_TEXT:
                replaceFragment(R.id.bottom_gallery_cont,mAddTextFragment);
                title_cont.setText("文字");
                break;
            case MODE_BEAUTY:
                replaceFragment(R.id.bottom_gallery_cont,mBeautyFragment);
                title_cont.setText("美颜");
                break;
            case MODE_NONE:
                setYyPage();
                title_cont.setText("");
                break;

        }
    }
    public void setYyPage(){
        bannerFlipper.showPrevious();
        bottomGalleryCont.setVisibility(View.GONE);
    }
    public void showCjWrapper(){
        cj_wrapper.setVisibility(View.VISIBLE);
    }
    public void hideCjWrapper(){
        cj_wrapper.setVisibility(View.GONE);
    }
    public Boolean getShowCjState(){
        return  (cj_wrapper.getVisibility()==View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // System.out.println("RESULT_OK");
            switch (requestCode) {
                case SELECT_GALLERY_IMAGE_CODE://
                    handleSelectFromAblum(data);
                    break;
            }// end switch
        }
    }

    private void handleSelectFromAblum(Intent data){
        String t_path = data.getStringExtra("imgPath");
        reSetBaseImage(t_path);
    }
}// end class
