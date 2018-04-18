package com.prolificinteractive.materialcalendarview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by cuonghc on 4/10/18.
 */

public class CustomDayView extends LinearLayout {
    private String mDay = "-1";
    private boolean mIsChecked = false;
    protected int selectionColor = Color.BLACK;
    private int mTextDayColor = Color.BLACK;

    public CustomDayView(Context context) {
        super(context);
        init();
    }

    public CustomDayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomDayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private CustomDay mCustomDay;

    public void setCustomDay(CustomDay customDay) {
        mCustomDay = customDay;
    }

    private void init() {
        setOrientation(LinearLayout.VERTICAL);
        selectionColor = Color.RED;
//        setChecked(mIsChecked);
    }

    public void setTextColor(int colors) {
        mTextDayColor = colors;
        mTextViewDay.setTextColor(colors);
    }

    public ColorStateList getTextColors() {
        return mTextViewDay.getTextColors();
    }

    public void setText(SpannableString text) {
        setText(String.valueOf(text));
    }

    public void setText(String text) {
        mDay = text;
        removeAllViews();
        addView(createDayView(String.valueOf(text)));
        updateLayoutItem();
//        setChecked(mIsChecked);
//        addView(createNoteView(5));
//        addView(createPriceView(5));
    }


    private void updateLayoutItem() {
        int textColor = Color.GRAY;
        if (mCustomDay != null) {
            if (mCustomDay.getEnable()) {
                if (mCustomDay.getNote() != null && mCustomDay.getNote().size() > 0) {
                    addView(createNoteView(mCustomDay.getNote().get(0)));
                    if (mCustomDay.getNote().size() == 2) {
                        addView(createNoteView(mCustomDay.getNote().get(1)));
                    }
                }
                if (mCustomDay.getPrice() != null) {
                    addView(createPriceView(mCustomDay.getPrice()));
                }
                textColor = Color.BLACK;
            }
            mTextViewDay.setTextColor(textColor);
            mTextViewDay.invalidate();
        }
    }



    public void setChecked(boolean checked) {
        mIsChecked = checked;
        regenerateBackground();
    }

    protected void regenerateBackground() {
        if (mIsChecked) {
            setBackgroundColor(selectionColor);
//            Log.e(getClass().getSimpleName(), "checked: " + String.valueOf(mDay));
        } else {
            setBackgroundColor(0);
//            Log.e(getClass().getSimpleName(), "not checked: " + String.valueOf(mDay));
        }
    }

    public boolean isChecked() {
        return mIsChecked;
    }

    public void setTextAppearance(Object text, Object text1) {

    }

    public String getText() {
        return mDay;
    }

    private TextView mTextViewDay;

    private TextView createDayView(String day) {
        mTextViewDay = new TextView(getContext());
        mTextViewDay.setTextColor(mTextDayColor);
        mTextViewDay.setGravity(Gravity.CENTER);
        setPadding(5,5,5,5);
        mTextViewDay.setText(day);
        mTextViewDay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        return mTextViewDay;
    }

    private TextView createNoteView(String note) {
        TextView textView = new TextView(getContext());
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimension(R.dimen.text_size_custom_day));
        textView.setPadding(0,5,0,5);
        textView.setText(note);
        textView.setBackgroundColor(Color.RED);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,5,0,0);
        textView.setLayoutParams(layoutParams);
        return textView;
    }

    private RelativeLayout createPriceView(int price) {
        RelativeLayout relativeLayout = new RelativeLayout(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;
        relativeLayout.setLayoutParams(layoutParams);

        TextView textView = new TextView(getContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimension(R.dimen.text_size_custom_day));
        if (price >= 0) {
            textView.setText("+" + String.valueOf(price));
            textView.setTextColor(Color.BLACK);
        } else {
            textView.setText(String.valueOf(price));
            textView.setTextColor(Color.RED);
        }
        textView.setGravity(Gravity.CENTER);
        setPadding(5,5,5,5);
        textView.setTypeface(null, Typeface.BOLD);
        relativeLayout.addView(textView);
        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        textView.setLayoutParams(layoutParams1);

        return relativeLayout;
    }
}
