package valenet.com.br.gestordeos.client;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import valenet.com.br.gestordeos.client.Fragments.ObservationFragment;
import valenet.com.br.gestordeos.client.Fragments.ClientFragment;
import valenet.com.br.gestordeos.client.Fragments.OsDataFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int numOfTabs;

    public PagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                OsDataFragment tab1 = new OsDataFragment();
                return tab1;
            case 1:
                ClientFragment tab2 = new ClientFragment();
                return tab2;
            case 2:
                ObservationFragment tab3 = new ObservationFragment();
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
