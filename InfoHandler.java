import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**Przyklad ContentHandlera.
 * Informuje o niektorych zdarzeniach SAX wypisujac tekst na standardowe wyjscie.
 * @author Patryk Czarnik
 */
public class InfoHandler extends DefaultHandler {

  boolean bFirstName = false;
  boolean bLastName = false;
  boolean bNickName = false;
  boolean bMarks = false;
  String rollNo;
  int flag = 0;

  @Override
  public void startElement(String uri,
                           String localName, String qName, Attributes attributes) throws SAXException {

    if (qName.equalsIgnoreCase("film")) {

    } else if (qName.equalsIgnoreCase("tytul")) {
      bFirstName = true;
      rollNo = attributes.getValue("rok");
    } else if (qName.equalsIgnoreCase("gatunek")) {
      bLastName = true;
    } else if (qName.equalsIgnoreCase("dataPremiery")) {
      bNickName = true;
    }
    else if (qName.equalsIgnoreCase("rezyser")) {
      bMarks = true;
    }
  }

  @Override
  public void endElement(String uri,
                         String localName, String qName) throws SAXException {
    if (qName.equalsIgnoreCase("film")) {
      System.out.println("\n\n");
    }
  }

  @Override
  public void characters(char ch[], int start, int length) throws SAXException {

    if (bFirstName) {
      System.out.println("Tytul: \t\t\t\t"
              + new String(ch, start, length) + "\nRok:\t\t\t\t" + rollNo);
      bFirstName = false;
    } else if (bLastName) {
      if( flag == 0) {
        System.out.println("Gatunek:\t\t\t" + new String(ch, start, length));
        bLastName = false;
        flag = 1;
      }
      else {
        System.out.println("\t\t\t\t\t"+ new String(ch, start, length));
        bLastName = false;
      }
    } else if (bNickName) {
      flag = 0;
      System.out.println("Data premiery:\t\t" + new String(ch, start, length));
      bNickName = false;
    } else if (bMarks) {
      System.out.println("Rezyser:\t\t\t" + new String(ch, start, length));
      bMarks = false;
    }
  }
}
