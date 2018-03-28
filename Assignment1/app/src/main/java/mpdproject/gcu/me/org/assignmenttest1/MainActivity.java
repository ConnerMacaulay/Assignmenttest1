//
//
// Conner Macaulay
// S1432569
//
//

package mpdproject.gcu.me.org.assignmenttest1;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private String url1="https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String url2="https://trafficscotland.org/rss/feeds/roadworks.aspx";
    private String url3="https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String url4= "http://floodline.sepa.org.uk/feed/floodline.aspx";
    private TextView urlInput;
    private Button CIButton;
    private Button CRButton;
    private Button PRButton;


    public int urlSelected = 0;
    public String finalIncidents = "";
    public String finalRoadworks = "";
    public String finalPlannedRoadworks = "";

    LinkedList <InfoClass> infoList = null;
    LinkedList <InfoClass> incidentsList = null;
    LinkedList <InfoClass> roadworksList = null;
    LinkedList <InfoClass> plannedRoadworksList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        urlInput = (TextView)findViewById(R.id.urlInput);
        CIButton = (Button)findViewById(R.id.CIButton);
        CIButton.setOnClickListener(this);
        CRButton = (Button)findViewById(R.id.CRButton);
        CRButton.setOnClickListener(this);
        PRButton = (Button)findViewById(R.id.PRButton);
        PRButton.setOnClickListener(this);
    } // End of onCreate

    //Temp List
    private LinkedList<InfoClass> parseData(String dataToParse)
    {
        InfoClass info = null;
        LinkedList <InfoClass> infoList = null;
        boolean found = false;

        try
        {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput( new StringReader( dataToParse ) );
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                // Found a start tag
                if(eventType == XmlPullParser.START_TAG)
                {
                    // Check which Tag we have
                    if (xpp.getName().equalsIgnoreCase("channel"))
                    {
                        infoList  = new LinkedList<InfoClass>();
                    }
                    else
                    if (xpp.getName().equalsIgnoreCase("item"))
                    {
                        Log.e("XMLParser","Item Start Tag found");
                        info = new InfoClass();
                        found = true;
                    }
                    else
                    if (xpp.getName().equalsIgnoreCase("title")&& found)
                    {
                        // Now just get the associated text
                        String temp = xpp.nextText();
                        // Do something with text
                        Log.e("XMLParser", temp);
                        info.set_infoName(temp);
                    }
                    else
                        // Check which Tag we have
                        if (xpp.getName().equalsIgnoreCase("description")&& found)
                        {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            // Do something with text
                            Log.e("XMLParser", temp);
                            info.set_infoDescription(temp);
                        }
                        else
                            // Check which Tag we have
                            if (xpp.getName().equalsIgnoreCase("pubDate")&& found)
                            {
                                // Now just get the associated text
                                String temp = xpp.nextText();
                                // Do something with text
                                Log.e("XMLParser", temp);
                                info.set_itemDate(temp);
                            }
                }
                else
                if(eventType == XmlPullParser.END_TAG)
                {
                    if (xpp.getName().equalsIgnoreCase("item"))
                    {
                        Log.e("XMLParser","Item is " + info.toString());
                        infoList.add(info);
                    }
                    else
                    if (xpp.getName().equalsIgnoreCase("channel"))
                    {
                        int size;
                        size = infoList.size();
                        Log.e("XMLParser","Number of incidents " + size);
                    }
                }


                // Get the next event
                eventType = xpp.next();

            } // End of while

            //return alist;
        }
        catch (XmlPullParserException ae1)
        {
            Log.e("XMLParser","Parsing error" + ae1.toString());
        }
        catch (IOException ae1)
        {
            Log.e("XMLParser","IO error during parsing");
        }

        Log.e("XMLParser","End document");

        return infoList;

    }//End of temp list

    public void onClick(View aview)
    {
        switch(aview.getId())
        {
            case R.id.CIButton:
                urlSelected = 0;
                startProgress();
                break;
            case R.id.CRButton:
                urlSelected = 1;
                startProgress();
                break;
            case R.id.PRButton:
                urlSelected = 2;
                startProgress();
                break;
        }
    }

    public void startProgress()
    {
        switch(urlSelected) {
            // Run network access on a separate thread;
            case 0:
                new Thread(new Task(url1)).start();
                break;
            case 1:
                new Thread(new Task(url2)).start();
                break;
            case 2:
                new Thread(new Task(url3)).start();
                break;
        }
    } //

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    class Task implements Runnable
    {
        private String result = "";
        private String firstResult = "";
        private String url;

        public Task(String aurl)
        {
            url = aurl;
        }
        @Override
        public void run()
        {

            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";


            Log.e("XMLParser","in run");

            try
            {
                Log.e("XMLParser","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                while ((inputLine = in.readLine()) != null)
                {
                    result = result + inputLine;
                    Log.e("XMLParser",inputLine);

                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("XMLParser", "ioexception");
            }

            infoList = parseData(result);


            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run()
                {
                    switch(urlSelected)
                    {
                        case 0:
                            incidentsList = (LinkedList<InfoClass>)infoList.clone();
                            for (int i = 0; i < incidentsList.size(); i++)
                            {
                                incidentsList.get(i).toString().replace("<br />", " ");
                                finalIncidents = finalIncidents + incidentsList.get(i).toString() + "\n" + "\n";

                            }
                            String incidents = finalIncidents.replaceAll("<br />", "\n");
                            urlInput.setText(incidents);
                            break;
                        case 1:
                            roadworksList = (LinkedList<InfoClass>)infoList.clone();
                            for (int i = 0; i < roadworksList.size(); i++)
                            {
                                roadworksList.get(i).toString().replace("<br />", " ");
                                finalRoadworks = finalRoadworks + roadworksList.get(i).toString() + "\n" + "\n";
                            }
                            String roadworks = finalRoadworks.replaceAll("<br />", "\n");
                            urlInput.setText(roadworks);

                            break;
                        case 2:
                            plannedRoadworksList = (LinkedList<InfoClass>)infoList.clone();
                            for (int i = 0; i < plannedRoadworksList.size(); i++)
                            {
                                plannedRoadworksList.get(i).toString().replace("<br />", " ");
                                finalPlannedRoadworks = finalPlannedRoadworks + plannedRoadworksList.get(i).toString() + "\n" + "\n";
                            }
                            String plannedRoadworks = finalPlannedRoadworks.replaceAll("<br />", "\n");
                            urlInput.setText(plannedRoadworks);
                            break;
                    }

                }
            });
        }

    }
}
