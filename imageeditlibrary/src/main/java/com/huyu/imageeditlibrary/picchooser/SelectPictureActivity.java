
package com.huyu.imageeditlibrary.picchooser;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.LinearLayout;

import com.gyf.barlibrary.ImmersionBar;
import com.huyu.imageeditlibrary.BaseActivity;
import com.huyu.imageeditlibrary.R;
import com.huyu.imageeditlibrary.editimage.utils.FileUtil;

import java.io.File;
import java.util.List;

import static com.huyu.imageeditlibrary.editimage.EditImageActivity.start;

public class SelectPictureActivity extends BaseActivity {
    private Boolean isS = false;
    private Uri photoURI = null;
    public static final int TAKE_PHOTO_CODE = 8;
    private String path;
    private File photoFile;
    private int modeid = 6;
    @Override
    protected void onCreate(final Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_image_select);
        checkInitImageLoader();
        setResult(RESULT_CANCELED);
        LinearLayout tit_bar = findViewById(R.id.bar_wrapper);
        ImmersionBar.with(this)
                .statusBarColor(R.color.black)//状态栏颜色
                .statusBarDarkFont(false)//图标颜色，false白色，true黑色
                .titleBar(tit_bar)//状态栏占位组件
                .init();
        // Create new fragment and transaction
        Fragment newFragment = new BucketsFragment();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        // Replace whatever is in the fragment_container view with this
        // fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.fragment_wrapper, newFragment);

        // Commit the transaction
        transaction.commit();

        LinearLayout gobackbtn = findViewById(R.id.goback);
        gobackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goback();
            }
        });

        LinearLayout gotoPhone = findViewById(R.id.gotophone);
        gotoPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPhone();
            }
        });
        Intent intent = getIntent();
        modeid = intent.getIntExtra("mode",6);
    }

    void showBucket(final int bucketId) {
        Bundle b = new Bundle();
        b.putInt("bucket", bucketId);
        Fragment f = new ImagesFragment();
        f.setArguments(b);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_wrapper, f).addToBackStack(null).commit();
        isS = true;
    }

    void imageSelected(final String imgPath, final String imgTaken, final long imageSize) {
        returnResult(imgPath, imgTaken, imageSize);
    }

    private void returnResult(final String imgPath, final String imageTaken, final long imageSize) {
          path = imgPath;
          editImageClick();
//        Intent result = new Intent();
//        result.putExtra("imgPath", imgPath);
//        result.putExtra("dateTaken", imageTaken);
//        result.putExtra("imageSize", imageSize);
//        setResult(RESULT_OK, result);
//        finish();
    }

    public void goback(){
        if (isS){
            getSupportFragmentManager().popBackStack();
            isS = false;
        }else{
            finish();
        }

    }

    public void gotoPhone(){
        doTakePhoto();
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
                            photoFile = FileUtil.genEditFile();
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
                case TAKE_PHOTO_CODE://拍照返回
                    handleTakePhoto(data);
                    break;
            }
        }
    }
    /**
     * 处理拍照返回
     *
     * @param data
     */
    private void handleTakePhoto(Intent data) {
        if (photoURI != null) {//拍摄成功
            path = photoFile.getAbsolutePath();
            //startLoadTask();
            editImageClick();
        }
    }

    /**
     * 编辑选择的图片
     *
     * @author panyi
     */
    private void editImageClick() {
        File outputFile = FileUtil.genEditFile();
        start(this,path,outputFile.getAbsolutePath(),9,modeid);
    }
}
