package org.immregistries.smm.transform.procedure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public class AlternativeSpellings extends ProcedureCommon implements ProcedureInterface {

  public enum Field {
                     FIRST_NAME(5, 2),
                     MIDDLE_NAME(5, 3),
                     LAST_NAME(5, 1),
                     MOTHERS_MAIDEN_NAME(6, 1),
                     MOTHERS_FIRST_NAME(6, 2),
                     ADDRESS_STREET(11, 1),
                     ADDRESS_CITY(11, 3);

    int fieldPos;
    int subPos;

    private Field(int fieldPos, int subPos) {
      this.fieldPos = fieldPos;
      this.subPos = subPos;
    }
  }

  private static final Map<String, List<String>> ALTERNATIVE_BOY_FIRST_NAMES = new HashMap<>();
  private static final Map<String, List<String>> ALTERNATIVE_GIRL_FIRST_NAMES = new HashMap<>();
  private static final Map<String, List<String>> ALTERNATIVE_LAST_NAMES = new HashMap<>();
  private static final Map<String, List<String>> ALTERNATIVE_CITIES = new HashMap<>();

  private Transformer transformer;
  private Field field;

  public AlternativeSpellings(Field field) {
    this.field = field;
  }

  @Override
  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {
    List<String[]> fieldsList = readMessage(transformRequest);

    for (String[] fields : fieldsList) {
      String segmentName = fields[0];
      if ("PID".equals(segmentName)) {
        String value = readValue(fields, field.fieldPos, field.subPos);
        value = varyText(value, transformer, field);
        updateValue(value, fields, field.fieldPos, field.subPos);
      }
    }

    putMessageBackTogether(transformRequest, fieldsList);
  }

  protected static String varyText(String name, Transformer transformer, Field field) {
    switch (field) {
      case MOTHERS_FIRST_NAME:
        return pickName(name, transformer, Arrays.asList(ALTERNATIVE_GIRL_FIRST_NAMES));

      case LAST_NAME:
      case MOTHERS_MAIDEN_NAME:
        return pickName(name, transformer, Arrays.asList(ALTERNATIVE_LAST_NAMES));

      case ADDRESS_STREET:
        String originalStreet = getAddressStreetName(name);
        String newString =
            pickName(originalStreet, transformer, Arrays.asList(ALTERNATIVE_LAST_NAMES));
        return capitalizeFirst(replaceAddressStreet(name, newString));

      case ADDRESS_CITY:
        return pickName(name, transformer, Arrays.asList(ALTERNATIVE_CITIES));

      case FIRST_NAME:
      case MIDDLE_NAME:
      default:
        // check to see if the name passed in is in our boy or girl lists
        // in an attempt to keep the same gendered name

        // the reason we always pass in both lists is for a niche case
        // Zowie is on the boys concept list
        // but this class considers Zowie as part of the girl's alt spelling
        // group consisting of zoey, zoe, zoie, and zowie
        // there may be other cases
        List<Map<String, List<String>>> altMaps = new ArrayList<>();
        if (transformer.doesValueExist("BOY", name)) {
          // name was found in the boy concept list, so put that one first
          // but include the girl concept list just in case the name is found there
          altMaps.add(ALTERNATIVE_BOY_FIRST_NAMES);
          altMaps.add(ALTERNATIVE_GIRL_FIRST_NAMES);
        }
        if (transformer.doesValueExist("GIRL", name)) {
          // opposite of above
          altMaps.add(ALTERNATIVE_GIRL_FIRST_NAMES);
          altMaps.add(ALTERNATIVE_BOY_FIRST_NAMES);
        }
        if (altMaps.isEmpty()) {
          // name wasn't found in the concept lists at all
          // so include both, and shuffle them
          // if the name isn't in our alt maps
          // the pickName method will randomly select from the first map passed in
          altMaps.add(ALTERNATIVE_BOY_FIRST_NAMES);
          altMaps.add(ALTERNATIVE_GIRL_FIRST_NAMES);
          Collections.shuffle(altMaps);
        }

        return pickName(name, transformer, altMaps);
    }
  }

  @SuppressWarnings("unchecked")
  private static String pickName(String name, Transformer transformer,
      List<Map<String, List<String>>> nameMaps) {

    Random random = transformer.getRandom();
    boolean upperCase = name.toUpperCase().equals(name);
    boolean lowerCase = name.toLowerCase().equals(name);

    // check if any of the maps passed in have our name
    // need to do this for example a first name we have both boy and girl lists
    Map<String, List<String>> matchingMap = null;
    for (Map<String, List<String>> nameMap : nameMaps) {
      if (nameMap.containsKey(name.toUpperCase())) {
        matchingMap = nameMap;
        break;
      }
    }

    if (matchingMap == null) {
      // always use the first map passed in
      // this is in the case of both boy/girl lists passed in
      // and the current name was found in the first list
      // BUT in the small chance that the current name is actually on the other list
      Map<String, List<String>> nameMap = nameMaps.get(0);

      // then pick a random key from that map
      List<String> alts = (List<String>) nameMap.values().toArray()[random.nextInt(nameMap.size())];

      // finally pick a random alternative name
      return alts.get(random.nextInt(alts.size()));
    } else {
      // got a match, pick a random alternative name
      List<String> nameList = matchingMap.get(name.toUpperCase());
      name = nameList.get(random.nextInt(nameList.size()));
    }

    if (upperCase) {
      name = name.toUpperCase();
    } else if (lowerCase) {
      name = name.toLowerCase();
    }
    return name;
  }

  private static void addBoyFirstName(String... names) {
    addName(ALTERNATIVE_BOY_FIRST_NAMES, names);
  }

  private static void addGirlFirstName(String... names) {
    addName(ALTERNATIVE_GIRL_FIRST_NAMES, names);
  }

  private static void addLastName(String... names) {
    addName(ALTERNATIVE_LAST_NAMES, names);
  }

  private static void addCityName(String... names) {
    addName(ALTERNATIVE_CITIES, names);
  }

  private static void addName(Map<String, List<String>> map, String... names) {
    if (names == null || names.length < 2) {
      return;
    }

    for (int i = 0; i < names.length; i++) {
      String key = names[i].toUpperCase();
      List<String> values = new ArrayList<>();
      for (int j = 0; j < names.length; j++) {
        if (i != j) {
          values.add(names[j]);
        }
      }
      if (map.containsKey(key)) {
        map.get(key).addAll(values);
      } else {
        map.put(key, new ArrayList<>(values));
      }
    }
  }

  static {
    addGirlFirstName("Kaitlyn", "Caitlin", "Katelyn", "Caitlyn");
    addGirlFirstName("Haley", "Haleigh", "Hailey", "Hayley", "Hailie", "Haylee");
    addBoyFirstName("Jaiden", "Jaden", "Jayden", "Jadyn", "Jaeden", "Jaydin");
    addBoyFirstName("Alanna", "Alana", "Alannah", "Alanah", "Alayna", "Elayna");
    addBoyFirstName("Kaden", "Caden", "Kaiden", "Cayden", "Kaeden", "Kadan", "Kadyn");
    addGirlFirstName("Kristin", "Kristen", "Kristyn", "Kirstin");
    addBoyFirstName("Aidan", "Aiden", "Ayden", "Aden");
    addBoyFirstName("Camryn", "Cameron", "Kamryn", "Kamron", "Kameron");
    addGirlFirstName("Jocelyn", "Jocelynn", "Joselyn", "Joslyn");
    addBoyFirstName("Greyson", "Grayson", "Graysen", "Graeson", "Greysonn");
    addGirlFirstName("Emilee", "Emily", "Emmalee", "Emmaleigh");
    addGirlFirstName("Mariella", "Mariela", "Marielle", "Maryella");
    addGirlFirstName("Kiera", "Keira", "Kyra", "Ciara");
    addGirlFirstName("Carleigh", "Carly", "Carlee", "Karlee", "Karlie");
    addGirlFirstName("Destinee", "Destiny", "Destiney");
    addBoyFirstName("Brayden", "Braden", "Braeden", "Bradon", "Braiden");
    addGirlFirstName("Makenzie", "Mackenzie", "Mckenzie", "McKenzie");
    addGirlFirstName("Nataly", "Natalie", "Natalee", "Nathalie");
    addBoyFirstName("Karter", "Carter", "Kartar", "Kharlton");
    addGirlFirstName("Kamila", "Camila", "Camilla", "Kamilla");
    addGirlFirstName("Madalyn", "Madeline", "Madelyn", "Madeleine");
    addBoyFirstName("Zackary", "Zachary", "Zackery", "Zachery");
    addGirlFirstName("Abrielle", "Abriella", "Aubrielle", "Aubriella");
    addGirlFirstName("Cristina", "Christina", "Kristina", "Cristine", "Kristine");
    addBoyFirstName("Braeden", "Braden", "Bradyn", "Brayden");
    addGirlFirstName("Katharine", "Katherine", "Kathryn", "Katarina", "Catherine");
    addBoyFirstName("Landon", "Landen", "Landyn", "Landen");
    addGirlFirstName("Lila", "Lilah", "Lyla", "Lylah");
    addGirlFirstName("Makayla", "Michaela", "Mikayla", "Makaila", "Makaela");
    addGirlFirstName("Rachael", "Rachel", "Racheal", "Rachelle", "Racquel");
    addBoyFirstName("Serenity", "Serenitee", "Serenitey", "Sereniti", "Serenitye");
    addBoyFirstName("Taryn", "Taren", "Tarynn", "Tarin");
    addGirlFirstName("Zoe", "Zoey", "Zoie", "Zowie");

    addGirlFirstName("Brittany", "Britney");
    addBoyFirstName("Greg", "Gregg");
    addGirlFirstName("Sarah", "Sara");
    addBoyFirstName("Steven", "Stephen");
    addGirlFirstName("Lee", "Leigh");
    addGirlFirstName("Noel", "Nowell");
    addBoyFirstName("Saul", "Sol");
    addBoyFirstName("Sean", "Shawn");
    addGirlFirstName("Shea", "Shae");
    addGirlFirstName("Sky", "Skye");
    addBoyFirstName("Ty", "Tie");
    addBoyFirstName("Yael", "Yale");
    addBoyFirstName("Zack", "Zach");
    addGirlFirstName("Elle", "Ell");
    addGirlFirstName("Freya", "Freyja");
    addGirlFirstName("Ginny", "Ginie");
    addBoyFirstName("Isaac", "Isaak");
    addBoyFirstName("Kai", "Ky");
    addBoyFirstName("Liam", "Lyam");
    addGirlFirstName("Maeve", "Maev");
    addBoyFirstName("Rhett", "Rhet");
    addGirlFirstName("Sidney", "Sydney");
    addBoyFirstName("Calvin", "Kalvin");
    addBoyFirstName("Derek", "Derick");
    addGirlFirstName("Evelyn", "Evelin");
    addGirlFirstName("Gael", "Gale");
    addGirlFirstName("Jacqueline", "Jaclyn");
    addGirlFirstName("Karen", "Karin");
    addGirlFirstName("Lina", "Lena");
    addGirlFirstName("Nina", "Nena");
    addGirlFirstName("Paige", "Page");
    addBoyFirstName("Bret", "Brett");
    addGirlFirstName("Dawn", "Don");
    addBoyFirstName("Ethan", "Ethen");
    addBoyFirstName("Gavin", "Gaven");
    addBoyFirstName("Jace", "Jayce");
    addBoyFirstName("Kurt", "Curt");
    addGirlFirstName("Maya", "Mya");
    addBoyFirstName("Nash", "Naash");
    addGirlFirstName("Vicki", "Vicky");
    addBoyFirstName("Wren", "Ren");
    addGirlFirstName("Deborah", "Debra");
    addGirlFirstName("Genevieve", "Genevive");
    addGirlFirstName("Julianne", "Julian");
    addBoyFirstName("Kellen", "Kellan");
    addGirlFirstName("Lucia", "Lucja");
    addGirlFirstName("Margo", "Margot");
    addBoyFirstName("Toby", "Tobey");
    addGirlFirstName("Aria", "Arya");
    addGirlFirstName("Bianca", "Bianka");
    addGirlFirstName("Caroline", "Carolyn");
    addGirlFirstName("Elena", "Elaina");
    addGirlFirstName("Giada", "Jada");
    addGirlFirstName("Jasmine", "Jazmin");
    addBoyFirstName("Kian", "Keon");
    addBoyFirstName("Leif", "Leaf");
    addGirlFirstName("Mavis", "Mavys");
    addGirlFirstName("Sloane", "Sloan");

    addLastName("Smith", "Smyth", "Smythe");
    addLastName("Taylor", "Tayler", "Taylour");
    addLastName("Anderson", "Andersen", "Andersson");
    addLastName("Clark", "Clarke", "Klark");
    addLastName("Davis", "Davies", "Daviss");
    addLastName("Harrison", "Harrisson", "Haryson");
    addLastName("Mitchell", "Mitchel", "Mittel");
    addLastName("Robinson", "Robison", "Robeson", "Robson");
    addLastName("Stewart", "Stuart", "Stuwart", "Steward");
    addLastName("Thompson", "Tompson", "Tomson");
    addLastName("Williams", "Williamson", "Willimson");
    addLastName("Young", "Yung", "Yong");
    addLastName("Bennett", "Bennet", "Bennit");
    addLastName("Graham", "Gram", "Graam");
    addLastName("Nelson", "Nelsen", "Nellson");
    addLastName("Henderson", "Hendrikson", "Hendrson");
    addLastName("MacDonald", "Macdonald", "Mcdonald");
    addLastName("Schmidt", "Schmitt", "Schmid");
    addLastName("Ferguson", "Fergusson", "Fergason");
    addLastName("Johnson", "Johnsen", "Johnsson");
    addLastName("Bowen", "Bouwen");
    addLastName("Cohen", "Koen");
    addLastName("Fletcher", "Flecher");
    addLastName("Hughes", "Hues");
    addLastName("Knight", "Nite");
    addLastName("Lee", "Leigh");
    addLastName("Meyer", "Myer");
    addLastName("Price", "Prize");
    addLastName("Turner", "Terner");
    addLastName("Wright", "Write");
    addLastName("Baker", "Becker");
    addLastName("Coleman", "Kuhlman");
    addLastName("Dyer", "Dire");
    addLastName("Graham", "Gram");
    addLastName("Miller", "Mylar");
    addLastName("Peterson", "Petersen");
    addLastName("Scott", "Skaat");
    addLastName("Wilson", "Wilsson");
    addLastName("Larson", "Larsen");

    addCityName("Pittsburgh", "Pittsburg", "Pitsburgh");
    addCityName("Albuquerque", "Albukerque", "Alburquerque");
    addCityName("Cincinnati", "Cincinatti", "Cincinnatti");
    addCityName("Phoenix", "Pheonix", "Phoneix");
    addCityName("Minneapolis", "Minneaplis", "Minniapolis");
    addCityName("Louisville", "Luisville", "Lousville");
    addCityName("Sacramento", "Sacremento", "Saccramento");
    addCityName("Raleigh", "Raliegh", "Ralegh");
    addCityName("Des Moines", "Des Moins", "Desmoines");
    addCityName("Spokane", "Spokan", "Spoakane");
    addCityName("Philadelphia", "Philidelphia", "Philly");
    addCityName("Baltimore", "Baltimor", "Baltimoore");
    addCityName("Houston", "Huston", "Housten");
    addCityName("Nashville", "Nashvile", "Nashvill");
    addCityName("Charlotte", "Charlote", "Charolotte");
    addCityName("Kansas City", "Kansas Sity", "Kansis City");
    addCityName("Indianapolis", "Indianopolis", "Indiannapolis");
    addCityName("San Diego", "San Deigo", "Sandiego");
    addCityName("Seattle", "Seatlle", "Seattel");
    addCityName("Austin", "Austen", "Auston");
    addCityName("Anchorage", "Anchourage", "Ankorage");
    addCityName("Asheville", "Ashville", "Ashvil");
    addCityName("Boise", "Boisee", "Boisey");
    addCityName("Chattanooga", "Chatanooga", "Chattanoogo");
    addCityName("Eugene", "Eugenne", "Eugen");
    addCityName("Fargo", "Farggo", "Phargo");
    addCityName("Helena", "Helenna", "Helenah");
    addCityName("Laramie", "Laramee", "Laramy");
    addCityName("Missoula", "Misoula", "Misula");
    addCityName("Olympia", "Olimpia", "Olympya");
    addCityName("Appleton", "Appletown");
    addCityName("Kalamazoo", "Kalamaazoo", "Kalamazo");
    addCityName("Provo", "Provoo", "Pravo");
    addCityName("Schenectady", "Schenectadye", "Schenectad");
    addCityName("Tallahassee", "Talahassee", "Tallahasee");
    addCityName("Wichita", "Wichitta", "Wichitta");
    addCityName("Ypsilanti", "Ypsilante", "Ypsilanty");
    addCityName("Zanesville", "Zansville", "Zanessville");
    addCityName("Coeur d'Alene", "Coeur d Alene", "Coeur dalene");
    addCityName("Sioux Falls", "Sioux Fals", "Su Falls");
    addCityName("Mobile", "Mobil", "Moobile");
    addCityName("Poughkeepsie", "Poughkipsee", "Poughkeepsy");
    addCityName("Worcester", "Worchester", "Worster");
    addCityName("Gulfport", "Golfport");
    addCityName("Milwaukee", "Milwaukie");
  }

  @Override
  public void setTransformer(Transformer transformer) {
    this.transformer = transformer;
  }
}
