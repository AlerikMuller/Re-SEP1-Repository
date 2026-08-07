package utility;

import model.TripPlanningCompany;
import parser.ParserException;
import parser.XmlJsonParser;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * A utility class that handles JSON file operations for serializing and deserializing objects.
 * Uses the XmlJsonParser to convert objects to/from JSON format and persist them to files.
 *
 * @author Kelsang Sherpa
 * @version 1.0
 */
public class MyFileHandler
{
    private final XmlJsonParser parser;
  /**
   * No-argument constructor initializing the XmlJsonParser.
   */
    public MyFileHandler()
    {
      parser = new XmlJsonParser();
    }

    /* ---------------- JSON ---------------- */

  /**
   * Store new object data to the configured JSON file.
   * @param fileName the name of the output JSON file.
   * @param object the object to convert
   * @param <T> the type of the object to convert
   * @throws ParserException if any exceptions parsing, transforming, writing or reading
   */
    public <T> void saveToJson(String fileName, T object)
        throws ParserException
    {
      parser.toJsonFile(object, fileName);
    }

  /**
   * loads the company data from the configured JSON file
   * @param fileName the name of the JSON file
   * @param type the Class type of the object being returned
   * @return returns the object created from the JSON file
   * @param <T> the type of the object to return
   * @throws ParserException - if any exceptions occur
   */
    public <T> T loadFromJson(String fileName, Class<T> type)
        throws ParserException
    {
      return parser.fromJsonFile(fileName, type);
    }

  /**
   * Loads an object from a JSON file with a specified generic type.
   * Reads the JSON file and deserializes it to the provided type parameter.
   *
   * @param fileName the name of the JSON file to read
   * @param type the Type of the object to deserialize to
   * @param <T> the type of the object to return
   * @return the object deserialized from the JSON file
   * @throws ParserException if an error occurs during JSON parsing
   * @throws IOException if an error occurs while reading the file
   */
    public <T> T loadFromJson(String fileName, Type type)
        throws ParserException, IOException
    {
      String json = parser.fromFile(fileName);
      return parser.fromJson(json, type);
    }

}