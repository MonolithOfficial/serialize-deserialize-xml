package test_ground;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.List;

// XML სტრუქტურის (GetCurrencyRates.xml) შესაბამისი Plain Old Java Class (POJO) ნოტაცია.
//   |||||     გასათვალისწინებელია, რომ რაც უფრო რთულია XML-ის სტრუქტურა,
//   მით უფრო რთულია მისი შესაბამისი POJO ნოტაცია.     |||||
@JacksonXmlRootElement(localName = "soap:Envelope")
public class RateReqModel {
    @JacksonXmlProperty(isAttribute = true, localName = "xmlns:soap")
    private String soap = "http://schemas.xmlsoap.org/soap/envelope/";

    @JacksonXmlProperty(isAttribute = true, localName = "xmlns:tem")
    private String tem = "http://tempuri.org/";

    @JacksonXmlProperty(localName = "soap:Header", namespace = "")
    private String Header;

    @JacksonXmlElementWrapper(localName = "soap:Body")
    private List<GetCurrencyRates> GetCurrencyRates;

//  კონსტრუქტორი იღებს GetCurrencyRates ტიპის წევრების მქონე ლისტს.
    public RateReqModel(List<GetCurrencyRates> GetCurrencyRates) {
        this.GetCurrencyRates = GetCurrencyRates;
    }
}