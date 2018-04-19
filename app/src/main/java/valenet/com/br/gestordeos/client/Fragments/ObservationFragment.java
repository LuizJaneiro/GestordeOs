package valenet.com.br.gestordeos.client.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.utils.ValenetUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ObservationFragment extends Fragment {

    @BindView(R.id.text_view_os_observacao)
    TextView textViewOsObservacao;
    Unbinder unbinder;
    private Os os;

    public ObservationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        os = getArguments().getParcelable(ValenetUtils.KEY_OS);
        View view = inflater.inflate(R.layout.fragment_observation, container, false);
        unbinder = ButterKnife.bind(this, view);

        if(os.getOBSERVACAO() == null)
            textViewOsObservacao.setText("Não há observações para esta OS.");
        else
            textViewOsObservacao.setText(os.getOBSERVACAO()+"");

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.clear();
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
