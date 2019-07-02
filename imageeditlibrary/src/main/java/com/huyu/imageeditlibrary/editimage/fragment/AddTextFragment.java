package com.huyu.imageeditlibrary.editimage.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.huyu.imageeditlibrary.R;
import com.huyu.imageeditlibrary.editimage.EditImageActivity;
import com.huyu.imageeditlibrary.editimage.ModuleConfig;
import com.huyu.imageeditlibrary.editimage.task.StickerTask;
import com.huyu.imageeditlibrary.editimage.ui.ColorPicker;
import com.huyu.imageeditlibrary.editimage.view.TextStickerView;


/**
 * 添加文本贴图
 *
 * @author 潘易
 */
public class AddTextFragment extends BaseEditFragment implements TextWatcher {
    public static final int INDEX = ModuleConfig.INDEX_ADDTEXT;
    public static final String TAG = AddTextFragment.class.getName();

    private View mainView;
    private View backToMenu;// 返回主菜单

    private EditText mInputText;//输入框
    private ImageView mTextColorSelector;//颜色选择器
    private TextStickerView mTextStickerView;// 文字贴图显示控件
    private CheckBox mAutoNewLineCheck;

    private ColorPicker mColorPicker;

    private int mTextColor = Color.WHITE;
    private InputMethodManager imm;

