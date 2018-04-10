package valenet.com.br.gestordeos.os_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.model.Os;
import valenet.com.br.gestordeos.utils.ClickGuard;

public class OsItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Os> osList;
    private final Context context;

    public OsItemAdapter(List<Os> osList, Context context) {
        this.osList = osList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v;
        RecyclerView.ViewHolder viewHolder = null;

        v = inflater.inflate(R.layout.card_os_item, parent, false);
        viewHolder = new MViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Os item = osList.get(position);
        ((MViewHolder) holder).textViewClientName.setText(item.getClient());
        ((MViewHolder) holder).textViewDistance.setText(item.getDistance() + " Km");
        String details = new String();
        details = item.getType() + " em ";
        int day = item.getDate().getDay();
        int month = item.getDate().getMonth();
        int year = item.getDate().getYear();

        details += day + "/" + month + "/" + year;

        ((MViewHolder) holder).textViewDetails.setText(details);

        //ClickGuard.guard(((MViewHolder) holder).osItemView);
    }

    @Override
    public int getItemCount() {
        if (osList != null) {
            return osList.size();
        }
        return 0;
    }

    class MViewHolder extends RecyclerView.ViewHolder {
        final TextView textViewClientName;
        final TextView textViewDetails;
        final TextView textViewDistance;
        final ViewGroup osItemView;

        MViewHolder(View container) {
            super(container);
            this.textViewClientName = (TextView) container.findViewById(R.id.text_view_client_name);
            this.textViewDetails = (TextView) container.findViewById(R.id.text_view_details);
            this.textViewDistance = (TextView) container.findViewById(R.id.text_view_distance);
            this.osItemView = (ViewGroup) container.findViewById(R.id.os_item_view);
        }
    }

}
