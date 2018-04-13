package valenet.com.br.gestordeos.utils;

import java.text.Normalizer;

public class ValenetUtils {

    public static final String BASE_URL = "https://api.valenet.com.br/api/";
    public static final String KEY_FILTERED_LIST = "KEY_FILTERED_LIST";
    public static final Integer GROUP_OS_MERCANTIL = 1;
    public static final Integer GROUP_OS_CORRETIVA = 2;

    public static String removeAccent(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }
}
