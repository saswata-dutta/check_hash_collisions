package check_hash;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import com.google.common.collect.ImmutableMap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class TestKryo {
    public static void main(String[] args) {
        Kryo kryo = new Kryo();
        kryo.register(ImmutableMap.class, new JavaSerializer());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        kryo.writeClassAndObject(output,
                ImmutableMap.of("a", "b"));
        output.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        Input input = new Input(bais);
        kryo.readClassAndObject(input);
        input.close();
    }
}
