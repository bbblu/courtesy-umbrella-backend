package tw.edu.ntub.imd.courtesyumbrella.wrapper;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.BrowserType;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;
import org.springframework.lang.NonNull;
import tw.edu.ntub.imd.databaseconfig.enumerated.LogRecordDevice;
import tw.edu.ntub.imd.databaseconfig.enumerated.LogRecordDeviceType;

import javax.servlet.http.HttpServletRequest;

public class RequestWrapper {
    private final HttpServletRequest request;
    private final String originUserAgent;
    private final UserAgent userAgent;

    public RequestWrapper(@NonNull HttpServletRequest request) {
        this.request = request;
        originUserAgent = request.getHeader("User-Agent");
        this.userAgent = new UserAgent(originUserAgent);
    }

    public String getIpv4() {
        return request.getRemoteAddr();
    }

    public LogRecordDevice getDevice() {
        if (originUserAgent.startsWith("Postman")) {
            return LogRecordDevice.POSTMAN;
        }
        Browser browser = userAgent.getBrowser();
        BrowserType browserType = browser.getBrowserType();
        switch (browserType) {
            case WEB_BROWSER:
                return LogRecordDevice.BROWSER;
            case MOBILE_BROWSER:
                return LogRecordDevice.APP_BROWSER;
            case APP:
                return LogRecordDevice.APP;
            default:
                return LogRecordDevice.UNKNOWN;
        }
    }

    public LogRecordDeviceType getDeviceType() {
        if (originUserAgent.startsWith("Postman")) {
            return LogRecordDeviceType.POSTMAN;
        }
        Browser browser = userAgent.getBrowser();
        Browser parent = browser.getGroup();
        switch (parent) {
            case IE:
                return LogRecordDeviceType.IE;
            case EDGE:
                return LogRecordDeviceType.EDGE;
            case CHROME:
                return LogRecordDeviceType.CHROME;
            case FIREFOX:
                return LogRecordDeviceType.FIRE_FOX;
            case SAFARI:
                return LogRecordDeviceType.SAFARI;
            case OPERA:
                return LogRecordDeviceType.OPERA;
            default:
                return LogRecordDeviceType.UNKNOWN;
        }
    }

    public String getDeviceVersion() {
        if (originUserAgent.startsWith("Postman")) {
            return originUserAgent.split("/")[1];
        }
        Browser browser = userAgent.getBrowser();
        Version browserVersion = browser.getVersion(originUserAgent);
        return browserVersion.getVersion();
    }
}
