package bluetooth.client.pbap;

import android.util.Log;
import android.util.Xml;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

class BluetoothPbapVcardListing {
    private static final String TAG = "BluetoothPbapVcardListing";
    ArrayList<BluetoothPbapCard> mCards = new ArrayList();

    public BluetoothPbapVcardListing(InputStream in) throws IOException {
        parse(in);
    }

    private void parse(InputStream in) throws IOException {
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(in, "UTF-8");
            for (int eventType = parser.getEventType(); eventType != 1; eventType = parser.next()) {
                if (eventType == 2 && parser.getName().equals("card")) {
                    this.mCards.add(new BluetoothPbapCard(parser.getAttributeValue(null, "handle"), parser.getAttributeValue(null, "name")));
                }
            }
        } catch (XmlPullParserException e) {
            Log.e(TAG, "XML parser error when parsing XML", e);
        }
    }

    public ArrayList<BluetoothPbapCard> getList() {
        return this.mCards;
    }
}
