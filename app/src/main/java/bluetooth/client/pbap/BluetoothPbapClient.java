package bluetooth.client.pbap;

import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.lang.ref.WeakReference;

public class BluetoothPbapClient {
    public static final String CCH_PATH = "telecom/cch.vcf";
    public static final int EVENT_PULL_PHONE_BOOK_DONE = 2;
    public static final int EVENT_PULL_PHONE_BOOK_ERROR = 102;
    public static final int EVENT_PULL_PHONE_BOOK_SIZE_DONE = 5;
    public static final int EVENT_PULL_PHONE_BOOK_SIZE_ERROR = 105;
    public static final int EVENT_PULL_VCARD_ENTRY_DONE = 4;
    public static final int EVENT_PULL_VCARD_ENTRY_ERROR = 104;
    public static final int EVENT_PULL_VCARD_LISTING_DONE = 3;
    public static final int EVENT_PULL_VCARD_LISTING_ERROR = 103;
    public static final int EVENT_PULL_VCARD_LISTING_SIZE_DONE = 6;
    public static final int EVENT_PULL_VCARD_LISTING_SIZE_ERROR = 106;
    public static final int EVENT_SESSION_AUTH_REQUESTED = 203;
    public static final int EVENT_SESSION_AUTH_TIMEOUT = 204;
    public static final int EVENT_SESSION_CONNECTED = 201;
    public static final int EVENT_SESSION_DISCONNECTED = 202;
    public static final int EVENT_SET_PHONE_BOOK_DONE = 1;
    public static final int EVENT_SET_PHONE_BOOK_ERROR = 101;
    public static final String ICH_PATH = "telecom/ich.vcf";
    public static final short MAX_LIST_COUNT = (short) -1;
    public static final String MCH_PATH = "telecom/mch.vcf";
    public static final String OCH_PATH = "telecom/och.vcf";
    public static final byte ORDER_BY_ALPHABETICAL = (byte) 1;
    public static final byte ORDER_BY_DEFAULT = (byte) -1;
    public static final byte ORDER_BY_INDEXED = (byte) 0;
    public static final byte ORDER_BY_PHONETIC = (byte) 2;
    public static final String PB_PATH = "telecom/pb.vcf";
    public static final byte SEARCH_ATTR_NAME = (byte) 0;
    public static final byte SEARCH_ATTR_NUMBER = (byte) 1;
    public static final byte SEARCH_ATTR_SOUND = (byte) 2;
    public static final String SIM_CCH_PATH = "SIM1/telecom/cch.vcf";
    public static final String SIM_ICH_PATH = "SIM1/telecom/ich.vcf";
    public static final String SIM_MCH_PATH = "SIM1/telecom/mch.vcf";
    public static final String SIM_OCH_PATH = "SIM1/telecom/och.vcf";
    public static final String SIM_PB_PATH = "SIM1/telecom/pb.vcf";
    private static final String TAG = "BluetoothPbapClient";
    public static final long VCARD_ATTR_ADDR = 32;
    public static final long VCARD_ATTR_AGENT = 32768;
    public static final long VCARD_ATTR_BDAY = 16;
    public static final long VCARD_ATTR_CATEGORIES = 16777216;
    public static final long VCARD_ATTR_CLASS = 67108864;
    public static final long VCARD_ATTR_EMAIL = 256;
    public static final long VCARD_ATTR_FN = 2;
    public static final long VCARD_ATTR_GEO = 2048;
    public static final long VCARD_ATTR_KEY = 4194304;
    public static final long VCARD_ATTR_LABEL = 64;
    public static final long VCARD_ATTR_LOGO = 16384;
    public static final long VCARD_ATTR_MAILER = 512;
    public static final long VCARD_ATTR_N = 4;
    public static final long VCARD_ATTR_NICKNAME = 8388608;
    public static final long VCARD_ATTR_NOTE = 131072;
    public static final long VCARD_ATTR_ORG = 65536;
    public static final long VCARD_ATTR_PHOTO = 8;
    public static final long VCARD_ATTR_PROID = 33554432;
    public static final long VCARD_ATTR_REV = 262144;
    public static final long VCARD_ATTR_ROLE = 8192;
    public static final long VCARD_ATTR_SORT_STRING = 134217728;
    public static final long VCARD_ATTR_SOUND = 524288;
    public static final long VCARD_ATTR_TEL = 128;
    public static final long VCARD_ATTR_TITLE = 4096;
    public static final long VCARD_ATTR_TZ = 1024;
    public static final long VCARD_ATTR_UID = 2097152;
    public static final long VCARD_ATTR_URL = 1048576;
    public static final long VCARD_ATTR_VERSION = 1;
    public static final long VCARD_ATTR_X_IRMC_CALL_DATETIME = 268435456;
    public static final byte VCARD_TYPE_21 = (byte) 0;
    public static final byte VCARD_TYPE_30 = (byte) 1;
    private final Handler mClientHandler;
    private ConnectionState mConnectionState = ConnectionState.DISCONNECTED;
    private final BluetoothPbapSession mSession;
    private SessionHandler mSessionHandler;

