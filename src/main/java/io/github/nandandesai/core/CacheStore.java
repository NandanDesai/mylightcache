package io.github.nandandesai.core;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public class CacheStore<T> {

    private LRUMap<String, T> lruMap=null;
    private String storeName;
    private int size;
    private String cacheStoragePath="./.cachestore";
    private XStream xstream;
    public CacheStore(String storeName, int size) throws IOException {
        if(!Files.isDirectory(Paths.get(cacheStoragePath))) {
            Files.createDirectories(Paths.get(cacheStoragePath));
        }
        this.storeName=storeName;
        this.size=size;
        this.xstream=new XStream(new StaxDriver());
        xstream.allowTypesByRegExp(new String[] { ".*" });
    }

    public void openNewStore(){
        lruMap=new LRUMap<>(size);
    }

    public void loadExistingStore(){
        //load from file
        lruMap=(LRUMap<String, T>)xstream.fromXML(new File(cacheStoragePath+"/"+storeName));
    }


    public void close() throws IOException{
        String data=xstream.toXML(lruMap);
        Path path = Paths.get(cacheStoragePath+"/"+storeName);
        byte[] strToBytes = data.getBytes();

        Files.write(path, strToBytes);
        //write the LRUMap to the disk
    }

    public T get(String key){
        return lruMap.get(key);
    }

    public void put(String key, T value){
        lruMap.put(key, value);
    }

    public void delete(String key){
        lruMap.remove(key);
    }
}

class LRUMap<K, V> extends LinkedHashMap<K, V> {
    private int size;

    LRUMap(int size) {
        super(size, 0.75f, true);
        this.size = size;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > size;
    }

}
