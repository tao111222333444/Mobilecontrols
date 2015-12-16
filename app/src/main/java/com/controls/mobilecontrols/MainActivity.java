package com.controls.mobilecontrols;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  implements
        View.OnTouchListener {
    private View.OnClickListener aaa;
    private ImageView calculator;
    private FrameLayout.LayoutParams lParams;//计算器移动用的
    /**
     * 背景
     */
    private ViewGroup background;
    /**
     * 用于计算器移动用的参数
     */
    private int _xDelta;
    private int _yDelta;
    private float StartX;
    private float StartY;
    int width;
    int height;
    int screenWidth;
    int screenHeight;
    int NavigationBarheight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        calculator = (ImageView) findViewById(R.id.detail_calculator);
        background = (ViewGroup) findViewById(R.id.back);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        aaa = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"aaaaaa",Toast.LENGTH_SHORT).show();
            }};
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", aaa
                ).show();
            }
        });
        init();
    }
    /**
     * 初始化计算器
     */
    private void init(){
        lParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWidth_Height();
        getView_Width_Height(calculator);
        lParams.leftMargin =0-width/2;//计算器初始距离左边框为负的；控件宽度的一半
        lParams.topMargin = screenHeight/3*2;
        lParams.bottomMargin = -250;
        lParams.rightMargin = -250;


        getNavigationBarHeight();
        calculator.setVisibility(View.VISIBLE);
        calculator.setLayoutParams(lParams);
        calculator.setOnTouchListener(this);
    }

    /**
     * 获取控件的高和宽
     * @param view
     */
    private void getView_Width_Height(View view){
        width =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        height =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        calculator.measure(width, height);
        height = view.getMeasuredHeight();
        width=view.getMeasuredWidth();
    }

    /**
     * 获取屏幕宽高
     */
    private void getWidth_Height(){
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        screenWidth = mDisplayMetrics.widthPixels;
        screenHeight = mDisplayMetrics.heightPixels;
        Log.e("main", "宽度 " + screenWidth + "高度 " + screenHeight);
    }

    /**
     * 获取底部工具栏高度
     * @return
     */
    private void getNavigationBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android");
        NavigationBarheight = resources.getDimensionPixelSize(resourceId);
        Log.e("dbw", "Navi height:" + NavigationBarheight);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * 用于判断view的移动
     * @param view
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if(lParams.leftMargin<0){//判断控件是否半隐藏状态
                    lParams.leftMargin = 0;
                }else if(lParams.topMargin<0){
                    lParams.topMargin=0;
                }else if(X -_xDelta> screenWidth - width){
                    lParams.leftMargin = screenWidth-width;
                }
                _xDelta = X - lParams.leftMargin;
                _yDelta = Y - lParams.topMargin;
                StartX =  X;
                StartY = Y;
//				Log.e(TAG,"_xDelta "+_xDelta+"  _yDelta "+_yDelta+"  lParams.rightMargin "+lParams.rightMargin);
                break;
            case MotionEvent.ACTION_UP:
                //关键部分：移动距离较小，视为onclick点击行为
                if (Math.abs(X - StartX) < 1.5 && Math.abs(Y - StartY) < 1.5){//计算器弹出
                    //这放点击后的逻辑代码
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
//				RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
//						.getLayoutParams();
                if(X - _xDelta<0 - width/2){
                    lParams.leftMargin = 0- width/2;
                }else if(X -_xDelta> screenWidth - width/2){
                    lParams.leftMargin = screenWidth - width/2;
                }else{
                    lParams.leftMargin = X - _xDelta;
                }
                if(Y - _yDelta<0 - height/2){
                    lParams.topMargin = 0 - height/2;
                }else if(Y -_yDelta > screenHeight - height-NavigationBarheight){
                    lParams.topMargin = screenHeight - height-NavigationBarheight;
                }else{
                    lParams.topMargin = Y - _yDelta;
                }
                lParams.rightMargin = -250;
                lParams.bottomMargin = -250;
                view.setLayoutParams(lParams);
                break;
        }
        background.invalidate();
        return true;
    }
}
