import java.util.Random;

/**
 * Created by Zver on 13.01.2016.
 */
public class EmailGenerator {
    private final String SYMBOLS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final Random rand = new Random();

    /**
     * генерация email
     *
     * @param length длина генерируемого имени
     * @param domain шаблонный домен
     * @return email
     */
    public String randomEmail(int length, String domain) {
        String name = randomName(length);
        return name + "@" + domain;
    }

    /**
     * генерация имени
     *
     * @param length длина генерируемого имени
     * @return name
     */
    private String randomName(int length) {
        StringBuffer sb = new StringBuffer(length);
        for (int i = 0; i < length; i++)
            sb.append(SYMBOLS.charAt(rand.nextInt(SYMBOLS.length())));
        return sb.toString();
    }
}
