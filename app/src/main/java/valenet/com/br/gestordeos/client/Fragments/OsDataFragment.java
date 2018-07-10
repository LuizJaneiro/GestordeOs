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
    @BindView(R.id.text_view_os_address)
    TextView textViewOsAddress;
    @BindView(R.id.os_layout_address)
    RelativeLayout osLayoutAddress;
    @BindView(R.id.text_view_os_agendado)
    TextView textViewOsAgendado;
    @BindView(R.id.os_layout_agendado)
    RelativeLayout osLayoutAgendado;

    Unbinder unbinder;


    private Os os;
    private boolean cameFromSchedule;

    public OsDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        os = getArguments().getParcelable(ValenetUtils.KEY_OS);
        cameFromSchedule = getArguments().getBoolean(ValenetUtils.KEY_CAME_FROM_SCHEDULE);

        View view = inflater.inflate(R.layout.fragment_os_data, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (os.getOsid() == null)
            osLayoutId.setVisibility(View.GONE);
        else
            textViewOsId.setText(os.getOsid() + "");

        if (os.getEmissao() == null)
            osLayoutEmissao.setVisibility(View.GONE);
        else
            textViewOsEmissao.setText(ValenetUtils.convertJsonToStringDate(os.getEmissao()));

        if (os.getTipoRede() == null)
            osLayoutTpRede.setVisibility(View.GONE);
        else
            textViewOsTpRede.setText(os.getTipoRede() + "");

        if (cameFromSchedule) {
            if (osLayoutAgendado != null)
                osLayoutAgendado.setVisibility(View.GONE);
        } else {
            if (os.getAgendadoPara() == null)
                osLayoutAgendado.setVisibility(View.GONE);
            else
                textViewOsAgendado.setText(os.getAgendadoPara() + "");
        }

        if (os.getDesignacaoTipo() == null)
            osLayoutTpDesignacao.setVisibility(View.GONE);
        else {
            String designacao = os.getDesignacaoTipo() + " - ";
            if (os.getDesignacaoDescricao() == null) {
                designacao += "Sem descrição";
            } else {
                designacao += os.getDesignacaoDescricao();
            }
            textViewOsTpDesignacao.setText(designacao);
        }

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
