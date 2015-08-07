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
