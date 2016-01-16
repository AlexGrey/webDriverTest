import java.util.Random;

/**
 * Created by Zver on 13.01.2016.
 */
public class EmailGenerator {
    private final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final Random rand = new Random();

    /**
     * генерация email
     *
     * @param length длина генерируемого имени
     * @param domain шаблонный домен
     * @return
     */
    public String randomEmail(int length, String domain) {
        String name = randomName(length);
        return name + "@" + domain;
    }

    /**
     * генерация имени
     *
     * @param len длина генерируемого имени
     * @return
     */
    private String randomName(int len) {
        StringBuffer sb = new StringBuffer(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rand.nextInt(AB.length())));
        return sb.toString();
    }
}
