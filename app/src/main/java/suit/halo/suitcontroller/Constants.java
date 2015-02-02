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

    public static final String DEVICE_IDENTIFIER = "beagle";
    public static final int DEVICE_CHANNEL = 2;
    public static final int blueToothChannel = 1;

    public static enum VOICE_MENU_MODE
    {
        KEYWORD, FIRST_LEVEL, SECOND_LEVEL
    }


    public static final String[] getFirstWords()
    {
        List<String> ret = new LinkedList<>();

        Map<String, String[]> commandPhrases = getCommandPhrases();
        for (String s : commandPhrases.keySet())
        {
            ret.add(s);
        }
        String[] retArr = new String[ret.size()];
        return ret.toArray(retArr);
    }

    public static final String[] getSecondWords(String firstWord)
    {
        List<String> ret = new LinkedList<>();

        Map<String, String[]> commandPhrases = getCommandPhrases();
        for (String s : commandPhrases.get(firstWord))
        {
            ret.add(s);
        }
        String[] retArr = new String[ret.size()];
        return ret.toArray(retArr);
    }


    public static final Map<String, String[]> getCommandPhrases()
    {
        Map<String, String[]> commandPhrases = new HashMap<>();
        String[] cooling = new String[2];
        cooling[0] = "on";
        cooling[1] = "off";
        String[] lights = new String[3];
        lights[0] = "on";
        lights[1] = "off";
        lights[2] = "auto";
        String[] headLights = new String[2];
        headLights[0] = "on";
        headLights[1] = "off";
        commandPhrases.put("cooling", cooling);
        commandPhrases.put("lights", lights);
        commandPhrases.put("head lights", headLights);

        return commandPhrases;
    }

    public static final Integer getIconResource(String info)
    {
        switch (info)
        {
            case "cooling":
                return R.drawable.cooling;
            case "lights":
                return R.drawable.lights;
            case "head lights":
                return R.drawable.head_light;
            case "on":
                return R.drawable.icon_low;
            case "off":
                return R.drawable.icon_off;
            case "auto":
                return R.drawable.icon_high;
            default:
                return R.drawable.ic_glass_logo;
        }
    }
}