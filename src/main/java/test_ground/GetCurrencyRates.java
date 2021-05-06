package test_ground;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.List;

public class GetCurrencyRates {
    @JacksonXmlElementWrapper(localName = "tem:GetCurrencyRates")
    @JacksonXmlProperty(localName = "tem:RateDate")

    private List<String> RateDate;
    public GetCurrencyRates(List<String> rateDate) {
        RateDate = rateDate;
    }

}



