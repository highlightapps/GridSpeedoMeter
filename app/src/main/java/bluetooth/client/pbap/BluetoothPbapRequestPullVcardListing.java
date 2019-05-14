package bluetooth.client.pbap;

import bluetooth.client.pbap.utils.ObexAppParameters;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.obex.HeaderSet;

final class BluetoothPbapRequestPullVcardListing extends BluetoothPbapRequest {
    private static final String TAG = "BluetoothPbapRequestPullVcardListing";
    private static final String TYPE = "x-bt/vcard-listing";
    private int mNewMissedCalls = -1;
    private BluetoothPbapVcardListing mResponse = null;

    public BluetoothPbapRequestPullVcardListing(String folder, byte order, byte searchAttr, String searchVal, int maxListCount, int listStartOffset) {
        if (maxListCount < 0 || maxListCount > 65535) {
            throw new IllegalArgumentException("maxListCount should be [0..65535]");
        } else if (listStartOffset < 0 || listStartOffset > 65535) {
            throw new IllegalArgumentException("listStartOffset should be [0..65535]");
        } else {
            if (folder == null) {
                folder = "";
            }
            this.mHeaderSet.setHeader(1, folder);
            this.mHeaderSet.setHeader(66, TYPE);
            ObexAppParameters oap = new ObexAppParameters();
            if (order >= (byte) 0) {
                oap.add((byte) 1, order);
            }
            if (searchVal != null) {
                oap.add((byte) 3, searchAttr);
                oap.add((byte) 2, searchVal);
            }
            if (maxListCount > 0) {
                oap.add((byte) 4, (short) maxListCount);
            }
            if (listStartOffset > 0) {
                oap.add((byte) 5, (short) listStartOffset);
            }
            oap.addToHeaderSet(this.mHeaderSet);
        }
    }

    protected void readResponse(InputStream stream) throws IOException {
        Log.v(TAG, "readResponse");
        this.mResponse = new BluetoothPbapVcardListing(stream);
    }

    protected void readResponseHeaders(HeaderSet headerset) {
        Log.v(TAG, "readResponseHeaders");
        ObexAppParameters oap = ObexAppParameters.fromHeaderSet(headerset);
        if (oap.exists((byte) 9)) {
            this.mNewMissedCalls = oap.getByte((byte) 9);
        }
    }

    public ArrayList<BluetoothPbapCard> getList() {
        return this.mResponse.getList();
    }

    public int getNewMissedCalls() {
        return this.mNewMissedCalls;
    }
}
