
package SchedulingHeuristic;

import java.time.LocalDate;
import java.util.ArrayList;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;


/**
 *
 * @author desrepair
 */
public class DutyBlockCodec implements Codec<DutyBlock> {
    private final CodecRegistry codecRegistry;
    
    public DutyBlockCodec(final CodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
    }

    @Override
    public void encode(BsonWriter writer, DutyBlock t, EncoderContext ec) {
        writer.writeStartDocument();
            Codec dateCodec = codecRegistry.get(LocalDate.class);
            writer.writeName("startDate");
            ec.encodeWithChildContext(dateCodec, writer, t.getStartDate());
            writer.writeName("endDate");
            ec.encodeWithChildContext(dateCodec, writer, t.getEndDate());
            writer.writeName("blockLength");
            writer.writeInt32(t.getBlockLength());
            writer.writeName("pointValue");
            writer.writeDouble(t.getPointValue());

            //Writing ArrayList of RAs
            writer.writeName("assigned");
            writer.writeStartArray();
                for (Ra ra : t.getRasOnDuty()) {
                    Codec raCodec = codecRegistry.get(Ra.class);
                    ec.encodeWithChildContext(raCodec, writer, ra);
                }
            writer.writeEndArray();
        writer.writeEndDocument();
    }

    @Override
    public Class<DutyBlock> getEncoderClass() {
        return DutyBlock.class;
    }

    @Override
    public DutyBlock decode(BsonReader reader, DecoderContext dc) {
        reader.readStartDocument();
            Codec<LocalDate> dateCodec = codecRegistry.get(LocalDate.class);
            reader.readName();
            LocalDate startDate = dateCodec.decode(reader, dc);
            reader.readName();
            LocalDate endDate = dateCodec.decode(reader, dc);
            reader.readName();
            int blockLength = reader.readInt32();
            reader.readName();
            double pointValue = reader.readDouble();

            //Reading ArrayList of RAs
            reader.readName();
            Codec<Ra> raCodec = codecRegistry.get(Ra.class);
            ArrayList<Ra> rasOnDuty = new ArrayList<>();
            reader.readStartArray();
                while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
                    rasOnDuty.add(raCodec.decode(reader, dc));
                }
            reader.readEndArray();
        reader.readEndDocument();
        
        return new DutyBlock(startDate, endDate, blockLength, pointValue, rasOnDuty);
    }
    
}
