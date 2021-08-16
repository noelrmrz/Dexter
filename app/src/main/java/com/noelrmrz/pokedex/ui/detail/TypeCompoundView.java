package com.noelrmrz.pokedex.ui.detail;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.flexbox.FlexboxLayout;
import com.noelrmrz.pokedex.R;
import com.noelrmrz.pokedex.utilities.HelperTools;

public class TypeCompoundView extends FlexboxLayout {

    TextView textViewEffectiveness;
    TextView textViewTypeName;
    GradientDrawable typeBackground;

    public TypeCompoundView(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.cv_type, this, true);

        textViewTypeName = (TextView) getChildAt(0);

        textViewEffectiveness = (TextView) getChildAt(1);

        // set the background color
        setBackgroundResource(R.drawable.rectangle);
        typeBackground = (GradientDrawable) getBackground();

        // set up the margins
        FlexboxLayout.LayoutParams params= new FlexboxLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        int margins = (int) HelperTools.convertDpToPixel(4, getContext());
        params.setMargins(margins, margins, margins, margins);
        setLayoutParams(params);
    }

    public TypeCompoundView(Context context) {
        this(context, null);
    }

    public void setValueColor(int color) {
        // set the background
        typeBackground.setColor(color);
    }

    public void setTypeName(String typeName) {
        textViewTypeName.setText(typeName);
    }

    public void setTypeEffect(String effectiveness) {
        textViewEffectiveness.setText(effectiveness);
    }
}
