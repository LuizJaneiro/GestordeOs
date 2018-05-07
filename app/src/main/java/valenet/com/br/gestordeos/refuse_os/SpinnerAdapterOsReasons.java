package valenet.com.br.gestordeos.refuse_os;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.List;

public class SpinnerAdapterOsReasons extends ArrayAdapter<String> {
    public SpinnerAdapterOsReasons(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public SpinnerAdapterOsReasons(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public SpinnerAdapterOsReasons(@NonNull Context context, int resource, @NonNull String[] objects) {
        super(context, resource, objects);
    }

    public SpinnerAdapterOsReasons(@NonNull Context context, int resource, int textViewResourceId, @NonNull String[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public SpinnerAdapterOsReasons(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
    }

    public SpinnerAdapterOsReasons(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<String> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public int getCount() {
        int count = super.getCount();

        return count>0 ? count-1 : count ;
    }
}
