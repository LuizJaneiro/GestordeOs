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
public class OsDataFragment extends Fragment {


    @BindView(R.id.text_view_os_id)
    TextView textViewOsId;
    @BindView(R.id.os_layout_id)
    ViewGroup osLayoutId;
    @BindView(R.id.text_view_os_emissao)
    TextView textViewOsEmissao;
    @BindView(R.id.os_layout_emissao)
    ViewGroup osLayoutEmissao;
    @BindView(R.id.text_view_os_solicitacao)
    TextView textViewOsSolicitacao;
    @BindView(R.id.os_layout_solicitacao)
    ViewGroup osLayoutSolicitacao;
    @BindView(R.id.text_view_os_tp_rede)
    TextView textViewOsTpRede;
    @BindView(R.id.os_layout_tp_rede)
    ViewGroup osLayoutTpRede;
    @BindView(R.id.text_view_os_tp_designacao)
    TextView textViewOsTpDesignacao;
    @BindView(R.id.os_layout_tp_designacao)
    ViewGroup osLayoutTpDesignacao;
    @BindView(R.id.text_view_os_designacao_descricao)
    TextView textViewOsDesignacaoDescricao;
    @BindView(R.id.os_layout_designacao_descricao)
    ViewGroup osLayoutDesignacaoDescricao;
    @BindView(R.id.text_view_os_rede)
    TextView textViewOsRede;
    @BindView(R.id.os_layout_rede)
    ViewGroup osLayoutRede;
    @BindView(R.id.text_view_os_status)
    TextView textViewOsStatus;
    @BindView(R.id.os_layout_status)
    ViewGroup osLayoutStatus;
    @BindView(R.id.text_view_os_agendado_para)
    TextView textViewOsAgendadoPara;
    @BindView(R.id.os_layout_agendado_para)
    ViewGroup osLayoutAgendadoPara;
    @BindView(R.id.text_view_os_mapeado)
    TextView textViewOsMapeado;
    @BindView(R.id.os_layout_mapeado)
    ViewGroup osLayoutMapeado;
    Unbinder unbinder;

    private Os os;

    public OsDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        os = getArguments().getParcelable(ValenetUtils.KEY_OS);

        View view = inflater.inflate(R.layout.fragment_os_data, container, false);
        unbinder = ButterKnife.bind(this, view);

        if(os.getOsid() == null)
            osLayoutId.setVisibility(View.GONE);
        else
            textViewOsId.setText(os.getOsid()+"");

        if(os.getEmissao() == null)
            osLayoutEmissao.setVisibility(View.GONE);
        else
            textViewOsEmissao.setText(ValenetUtils.convertJsonToStringDate(os.getEmissao()));

        if(os.getTipoRede() == null)
            osLayoutTpRede.setVisibility(View.GONE);
        else
            textViewOsTpRede.setText(os.getTipoRede()+"");

        if(os.getDesignacaoTipo() == null)
            osLayoutTpDesignacao.setVisibility(View.GONE);
        else
            textViewOsTpDesignacao.setText(os.getDesignacaoTipo()+"");

        if(os.getDesignacaoDescricao() == null)
            osLayoutDesignacaoDescricao.setVisibility(View.GONE);
        else
            textViewOsDesignacaoDescricao.setText(os.getDesignacaoDescricao()+"");

        if(os.getRede() == null)
            osLayoutRede.setVisibility(View.GONE);
        else
            textViewOsRede.setText(os.getRede()+"");

        if(os.getStatusOs() == null)
            osLayoutStatus.setVisibility(View.GONE);
        else
            textViewOsStatus.setText(os.getStatusOs()+"");

        if(os.getAgendadoPara() == null)
            osLayoutAgendadoPara.setVisibility(View.GONE);
        else
            textViewOsAgendadoPara.setText(os.getAgendadoPara()+"");

        if(os.getMapeado() == null)
            osLayoutMapeado.setVisibility(View.GONE);
        else
            textViewOsMapeado.setText(os.getMapeado()+"");

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
