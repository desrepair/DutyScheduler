package DutyDatabase;


import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author desrepair
 */
public class ScheduledDutyCodecProvider implements CodecProvider {

    @Override
    public <T> Codec<T> get(Class<T> type, CodecRegistry cr) {
        if (type == ScheduledDuty.class) {
            return (Codec<T>) new ScheduledDutyCodec(cr);
        }
        return null;
    }
    
}
