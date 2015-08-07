/*
 * Duty Scheduler: A scheduler for point based duty.
 * Copyright (c) 2015 Khetthai Laksanakorn
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * For source code and contact information for Khetthai Laksanakorn,
 * see <http://www.github.com/desrepair/DutyScheduler>.
 *
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
