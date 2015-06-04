
package SchedulingHeuristic;

import java.time.LocalDate;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

/**
 *
 * @author desrepair
 */
public class LocalDateCodec implements Codec<LocalDate> {

    @Override
    public void encode(BsonWriter writer, LocalDate t, EncoderContext ec) {
        writer.writeString(t.toString());
    }

    @Override
    public Class<LocalDate> getEncoderClass() {
        return LocalDate.class;
    }

    @Override
    public LocalDate decode(BsonReader reader, DecoderContext dc) {
        String date = reader.readString();
        return LocalDate.parse(date);
    }
}
