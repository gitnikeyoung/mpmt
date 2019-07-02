package com.huyu.psxt.Activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
;
import com.huyu.psxt.R;
import com.huyu.psxt.Tools.SystemTools;
import com.huyu.imageeditlibrary.picchooser.SelectPictureActivity;


import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.huyu.imageeditlibrary.editimage.EditImageActivity.start;


public class MainImageActivity extends BaseActivity {
    public static final int REQUEST_CODE_EDITER = 200;
    public static final int REQUEST_PERMISSON_SORAGE = 1;
    public static final int REQUEST_PERMISSON_CAMERA = 2;
    public static int modeid = 0;

    public static final int SELECT_GALLERY_IMAGE_CODE = 7;
    public static final int TAKE_PHOTO_CODE = 8;
    public static final int ACTION_REQUEST_EDITIMAGE = 9;
    private String path;
    private Uri photoURI = null;
    private Bitmap mainBitmap;
    private int imageWidth, imageHeight;//
    private static final String TAG = "MainImageActivity";
    private File photoFile = null;
    @BindView(R.id.bar_wrapper)
    LinearLayout bar_wrapper;
    private String[] permissions = {Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};//权限
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_image);
        ButterKnife.bind(this);
        viewCompatibility();
        PermissionsUtil.requestPermission(this, new PermissionListener() {
            @Override
            public void permissionGranted(@NonNull String[] permission) {
                Log.e(TAG, "permissionGranted: "+permission.toString() );
            }

            @Override
            public void permissionDenied(@NonNull String[] permission) {
                Log.e(TAG, "permissionDenied: "+permission.toString() );
            }
        }, permissions);
    }

    //兼容性代码
    private void viewCompatibility(){
        setDeacultBar(R.color.colorBlue3,true,bar_wrapper);
    }

    /**
     * 编辑选择的图片
     *
     * @author panyi
     */
    private void editImageClick() {
        File outputFile = com.huyu.psxt.FileUtils.genEditFile();
        start(this,path,outputFile.getAbsolutePath(),ACTION_REQUEST_EDITIMAGE,modeid);
    }

    @OnClick(R.id.editer_btn)
    public void editerImage(){
        modeid = 6;
        openAblum();
    }

    @OnClick(R.id.tietu_btn)
    public void tieTuImage(){
        modeid = 1;
        openAblum();
    }
    @OnClick({R.id.lujing_btn})
    public void luJingImage(){
        modeid = 2;
        openAblum();
    }
    @OnClick(R.id.caijian_btn)
    public void aijianImage(){
        modeid = 3;
        openAblum();
    }

    @OnClick(R.id.xuanzhuan_btn)
    public void xuanzhuanImage(){
        modeid = 4;
        openAblum();
    }

    @OnClick(R.id.wenzi_btn)
    public void wenziImage(){
        modeid = 5;
        openAblum();
    }
    @OnClick(R.id.meiyan_btn)
    public void meiyanImage(){
        modeid = 7;
        openAblum();
    }
    @OnClick(R.id.btn_pz)
    public void bgeinPz(){
        doTakePhoto();
    }

    private void openAblum() {
        Intent intent = new Intent(MainImageActivity.this, SelectPictureActivity.class);
        intent.putExtra("mode",modeid);
        MainImageActivity.this.startActivityForResult(intent,
                SELECT_GALLERY_IMAGE_CODE);
    }

    /**
     * 拍摄照片
     */
    private void doTakePhoto() {
        try{
            //获取相机包名
            Intent infoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            List<ResolveInfo> infos = getPackageManager().queryIntentActivities(infoIntent, 0);
            if(infos != null && infos.size() > 0) {
                for(ResolveInfo info:infos) {
                    int flags = info.activityInfo.applicationInfo.flags;
                    if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) { //系统相机
                        String packageName=info.activityInfo.packageName;
                        String className=info.activityInfo.name;
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        ComponentName cn=new ComponentName(packageName, className);
                        intent.setComponent(cn);
                        intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            photoFile = com.huyu.psxt.FileUtils.genEditFile();
                            if (photoFile != null) {
                                photoURI = FileProvider.getUriForFile(
                                        this,
                                        this.getPackageName() + ".fileprovider",
                                        photoFile);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                startActivityForResult(intent, TAKE_PHOTO_CODE);
                            }
                        }
                        return;
                    }
                }
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

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
                case TAKE_PHOTO_CODE://拍照返回
                    handleTakePhoto(data);
                    break;
                case ACTION_REQUEST_EDITIMAGE://
                   // handleEditorImage(data);
                    break;
//                case REQUEST_CODE_EDITER:
//                    List<MediaEntity> result = Phoenix.result(data);
//                    if (result.size()>0){
//                        MediaEntity m1 = result.get(0);
//                        String p1 = m1.getLocalPath();
//                        handleSelectFromAblum(p1);
//                    }
//                    //editImageClick();
//                    break;
            }// end switch
        }
    }
    @Override
    public void onBackPressed() {
        SystemTools.showNormalDialog(this);
    }

    /**
     * 处理拍照返回
     *
     * @param data
     */
    private void handleTakePhoto(Intent data) {
        if (photoURI != null) {//拍摄成功
            //path = FileUtils.getRealFilePath(this,photoURI);
            path = photoFile.getAbsolutePath();
            modeid = 6;
            editImageClick();
        }
    }


    /*选择图片返回*/
    private void handleSelectFromAblum(Intent data) {
        String filepath = data.getStringExtra("imgPath");
        path = filepath;
        editImageClick();
        // System.out.println("path---->"+path);
        //startLoadTask();
    }

    private void startLoadTask() {
        //MainImageActivity.LoadImageTask task = new MainImageActivity.LoadImageTask();
        //task.execute(path);
    }


}
