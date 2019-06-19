import syntaxtree.*;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.ArrayList;

import javafx.util.Pair;
import java.io.*;

class Main {
    public static void main (String [] args) throws RuntimeException{
        boolean error = false, displayOffsets = false;
        FileInputStream fis = null;

        for(String arg: args){
            if(arg.equals("--offsets"))
                displayOffsets = true;
        }

        /* for each file path given from the cmd */
        for(String arg : args){
            if(arg.equals("--offsets"))
                continue;

            /* try and: open, parse and visit the syntax tree of the program */
            try{

                fis = new FileInputStream(arg);
                MiniJavaParser parser = new MiniJavaParser(fis);
                System.out.println("\033[1m" + arg + "\033[0m\nParsing:\u001B[32m\033[1m Successful \u001B[0m");

                /* first, traverse the tree once, to create the lookup symbol table and report minor errors*/
                FirstVisitor v0 = new FirstVisitor();
                Goal root = parser.Goal();
                root.accept(v0, null);
                System.out.println("First Pass Semantic Check: \u001B[32m\033[1mSuccessful \u001B[0m");

                /* then traverse it one more time, to type check */
                SecondVisitor v1 = new SecondVisitor(v0.classes);
                root.accept(v1);

                System.out.println("Second Pass Semantic Check: \u001B[32m\033[1mSuccessful \u001B[0m");
                System.out.println("Semantic Check: \u001B[32m\033[1m Successful \u001B[0m");

                if(displayOffsets){
                    System.out.println("Offsets\n-------");

                    /* For each class */
                    for (Map.Entry<String, ClassData> entry : v0.classes.entrySet()) {
                        String name = entry.getKey();
                        System.out.println("Class: " + name);

                        /* For each variable of the class, print offset */
                        System.out.println("\n\tFields\n\t------\n");
                        for(Map.Entry<String, Pair<String, Integer>> var : entry.getValue().vars.entrySet())
                            System.out.println("\t\t" + name + "." + var.getKey() + ": " + var.getValue().getValue());

                        /* For each pointer to a member method, print offset */
                        System.out.println("\n\tMethods\n\t-------\n");
                        for(Map.Entry<String, Triplet<String, Integer, ArrayList<String>>> func : entry.getValue().methods.entrySet())
                            System.out.println("\t\t" + name + "." + func.getKey() + ": " + func.getValue().getSecond());                       
                    }   
                }
            }
            /* handle exceptions */
            catch(SemError e){
                System.out.println(e.getMessage());
                error = true;
            }
            catch(ParseException ex){
                System.out.println(ex.getMessage());
            }
            catch(FileNotFoundException ex){
                    System.err.println(ex.getMessage());
            }

            /* clean things up */
            finally{
                try{
                    if(fis != null) fis.close();
                }
                catch(IOException ex){
                    System.err.println(ex.getMessage());
                }
            }

        }
        if(!displayOffsets)
            System.out.println("To view field and method offsets for each class rerun with --offsets");
    }
}