package bluetooth.client.pbap.utils;

import android.util.Log;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class BmsgTokenizer {
    private final Matcher mMatcher;
    private final int mOffset;
    private int mPos;
    private final String mStr;

    public static class Property {
        public final String name;
        public final String value;

        public Property(String name, String value) {
            if (name == null || value == null) {
                throw new IllegalArgumentException();
            }
            this.name = name;
            this.value = value;
            Log.v("BMSG >> ", toString());
        }

        public String toString() {
            return this.name + ":" + this.value;
        }

        public boolean equals(Object o) {
            return (o instanceof Property) && ((Property) o).name.equals(this.name) && ((Property) o).value.equals(this.value);
        }
    }

    public BmsgTokenizer(String str) {
        this(str, 0);
    }

    public BmsgTokenizer(String str, int offset) {
        this.mPos = 0;
        this.mStr = str;
        this.mOffset = offset;
        this.mMatcher = Pattern.compile("(([^:]*):(.*))?\r\n").matcher(str);
        this.mPos = this.mMatcher.regionStart();
    }

    public Property next(boolean alwaysReturn) throws ParseException {
        boolean found = false;
        do {
            this.mMatcher.region(this.mPos, this.mMatcher.regionEnd());
            if (this.mMatcher.lookingAt()) {
                this.mPos = this.mMatcher.end();
                if (this.mMatcher.group(1) != null) {
                    found = true;
                    continue;
                }
            } else if (alwaysReturn) {
                return null;
            } else {
                throw new ParseException("Property or empty line expected", pos());
            }
        } while (!found);
        return new Property(this.mMatcher.group(2), this.mMatcher.group(3));
    }

    public Property next() throws ParseException {
        return next(false);
    }

    public String remaining() {
        return this.mStr.substring(this.mPos);
    }

    public int pos() {
        return this.mPos + this.mOffset;
    }
}
