import Data.FormulaOne.Drivers;
import Data.FormulaOne.SingleDriver;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import static io.restassured.RestAssured.given;

import org.apache.commons.io.IOUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import test_ground.GetCurrencyRates;
import test_ground.RateReqModel;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DeserializeXML {
    @DataProvider
    public static Object[][] dataproviderTest() {
        return new Object[][]{
            {"2021-03-29", "saxeli"},
            {"2021-03-29"}
        };
    }

    @Test
//  XML-ის დესერიალიზაცია XML -> ჯავას ობიექტი: კონტექსტისთვის აღებულია რაიმე XML ფაილი და შესაბამისი
//  ტექსტური ფაილი, ეს უკანასკნელი მონაცემთა ბაზის როლს ასრულებს.
    public void deserializeFormulaOne() throws IOException {

        File xmlSource = new File("src/main/resources/FormulaOne.xml");
        File databaseSource = new File("src/main/resources/FormulaOneDriversDB.txt");

//      მოცემული კლასი XML სტრინგის დესერიალიზაციას მოახდენს.
        XmlMapper xmlMapper = new XmlMapper();

//      ზედა ინსტანციისთვის საჭირო XML სტრინგს ვიღებთ შემოტანილი ფაილის სტრინგად
//      კონვერტირების შემდგომ. inputStreamToString არ არის ჩაშენებული ფუნქცია -
//      შეგიძლიათ ის იხილოთ ამ ფაილის ბოლოში.
        String xml = inputStreamToString(new FileInputStream(xmlSource));

//      Drivers არის წინასწარ განსაზღვრული, XML სტრუქტურასთან გაიგივებული კლასი, ე.წ. POJO.
//      XmlMapper-ის ინსტანცია XML-ის სტრინგიდან აიღებს თითოეულ ნოუდს და მას მის შესაბამის
//      Drivers კლასში არსებულ ცვლადთან დააკავშირებს.
//      ამ კონკრეტული XML სტრუქტურის თანახმად, drivers ობიექტი შეიცავს SingleDriver ტიპის
//      ობიექტებს, ერთი ლისტის სახით.
        Drivers drivers = xmlMapper.readValue(xml, Drivers.class);



//      მოცემული კოდი ჩამოუვლის თითოეულ drivers.getDrivers() ლისტში არსებულ
//      SingleDriver ტიპის ობიექტებს და მათ სახელებს შეამოწმებს შემოტანილი ტექსტური ფაილის
//      მეშვეობით.
        BufferedReader rd;
        try {
            rd = new BufferedReader(new FileReader(databaseSource));
            String line = rd.readLine();

            for (SingleDriver driver : drivers.getDrivers()){
//                System.out.println(driver.getfName());
//                System.out.println(line);
                Assert.assertEquals(driver.getfName(), line);

                line = rd.readLine();
            }
            rd.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }


//  XML-ის სერიალიზაცია: ჯავას ობიექტი -> XML
    @Test(dataProvider = "dataproviderTest")
    public void SerializeXML(String tarigi, String saxeli) throws IOException {


//      აქ XmlMapper-ის ინსტანცია
        XmlMapper xmlMapper = new XmlMapper();
//      იდენტაციის მხარდაჭერა (XML არ გამოჩნდება ერთ ხაზზე).
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);


//      XML სტრუქტურის ყველაზე შიდა წევრი, რომელსაც გააჩნია value.
//      მოცემულ ლისტში მეტი თარიღს დამატებით შეიქმნება მეტი XML ნოუდი.
        List<String> RateDates = new ArrayList<>();
        RateDates.add(tarigi);

//      XML სტრუქტურის მიხედით ზედა ნოუდის მშობელი ელემენტი, რომელსაც ასევე ლისტის
//      ფორმა გააჩნია.
        List<GetCurrencyRates> GetCurrencies = new ArrayList<>();
        GetCurrencyRates gc = new GetCurrencyRates(RateDates);
        GetCurrencies.add(gc);

//      იქმნება RateReqModel კლასის ინსტანცია, ანუ გზავნილის სრული მოდელი, რომელიც, თავის
//      მხრივ, writeValueAsString() მეთოდში გატარების შემდეგ წარმოქმნის ვალიდურ XML სტრინგს.
        String xml = xmlMapper.writeValueAsString(new RateReqModel(GetCurrencies));

//      Jackson ბიბლიოთეკა ვერ აშორებს ამ ერთ ზედმეტ ელემენტს, ამიტომ საჭიროა მისი ლიკვიდაცია
//      String უტილიტების მეშვეობით.
        xml = xml.replace("<GetCurrencyRates>", "");
        xml = xml.replace("</GetCurrencyRates>", "");
        System.out.println(xml);

//      რიქვესთის გაგზავნა. თანდართულია ასევე თვით XML ფაილის გადაცემის ალტერნატივაც.
        given().header("Content-Type","text/xml")

                .and().body(xml)
                .when().post("http://currencyconverter.kowabunga.net/converter.asmx").then().log().all();
    }
//  ეს ფუნქცია აკონვერტირებს XML ფაილის შიგთავსს სტრინგად.
    public String inputStreamToString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        br.close();
        return sb.toString();
    }


}


