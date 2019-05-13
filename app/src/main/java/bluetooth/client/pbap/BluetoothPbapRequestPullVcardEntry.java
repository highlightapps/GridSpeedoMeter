package bluetooth.client.pbap;

import android.bluetooth.client.pbap.utils.ObexAppParameters;
import android.util.Log;
import com.android.vcard.VCardEntry;
import java.io.IOException;
import java.io.InputStream;
import javax.obex.ResponseCodes;

final class BluetoothPbapRequestPullVcardEntry extends BluetoothPbapRequest {
    private static final String TAG = "BluetoothPbapRequestPullVcardEntry";
    private static final String TYPE = "x-bt/vcard";
    private final byte mFormat;
    private BluetoothPbapVcardList mResponse;

    public BluetoothPbapRequestPullVcardEntry(String handle, long filter, byte format) {
        this.mHeaderSet.setHeader(1, handle);
        this.mHeaderSet.setHeader(66, TYPE);
        if (!(format == (byte) 0 || format == (byte) 1)) {
            format = (byte) 0;
        }
        ObexAppParameters oap = new ObexAppParameters();
        if (filter != 0) {
            oap.add((byte) 6, filter);
        }
        oap.add((byte) 7, format);
        oap.addToHeaderSet(this.mHeaderSet);
        this.mFormat = format;
    }

    protected void readResponse(InputStream stream) throws IOException {
        Log.v(TAG, "readResponse");
        this.mResponse = new BluetoothPbapVcardList(stream, this.mFormat);
    }

    protected void checkResponseCode(int responseCode) throws IOException {
        Log.v(TAG, "checkResponseCode");
        if (this.mResponse.getCount() != 0) {
            return;
        }
        if (responseCode == 196 || responseCode == ResponseCodes.OBEX_HTTP_NOT_ACCEPTABLE) {
            Log.v(TAG, "Vcard Entry not found");
            return;
        }
        throw new IOException("Invalid response received");
    }

    public VCardEntry getVcard() {
        return this.mResponse.getFirst();
    }
}
