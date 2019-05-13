package bluetooth.client.pbap;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import java.io.IOException;
import java.util.UUID;

class BluetoothPbapSession implements Callback {
    public static final int ACTION_LISTING = 14;
    public static final int ACTION_PHONEBOOK_SIZE = 16;
    public static final int ACTION_VCARD = 15;
    public static final int AUTH_REQUESTED = 8;
    public static final int AUTH_TIMEOUT = 9;
    private static final String PBAP_UUID = "0000112f-0000-1000-8000-00805f9b34fb";
    public static final int REQUEST_COMPLETED = 3;
    public static final int REQUEST_FAILED = 4;
    private static final int RFCOMM_CONNECTED = 1;
    private static final int RFCOMM_FAILED = 2;
    public static final int SESSION_CONNECTED = 6;
    public static final int SESSION_CONNECTING = 5;
    public static final int SESSION_DISCONNECTED = 7;
    private static final String TAG = "android.bluetooth.client.pbap.BluetoothPbapSession";
    private final BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
    private RfcommConnectThread mConnectThread;
    private final BluetoothDevice mDevice;
    private final HandlerThread mHandlerThread;
    private BluetoothPbapObexSession mObexSession;
    private final Handler mParentHandler;
    private BluetoothPbapRequest mPendingRequest = null;
    private final Handler mSessionHandler;
    private BluetoothPbapObexTransport mTransport;

    private class RfcommConnectThread extends Thread {
        private static final String TAG = "RfcommConnectThread";
        private BluetoothSocket mSocket;

        public RfcommConnectThread() {
            super(TAG);
        }

        public void run() {
            if (BluetoothPbapSession.this.mAdapter.isDiscovering()) {
                BluetoothPbapSession.this.mAdapter.cancelDiscovery();
            }
            try {
                this.mSocket = BluetoothPbapSession.this.mDevice.createRfcommSocketToServiceRecord(UUID.fromString(BluetoothPbapSession.PBAP_UUID));
                this.mSocket.connect();
                BluetoothPbapSession.this.mSessionHandler.obtainMessage(1, new BluetoothPbapObexTransport(this.mSocket)).sendToTarget();
            } catch (IOException e) {
                closeSocket();
                BluetoothPbapSession.this.mSessionHandler.obtainMessage(2).sendToTarget();
            }
        }

        private void closeSocket() {
            try {
                if (this.mSocket != null) {
                    this.mSocket.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error when closing socket", e);
            }
        }
    }

    public BluetoothPbapSession(BluetoothDevice device, Handler handler) {
        if (this.mAdapter == null) {
            throw new NullPointerException("No Bluetooth adapter in the system");
        }
        this.mDevice = device;
        this.mParentHandler = handler;
        this.mConnectThread = null;
        this.mTransport = null;
        this.mObexSession = null;
        this.mHandlerThread = new HandlerThread("PBAP session handler", 10);
        this.mHandlerThread.start();
        this.mSessionHandler = new Handler(this.mHandlerThread.getLooper(), this);
    }

    public boolean handleMessage(Message msg) {
        Log.d(TAG, "Handler: msg: " + msg.what);
        switch (msg.what) {
            case 1:
                this.mConnectThread = null;
                this.mTransport = (BluetoothPbapObexTransport) msg.obj;
                startObexSession();
                break;
            case 2:
                this.mConnectThread = null;
                this.mParentHandler.obtainMessage(7).sendToTarget();
                if (this.mPendingRequest != null) {
                    this.mParentHandler.obtainMessage(4, this.mPendingRequest).sendToTarget();
                    this.mPendingRequest = null;
                    break;
                }
                break;
            case 100:
                this.mParentHandler.obtainMessage(6).sendToTarget();
                if (this.mPendingRequest != null) {
                    this.mObexSession.schedule(this.mPendingRequest);
                    this.mPendingRequest = null;
                    break;
                }
                break;
            case 101:
                stopObexSession();
                this.mParentHandler.obtainMessage(7).sendToTarget();
                if (this.mPendingRequest != null) {
                    this.mParentHandler.obtainMessage(4, this.mPendingRequest).sendToTarget();
                    this.mPendingRequest = null;
                    break;
                }
                break;
            case 102:
                this.mParentHandler.obtainMessage(7).sendToTarget();
                stopRfcomm();
                break;
            case 103:
                this.mParentHandler.obtainMessage(3, msg.obj).sendToTarget();
                break;
            case 104:
                this.mParentHandler.obtainMessage(4, msg.obj).sendToTarget();
                break;
            case 105:
                this.mParentHandler.obtainMessage(8).sendToTarget();
                this.mSessionHandler.sendMessageDelayed(this.mSessionHandler.obtainMessage(106), 30000);
                break;
            case 106:
                setAuthResponse(null);
                this.mParentHandler.obtainMessage(9).sendToTarget();
                break;
            default:
                return false;
        }
        return true;
    }

    public void start() {
        Log.d(TAG, "start");
        startRfcomm();
    }

    public void stop() {
        Log.d(TAG, "Stop");
        stopObexSession();
        stopRfcomm();
    }

    public void abort() {
        Log.d(TAG, "abort");
        if (this.mPendingRequest != null) {
            this.mParentHandler.obtainMessage(4, this.mPendingRequest).sendToTarget();
            this.mPendingRequest = null;
        }
        if (this.mObexSession != null) {
            this.mObexSession.abort();
        }
    }

    public boolean makeRequest(BluetoothPbapRequest request) {
        Log.v(TAG, "makeRequest: " + request.getClass().getSimpleName());
        if (this.mPendingRequest != null) {
            Log.w(TAG, "makeRequest: request already queued, exiting");
            return false;
        } else if (this.mObexSession != null) {
            return this.mObexSession.schedule(request);
        } else {
            this.mPendingRequest = request;
            startRfcomm();
            return true;
        }
    }

    public boolean setAuthResponse(String key) {
        Log.d(TAG, "setAuthResponse key=" + key);
        this.mSessionHandler.removeMessages(106);
        if (this.mObexSession == null) {
            return false;
        }
        return this.mObexSession.setAuthReply(key);
    }

    private void startRfcomm() {
        Log.d(TAG, "startRfcomm");
        if (this.mConnectThread == null && this.mObexSession == null) {
            this.mParentHandler.obtainMessage(5).sendToTarget();
            this.mConnectThread = new RfcommConnectThread();
            this.mConnectThread.start();
        }
    }

    private void stopRfcomm() {
        Log.d(TAG, "stopRfcomm");
        if (this.mConnectThread != null) {
            try {
                this.mConnectThread.join();
            } catch (InterruptedException e) {
            }
            this.mConnectThread = null;
        }
        if (this.mTransport != null) {
            try {
                this.mTransport.close();
            } catch (IOException e2) {
            }
            this.mTransport = null;
        }
    }

    private void startObexSession() {
        Log.d(TAG, "startObexSession");
        this.mObexSession = new BluetoothPbapObexSession(this.mTransport);
        this.mObexSession.start(this.mSessionHandler);
    }

    private void stopObexSession() {
        Log.d(TAG, "stopObexSession");
        if (this.mObexSession != null) {
            this.mObexSession.stop();
            this.mObexSession = null;
        }
    }
}
