import weblogic.servlet.logging.CustomELFLogger;
import weblogic.servlet.logging.FormatStringBuffer;
import weblogic.servlet.logging.HttpAccountingInfo;

public class HTTPAccessFields implements CustomELFLogger {
    private static boolean empty( final String s ) {
        return s == null || s.trim().isEmpty();
    }

    private String getIP(HttpAccountingInfo metrics) {
        String ip = metrics.getHeader("X-Forwarded-For");
        if(empty(ip)){
            ip = metrics.getHeader("WL-Proxy-Client-IP");
        }
        if(empty(ip)) {
            ip = metrics.getRemoteAddr();
        }
        return ip;
    }

    public void logField(HttpAccountingInfo metrics, FormatStringBuffer buff) {
        buff.appendValueOrDash(metrics.getRemoteUser());
        buff.append(" ");
        buff.appendValueOrDash(getIP(metrics));
    }
}