    public enum ConnectionState {
        DISCONNECTED,
        CONNECTING,
        CONNECTED,
        DISCONNECTING
    }

    private static class SessionHandler extends Handler {
        private final WeakReference<BluetoothPbapClient> mClient;

        SessionHandler(BluetoothPbapClient client) {
            this.mClient = new WeakReference(client);
        }

        public void handleMessage(Message msg) {
            Log.d(BluetoothPbapClient.TAG, "handleMessage: what=" + msg.what);
            BluetoothPbapClient client = (BluetoothPbapClient) this.mClient.get();
            if (client != null) {
                BluetoothPbapRequest req;
                switch (msg.what) {
                    case 3:
                        req = msg.obj;
                        if (req instanceof BluetoothPbapRequestPullPhoneBookSize) {
                            client.sendToClient(5, ((BluetoothPbapRequestPullPhoneBookSize) req).getSize());
                            return;
                        } else if (req instanceof BluetoothPbapRequestPullVcardListingSize) {
                            client.sendToClient(6, ((BluetoothPbapRequestPullVcardListingSize) req).getSize());
                            return;
                        } else if (req instanceof BluetoothPbapRequestPullPhoneBook) {
                            BluetoothPbapRequestPullPhoneBook r = (BluetoothPbapRequestPullPhoneBook) req;
                            client.sendToClient(2, r.getNewMissedCalls(), r.getList());
                            return;
                        } else if (req instanceof BluetoothPbapRequestPullVcardListing) {
                            BluetoothPbapRequestPullVcardListing r2 = (BluetoothPbapRequestPullVcardListing) req;
                            client.sendToClient(3, r2.getNewMissedCalls(), r2.getList());
                            return;
                        } else if (req instanceof BluetoothPbapRequestPullVcardEntry) {
                            client.sendToClient(4, (Object) ((BluetoothPbapRequestPullVcardEntry) req).getVcard());
                            return;
                        } else if (req instanceof BluetoothPbapRequestSetPath) {
                            client.sendToClient(1);
                            return;
                        } else {
                            return;
                        }
                    case 4:
                        req = (BluetoothPbapRequest) msg.obj;
                        if (req instanceof BluetoothPbapRequestPullPhoneBookSize) {
                            client.sendToClient(105);
                            return;
                        } else if (req instanceof BluetoothPbapRequestPullVcardListingSize) {
                            client.sendToClient(106);
                            return;
                        } else if (req instanceof BluetoothPbapRequestPullPhoneBook) {
                            client.sendToClient(102);
                            return;
                        } else if (req instanceof BluetoothPbapRequestPullVcardListing) {
                            client.sendToClient(103);
                            return;
                        } else if (req instanceof BluetoothPbapRequestPullVcardEntry) {
                            client.sendToClient(104);
                            return;
                        } else if (req instanceof BluetoothPbapRequestSetPath) {
                            client.sendToClient(101);
                            return;
                        } else {
                            return;
                        }
                    case 5:
                        client.mConnectionState = ConnectionState.CONNECTING;
                        return;
                    case 6:
                        client.mConnectionState = ConnectionState.CONNECTED;
                        client.sendToClient(201);
                        return;
                    case 7:
                        client.mConnectionState = ConnectionState.DISCONNECTED;
                        client.sendToClient(202);
                        return;
                    case 8:
                        client.sendToClient(203);
                        return;
                    case 9:
                        client.sendToClient(204);
                        return;
                    default:
                        return;
                }
            }
        }
    }

