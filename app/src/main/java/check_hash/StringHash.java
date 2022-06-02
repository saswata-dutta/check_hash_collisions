package check_hash;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class StringHash {
    public static void main(String[] args) {
        String location = args[0];
        String prefix = "14";
        Map<Integer, Integer> counter = new HashMap<>();
        HashFunction hf = Hashing.crc32();

        try (Stream<String> in = Files.lines(Paths.get(location))) {
            in.forEach(line -> process(line.trim(), prefix, counter, hf));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Map.Entry<Integer, Integer> kv : counter.entrySet()) {
            if (kv.getValue() > 1) {
                System.out.println(kv.getKey() + "=" + kv.getValue());
            }
        }
        System.out.println("Fin");
    }

    private static void process(String line,
                                String prefix,
                                Map<Integer, Integer> counter,
                                HashFunction hf) {
        int hash = Math.abs(hf.newHasher().putString(line, Charsets.UTF_8).hash().asInt());

        int len = 9 - prefix.length();
        String keyStr = prefix +
                Strings.padStart(String.valueOf(hash), len, '0').
                        substring(0, len);
        int key = Integer.parseInt(keyStr);
        counter.merge(key, 1, Integer::sum);
    }
}
