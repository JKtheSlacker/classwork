// Default Mapper class
class Mapper {
  public void map(String key, String value){
    String[] words = key.split(" ");
    for (int i = 0; i < words.length; i++){
      Wmr.emit(words[i], "1");
    }
  }
}

// Default Reducer class
class Reducer {
  public void reduce(String key, WmrIterator iter){
    int sum = 0;
    for (String value : iter){
      sum += Integer.parseInt(value);
    }
    Wmr.emit(key, Integer.valueOf(sum).toString());
  } 
}

// Modified to strip all punctuation except []
// [] causes the regex to fail without any
// apparent or mildly obscure resolution
class Mapper {
  public void map(String key, String value){
    String[] words = key.split(" |[!\"#$%&\'()*+,-./:;<=>?@\\^_`{|}~]");
    for (int i = 0; i < words.length; i++){
      Wmr.emit(words[i], "1");
    }
  }
}

// Modified to take the output of the word counter
// and produce a list of words ordered
// by frequency
class Mapper {
  public void map(String key, String value){
    String[] words = key.split(" |[!\"#$%&\'()*+,-./:;<=>?@\\^_`{|}~]");
    for (int i = 0; i < words.length-1; i+=2){
      Wmr.emit(words[i], words[i+1]);
    }
  }
}

// Reducer for the frequency listing mapper
class Reducer {
  public void reduce(String key, WmrIterator iter){
    int keyValue = 0;
    for (String value : iter){
      keyValue = Integer.parseInt(value);
    }
    Wmr.emit(Integer.valueOf(keyValue).toString(), key);
  }
}