
package fr.cls.argos.dataxmldistribution.service;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "DixService", targetNamespace = "http://service.dataxmldistribution.argos.cls.fr/", wsdlLocation = "http://ws-argos.cls.fr/argosDws/services/DixService?wsdl")
public class DixService
    extends Service
{

    private final static URL DIXSERVICE_WSDL_LOCATION;
    private final static WebServiceException DIXSERVICE_EXCEPTION;
    private final static QName DIXSERVICE_QNAME = new QName("http://service.dataxmldistribution.argos.cls.fr/", "DixService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://ws-argos.cls.fr/argosDws/services/DixService?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        DIXSERVICE_WSDL_LOCATION = url;
        DIXSERVICE_EXCEPTION = e;
    }

    public DixService() {
        super(__getWsdlLocation(), DIXSERVICE_QNAME);
    }

    public DixService(WebServiceFeature... features) {
        super(__getWsdlLocation(), DIXSERVICE_QNAME, features);
    }

    public DixService(URL wsdlLocation) {
        super(wsdlLocation, DIXSERVICE_QNAME);
    }

    public DixService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, DIXSERVICE_QNAME, features);
    }

    public DixService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public DixService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns DixServicePortType
     */
    @WebEndpoint(name = "DixServicePort")
    public DixServicePortType getDixServicePort() {
        return super.getPort(new QName("http://service.dataxmldistribution.argos.cls.fr/", "DixServicePort"), DixServicePortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns DixServicePortType
     */
    @WebEndpoint(name = "DixServicePort")
    public DixServicePortType getDixServicePort(WebServiceFeature... features) {
        return super.getPort(new QName("http://service.dataxmldistribution.argos.cls.fr/", "DixServicePort"), DixServicePortType.class, features);
    }

    private static URL __getWsdlLocation() {
        if (DIXSERVICE_EXCEPTION!= null) {
            throw DIXSERVICE_EXCEPTION;
        }
        return DIXSERVICE_WSDL_LOCATION;
    }

}
