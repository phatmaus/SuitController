package suit.halo.suitcontroller;

import android.content.Context;

import com.google.glass.voice.VoiceCommand;
import com.google.glass.voice.VoiceConfig;
import com.google.glass.voice.VoiceInputHelper;

import java.util.LinkedList;

public class VoiceDetection extends StubVoiceListener
{

    private static final String THIS = VoiceDetection.class.getSimpleName();

    private final VoiceConfig mVoiceConfig;
    private String[] keyWordPhrases;
    private String[] commandPhrases;
    private VoiceInputHelper mVoiceInputHelper;
    private VoiceDetectionListener mListener;
    private boolean mRunning = true;
    private boolean isInCommandMode;

    public VoiceDetection(Context context, String keyWord, VoiceDetectionListener listener, String[] commands, String[] junkWords)
    {
        mVoiceInputHelper = new VoiceInputHelper(context, this);

        keyWordPhrases = assemblePhrases(new String[]{keyWord}, new String[0], junkWords);
        commandPhrases = assemblePhrases(new String[0], commands, new String[0]);

        mVoiceConfig = new VoiceConfig(keyWordPhrases);
        mVoiceConfig.setShouldSaveAudio(false);

        mListener = listener;
    }

    private String[] assemblePhrases(String[] hotword, String[] commands, String[] junkWords)
    {
        LinkedList<String> combinedPhraseList = new LinkedList<>();
        for (String s : hotword)
        {
            combinedPhraseList.add(s);
        }
        for (String s : commands)
        {
            combinedPhraseList.add(s);
        }
        for (String s : junkWords)
        {
            combinedPhraseList.add(s);
        }
        return combinedPhraseList.toArray(new String[combinedPhraseList.size()]);
    }

    public void changePhrases(boolean isInCommandMode)
    {
        this.isInCommandMode = isInCommandMode;
        mVoiceConfig.setCustomPhrases(isInCommandMode ? commandPhrases : keyWordPhrases);
        mVoiceInputHelper.setVoiceConfig(mVoiceConfig);
    }

    @Override
    public VoiceConfig onVoiceCommand(VoiceCommand vc)
    {
        String literal = vc.getLiteral();

        if(isInCommandMode)
        {
            for (int i = 1; i < commandPhrases.length; ++i)
            {
                String item = commandPhrases[i];
                if(item.equalsIgnoreCase(literal))
                {
                    mListener.onPhraseDetected(i , literal);
                    return null;
                }
            }
        }
        else if (keyWordPhrases[0].equalsIgnoreCase(literal))
        {
           mListener.onHotwordDetected();
            return null;
        }
        return null;
    }

    public void start()
    {
        mRunning = true;
        mVoiceInputHelper.setVoiceConfig(mVoiceConfig);
    }

    public void stop()
    {
        mRunning = false;
        mVoiceInputHelper.setVoiceConfig(null);
    }

    @Override
    public boolean isRunning()
    {
        return mRunning;
    }

    public interface VoiceDetectionListener
    {
        public void onHotwordDetected();

        public void onPhraseDetected(int index, String phrase);
    }
}

