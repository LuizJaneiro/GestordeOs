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

public class ClientFragment extends Fragment {

    @BindView(R.id.text_view_os_nome)
    TextView textViewOsNome;
    @BindView(R.id.os_layout_name)
    ViewGroup osLayoutName;
    @BindView(R.id.text_view_os_telefone)
    TextView textViewOsTelefone;
    @BindView(R.id.os_layout_telefone)
    ViewGroup osLayoutTelefone;
    @BindView(R.id.text_view_os_contato)
    TextView textViewOsContato;
    @BindView(R.id.os_layout_contato)
    ViewGroup osLayoutContato;
    @BindView(R.id.text_view_os_tp_logradouro)
    TextView textViewOsTpLogradouro;
    @BindView(R.id.os_layout_tp_logradouro)
    ViewGroup osLayoutTpLogradouro;
    @BindView(R.id.text_view_os_logradouro)
    TextView textViewOsLogradouro;
    @BindView(R.id.os_layout_logradouro)
    ViewGroup osLayoutLogradouro;
    @BindView(R.id.text_view_os_numero)
    TextView textViewOsNumero;
    @BindView(R.id.os_layout_numero)
    ViewGroup osLayoutNumero;
    @BindView(R.id.text_view_os_complemento)
    TextView textViewOsComplemento;
    @BindView(R.id.os_layout_complemento)
    ViewGroup osLayoutComplemento;
    @BindView(R.id.text_view_os_andar)
    TextView textViewOsAndar;
    @BindView(R.id.os_layout_andar)
    ViewGroup osLayoutAndar;
    @BindView(R.id.text_view_os_bairro)
    TextView textViewOsBairro;
    @BindView(R.id.os_layout_bairro)
    ViewGroup osLayoutBairro;
    @BindView(R.id.text_view_os_cidade)
    TextView textViewOsCidade;
    @BindView(R.id.os_layout_cidade)
    ViewGroup osLayoutCidade;
    @BindView(R.id.text_view_os_uf)
    TextView textViewOsUf;
    @BindView(R.id.os_layout_uf)
    ViewGroup osLayoutUf;
    @BindView(R.id.text_view_os_cep)
    TextView textViewOsCep;
    @BindView(R.id.os_layout_cep)
    ViewGroup osLayoutCep;
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
        
        if(os.getCliente() == null)
            osLayoutName.setVisibility(View.GONE);
        else
            textViewOsNome.setText(os.getCliente()+"");

        if(os.getTelefoneCliente() == null)
            osLayoutName.setVisibility(View.GONE);
        else
            textViewOsTelefone.setText(os.getTelefoneCliente()+"");

        if(os.getContato() == null)
            osLayoutContato.setVisibility(View.GONE);
        else
            textViewOsContato.setText(os.getContato()+"");

        if(os.getTpLogradouro() == null)
            osLayoutTpLogradouro.setVisibility(View.GONE);
        else
            textViewOsTpLogradouro.setText(os.getTpLogradouro()+"");

        if(os.getLogradouro() == null)
            osLayoutLogradouro.setVisibility(View.GONE);
        else
            textViewOsLogradouro.setText(os.getLogradouro()+"");

        if(os.getNumero() == null)
            osLayoutNumero.setVisibility(View.GONE);
        else
            textViewOsNumero.setText(os.getNumero()+"");

        if(os.getAndar() == null)
            osLayoutAndar.setVisibility(View.GONE);
        else
            textViewOsAndar.setText(os.getAndar()+"");

        if(os.getBairro() == null)
            osLayoutBairro.setVisibility(View.GONE);
        else
            textViewOsBairro.setText(os.getBairro()+"");

        if(os.getCidade() == null)
            osLayoutCidade.setVisibility(View.GONE);
        else
            textViewOsCidade.setText(os.getCidade()+"");

        if(os.getUf() == null)
            osLayoutUf.setVisibility(View.GONE);
        else
            textViewOsUf.setText(os.getUf()+"");

        if(os.getCep() == null)
            osLayoutCep.setVisibility(View.GONE);
        else
            textViewOsCep.setText(os.getCep()+"");

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