    private SaveTextStickerTask mSaveTask;
    private ImageView cbtn1;
    private ImageView cbtn2;
    private ImageView cbtn3;
    private ImageView cbtn4;
    private ImageView cbtn5;
    private ImageView cbtn6;
    private ImageView cbtn7;
    private ImageView cbtn8;
    private ImageView cbtn9;
    public static AddTextFragment newInstance() {
        AddTextFragment fragment = new AddTextFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mainView = inflater.inflate(R.layout.fragment_edit_image_add_text, null);
        cbtn1 = mainView.findViewById(R.id.c_btn1);
        cbtn1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextStickerView.setTextColor(Color.rgb(67, 67, 67));
            }
        });
        cbtn2 = mainView.findViewById(R.id.c_btn2);
        cbtn2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextStickerView.setTextColor(Color.rgb(149, 149, 149));
            }
        });
        cbtn3 = mainView.findViewById(R.id.c_btn3);
        cbtn3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextStickerView.setTextColor(Color.rgb(238, 238, 238));
            }
        });
        cbtn4 = mainView.findViewById(R.id.c_btn4);
        cbtn4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextStickerView.setTextColor(Color.rgb(255, 0, 0));
            }
        });
        cbtn5 = mainView.findViewById(R.id.c_btn5);
        cbtn5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextStickerView.setTextColor(Color.rgb(120, 100, 100));
            }
        });
        cbtn6 = mainView.findViewById(R.id.c_btn6);
        cbtn6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextStickerView.setTextColor(Color.rgb(0, 0, 255));
            }
        });
        cbtn7 = mainView.findViewById(R.id.c_btn7);
        cbtn7.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextStickerView.setTextColor(Color.rgb(255, 255, 0));
            }
        });
        cbtn8 = mainView.findViewById(R.id.c_btn8);
        cbtn8.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextStickerView.setTextColor(Color.rgb(0, 255, 255));
            }
        });
        cbtn9 = mainView.findViewById(R.id.c_btn9);
        cbtn9.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextStickerView.setTextColor(Color.rgb(255, 0, 255));
            }
        });
        return mainView;
    }

    @Override
    public void onResume() {
        super.onResume();
        onShow();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mTextStickerView = (TextStickerView)getActivity().findViewById(R.id.text_sticker_panel);

        backToMenu = mainView.findViewById(R.id.back_to_main);
        mInputText = (EditText) mainView.findViewById(R.id.text_input);
        mTextColorSelector = (ImageView) mainView.findViewById(R.id.text_color);
        mAutoNewLineCheck = (CheckBox) mainView.findViewById(R.id.check_auto_newline);

        backToMenu.setOnClickListener(new BackToMenuClick());// 返回主菜单
        mColorPicker = new ColorPicker(getActivity(), 255, 0, 0,this);
        mTextColorSelector.setOnClickListener(new SelectColorBtnClick());
        mInputText.addTextChangedListener(this);
        mTextStickerView.setEditText(mInputText);

        //统一颜色设置
        //mTextColorSelector.setBackgroundColor(mColorPicker.getColor());
        mTextStickerView.setTextColor(mColorPicker.getColor());
    }

    @Override
    public void afterTextChanged(Editable s) {
        //mTextStickerView change
        String text = s.toString().trim();
        mTextStickerView.setText(text);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    /**
     * 颜色选择 按钮点击
     */
    private final class SelectColorBtnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mColorPicker.show();
            ImageButton okColor = (ImageButton) mColorPicker.findViewById(R.id.okColorButton);
            okColor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeTextColor(mColorPicker.getColor());
                    mColorPicker.dismiss();
                }
            });
        }
    }//end inner class

    /**
     * 修改字体颜色
     *
     * @param newColor
     */
    public void changeTextColor(int newColor) {
        this.mTextColor = newColor;
        //mTextColorSelector.setBackgroundColor(mTextColor);
        mTextStickerView.setTextColor(mTextColor);
    }

    public void hideInput() {
        if (getActivity() != null && getActivity().getCurrentFocus() != null && isInputMethodShow()) {
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public boolean isInputMethodShow() {
        return imm.isActive();
    }

    /**
     * 返回按钮逻辑
     *
     * @author panyi
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
        hideInput();
        //activity.mode = EditImageActivity.MODE_NONE;
        //activity.bottomGallery.setCurrentItem(MainMenuFragment.INDEX);
        activity.mainImage.setVisibility(View.VISIBLE);
        //activity.bannerFlipper.showPrevious();
        mTextStickerView.setVisibility(View.GONE);
    }

    @Override
    public void onShow() {
        //activity.mode = EditImageActivity.MODE_TEXT;
        activity.mainImage.setImageBitmap(activity.getMainBit());
        activity.bannerFlipper.showNext();
        mTextStickerView.setVisibility(View.VISIBLE);
        mInputText.clearFocus();
    }

    /**
     * 保存贴图图片
     */
    public void applyTextImage() {
        if (mSaveTask != null) {
            mSaveTask.cancel(true);
        }

        //启动任务
        mSaveTask = new SaveTextStickerTask(activity);
        mSaveTask.execute(activity.getMainBit());
    }

    /**
     * 文字合成任务
     * 合成最终图片
     */
    private final class SaveTextStickerTask extends StickerTask {

        public SaveTextStickerTask(EditImageActivity activity) {
            super(activity);
        }

        @Override
        public void handleImage(Canvas canvas, Matrix m) {
            float[] f = new float[9];
            m.getValues(f);
            int dx = (int) f[Matrix.MTRANS_X];
            int dy = (int) f[Matrix.MTRANS_Y];
            float scale_x = f[Matrix.MSCALE_X];
            float scale_y = f[Matrix.MSCALE_Y];
            canvas.save();
            canvas.translate(dx, dy);
            canvas.scale(scale_x, scale_y);
            //System.out.println("scale = " + scale_x + "       " + scale_y + "     " + dx + "    " + dy);
            mTextStickerView.drawText(canvas, mTextStickerView.layout_x,
                    mTextStickerView.layout_y, mTextStickerView.mScale, mTextStickerView.mRotateAngle);
            canvas.restore();
        }

        @Override
        public void onPostResult(Bitmap result) {
            mTextStickerView.clearTextContent();
            mTextStickerView.resetView();

            activity.changeMainBitmap(result , true);
            backToMain();
        }
    }//end inner class

    @Override
    public void onDestroy() {
        super.onDestroy();
        backToMain();
        if (mSaveTask != null && !mSaveTask.isCancelled()) {
            mSaveTask.cancel(true);
        }
    }
}// end class