    private void sendToClient(int eventId) {
        sendToClient(eventId, 0, null);
    }

    private void sendToClient(int eventId, int param) {
        sendToClient(eventId, param, null);
    }

    private void sendToClient(int eventId, Object param) {
        sendToClient(eventId, 0, param);
    }

    private void sendToClient(int eventId, int param1, Object param2) {
        this.mClientHandler.obtainMessage(eventId, param1, 0, param2).sendToTarget();
    }

    public BluetoothPbapClient(BluetoothDevice device, Handler handler) {
        if (device == null) {
            throw new NullPointerException("BluetothDevice is null");
        }
        this.mClientHandler = handler;
        this.mSessionHandler = new SessionHandler(this);
        this.mSession = new BluetoothPbapSession(device, this.mSessionHandler);
    }

    public void connect() {
        this.mSession.start();
    }

    public void finalize() {
        if (this.mSession != null) {
            this.mSession.stop();
        }
    }

    public void disconnect() {
        this.mSession.stop();
    }

    public void abort() {
        this.mSession.abort();
    }

    public ConnectionState getState() {
        return this.mConnectionState;
    }

    public boolean setPhoneBookFolderRoot() {
        return this.mSession.makeRequest(new BluetoothPbapRequestSetPath(false));
    }

    public boolean setPhoneBookFolderUp() {
        return this.mSession.makeRequest(new BluetoothPbapRequestSetPath(true));
    }

    public boolean setPhoneBookFolderDown(String folder) {
        return this.mSession.makeRequest(new BluetoothPbapRequestSetPath(folder));
    }

    public boolean pullPhoneBookSize(String pbName) {
        return this.mSession.makeRequest(new BluetoothPbapRequestPullPhoneBookSize(pbName));
    }

    public boolean pullVcardListingSize(String folder) {
        return this.mSession.makeRequest(new BluetoothPbapRequestPullVcardListingSize(folder));
    }

    public boolean pullPhoneBook(String pbName) {
        return pullPhoneBook(pbName, 0, (byte) 0, 0, 0);
    }

    public boolean pullPhoneBook(String pbName, long filter, byte format) {
        return pullPhoneBook(pbName, filter, format, 0, 0);
    }

    public boolean pullPhoneBook(String pbName, int maxListCount, int listStartOffset) {
        return pullPhoneBook(pbName, 0, (byte) 0, maxListCount, listStartOffset);
    }

    public boolean pullPhoneBook(String pbName, long filter, byte format, int maxListCount, int listStartOffset) {
        return this.mSession.makeRequest(new BluetoothPbapRequestPullPhoneBook(pbName, filter, format, maxListCount, listStartOffset));
    }

    public boolean pullVcardListing(String folder) {
        return pullVcardListing(folder, (byte) -1, (byte) 0, null, 0, 0);
    }

    public boolean pullVcardListing(String folder, byte order) {
        return pullVcardListing(folder, order, (byte) 0, null, 0, 0);
    }

    public boolean pullVcardListing(String folder, byte searchAttr, String searchVal) {
        return pullVcardListing(folder, (byte) -1, searchAttr, searchVal, 0, 0);
    }

    public boolean pullVcardListing(String folder, byte order, int maxListCount, int listStartOffset) {
        return pullVcardListing(folder, order, (byte) 0, null, maxListCount, listStartOffset);
    }

    public boolean pullVcardListing(String folder, int maxListCount, int listStartOffset) {
        return pullVcardListing(folder, (byte) -1, (byte) 0, null, maxListCount, listStartOffset);
    }

    public boolean pullVcardListing(String folder, byte order, byte searchAttr, String searchVal, int maxListCount, int listStartOffset) {
        return this.mSession.makeRequest(new BluetoothPbapRequestPullVcardListing(folder, order, searchAttr, searchVal, maxListCount, listStartOffset));
    }

    public boolean pullVcardEntry(String handle) {
        return pullVcardEntry(handle, 0, (byte) 0);
    }

    public boolean pullVcardEntry(String handle, long filter, byte format) {
        return this.mSession.makeRequest(new BluetoothPbapRequestPullVcardEntry(handle, filter, format));
    }

    public boolean setAuthResponse(String key) {
        Log.d(TAG, " setAuthResponse key=" + key);
        return this.mSession.setAuthResponse(key);
    }
}
