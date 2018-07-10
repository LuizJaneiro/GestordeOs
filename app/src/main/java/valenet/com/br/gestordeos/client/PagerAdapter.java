package valenet.com.br.gestordeos.client;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import valenet.com.br.gestordeos.client.Fragments.ObservationFragment;
import valenet.com.br.gestordeos.client.Fragments.ClientFragment;
import valenet.com.br.gestordeos.client.Fragments.OsDataFragment;
import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.utils.ValenetUtils;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int numOfTabs;
    private Os os;
    private boolean cameFromSchedule;

    public PagerAdapter(FragmentManager fm, Os os, boolean cameFromSchedule, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
        this.os = os;
        this.cameFromSchedule = cameFromSchedule;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ValenetUtils.KEY_OS, os);
        bundle.putBoolean(ValenetUtils.KEY_CAME_FROM_SCHEDULE, cameFromSchedule);
        switch (position) {
            case 0:
                OsDataFragment tab1 = new OsDataFragment();
                tab1.setArguments(bundle);
                return tab1;
            case 1:
                ClientFragment tab2 = new ClientFragment();
                tab2.setArguments(bundle);
                return tab2;
            case 2:
                ObservationFragment tab3 = new ObservationFragment();
                tab3.setArguments(bundle);
                return  tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this.numOfTabs;
    }
}
