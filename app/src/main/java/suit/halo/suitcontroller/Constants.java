package suit.halo.suitcontroller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Constants
{
    public static final String HOTWORD = "hotword";
    public static final String PHRASES = "phrases";

    public static final String OK_GLASS = "Pear";
    public static final String COOLING_HIGH = "Cooling High";
    public static final String COOLING_MEDIUM = "Cooling Medium";
    public static final String COOLING_LOW = "Cooling Low";
    public static final String COOLING_OFF = "Cooling Off";
    public static final String LIGHTS_HIGH = "Lights High";
    public static final String LIGHTS_LOW = "Lights Low";
    public static final String LIGHTS_OFF = "Lights Off";

    public static enum VOICE_MENU_MODE
    {
        KEYWORD, FIRST_LEVEL, SECOND_LEVEL
    }


    public static final String[] getFirstWords()
    {
        List<String> ret = new LinkedList<>();

        Map<String,String[]> commandPhrases = getCommandPhrases();
        for(String s : commandPhrases.keySet())
        {
            ret.add(s);
        }
        String [] retArr = new String[ret.size()];
        return ret.toArray(retArr);
    }

    public static final String[] getSecondWords(String firstWord)
    {
        List<String> ret = new LinkedList<>();

        Map<String,String[]> commandPhrases = getCommandPhrases();
        for(String s : commandPhrases.get(firstWord))
        {
            ret.add(s);
        }
        String [] retArr = new String[ret.size()];
        return ret.toArray(retArr);
    }


    public static final Map<String, String[]> getCommandPhrases()
    {
        Map<String, String[]> commandPhrases = new HashMap<>();
        String[] cooling = new String[4];
        cooling[0] = "Low";
        cooling[1] = "Medium";
        cooling[2] = "High";
        cooling[3] = "Off";
        String[] lights = new String[3];
        lights[0] = "Low";
        lights[1] = "Off";
        lights[2] = "High";
        commandPhrases.put("Cooling",cooling);
        commandPhrases.put("Lights",lights);

        return commandPhrases;
    }
}

