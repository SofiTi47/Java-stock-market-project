//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.06.01 at 08:18:27 PM IDT 
//


package src.JAXB;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element ref="{}rse-stocks"/>
 *         &lt;element ref="{}rse-users"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "rizpa-stock-exchange-descriptor")
public class RizpaStockExchangeDescriptor {

    @XmlElement(name = "rse-stocks", required = true)
    protected RseStocks rseStocks;
    @XmlElement(name = "rse-users", required = true)
    protected RseUsers rseUsers;

    /**
     * Gets the value of the rseStocks property.
     * 
     * @return
     *     possible object is
     *     {@link RseStocks }
     *     
     */
    public RseStocks getRseStocks() {
        return rseStocks;
    }

    /**
     * Sets the value of the rseStocks property.
     * 
     * @param value
     *     allowed object is
     *     {@link RseStocks }
     *     
     */
    public void setRseStocks(RseStocks value) {
        this.rseStocks = value;
    }

    /**
     * Gets the value of the rseUsers property.
     * 
     * @return
     *     possible object is
     *     {@link RseUsers }
     *     
     */
    public RseUsers getRseUsers() {
        return rseUsers;
    }

    /**
     * Sets the value of the rseUsers property.
     * 
     * @param value
     *     allowed object is
     *     {@link RseUsers }
     *     
     */
    public void setRseUsers(RseUsers value) {
        this.rseUsers = value;
    }

}
