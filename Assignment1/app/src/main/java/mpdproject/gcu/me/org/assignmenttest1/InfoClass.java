//
//
// Conner Macaulay
// S1432569
//
//
package mpdproject.gcu.me.org.assignmenttest1;

public class InfoClass
{
    private String _infoName;
    private String _infoDescription;
    private String _itemDate;

    public InfoClass()
    {
        _infoName = "";
        _infoDescription = "";
        _itemDate = "";
    }

    public InfoClass(String infoName, String infoDescription, String itemDate)
    {
        _infoName = infoName;
        _infoDescription = infoDescription;
        _itemDate = itemDate;
    }

    public String get_infoName()
    {
        return _infoName;
    }

    public void set_infoName(String infoName)
    {
        this._infoName = infoName;
    }

    public String get_infoDescription()
    {
        return _infoDescription;
    }

    public void set_infoDescription(String infoDescription)
    {
        this._infoDescription = infoDescription;
    }

    public String get_itemDate()
    {
        return _itemDate;
    }

    public void set_itemDate(String itemDate)
    {
        this._itemDate = itemDate;
    }

    public String toString()
    {
       //String temp = "";

        //temp =
        return _infoName + " " + _infoDescription + " " + _itemDate;

        //return temp;
    }

} // End of class
