package suit.halo.suitcontroller;

import android.app.Activity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.voice_menu);

        mEyeGestureManager = EyeGestureManager.from(this);
        mEyeGestureListener = new EyeGestureListener();

        mTextView = (TextView) findViewById(R.id.status_text);

        mVoiceDetection = new VoiceDetection(this, Constants.OK_GLASS, this, Constants.VOICE_MENU_OPTIONS, getResources().getStringArray(R.array.junk_words));

        mScroll = (HeadListView) findViewById(R.id.hotword_chooser);
        mScroll.setAdapter(new ArrayAdapter<String>(this, R.layout.voice_menu_item, Constants.VOICE_MENU_OPTIONS));
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
        mVoiceDetection.changePhrases(false);
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
        mTextView.setText("Command mode");
        mVoiceDetection.changePhrases(true);
    }

    @Override
    public void onPhraseDetected(int index, String phrase)
    {
        mTextView.setText(phrase);
        mVoiceDetection.changePhrases(false);
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
                    mTextView.setText(((String) mScroll.getSelectedItem()));
                }
            });

        }
    }
}