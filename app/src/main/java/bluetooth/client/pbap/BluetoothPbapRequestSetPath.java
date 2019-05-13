package bluetooth.client.pbap;

import android.util.Log;
import java.io.IOException;
import javax.obex.ClientSession;
import javax.obex.HeaderSet;
import javax.obex.ResponseCodes;

final class BluetoothPbapRequestSetPath extends BluetoothPbapRequest {
    /* renamed from: $SWITCH_TABLE$android$bluetooth$client$pbap$BluetoothPbapRequestSetPath$SetPathDir */
    private static /* synthetic */ int[] f59x6dabe155 = null;
    private static final String TAG = "BluetoothPbapRequestSetPath";
    private SetPathDir mDir;

    private enum SetPathDir {
        ROOT,
        UP,
        DOWN
    }

    /* renamed from: $SWITCH_TABLE$android$bluetooth$client$pbap$BluetoothPbapRequestSetPath$SetPathDir */
    static /* synthetic */ int[] m2343x6dabe155() {
        int[] iArr = f59x6dabe155;
        if (iArr == null) {
            iArr = new int[SetPathDir.values().length];
            try {
                iArr[SetPathDir.DOWN.ordinal()] = 3;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[SetPathDir.ROOT.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[SetPathDir.UP.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            f59x6dabe155 = iArr;
        }
        return iArr;
    }

    public BluetoothPbapRequestSetPath(String name) {
        this.mDir = SetPathDir.DOWN;
        this.mHeaderSet.setHeader(1, name);
    }

    public BluetoothPbapRequestSetPath(boolean goUp) {
        this.mHeaderSet.setEmptyNameHeader();
        if (goUp) {
            this.mDir = SetPathDir.UP;
        } else {
            this.mDir = SetPathDir.ROOT;
        }
    }

    public void execute(ClientSession session) {
        Log.v(TAG, "execute");
        HeaderSet hs = null;
        try {
            switch (m2343x6dabe155()[this.mDir.ordinal()]) {
                case 1:
                case 3:
                    hs = session.setPath(this.mHeaderSet, false, false);
                    break;
                case 2:
                    hs = session.setPath(this.mHeaderSet, true, false);
                    break;
            }
            this.mResponseCode = hs.getResponseCode();
        } catch (IOException e) {
            this.mResponseCode = ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;
        }
    }
}
