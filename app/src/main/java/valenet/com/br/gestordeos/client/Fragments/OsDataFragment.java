package valenet.com.br.gestordeos.client.Fragments;


import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.model.entity.OrdemDeServico;
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
    @BindView(R.id.text_view_download_pdf_os)
    TextView textViewDownloadPdfOs;

    Unbinder unbinder;

    private OrdemDeServico ordemDeServico;
    private boolean cameFromSchedule;
    private DownloadManager downloadManager;

    public OsDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ordemDeServico = getArguments().getParcelable(ValenetUtils.KEY_OS);
        cameFromSchedule = getArguments().getBoolean(ValenetUtils.KEY_CAME_FROM_SCHEDULE);

        View view = inflater.inflate(R.layout.fragment_os_data, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (ordemDeServico.getOsid() == null)
            osLayoutId.setVisibility(View.GONE);
        else
            textViewOsId.setText(ordemDeServico.getOsid() + "");

        if (ordemDeServico.getEmissao() == null)
            osLayoutEmissao.setVisibility(View.GONE);
        else
            textViewOsEmissao.setText(ValenetUtils.convertJsonToStringDate(ordemDeServico.getEmissao()));

        if (ordemDeServico.getTipoRede() == null)
            osLayoutTpRede.setVisibility(View.GONE);
        else
            textViewOsTpRede.setText(ordemDeServico.getTipoRede() + "");

        if (cameFromSchedule) {
            if (osLayoutAgendado != null)
                osLayoutAgendado.setVisibility(View.GONE);
        } else {
            if (ordemDeServico.getAgendadoPara() == null)
                osLayoutAgendado.setVisibility(View.GONE);
            else
                textViewOsAgendado.setText(ordemDeServico.getAgendadoPara() + "");
        }

        if (ordemDeServico.getDesignacaoTipo() == null)
            osLayoutTpDesignacao.setVisibility(View.GONE);
        else {
            String designacao = ordemDeServico.getDesignacaoTipo() + " - ";
            if (ordemDeServico.getDesignacaoDescricao() == null) {
                designacao += "Sem descrição";
            } else {
                designacao += ordemDeServico.getDesignacaoDescricao();
            }
            textViewOsTpDesignacao.setText(designacao);
        }

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

    @OnClick(R.id.text_view_download_pdf_os)
    public void onViewClicked() {
        final Activity activity = this.getActivity();
        android.app.AlertDialog.Builder builderCad;
        builderCad = new android.app.AlertDialog.Builder(activity);
        builderCad.setTitle("Atenção");
        builderCad.setMessage("Deseja realizar o download do PDF?");
        builderCad.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                downloadManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse("http://api3.valenet.com.br/ordemdeservico/downloados?osid=" + ordemDeServico.getOsid());
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                Long reference = downloadManager.enqueue(request);
            }
        });
        builderCad.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final android.app.AlertDialog dialog = builderCad.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.btn_negative_dialog));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.btn_positive_dialog));
            }
        });
        dialog.show();
    }
}
