package utility;

import model.TripPlanningCompany;
import parser.ParserException;
import parser.XmlJsonParser;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;

public class MyFileHandler
{
    private final XmlJsonParser parser;

    public MyFileHandler()
    {
      parser = new XmlJsonParser();
    }

    /* ---------------- JSON ---------------- */

    public <T> void saveToJson(String fileName, T object)
        throws ParserException
    {
      parser.toJsonFile(object, fileName);
    }

    public <T> T loadFromJson(String fileName, Class<T> type)
        throws ParserException
    {
      return parser.fromJsonFile(fileName, type);
    }

    public <T> T loadFromJson(String fileName, Type type)
        throws ParserException, IOException
    {
      String json = parser.fromFile(fileName);
      return parser.fromJson(json, type);
    }

}