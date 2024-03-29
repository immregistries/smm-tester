package org.immregistries.smm.transform.procedure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public class FirstNameConvertNickname extends ProcedureCommon implements ProcedureInterface {

  public static final double NICKNAME_FILE_FREQUENCY_THRESHOLD = 0.5;

  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {

    List<String[]> fieldsList = readMessage(transformRequest);
    for (String[] fields : fieldsList) {
      String segmentName = fields[0];
      if ("PID".equals(segmentName)) {
        int fieldPos = 5;
        int subPos = 2;
        String firstName = readValue(fields, fieldPos, subPos);
        firstName = varyText(firstName, transformer);
        updateValue(firstName, fields, fieldPos, subPos);
      }
    }

    putMessageBackTogether(transformRequest, fieldsList);
  }

  protected static String varyText(String firstName, Transformer transformer) {
    return nameToNicknameMap(firstName, transformer);
  }

  // nicknames from https://github.com/immregistries-internal/AART/issues/2482
  protected static String nameToNicknameMap(String name, Transformer transformer) {
    Random random = transformer.getRandom();
    List<String> nameList = nickNamesToNamesListMap.get(name.toUpperCase());
    boolean upperCase = name.toUpperCase().equals(name);
    boolean lowerCase = name.toLowerCase().equals(name);

    if (nameList == null) {
      // this is not a nickname, maybe it's a name that has a nickname
      List<String> nickNameList = namesToNickNameListMap.get(name.toUpperCase());
      if (nickNameList == null) {
        // this is not a name either, pick a random name that a nickname maps to
        Object[] keySet = namesToNickNameListMap.keySet().toArray();
        String key = (String) keySet[random.nextInt(keySet.length)];
        List<String> valueList = namesToNickNameListMap.get(key);
        name = valueList.get(random.nextInt(valueList.size()));
      } else {
        // Picking a random nickname
        name = nickNameList.get(random.nextInt(nickNameList.size()));
      }
    } else {
      // picking a random name that this nickname could come from
      name = nameList.get(random.nextInt(nameList.size()));
    }

    if (upperCase) {
      name = name.toUpperCase();
    } else if (lowerCase) {
      name = name.toLowerCase();
    }
    return name;
  }

  private static Map<String, List<String>> namesToNickNameListMap = new HashMap<>();
  private static Map<String, List<String>> nickNamesToNamesListMap = new HashMap<>();

  private static void addNameAndNickName(String name, String nickName) {
    List<String> nickNameList = namesToNickNameListMap.get(name.toUpperCase());
    if (nickNameList == null) {
      nickNameList = new ArrayList<>();
      namesToNickNameListMap.put(name.toUpperCase(), nickNameList);
    }
    nickNameList.add(nickName);

    List<String> nameList = nickNamesToNamesListMap.get(nickName.toUpperCase());
    if (nameList == null) {
      nameList = new ArrayList<>();
      nickNamesToNamesListMap.put(nickName.toUpperCase(), nameList);
    }
    nameList.add(name);
  }

  static {
    loadFileNicknames();

    addNameAndNickName("Alexander", "Alex");
    addNameAndNickName("Alexander", "Xander");
    addNameAndNickName("Benjamin", "Ben");
    addNameAndNickName("Benjamin", "Benny");
    addNameAndNickName("Charles", "Charlie");
    addNameAndNickName("Charles", "Chuck");
    addNameAndNickName("Christopher", "Chris");
    addNameAndNickName("Christopher", "Topher");
    addNameAndNickName("Daniel", "Dan");
    addNameAndNickName("Daniel", "Danny");
    addNameAndNickName("David", "Dave");
    addNameAndNickName("David", "Davy");
    addNameAndNickName("Edward", "Ed");
    addNameAndNickName("Edward", "Eddie");
    addNameAndNickName("Elizabeth", "Liz");
    addNameAndNickName("Elizabeth", "Lizzie");
    addNameAndNickName("Elizabeth", "Beth");
    addNameAndNickName("Emily", "Em");
    addNameAndNickName("Emily", "Emmy");
    addNameAndNickName("George", "Geo");
    addNameAndNickName("George", "Georgie");
    addNameAndNickName("Henry", "Hank");
    addNameAndNickName("Henry", "Harry");
    addNameAndNickName("Isabella", "Izzy");
    addNameAndNickName("Isabella", "Bella");
    addNameAndNickName("Jack", "Jackson");
    addNameAndNickName("Jack", "Jax");
    addNameAndNickName("James", "Jim");
    addNameAndNickName("James", "Jimmy");
    addNameAndNickName("John", "Jack");
    addNameAndNickName("John", "Johnny");
    addNameAndNickName("Katherine", "Kate");
    addNameAndNickName("Katherine", "Katie");
    addNameAndNickName("Margaret", "Maggie");
    addNameAndNickName("Margaret", "Peggy");
    addNameAndNickName("Michael", "Mike");
    addNameAndNickName("Michael", "Mikey");
    addNameAndNickName("Nicholas", "Nick");
    addNameAndNickName("Nicholas", "Nicky");
    addNameAndNickName("Olivia", "Liv");
    addNameAndNickName("Olivia", "Livvy");
    addNameAndNickName("Richard", "Rich");
    addNameAndNickName("Richard", "Rick");
    addNameAndNickName("Richard", "Ricky");
    addNameAndNickName("Richard", "Dick");
    addNameAndNickName("Robert", "Rob");
    addNameAndNickName("Robert", "Robbie");
    addNameAndNickName("Robert", "Bob");
    addNameAndNickName("Robert", "Bobby");
    addNameAndNickName("Samantha", "Sam");
    addNameAndNickName("Samantha", "Sammy");
    addNameAndNickName("Sarah", "Sar");
    addNameAndNickName("Sarah", "Sally");
    addNameAndNickName("Thomas", "Tom");
    addNameAndNickName("Thomas", "Tommy");
    addNameAndNickName("William", "Will");
    addNameAndNickName("William", "Willie");
    addNameAndNickName("William", "Bill");
    addNameAndNickName("William", "Billy");
    addNameAndNickName("Alexandra", "Alex");
    addNameAndNickName("Alexandra", "Lexi");
    addNameAndNickName("Andrew", "Andy");
    addNameAndNickName("Andrew", "Drew");
    addNameAndNickName("Annabelle", "Anna");
    addNameAndNickName("Annabelle", "Belle");
    addNameAndNickName("Anthony", "Tony");
    addNameAndNickName("Anthony", "Ant");
    addNameAndNickName("Audrey", "Audie");
    addNameAndNickName("Audrey", "DeeDee");
    addNameAndNickName("Caroline", "Carol");
    addNameAndNickName("Caroline", "Carrie");
    addNameAndNickName("Catherine", "Cathy");
    addNameAndNickName("Catherine", "Cat");
    addNameAndNickName("Charlotte", "Charlie");
    addNameAndNickName("Charlotte", "Lottie");
    addNameAndNickName("Christian", "Chris");
    addNameAndNickName("Christian", "Kit");
    addNameAndNickName("Claire", "Clara");
    addNameAndNickName("Claire", "Clare");
    addNameAndNickName("Daniel", "Dan");
    addNameAndNickName("Daniel", "Danny");
    addNameAndNickName("Eleanor", "Ellie");
    addNameAndNickName("Eleanor", "Nellie");
    addNameAndNickName("Emma", "Em");
    addNameAndNickName("Emma", "Emmy");
    addNameAndNickName("Francesca", "Fran");
    addNameAndNickName("Francesca", "Frankie");
    addNameAndNickName("Frederick", "Fred");
    addNameAndNickName("Frederick", "Freddie");
    addNameAndNickName("Grace", "Gracie");
    addNameAndNickName("Grace", "Gray");
    addNameAndNickName("Harrison", "Harry");
    addNameAndNickName("Harrison", "Harris");
    addNameAndNickName("Isabelle", "Izzy");
    addNameAndNickName("Isabelle", "Belle");
    addNameAndNickName("Jacob", "Jake");
    addNameAndNickName("Jacob", "Jay");
    addNameAndNickName("Joseph", "Joe");
    addNameAndNickName("Joseph", "Joey");
    addNameAndNickName("Katherine", "Kate");
    addNameAndNickName("Katherine", "Katie");
    addNameAndNickName("Leonardo", "Leo");
    addNameAndNickName("Leonardo", "Lenny");
    addNameAndNickName("Lily", "Lil");
    addNameAndNickName("Lily", "Lils");
    addNameAndNickName("Madison", "Maddie");
    addNameAndNickName("Madison", "Mads");
    addNameAndNickName("Matthew", "Matt");
    addNameAndNickName("Matthew", "Matty");
    addNameAndNickName("Nathaniel", "Nate");
    addNameAndNickName("Nathaniel", "Nathan");
    addNameAndNickName("Penelope", "Penny");
    addNameAndNickName("Penelope", "Nell");
    addNameAndNickName("Rose", "Rosie");
    addNameAndNickName("Rose", "Rosa");
    addNameAndNickName("Samuel", "Sam");
    addNameAndNickName("Samuel", "Sammy");
    addNameAndNickName("Sophia", "Sophie");
    addNameAndNickName("Sophia", "Soph");
    addNameAndNickName("Theodore", "Theo");
    addNameAndNickName("Theodore", "Ted");
    addNameAndNickName("Victoria", "Vicky");
    addNameAndNickName("Victoria", "Tori");
    addNameAndNickName("Zachary", "Zack");
    addNameAndNickName("Zachary", "Zachie");
    addNameAndNickName("Alejandro", "Alex");
    addNameAndNickName("Carolina", "Caro");
    addNameAndNickName("Eduardo", "Eddy");
    addNameAndNickName("Francisco", "Frank");
    addNameAndNickName("Francisco", "Paco");
    addNameAndNickName("Gabriela", "Gabby");
    addNameAndNickName("Guillermo", "Will");
    addNameAndNickName("Guillermo", "Memo");
    addNameAndNickName("Isabella", "Izzy");
    addNameAndNickName("Isabella", "Bella");
    addNameAndNickName("Javier", "Javi");
    addNameAndNickName("Juan", "Johnny");
    addNameAndNickName("Luis", "Louie");
    addNameAndNickName("Maria", "Mari");
    addNameAndNickName("Miguel", "Mike");
    addNameAndNickName("Roberto", "Rob");
    addNameAndNickName("Roberto", "Bobby");
    addNameAndNickName("Adriana", "Adri");
    addNameAndNickName("Adriana", "Ana");
    addNameAndNickName("Ana", "Ani");
    addNameAndNickName("Ana", "Annie");
    addNameAndNickName("Antonio", "Tony");
    addNameAndNickName("Antonio", "Tonio");
    addNameAndNickName("Carlos", "Carlitos");
    addNameAndNickName("Carlos", "Charlie");
    addNameAndNickName("Carmen", "Car");
    addNameAndNickName("Carmen", "Carm");
    addNameAndNickName("Diego", "Di");
    addNameAndNickName("Esteban", "Steve");
    addNameAndNickName("Esteban", "Esti");
    addNameAndNickName("Fernando", "Fern");
    addNameAndNickName("Fernando", "Nando");
    addNameAndNickName("Giselle", "Gigi");
    addNameAndNickName("Giselle", "Elle");
    addNameAndNickName("Ignacio", "Nacho");
    addNameAndNickName("Ignacio", "Iggy");
    addNameAndNickName("Javier", "Javi");
    addNameAndNickName("Jose", "Joe");
    addNameAndNickName("Jose", "Pepe");
    addNameAndNickName("Juanita", "Juan");
    addNameAndNickName("Juanita", "Nita");
    addNameAndNickName("Julio", "Jules");
    addNameAndNickName("Julio", "July");
    addNameAndNickName("Marisol", "Mari");
    addNameAndNickName("Marisol", "Sol");
    addNameAndNickName("Roberto", "Rob");
    addNameAndNickName("Roberto", "Raul");
    addNameAndNickName("Salvador", "Sal");
    addNameAndNickName("Salvador", "Vador");
    addNameAndNickName("Santiago", "Santi");
    addNameAndNickName("Santiago", "Tiago");
    addNameAndNickName("Veronica", "Vero");
    addNameAndNickName("Veronica", "Ronnie");
    addNameAndNickName("Ximena", "Xime");
    addNameAndNickName("Ximena", "Mena");
    addNameAndNickName("Yvette", "Eve");
    addNameAndNickName("Yvette", "Vette");
    addNameAndNickName("Antoinette", "Toni");
    addNameAndNickName("Brandon", "Bran");
    addNameAndNickName("DeShawn", "Shawn");
    addNameAndNickName("Dominique", "Dom");
    addNameAndNickName("Jasmine", "Jazz");
    addNameAndNickName("Keisha", "Kiki");
    addNameAndNickName("LaToya", "Toya");
    addNameAndNickName("Malik", "Mal");
    addNameAndNickName("Maurice", "Moe");
    addNameAndNickName("Monique", "Mo");
    addNameAndNickName("Shaquille", "Shaq");
    addNameAndNickName("Tyrone", "Ty");
    addNameAndNickName("Aaliyah", "Liya");
    addNameAndNickName("Jamal", "Mal");
    addNameAndNickName("Darnell", "Darn");
    addNameAndNickName("Shantel", "Shanny");
    addNameAndNickName("Tyrone", "Ty");
    addNameAndNickName("Tyrone", "Ron");
    addNameAndNickName("Deon", "DJ");
    addNameAndNickName("Lashonda", "Shaunda");
    addNameAndNickName("Tameka", "Meka");
    addNameAndNickName("Tyrell", "Ty");
    addNameAndNickName("Tyrell", "Rell");
    addNameAndNickName("Shamika", "Mika");
    addNameAndNickName("Shamika", "Shami");
    addNameAndNickName("Darius", "D");
    addNameAndNickName("Darius", "Rius");
    addNameAndNickName("Dionne", "Dee");
    addNameAndNickName("Dionne", "Nene");
    addNameAndNickName("Jabari", "Bari");
    addNameAndNickName("Tamika", "Mika");
    addNameAndNickName("Aaliyah", "Aliyah");
    addNameAndNickName("Aaliyah", "Liyah");
    addNameAndNickName("Ahmad", "Ah");
    addNameAndNickName("Ahmad", "Madi");
    addNameAndNickName("Aniyah", "Niya");
    addNameAndNickName("Aniyah", "Niyah");
    addNameAndNickName("Brandon", "Bran");
    addNameAndNickName("Brandon", "B-Dog");
    addNameAndNickName("Cameron", "Cam");
    addNameAndNickName("Cameron", "Cammy");
    addNameAndNickName("DeShawn", "Shawn");
    addNameAndNickName("DeShawn", "Des");
    addNameAndNickName("Ebony", "Ebby");
    addNameAndNickName("Jada", "Jay");
    addNameAndNickName("Jamal", "Jay");
    addNameAndNickName("Jamal", "Mal");
    addNameAndNickName("Jasmine", "Jaz");
    addNameAndNickName("Jasmine", "Jazzy");
    addNameAndNickName("Javon", "Jay");
    addNameAndNickName("Kiana", "Kiki");
    addNameAndNickName("Kiana", "Ki");
    addNameAndNickName("Malik", "Mal");
    addNameAndNickName("Malik", "Kiki");
    addNameAndNickName("Monique", "Mo");
    addNameAndNickName("Monique", "Nique");
    addNameAndNickName("Rashad", "Rash");
    addNameAndNickName("Rashad", "Shad");
    addNameAndNickName("Shanice", "Shan");
    addNameAndNickName("Shanice", "Nisi");
    addNameAndNickName("Tariq", "Tar");
    addNameAndNickName("Tayshaun", "Tay");
    addNameAndNickName("Tayshaun", "Sha");
    addNameAndNickName("Jia", "Jenny");
    addNameAndNickName("Jin", "Jen");
    addNameAndNickName("Jun", "June");
    addNameAndNickName("Min", "Minnie");
    addNameAndNickName("Seung", "Sean");
    addNameAndNickName("Soo", "Sue");
    addNameAndNickName("Wei", "Wayne");
    addNameAndNickName("Cheng", "Charlie");
    addNameAndNickName("Cheng", "Chengy");
    addNameAndNickName("Fang", "Fanny");
    addNameAndNickName("Fang", "Fangy");
    addNameAndNickName("Hong", "Hon");
    addNameAndNickName("Hong", "Hongy");
    addNameAndNickName("Jian", "Jay");
    addNameAndNickName("Jian", "Jiany");
    addNameAndNickName("Jing", "Jin");
    addNameAndNickName("Jing", "Jingy");
    addNameAndNickName("Kai", "Kai-Kai");
    addNameAndNickName("Lei", "Lee");
    addNameAndNickName("Lei", "Leiy");
    addNameAndNickName("Li", "Lily");
    addNameAndNickName("Li", "Li-Li");
    addNameAndNickName("Mei", "May");
    addNameAndNickName("Mei", "Mei-Mei");
    addNameAndNickName("Ming", "Mingy");
    addNameAndNickName("Ming", "Mingo");
    addNameAndNickName("Shan", "Sean");
    addNameAndNickName("Shan", "Shany");
    addNameAndNickName("Wei", "Wayne");
    addNameAndNickName("Wei", "Weiy");
    addNameAndNickName("Xiao", "Shao");
    addNameAndNickName("Xiao", "Xiaoy");
    addNameAndNickName("Xiu", "Sue");
    addNameAndNickName("Xiu", "Xiuy");
    addNameAndNickName("Yi", "Yiyi");
    addNameAndNickName("Yu", "Yuyu");
    addNameAndNickName("Yu", "Yulie");
    addNameAndNickName("Zhi", "Zhizhi");
    addNameAndNickName("Akira", "Aki");
    addNameAndNickName("Asuka", "Asu");
    addNameAndNickName("Asuka", "Suki");
    addNameAndNickName("Emi", "Em");
    addNameAndNickName("Haruki", "Haru");
    addNameAndNickName("Haruki", "Ruki");
    addNameAndNickName("Hikaru", "Hika");
    addNameAndNickName("Hikaru", "Karu");
    addNameAndNickName("Jun", "June");
    addNameAndNickName("Jun", "Juno");
    addNameAndNickName("Kai", "Kai-Kun");
    addNameAndNickName("Kai", "Kaito");
    addNameAndNickName("Kaito", "Kai");
    addNameAndNickName("Kaito", "Kait");
    addNameAndNickName("Kana", "Kanny");
    addNameAndNickName("Kana", "Kanakana");
    addNameAndNickName("Kento", "Ken");
    addNameAndNickName("Kento", "Tito");
    addNameAndNickName("Maki", "Macky");
    addNameAndNickName("Maki", "Maki-Maki");
    addNameAndNickName("Ren", "Ren-Ren");
    addNameAndNickName("Ren", "Renny");
    addNameAndNickName("Rina", "Riri");
    addNameAndNickName("Rina", "Rin");
    addNameAndNickName("Saki", "Sak");
    addNameAndNickName("Saki", "Saki-Saki");
    addNameAndNickName("Satoshi", "Sato");
    addNameAndNickName("Satoshi", "Toshi");
    addNameAndNickName("Yuna", "Yun");
    addNameAndNickName("Yuna", "Yuyu");
    addNameAndNickName("Yuto", "Yu");
    addNameAndNickName("Yuto", "Toto");
    addNameAndNickName("Bong", "Bongy");
    addNameAndNickName("Chul", "Chu");
    addNameAndNickName("Chul", "Chuly");
    addNameAndNickName("Eun", "Euny");
    addNameAndNickName("Eun", "Eun-Eun");
    addNameAndNickName("Hee", "Heey");
    addNameAndNickName("Hyun", "Hyuny");
    addNameAndNickName("Jae", "Jay");
    addNameAndNickName("Jae", "Jae-Jae");
    addNameAndNickName("Jin", "Jiny");
    addNameAndNickName("Joon", "June");
    addNameAndNickName("Joon", "Joon-Joon");
    addNameAndNickName("Min", "Minny");
    addNameAndNickName("Seo-Yeon", "Seo");
    addNameAndNickName("Seo-Yeon", "Yeon-Yeon");
    addNameAndNickName("Seung", "Sean");
    addNameAndNickName("Seung", "Seungy");
    addNameAndNickName("Soo", "Sue");
    addNameAndNickName("Soo", "Soo-Soo");
    addNameAndNickName("Sun", "Sunny");
    addNameAndNickName("Sun", "Sun-Sun");
    addNameAndNickName("Tae", "Tay");
    addNameAndNickName("Tae", "Tae-Tae");
    addNameAndNickName("Yoo-Jung", "Yoo");
    addNameAndNickName("Yoo-Jung", "Yoo-Yoo");
    addNameAndNickName("Young", "Youngy");
    addNameAndNickName("Ahmed", "Ahmad");
    addNameAndNickName("Farah", "Fara");
    addNameAndNickName("Hasan", "Hassan");
    addNameAndNickName("Khadija", "Khadi");
    addNameAndNickName("Mohamed", "Mohamad");
    addNameAndNickName("Mohamed", "Mo");
    addNameAndNickName("Omar", "Omer");
    addNameAndNickName("Rania", "Ran");
    addNameAndNickName("Samir", "Sam");
    addNameAndNickName("Ahmed", "Ahmad");
    addNameAndNickName("Ahmed", "Amed");
    addNameAndNickName("Ali", "Al");
    addNameAndNickName("Ali", "Alie");
    addNameAndNickName("Amira", "Amir");
    addNameAndNickName("Amira", "Mira");
    addNameAndNickName("Fatima", "Fati");
    addNameAndNickName("Fatima", "Tima");
    addNameAndNickName("Habib", "Hab");
    addNameAndNickName("Habib", "Habibi");
    addNameAndNickName("Hasan", "Has");
    addNameAndNickName("Hasan", "Hasanu");
    addNameAndNickName("Hisham", "Hish");
    addNameAndNickName("Hisham", "Hishami");
    addNameAndNickName("Khalid", "Khal");
    addNameAndNickName("Khalid", "Khaled");
    addNameAndNickName("Layla", "Lay");
    addNameAndNickName("Layla", "Laila-Loo");
    addNameAndNickName("Mahmoud", "Mah");
    addNameAndNickName("Mahmoud", "Mahmoudi");
    addNameAndNickName("Mariam", "Mari");
    addNameAndNickName("Mariam", "Mimi");
    addNameAndNickName("Mohamed", "Mo");
    addNameAndNickName("Mohamed", "Mohammedi");
    addNameAndNickName("Nour", "Noor");
    addNameAndNickName("Nour", "Nouri");
    addNameAndNickName("Omar", "O");
    addNameAndNickName("Omar", "Omy");
    addNameAndNickName("Rania", "Ran");
    addNameAndNickName("Rania", "Riri");
    addNameAndNickName("Yusuf", "Youssefi");
    addNameAndNickName("Amir", "Am");
    addNameAndNickName("Amir", "Amiri");
    addNameAndNickName("Arash", "Ash");
    addNameAndNickName("Arash", "Arashi");
    addNameAndNickName("Bahram", "Bah");
    addNameAndNickName("Bahram", "Bahrami");
    addNameAndNickName("Behzad", "Behz");
    addNameAndNickName("Behzad", "Behzi");
    addNameAndNickName("Dariush", "Dar");
    addNameAndNickName("Dariush", "Darishi");
    addNameAndNickName("Farhad", "Far");
    addNameAndNickName("Farhad", "Farhadi");
    addNameAndNickName("Golshan", "Goli");
    addNameAndNickName("Golshan", "Gol-Gol");
    addNameAndNickName("Hamed", "Ham");
    addNameAndNickName("Hamed", "Hami");
    addNameAndNickName("Kamran", "Kam");
    addNameAndNickName("Kamran", "Kamrani");
    addNameAndNickName("Leila", "Lei");
    addNameAndNickName("Leila", "Lili");
    addNameAndNickName("Mehran", "Mehr");
    addNameAndNickName("Mehran", "Mehrani");
    addNameAndNickName("Parisa", "Pari");
    addNameAndNickName("Parisa", "Pari-Pari");
    addNameAndNickName("Reza", "Rez");
    addNameAndNickName("Reza", "Rezi");
    addNameAndNickName("Shahrzad", "Shaz");
    addNameAndNickName("Shahrzad", "Shazzy");
    addNameAndNickName("Aysu", "Ay");
    addNameAndNickName("Aysu", "Aysuu");
    addNameAndNickName("Can", "Canny");
    addNameAndNickName("Can", "Can-Can");
    addNameAndNickName("Deniz", "Den");
    addNameAndNickName("Deniz", "Denzy");
    addNameAndNickName("Emre", "Em");
    addNameAndNickName("Emre", "Emy");
    addNameAndNickName("Gizem", "Giz");
    addNameAndNickName("Gizem", "Gizzie");
    addNameAndNickName("Kemal", "Kem");
    addNameAndNickName("Kemal", "Kemi");
    addNameAndNickName("Leyla", "Ley");
    addNameAndNickName("Leyla", "Lulu");
    addNameAndNickName("Mehmet", "Mem");
    addNameAndNickName("Mehmet", "Mehmedi");
    addNameAndNickName("Nesrin", "Nes");
    addNameAndNickName("Nesrin", "Nes-Nes");
    addNameAndNickName("Ozgur", "Oz");
    addNameAndNickName("Ozgur", "Ozzy");
    addNameAndNickName("Serkan", "Ser");
    addNameAndNickName("Serkan", "Serki");
    addNameAndNickName("Tuncay", "Tun");
    addNameAndNickName("Tuncay", "Tuni");
    addNameAndNickName("Umut", "Um");
    addNameAndNickName("Umut", "Umy");
    addNameAndNickName("Yusuf", "Yusufoglu");
  }

  public static void loadFileNicknames() {
    try (BufferedReader br = new BufferedReader(new InputStreamReader(
        FirstNameConvertNickname.class.getResourceAsStream(("/nicknames.csv"))))) {
      
      // skip header
      br.readLine();
      
      String line;
      while ((line = br.readLine()) != null) {
        String[] parts = line.split("\\,");

        double frequency = Double.parseDouble(parts[3]);
        if (frequency >= NICKNAME_FILE_FREQUENCY_THRESHOLD) {
          String name = parts[0].substring(0, 1) + parts[0].substring(1).toLowerCase();
          String nickname = parts[1].substring(0, 1) + parts[1].substring(1).toLowerCase();
          addNameAndNickName(name, nickname);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {

  }

  private Transformer transformer;

  public void setTransformer(Transformer transformer) {
    this.transformer = transformer;
  }
}
