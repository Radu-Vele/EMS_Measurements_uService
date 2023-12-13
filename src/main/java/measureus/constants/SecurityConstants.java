package measureus.constants;

public class SecurityConstants {
    public static final String[] ALLOWED_ORIGINS = {"http://localhost:5173", "http://localhost:8081"};
    public static final String[] NO_AUTH_REQUIRED_PATTERNS = {};

    public static final String[] USER_AUTH_REQUIRED_PATTERNS = {"/measurements/getConsumptionForDay"};
}
