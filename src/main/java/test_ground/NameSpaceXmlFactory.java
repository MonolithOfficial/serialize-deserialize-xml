package test_ground;

import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Objects;

// დამატებული ფუნქციონალი: namespace-ების დამატება გადეცემული Map ობიექტის მეშვეობით.
public class NameSpaceXmlFactory extends XmlFactory {
    private final Map<String, String> prefix2Namespace;

    public NameSpaceXmlFactory(Map<String, String> prefix2Namespace) {
        this.prefix2Namespace = Objects.requireNonNull(prefix2Namespace);
    }

    @Override
    protected XMLStreamWriter _createXmlWriter(IOContext ctxt, Writer w) throws IOException {
        XMLStreamWriter writer = super._createXmlWriter(ctxt, w);
        try {
            for (Map.Entry<String, String> e : prefix2Namespace.entrySet()){
                writer.setPrefix(e.getKey(), e.getValue());
            }
        }catch (XMLStreamException e){
            StaxUtil.throwAsGenerationException(e, null);
        }
        return writer;
    }
}
