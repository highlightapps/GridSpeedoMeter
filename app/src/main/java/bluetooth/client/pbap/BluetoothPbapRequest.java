package bluetooth.client.pbap;

import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import javax.obex.ClientOperation;
import javax.obex.ClientSession;
import javax.obex.HeaderSet;
import javax.obex.ResponseCodes;

abstract class BluetoothPbapRequest {
    protected static final byte OAP_TAGID_FILTER = (byte) 6;
    protected static final byte OAP_TAGID_FORMAT = (byte) 7;
    protected static final byte OAP_TAGID_LIST_START_OFFSET = (byte) 5;
    protected static final byte OAP_TAGID_MAX_LIST_COUNT = (byte) 4;
    protected static final byte OAP_TAGID_NEW_MISSED_CALLS = (byte) 9;
    protected static final byte OAP_TAGID_ORDER = (byte) 1;
    protected static final byte OAP_TAGID_PHONEBOOK_SIZE = (byte) 8;
    protected static final byte OAP_TAGID_SEARCH_ATTRIBUTE = (byte) 3;
    protected static final byte OAP_TAGID_SEARCH_VALUE = (byte) 2;
    private static final String TAG = "BluetoothPbapRequest";
    private boolean mAborted = false;
    protected HeaderSet mHeaderSet = new HeaderSet();
    private ClientOperation mOp = null;
    protected int mResponseCode;

    public final boolean isSuccess() {
        return this.mResponseCode == ResponseCodes.OBEX_HTTP_OK;
    }

    public void execute(ClientSession session) throws IOException {
        Log.v(TAG, "execute");
        if (this.mAborted) {
            this.mResponseCode = ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;
            return;
        }
        try {
            this.mOp = (ClientOperation) session.get(this.mHeaderSet);
            this.mOp.setGetFinalFlag(true);
            this.mOp.continueOperation(true, false);
            readResponseHeaders(this.mOp.getReceivedHeader());
            InputStream is = this.mOp.openInputStream();
            readResponse(is);
            is.close();
            this.mOp.close();
            this.mResponseCode = this.mOp.getResponseCode();
            Log.d(TAG, "mResponseCode=" + this.mResponseCode);
            checkResponseCode(this.mResponseCode);
        } catch (IOException e) {
            Log.e(TAG, "IOException occured when processing request", e);
            this.mResponseCode = ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;
            throw e;
        }
    }

    public void abort() {
        this.mAborted = true;
        if (this.mOp != null) {
            try {
                this.mOp.abort();
            } catch (IOException e) {
                Log.e(TAG, "Exception occured when trying to abort", e);
            }
        }
    }

    protected void readResponse(InputStream stream) throws IOException {
        Log.v(TAG, "readResponse");
    }

    protected void readResponseHeaders(HeaderSet headerset) {
        Log.v(TAG, "readResponseHeaders");
    }

    protected void checkResponseCode(int responseCode) throws IOException {
        Log.v(TAG, "checkResponseCode");
    }
}
