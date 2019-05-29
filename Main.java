import java.io.FileOutputStream;
import java.lang.reflect.Constructor;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

// rejestr do tworzenia implementacji DOM
import org.w3c.dom.*;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;

// Implementacja DOM Level 3 Load & Save
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSParser; // Do serializacji (zapisywania) dokumentow
import org.w3c.dom.ls.LSSerializer;
import org.w3c.dom.ls.LSOutput;

// Konfigurator i obsluga bledow

// Do pracy z dokumentem


public class Main {
    public static Document document;

    public static void main(String[] args) {
        int i = 4;
        while (i!=0){
            i=4;
            System.out.println("CO CHCESZ ZROBIC? PODAJ LICZBE \n" +
                    " 1 -- POKAZ FILMY\n" +
                    " 2 -- DODAJ FILM \n" +
                    " 3 -- USUN FILM \n"  +
                    " 4 -- EDYTUJ FILM \n"+
                    " 0 -- WYJDZ Z POROGRAMU");
            i=wczytaj();
            switch(i) {
                case 1:
                    Wypisz(args);
                    break;
                case 2:
                    Dodaj(args);
                    break;
                case 3:
                    Usun(args);
                    break;
                case 4:
                    Edytuj(args);
                    break;
                case 0:
                    break;
                default:
                    // code block
            }
        }

    }

    public static int wczytaj(){
        Scanner in = new Scanner(System.in);
        int i = 4;
            try {
                i = in.nextInt();
            }catch (InputMismatchException e){
                System.out.println("PODAJ DOBRA LICZBE");
            }


        return i;
    }

