package Data.FormulaOne;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.ArrayList;
import java.util.List;

public class Drivers {
    private List<SingleDriver> drivers = new ArrayList<SingleDriver>();


    public List<SingleDriver> getDrivers() {
        return drivers;
    }
}
