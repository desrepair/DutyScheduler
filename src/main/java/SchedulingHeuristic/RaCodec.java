/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class RaCodec implements Codec<Ra> {
    private final CodecRegistry codecRegistry;
    
    public RaCodec(final CodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
    }

    @Override
    public void encode(BsonWriter writer, Ra t, EncoderContext ec) {
        writer.writeStartDocument();
            Codec<LocalDate> dateCodec = codecRegistry.get(LocalDate.class);
            writer.writeName("name");
            writer.writeString(t.getName());
            writer.writeName("pointsTaken");
            writer.writeDouble(t.getPointsTaken());
            writer.writeName("previousDutyDay");
            ec.encodeWithChildContext(dateCodec, writer, t.getPreviousDutyDay());

            //Write Blackout Date Array
            writer.writeName("blackoutDates");
            writer.writeStartArray();
                for (LocalDate blackoutDate : t.getBlackoutDates()) {
                    ec.encodeWithChildContext(dateCodec, writer, blackoutDate);
                }
            writer.writeEndArray();

            //Write Duty Day Array
            writer.writeName("dutyDays");
            writer.writeStartArray();
                for (LocalDate dutyDay : t.getDutyDays()) {
                    ec.encodeWithChildContext(dateCodec, writer, dutyDay);
                }
            writer.writeEndArray();
        writer.writeEndDocument();
    }

    @Override
    public Class<Ra> getEncoderClass() {
        return Ra.class;
    }

    @Override
    public Ra decode(BsonReader reader, DecoderContext dc) {
        reader.readStartDocument();
            reader.readName();
            String name = reader.readString();
            reader.readName();
            Double pointsTaken = reader.readDouble();
            reader.readName();
            Codec<LocalDate> dateCodec = codecRegistry.get(LocalDate.class);
            LocalDate previousDutyDay = dateCodec.decode(reader, dc);

            //Read Blackout Date Array
            reader.readName();
            ArrayList<LocalDate> blackoutDates = new ArrayList<>();
            reader.readStartArray();
                while(reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
                    blackoutDates.add(dateCodec.decode(reader, dc));
                }
            reader.readEndArray();

            //Read Duty Date Array
            reader.readName();
            ArrayList<LocalDate> dutyDays = new ArrayList<>();
            reader.readStartArray();
                while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
                    dutyDays.add(dateCodec.decode(reader, dc));
                }
            reader.readEndArray();
        reader.readEndDocument();
        
        return new Ra(name, pointsTaken, previousDutyDay, blackoutDates, dutyDays);
    }
}
