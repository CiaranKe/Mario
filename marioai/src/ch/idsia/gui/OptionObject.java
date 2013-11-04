package ch.idsia.gui;

/**
 * Created with IntelliJ IDEA.
 * User: Ciaran Kearney
 * Date: 04/11/13
 */
public class OptionObject
{
    private String option;
    private String optionValue;

    public OptionObject(String newOption, String newOptionValue)
    {
        this.option = newOption;
        this.optionValue = newOptionValue;
    }

    public void setValue(String newOptionValue)
    {
        this.optionValue = newOptionValue;
    }

    public String toString()
    {
        return this.option + this.optionValue;
    }
}
