import weblogic.servlet.logging.CustomELFLogger;
import weblogic.servlet.logging.FormatStringBuffer;
import weblogic.servlet.logging.HttpAccountingInfo;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTTPSoapActionFields implements CustomELFLogger {
    private static boolean empty( final String s ) {
        return s == null || s.trim().isEmpty();
    }

    private static Pattern pattern = Pattern.compile("action=\"?([\\.\\/\\w:]+)\"?");

    public void logField(HttpAccountingInfo metrics, FormatStringBuffer buff) {
        String addthis = null;

        // Code inspired by https://github.com/MaartenSmeets/weblogic
        String soapaction = metrics.getHeader("SOAPAction");
        // SOAP 1.2 based on a regex on the Content-type
        if (empty(soapaction)) {
            String contenttypestr = metrics.getContentType();
            if(contenttypestr != null) {
                Matcher matcher = pattern.matcher(contenttypestr);
                if (matcher.matches()) {
                    addthis = matcher.group(1);
                }
            }
        // SOAP 1.1 is based on a SOAPAction header
        } else {
            soapaction = soapaction.replace("\"", "");
            if (soapaction.length() > 0) {
                addthis = soapaction;
            }
        }
        buff.appendValueOrDash(addthis);
    }
}
