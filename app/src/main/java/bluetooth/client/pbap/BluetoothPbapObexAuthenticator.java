package bluetooth.client.pbap;

import android.os.Handler;
import android.util.Log;
import javax.obex.Authenticator;
import javax.obex.PasswordAuthentication;

class BluetoothPbapObexAuthenticator implements Authenticator {
    private static final String TAG = "BluetoothPbapObexAuthenticator";
    private final Handler mCallback;
    private boolean mReplied;
    private String mSessionKey;

    public BluetoothPbapObexAuthenticator(Handler callback) {
        this.mCallback = callback;
    }

    public synchronized void setReply(String key) {
        Log.d(TAG, "setReply key=" + key);
        this.mSessionKey = key;
        this.mReplied = true;
        notify();
    }

    public PasswordAuthentication onAuthenticationChallenge(String description, boolean isUserIdRequired, boolean isFullAccess) {
        this.mReplied = false;
        Log.d(TAG, "onAuthenticationChallenge: sending request");
        this.mCallback.obtainMessage(105).sendToTarget();
        synchronized (this) {
            while (!this.mReplied) {
                try {
                    Log.v(TAG, "onAuthenticationChallenge: waiting for response");
                    wait();
                } catch (InterruptedException e) {
                    Log.e(TAG, "Interrupted while waiting for challenge response");
                }
            }
        }
        if (this.mSessionKey == null || this.mSessionKey.length() == 0) {
            Log.v(TAG, "onAuthenticationChallenge: mSessionKey is empty, timeout/cancel occured");
            return null;
        }
        Log.v(TAG, "onAuthenticationChallenge: mSessionKey=" + this.mSessionKey);
        return new PasswordAuthentication(null, this.mSessionKey.getBytes());
    }

    public byte[] onAuthenticationResponse(byte[] userName) {
        return null;
    }
}