    public static void Dodaj(String[] args) {
        if (args.length == 0) {
            printUsage();
            System.exit(1);
        }

        try {
            /*
             * ustawienie systemowej wlasnosci (moze byc dokonane w innym
             * miejscu, pliku konfiguracyjnym w systemie itp.) konkretna
             * implementacja DOM
             */
            System.setProperty(DOMImplementationRegistry.PROPERTY,
                    "org.apache.xerces.dom.DOMXSImplementationSourceImpl");
            DOMImplementationRegistry registry = DOMImplementationRegistry
                    .newInstance();

            // pozyskanie implementacji Load & Save DOM Level 3 z rejestru
            DOMImplementationLS impl = (DOMImplementationLS) registry
                    .getDOMImplementation("LS");

            // stworzenie DOMBuilder
            LSParser builder = impl.createLSParser(
                    DOMImplementationLS.MODE_SYNCHRONOUS, null);

            // pozyskanie konfiguratora - koniecznie zajrzec do dokumentacji co
            // mozna poustawiac
            DOMConfiguration config = builder.getDomConfig();

            // stworzenie DOMErrorHandler i zarejestrowanie w konfiguratorze
            DOMErrorHandler errorHandler = getErrorHandler();
            config.setParameter("error-handler", errorHandler);

            // set validation feature
            config.setParameter("validate", Boolean.TRUE);

            // set schema language
            config.setParameter("schema-type",
                    "http://www.w3.org/2001/XMLSchema");

            // set schema location
            config.setParameter("schema-location", "personal.xsd");

            System.out.println("Parsowanie " + args[0] + "...");
            // sparsowanie dokumentu i pozyskanie "document" do dalszej pracy
            document = builder.parseURI(args[0]);

            // praca z dokumentem, modyfikacja zawartosci etc... np.
            // tutaj dodanie nowego pracownika poprzez skopiowanie innego

            System.out.println("Podaj Tytul: ");
            Scanner scanner = new Scanner(System.in);
            String tytul = scanner.nextLine();

            System.out.println("Podaj Gatunek: ");
            scanner = new Scanner(System.in);
            String gatunek = scanner.nextLine();

            System.out.println("Podaj Rezysera: ");
            scanner = new Scanner(System.in);
            String rezyser = scanner.nextLine();
            String date = "s";
            while (date.length() < 4){
                System.out.println("Podaj date w formacie yyyy-mm-dd: ");
            scanner = new Scanner(System.in);
            date = scanner.nextLine();
        }
            String rok = date.substring(0, 4);

            int istnieje = 0;
            NodeList gatunki = document.getElementsByTagName("gatunek");
            for (int i = 0; i < gatunki.getLength(); i++) {
                if (gatunki.item(i).getTextContent().equals(gatunek)) {
                    istnieje = 1;
                }
            }
            if (istnieje == 1) {
                NodeList film = document.getElementsByTagName("film");
                Element ten = (Element) film.item(1);
                Element newElem = (Element) ten.cloneNode(true);
                NodeList nl = newElem.getChildNodes();
                for (int i = 0; i < nl.getLength(); i++) {
                    Node n = nl.item(i);
                    if (n.getNodeName().equals("tytul")) {
                        Element xd = (Element) n;
                        n.setTextContent(tytul);
                        xd.setAttribute("rok", rok);

                    }
                    if (n.getNodeName().equals("gatunek")) {
                        n.setTextContent(gatunek);
                    }
                    if (n.getNodeName().equals("dataPremiery")) {
                        n.setTextContent(date);
                    }
                    if (n.getNodeName().equals("rezyser")) {
                        n.setTextContent(rezyser);
                    }
                }
                document.getFirstChild().insertBefore(newElem, null);
            }
            else {
                System.out.println("NIE MA TAKIEGO GATUNKU");
            }

            // pozyskanie serializatora
            LSSerializer domWriter = impl.createLSSerializer();
            // pobranie konfiguratora dla serializatora
            config = domWriter.getDomConfig();
            config.setParameter("xml-declaration", Boolean.TRUE);

            // pozyskanie i konfiguracja Wyjscia
            LSOutput dOut = impl.createLSOutput();
            dOut.setEncoding("latin2");
            dOut.setByteStream(new FileOutputStream("new_" + args[0]));

            System.out.println("Serializing document... ");
            domWriter.write(document, dOut);

            // Wyjscie na ekran
            // dOut.setByteStream(System.out);
            // domWriter.writeNode(System.out, document);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void Usun(String[] args) {
        if (args.length == 0) {
            printUsage();
            System.exit(1);
        }

        try {
            /*
             * ustawienie systemowej wlasnosci (moze byc dokonane w innym
             * miejscu, pliku konfiguracyjnym w systemie itp.) konkretna
             * implementacja DOM
             */
            System.setProperty(DOMImplementationRegistry.PROPERTY,
                    "org.apache.xerces.dom.DOMXSImplementationSourceImpl");
            DOMImplementationRegistry registry = DOMImplementationRegistry
                    .newInstance();

            // pozyskanie implementacji Load & Save DOM Level 3 z rejestru
            DOMImplementationLS impl = (DOMImplementationLS) registry
                    .getDOMImplementation("LS");

            // stworzenie DOMBuilder
            LSParser builder = impl.createLSParser(
                    DOMImplementationLS.MODE_SYNCHRONOUS, null);

            // pozyskanie konfiguratora - koniecznie zajrzec do dokumentacji co
            // mozna poustawiac
            DOMConfiguration config = builder.getDomConfig();

            // stworzenie DOMErrorHandler i zarejestrowanie w konfiguratorze
            DOMErrorHandler errorHandler = getErrorHandler();
            config.setParameter("error-handler", errorHandler);

            // set validation feature
            config.setParameter("validate", Boolean.TRUE);

            // set schema language
            config.setParameter("schema-type",
                    "http://www.w3.org/2001/XMLSchema");

            // set schema location
            config.setParameter("schema-location", "personal.xsd");

            System.out.println("Parsowanie " + args[0] + "...");
            // sparsowanie dokumentu i pozyskanie "document" do dalszej pracy
            document = builder.parseURI(args[0]);

            // praca z dokumentem, modyfikacja zawartosci etc... np.
            // tutaj dodanie nowego pracownika poprzez skopiowanie innego


            Scanner scanner = new Scanner(System.in);
            NodeList film = document.getElementsByTagName("film");
            for(int i = 0; i < film.getLength();i++) {
                Node node = film.item(i);
                NodeList list = node.getChildNodes();
                for (int j = 0; j < list.getLength(); j++) {
                    if (list.item(j).getLocalName() == "tytul") {
                        System.out.println("Tytul:\t\t\t" + list.item(j).getTextContent());
                    }

                     else
                        continue;

                }
            }
            System.out.println("KTORY CHCESZ USUNAC? WPISZ TYTUL: ");
            scanner = new Scanner(System.in);
            String tytul = scanner.nextLine();
            int flag = 0;
            for(int i = 0; i < film.getLength();i++) {
                Node node = film.item(i);
                NodeList list = node.getChildNodes();
                for (int j = 0; j < list.getLength(); j++) {
                    if (list.item(j).getLocalName() == "tytul") {
                        flag++;
                    }

                    else
                        continue;

                }
            }
            if (flag != 0){
                for(int i = 0; i < film.getLength();i++) {
                    Node node = film.item(i);
                    NodeList list = node.getChildNodes();
                    for (int j = 0; j < list.getLength(); j++) {
                        if (list.item(j).getTextContent().equals(tytul)) {
                            film.item(i).getParentNode().removeChild(film.item(i));
                            System.out.println("USUNIETO");
                        }

                        else
                            continue;
                    }
                }
            }
            else
                System.out.println("NIE MA TAKIEGO FILMU");




            // pozyskanie serializatora
            LSSerializer domWriter = impl.createLSSerializer();
            // pobranie konfiguratora dla serializatora
            config = domWriter.getDomConfig();
            config.setParameter("xml-declaration", Boolean.TRUE);

            // pozyskanie i konfiguracja Wyjscia
            LSOutput dOut = impl.createLSOutput();
            dOut.setEncoding("latin2");
            dOut.setByteStream(new FileOutputStream("new_" + args[0]));

            System.out.println("Serializing document... ");
            domWriter.write(document, dOut);

            // Wyjscie na ekran
            // dOut.setByteStream(System.out);
            // domWriter.writeNode(System.out, document);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void Wypisz(String[] args) {
        if (args.length == 0) {
            printUsage();
            System.exit(1);
        }

        try {
            /*
             * ustawienie systemowej wlasnosci (moze byc dokonane w innym
             * miejscu, pliku konfiguracyjnym w systemie itp.) konkretna
             * implementacja DOM
             */
            System.setProperty(DOMImplementationRegistry.PROPERTY,
                    "org.apache.xerces.dom.DOMXSImplementationSourceImpl");
            DOMImplementationRegistry registry = DOMImplementationRegistry
                    .newInstance();

            // pozyskanie implementacji Load & Save DOM Level 3 z rejestru
            DOMImplementationLS impl = (DOMImplementationLS) registry
                    .getDOMImplementation("LS");

            // stworzenie DOMBuilder
            LSParser builder = impl.createLSParser(
                    DOMImplementationLS.MODE_SYNCHRONOUS, null);

            // pozyskanie konfiguratora - koniecznie zajrzec do dokumentacji co
            // mozna poustawiac
            DOMConfiguration config = builder.getDomConfig();

            // stworzenie DOMErrorHandler i zarejestrowanie w konfiguratorze
            DOMErrorHandler errorHandler = getErrorHandler();
            config.setParameter("error-handler", errorHandler);

            // set validation feature
            config.setParameter("validate", Boolean.TRUE);

            // set schema language
            config.setParameter("schema-type",
                    "http://www.w3.org/2001/XMLSchema");

            // set schema location
            config.setParameter("schema-location", "personal.xsd");

            System.out.println("Parsowanie " + args[0] + "...");
            // sparsowanie dokumentu i pozyskanie "document" do dalszej pracy
            document = builder.parseURI(args[0]);

            // praca z dokumentem, modyfikacja zawartosci etc... np.
            // tutaj dodanie nowego pracownika poprzez skopiowanie innego
            int flag;
            NodeList film = document.getElementsByTagName("film");
            for(int i = 0; i < film.getLength();i++){
                Node node = film.item(i);
                NodeList list = node.getChildNodes();
                flag = 0;
                for( int j = 0; j<list.getLength(); j++){
                    if(list.item(j).getLocalName() == "tytul"){
                        System.out.println("Tytul:\t\t\t"+ list.item(j).getTextContent());
                    }
                    else if(list.item(j).getLocalName() == "gatunek"){
                        if(flag == 0){
                            System.out.println("Gatunki:\t\t"+list.item(j).getTextContent());
                            flag++;
                        }
                    else{
                            System.out.println("\t\t\t\t"+list.item(j).getTextContent());

                        }
                    }
                    else if(list.item(j).getLocalName() == "dataPremiery"){
                        System.out.println("Data Premiery:\t"+ list.item(j).getTextContent());
                    }
                    else if(list.item(j).getLocalName() == "rezyser"){
                        System.out.println("Rezyser:\t\t"+ list.item(j).getTextContent()+"\n\n");
                    }
                    else
                        continue;

                }
            }


            // pozyskanie serializatora
            LSSerializer domWriter = impl.createLSSerializer();
            // pobranie konfiguratora dla serializatora
            config = domWriter.getDomConfig();
            config.setParameter("xml-declaration", Boolean.TRUE);

            // pozyskanie i konfiguracja Wyjscia
            LSOutput dOut = impl.createLSOutput();
            dOut.setEncoding("latin2");
            dOut.setByteStream(new FileOutputStream("new_" + args[0]));

            System.out.println("Serializing document... ");
            domWriter.write(document, dOut);

            // Wyjscie na ekran
            // dOut.setByteStream(System.out);
            // domWriter.writeNode(System.out, document);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void Edytuj(String[] args) {
        if (args.length == 0) {
            printUsage();
            System.exit(1);
        }

        try {
            /*
             * ustawienie systemowej wlasnosci (moze byc dokonane w innym
             * miejscu, pliku konfiguracyjnym w systemie itp.) konkretna
             * implementacja DOM
             */
            System.setProperty(DOMImplementationRegistry.PROPERTY,
                    "org.apache.xerces.dom.DOMXSImplementationSourceImpl");
            DOMImplementationRegistry registry = DOMImplementationRegistry
                    .newInstance();

            // pozyskanie implementacji Load & Save DOM Level 3 z rejestru
            DOMImplementationLS impl = (DOMImplementationLS) registry
                    .getDOMImplementation("LS");

            // stworzenie DOMBuilder
            LSParser builder = impl.createLSParser(
                    DOMImplementationLS.MODE_SYNCHRONOUS, null);

            // pozyskanie konfiguratora - koniecznie zajrzec do dokumentacji co
            // mozna poustawiac
            DOMConfiguration config = builder.getDomConfig();

            // stworzenie DOMErrorHandler i zarejestrowanie w konfiguratorze
            DOMErrorHandler errorHandler = getErrorHandler();
            config.setParameter("error-handler", errorHandler);

            // set validation feature
            config.setParameter("validate", Boolean.TRUE);

            // set schema language
            config.setParameter("schema-type",
                    "http://www.w3.org/2001/XMLSchema");

            // set schema location
            config.setParameter("schema-location", "personal.xsd");

            System.out.println("Parsowanie " + args[0] + "...");
            // sparsowanie dokumentu i pozyskanie "document" do dalszej pracy
            document = builder.parseURI(args[0]);

            // praca z dokumentem, modyfikacja zawartosci etc... np.
            // tutaj dodanie nowego pracownika poprzez skopiowanie innego


            Scanner scanner = new Scanner(System.in);
            NodeList film = document.getElementsByTagName("film");
            for(int i = 0; i < film.getLength();i++) {
                Node node = film.item(i);
                NodeList list = node.getChildNodes();
                for (int j = 0; j < list.getLength(); j++) {
                    if (list.item(j).getLocalName() == "tytul") {
                        System.out.println("Tytul:\t\t\t" + list.item(j).getTextContent());
                    }

                    else
                        continue;

                }
            }
            System.out.println("KTORY FILM CHCESZ EDYTOWAC? WPISZ TYTUL: ");
            scanner = new Scanner(System.in);
            String tytul = scanner.nextLine();
            int flag = 0;
            for(int i = 0; i < film.getLength();i++) {
                Node node = film.item(i);
                NodeList list = node.getChildNodes();
                for (int j = 0; j < list.getLength(); j++) {
                    if (list.item(j).getTextContent().equals(tytul)) {
                        flag++;
                    }

                    else
                        continue;

                }
            }
            if (flag != 0){
                int num = 6;
                while (num >4 || num<0) {
                    System.out.println("CO CHCESZ EDYTOWAC? \n 1 - Tytul\n 2 - Gatunek \n 3 - Data Premiery \n 4 - Rezyser \n 0 - Anuluj");
                    scanner = new Scanner(System.in);
                    num = scanner.nextInt();
                }
                switch (num){
                    case 1:
                        System.out.println("WPISZ NOWY TYTUL");
                        scanner = new Scanner(System.in);
                        String tyt = scanner.nextLine();
                        for(int i = 0; i < film.getLength();i++) {
                            Node node = film.item(i);
                            NodeList list = node.getChildNodes();
                            for (int j = 0; j < list.getLength(); j++) {
                                if (list.item(j).getTextContent().equals(tytul)) {
                                    list.item(j).setTextContent(tyt);
                                }
                            }
                        }
                        break;
                    case 2:
                        System.out.println("WPISZ NOWY GATUNEK");
                        int id = 0;
                        scanner = new Scanner(System.in);
                        String gat = scanner.nextLine();
                        for(int i = 0; i < film.getLength();i++) {
                            Node node = film.item(i);
                            NodeList list = node.getChildNodes();
                            for (int j = 0; j < list.getLength(); j++) {
                                if (list.item(j).getTextContent().equals(tytul)) {
                                    id = i;
                                }
                            }
                        }
                            Node nodee = film.item(id);
                            NodeList list = nodee.getChildNodes();
                            for (int j = 0; j < list.getLength(); j++) {
                                if (list.item(j).getLocalName() == "gatunek") {
                                    list.item(j).setTextContent(gat);
                                }
                            }

                        break;
                    case 3:
                        System.out.println("WPISZ NOWA DATE");
                        int ide = 0;
                        scanner = new Scanner(System.in);
                        String data = scanner.nextLine();
                        for(int i = 0; i < film.getLength();i++) {
                            Node node = film.item(i);
                            NodeList listt = node.getChildNodes();
                            for (int j = 0; j < listt.getLength(); j++) {
                                if (listt.item(j).getTextContent().equals(tytul)) {
                                    ide = i;
                                }
                            }
                        }
                            Node node = film.item(ide);
                            NodeList listt = node.getChildNodes();
                            for (int j = 0; j < listt.getLength(); j++) {
                                if (listt.item(j).getLocalName() == "dataPremiery") {
                                    listt.item(j).setTextContent(data);
                                }
                            }

                        break;
                    case 4:
                        System.out.println("WPISZ NOWEGO REZYSERA");
                        int idee =0;
                        scanner = new Scanner(System.in);
                        String dat = scanner.nextLine();
                        for(int i = 0; i < film.getLength();i++) {
                            Node nodeee = film.item(i);
                            NodeList listtt = nodeee.getChildNodes();
                            for (int j = 0; j < listtt.getLength(); j++) {
                                if (listtt.item(j).getTextContent().equals(tytul)) {
                                    idee = i;
                                }
                            }
                        }
                            Node nodeee = film.item(idee);
                            NodeList listtt = nodeee.getChildNodes();
                            for (int j = 0; j < listtt.getLength(); j++) {
                                if (listtt.item(j).getLocalName() == "rezyser") {
                                    listtt.item(j).setTextContent(dat);
                                }
                            }

                        break;
                }



            }
            else
                System.out.println("NIE MA TAKIEGO FILMU");




            // pozyskanie serializatora
            LSSerializer domWriter = impl.createLSSerializer();
            // pobranie konfiguratora dla serializatora
            config = domWriter.getDomConfig();
            config.setParameter("xml-declaration", Boolean.TRUE);

            // pozyskanie i konfiguracja Wyjscia
            LSOutput dOut = impl.createLSOutput();
            dOut.setEncoding("latin2");
            dOut.setByteStream(new FileOutputStream("new_" + args[0]));

            System.out.println("Serializing document... ");
            domWriter.write(document, dOut);

            // Wyjscie na ekran
            // dOut.setByteStream(System.out);
            // domWriter.writeNode(System.out, document);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }




    private static void printUsage() {

        System.err.println("usage: java Dom3Demo uri");
        System.err.println();
        System.err
                .println("NOTE: You can only validate DOM tree against XML Schemas.");

    }

    // obsluga bledow za pomoca anonimowej klasy wewnetrznej implementujacej
    // DOMErrorHandler
    // por. SAX ErrorHandler
    public static DOMErrorHandler getErrorHandler() {
        return new DOMErrorHandler() {
            public boolean handleError(DOMError error) {
                short severity = error.getSeverity();
                if (severity == error.SEVERITY_ERROR) {
                    System.out.println("[dom3-error]: " + error.getMessage());
                }
                if (severity == error.SEVERITY_WARNING) {
                    System.out.println("[dom3-warning]: " + error.getMessage());
                }
                if (severity == error.SEVERITY_FATAL_ERROR) {
                    System.out.println("[dom3-fatal-error]: "
                            + error.getMessage());
                }
                return true;
            }
        };
    }

}
