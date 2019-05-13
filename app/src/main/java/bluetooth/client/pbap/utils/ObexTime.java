package bluetooth.client.pbap.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ObexTime {
    private Date mDate;

    public ObexTime(String time) {
        Matcher m = Pattern.compile("(\\d{4})(\\d{2})(\\d{2})T(\\d{2})(\\d{2})(\\d{2})(([+-])(\\d{2})(\\d{2}))?").matcher(time);
        if (m.matches()) {
            Calendar cal = Calendar.getInstance();
            cal.set(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)) - 1, Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5)), Integer.parseInt(m.group(6)));
            if (m.group(7) != null) {
                int ohh = Integer.parseInt(m.group(9));
                int offset = (((ohh * 60) + Integer.parseInt(m.group(10))) * 60) * 1000;
                if (m.group(8).equals("-")) {
                    offset = -offset;
                }
                TimeZone tz = TimeZone.getTimeZone("UTC");
                tz.setRawOffset(offset);
                cal.setTimeZone(tz);
            }
            this.mDate = cal.getTime();
        }
    }

    public ObexTime(Date date) {
        this.mDate = date;
    }

    public Date getTime() {
        return this.mDate;
    }

    public String toString() {
        if (this.mDate == null) {
            return null;
        }
        Calendar.getInstance().setTime(this.mDate);
        return String.format(Locale.US, "%04d%02d%02dT%02d%02d%02d", new Object[]{Integer.valueOf(cal.get(1)), Integer.valueOf(cal.get(2) + 1), Integer.valueOf(cal.get(5)), Integer.valueOf(cal.get(11)), Integer.valueOf(cal.get(12)), Integer.valueOf(cal.get(13))});
    }
}
