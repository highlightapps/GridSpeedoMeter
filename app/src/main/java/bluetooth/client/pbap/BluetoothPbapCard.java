package bluetooth.client.pbap;

import com.android.vcard.VCardConstants;
import com.android.vcard.VCardEntry;
import com.android.vcard.VCardEntry.EmailData;
import com.android.vcard.VCardEntry.NameData;
import com.android.vcard.VCardEntry.PhoneData;
import com.anjlab.android.iab.v3.Constants;
import com.google.android.gms.plus.PlusShare;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BluetoothPbapCard {
    /* renamed from: N */
    public final String f0N;
    public final String firstName;
    public final String handle;
    public final String lastName;
    public final String middleName;
    public final String prefix;
    public final String suffix;

    public BluetoothPbapCard(String handle, String name) {
        String str = null;
        this.handle = handle;
        this.f0N = name;
        String[] parsedName = name.split(";", 5);
        this.lastName = parsedName.length < 1 ? null : parsedName[0];
        this.firstName = parsedName.length < 2 ? null : parsedName[1];
        this.middleName = parsedName.length < 3 ? null : parsedName[2];
        this.prefix = parsedName.length < 4 ? null : parsedName[3];
        if (parsedName.length >= 5) {
            str = parsedName[4];
        }
        this.suffix = str;
    }

    public String toString() {
        JSONObject json = new JSONObject();
        try {
            json.put("handle", this.handle);
            json.put(VCardConstants.PROPERTY_N, this.f0N);
            json.put("lastName", this.lastName);
            json.put("firstName", this.firstName);
            json.put("middleName", this.middleName);
            json.put("prefix", this.prefix);
            json.put("suffix", this.suffix);
        } catch (JSONException e) {
        }
        return json.toString();
    }

    public static String jsonifyVcardEntry(VCardEntry vcard) {
        JSONObject json = new JSONObject();
        try {
            NameData name = vcard.getNameData();
            json.put("formatted", name.getFormatted());
            json.put("family", name.getFamily());
            json.put("given", name.getGiven());
            json.put("middle", name.getMiddle());
            json.put("prefix", name.getPrefix());
            json.put("suffix", name.getSuffix());
        } catch (JSONException e) {
        }
        try {
            JSONArray jsonPhones = new JSONArray();
            List<PhoneData> phones = vcard.getPhoneList();
            if (phones != null) {
                for (PhoneData phone : phones) {
                    JSONObject jsonPhone = new JSONObject();
                    jsonPhone.put(Constants.RESPONSE_TYPE, phone.getType());
                    jsonPhone.put("number", phone.getNumber());
                    jsonPhone.put(PlusShare.KEY_CALL_TO_ACTION_LABEL, phone.getLabel());
                    jsonPhone.put("is_primary", phone.isPrimary());
                    jsonPhones.put(jsonPhone);
                }
                json.put("phones", jsonPhones);
            }
        } catch (JSONException e2) {
        }
        try {
            JSONArray jsonEmails = new JSONArray();
            List<EmailData> emails = vcard.getEmailList();
            if (emails != null) {
                for (EmailData email : emails) {
                    JSONObject jsonEmail = new JSONObject();
                    jsonEmail.put(Constants.RESPONSE_TYPE, email.getType());
                    jsonEmail.put("address", email.getAddress());
                    jsonEmail.put(PlusShare.KEY_CALL_TO_ACTION_LABEL, email.getLabel());
                    jsonEmail.put("is_primary", email.isPrimary());
                    jsonEmails.put(jsonEmail);
                }
                json.put("emails", jsonEmails);
            }
        } catch (JSONException e3) {
        }
        return json.toString();
    }
}
