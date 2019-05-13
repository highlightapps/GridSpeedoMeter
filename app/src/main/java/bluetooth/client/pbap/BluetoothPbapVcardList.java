package bluetooth.client.pbap;

import com.android.vcard.VCardEntry;
import com.android.vcard.VCardEntryConstructor;
import com.android.vcard.VCardEntryCounter;
import com.android.vcard.VCardEntryHandler;
import com.android.vcard.VCardParser;
import com.android.vcard.VCardParser_V21;
import com.android.vcard.VCardParser_V30;
import com.android.vcard.exception.VCardException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

class BluetoothPbapVcardList {
    private final ArrayList<VCardEntry> mCards = new ArrayList();

    class CardEntryHandler implements VCardEntryHandler {
        CardEntryHandler() {
        }

        public void onStart() {
        }

        public void onEntryCreated(VCardEntry entry) {
            BluetoothPbapVcardList.this.mCards.add(entry);
        }

        public void onEnd() {
        }
    }

    public BluetoothPbapVcardList(InputStream in, byte format) throws IOException {
        parse(in, format);
    }

    private void parse(InputStream in, byte format) throws IOException {
        VCardParser parser;
        if (format == (byte) 1) {
            parser = new VCardParser_V30();
        } else {
            parser = new VCardParser_V21();
        }
        VCardEntryConstructor constructor = new VCardEntryConstructor();
        VCardEntryCounter counter = new VCardEntryCounter();
        constructor.addEntryHandler(new CardEntryHandler());
        parser.addInterpreter(constructor);
        parser.addInterpreter(counter);
        try {
            parser.parse(in);
        } catch (VCardException e) {
            e.printStackTrace();
        }
    }

    public int getCount() {
        return this.mCards.size();
    }

    public ArrayList<VCardEntry> getList() {
        return this.mCards;
    }

    public VCardEntry getFirst() {
        return (VCardEntry) this.mCards.get(0);
    }
}
