package org.immregistries.smm.transform.procedure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public class AlternativeEndingVowels extends ProcedureCommon implements ProcedureInterface {

  public enum Field {
                     FIRST_NAME(5, 2, false),
                     MIDDLE_NAME(5, 3, false),
                     LAST_NAME(5, 1, false),
                     MOTHERS_MAIDEN_NAME(6, 1, false),
                     MOTHERS_FIRST_NAME(6, 2, false),
                     ADDRESS_STREET(11, 1, false),
                     ADDRESS_CITY(11, 3, false),
                     EMAIL(13, 4, true);

    int fieldPos;
    int subPos;
    boolean repeatedField;

    private Field(int fieldPos, int subPos, boolean repeatedField) {
      this.fieldPos = fieldPos;
      this.subPos = subPos;
      this.repeatedField = repeatedField;
    }
  }

  private static final Map<String, List<String>> ENDINGS =
      new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
  private static final List<String> ALTERNATIVE_BOY_FIRST_NAMES = new ArrayList<>();
  private static final List<String> ALTERNATIVE_GIRL_FIRST_NAMES = new ArrayList<>();
  private static final List<String> ALTERNATIVE_LAST_NAMES = new ArrayList<>();
  private static final List<String> ALTERNATIVE_CITIES = new ArrayList<>();

  private Transformer transformer;
  private Field field;

  public AlternativeEndingVowels(Field field) {
    this.field = field;
  }

  @Override
  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {

    List<String[]> fieldsList = readMessage(transformRequest);

    for (String[] fields : fieldsList) {
      String segmentName = fields[0];
      if ("PID".equals(segmentName)) {
        if (!field.repeatedField) {
          String value = readValue(fields, field.fieldPos, field.subPos);
          value = varyText(value, transformer, field);
          updateValue(value, fields, field.fieldPos, field.subPos);
        } else {
          String[] repeatFields = readRepeats(fields, Field.EMAIL.fieldPos);
          int pos = 0;
          for (String value : repeatFields) {
            if (field == Field.EMAIL) {
              String email = readRepeatValue(value, Field.EMAIL.subPos);
              if (email.indexOf('@') > 0) {
                email = varyText(email, transformer, field);
                updateRepeat(email, repeatFields, pos, Field.EMAIL.subPos);
              }
            }
            pos++;
          }
          String fieldFinal = createRepeatValue(repeatFields);
          updateContent(fieldFinal, fields, Field.EMAIL.fieldPos);
        }
      }
    }

    putMessageBackTogether(transformRequest, fieldsList);
  }

  protected static String varyText(String name, Transformer transformer, Field field) {
    Random random = transformer.getRandom();
    boolean upperCase = name.toUpperCase().equals(name);
    boolean lowerCase = name.toLowerCase().equals(name);

    String originalName = name;
    if (field == Field.ADDRESS_STREET) {
      name = getAddressStreetName(name);
    } else if (field == Field.EMAIL) {
      name = name.substring(0, name.indexOf("@"));
    }

    String before = name;

    // check if name ends with one of the currently supported endings ("ie", ue", etc.)
    // and swap it with one of the matching endings
    // try and match the longer swaps first
    int length = 3;
    while (length > 0 && before.equals(name)) {
      if (name.length() > length && ENDINGS.containsKey(name.substring(name.length() - length))) {
        List<String> options = ENDINGS.get(name.substring(name.length() - length));
        name =
            name.substring(0, name.length() - length) + options.get(random.nextInt(options.size()));
      }
      length--;
    }

    // if the name didn't change, let's convert the name to one of vowel-ending names
    if (before.equals(name)) {
      switch (field) {
        case MOTHERS_FIRST_NAME:
          name =
              ALTERNATIVE_GIRL_FIRST_NAMES.get(random.nextInt(ALTERNATIVE_GIRL_FIRST_NAMES.size()));
          break;

        case LAST_NAME:
        case MOTHERS_MAIDEN_NAME:
        case ADDRESS_STREET:
        case EMAIL:
          name = ALTERNATIVE_LAST_NAMES.get(random.nextInt(ALTERNATIVE_LAST_NAMES.size()));
          break;

        case ADDRESS_CITY:
          name = ALTERNATIVE_CITIES.get(random.nextInt(ALTERNATIVE_CITIES.size()));
          break;

        case FIRST_NAME:
        case MIDDLE_NAME:
        default:
          name = random.nextBoolean()
              ? ALTERNATIVE_GIRL_FIRST_NAMES
                  .get(random.nextInt(ALTERNATIVE_GIRL_FIRST_NAMES.size()))
              : ALTERNATIVE_BOY_FIRST_NAMES.get(random.nextInt(ALTERNATIVE_BOY_FIRST_NAMES.size()));
          break;
      }
    }

    if (field == Field.ADDRESS_STREET) {
      name = capitalizeFirst(replaceAddressStreet(originalName, name));
    } else if (field == Field.EMAIL) {
      name = name.replace(" ", ".").replace("'", ".")
          + originalName.substring(originalName.indexOf("@"));
    }

    if (upperCase) {
      name = name.toUpperCase();
    } else if (lowerCase) {
      name = name.toLowerCase();
    }

    return name;
  }

  private static void addGroup(String... names) {
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
      if (ENDINGS.containsKey(key)) {
        ENDINGS.get(key).addAll(values);
      } else {
        ENDINGS.put(key, new ArrayList<>(values));
      }
    }
  }

  static {
    addGroup("ie", "y", "ey", "ee", "i");
    addGroup("o", "oe", "oh", "ow");
    addGroup("u", "ew", "ue", "eau", "ou", "ieu", "iew");
    addGroup("a", "ah", "uh");

    ALTERNATIVE_GIRL_FIRST_NAMES.add("Haley");
    ALTERNATIVE_GIRL_FIRST_NAMES.add("Hailey");
    ALTERNATIVE_GIRL_FIRST_NAMES.add("Keira");
    ALTERNATIVE_GIRL_FIRST_NAMES.add("Carly");
    ALTERNATIVE_GIRL_FIRST_NAMES.add("Destiny");
    ALTERNATIVE_GIRL_FIRST_NAMES.add("Mackenzie");
    ALTERNATIVE_GIRL_FIRST_NAMES.add("Emily");
    ALTERNATIVE_GIRL_FIRST_NAMES.add("Mariella");
    ALTERNATIVE_GIRL_FIRST_NAMES.add("Camila");
    ALTERNATIVE_GIRL_FIRST_NAMES.add("Abriella");
    ALTERNATIVE_GIRL_FIRST_NAMES.add("Lilah");
    ALTERNATIVE_GIRL_FIRST_NAMES.add("Serenity");
    ALTERNATIVE_GIRL_FIRST_NAMES.add("Zoey");
    ALTERNATIVE_GIRL_FIRST_NAMES.add("Zoe");
    ALTERNATIVE_GIRL_FIRST_NAMES.add("Sarah");
    ALTERNATIVE_GIRL_FIRST_NAMES.add("Freya");
    ALTERNATIVE_GIRL_FIRST_NAMES.add("Sidney");
    ALTERNATIVE_GIRL_FIRST_NAMES.add("Maya");
    ALTERNATIVE_GIRL_FIRST_NAMES.add("Vicki");
    ALTERNATIVE_GIRL_FIRST_NAMES.add("Vicky");
    ALTERNATIVE_GIRL_FIRST_NAMES.add("Margo");
    ALTERNATIVE_GIRL_FIRST_NAMES.add("Ashley");
    ALTERNATIVE_GIRL_FIRST_NAMES.add("Avery");
    ALTERNATIVE_GIRL_FIRST_NAMES.add("Bailey");
    ALTERNATIVE_GIRL_FIRST_NAMES.add("Courtney");
    ALTERNATIVE_GIRL_FIRST_NAMES.add("Indigo");
    ALTERNATIVE_GIRL_FIRST_NAMES.add("Kennedy");
    ALTERNATIVE_GIRL_FIRST_NAMES.add("Leslie");
    ALTERNATIVE_GIRL_FIRST_NAMES.add("Sasha");
    ALTERNATIVE_GIRL_FIRST_NAMES.add("Misha");
    ALTERNATIVE_GIRL_FIRST_NAMES.add("Victoria");
    ALTERNATIVE_GIRL_FIRST_NAMES.add("Julia");

    ALTERNATIVE_BOY_FIRST_NAMES.add("Alejandro");
    ALTERNATIVE_BOY_FIRST_NAMES.add("Alexi");
    ALTERNATIVE_BOY_FIRST_NAMES.add("Alfie");
    ALTERNATIVE_BOY_FIRST_NAMES.add("Alonzo");
    ALTERNATIVE_BOY_FIRST_NAMES.add("Anthony");
    ALTERNATIVE_BOY_FIRST_NAMES.add("Antonio");
    ALTERNATIVE_BOY_FIRST_NAMES.add("Blue");
    ALTERNATIVE_BOY_FIRST_NAMES.add("Brody");
    ALTERNATIVE_BOY_FIRST_NAMES.add("Casey");
    ALTERNATIVE_BOY_FIRST_NAMES.add("Cassidy");
    ALTERNATIVE_BOY_FIRST_NAMES.add("Charlie");
    ALTERNATIVE_BOY_FIRST_NAMES.add("Cody");
    ALTERNATIVE_BOY_FIRST_NAMES.add("Cory");
    ALTERNATIVE_BOY_FIRST_NAMES.add("Desi");
    ALTERNATIVE_BOY_FIRST_NAMES.add("Demitri");
    ALTERNATIVE_BOY_FIRST_NAMES.add("Ezra");
    ALTERNATIVE_BOY_FIRST_NAMES.add("Finley");
    ALTERNATIVE_BOY_FIRST_NAMES.add("Gillespie");
    ALTERNATIVE_BOY_FIRST_NAMES.add("Harley");
    ALTERNATIVE_BOY_FIRST_NAMES.add("Henry");
    ALTERNATIVE_BOY_FIRST_NAMES.add("Ichiro");
    ALTERNATIVE_BOY_FIRST_NAMES.add("Marco");
    ALTERNATIVE_BOY_FIRST_NAMES.add("Marley");
    ALTERNATIVE_BOY_FIRST_NAMES.add("Monroe");
    ALTERNATIVE_BOY_FIRST_NAMES.add("Drew");
    ALTERNATIVE_BOY_FIRST_NAMES.add("Elijah");
    ALTERNATIVE_BOY_FIRST_NAMES.add("Micah");
    ALTERNATIVE_BOY_FIRST_NAMES.add("Shiloh");
    ALTERNATIVE_BOY_FIRST_NAMES.add("Andrew");
    ALTERNATIVE_BOY_FIRST_NAMES.add("Zachary");
    ALTERNATIVE_BOY_FIRST_NAMES.add("Wesley");
    ALTERNATIVE_BOY_FIRST_NAMES.add("Timothy");
    ALTERNATIVE_BOY_FIRST_NAMES.add("Stanley");
    ALTERNATIVE_BOY_FIRST_NAMES.add("Rocco");
    ALTERNATIVE_BOY_FIRST_NAMES.add("Percy");

    ALTERNATIVE_LAST_NAMES.add("Silva");
    ALTERNATIVE_LAST_NAMES.add("Costa");
    ALTERNATIVE_LAST_NAMES.add("Carvalho");
    ALTERNATIVE_LAST_NAMES.add("Riley");
    ALTERNATIVE_LAST_NAMES.add("Russo");
    ALTERNATIVE_LAST_NAMES.add("Lima");
    ALTERNATIVE_LAST_NAMES.add("Ribeiro");
    ALTERNATIVE_LAST_NAMES.add("Barbosa");
    ALTERNATIVE_LAST_NAMES.add("Crew");
    ALTERNATIVE_LAST_NAMES.add("Doe");
    ALTERNATIVE_LAST_NAMES.add("Bo");
    ALTERNATIVE_LAST_NAMES.add("Costello");
    ALTERNATIVE_LAST_NAMES.add("Ali");
    ALTERNATIVE_LAST_NAMES.add("De Barra");
    ALTERNATIVE_LAST_NAMES.add("Donohue");
    ALTERNATIVE_LAST_NAMES.add("Lee");
    ALTERNATIVE_LAST_NAMES.add("O'Shea");
    ALTERNATIVE_LAST_NAMES.add("O'Hara");
    ALTERNATIVE_LAST_NAMES.add("Dimitriou");
    ALTERNATIVE_LAST_NAMES.add("Vega");
    ALTERNATIVE_LAST_NAMES.add("Miyamoto");
    ALTERNATIVE_LAST_NAMES.add("Ayano");
    ALTERNATIVE_LAST_NAMES.add("Fujiwara");
    ALTERNATIVE_LAST_NAMES.add("Famitsu");
    ALTERNATIVE_LAST_NAMES.add("Zhu");
    ALTERNATIVE_LAST_NAMES.add("Luo");

    ALTERNATIVE_CITIES.add("Milwaukee");
    ALTERNATIVE_CITIES.add("Atlanta");
    ALTERNATIVE_CITIES.add("Wausau");
    ALTERNATIVE_CITIES.add("Sparta");
    ALTERNATIVE_CITIES.add("Alcona");
    ALTERNATIVE_CITIES.add("Mio");
    ALTERNATIVE_CITIES.add("Leslie");
    ALTERNATIVE_CITIES.add("Waterloo");
    ALTERNATIVE_CITIES.add("Bellevue");
    ALTERNATIVE_CITIES.add("Laramie");
    ALTERNATIVE_CITIES.add("Haysi");
    ALTERNATIVE_CITIES.add("Guthrie");
    ALTERNATIVE_CITIES.add("Fairview");
    ALTERNATIVE_CITIES.add("Tuscaloosa");
    ALTERNATIVE_CITIES.add("Mamou");
    ALTERNATIVE_CITIES.add("Twitty");
    ALTERNATIVE_CITIES.add("Vici");
    ALTERNATIVE_CITIES.add("Seeley");
    ALTERNATIVE_CITIES.add("Dixie");
    ALTERNATIVE_CITIES.add("Boise");
    ALTERNATIVE_CITIES.add("Reno");
    ALTERNATIVE_CITIES.add("Mettah");
    ALTERNATIVE_CITIES.add("Sandy");
    ALTERNATIVE_CITIES.add("Monroe");
    ALTERNATIVE_CITIES.add("San Francisco");
    ALTERNATIVE_CITIES.add("Olympia");
    ALTERNATIVE_CITIES.add("Fresno");
    ALTERNATIVE_CITIES.add("San Luis Obispo");
    ALTERNATIVE_CITIES.add("Maricopa");
    ALTERNATIVE_CITIES.add("Miami");
    ALTERNATIVE_CITIES.add("Lake Mary");
    ALTERNATIVE_CITIES.add("Savannah");
    ALTERNATIVE_CITIES.add("Albuquerque");
    ALTERNATIVE_CITIES.add("Beattie");
    ALTERNATIVE_CITIES.add("Berkeley");
    ALTERNATIVE_CITIES.add("Brinkley");
    ALTERNATIVE_CITIES.add("Cincinnati");
    ALTERNATIVE_CITIES.add("Fortescue");
    ALTERNATIVE_CITIES.add("Hershey");
    ALTERNATIVE_CITIES.add("Huntley");
    ALTERNATIVE_CITIES.add("Montgomery");
    ALTERNATIVE_CITIES.add("Punxsutawney");
  }

  @Override
  public void setTransformer(Transformer transformer) {
    this.transformer = transformer;
  }
}
