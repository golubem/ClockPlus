package com.philliphsu.clock2.timepickers;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.philliphsu.clock2.R;
import com.philliphsu.clock2.util.ConfigurationUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Phillip Hsu on 7/21/2016.
 */
public class TwentyFourHourGridItem extends LinearLayout {

    @Bind(R.id.primary) TextView mPrimaryText;
    @Bind(R.id.secondary) TextView mSecondaryText;

    public TwentyFourHourGridItem(Context context) {
        super(context);
        init();
    }

    public TwentyFourHourGridItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.TwentyFourHourGridItem, 0, 0);
        try {
            setPrimaryText(a.getString(R.styleable.TwentyFourHourGridItem_primaryText));
            setSecondaryText(a.getString(R.styleable.TwentyFourHourGridItem_secondaryText));
        } finally {
            a.recycle();
        }
    }

    public CharSequence getPrimaryText() {
        return mPrimaryText.getText();
    }

    public void setPrimaryText(CharSequence text) {
        mPrimaryText.setText(text);
    }

    public CharSequence getSecondaryText() {
        return mSecondaryText.getText();
    }

    public void setSecondaryText(CharSequence text) {
        mSecondaryText.setText(text);
    }

    public void swapTexts() {
        CharSequence primary = mPrimaryText.getText();
        setPrimaryText(mSecondaryText.getText());
        setSecondaryText(primary);
    }

    public TextView getPrimaryTextView() {
        return (TextView) getChildAt(0);
    }

    public TextView getSecondaryTextView() {
        return (TextView) getChildAt(1);
    }

    private void init() {
        // TODO: Why isn't ALT-ENTER giving us an option to static import this method?
        final int orientation = ConfigurationUtils.getOrientation(getResources());
        setOrientation(orientation == Configuration.ORIENTATION_PORTRAIT ?
                VERTICAL : /*LANDSCAPE*/HORIZONTAL);
        setGravity(Gravity.CENTER);
        inflate(getContext(), R.layout.content_24h_grid_item, this);
        ButterKnife.bind(this);
    }
}
