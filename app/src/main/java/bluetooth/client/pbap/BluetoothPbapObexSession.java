package bluetooth.client.pbap;

import android.os.Handler;
import android.util.Log;
import java.io.IOException;
import javax.obex.ClientSession;
import javax.obex.HeaderSet;
import javax.obex.ObexTransport;
import javax.obex.ResponseCodes;

final class BluetoothPbapObexSession {
    static final int OBEX_SESSION_AUTHENTICATION_REQUEST = 105;
    static final int OBEX_SESSION_AUTHENTICATION_TIMEOUT = 106;
    static final int OBEX_SESSION_CONNECTED = 100;
    static final int OBEX_SESSION_DISCONNECTED = 102;
    static final int OBEX_SESSION_FAILED = 101;
    static final int OBEX_SESSION_REQUEST_COMPLETED = 103;
    static final int OBEX_SESSION_REQUEST_FAILED = 104;
    private static final byte[] PBAP_TARGET;
    private static final String TAG = "BluetoothPbapObexSession";
    private BluetoothPbapObexAuthenticator mAuth = null;
    private ObexClientThread mObexClientThread;
    private Handler mSessionHandler;
    private final ObexTransport mTransport;

    /* renamed from: android.bluetooth.client.pbap.BluetoothPbapObexSession$1 */
    class C00001 extends Thread {
        C00001() {
        }

        public void run() {
            BluetoothPbapObexSession.this.mObexClientThread.mRequest.abort();
        }
    }

    private class ObexClientThread extends Thread {
        private static final String TAG = "ObexClientThread";
        private ClientSession mClientSession = null;
        private BluetoothPbapRequest mRequest = null;
        private volatile boolean mRunning = true;

        public void run() {
            super.run();
            if (connect()) {
                BluetoothPbapObexSession.this.mSessionHandler.obtainMessage(100).sendToTarget();
                while (this.mRunning) {
                    synchronized (this) {
                        try {
                            if (this.mRequest == null) {
                                wait();
                            }
                        } catch (InterruptedException e) {
                            this.mRunning = false;
                        }
                    }
                    if (this.mRunning && this.mRequest != null) {
                        try {
                            this.mRequest.execute(this.mClientSession);
                        } catch (IOException e2) {
                            this.mRunning = false;
                        }
                        if (this.mRequest.isSuccess()) {
                            BluetoothPbapObexSession.this.mSessionHandler.obtainMessage(103, this.mRequest).sendToTarget();
                        } else {
                            BluetoothPbapObexSession.this.mSessionHandler.obtainMessage(104, this.mRequest).sendToTarget();
                        }
                    }
                    this.mRequest = null;
                }
                disconnect();
                BluetoothPbapObexSession.this.mSessionHandler.obtainMessage(102).sendToTarget();
                return;
            }
            BluetoothPbapObexSession.this.mSessionHandler.obtainMessage(101).sendToTarget();
        }

        public synchronized boolean schedule(BluetoothPbapRequest request) {
            boolean z;
            Log.d(TAG, "schedule: " + request.getClass().getSimpleName());
            if (this.mRequest != null) {
                z = false;
            } else {
                this.mRequest = request;
                notify();
                z = true;
            }
            return z;
        }

        private boolean connect() {
            Log.d(TAG, "connect");
            try {
                this.mClientSession = new ClientSession(BluetoothPbapObexSession.this.mTransport);
                this.mClientSession.setAuthenticator(BluetoothPbapObexSession.this.mAuth);
                HeaderSet hs = new HeaderSet();
                hs.setHeader(70, BluetoothPbapObexSession.PBAP_TARGET);
                try {
                    if (this.mClientSession.connect(hs).getResponseCode() == ResponseCodes.OBEX_HTTP_OK) {
                        return true;
                    }
                    disconnect();
                    return false;
                } catch (IOException e) {
                    return false;
                }
            } catch (IOException e2) {
                return false;
            }
        }

        private void disconnect() {
            Log.d(TAG, "disconnect");
            if (this.mClientSession != null) {
                try {
                    this.mClientSession.disconnect(null);
                    this.mClientSession.close();
                } catch (IOException e) {
                }
            }
        }
    }

    static {
        byte[] bArr = new byte[16];
        bArr[0] = (byte) 121;
        bArr[1] = (byte) 97;
        bArr[2] = (byte) 53;
        bArr[3] = (byte) -16;
        bArr[4] = (byte) -16;
        bArr[5] = (byte) -59;
        bArr[6] = (byte) 17;
        bArr[7] = (byte) -40;
        bArr[8] = (byte) 9;
        bArr[9] = (byte) 102;
        bArr[10] = (byte) 8;
        bArr[12] = (byte) 32;
        bArr[13] = (byte) 12;
        bArr[14] = (byte) -102;
        bArr[15] = (byte) 102;
        PBAP_TARGET = bArr;
    }

    public BluetoothPbapObexSession(ObexTransport transport) {
        this.mTransport = transport;
    }

    public void start(Handler handler) {
        Log.d(TAG, "start");
        this.mSessionHandler = handler;
        this.mAuth = new BluetoothPbapObexAuthenticator(this.mSessionHandler);
        this.mObexClientThread = new ObexClientThread();
        this.mObexClientThread.start();
    }

    public void stop() {
        Log.d(TAG, "stop");
        if (this.mObexClientThread != null) {
            try {
                this.mObexClientThread.interrupt();
                this.mObexClientThread.join();
                this.mObexClientThread = null;
            } catch (InterruptedException e) {
            }
        }
    }

    public void abort() {
        Log.d(TAG, "abort");
        if (this.mObexClientThread != null && this.mObexClientThread.mRequest != null) {
            new C00001().run();
        }
    }

    public boolean schedule(BluetoothPbapRequest request) {
        Log.d(TAG, "schedule: " + request.getClass().getSimpleName());
        if (this.mObexClientThread != null) {
            return this.mObexClientThread.schedule(request);
        }
        Log.e(TAG, "OBEX session not started");
        return false;
    }

    public boolean setAuthReply(String key) {
        Log.d(TAG, "setAuthReply key=" + key);
        if (this.mAuth == null) {
            return false;
        }
        this.mAuth.setReply(key);
        return true;
    }
}
