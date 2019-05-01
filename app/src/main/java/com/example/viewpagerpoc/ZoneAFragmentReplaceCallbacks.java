package com.example.viewpagerpoc;

import android.os.Bundle;

public interface ZoneAFragmentReplaceCallbacks {
    void updateFragment(ZoneAFragmentsEnum fragment, Bundle bundle);
    void updateFragment(ZoneAFragmentsEnum fragment);
    void goBackToPreviousFragment();
}
