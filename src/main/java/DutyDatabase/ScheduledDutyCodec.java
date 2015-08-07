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

package DutyDatabase;

import SchedulingHeuristic.DutyBlock;
import java.util.ArrayList;
import org.bson.BsonReader;
import org.bson.BsonString;
import org.bson.BsonType;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;

/**
 *
 * @author desrepair
 */
public class ScheduledDutyCodec implements CollectibleCodec<ScheduledDuty> {
    private CodecRegistry registry;
    
    public ScheduledDutyCodec(CodecRegistry cr) {
        registry = cr;
    }

    @Override
    public void encode(BsonWriter writer, ScheduledDuty t, EncoderContext ec) {
        writer.writeStartDocument();
            writer.writeName("_id");
            writer.writeObjectId(t.getId());
            writer.writeName("name");
            writer.writeString(t.getName());
            writer.writeName("dutyCalendar");
            writer.writeStartArray();
            for (DutyBlock block : t.getDutyCalendar()) {
                ec.encodeWithChildContext(registry.get(DutyBlock.class), writer, block);
            }
            writer.writeEndArray();
        writer.writeEndDocument();
    }

    @Override
    public Class<ScheduledDuty> getEncoderClass() {
        return ScheduledDuty.class;
    }

    @Override
    public ScheduledDuty decode(BsonReader reader, DecoderContext dc) {
        reader.readStartDocument();
            reader.readName();
            ObjectId _id = reader.readObjectId();
            reader.readName();
            String name = reader.readString();
            reader.readName();
            ArrayList<DutyBlock> block = new ArrayList<>();
            Codec<DutyBlock> blockCodec = registry.get(DutyBlock.class);
            reader.readStartArray();
                while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
                    block.add(blockCodec.decode(reader, dc));
                }
            reader.readEndArray();
        reader.readEndDocument();
        return new ScheduledDuty(_id, name, block);
    }

    @Override
    public ScheduledDuty generateIdIfAbsentFromDocument(ScheduledDuty t) {
        return documentHasId(t) ? t : t.withNewObjectId();
    }

    @Override
    public boolean documentHasId(ScheduledDuty t) {
        return t.getId() != null;
    }

    @Override
    public BsonValue getDocumentId(ScheduledDuty t) {
        if (!documentHasId(t)) {
            throw new IllegalStateException();
        }
        return new BsonString(t.getId().toHexString());
    }
    
}
