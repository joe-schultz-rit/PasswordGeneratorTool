
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Scanner;

/**
 * Password Generator
 *
 * @author Joseph Schultz
 */
public class PasswordGenerator extends Observable{
    /** File to write passwords to */
    private File toFile;
    /** Start range of generator */
    private int start;
    /** End range of generator */
    private int end;
    /** List of characters to use */
    private ArrayList chars;

    public PasswordGenerator(){

    }

    public void init(File toFile, File charDictionary, int start, int end){
        this.toFile = toFile;
        this.start = start;
        this.end = end;
        this.chars = genCharList(charDictionary);
        System.out.println("\f");
    }

    private ArrayList genCharList(File charDictionary) {
        ArrayList charList = new ArrayList();
        try (Scanner in = new Scanner(charDictionary)){
            while(in.hasNext()){
                String currLine = in.nextLine();
                charList.add(currLine);
            }
        } catch (IOException e) {
            System.out.println("Character dictionary file not valid!");
        }
        System.out.println(charList);
        return charList;
    }

    public void start(){
        try(PrintWriter writer = new PrintWriter(new FileOutputStream(toFile,true ))){
            System.out.println("Starting Generation!");
            this.run(writer, 1);
        } catch (IOException e){
            System.out.println("Write file not valid!");
        }
    }

    /**
     * Recursively
     * @param writer
     */
    private void run(PrintWriter writer, int currLength){
        try{
            PrintWriter tempWriter1 = new PrintWriter("temp" + currLength + ".txt", "UTF-8");
            try(PrintWriter tempWriter = new PrintWriter(new FileOutputStream(new File("temp" + currLength + ".txt")))) {
                System.out.println("Current Length: " + currLength);
                if (currLength == 1) {
                    for (int i = 0; i < chars.size(); i++) {
                        tempWriter.println(chars.get(i));
                        if(start == 1){
                            writer.println(chars.get(i));
                        }
                    }
                } else {
                    String lastWord = "";
                    //ArrayList nextPasswords = new ArrayList();
                    for (int i = 0; i < chars.size(); i++) {
                        try {
                            Scanner prev = new Scanner(new File("temp" + (currLength - 1) + ".txt"));
                            while (prev.hasNextLine()) {
                                String line = prev.nextLine();
                                String password = (String) chars.get(i);
                                //password += prevPasswords.get(j);
                                password += line;
                                if(currLength >= start){
                                    writer.println(password);
                                }
                                tempWriter.println(password);
                                lastWord = password;
                            }
                        } catch (FileNotFoundException e) {

                        }

                    }
                    int count = 0;
                    String[] lastPass = lastWord.split("");
                    for (int i = 0; i < lastPass.length; i++) {
                        if (lastPass[i].equals(chars.get(chars.size() - 1))) {
                            count += 1;
                        }
                    }
                    if (count == end) {

                        return;
                    }
                }
                tempWriter.close();

            }
            tempWriter1.close();
//            if(currLength > 1) {
//                Path path = Paths.get("temp" + (currLength - 1) + ".txt");
//                try {
//                    Files.delete(path);
//                } catch (NoSuchFileException x) {
//                    System.err.format("%s: no such" + " file or directory%n", path);
//                } catch (DirectoryNotEmptyException x) {
//                    System.err.format("%s not empty%n", path);
//                } catch (IOException x) {
//                    // File permission problems are caught here.
//                    System.err.println(x);
//                }
//            }
            run(writer, currLength + 1);
        } catch (IOException e) {
            // do something
        }
    }

    public static void main(String[] args){
        if(args.length != 4){
            System.out.println("Usage: java PasswordGenerator [Password Dictionary File] [Character Dictionary File] [startLength] [endLength]");
            return;
        }
        PasswordGenerator pwdGen = new PasswordGenerator();
        pwdGen.init(new File(args[0]), new File(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        pwdGen.start();
    }


}
