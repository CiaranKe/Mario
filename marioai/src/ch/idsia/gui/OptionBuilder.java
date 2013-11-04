package ch.idsia.gui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Ciaran Kearney
 * Date: 04/11/13
 */
public class OptionBuilder
{
    Map<String,OptionObject> options;

    public OptionBuilder()
    {
        this.options = new HashMap<String,OptionObject>();
    }

    public OptionObject getOption(String optionName)
    {
        return this.options.get(optionName);
    }

    public String buildOptionList()
    {
        StringBuilder optionString = new StringBuilder();
        for (Map.Entry<String,OptionObject> option: this.options.entrySet())
        {
            optionString.append(option.getValue().toString());
        }
        return optionString.toString();
    }

}
