package com.recycup.recycup;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.ImageViewCompat;

public class MainCardView extends ConstraintLayout {


    ImageView imageView;
    TextView textView;
    public MainCardView(Context context) {
        super(context);
        initView();

    }

    public MainCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);



    }

    public MainCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        getAttrs(attrs, defStyleAttr);


    }

    //xml을 inflate한다.
    private void initView() {

        inflate(getContext(), R.layout.main_cardview, this);


        imageView = (ImageView) findViewById(R.id.cardViewImage);
        textView = (TextView) findViewById(R.id.cardViewText);

    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MainCardView);

        setTypeArray(typedArray);
    }


    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MainCardView, defStyle, 0);
        setTypeArray(typedArray);

    }


    private void setTypeArray(TypedArray typedArray) {


        Drawable image = typedArray.getDrawable(R.styleable.MainCardView_image);



        int imageColor = typedArray.getColor(R.styleable.MainCardView_imageColor, 0);
        DrawableCompat.setTint(image,imageColor);

        imageView.setBackground(image);


        String text = typedArray.getString(R.styleable.MainCardView_text);
        textView.setText(text);

        int textColor = typedArray.getColor(R.styleable.MainCardView_textColor, 0);
        textView.setTextColor(textColor);

        float textSize = typedArray.getFloat(R.styleable.MainCardView_textSize,0);
        textView.setTextSize(textSize);


        typedArray.recycle();

    }

    public void setText(String text){
        textView.setText(text);
    }

    public void setImage(Drawable drawable){
        imageView.setBackground(drawable);
    }

    public void setTextColor(int color){
        textView.setTextColor(color);
    }

    public void setImageColor(int color){
        ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(color));
    }



}
