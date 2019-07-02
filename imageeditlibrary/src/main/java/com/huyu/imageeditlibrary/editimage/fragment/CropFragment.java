package com.huyu.imageeditlibrary.editimage.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
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
import android.widget.TextView;

import com.huyu.imageeditlibrary.BaseActivity;
import com.huyu.imageeditlibrary.R;
import com.huyu.imageeditlibrary.editimage.ModuleConfig;
import com.huyu.imageeditlibrary.editimage.model.RatioItem;
import com.huyu.imageeditlibrary.editimage.utils.Matrix3;
import com.huyu.imageeditlibrary.editimage.view.CropImageView;
import com.huyu.imageeditlibrary.editimage.view.imagezoom.ImageViewTouchBase;


/**
 * 图片剪裁Fragment
 * 
 * @author panyi
 * 
 */
public class CropFragment extends BaseEditFragment {
    public static final int INDEX = ModuleConfig.INDEX_CROP;
	public static final String TAG = CropFragment.class.getName();
	private ImageView showblbtn;//显示比例选择按钮
	private View mainView;
	private View backToMenu;// 返回主菜单
	public CropImageView mCropPanel;// 剪裁操作面板
	private LinearLayout ratioList;
	private static List<RatioItem> dataList = new ArrayList<RatioItem>();

	public static List<RatioItem> getDataList() {
		return dataList;
	}

	static {
		// init data
		dataList.add(new RatioItem("none", -1f));
		dataList.add(new RatioItem("1:1", 1f));
		dataList.add(new RatioItem("1:2", 1 / 2f));
		dataList.add(new RatioItem("1:3", 1 / 3f));
		dataList.add(new RatioItem("2:3", 2 / 3f));
		dataList.add(new RatioItem("3:4", 3 / 4f));
		dataList.add(new RatioItem("2:1", 2f));
		dataList.add(new RatioItem("3:1", 3f));
		dataList.add(new RatioItem("3:2", 3 / 2f));
		dataList.add(new RatioItem("4:3", 4 / 3f));

	}
	private List<TextView> textViewList = new ArrayList<TextView>();

	public static int SELECTED_COLOR = Color.WHITE;
	public static int UNSELECTED_COLOR = Color.WHITE;
	private CropRationClick mCropRationClick = new CropRationClick();

	public CropRationClick getmCropRationClick() {
		return mCropRationClick;
	}

	public TextView selctedTextView;

