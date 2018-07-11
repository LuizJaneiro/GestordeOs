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
import valenet.com.br.gestordeos.model.entity.OrdemDeServico;
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

    private OrdemDeServico ordemDeServico;

    public ClientFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ordemDeServico = getArguments().getParcelable(ValenetUtils.KEY_OS);

        View view = inflater.inflate(R.layout.fragment_client, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (ordemDeServico.getCliente() == null)
            osLayoutClientName.setVisibility(View.GONE);
        else
            textViewOsClientName.setText(ordemDeServico.getCliente());

        if (ordemDeServico.getTelefoneCliente() == null)
            osLayoutClientPhone.setVisibility(View.GONE);
        else
            textViewOsClientPhone.setText(ordemDeServico.getTelefoneCliente());

        String address = ValenetUtils.buildOsAddress(ordemDeServico.getTpLogradouro(), ordemDeServico.getLogradouro(), ordemDeServico.getComplemento(), ordemDeServico.getNumero(), ordemDeServico.getAndar(), ordemDeServico.getBairro());

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
