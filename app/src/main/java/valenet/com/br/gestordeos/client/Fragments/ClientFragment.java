package valenet.com.br.gestordeos.client.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.utils.ValenetUtils;

public class ClientFragment extends Fragment {

    @BindView(R.id.text_view_os_client_name)
    TextView textViewOsClientName;
    @BindView(R.id.os_layout_client_name)
    RelativeLayout osLayoutClientName;
    @BindView(R.id.text_view_os_client_phone)
    TextView textViewOsClientPhone;
    @BindView(R.id.os_layout_client_phone)
    RelativeLayout osLayoutClientPhone;
    @BindView(R.id.text_view_os_address)
    TextView textViewOsAddress;
    @BindView(R.id.os_layout_address)
    RelativeLayout osLayoutAddress;

    Unbinder unbinder;

    private Os os;

    public ClientFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        os = getArguments().getParcelable(ValenetUtils.KEY_OS);

        View view = inflater.inflate(R.layout.fragment_client, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (os.getCliente() == null)
            osLayoutClientName.setVisibility(View.GONE);
        else
            textViewOsClientName.setText(os.getCliente());

        if (os.getTelefoneCliente() == null)
            osLayoutClientPhone.setVisibility(View.GONE);
        else
            textViewOsClientPhone.setText(os.getTelefoneCliente());

        String address = ValenetUtils.buildOsAddress(os.getTpLogradouro(), os.getLogradouro(), os.getComplemento(), os.getNumero(), os.getAndar(), os.getBairro());

        textViewOsAddress.setText(address);


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
