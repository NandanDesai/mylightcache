import io.github.nandandesai.core.CacheStore;

public class test {

    public static void main(String[] args){
        try {
            CacheStore<String> cacheStore = new CacheStore<String>("stringCache", 3);
            cacheStore.loadExistingStore();
            cacheStore.put("1", "nandan");

            System.out.println(cacheStore.get("1"));
            cacheStore.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
