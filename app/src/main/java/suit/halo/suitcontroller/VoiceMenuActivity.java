package suit.halo.suitcontroller;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.glass.eye.EyeGesture;
import com.google.android.glass.eye.EyeGestureManager;
import com.google.android.glass.eye.EyeGestureManager.Listener;

public class VoiceMenuActivity extends Activity implements VoiceDetection.VoiceDetectionListener
{
    private TextView mTextView;
    private VoiceDetection mVoiceDetection;
    private HeadListView mScroll;
    private EyeGestureManager mEyeGestureManager;
    private EyeGestureListener mEyeGestureListener;
    private EyeGesture winkGesture = EyeGesture.WINK;
    public String firstWord = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.voice_menu);

        mEyeGestureManager = EyeGestureManager.from(this);
        mEyeGestureListener = new EyeGestureListener();

        mTextView = (TextView) findViewById(R.id.status_text);

        mVoiceDetection = new VoiceDetection(this, Constants.OK_GLASS, this, getResources().getStringArray(R.array.junk_words));

        mScroll = (HeadListView) findViewById(R.id.hotword_chooser);
        mScroll.setAdapter(new ArrayAdapter<>(this, R.layout.voice_menu_item, Constants.getFirstWords()));
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        mEyeGestureManager.register(winkGesture, mEyeGestureListener);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        mEyeGestureManager.unregister(winkGesture, mEyeGestureListener);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_CENTER));
        dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_CENTER));
        mVoiceDetection.changePhrases(Constants.VOICE_MENU_MODE.KEYWORD);
        mVoiceDetection.start();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mVoiceDetection.stop();
    }

    @Override
    public void onHotwordDetected()
    {
        processCommand("Command mode");
        mVoiceDetection.changePhrases(Constants.VOICE_MENU_MODE.FIRST_LEVEL);
    }

    @Override
    public void onFirstWordDetected(int index, String phrase)
    {
        firstWord = phrase;

        mScroll.setAdapter(new ArrayAdapter<>(this, R.layout.voice_menu_item, Constants.getSecondWords(phrase)));

        mVoiceDetection.changePhrases(Constants.VOICE_MENU_MODE.SECOND_LEVEL,phrase);
    }

    @Override
    public void onSecondWordDetected(int index, String phrase)
    {
        processCommand(firstWord + " " + phrase);

        mScroll.setAdapter(new ArrayAdapter<>(this, R.layout.voice_menu_item, Constants.getFirstWords()));

        mVoiceDetection.changePhrases(Constants.VOICE_MENU_MODE.KEYWORD,phrase);
    }

    private class EyeGestureListener implements Listener
    {

        @Override
        public void onEnableStateChange(EyeGesture eyeGesture, boolean paramBoolean)
        {
        }

        @Override
        public void onDetected(final EyeGesture eyeGesture)
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    String selectedItem = mScroll.getSelectedItem().toString();
                    if((mVoiceDetection.mode == Constants.VOICE_MENU_MODE.FIRST_LEVEL)||(mVoiceDetection.mode == Constants.VOICE_MENU_MODE.KEYWORD))
                    {
                        firstWord = selectedItem;
                        mScroll.setAdapter(new ArrayAdapter<String>(VoiceMenuActivity.this, R.layout.voice_menu_item,
                                Constants.getSecondWords(selectedItem)));
                        mVoiceDetection.changePhrases(Constants.VOICE_MENU_MODE.SECOND_LEVEL,selectedItem);
                        processCommand(selectedItem);
                    }
                    else
                    {//second level

                        processCommand(firstWord + " " + selectedItem);
                        mScroll.setAdapter(new ArrayAdapter<>(VoiceMenuActivity.this, R.layout.voice_menu_item, Constants.getFirstWords()));

                        mVoiceDetection.changePhrases(Constants.VOICE_MENU_MODE.KEYWORD,selectedItem);
                    }
                }
            });

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return true;
    }

    public void processCommand(String command)
    {
        mTextView.setText(command);
    }


}