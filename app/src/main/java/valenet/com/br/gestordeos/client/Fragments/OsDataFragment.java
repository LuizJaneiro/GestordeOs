package valenet.com.br.gestordeos.client.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import valenet.com.br.gestordeos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OsDataFragment extends Fragment {


    public OsDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_os_data, container, false);
    }

}
