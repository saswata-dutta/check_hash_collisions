package check_hash;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExtractorChainApp {

    public static Function<Map<String, String>, String> keyExtractor(final String key) {
        return (Map<String, String> items) -> items.getOrDefault(key, "");
    }

    public static Function<String, String> prefix(final int len) {
        return str -> str.substring(0, len);
    }

    public static Function<String, String> suffix(final int len) {
        return str -> str.substring(str.length() - len);
    }

    public static class MetricDimExt {
        public final List<Function<Map<String, String>, String>> extractors;

        public MetricDimExt(List<Function<Map<String, String>, String>> extractors) {
            this.extractors = extractors;
        }

        public List<String> extract(final Map<String, String> items) {
            return extractors.stream().map(e -> e.apply(items)).collect(Collectors.toList());
        }
    }

    public static List<List<String>> extract(Map<String, String> items, Stream<MetricDimExt> metricDimExts) {
        return metricDimExts.map(m -> m.extract(items)).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        Stream<MetricDimExt> metricDimExts = Stream.of(
                Collections.singletonList(keyExtractor("k1")),
                Arrays.asList(keyExtractor("k1"), keyExtractor("k2")),
                Arrays.asList(
                        keyExtractor("k1").andThen(prefix(3)),
                        keyExtractor("k1").andThen(suffix(5)),
                        keyExtractor("k1").andThen(suffix(5)).andThen(prefix(2))),
                Collections.singletonList(keyExtractor("fake"))).map(MetricDimExt::new);

        Map<String, String> items = new HashMap<>();
        items.put("k1", "123456789");
        items.put("k2", "123456789");

        System.out.println(extract(items, metricDimExts));
    }
}
