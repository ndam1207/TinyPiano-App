package com.example.android.myapplication;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.myapplication.noteplayers.NotePlayer;
import com.example.android.myapplication.touches.Touch;
import com.example.android.myapplication.touches.TouchAction;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.example.android.myapplication.touches.TouchAction.DOWN;
import static com.example.android.myapplication.touches.TouchAction.UP;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.toString() ;
    private List<ImageView> blackKeys = new ArrayList<>();
    private List<ImageView> whiteKeys = new ArrayList<>();
    private List<TouchInfo> touchInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        whiteKeys.add((ImageView) findViewById(R.id.c));
        whiteKeys.add((ImageView) findViewById(R.id.d));
        whiteKeys.add((ImageView) findViewById(R.id.e));
        whiteKeys.add((ImageView) findViewById(R.id.f));
        whiteKeys.add((ImageView) findViewById(R.id.g));
        whiteKeys.add((ImageView) findViewById(R.id.a));
        whiteKeys.add((ImageView) findViewById(R.id.b));

        blackKeys.add((ImageView) findViewById(R.id.c_sharp));
        blackKeys.add((ImageView) findViewById(R.id.d_sharp));
        blackKeys.add((ImageView) findViewById(R.id.f_sharp));
        blackKeys.add((ImageView) findViewById(R.id.g_sharp));
        blackKeys.add((ImageView) findViewById(R.id.a_sharp));

        NotePlayer.loadSounds(this);
    }

    @Override
        public boolean onTouchEvent(MotionEvent event) {

        List<Touch> touches = Touch.processEvent(event);

        Log.d(TAG, String.format("onTouchEvent: %s", touches));

        if (touches.size() == 0) return false;

        Touch firstTouch = touches.get(0);

        if (firstTouch.getAction() == DOWN) {
            ImageView pressedKey = findKeyByTouch(firstTouch);
            if (!checkPressedKey(pressedKey)) {
                //TODO: Play
                String note = pressedKey.getTag().toString();
                NotePlayer.play(note);
                touchInfoList.add(new TouchInfo(pressedKey, firstTouch));
            }
        }

        else if(firstTouch.getAction() == UP){
            ImageView pressedKey = findKeyByTouch(firstTouch);
            if(pressedKey != null) {
                Iterator<TouchInfo> touchInfoIterator = touchInfoList.iterator();
                while (touchInfoIterator.hasNext()) {
                    TouchInfo touchInfo = touchInfoIterator.next();
                    if (touchInfo.touch.getTouchId() == firstTouch.getTouchId()) {
                        touchInfoIterator.remove();
                    }
                }
            }
        }

        //TODO: Doc lai doan nay
        else if(firstTouch.getAction() == TouchAction.MOVE){
            for(Touch touch : touches){
                ImageView pressedKey = findKeyByTouch(touch);
                Iterator<TouchInfo> touchInfoIterator = touchInfoList.iterator();
                while(touchInfoIterator.hasNext()){
                    TouchInfo touchInfo = touchInfoIterator.next();
                    if(touchInfo.touch.equals(touch) && touchInfo.pressedKey != pressedKey){
                        touchInfoIterator.remove();
                    }
                }

                if(!checkPressedKey(pressedKey)){
                    //TODO: Play note
                    touchInfoList.add(new TouchInfo(pressedKey,touch));
                    String note = pressedKey.getTag().toString();
                    NotePlayer.play(note);
                }
            }
        }


//        for(View blackKey : blackKeys){
//            if(isInside(event.getX(), event.getY(), blackKey)) {
//                Log.d(TAG, "blackKey: " + blackKey.getTag());
//            }
//        }
//
//        for(View whiteKey : whiteKeys){
//            if(isInside(event.getX(), event.getY(), whiteKey) ) {
//                Log.d(TAG, "whiteKey: " + whiteKey.getTag());
//            }
//        }
        updateKeyImages();
        return super.onTouchEvent(event);
    }

    private void updateKeyImages(){
        for(ImageView blackKey : blackKeys){
            if(checkPressedKey(blackKey)){
                blackKey.setImageResource(R.drawable.pressed_black_key);
            }

            else{
                blackKey.setImageResource(R.drawable.default_black_key);
            }
        }

        for(ImageView whiteKey : whiteKeys){
            if(checkPressedKey(whiteKey)){
                whiteKey.setImageResource(R.drawable.pressed_white_key);
            }
            else{
                whiteKey.setImageResource(R.drawable.default_white_key);
            }
        }
    }

    private boolean checkPressedKey(ImageView pressedKey){
        for(TouchInfo touchInfo : touchInfoList){
            if(touchInfo.pressedKey == pressedKey) return true;
        }
        return false;
    }

    private ImageView findKeyByTouch(Touch touch){
        for(ImageView blackKey : blackKeys){
            if(touch.checkHit(blackKey)){
                return blackKey;
            }
        }

        for(ImageView whiteKey : whiteKeys){
            if(touch.checkHit(whiteKey)){
                return whiteKey;
            }
        }

        return null;
    }


//    public boolean isInside (float x, float y, View v){
//        int location[] = new int[2];
//        v.getLocationOnScreen(location);
//
//        int left = location[0];
//        int right = left + v.getWidth();
//        int top = location[1];
//        int bottom = top + v.getHeight();
//
//        return (x > left && x < right
//        && y > top && y < bottom);
//    }

    class TouchInfo {
        public ImageView pressedKey;
        public Touch touch;

        public TouchInfo(ImageView pressedKey, Touch touch) {
            this.pressedKey = pressedKey;
            this.touch = touch;
        }
    }
}
