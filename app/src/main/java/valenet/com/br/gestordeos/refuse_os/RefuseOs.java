package valenet.com.br.gestordeos.refuse_os;

import java.util.List;

import valenet.com.br.gestordeos.model.entity.ReasonRefuseOs;

public interface RefuseOs {
    interface RefuseOsView {

        void hideRefuseOsView();

        void showRefuseOsView();

        void hideLoading();

        void showLoading();

        void showErrorConectionView();

        void hideErrorConectionView();

        void showErrorServerView();

        void hideErrorServerView();

        void loadReasonsRefuseOs(List<ReasonRefuseOs> reasonRefuseOsList);

        void showErrorRefuseOs();

        void showSuccessRefuseOs();
    }

    interface RefuseOsPresenter {
        void getReasonsToRefuseOs();

        void putRefuseOs(Integer agendamentoId, Integer motcanId, String motcanTx);
    }

    interface RefuseOsInteractor {
        void getReasonsToRefuseOs(onFinishedGetReasonsToRefuseOs listener);

        void putRefuseOs(Integer agendamentoId, Integer motcanId, String motcanTx, onFinishedPutRefuseOs listener);

        interface onFinishedGetReasonsToRefuseOs {
            void onSuccessGetReasonsToRefuseOs(List<ReasonRefuseOs> reasonRefuseOsList);

            void onErrorConectionGetReasonsToRefuseOs();

            void onErrorServerGetReasonsToRefuseOs();
        }

        interface onFinishedPutRefuseOs {
            void onSuccessPutRefuseOs();

            void onErrorPutRefuseOs();
        }

    }
}
