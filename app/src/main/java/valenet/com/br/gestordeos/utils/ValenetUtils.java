package valenet.com.br.gestordeos.utils;

import java.text.Normalizer;

public class ValenetUtils {

    public static final String KEY_FILTERED_LIST = "KEY_FILTERED_LIST";

    public static String removeAccent(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }
}
