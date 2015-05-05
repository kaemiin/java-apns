import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.io.InputStream;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

import com.notnoop.apns.utils.junit.Repeat;
import com.notnoop.apns.internal.Utilities;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.SimpleApnsNotification;

@SuppressWarnings("ALL")
public class PChatTest {
    
    private static final String CLIENT_STORE = "apn_production_identity.p12";
    
    private static final String CLIENT_PASSWORD = "";
    
    // sandbox: gateway.sandbox.push.apple.com
    private static final String APNS_ADDR = "gateway.push.apple.com";
    
    private static final int APNS_PORT = 2195;
    
    private static final String DEVICE_TOKEN = "";
    
    private static SimpleApnsNotification msg1 = new SimpleApnsNotification(DEVICE_TOKEN, "{\"aps\":{\"alert\":\"test\",\"sound\":\"default.caf\"},\"command\":\"18|20150324111357|20150413112116\"}");
    
    public static SSLContext clientContext() {
        try {
            InputStream stream = PChatTest.class.getResourceAsStream("/" + CLIENT_STORE);
            assert stream != null;
            SSLContext context = Utilities.newSSLContext(stream, CLIENT_PASSWORD, "PKCS12", "sunx509");
            context.init(null, new TrustManager[] { new X509TrustManagerTrustAll() }, new SecureRandom());
            return context;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }   
    
    static class X509TrustManagerTrustAll implements X509TrustManager {
        public boolean checkClientTrusted(java.security.cert.X509Certificate[] chain){
            return true;
        }

        public boolean isServerTrusted(java.security.cert.X509Certificate[] chain){
            return true;
        }

        public boolean isClientTrusted(java.security.cert.X509Certificate[] chain){
            return true;
        }

        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {}

        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {}
    }
    

    @Before
    public void startup() {

    }

    @After
    public void tearDown() {

    }
    
    @Repeat(count = 1)
    @Test(timeout = 20000)
    public void testSendOneSimple() throws InterruptedException {

        System.out.println("Starting to test (20s)...");
        
        ApnsService service =
                APNS.newService().withSSLContext(clientContext())
                .withGatewayDestination(APNS_ADDR, APNS_PORT)
                .build(); 
        
        service.push(msg1);
        
        
        //TODO
    }    
}