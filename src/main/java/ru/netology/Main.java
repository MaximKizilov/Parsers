package ru.netology;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        // List<Employee> list1 = parseCSV(columnMapping, fileName);
        //  List<Employee> list2 = parseXML("data.xml");
        //   String json = listToJson(list2);
        //   writeString(json);
        String json = readString("new_data.json");
        List<Employee> list3 = jsonToList(json);
        System.out.println(list3);
    }

    private static List<Employee> jsonToList(String json) {
        JSONParser parser = new JSONParser();
        //  Object obj = parser.parse(new FileReader(json));
        List<Employee> object = new ArrayList<>();
        JSONArray jsonArray = (JSONArray) JSONValue.parse(json);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        for (int i = 0; i < jsonArray.size(); i++) {
            object.add(gson.fromJson(jsonArray.get(i).toString(), Employee.class));
        }
        return object;
    }


    private static String readString(String jsonFile) {
        String s;
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(jsonFile))) {
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
            return sb.toString();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    private static List<Employee> parseXML(String xMLname) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xMLname);
        document.getDocumentElement().normalize();
        NodeList employeeElements = document.getElementsByTagName("employee");
        List<Employee> colections = new ArrayList<Employee>();
        for (int i = 0; i < employeeElements.getLength(); i++) {
            Node nNode = employeeElements.item(i);
            if (employeeElements.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) employeeElements.item(i);
                NamedNodeMap map = elem.getAttributes();
                Node node1 = elem.getElementsByTagName("id").item(0);
                int id = Integer.parseInt(node1.getTextContent());
                Node node2 = elem.getElementsByTagName("firstName").item(0);
                String fname = node2.getTextContent();
                Node node3 = elem.getElementsByTagName("lastName").item(0);
                String lname = node3.getTextContent();
                Node node4 = elem.getElementsByTagName("country").item(0);
                String country = node4.getTextContent();
                Node node5 = elem.getElementsByTagName("age").item(0);
                int age = Integer.parseInt(node5.getTextContent());
                colections.add(new Employee(id, fname, lname, country, age));
            }
        }
        return colections;
    }

    static List<Employee> parseCSV(String[] key, String CSVname) {
        try (CSVReader reader = new CSVReader(new FileReader(CSVname))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(key);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            List<Employee> employees = csv.parse();
            return employees;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static String listToJson(List<Employee> fromCSV) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        String json = gson.toJson(fromCSV, listType);
        return json;
    }

    static void writeString(String json) {
        try (FileWriter file = new FileWriter("new_data.json")) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}   
