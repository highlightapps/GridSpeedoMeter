package bluetooth.client.pbap;

import bluetooth.client.pbap.utils.ObexAppParameters;
import android.util.Log;
import javax.obex.HeaderSet;

class BluetoothPbapRequestPullVcardListingSize extends BluetoothPbapRequest {
    private static final String TAG = "BluetoothPbapRequestPullVcardListingSize";
    private static final String TYPE = "x-bt/vcard-listing";
    private int mSize;

    public BluetoothPbapRequestPullVcardListingSize(String folder) {
        this.mHeaderSet.setHeader(1, folder);
        this.mHeaderSet.setHeader(66, TYPE);
        ObexAppParameters oap = new ObexAppParameters();
        oap.add((byte) 4, (short) 0);
        oap.addToHeaderSet(this.mHeaderSet);
    }

    protected void readResponseHeaders(HeaderSet headerset) {
        Log.v(TAG, "readResponseHeaders");
        this.mSize = ObexAppParameters.fromHeaderSet(headerset).getShort((byte) 8);
    }

    public int getSize() {
        return this.mSize;
    }
}
