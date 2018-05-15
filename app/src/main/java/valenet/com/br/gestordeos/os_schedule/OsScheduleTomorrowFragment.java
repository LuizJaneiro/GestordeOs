package valenet.com.br.gestordeos.os_schedule;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.main.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class OsScheduleTomorrowFragment extends Fragment implements MainActivity.navigateInterface {


    public OsScheduleTomorrowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_os_schedule_tomorrow, container, false);
    }

    public void setOsListNavigation(){
        ((MainActivity)this.getActivity()).setNavigateInterface(this);
    }

    @Override
    public void navigateToOsSearch() {

    }
}
