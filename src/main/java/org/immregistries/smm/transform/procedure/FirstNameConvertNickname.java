package org.immregistries.smm.transform.procedure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public class FirstNameConvertNickname extends ProcedureCommon implements ProcedureInterface {



  public FirstNameConvertNickname() {

  }

  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {
    List<String[]> fieldsList = readMessage(transformRequest);
    {
      for (String[] fields : fieldsList) {
        String segmentName = fields[0];
        if (segmentName.equals("PID")) {
          int fieldPos = 5;
          int subPos = 2;
          String firstName = readValue(fields, fieldPos, subPos);
          firstName = nameToNicknameMap(firstName, transformer);
          updateValue(firstName, fields, fieldPos, subPos);
        }

      }
    }
    putMessageBackTogether(transformRequest, fieldsList);
  }

  // Pulled common nicknames found here: https://www.familiesunearthed.com/nicknames.htm
  //  + Removed nicknames that show up as names
  //  + Every name can have more then one nickname.
  //  + Every nickname can have more than one name that maps to it. 

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
    {
      List<String> nickNameList = namesToNickNameListMap.get(name.toUpperCase());
      if (nickNameList == null) {
        nickNameList = new ArrayList<>();
        namesToNickNameListMap.put(name.toUpperCase(), nickNameList);
      }
      nickNameList.add(nickName);
    }
    {
      List<String> nameList = nickNamesToNamesListMap.get(nickName.toUpperCase());
      if (nameList == null) {
        nameList = new ArrayList<>();
        nickNamesToNamesListMap.put(nickName.toUpperCase(), nameList);
      }
      nameList.add(name);
    }
  }

  static {
    addNameAndNickName("Abram", "Abe");
    addNameAndNickName("Abram", "Aby");
    addNameAndNickName("Albert", "Bert");
    addNameAndNickName("Albert", "Bertie");
    addNameAndNickName("Alicia", "Sissie");
    addNameAndNickName("Alicia", "Ailie");
    addNameAndNickName("Alicia", "Allie");
    addNameAndNickName("Alicia", "Ally");
    addNameAndNickName("Alicia", "Cissie");
    addNameAndNickName("Alicia", "Elsie");
    addNameAndNickName("Alison", "Elsie");
    addNameAndNickName("Allison", "Elsie");
    addNameAndNickName("Allison", "Ailie");
    addNameAndNickName("Anna", "Annie");
    addNameAndNickName("Anna", "Nan");
    addNameAndNickName("Anna", "Nanny");
    addNameAndNickName("Anna", "Nina");
    addNameAndNickName("Annabella", "Annaple");
    addNameAndNickName("Anne", "Annie");
    addNameAndNickName("Anne", "Nan");
    addNameAndNickName("Anne", "Nanny");
    addNameAndNickName("Anne", "Nina");
    addNameAndNickName("Annette", "Annie");
    addNameAndNickName("Annette", "Nan");
    addNameAndNickName("Annette", "Nanny");
    addNameAndNickName("Antonina", "Nina");
    addNameAndNickName("Antony", "Tony");
    addNameAndNickName("Arabella", "Bel");
    addNameAndNickName("Arabella", "Bella");
    addNameAndNickName("Augustin", "Austin");
    addNameAndNickName("Carol", "Carrie");
    addNameAndNickName("Catherina", "Casy");
    addNameAndNickName("Catherina", "Cathie");
    addNameAndNickName("Catherina", "Kate");
    addNameAndNickName("Catherina", "Kathie");
    addNameAndNickName("Catherina", "Katie");
    addNameAndNickName("Catherina", "Katrine");
    addNameAndNickName("Catherina", "Kit");
    addNameAndNickName("Catherina", "Kitty");
    addNameAndNickName("Cecila", "Cissie");
    addNameAndNickName("Cecilia", "Cissie");
    addNameAndNickName("Cecily", "Cissie");
    addNameAndNickName("Cecily", "Cis");
    addNameAndNickName("Cecily", "Cissy");
    addNameAndNickName("Cecily", "Sis");
    addNameAndNickName("Cecily", "Sisely");
    addNameAndNickName("Christopher", "Kit");
    addNameAndNickName("Clarice", "Claire");
    addNameAndNickName("Clarice", "Clare");
    addNameAndNickName("Clarissa", "Claire");
    addNameAndNickName("Clarissa", "Clare");
    addNameAndNickName("Clemintine", "Clement");
    addNameAndNickName("Crispian", "Crispus");
    addNameAndNickName("Denys", "Denis");
    addNameAndNickName("Doris", "Dot");
    addNameAndNickName("Dorothy", "Dol");
    addNameAndNickName("Dorothy", "Dolly");
    addNameAndNickName("Dorothy", "Dora");
    addNameAndNickName("Dorothy", "Dot");
    addNameAndNickName("Edmund", "Ed");
    addNameAndNickName("Edmund", "Eddie");
    addNameAndNickName("Edmund", "Eddy");
    addNameAndNickName("Edmund", "Ned");
    addNameAndNickName("Edmund", "Neddy");
    addNameAndNickName("Edwin", "Ed");
    addNameAndNickName("Edwin", "Eddie");
    addNameAndNickName("Edwin", "Eddy");
    addNameAndNickName("Eleanor", "Norah");
    addNameAndNickName("Elinor", "Norah");
    addNameAndNickName("Elinor", "Ella");
    addNameAndNickName("Elinor", "Ellen");
    addNameAndNickName("Elinor", "Nell");
    addNameAndNickName("Elinor", "Nellie");
    addNameAndNickName("Elinor", "Nora");
    addNameAndNickName("Elisabeth", "Elsie");
    addNameAndNickName("Eliza", "Elsie");
    addNameAndNickName("Eliza", "Bess");
    addNameAndNickName("Eliza", "Bessie");
    addNameAndNickName("Eliza", "Bessy");
    addNameAndNickName("Eliza", "Beth");
    addNameAndNickName("Eliza", "Betsy");
    addNameAndNickName("Eliza", "Betty");
    addNameAndNickName("Eliza", "Libby");
    addNameAndNickName("Eliza", "Lisa");
    addNameAndNickName("Eliza", "Liz");
    addNameAndNickName("Eliza", "Liza");
    addNameAndNickName("Eliza", "Lizzie");
    addNameAndNickName("Elizabeth", "Elsie");
    addNameAndNickName("Elizabeth", "Bess");
    addNameAndNickName("Elizabeth", "Bessie");
    addNameAndNickName("Elizabeth", "Bessy");
    addNameAndNickName("Elizabeth", "Beth");
    addNameAndNickName("Elizabeth", "Betsy");
    addNameAndNickName("Elizabeth", "Betty");
    addNameAndNickName("Elizabeth", "Libby");
    addNameAndNickName("Elizabeth", "Lisa");
    addNameAndNickName("Elizabeth", "Liz");
    addNameAndNickName("Elizabeth", "Liza");
    addNameAndNickName("Elizabeth", "Lizzie");
    addNameAndNickName("Emily", "Emm");
    addNameAndNickName("Emily", "Emmie");
    addNameAndNickName("Emma", "Emm");
    addNameAndNickName("Emma", "Emmie");
    addNameAndNickName("Emmeline", "Emm");
    addNameAndNickName("Emmeline", "Emmie");
    addNameAndNickName("Eveline", "Eva");
    addNameAndNickName("Eveline", "Eve");
    addNameAndNickName("Evelyn", "Eva");
    addNameAndNickName("Evelyn", "Eve");
    addNameAndNickName("Ezekiel", "Zeke");
    addNameAndNickName("Frederica", "Frankie");
    addNameAndNickName("Frederick", "Fred");
    addNameAndNickName("Frederick", "Freddie");
    addNameAndNickName("Frederick", "Freddy");
    addNameAndNickName("Georgiana", "Georgie");
    addNameAndNickName("Georgina", "Georgie");
    addNameAndNickName("Hannah", "Annie");
    addNameAndNickName("Hannah", "Nan");
    addNameAndNickName("Hannah", "Nanny");
    addNameAndNickName("Hannah", "Nina");
    addNameAndNickName("Hannah", "Anna");
    addNameAndNickName("Hannah", "Anne");
    addNameAndNickName("Harold", "Harry");
    addNameAndNickName("Harriot", "Hatty");
    addNameAndNickName("Helen", "Norah");
    addNameAndNickName("Helen", "Nell");
    addNameAndNickName("Helen", "Ailie");
    addNameAndNickName("Helen", "Ella");
    addNameAndNickName("Helen", "Ellen");
    addNameAndNickName("Helen", "Nellie");
    addNameAndNickName("Helen", "Nora");
    addNameAndNickName("Helena", "Nell");
    addNameAndNickName("Helena", "Nellie");
    addNameAndNickName("Helena", "Lena");
    addNameAndNickName("Helene", "Nell");
    addNameAndNickName("Helene", "Nellie");
    addNameAndNickName("Helene", "Lena");
    addNameAndNickName("Hester", "Essie");
    addNameAndNickName("Hesther", "Essie");
    addNameAndNickName("Honaria", "Nora");
    addNameAndNickName("Honaria", "Norah");
    addNameAndNickName("Honora", "Nora");
    addNameAndNickName("Hugo", "Huggin");
    addNameAndNickName("Hugo", "Hughie");
    addNameAndNickName("Hugo", "Hughoc");
    addNameAndNickName("Humphry", "Humph");
    addNameAndNickName("Immanuel", "Manny");
    addNameAndNickName("Isabeau", "Bel");
    addNameAndNickName("Isabeau", "Bella");
    addNameAndNickName("Isabeau", "Isa");
    addNameAndNickName("Isabeau", "Sibella");
    addNameAndNickName("Isabeau", "Tib");
    addNameAndNickName("Isabeau", "Tibbie");
    addNameAndNickName("Isabella", "Bel");
    addNameAndNickName("Isabella", "Bella");
    addNameAndNickName("Isabella", "Isa");
    addNameAndNickName("Isabella", "Izzy");
    addNameAndNickName("Isabella", "Izzie");
    addNameAndNickName("Isabella", "Issy");
    addNameAndNickName("Isabella", "Sibella");
    addNameAndNickName("Isabella", "Tib");
    addNameAndNickName("Isabella", "Tibbie");
    addNameAndNickName("Isobel", "Bel");
    addNameAndNickName("Isobel", "Bella");
    addNameAndNickName("Isobel", "Isa");
    addNameAndNickName("Isobel", "Sibella");
    addNameAndNickName("Isobel", "Tib");
    addNameAndNickName("Isobel", "Tibbie");
    addNameAndNickName("Isobel", "Izzy");
    addNameAndNickName("Isobel", "Izzie");
    addNameAndNickName("Isobel", "Issy");
    addNameAndNickName("Izaak", "Ik");
    addNameAndNickName("Izaak", "Ike");
    addNameAndNickName("Jean", "Jean");
    addNameAndNickName("Jean", "Jeanie");
    addNameAndNickName("Jean", "Jeannie");
    addNameAndNickName("Jean", "Jen");
    addNameAndNickName("Jean", "Jennie");
    addNameAndNickName("Jean", "Jenny");
    addNameAndNickName("Jeanne", "Jean");
    addNameAndNickName("Jeanne", "Jeanie");
    addNameAndNickName("Jeanne", "Jeannie");
    addNameAndNickName("Jeanne", "Jen");
    addNameAndNickName("Jeanne", "Jennie");
    addNameAndNickName("Jeanne", "Jenny");
    addNameAndNickName("Jeannette", "Jean");
    addNameAndNickName("Jeannette", "Jeanie");
    addNameAndNickName("Jeannette", "Jeannie");
    addNameAndNickName("Jeannette", "Jen");
    addNameAndNickName("Jeannette", "Jennie");
    addNameAndNickName("Jeannette", "Jenny");
    addNameAndNickName("Jeremias", "Jeremy");
    addNameAndNickName("Joanna", "Jo");
    addNameAndNickName("Johanna", "Jo");
    addNameAndNickName("Josephine", "Jo");
    addNameAndNickName("Juliet", "Jules");
    addNameAndNickName("Juliet", "Julie");
    addNameAndNickName("Julius", "Jule");
    addNameAndNickName("Katharine", "Casy");
    addNameAndNickName("Katharine", "Cathie");
    addNameAndNickName("Katharine", "Kate");
    addNameAndNickName("Katharine", "Kathie");
    addNameAndNickName("Katharine", "Katie");
    addNameAndNickName("Katharine", "Katrine");
    addNameAndNickName("Katharine", "Kit");
    addNameAndNickName("Katharine", "Kitty");
    addNameAndNickName("Katherine", "Casy");
    addNameAndNickName("Katherine", "Cathie");
    addNameAndNickName("Katherine", "Kate");
    addNameAndNickName("Katherine", "Kathie");
    addNameAndNickName("Katherine", "Katie");
    addNameAndNickName("Katherine", "Katrine");
    addNameAndNickName("Katherine", "Kit");
    addNameAndNickName("Katherine", "Kitty");
    addNameAndNickName("Kathleen", "Cathie");
    addNameAndNickName("Kathleen", "Kate");
    addNameAndNickName("Kathleen", "Kathie");
    addNameAndNickName("Kathleen", "Katie");
    addNameAndNickName("Kathleen", "Katrine");
    addNameAndNickName("Kathleen", "Kit");
    addNameAndNickName("Kathleen", "Kitty");
    addNameAndNickName("Kathleen", "Cate");
    addNameAndNickName("Lawrence", "Larry");
    addNameAndNickName("Lawrence", "Lorrie");
    addNameAndNickName("Leonora", "Norah");
    addNameAndNickName("Leonora", "Ella");
    addNameAndNickName("Leonora", "Ellen");
    addNameAndNickName("Leonora", "Nellie");
    addNameAndNickName("Leonora", "Nora");
    addNameAndNickName("Leonora", "Nell");
    addNameAndNickName("Lettice", "Lettie");
    addNameAndNickName("Lorenzo", "Larry");
    addNameAndNickName("Lorenzo", "Lorrie");
    addNameAndNickName("Louis", "Lew");
    addNameAndNickName("Louis", "Lewie");
    addNameAndNickName("Louis", "Lou");
    addNameAndNickName("Louis", "Louie");
    addNameAndNickName("Louisa", "Lou");
    addNameAndNickName("Louisa", "Louie");
    addNameAndNickName("Louise", "Lou");
    addNameAndNickName("Louise", "Louie");
    addNameAndNickName("Lucinda", "Lucy");
    addNameAndNickName("Ludovic", "Lew");
    addNameAndNickName("Ludovic", "Lewie");
    addNameAndNickName("Ludovic", "Lou");
    addNameAndNickName("Ludovic", "Louie");
    addNameAndNickName("Madeline", "Maud");
    addNameAndNickName("Madeline", "Maddie");
    addNameAndNickName("Magdalene", "Lena");
    addNameAndNickName("Magdalene", "Maud");
    addNameAndNickName("Marcellus", "Mark");
    addNameAndNickName("Marcius", "Mark");
    addNameAndNickName("Marion", "Mamie");
    addNameAndNickName("Martha", "Mat");
    addNameAndNickName("Martha", "Pat");
    addNameAndNickName("Martha", "Matty");
    addNameAndNickName("Martha", "Patty");
    addNameAndNickName("Mathilda", "Pat");
    addNameAndNickName("Mathilda", "Mat");
    addNameAndNickName("Mathilda", "Mattie");
    addNameAndNickName("Mathilda", "Matty");
    addNameAndNickName("Mathilda", "Maud");
    addNameAndNickName("Mathilda", "Patty");
    addNameAndNickName("Mathilda", "Tilda");
    addNameAndNickName("Mathilda", "Tillie");
    addNameAndNickName("Matilda", "Pat");
    addNameAndNickName("Matthew", "Mat");
    addNameAndNickName("Matthias", "Mat");
    addNameAndNickName("Matthias", "Matt");
    addNameAndNickName("Megan", "Meg");
    addNameAndNickName("Miriam", "Mamie");
    addNameAndNickName("Miriam", "May");
    addNameAndNickName("Miriam", "Minnie");
    addNameAndNickName("Miriam", "Moll");
    addNameAndNickName("Miriam", "Molly");
    addNameAndNickName("Miriam", "Pol");
    addNameAndNickName("Miriam", "Polly");
    addNameAndNickName("Nancy", "Nan");
    addNameAndNickName("Nancy", "Nina");
    addNameAndNickName("Nathanael", "Nat");
    addNameAndNickName("Nathaniel", "Nat");
    addNameAndNickName("Nicolas", "Nic");
    addNameAndNickName("Nicolas", "Nick");
    addNameAndNickName("Octavus", "Tave");
    addNameAndNickName("Octavus", "Tavy");
    addNameAndNickName("Olive", "Ollie");
    addNameAndNickName("Olive", "Olly");
    addNameAndNickName("Olivia", "Ollie");
    addNameAndNickName("Olivia", "Olly");
    addNameAndNickName("Patricius", "Paddy");
    addNameAndNickName("Patricius", "Pat");
    addNameAndNickName("Paulinus", "Paul");
    addNameAndNickName("Phillis", "Phyl");
    addNameAndNickName("Pollyanna", "Polly");
    addNameAndNickName("Prescilla", "Sissie");
    addNameAndNickName("Raymund", "Ray");
    addNameAndNickName("Rebekah", "Beck");
    addNameAndNickName("Rebekah", "Becky");
    addNameAndNickName("Rebekah", "Bex");
    addNameAndNickName("Reynold", "Reg");
    addNameAndNickName("Reynold", "Reggie");
    addNameAndNickName("Richard", "Rick");
    addNameAndNickName("Robert", "Bertie");
    addNameAndNickName("Robert", "Bert");
    addNameAndNickName("Roderic", "Rick");
    addNameAndNickName("Roderic", "Rod");
    addNameAndNickName("Roderic", "Roddie");
    addNameAndNickName("Roderic", "Roddy");
    addNameAndNickName("Rodolphus", "Ralph");
    addNameAndNickName("Rosabel", "Rosie");
    addNameAndNickName("Rosabella", "Rosie");
    addNameAndNickName("Rosalia", "Rosie");
    addNameAndNickName("Rosalie", "Rosie");
    addNameAndNickName("Rosalind", "Rosie");
    addNameAndNickName("Rosanna", "Rosie");
    addNameAndNickName("Rudolph", "Ralph");
    addNameAndNickName("Rudolphus", "Ralph");
    addNameAndNickName("Rupert", "Bob");
    addNameAndNickName("Rupert", "Bobby");
    addNameAndNickName("Rupert", "Dob");
    addNameAndNickName("Rupert", "Dobbin");
    addNameAndNickName("Rupert", "Rob");
    addNameAndNickName("Rupert", "Robbie");
    addNameAndNickName("Rupert", "Robin");
    addNameAndNickName("Sara", "Sal");
    addNameAndNickName("Sara", "Sally");
    addNameAndNickName("Simeon", "Sim");
    addNameAndNickName("Simone", "Sim");
    addNameAndNickName("Steven", "Steenie");
    addNameAndNickName("Steven", "Steve");
    addNameAndNickName("Steven", "Stevie");
    addNameAndNickName("Susanna", "Sue");
    addNameAndNickName("Susanna", "Suke");
    addNameAndNickName("Susanna", "Suky");
    addNameAndNickName("Susanna", "Susie");
    addNameAndNickName("Susanna", "Susy");
    addNameAndNickName("Susannah", "Sue");
    addNameAndNickName("Susannah", "Suke");
    addNameAndNickName("Susannah", "Suky");
    addNameAndNickName("Susannah", "Susie");
    addNameAndNickName("Susannah", "Susy");
    addNameAndNickName("Sylvester", "Sly");
    addNameAndNickName("Sylvester", "Vest");
    addNameAndNickName("Sylvester", "Vester");
    addNameAndNickName("Theodora", "Dora");
    addNameAndNickName("Tobias", "Toby");
    addNameAndNickName("Wilhelmena", "Minnie");
    addNameAndNickName("Winfred", "Winnie");
    addNameAndNickName("Zachary", "Zac");
    addNameAndNickName("Zachary", "Zach");
    addNameAndNickName("Zechariah", "Zac");
    addNameAndNickName("Zechariah", "Zach");
    addNameAndNickName("Zechariah", "Zacky");
    addNameAndNickName("Zechariah", "Zak");
    addNameAndNickName("Abigail", "Abby");
    addNameAndNickName("Abigail", "Nabby");
    addNameAndNickName("Abraham", "Abe");
    addNameAndNickName("Abraham", "Abram");
    addNameAndNickName("Abraham", "Aby");
    addNameAndNickName("Adam", "Edie");
    addNameAndNickName("Adam", "Yiddy");
    addNameAndNickName("Aeneas", "Eneas");
    addNameAndNickName("Agnes", "Aggie");
    addNameAndNickName("Agnes", "Aggy");
    addNameAndNickName("Agnes", "Inez ");
    addNameAndNickName("Alexander", "Alec");
    addNameAndNickName("Alexander", "Aleck");
    addNameAndNickName("Alexander", "Alex");
    addNameAndNickName("Alexander", "Alick");
    addNameAndNickName("Alexander", "Eck");
    addNameAndNickName("Alexander", "Ecky");
    addNameAndNickName("Alexander", "Lex");
    addNameAndNickName("Alexander", "Lexie");
    addNameAndNickName("Alexander", "Sanders");
    addNameAndNickName("Alexander", "Sandy");
    addNameAndNickName("Alexander", "Sawnie");
    addNameAndNickName("Alfred", "Alf");
    addNameAndNickName("Alfred", "Alfie");
    addNameAndNickName("Algernon", "Algy");
    addNameAndNickName("Alice", "Ailie");
    addNameAndNickName("Alice", "Allie");
    addNameAndNickName("Alice", "Ally");
    addNameAndNickName("Alice", "Elsie");
    addNameAndNickName("Alison", "Ailie");
    addNameAndNickName("Allan", "Alan");
    addNameAndNickName("Andrew", "Andy");
    addNameAndNickName("Angelica", "Angel");
    addNameAndNickName("Ann", "Annie");
    addNameAndNickName("Ann", "Nan");
    addNameAndNickName("Ann", "Nanny");
    addNameAndNickName("Ann", "Nina");
    addNameAndNickName("Annabel", "Annaple");
    addNameAndNickName("Annette", "Anna");
    addNameAndNickName("Annette", "Anne");
    addNameAndNickName("Anthony", "Tony");
    addNameAndNickName("Antoinette", "Net");
    addNameAndNickName("Antoinette", "Netty");
    addNameAndNickName("Arabella", "Belle");
    addNameAndNickName("Archibald", "Archie");
    addNameAndNickName("Archibald", "Archy");
    addNameAndNickName("Archibald", "Baldie");
    addNameAndNickName("Augustine", "Augustin");
    addNameAndNickName("Augustine", "Austin");
    addNameAndNickName("Augustus", "Gus");
    addNameAndNickName("Augustus", "Gussie");
    addNameAndNickName("Augustus", "Gustus");
    addNameAndNickName("Barbara", "Bab");
    addNameAndNickName("Barbara", "Babbie");
    addNameAndNickName("Barbara", "Babs");
    addNameAndNickName("Bartholomew", "Bart");
    addNameAndNickName("Bartholomew", "Bat");
    addNameAndNickName("Benedict", "Bennet");
    addNameAndNickName("Benjamin", "Ben");
    addNameAndNickName("Benjamin", "Benny");
    addNameAndNickName("Bernard", "Barney");
    addNameAndNickName("Bertha", "Bertie");
    addNameAndNickName("Bertha", "Berty");
    addNameAndNickName("Bertram", "Bert");
    addNameAndNickName("Bridget", "Biddy");
    addNameAndNickName("Caitlin", "Cate");
    addNameAndNickName("Caroline", "Caddie");
    addNameAndNickName("Caroline", "Carrie");
    addNameAndNickName("Casimir", "Cassie");
    addNameAndNickName("Catherine", "Casy");
    addNameAndNickName("Catherine", "Cathie");
    addNameAndNickName("Catherine", "Kate");
    addNameAndNickName("Catherine", "Kathie");
    addNameAndNickName("Catherine", "Katie");
    addNameAndNickName("Catherine", "Katrine");
    addNameAndNickName("Catherine", "Kit");
    addNameAndNickName("Catherine", "Kitty");
    addNameAndNickName("Cecila", "Sissie");
    addNameAndNickName("Cecilia", "Cis");
    addNameAndNickName("Cecilia", "Cissy");
    addNameAndNickName("Cecilia", "Sis");
    addNameAndNickName("Cecilia", "Sisely");
    addNameAndNickName("Charles", "Charley");
    addNameAndNickName("Charles", "Chas");
    addNameAndNickName("Charles", "Charlie");
    addNameAndNickName("Charles", "Chuck");
    addNameAndNickName("Christian", "Christy");
    addNameAndNickName("Christina", "Chrissie");
    addNameAndNickName("Christina", "Christie");
    addNameAndNickName("Christina", "Teenie");
    addNameAndNickName("Christina", "Tina");
    addNameAndNickName("Christina", "Xina");
    addNameAndNickName("Christopher", "Chris");
    addNameAndNickName("Christopher", "Kester");
    addNameAndNickName("Clara", "Claire");
    addNameAndNickName("Clara", "Clare");
    addNameAndNickName("Claudius", "Claud");
    addNameAndNickName("Clementina", "Clement");
    addNameAndNickName("Constance", "Connie");
    addNameAndNickName("Cora", "Corinna");
    addNameAndNickName("Crispin", "Crispus");
    addNameAndNickName("Daniel", "Dan");
    addNameAndNickName("Daniel", "Danny");
    addNameAndNickName("David", "Dave");
    addNameAndNickName("David", "Davy");
    addNameAndNickName("Debrah", "Deb");
    addNameAndNickName("Debrah", "Debbie");
    addNameAndNickName("Debra", "Deb");
    addNameAndNickName("Debra", "Debbie");
    addNameAndNickName("Deborah", "Deb");
    addNameAndNickName("Deborah", "Debbie");
    addNameAndNickName("Dennis", "Denis");
    addNameAndNickName("Diana", "Di");
    addNameAndNickName("Diana", "Die");
    addNameAndNickName("Dorothea", "Dol");
    addNameAndNickName("Dorothea", "Dolly");
    addNameAndNickName("Dorothea", "Dora");
    addNameAndNickName("Dorothea", "Dot");
    addNameAndNickName("Edith", "Ada");
    addNameAndNickName("Edward", "Ed");
    addNameAndNickName("Edward", "Eddie");
    addNameAndNickName("Edward", "Eddy");
    addNameAndNickName("Edward", "Ned");
    addNameAndNickName("Edward", "Neddy");
    addNameAndNickName("Edward", "Ted");
    addNameAndNickName("Edward", "Teddy");
    addNameAndNickName("Eleanor", "Ella");
    addNameAndNickName("Eleanor", "Ellen");
    addNameAndNickName("Eleanor", "Nell");
    addNameAndNickName("Eleanor", "Nellie");
    addNameAndNickName("Eleanor", "Nora");
    addNameAndNickName("Elisabeth", "Bess");
    addNameAndNickName("Elisabeth", "Bessie");
    addNameAndNickName("Elisabeth", "Bessy");
    addNameAndNickName("Elisabeth", "Beth");
    addNameAndNickName("Elisabeth", "Betsy");
    addNameAndNickName("Elisabeth", "Betty");
    addNameAndNickName("Elisabeth", "Libby");
    addNameAndNickName("Elisabeth", "Lisa");
    addNameAndNickName("Elisabeth", "Liz");
    addNameAndNickName("Elisabeth", "Liza");
    addNameAndNickName("Elisabeth", "Lizzie");
    addNameAndNickName("Elspeth", "Elspie");
    addNameAndNickName("Emeline", "Emm");
    addNameAndNickName("Emeline", "Emmie");
    addNameAndNickName("Emmanuel", "Manny");
    addNameAndNickName("Esther", "Essie");
    addNameAndNickName("Eugenia", "Genie");
    addNameAndNickName("Euphemia", "Effie");
    addNameAndNickName("Euphemia", "Euphie");
    addNameAndNickName("Euphemia", "Phamie");
    addNameAndNickName("Euphemia", "Phemie");
    addNameAndNickName("Evelina", "Eva");
    addNameAndNickName("Evelina", "Eve");
    addNameAndNickName("Ezekial", "Zeke");
    addNameAndNickName("Florence", "Flo");
    addNameAndNickName("Florence", "Flossie");
    addNameAndNickName("Florence", "Floy");
    addNameAndNickName("Frances", "Fanny");
    addNameAndNickName("Francis", "Francie");
    addNameAndNickName("Francis", "Frank");
    addNameAndNickName("Francis", "Frankie");
    addNameAndNickName("Frederic", "Fred");
    addNameAndNickName("Frederic", "Freddie");
    addNameAndNickName("Frederic", "Freddy");
    addNameAndNickName("Gabriel", "Gabe");
    addNameAndNickName("George", "Dod");
    addNameAndNickName("George", "Doddy");
    addNameAndNickName("George", "Georgie");
    addNameAndNickName("Gertrude", "Gertie");
    addNameAndNickName("Gertrude", "Trudy");
    addNameAndNickName("Gilbert", "Gil");
    addNameAndNickName("Gregory", "Greg");
    addNameAndNickName("Griselda", "Grissel");
    addNameAndNickName("Hadrian", "Adrian");
    addNameAndNickName("Harriet", "Hatty");
    addNameAndNickName("Helen", "Aileen");
    addNameAndNickName("Helen", "Eileen");
    addNameAndNickName("Helen", "Lena");
    addNameAndNickName("Henrietta", "Etta");
    addNameAndNickName("Henrietta", "Hetty");
    addNameAndNickName("Henrietta", "Nettie");
    addNameAndNickName("Henry", "Hal");
    addNameAndNickName("Henry", "Harry");
    addNameAndNickName("Henry", "Hen");
    addNameAndNickName("Henry", "Henny");
    addNameAndNickName("Henry", "Hy");
    addNameAndNickName("Honora", "Norah");
    addNameAndNickName("Horatio", "Horace");
    addNameAndNickName("Hugh", "Huggin");
    addNameAndNickName("Hugh", "Hughie");
    addNameAndNickName("Hugh", "Hughoc");
    addNameAndNickName("Humphrey", "Humph");
    addNameAndNickName("Isaac", "Ik");
    addNameAndNickName("Isaac", "Ike");
    addNameAndNickName("Isabel", "Bel");
    addNameAndNickName("Isabel", "Bella");
    addNameAndNickName("Isabel", "Isa");
    addNameAndNickName("Isabel", "Izzy");
    addNameAndNickName("Isabel", "Izzie");
    addNameAndNickName("Isabel", "Issy");
    addNameAndNickName("Isabel", "Sibella");
    addNameAndNickName("Isabel", "Tib");
    addNameAndNickName("Isabel", "Tibbie");
    addNameAndNickName("Jacob", "Jake");
    addNameAndNickName("James", "Jeames");
    addNameAndNickName("James", "Jem");
    addNameAndNickName("James", "Jemmy");
    addNameAndNickName("James", "Jim");
    addNameAndNickName("James", "Jimmie");
    addNameAndNickName("James", "Jimmy");
    addNameAndNickName("Jane", "Jean");
    addNameAndNickName("Jane", "Jeanie");
    addNameAndNickName("Jane", "Jeannie");
    addNameAndNickName("Janet", "Jen");
    addNameAndNickName("Janet", "Jennie");
    addNameAndNickName("Janet", "Jenny");
    addNameAndNickName("Janet", "Jess");
    addNameAndNickName("Janet", "Jessie");
    addNameAndNickName("Jeremiah", "Jeremy");
    addNameAndNickName("Joan", "Jo");
    addNameAndNickName("John", "Jack");
    addNameAndNickName("John", "Jock");
    addNameAndNickName("John", "Johnnie");
    addNameAndNickName("John", "Johnny");
    addNameAndNickName("Joseph", "Joe");
    addNameAndNickName("Joseph", "Joey");
    addNameAndNickName("Josephine", "Josie");
    addNameAndNickName("Josephine", "Josy");
    addNameAndNickName("Josephine", "Jozy");
    addNameAndNickName("Josephine", "Pheny");
    addNameAndNickName("Joshua", "Josh");
    addNameAndNickName("Judith", "Judy");
    addNameAndNickName("Julia", "Jules");
    addNameAndNickName("Julia", "Julie");
    addNameAndNickName("Julian", "Jule");
    addNameAndNickName("Kathleen", "Kath");
    addNameAndNickName("Kenneth", "Ken");
    addNameAndNickName("Laurence", "Larry");
    addNameAndNickName("Laurence", "Lorrie");
    addNameAndNickName("Laurinda", "Laura");
    addNameAndNickName("Leonard", "Len");
    addNameAndNickName("Leonard", "Lenny");
    addNameAndNickName("Letitia", "Lettie");
    addNameAndNickName("Lewis", "Lew");
    addNameAndNickName("Lewis", "Lewie");
    addNameAndNickName("Lewis", "Lou");
    addNameAndNickName("Lewis", "Louie");
    addNameAndNickName("Lilian", "Lilly");
    addNameAndNickName("Lilian", "Lily");
    addNameAndNickName("Lucas", "Luke");
    addNameAndNickName("Lucia", "Lucy");
    addNameAndNickName("Lucinda", "Cindy");
    addNameAndNickName("Madeline", "Maudlin");
    addNameAndNickName("Madison", "Maddie");
    addNameAndNickName("Marcus", "Mark");
    addNameAndNickName("Margaret", "Gritty");
    addNameAndNickName("Margaret", "Madge");
    addNameAndNickName("Margaret", "Mag");
    addNameAndNickName("Margaret", "Maggie");
    addNameAndNickName("Margaret", "Margery");
    addNameAndNickName("Margaret", "Margie");
    addNameAndNickName("Margaret", "Marjorie");
    addNameAndNickName("Margaret", "Marjory");
    addNameAndNickName("Margaret", "Meg");
    addNameAndNickName("Margaret", "Meggy");
    addNameAndNickName("Margaret", "Meta");
    addNameAndNickName("Margaret", "Peg");
    addNameAndNickName("Margaret", "Peggy");
    addNameAndNickName("Mary", "Mamie");
    addNameAndNickName("Mary", "May");
    addNameAndNickName("Mary", "Minnie");
    addNameAndNickName("Mary", "Moll");
    addNameAndNickName("Mary", "Molly");
    addNameAndNickName("Mary", "Pol");
    addNameAndNickName("Mary", "Polly");
    addNameAndNickName("Matilda", "Mat");
    addNameAndNickName("Matilda", "Mattie");
    addNameAndNickName("Matilda", "Matty");
    addNameAndNickName("Matilda", "Maud");
    addNameAndNickName("Matilda", "Patty");
    addNameAndNickName("Matilda", "Tilda");
    addNameAndNickName("Matilda", "Tillie");
    addNameAndNickName("Matthew", "Matt");
    addNameAndNickName("Michael", "Mick");
    addNameAndNickName("Michael", "Micky");
    addNameAndNickName("Michael", "Mike");
    addNameAndNickName("Moses", "Mose");
    addNameAndNickName("Moses", "Mosey");
    addNameAndNickName("Myphanwy", "Mif");
    addNameAndNickName("Myphanwy", "Miffy");
    addNameAndNickName("Nancy", "Nance");
    addNameAndNickName("Nathan", "Nat");
    addNameAndNickName("Nicholas", "Nic");
    addNameAndNickName("Nicholas", "Nick");
    addNameAndNickName("Octavius", "Tave");
    addNameAndNickName("Octavius", "Tavy");
    addNameAndNickName("Oliver", "Nol");
    addNameAndNickName("Oliver", "Nolly");
    addNameAndNickName("Oliver", "Ollie");
    addNameAndNickName("Oliver", "Olly");
    addNameAndNickName("Patrick", "Paddy");
    addNameAndNickName("Patrick", "Pat");
    addNameAndNickName("Paulus", "Paul");
    addNameAndNickName("Penelope", "Penny");
    addNameAndNickName("Peter", "Pete");
    addNameAndNickName("Peter", "Peterkin");
    addNameAndNickName("Philip", "Phil");
    addNameAndNickName("Philip", "Pip");
    addNameAndNickName("Philippa", "Pippa");
    addNameAndNickName("Phyllis", "Phyl");
    addNameAndNickName("Prescilla", "Cissie");
    addNameAndNickName("Prudence", "Prudy");
    addNameAndNickName("Prudence", "Prue");
    addNameAndNickName("Raymond", "Ray");
    addNameAndNickName("Rebecca", "Beck");
    addNameAndNickName("Rebecca", "Becky");
    addNameAndNickName("Rebecca", "Bex");
    addNameAndNickName("Reginald", "Reg");
    addNameAndNickName("Reginald", "Reggie");
    addNameAndNickName("Richard", "Dick");
    addNameAndNickName("Richard", "Dicken");
    addNameAndNickName("Richard", "Dickie");
    addNameAndNickName("Richard", "Dickon");
    addNameAndNickName("Richard", "Dicky");
    addNameAndNickName("Robert", "Bob");
    addNameAndNickName("Robert", "Bobby");
    addNameAndNickName("Robert", "Dob");
    addNameAndNickName("Robert", "Dobbin");
    addNameAndNickName("Robert", "Rob");
    addNameAndNickName("Robert", "Robbie");
    addNameAndNickName("Robert", "Robin");
    addNameAndNickName("Roderick", "Rick");
    addNameAndNickName("Roderick", "Rod");
    addNameAndNickName("Roderick", "Roddie");
    addNameAndNickName("Roderick", "Roddy");
    addNameAndNickName("Rodolph", "Ralph");
    addNameAndNickName("Roger", "Hodge");
    addNameAndNickName("Roger", "Hodgekin");
    addNameAndNickName("Roger", "Rodge");
    addNameAndNickName("Ronald", "Ron");
    addNameAndNickName("Rosa", "Rosie");
    addNameAndNickName("Roxana", "Roxy");
    addNameAndNickName("Samuel", "Sam");
    addNameAndNickName("Samuel", "Sammy");
    addNameAndNickName("Sarah", "Sal");
    addNameAndNickName("Sarah", "Sally");
    addNameAndNickName("Sebastian", "Seb");
    addNameAndNickName("Silvester", "Sly");
    addNameAndNickName("Silvester", "Vest");
    addNameAndNickName("Silvester", "Vester");
    addNameAndNickName("Simon", "Sim");
    addNameAndNickName("Solomon", "Sol");
    addNameAndNickName("Sophia", "Sophie");
    addNameAndNickName("Sophia", "Sophy");
    addNameAndNickName("Stephen", "Steenie");
    addNameAndNickName("Stephen", "Steve");
    addNameAndNickName("Stephen", "Stevie");
    addNameAndNickName("Susan", "Sue");
    addNameAndNickName("Susan", "Suke");
    addNameAndNickName("Susan", "Suky");
    addNameAndNickName("Susan", "Susie");
    addNameAndNickName("Susan", "Susy");
    addNameAndNickName("Tamara", "Tammy");
    addNameAndNickName("Theresa", "Terry");
    addNameAndNickName("Theresa", "Tracie");
    addNameAndNickName("Thomas", "Tam");
    addNameAndNickName("Thomas", "Tammie");
    addNameAndNickName("Thomas", "Tom");
    addNameAndNickName("Thomas", "Tommy");
    addNameAndNickName("Timothy", "Tim");
    addNameAndNickName("Tobiah", "Toby");
    addNameAndNickName("Victor", "Vic");
    addNameAndNickName("Victoria", "Vicky");
    addNameAndNickName("Vincent", "Vinty");
    addNameAndNickName("Walter", "Wat");
    addNameAndNickName("Walter", "Watty");
    addNameAndNickName("Wilhelmena", "Mina");
    addNameAndNickName("Wilhelmena", "Minella");
    addNameAndNickName("Wilhelmena", "Wilmett");
    addNameAndNickName("Wilhelmena", "Wilmot");
    addNameAndNickName("William", "Bill");
    addNameAndNickName("William", "Billie");
    addNameAndNickName("William", "Billy");
    addNameAndNickName("William", "Will");
    addNameAndNickName("William", "Willie");
    addNameAndNickName("William", "Willy");
    addNameAndNickName("Winifred", "Winnie");
    addNameAndNickName("Zachariah", "Zac");
    addNameAndNickName("Zachariah", "Zach");
    addNameAndNickName("Zachariah", "Zacky");
    addNameAndNickName("Zachariah", "Zak");
  }

  private Transformer transformer;
  public void setTransformer(Transformer transformer) {
    this.transformer = transformer;
  }



}