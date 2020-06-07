package com.company;

import java.io.*;

public class SaveObj {
    public void Serialization(){
        File file = new File("Obj.data");
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectStream = null;
        try{
            fileOutputStream = new FileOutputStream(file);
            objectStream = new ObjectOutputStream(fileOutputStream);
            for(card_index card:MySingleton.pack.Array){
                if(card instanceof Person)
                    objectStream.writeObject((Person) card);
                else
                    objectStream.writeObject((law) card);
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        finally{
            try{
                objectStream.close();
                fileOutputStream.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public void Deserialization(){
        FileInputStream fileInputStream = null;
        ObjectInputStream objectStream = null;
        try{
            fileInputStream = new FileInputStream("Obj.data");
            objectStream = new ObjectInputStream(fileInputStream);
            MySingleton.pack.Array.removeAllElements();
            try{
                for(int i = 0;;i++){
                    MySingleton.pack.Array.add((card_index) objectStream.readObject());
                    MySingleton.pack.Tree.put(MySingleton.pack.Array.get(i).id,MySingleton.pack.Array.size()-1);
                    MySingleton.pack.hash.put(MySingleton.pack.Array.get(i).id,Integer.toUnsignedLong(MySingleton.pack.Array.get(i).timespawn));

                }
            }catch (ClassNotFoundException e){
                e.printStackTrace();
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally{
            try{
                objectStream.close();
                fileInputStream.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

}
