package bluetooth.client.pbap;

import android.bluetooth.client.pbap.utils.ObexAppParameters;
import android.util.Log;
import com.android.vcard.VCardEntry;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.obex.HeaderSet;

final class BluetoothPbapRequestPullPhoneBook extends BluetoothPbapRequest {
    private static final String TAG = "BluetoothPbapRequestPullPhoneBook";
    private static final String TYPE = "x-bt/phonebook";
    private final byte mFormat;
    private int mNewMissedCalls = -1;
    private BluetoothPbapVcardList mResponse;

    public BluetoothPbapRequestPullPhoneBook(String pbName, long filter, byte format, int maxListCount, int listStartOffset) {
        if (maxListCount < 0 || maxListCount > 65535) {
            throw new IllegalArgumentException("maxListCount should be [0..65535]");
        } else if (listStartOffset < 0 || listStartOffset > 65535) {
            throw new IllegalArgumentException("listStartOffset should be [0..65535]");
        } else {
            this.mHeaderSet.setHeader(1, pbName);
            this.mHeaderSet.setHeader(66, TYPE);
            ObexAppParameters oap = new ObexAppParameters();
            if (!(format == (byte) 0 || format == (byte) 1)) {
                format = (byte) 0;
            }
            if (filter != 0) {
                oap.add((byte) 6, filter);
            }
            oap.add((byte) 7, format);
            if (maxListCount > 0) {
                oap.add((byte) 4, (short) maxListCount);
            } else {
                oap.add((byte) 4, (short) -1);
            }
            if (listStartOffset > 0) {
                oap.add((byte) 5, (short) listStartOffset);
            }
            oap.addToHeaderSet(this.mHeaderSet);
            this.mFormat = format;
        }
    }

    protected void readResponse(InputStream stream) throws IOException {
        Log.v(TAG, "readResponse");
        this.mResponse = new BluetoothPbapVcardList(stream, this.mFormat);
    }

    protected void readResponseHeaders(HeaderSet headerset) {
        Log.v(TAG, "readResponse");
        ObexAppParameters oap = ObexAppParameters.fromHeaderSet(headerset);
        if (oap.exists((byte) 9)) {
            this.mNewMissedCalls = oap.getByte((byte) 9);
        }
    }

    public ArrayList<VCardEntry> getList() {
        return this.mResponse.getList();
    }

    public int getNewMissedCalls() {
        return this.mNewMissedCalls;
    }
}