	public static CropFragment newInstance() {
		CropFragment fragment = new CropFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_edit_image_crop, null);
		showblbtn = mainView.findViewById(R.id.showbl_btn);
		showblbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					if (activity.getShowCjState()){
						activity.hideCjWrapper();
					}else{
						activity.showCjWrapper();
					}

			}
		});
		return mainView;
	}

	private void setUpRatioList() {
		// init UI
		ratioList.removeAllViews();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				dip2px(50),
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER_VERTICAL;
		params.leftMargin = 10;
		params.rightMargin = 10;
		for (int i = 0, len = dataList.size(); i < len; i++) {
			TextView text = new TextView(activity);
			text.setTextColor(UNSELECTED_COLOR);
			text.setTextSize(20);
			text.setGravity(Gravity.CENTER);
			textViewList.add(text);
			ImageView imageView = new ImageView(activity);
			imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			switch (dataList.get(i).getText()){
				case "none":
					text.setText("无");
					break;
				case "1:1":
					imageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.cj_ico2));
					break;
				case "1:2":
					imageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.cj_ico3));
					break;
				case "1:3":
					imageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.cj_ico4));
					break;
				case "2:3":
					imageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.cj_ico5));
					break;
				case "3:4":
					imageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.cj_ico6));
					break;
				case "2:1":
					imageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.cj_ico7));
					break;
				case "3:1":
					imageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.cj_ico8));
					break;
				case "3:2":
					imageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.cj_ico9));
					break;
				case "4:3":
					imageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.cj_ico10));
					break;
			}
			if (dataList.get(i).getText().equals("none")){
				ratioList.addView(text, params);
				text.setTag(i);
				text.setTag(dataList.get(i));
				text.setOnClickListener(mCropRationClick);
			}else{
				ratioList.addView(imageView, params);
				imageView.setTag(i);
				imageView.setTag(dataList.get(i));
				imageView.setOnClickListener(mCropRationClick);
			}


			if (i == 0) {
				selctedTextView = text;
			}
			dataList.get(i).setIndex(i);

		}// end for i
		selctedTextView.setTextColor(SELECTED_COLOR);
	}

	/**
	 * 选择剪裁比率
	 * 
	 * @author
	 * 
	 */
	public final class CropRationClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			//TextView curTextView = (TextView) v;
			//selctedTextView.setTextColor(UNSELECTED_COLOR);
			RatioItem dataItem = (RatioItem) v.getTag();
			//selctedTextView = curTextView;
			//selctedTextView.setTextColor(SELECTED_COLOR);

			mCropPanel.setRatioCropRect(activity.mainImage.getBitmapRect(),
					dataItem.getRatio());
			// System.out.println("dataItem   " + dataItem.getText());
		}
	}// end inner class

	@Override
	public void onResume() {
		super.onResume();
		onShow();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

        backToMenu = mainView.findViewById(R.id.back_to_main);
        ratioList = (LinearLayout) mainView.findViewById(R.id.ratio_list_group);
        setUpRatioList();
        this.mCropPanel = ensureEditActivity().mCropPanel;
		backToMenu.setOnClickListener(new BackToMenuClick());// 返回主菜单
	}

    @Override
    public void onShow() {
        //activity.mode = EditImageActivity.MODE_CROP;

        activity.mCropPanel.setVisibility(View.VISIBLE);
        activity.mainImage.setImageBitmap(activity.getMainBit());
        activity.mainImage.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        activity.mainImage.setScaleEnabled(false);// 禁用缩放
        // System.out.println(r.left + "    " + r.top);
        activity.bannerFlipper.showNext();

		//  bug  fixed  https://github.com/siwangqishiq/ImageEditor-Android/issues/59
		// 设置完与屏幕匹配的尺寸  确保变换矩阵设置生效后才设置裁剪区域
		activity.mainImage.post(new Runnable() {
			@Override
			public void run() {
				final RectF r = activity.mainImage.getBitmapRect();
				activity.mCropPanel.setCropRect(r);
			}
		});
    }

    /**
	 * 返回按钮逻辑
	 * 
	 * @author panyi
	 * 
	 */
	private final class BackToMenuClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			backToMain();
		}
	}// end class

	/**
	 * 返回主菜单
	 */
	@Override
	public void backToMain() {
		//activity.mode = EditImageActivity.MODE_NONE;
		mCropPanel.setVisibility(View.GONE);
		activity.mainImage.setScaleEnabled(true);// 恢复缩放功能
		//activity.bottomGallery.setCurrentItem(0);
		if (selctedTextView != null) {
			selctedTextView.setTextColor(UNSELECTED_COLOR);
		}
		mCropPanel.setRatioCropRect(activity.mainImage.getBitmapRect(), -1);
		//activity.bannerFlipper.showPrevious();
	}

	/**
	 * 保存剪切图片
	 */
	public void applyCropImage() {
		// System.out.println("保存剪切图片");
		CropImageTask task = new CropImageTask();
		task.execute(activity.getMainBit());
	}

	/**
	 * 图片剪裁生成 异步任务
	 * 
	 * @author panyi
	 * 
	 */
	private final class CropImageTask extends AsyncTask<Bitmap, Void, Bitmap> {
		private Dialog dialog;

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
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = BaseActivity.getLoadingDialog(getActivity(), R.string.saving_image,
					false);
			dialog.show();
		}

		@SuppressWarnings("WrongThread")
        @Override
		protected Bitmap doInBackground(Bitmap... params) {
			RectF cropRect = mCropPanel.getCropRect();// 剪切区域矩形
			Matrix touchMatrix = activity.mainImage.getImageViewMatrix();
			// Canvas canvas = new Canvas(resultBit);
			float[] data = new float[9];
			touchMatrix.getValues(data);// 底部图片变化记录矩阵原始数据
			Matrix3 cal = new Matrix3(data);// 辅助矩阵计算类
			Matrix3 inverseMatrix = cal.inverseMatrix();// 计算逆矩阵
			Matrix m = new Matrix();
			m.setValues(inverseMatrix.getValues());
			m.mapRect(cropRect);// 变化剪切矩形

			// Paint paint = new Paint();
			// paint.setColor(Color.RED);
			// paint.setStrokeWidth(10);
			// canvas.drawRect(cropRect, paint);
			// Bitmap resultBit = Bitmap.createBitmap(params[0]).copy(
			// Bitmap.Config.ARGB_8888, true);
			Bitmap resultBit = Bitmap.createBitmap(params[0],
					(int) cropRect.left, (int) cropRect.top,
					(int) cropRect.width(), (int) cropRect.height());

			//saveBitmap(resultBit, activity.saveFilePath);
			return resultBit;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			dialog.dismiss();
			if (result == null)
				return;

            activity.changeMainBitmap(result,true);
			activity.mCropPanel.setCropRect(activity.mainImage.getBitmapRect());
			backToMain();
		}
	}// end inner class

	/**
	 * 保存Bitmap图片到指定文件
	 */
	public static void saveBitmap(Bitmap bm, String filePath) {
		File f = new File(filePath);
		if (f.exists()) {
			f.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println("保存文件--->" + f.getAbsolutePath());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		backToMain();
	}

	public  int dip2px( float dpValue) {
		float scale = activity.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}// end class
